using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;
using System.Text;
using System.Timers;

namespace HappyHibachiServer
{
    class GenComServer
    {
        //size of updates in bytes
        public const int UPDATE_SIZE = 17;
        //port to listen on (temp test port)
        public const int GC_PORT = 6003;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.0.0.4");

        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();


        //public static Dictionary<Guid, ComState> players;
        //public static readonly object DICTIONARY_LOCK = new object();


        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, GC_PORT);

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
            ComState state = null;
            try
            {
                //connection finished, allow others to connect
                connectionFound.Set();

                Console.WriteLine("\nPlayer connected gen com");

                

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                

                byte[] id = new byte[16];
                handler.Receive(id, 16, 0);
                Guid clientID = new Guid(id);

                ClientState addSocket;
                lock (ConnectedPlayers.DICTIONARY_LOCK)
                {
                    if (ConnectedPlayers.playerDetails.TryGetValue(clientID, out addSocket))
                    {
                        addSocket.GenComSocket = handler;
                    }
                    else
                    {
                        addSocket = new ClientState(handler, null, null, null);
                        ConnectedPlayers.playerDetails.Add(clientID, addSocket);
                    }
                }
                //link writelock with the playerdetails gencomwritelock for this client
                state = new ComState(addSocket.GENCOM_WRITE_LOCK);
                //initialize values
                state.ClientSocket = handler;
                state.Update = new byte[UPDATE_SIZE];
                state.ClientID = clientID;

                //start receiving client updates
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch (Exception e)
            {   
                if(state != null)
                {
                    state.disposeTimer();
                }
                Console.WriteLine(e.ToString());
                Console.WriteLine("\nPlayer disconnected gen com connect");
            }
        }



        //read updates from clients
        public static void readUpdate(IAsyncResult ar)
        {
            //retreive the state object and socket
            ComState state = (ComState)ar.AsyncState;
            Socket handler = state.ClientSocket;

            try
            {
                ar.AsyncWaitHandle.WaitOne();
                if (handler.EndReceive(ar) == 0)
                {
                    state.disposeTimer();
                    Console.WriteLine("\nPlayer disconnected gen com readUpdate");
                    return;
                }

                byte type = state.Update[0];

                //if (serverUpdateAvailable(state))
                //{
                //    processSpCom(state);
                //}


                switch(type)
                {
                    //player requesting battle with another player
                    case 0:
                        processBattleReq(state);
                        break;
                    //player interacting with colloseum
                    case 1:
                        processColloseumReq(state);
                        break;
                    //player interacting with landmark
                    case 2:
                        processLandmarkReq(state);
                        break;
                    case 3:
                        processQuestComplete(state);
                        break;
                    case 4:
                        processItemReq(state);
                        break;
                    case 5:
                        processBattleResponse(state);
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
                state.disposeTimer();
                Console.WriteLine("\nPlayer disconnected gen com readUpdate");

            }
        }

        private static void processQuestComplete(ComState state)
        {
            var dbCon = new DatabaseConnect();
            dbCon.updatePlayerExpAfterQuest(state.ClientID);

            Console.WriteLine("Recieved quest complete");
        }

        private static void processBattleResponse(ComState state)
        {
            byte[] response = new byte[18];
            byte[] ack = { 1 };
            response[0] = 5;
            state.ClientSocket.Receive(response, 1, 17, 0);
            ClientState opponent;
            if (ConnectedPlayers.playerDetails.TryGetValue(getUpdateID(state.Update), out opponent))
            {
                lock(opponent.GENCOM_WRITE_LOCK)
                {
                    //Console.WriteLine(response[0]);
                    opponent.GenComSocket.Send(response, 18, 0);
                }
            }
            else
            {
                //indicate player no longer there
                ack[0] = 0;
            }
            //if match accepted send acknowledgment to player to connect to battle scene or not depending on if opponent still online
            if (response[17] == 1)
            {
                lock (state.WRITE_LOCK)
                {
                    state.ClientSocket.Send(ack, 1, 0);
                }
            }
        }

        //use shorts for sizes (in bytes) to save data
        //if image or anything being sent is larger than 64kb it's much too large

        private static void processItemReq(ComState state)
        {
            List<byte> itemIDs = new List<byte>();
            short size = 0;

            //verify item req valid
            //generate items and tell user what items they got, send a list of 0 if cant get items from landmark
            //place item ids (item ids are bytes) in itemIDs
            //store number of items in size
            //maybe a random number of items? not sure how you want to deal with that


            Random r = new Random();
            size = (short)r.Next(1, 4);
            for(int i = 0; i < size; i++)
            {
                //lets say 0 is a health pot and 1 is a landmine
                itemIDs.Add((byte)r.Next(0, 2));
            }

            byte[] type = { 4 };
            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(type);
                state.ClientSocket.Send(BitConverter.GetBytes(size));
                state.ClientSocket.Send(itemIDs.ToArray());
            }
        }

        private static bool serverUpdateAvailable(ComState state)
        {
            Console.WriteLine("quest elapsed 0");
            if (state.getQuest())
            {
                Console.WriteLine("quest elapsed 1");
                return true;
            }

            //anything else that needs to be checked other than quests?
            
            return false;
        }

        private static void processLandmarkReq(ComState state)
        {
            short sizeName;
            short sizeDescription;
            short sizeImage = 0;
            string name = "";
            string description = "";
            string image = "";

            //process database stuff and provide info on landmark
            //store landmark name description and image in respective vars
            var dbCon = new DatabaseConnect();
            dbCon.provideLandmarkInfoFromDB(getUpdateID(state.Update), ref name, ref description, ref image);
            //Console.WriteLine("Landmark GUID: " + state.ClientID.ToString());
            //Console.WriteLine("Landmark Name: " + name);
            //Console.WriteLine("Landmark description: " + description);
            //Console.WriteLine("Landmark image: " + image);

            sizeName = (short)Encoding.ASCII.GetByteCount(name);
            sizeDescription = (short)Encoding.ASCII.GetByteCount(description);
            //imagesize here
            byte[] type = { 2 };
            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(type);
                state.ClientSocket.Send(BitConverter.GetBytes(sizeName));
                state.ClientSocket.Send(Encoding.ASCII.GetBytes(name));
                state.ClientSocket.Send(BitConverter.GetBytes(sizeDescription));
                state.ClientSocket.Send(Encoding.ASCII.GetBytes(description));
                state.ClientSocket.Send(BitConverter.GetBytes(sizeImage));
            }
            //send image

        }

        private static void processColloseumReq(ComState state)
        {
            short sizeName;
            short sizeDescription;
            string name = "";
            string description = "";
            string image = "";

            //process database stuff and provide info on colloseum
            //store colloseum name and description in respective vars
            //add other details later
            var dbCon = new DatabaseConnect();
            dbCon.provideColosseumInfoFromDB(getUpdateID(state.Update), ref name, ref description, ref image);
            //Console.WriteLine("Colloseum GUID: " + state.ClientID.ToString());
            //Console.WriteLine("Colloseum Name: " + name);
            //Console.WriteLine("Colloseum description: " + description);
            //Console.WriteLine("Colloseum image: " + image);

            sizeName = (short)Encoding.ASCII.GetByteCount(name);
            sizeDescription = (short)Encoding.ASCII.GetByteCount(description);

            byte[] type = { 1 };
            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(type);
                state.ClientSocket.Send(BitConverter.GetBytes(sizeName));
                state.ClientSocket.Send(Encoding.ASCII.GetBytes(name));
                state.ClientSocket.Send(BitConverter.GetBytes(sizeDescription));
                state.ClientSocket.Send(Encoding.ASCII.GetBytes(description));
            }
        }

        private static void processBattleReq(ComState state)
        {
            byte[] outUpdate = new byte[33];
            outUpdate[0] = 0;
            Array.Copy(state.ClientID.ToByteArray(), 0, outUpdate, 1, 16);
            state.ClientSocket.Receive(outUpdate, 17, 16, 0);
            ClientState opponent;
            if(ConnectedPlayers.playerDetails.TryGetValue(getUpdateID(state.Update), out opponent))
            {
                lock(opponent.GENCOM_WRITE_LOCK)
                {
                    opponent.GenComSocket.Send(outUpdate, 33, 0);
                }
            }
            else
            {

                //temporarily write console message for potential troubleshooting
                Console.WriteLine("Received ID not in player table");
            }
        }

        private static void processSpCom(ComState state)
        {
            ////tell user they recieved a quest
            //byte[] type = { 3 };
            ////Console.WriteLine("quest elapsed 2");
            //lock (state.WRITE_LOCK)
            //{
            //    state.ClientSocket.Send(type);
            //}
        }

        private static Guid getUpdateID(byte[] update)
        {
            byte[] temp = new byte[16];
            Array.Copy(update, 1, temp, 0, 16);
            return new Guid(temp);
        }

    }

    // State object for reading client data asynchronously
    internal class ComState
    {
        //client's socket
        private Socket clientSocket;
        private Guid clientID;
        private byte[] update;
        private bool busy;
        public readonly object WRITE_LOCK;
        private Random rand;
        private System.Timers.Timer generateQuest = null;
        private bool quest;

        public bool getQuest()
        {
            if(quest)
            {
                quest = false;
                return true;
            }
            return false;
        }

        public void disposeTimer()
        {
            if(generateQuest != null)
            {
                generateQuest.Dispose();
            }
            
        }

        public ComState(object wl)
        {
            quest = false;
            WRITE_LOCK = wl;
            rand = new Random();
            generateQuest = new System.Timers.Timer(10000);
            generateQuest.Elapsed += setQuest;
            generateQuest.Start();
        }

        private void setQuest(object sender, ElapsedEventArgs e)
        {
            quest = true;
            //tell user they recieved a quest
            byte[] type = { 3 };
            lock (WRITE_LOCK)
            {
                //Console.WriteLine("quest elapsed");
                clientSocket.Send(type, 1, 0);
            }
        }



        public int getRandomPosition(int max)
        {
            return (int)(rand.NextDouble() * max);
        }

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

        public bool Busy
        {
            get
            {
                return busy;
            }

            set
            {
                busy = value;
            }
        }
    }
}
