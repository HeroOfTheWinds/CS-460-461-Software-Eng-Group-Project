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
        //public static Dictionary<Guid, TimeoutState> clientSockets = new Dictionary<Guid, TimeoutState>();

        //public static readonly object DICTIONARY_LOCK = new object();

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
            TimeoutState state = new TimeoutState();
            try
            {
                //connection finished, allow others to connect
                connectionFound.Set();

                Console.WriteLine("\nPlayer connected timeout manager");

                //get user id first, follow similar procedure
                //TimeoutState state = new TimeoutState(null, null, null);

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                //MAKE SURE TO ADD THIS TO CLIENT SIDE
                byte[] id = new byte[16];
                handler.Receive(id, 16, 0);
                state.ClientID = new Guid(id);

                state.ClientSocket = handler;

                //Console.WriteLine("loc 0\n");

                ClientState addSocket;
                lock (ConnectedPlayers.DICTIONARY_LOCK)
                {
                    if (ConnectedPlayers.playerDetails.TryGetValue(state.ClientID, out addSocket))
                    {
                        addSocket.TimeoutSocket = handler;
                    }
                    else
                    {
                        addSocket = new ClientState(null, null, null, handler);
                        ConnectedPlayers.playerDetails.Add(state.ClientID, addSocket);
                    }
                }

                //Console.WriteLine("loc 1");

                state.startTimeout();

                //Console.WriteLine("loc 2");

                //start recieving client connection confirmations
                handler.BeginReceive(state.TestCon, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                Console.WriteLine("\nPlayer disconnected timeout connect");
                if(state != null)
                {
                    state.cleanup();
                }
            }
        }



        //read updates from clients
        public static void readUpdate(IAsyncResult ar)
        {
            Socket handler = null;
            TimeoutState state = (TimeoutState)ar.AsyncState;

            try
            {
                //Console.WriteLine("Entered timeout readupdate");

                //retreive the state object and socket
                handler = state.ClientSocket;

                ar.AsyncWaitHandle.WaitOne();
                if(handler.EndReceive(ar) == 0)
                {
                    Console.WriteLine("\nPlayer disconnected timeout readUpdate");
                    if (state.Timeout != null)
                    {
                        state.cleanup();
                    }
                    
                    return;
                }

                //Console.WriteLine("Received ack\n");

                //when recieve ack, restart timout
                state.resetTimeout();
                handler.Send(state.TestCon);

                handler.BeginReceive(state.TestCon, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);

            }
            //end communications gracefully if player disconnects
            catch (Exception)
            {
                //Console.WriteLine(e.ToString());
                Console.WriteLine("\nPlayer disconnected timeout readUpdate");
                if (state.Timeout != null)
                {
                    state.cleanup();
                }
            }
        }



    }

    internal class TimeoutState
    {
        private byte[] testCon = { 0 };
        private Socket clientSocket;
        private System.Timers.Timer timeout = null;
        private Guid clientID;

        public System.Timers.Timer Timeout
        {
            get
            {
                return timeout;
            }
        }

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
            if (timeout != null)
            {
                cleanup();
            }
        }

        public void cleanup()
        {
            ClientState cs;
            lock(ConnectedPlayers.DICTIONARY_LOCK)
            {
                if(ConnectedPlayers.playerDetails.TryGetValue(ClientID, out cs))
                {
                    try
                    {
                        cs.TimeoutSocket.Shutdown(SocketShutdown.Both);
                        cs.TimeoutSocket.Close();
                    }
                    catch (Exception) { }

                    try
                    {
                        cs.GenComSocket.Shutdown(SocketShutdown.Both);
                        cs.GenComSocket.Close();
                    }
                    catch (Exception) { }

                    try
                    {
                        cs.OverworldSocket.Shutdown(SocketShutdown.Both);
                        cs.OverworldSocket.Close();
                    }
                    catch (Exception) { }

                    try
                    {
                        if (cs.BattleSocket != null)
                        {
                            cs.BattleSocket.Shutdown(SocketShutdown.Both);
                            cs.BattleSocket.Close();
                        }
                    }
                    catch (Exception) { }

                    ConnectedPlayers.playerDetails.Remove(ClientID);
                }
                if(timeout != null)
                {
                    timeout.Dispose();
                    timeout = null;
                }
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
    }
}
