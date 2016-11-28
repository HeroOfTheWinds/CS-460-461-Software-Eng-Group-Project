using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    public class BattleServer
    {
        public const int UPDATE_SIZE = 41;
        public const int BATTLE_PORT = 2224;
        public static readonly IPAddress IP = IPAddress.Parse("10.10.10.103");

        // Thread signal.
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();

        private static Dictionary<Guid, WaitConnection> waiting = new Dictionary<Guid, WaitConnection>();

        private static readonly object DICT_LOCK = new object();


        public static void startServer()
        {

            //might have to change code to get ip address
            //IPAddress ipAddress = Dns.GetHostEntry("localhost").AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(IP, BATTLE_PORT);

            // Create a TCP/IP socket.
            Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            // Bind the socket to the local endpoint and listen for incoming connections.
            try
            {
                listener.Bind(localEndPoint);
                listener.Listen(100);

                while (true)
                {
                    // Set the event to nonsignaled state.
                    connectionFound.Reset();

                    //asynchronously get connections
                    listener.BeginAccept(new AsyncCallback(connect), listener);

                    // Wait until a connection is made before continuing.
                    connectionFound.Wait();
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            Console.WriteLine("\nServer Closed...");
            Console.Read();

        }

        public static void connect(IAsyncResult ar)
        {
            try
            {
                Console.WriteLine("\nPlayer connected");
                WaitConnection wait;
                byte[] guid = new byte[16];
                Guid battleGUID;

                byte[] spawn = new byte[1];

                State state = new State();

                //connection finished, allow others to connect
                connectionFound.Set();

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
                    //if it is not, add it to the dictionary with a waitconnection object holding this client's socket
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

                handler.Send(spawn, 1, 0);

                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            catch(Exception)
            {
                Console.WriteLine("\nPlayer disconnected");
            }
        }

        public static void readUpdate(IAsyncResult ar)
        {
            Socket handler = null;
            try
            {
                bool battleEnd;
                bool win;

                //STORE HP AND LOCATIONS IN STATE, UPDATE AND USE FOR THINGS (anti-cheat stuff)

                // Retrieve the state object and the handler socket
                // from the asynchronous state object.
                State state = (State)ar.AsyncState;
                handler = state.ClientSocket;

                //NEED TO DEAL WITH SIZES AND STUFF, MAYBE SEND A MESSAGE WITH THE SIZE
                //for now everything sent as 41 bytes
                ar.AsyncWaitHandle.WaitOne();
                handler.EndReceive(ar);


                byte flags = state.Update[0];

                battleEnd = (flags & 1) == 1 ? true : false;
                win = ((flags >> 1) & 1) == 1 ? true : false;

                lock(state.WriteLock)
                {
                    send(state);
                }
                

                if (!battleEnd)
                {
                    handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
                }
                else
                {
                    //handle winner stuff (DB stuff, etc)

                    handler.Shutdown(SocketShutdown.Both);
                    handler.Close();
                    Console.WriteLine("\nPlayer disconnected");
                }
            }
            //stop gracefully if player disconnects before battle ends
            catch (Exception)
            {
                Console.WriteLine("\nPlayer disconnected");
                
            }
        }

        private static void send(State state)
        {
            byte[] isClient = new byte[1] { 0 };

            state.OpponentSocket.Send(isClient);
            state.OpponentSocket.Send(state.Update);


            isClient[0] = 1;
            state.ClientSocket.Send(isClient);
        }
    }

    internal class WaitConnection
    {
        private object writeLock;
        private ManualResetEventSlim wait;
        private Socket socket1;
        private Socket socket2;

        public WaitConnection()
        {
            wait = new ManualResetEventSlim();
        }

        public WaitConnection(Socket socket)
        {
            wait = new ManualResetEventSlim();
            socket1 = socket;
        }

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
        private object writeLock;
        private Socket clientSocket;
        private Socket opponentSocket;
        // Receive buffer.
        private byte[] update;

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
