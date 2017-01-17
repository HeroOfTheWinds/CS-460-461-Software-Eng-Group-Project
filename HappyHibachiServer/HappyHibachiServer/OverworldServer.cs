using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    class OverworldServer
    {
        //size of updates in bytes
        public const int UPDATE_SIZE = 8;
        //port to listen on (temp test port)
        public const int OVERWORLD_PORT = 1234;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.42.42.153");

        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();


        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, OVERWORLD_PORT);

            //create udp listener
            //assume raw udp should suffice, can add order checks or discard if issues arise
            Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);

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

                OverworldState state = new OverworldState();

                state.Update = new byte[UPDATE_SIZE];

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                
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

                //retreive the state object and socket
                OverworldState state = (OverworldState)ar.AsyncState;
                handler = state.ClientSocket;

                ar.AsyncWaitHandle.WaitOne();
                handler.EndReceive(ar);


                //process update (GPS coords stored in state.Update) into DB


                //next gps update not expected for long enough that it should probably be more efficient to handle parseing and sending synchronously
                //pretty sure asynch calls require a certain amount of memory overhead, can change if need though
                send(state);

                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);

            }
            //end communications gracefully if player disconnects
            catch (Exception)
            {
                Console.WriteLine("\nPlayer disconnected");

            }
        }



        //Process and send nearby data to player
        private static void send(OverworldState state)
        {
            //possibly figure out GPS range and use limited numerical value size to incorporate type of object into the same data size
            List<float> nearby = new List<float>();

            //place gps coords then object's guid in array to be sent

            //creates byte aray with proper number of bytes
            state.Nearby = new byte[nearby.Count * 8 * 16];
            //put nearby coords in byte array to be sent
            Buffer.BlockCopy(nearby.ToArray(), 0, state.Nearby, 0, nearby.Count * 4);
            //send nearby objects to client
            state.ClientSocket.Send(state.Nearby, 0, state.Nearby.Length, 0);
        }

    }



    // State object for reading client data asynchronously
    internal class OverworldState
    {
        //client's socket
        private Socket clientSocket;
        private byte[] update;
        private byte[] nearby;

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

        public byte[] Nearby
        {
            get
            {
                return nearby;
            }

            set
            {
                nearby = value;
            }
        }
    }

}

