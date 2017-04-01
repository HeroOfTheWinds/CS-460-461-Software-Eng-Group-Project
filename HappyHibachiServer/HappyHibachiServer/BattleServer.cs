using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    //server for running battles
    public class BattleServer
    {
        //size of updates in bytes
        public static readonly int UPDATE_SIZE = 41;
        //port to listen on
        public const int BATTLE_PORT = 7004;
        //server ip address
        public static readonly IPAddress IP = IPAddress.Parse("10.0.0.4");

        //signal for connections
        private static ManualResetEventSlim connectionFound = new ManualResetEventSlim();

        //stores waiting connections until second player connects
        private static Dictionary<Guid, WaitConnection> waiting = new Dictionary<Guid, WaitConnection>();

        private static readonly object DICT_LOCK = new object();


        public static void startServer()
        {

            IPEndPoint localEndPoint = new IPEndPoint(IP, BATTLE_PORT);

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
            //connection finished, allow others to connect
            connectionFound.Set();

            Console.WriteLine("\nPlayer connected");
            //hold data about the incoming request
            WaitConnection wait;
            byte[] guid = new byte[16];
            Guid battleGUID;
            //tell player which spawn to use
            byte[] spawn = new byte[1];
            //stores items for communications
            BattleState state = new BattleState();

            //get socket for client
            Socket listener = (Socket)ar.AsyncState;
            Socket handler = listener.EndAccept(ar);
            try
            {
                byte[] id = new byte[16];
                handler.Receive(id, 16, 0);
                state.ClientID = new Guid(id);
            }
            //separate try catches so second portion not hit until clientid set
            catch(Exception)
            {
                try
                {
                    //try to cleanly close connection
                    handler.Shutdown(SocketShutdown.Both);
                    handler.Close();
                }
                catch (Exception) { }
            }

            try
            {
                //retrieve player level from the db and use to create player stats
                state.ClientStats = new PlayerStats(new DatabaseConnect().getPlayerLevel(state.ClientID));

                ClientState addSocket;
                lock (ConnectedPlayers.DICTIONARY_LOCK)
                {
                    if (ConnectedPlayers.playerDetails.TryGetValue(state.ClientID, out addSocket))
                    {
                        addSocket.BattleSocket = handler;
                    }
                    else
                    {
                        addSocket = new ClientState(null, null, handler, null);
                        ConnectedPlayers.playerDetails.Add(state.ClientID, addSocket);
                    }
                }


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
                    //if this is the first player, add the guid to the dictionary with a waitconnection object holding this client's socket
                    if (!inDict)
                    {
                        wait = new WaitConnection(handler, state.ClientStats);
                        wait.WriteLock = new object();
                        wait.StatsLock = new object();
                        waiting.Add(battleGUID, wait);
                    }
                }
                //if this is the second client to connect
                if (inDict)
                {
                    //set second socket to this client's socket
                    wait.Socket2 = handler;
                    wait.P2 = state.ClientStats;
                    //set this client's opponent to the first client in wait
                    state.OpponentSocket = wait.Socket1;
                    state.OpponentStats = wait.P1;
                    state.WriteLock = wait.WriteLock;
                    state.StatsLock = wait.StatsLock;
                    //set event indicating both clients are connected
                    wait.setWait();
                    //indicate to client to spawn at second location
                    spawn[0] = 1;
                }
                else
                {
                    //add some sort of timeout?
                    //wait until second client connects
                    if(!wait.Wait.Wait(5000))
                    {
                        //remove from waiting list, and throw exception so connection cleaned up
                        waiting.Remove(battleGUID);
                        throw new Exception("Timeout");
                    }
                    //pass lock to both clients for writing to each others socket
                    state.WriteLock = wait.WriteLock;
                    state.StatsLock = wait.StatsLock;
                    //set this client's opponent to the second client in wait
                    state.OpponentSocket = wait.Socket2;
                    state.OpponentStats = wait.P2;
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
                //lock write operations on the socket
                lock(state.WriteLock)
                {
                    //tell the client where to spawn
                    handler.Send(spawn, 1, 0);
                }
                
                //start receiving client updates
                handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
            }
            //catch connection errors
            catch(Exception)
            {
                lock (ConnectedPlayers.DICTIONARY_LOCK)
                {
                    ClientState removeSocket;
                    if (ConnectedPlayers.playerDetails.TryGetValue(state.ClientID, out removeSocket))
                    {
                        removeSocket.BattleSocket = null;
                        try
                        {
                            //if value not found then connection should already be closed by cleanup procedure
                            handler.Shutdown(SocketShutdown.Both);
                            handler.Close();
                        }
                        catch (Exception) { }
                    }
                }
                Console.WriteLine("\nPlayer disconnected");
            }
        }

        //read updates from clients
        public static void readUpdate(IAsyncResult ar)
        {
            //retreive the state object and socket
            BattleState state = (BattleState)ar.AsyncState;
            Socket handler = state.ClientSocket;
            try
            {
                //bool battleEnd;
                //bool win;

                //NEED TO DEAL WITH SIZES AND STUFF, MAYBE SEND A MESSAGE WITH THE SIZE
                //for now everything sent as 41 bytes
                ar.AsyncWaitHandle.WaitOne();
                if (handler.EndReceive(ar) == 0)
                {
                    //ANYTHING ELSE NEED TO BE DONE ON THE SIDE OF THE PERSON WHO DCED?
                    return;
                }


                byte flags = state.Update[0];
                /*
                lock (state.StatsLock)
                {
                    if (((flags >> 5) & 1) == 1)
                    {
                        //HOW MUCH DAMAGE?
                    }
                    if (((flags >> 6) & 1) == 1)
                    {
                        state.OpponentStats.HP -= state.ClientStats.Attack - state.OpponentStats.Defense;
                    }

                }
                */
                //determine winner and if battle ended, change so can't cheat
                bool battleEnd = (flags & 1) == 1 ? true : false;
                bool win = ((flags >> 1) & 1) == 1 ? true : false;
                /*
                if (state.OpponentStats) flags += 1;
                if (controller.Win) flags += (1 << 1);
                */
                //lock write operations and pass along and acknowledge update
                lock (state.WriteLock)
                {
                    send(state);
                }
                
                //recursively read updates until the battle is over
                if (!battleEnd)
                {
                    handler.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(readUpdate), state);
                }
                else
                {
                    //handle winner stuff (DB stuff, etc)
                    if (win)
                    {
                        var dbCon = new DatabaseConnect();
                        dbCon.updatePlayerExpAfterBattle(state.ClientID);
                    }

                    //clean up
                    lock (ConnectedPlayers.DICTIONARY_LOCK)
                    {
                        ClientState removeSocket;
                        if(ConnectedPlayers.playerDetails.TryGetValue(state.ClientID, out removeSocket))
                        {
                            removeSocket.BattleSocket = null;
                            //if value not found then connection should already be closed
                            handler.Shutdown(SocketShutdown.Both);
                            handler.Close();
                        }
                    }
                    
                    Console.WriteLine("\nPlayer disconnected");
                }
            }
            //end communications gracefully if player disconnects before battle ends
            catch (Exception)
            {
                lock (ConnectedPlayers.DICTIONARY_LOCK)
                {
                    ClientState removeSocket;
                    if (ConnectedPlayers.playerDetails.TryGetValue(state.ClientID, out removeSocket))
                    {
                        removeSocket.BattleSocket = null;
                        try
                        {
                            //if value not found then connection should already be closed
                            handler.Shutdown(SocketShutdown.Both);
                            handler.Close();
                        }
                        catch(Exception) { }
                    }
                }
                Console.WriteLine("\nPlayer disconnected");
            }
        }

        //send updates and acknowledgements
        private static void send(BattleState state)
        {
            //indicate this is an update for the opponent
            byte[] isClient = new byte[1] { 0 };
            //send update to opponent
            state.OpponentSocket.Send(isClient);
            state.OpponentSocket.Send(state.Update);

            //indicate this is an acknowledgement for an update sent
            isClient[0] = 1;
            //send ack
            state.ClientSocket.Send(isClient);
        }
    }

    internal class PlayerStats
    {
        private float hp;
        private float attack;
        private float defense;
        //items that are used during the battle to be refunded if opponent disconnects
        //not currently handled since items are not stored in db at the moment
        private int healthPots;
        private int landmines;


        public PlayerStats(int playerLevel)
        {
            hp = 100 + (playerLevel - 1) * 21;
            attack = 15 + (playerLevel - 1) * 4;
            defense = 10 + (playerLevel - 1) * 3;
            healthPots = 0;
            landmines = 0;
        }

        public void landmineUsed()
        {
            landmines++;
        }

        public void healthPotUsed()
        {
            healthPots++;
        }

        public int HealthPots
        {
            get
            {
                return healthPots;
            }
        }

        public int Landmines
        {
            get
            {
                return landmines;
            }
        }

        public float HP
        {
            get
            {
                return hp;
            }

            set
            {
                hp = value;
            }
        }

        public float Attack
        {
            get
            {
                return attack;
            }

            set
            {
                attack = value;
            }
        }

        public float Defense
        {
            get
            {
                return defense;
            }

            set
            {
                defense = value;
            }
        }
    }

    //stores information about a waiting battle
    internal class WaitConnection
    {
        //lock for write operations after connection
        private object writeLock;
        private object statsLock;
        //blocks until both players connect
        private ManualResetEventSlim wait;
        //sockets of the clients in the battle
        private Socket socket1;
        private Socket socket2;

        private PlayerStats p1;
        private PlayerStats p2;

        public WaitConnection()
        {
            wait = new ManualResetEventSlim();
        }

        //set up a wait connection with the specified player socket
        public WaitConnection(Socket socket, PlayerStats stats)
        {
            wait = new ManualResetEventSlim();
            socket1 = socket;
            p1 = stats;
        }

        //getters and setters
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

        internal PlayerStats P1
        {
            get
            {
                return p1;
            }

            set
            {
                p1 = value;
            }
        }

        internal PlayerStats P2
        {
            get
            {
                return p2;
            }

            set
            {
                p2 = value;
            }
        }

        public object StatsLock
        {
            get
            {
                return statsLock;
            }

            set
            {
                statsLock = value;
            }
        }
    }


    // State object for reading client data asynchronously
    internal class BattleState
    {
        //lock for asyncronous write operations
        private object writeLock;
        private object statsLock;
        //client's socket
        private Socket clientSocket;
        //opponents socket
        private Socket opponentSocket;
        //buffer for updates
        private byte[] update;
        private Guid clientID;

        private PlayerStats clientStats;
        private PlayerStats opponentStats;

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

        internal PlayerStats ClientStats
        {
            get
            {
                return clientStats;
            }

            set
            {
                clientStats = value;
            }
        }

        internal PlayerStats OpponentStats
        {
            get
            {
                return opponentStats;
            }

            set
            {
                opponentStats = value;
            }
        }

        public object StatsLock
        {
            get
            {
                return statsLock;
            }

            set
            {
                statsLock = value;
            }
        }
    }
}
