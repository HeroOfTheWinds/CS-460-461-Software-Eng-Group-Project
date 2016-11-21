using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    public class TestSinglePerson
    {
        //possible race conditions for socket writes???
        public const int UPDATE_SIZE = 33;
        public const int BATTLE_PORT = 2224;
        public static readonly IPAddress IP = IPAddress.Parse("127.0.0.1");

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
            Console.WriteLine("\nPlayer connected");
            WaitConnection wait;
            byte[] guid = new byte[16];
            Guid battleGUID;

            State state = new State();

            // Signal the main thread to continue.
            connectionFound.Set();

            // Get the socket that handles the client request.
            Socket listener = (Socket)ar.AsyncState;
            Socket handler = listener.EndAccept(ar);

            //read the battle guid from the client
            handler.Receive(guid, 16, 0);
            battleGUID = new Guid(guid);
            /*
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
                //set event indicating both clients are connected

                //add some sort of timeout
                wait.setWait();
            }
            else
            {

                //wait until second client connects
                wait.Wait.Wait();
                //set this client's opponent to the second client in wait
                state.OpponentSocket = wait.Socket2;
                //both clients are connected, remove from waiting
                waiting.Remove(battleGUID);
            }
            */
            //set client's socket in state object
            state.ClientSocket = handler;
            //initialize buffer
            state.Update = new byte[UPDATE_SIZE];

            handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
        }

        public static void readUpdate(IAsyncResult ar)
        {
            bool battleEnd;
            bool win;

            //STORE HP AND LOCATIONS IN STATE, UPDATE AND USE FOR THINGS

            // Retrieve the state object and the handler socket
            // from the asynchronous state object.
            State state = (State)ar.AsyncState;
            Socket handler = state.ClientSocket;

            //NEED TO DEAL WITH SIZES AND STUFF, MAYBE SEND A MESSAGE WITH THE SIZE
            //for now everything sent as 33 bytes
            ar.AsyncWaitHandle.WaitOne();
            handler.EndReceive(ar);


            byte flags = state.Update[0];

            battleEnd = (flags & 1) == 1 ? true : false;
            win = ((flags >> 1) & 1) == 1 ? true : false;

            send(state);

            if (!battleEnd)
            {
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            else
            {
                //handle winner stuff

                handler.Shutdown(SocketShutdown.Both);
                handler.Close();
            }
        }

        private static void send(State state)
        {
            byte[] isClient = new byte[1] { 0 };

            //state.OpponentSocket.Send(isClient);
            //state.OpponentSocket.Send(state.Update);


            isClient[0] = 1;
            state.ClientSocket.Send(isClient);
        }
    }
}
