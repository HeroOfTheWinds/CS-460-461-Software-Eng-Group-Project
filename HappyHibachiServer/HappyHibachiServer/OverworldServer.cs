﻿using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    class OverworldServer
    {
        //size of updates in bytes
        public static readonly int UPDATE_SIZE = 8;
        //port to listen on (temp test port)
        public const int OVERWORLD_PORT = 6004;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.0.0.4");


        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();


        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, OVERWORLD_PORT);

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

                Console.WriteLine("\nPlayer connected overworld");

                OverworldState state = new OverworldState();

                state.Update = new byte[UPDATE_SIZE];

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                byte[] id = new byte[16];
                handler.Receive(id, 16, 0);
                
                state.ClientID = new Guid(id);
                Console.WriteLine(state.ClientID.ToString());
                state.ClientSocket = handler;

                ClientState addSocket;
                lock (ConnectedPlayers.DICTIONARY_LOCK)
                {
                    if (ConnectedPlayers.playerDetails.TryGetValue(state.ClientID, out addSocket))
                    {
                        addSocket.OverworldSocket = handler;
                    }
                    else
                    {
                        addSocket = new ClientState(null, handler, null, null);
                        ConnectedPlayers.playerDetails.Add(state.ClientID, addSocket);
                    }
                }

                state.Lat = addSocket.Latitude;
                state.Lon = addSocket.Longtitude;


                //start receiving client updates
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch (Exception e)
            {
                //Console.WriteLine(e);
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
                OverworldState state = (OverworldState)ar.AsyncState;
                handler = state.ClientSocket;
                ar.AsyncWaitHandle.WaitOne();
                if (handler.EndReceive(ar) == 0)
                {
                    Console.WriteLine("\nPlayer disconnected overworld readUpdate");
                    return;
                }

                //Console.WriteLine(BitConverter.ToSingle(state.Update, 0));

                //process update (GPS coords stored in state.Update) into DB
                //var dbCon = new DatabaseConnect();
                //dbCon.UpdatePlayerCoor(state.Update, state.ClientID);

                state.Lat[0] = BitConverter.ToSingle(state.Update, 0);
                state.Lon[0] = BitConverter.ToSingle(state.Update, 4);

                //next gps update not expected for long enough that it should probably be more efficient to handle parseing and sending synchronously
                //pretty sure asynch calls require a certain amount of memory overhead, can change if need though
                send(state);
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //end communications gracefully if player disconnects
            catch (Exception e)
            {
                //Console.WriteLine(e.ToString());
                Console.WriteLine("\nPlayer disconnected overworld readUpdate");

            }
        }



        //Process and send nearby data to player
        //later on add mechanism to avoid redundant object sending (at least for fixed position objects)
        private static void send(OverworldState state)
        {
            //possibly figure out GPS range and use limited numerical value size to incorporate type of object into the same data size
            List<float> nearbyC = new List<float>();
            List<Guid> nearbyID = new List<Guid>();
            List<byte> nearbyPlayerDetails = new List<byte>();

            //place gps coords and object's id in lists to be sent (indexes of latitude must be 2i and longtitude 2i+1 where i is the index of the respective objects guid)
            var dbCon = new DatabaseConnect();

            dbCon.findNearbyObjects(state.Lat[0], state.Lon[0], nearbyC, nearbyID, nearbyPlayerDetails);

            //use latitude to include the type of object it is. Determine if object is a (player, colloseum, landmark) and add (0, 1, 2) * 181 to latitude respectively
            //latitudes range is -90 - 90, so by doing this the type of object can be determined without sending additional data (value < 91: player, 90 < value < 272: colloseum, 271 < value: colloseum)

            //creates byte aray with proper number of bytes
            state.Nearby = new byte[nearbyC.Count * 4 + nearbyID.Count * 16 + nearbyPlayerDetails.Count];
            //put nearby coords in byte array to be sent
            Buffer.BlockCopy(nearbyC.ToArray(), 0, state.Nearby, 0, nearbyC.Count * 4);

            //put respective guids
            int i = 0;
            foreach(Guid id in nearbyID.ToArray())
            {
                Buffer.BlockCopy(id.ToByteArray(), 0, state.Nearby, nearbyC.Count * 4 + 16 * i, 16);
                i++;
            }

            Buffer.BlockCopy(nearbyPlayerDetails.ToArray(), 0, state.Nearby, nearbyC.Count * 4 + nearbyID.Count * 16, nearbyPlayerDetails.Count);

            //tell client size of update
            state.ClientSocket.Send(BitConverter.GetBytes(state.Nearby.Length));
            //tell client number of objects
            state.ClientSocket.Send(BitConverter.GetBytes(nearbyID.Count));
            //send nearby objects to client
            state.ClientSocket.Send(state.Nearby, 0, state.Nearby.Length, 0);
        }

    }



    // State object for reading client data asynchronously
    internal class OverworldState
    {
        //client's socket
        private Socket clientSocket;
        private Guid clientID;
        private byte[] update;
        private byte[] nearby;
        private float[] lat;
        private float[] lon;

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

        public float[] Lat
        {
            get
            {
                return lat;
            }

            set
            {
                lat = value;
            }
        }

        public float[] Lon
        {
            get
            {
                return lon;
            }

            set
            {
                lon = value;
            }
        }
    }

}

