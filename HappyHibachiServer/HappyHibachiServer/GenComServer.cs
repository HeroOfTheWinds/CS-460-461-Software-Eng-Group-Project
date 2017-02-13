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
        public static readonly IPAddress IP = IPAddress.Parse("10.42.42.153");

        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();


        private static Dictionary<Guid, ComState> players;


        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, GC_PORT);

            //create tcp listener
            Socket listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            players = new Dictionary<Guid, ComState>();
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
            ComState state = new ComState();

            try
            {
                //connection finished, allow others to connect
                connectionFound.Set();

                Console.WriteLine("\nPlayer connected gen com");

                state.Update = new byte[UPDATE_SIZE];

                //get socket for client
                Socket listener = (Socket)ar.AsyncState;
                Socket handler = listener.EndAccept(ar);

                state.ClientSocket = handler;

                byte[] id = new byte[16];
                handler.Receive(id, 16, 0);
                state.ClientID = new Guid(id);

                players.Add(state.ClientID, state);

                //start receiving client updates
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch (Exception e)
            {
                if(players.ContainsKey(state.ClientID))
                {
                    players.Remove(state.ClientID);
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
                handler.EndReceive(ar);

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
                if (players.ContainsKey(state.ClientID))
                {
                    players.Remove(state.ClientID);
                }
                Console.WriteLine("\nPlayer disconnected gen com readUpdate");

            }
        }

        private static void processBattleResponse(ComState state)
        {
            byte[] response = new byte[18];
            response[0] = 5;
            state.ClientSocket.Receive(response, 1, 17, 0);
            ComState opponent;
            if (players.TryGetValue(getUpdateID(state.Update), out opponent))
            {
                lock(opponent.WRITE_LOCK)
                {
                    opponent.ClientSocket.Send(response, 17, 0);
                }
            }
            else
            {
                //need some way to indicate player no longer available

                //temporarily write console message for potential troubleshooting
                Console.WriteLine("Received ID not in player table");
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
            lock (state.WRITE_LOCK)
            {
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
            //create variable for the image, for now just sending other 2 things

            //process database stuff and provide info on landmark
            //store landmark name description and image in respective vars
            //var dbCon = new DatabaseConnect();
            //dbCon.provideLandmarkInfoToDB(state.ClientID, name, description, image);

            sizeName = (short)ASCIIEncoding.ASCII.GetByteCount(name);
            sizeDescription = (short)ASCIIEncoding.ASCII.GetByteCount(description);
            //imagesize here
            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(BitConverter.GetBytes(sizeName));
                state.ClientSocket.Send(ASCIIEncoding.ASCII.GetBytes(name));
                state.ClientSocket.Send(BitConverter.GetBytes(sizeDescription));
                state.ClientSocket.Send(ASCIIEncoding.ASCII.GetBytes(description));
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
            //var dbCon = new DatabaseConnect();
            //dbCon.provideColosseumInfoToDB(state.ClientID, name, description, image);

            sizeName = (short)ASCIIEncoding.ASCII.GetByteCount(name);
            sizeDescription = (short)ASCIIEncoding.ASCII.GetByteCount(description);

            lock (state.WRITE_LOCK)
            {
                state.ClientSocket.Send(BitConverter.GetBytes(sizeName));
                state.ClientSocket.Send(ASCIIEncoding.ASCII.GetBytes(name));
                state.ClientSocket.Send(BitConverter.GetBytes(sizeDescription));
                state.ClientSocket.Send(ASCIIEncoding.ASCII.GetBytes(description));
            }
        }

        private static void processBattleReq(ComState state)
        {
            byte[] outUpdate = new byte[33];
            outUpdate[0] = 0;
            Array.Copy(state.ClientID.ToByteArray(), 0, outUpdate, 1, 16);
            state.ClientSocket.Receive(outUpdate, 17, 16, 0);
            ComState opponent;
            if(players.TryGetValue(getUpdateID(state.Update), out opponent))
            {
                lock(opponent.WRITE_LOCK)
                {
                    opponent.ClientSocket.Send(outUpdate, 17, 0);
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
        }

        private static Guid getUpdateID(byte[] update)
        {
            byte[] temp = new byte[16];
            Array.Copy(update, temp, 16);
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
        public readonly object WRITE_LOCK = new object();


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
