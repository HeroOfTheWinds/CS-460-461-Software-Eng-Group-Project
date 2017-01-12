using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    //server for running battles
    public class BattleServer
    {
        //size of updates in bytes
        public const int UPDATE_SIZE = 41;
        //port to listen on
        public const int BATTLE_PORT = 7004;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.42.42.153");

        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();

        //stores waiting connections until second player connects
        private static Dictionary<Guid, WaitConnection> waiting = new Dictionary<Guid, WaitConnection>();

        private static readonly object DICT_LOCK = new object();


        public static void startServer()
        {

            //might have to change code to get ip address
            //IPAddress ipAddress = Dns.GetHostEntry("localhost").AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(IP, BATTLE_PORT);

            //create tcp listener
            Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            try
            {
                //bind the socket to the endpoint and listen for connections
                listener.Bind(localEndPoint);
                listener.Listen(100);
                //connect to clients forever
                while (true)
                {
                    //reset connection signal
                    connectionFound.Reset();

                    //asynchronously get connections
                    listener.BeginAccept(new AsyncCallback(connect), listener);

                    //wait for connection to be made
                    connectionFound.Wait();
                }

            }
            //catch connection errors
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            Console.WriteLine("\nServer Closed...");
            Console.Read();

        }

        //async method for connecting to clients
        public static void connect(IAsyncResult ar)
        {
            try
            {
                //connection finished, allow others to connect
                connectionFound.Set();

                Console.WriteLine("\nPlayer connected");
                //hold data about the incoming request
                WaitConnection wait;
                byte[] guid = new byte[16];
                Guid battleGUID;
                //tell player which spawn to use
                byte[] spawn = new byte[1];
                //stores items for communications
                State state = new State();

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                //read the battle guid from the client
                handler.Receive(guid, 16, 0);
                battleGUID = new Guid(guid);

                bool inDict;

                //get exclusive access to waiting
                lock (DICT_LOCK)
                {
                    //check if battle guid is already in disctionary, and save the value to wait if it is
                    //that is, check if this is the first client to connect to the battle
                    inDict = waiting.TryGetValue(battleGUID, out wait);
                    //if this is the first player, add the guid to the dictionary with a waitconnection object holding this client's socket
                    if (!inDict)
                    {
                        wait = new WaitConnection(handler);
                        wait.WriteLock = new object();
                        waiting.Add(battleGUID, wait);
                    }
                }
                //if this is the second client to connect
                if (inDict)
                {
                    //set second socket to this client's socket
                    wait.Socket2 = handler;
                    //set this client's opponent to the first client in wait
                    state.OpponentSocket = wait.Socket1;
                    state.WriteLock = wait.WriteLock;
                    //set event indicating both clients are connected
                    wait.setWait();
                    //indicate to client to spawn at second location
                    spawn[0] = 1;
                }
                else
                {
                    //add some sort of timeout?
                    //wait until second client connects
                    wait.Wait.Wait();
                    //pass lock to both clients for writing to each others socket
                    state.WriteLock = wait.WriteLock;
                    //set this client's opponent to the second client in wait
                    state.OpponentSocket = wait.Socket2;
                    //both clients are connected, remove from waiting
                    //maybe move this later so players can reconnect
                    waiting.Remove(battleGUID);
                    //indicate to client to spawn at first location;
                    spawn[0] = 0;
                }

                //set client's socket in state object
                state.ClientSocket = handler;
                //initialize buffer
                state.Update = new byte[UPDATE_SIZE];

                //lock write operations on the socket
                lock(state.WriteLock)
                {
                    //tell the client where to spawn
                    handler.Send(spawn, 1, 0);
                }
                
                //start receiving client updates
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch(Exception)
            {
                Console.WriteLine("\nPlayer disconnected");
            }
        }

        //read updates from clients
        public static void readUpdate(IAsyncResult ar)
        {
            Socket handler = null;
            try
            {
                bool battleEnd;
                bool win;

                //STORE HP AND LOCATIONS IN STATE, UPDATE AND USE FOR THINGS (anti-cheat stuff)

                //retreive the state object and socket
                State state = (State)ar.AsyncState;
                handler = state.ClientSocket;

                //NEED TO DEAL WITH SIZES AND STUFF, MAYBE SEND A MESSAGE WITH THE SIZE
                //for now everything sent as 41 bytes
                ar.AsyncWaitHandle.WaitOne();
                handler.EndReceive(ar);


                byte flags = state.Update[0];

                //determine winner and if battle ended, change so can't cheat
                battleEnd = (flags & 1) == 1 ? true : false;
                win = ((flags >> 1) & 1) == 1 ? true : false;

                //lock write operations and pass along and acknowledge update
                lock(state.WriteLock)
                {
                    send(state);
                }
                
                //recursively read updates until the battle is over
                if (!battleEnd)
                {
                    handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
                }
                else
                {
                    //handle winner stuff (DB stuff, etc)

                    //clean up
                    handler.Shutdown(SocketShutdown.Both);
                    handler.Close();
                    Console.WriteLine("\nPlayer disconnected");
                }
            }
            //end communications gracefully if player disconnects before battle ends
            catch (Exception)
            {
                Console.WriteLine("\nPlayer disconnected");
                
            }
        }

        //send updates and acknowledgements
        private static void send(State state)
        {
            //indicate this is an update for the opponent
            byte[] isClient = new byte[1] { 0 };
            //send update to opponent
            state.OpponentSocket.Send(isClient);
            state.OpponentSocket.Send(state.Update);

            //indicate this is an acknowledgement for an update sent
            isClient[0] = 1;
            //send ack
            state.ClientSocket.Send(isClient);
        }
    }

    //stores information about a waiting battle
    internal class WaitConnection
    {
        //lock for write operations after connection
        private object writeLock;
        //blocks until both players connect
        private ManualResetEventSlim wait;
        //sockets of the clients in the battle
        private Socket socket1;
        private Socket socket2;

        public WaitConnection()
        {
            wait = new ManualResetEventSlim();
        }

        //set up a wait connection with the specified player socket
        public WaitConnection(Socket socket)
        {
            wait = new ManualResetEventSlim();
            socket1 = socket;
        }

        //getters and setters
        public Socket Socket1
        {
            get
            {
                return socket1;
            }

            set
            {
                socket1 = value;
            }
        }

        public Socket Socket2
        {
            get
            {
                return socket2;
            }

            set
            {
                socket2 = value;
            }
        }

        public void setWait()
        {
            wait.Set();
        }

        public void resetWait()
        {
            wait.Reset();
        }

        public ManualResetEventSlim Wait
        {
            get
            {
                return wait;
            }
        }

        public object WriteLock
        {
            get
            {
                return writeLock;
            }

            set
            {
                writeLock = value;
            }
        }
    }


    // State object for reading client data asynchronously
    internal class State
    {
        //lock for asyncronous write operations
        private object writeLock;
        //client's socket
        private Socket clientSocket;
        //opponents socket
        private Socket opponentSocket;
        //buffer for updates
        private byte[] update;

        //getters and setters
        public Socket ClientSocket
        {
            get
            {
                return clientSocket;
            }

            set
            {
                clientSocket = value;
            }
        }

        public Socket OpponentSocket
        {
            get
            {
                return opponentSocket;
            }

            set
            {
                opponentSocket = value;
            }

        }

        public byte[] Update
        {
            get
            {
                return update;
            }

            set
            {
                update = value;
            }
        }

        public object WriteLock
        {
            get
            {
                return writeLock;
            }

            set
            {
                writeLock = value;
            }
        }
    }
}
