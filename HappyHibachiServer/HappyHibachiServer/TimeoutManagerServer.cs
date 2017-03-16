using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using System.Threading;


namespace HappyHibachiServer
{
    class TimeoutManagerServer
    {
        //size of updates in bytes
        public static readonly int UPDATE_SIZE = 1;
        //port to listen on (temp test port)
        public const int TIMEOUT_MANAGER_PORT = 6002;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.0.0.4");

        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();

        //signal for connections
        public static Dictionary<Guid, TimeoutState> clientSockets = new Dictionary<Guid, TimeoutState>();

        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, TIMEOUT_MANAGER_PORT);

            //create udp listener
            //assume raw udp should suffice, can add order checks or discard if issues arise
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

                Console.WriteLine("\nPlayer connected timeout manager");

                TimeoutState state = new TimeoutState();

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                //this isnt exactly right, how to get state

                state.ClientSocket = handler;
                state.startTimeout();

                //start receiving client updates
                handler.BeginReceive(state.TestCon, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch (Exception)
            {
                Console.WriteLine("\nPlayer disconnected overworld connect");
            }
        }



        //read updates from clients
        public static void readUpdate(IAsyncResult ar)
        {
            Socket handler = null;
            try
            {

                //retreive the state object and socket
                TimeoutState state = (TimeoutState)ar.AsyncState;
                handler = state.ClientSocket;

                ar.AsyncWaitHandle.WaitOne();
                handler.EndReceive(ar);

                state.resetTimeout();
                handler.Send(state.TestCon);

                handler.BeginReceive(state.TestCon, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);

            }
            //end communications gracefully if player disconnects
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                Console.WriteLine("\nPlayer disconnected timeout readUpdate");

            }
        }



    }

    internal class TimeoutState
    {
        private byte[] testCon = { 0 };
        private Socket clientSocket;
        private Socket genComSocket;
        private Socket overworldSocket;
        private Socket battleSocket;
        private System.Timers.Timer timeout;
        private Guid clientID;

        public void startTimeout()
        {
            timeout = new System.Timers.Timer(10000);
            timeout.Elapsed += connectionTimeout;
        }

        public void resetTimeout()
        {
            timeout.Stop();
            timeout.Start();
        }

        private void connectionTimeout(object sender, ElapsedEventArgs e)
        {
            Console.WriteLine("Connection timeout");
            try
            {
                genComSocket.Shutdown(SocketShutdown.Both);
                genComSocket.Close();
            }
            catch(Exception) { }

            try
            {
                overworldSocket.Shutdown(SocketShutdown.Both);
                overworldSocket.Close();
            }
            catch(Exception) { }

            try
            {
                if (genComSocket != null)
                {
                    battleSocket.Shutdown(SocketShutdown.Both);
                    battleSocket.Close();
                }
            }
            catch(Exception) { }

            if(TimeoutManagerServer.clientSockets.ContainsKey(ClientID))
            {
                TimeoutManagerServer.clientSockets.Remove(ClientID);
            }
            lock(GenComServer.DICTIONARY_LOCK)
            {
                if (GenComServer.players.ContainsKey(ClientID))
                {
                    GenComServer.players.Remove(ClientID);
                }
            }
            
        }

        public Socket BattleSocket
        {
            get
            {
                return battleSocket;
            }

            set
            {
                battleSocket = value;
            }
        }

        public Socket OverworldSocket
        {
            get
            {
                return overworldSocket;
            }

            set
            {
                overworldSocket = value;
            }
        }

        public Socket GenComSocket
        {
            get
            {
                return genComSocket;
            }

            set
            {
                genComSocket = value;
            }
        }

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

        public byte[] TestCon
        {
            get
            {
                return testCon;
            }

            set
            {
                testCon = value;
            }
        }

        public Guid ClientID
        {
            get
            {
                return clientID;
            }

            set
            {
                clientID = value;
            }
        }

        public TimeoutState()
        {
            GenComSocket = null;
            OverworldSocket = null;
            BattleSocket = null;
        }
    }
}
