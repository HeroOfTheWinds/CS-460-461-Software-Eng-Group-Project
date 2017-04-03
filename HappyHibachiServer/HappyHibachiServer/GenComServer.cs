using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;
using System.Text;

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
                ComState state = new ComState(addSocket.GENCOM_WRITE_LOCK);
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
                    Console.WriteLine("\nPlayer disconnected gen com readUpdate");
                    return;
                }

                byte type = state.Update[0];

                if (serverUpdateAvailable())
                {
                    processSpCom(state);
                }


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
                Console.WriteLine("\nPlayer disconnected gen com readUpdate");

            }
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

            //------------------TEMPORARY TEST CODE, NO DB BACKING------------------

            Random r = new Random();
            size = (short)r.Next(1, 4);
            for(int i = 0; i < size; i++)
            {
                //lets say 0 is a health pot and 1 is a landmine
                itemIDs.Add((byte)r.Next(0, 2));
            }

            //----------------------------------------------------------------------

            byte[] type = { 4 };
            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(type);
                state.ClientSocket.Send(BitConverter.GetBytes(size));
                state.ClientSocket.Send(itemIDs.ToArray());
            }
        }

        private static bool serverUpdateAvailable()
        {
            //check if the server needs to send things to the user, eg quests etc
            //implement later
            
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
            Console.WriteLine("Landmark GUID: " + state.ClientID.ToString());
            Console.WriteLine("Landmark Name: " + name);
            Console.WriteLine("Landmark description: " + description);
            Console.WriteLine("Landmark image: " + image);

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
            Console.WriteLine("Colloseum GUID: " + state.ClientID.ToString());
            Console.WriteLine("Colloseum Name: " + name);
            Console.WriteLine("Colloseum description: " + description);
            Console.WriteLine("Colloseum image: " + image);

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
                //signal player could not be challenged when implemented
                //can probably just wait for server response when challenge sent
                //just send a byte indicating whether server determined client to be busy (check if busy set in state), or if dced
                //if client busy and receives request because server not updated yet or timed out, have it respond with 0, 1, or 2
                //where receiving a 2 in a type 5 com means client busy (0 declined, 1 accepted)

                //temporarily write console message for potential troubleshooting
                Console.WriteLine("Received ID not in player table");
            }
        }

        private static void processSpCom(ComState state)
        {
            //send stuff to client
            //this will have the type id of 3
            //implement later
            byte[] type = { 3 };
            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(type);
            }
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

        public ComState(object wl)
        {
            WRITE_LOCK = wl;
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
