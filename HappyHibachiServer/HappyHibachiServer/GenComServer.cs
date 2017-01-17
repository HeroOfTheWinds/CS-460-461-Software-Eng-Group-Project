using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    class GenComServer
    {
        //size of updates in bytes
        public const int UPDATE_SIZE = 8;
        //port to listen on (temp test port)
        public const int COM_PORT = 2345;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.42.42.153");

        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();


        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, COM_PORT);

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

                Console.WriteLine("\nPlayer connected");

                ComState state = new ComState();

                state.Update = new byte[UPDATE_SIZE];

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                Guid clientID = new Guid();


                //insert client id into db, use this as the key for identifying clients



                //start receiving client updates
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch (Exception)
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
                if(serverUpdateAvailable())
                {
                    processSpCom();
                }
                //retreive the state object and socket
                ComState state = (ComState)ar.AsyncState;
                handler = state.ClientSocket;

                ar.AsyncWaitHandle.WaitOne();
                handler.EndReceive(ar);


                state.Type = state.Update[0];


                switch(state.Type)
                {
                    //player requesting battle with another player
                    case 0:
                        processBattleReq();
                        break;
                    //player interacting with colloseum
                    case 1:
                        processColloseumReq();
                        break;
                    //player interacting with landmark
                    case 2:
                        processLandmarkReq();
                        break;
                    //anything else we may want
                    default:
                        Console.WriteLine("Error: invalid update type received");
                        break;
                }

                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);

            }
            //end communications gracefully if player disconnects
            catch (Exception)
            {
                Console.WriteLine("\nPlayer disconnected");

            }
        }

        private static bool serverUpdateAvailable()
        {
            //check if the server needs to send things to the user, eg quests etc
            return false;
        }

        async private static void processLandmarkReq()
        {
            //process database stuff and give player items
        }

        async private static void processColloseumReq()
        {
            //return info about colloseum and do other stuff that needs to be done
        }

        async private static void processBattleReq()
        {
            throw new NotImplementedException();
        }

        async private static void processSpCom()
        {
            //send stuff to client
        }

    }



    // State object for reading client data asynchronously
    internal class ComState
    {
        //client's socket
        private Socket clientSocket;
        private byte[] update;
        private byte type;

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

        public byte Type
        {
            get
            {
                return type;
            }

            set
            {
                type = value;
            }
        }
    }
}
