using UnityEngine;
using System.Collections;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Diagnostics;

public class BattleNetManager : MonoBehaviour
{
    //temp guid until dynamic ones are generated on battle start
    private static Guid battleID = new Guid("dddddddddddddddddddddddddddddddd");
    private static Guid opponentID;

    //ip address to connect to
    private static readonly IPAddress IP = IPAddress.Parse("132.160.49.90");
    //port to connect to
    public const int BATTLE_PORT = 7004;
    //size of update packets in bytes
    public const int UPDATE_SIZE = 41;

    //possible spawn locations
    private static readonly Spawn[] spawns = new Spawn[2]
    {
        new Spawn(new Vector3(0, 0, 0), Quaternion.Euler(0, 0, 0)),
        new Spawn(new Vector3(0, 0, 12), Quaternion.Euler(0, 180, 0))
    };

    
    private static readonly object UPDATE_LOCK = new object();
    private static readonly object I_FLAG_LOCK = new object();
    //public static readonly object O_FLAG_LOCK = new object();

    //buffer to receive whether incoming message is a client ack or enemy update
    private byte[] isClient;

    //buffer for receiving which spawn to use
    private byte[] spawn;

    //buffer to receive incoming updates
    private byte[] update;

    private ManualResetEvent readUpdate;
    private ManualResetEvent updateFin;

    //connected socket
    private Socket client;
    //local player
    private GameObject player;
    //opponent
    private GameObject opponent;
    //player components
    private PlayerControl controller;
    private PlayerStatus pstatus;
    private PlayerStatus estatus;
    //stores incoming updates and pushes updates for the enemy
    private EnemyUpdate eUpdate;
    //signal to main thread that an update has been prepared
    private bool receiveUpdate;
    //signal that an update should be sent
    private bool sendUpdate;

    private Stopwatch responseTime = new Stopwatch();

    public static Guid BattleID
    {
        get
        {
            return battleID;
        }

        set
        {
            //battleID = value;
        }
    }

    public static Guid OpponentID
    {
        get
        {
            return opponentID;
        }

        set
        {
            opponentID = value;
        }
    }


    // Use this for initialization
    void Start()
    {
        try
        {
            //remote endpoint of the server
            IPEndPoint remoteEP = new IPEndPoint(IP, BATTLE_PORT);

            //create TCP socket
            client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            //connect to remote endpoint
            client.Connect(remoteEP);

            //initializations
            isClient = new byte[1];
            spawn = new byte[1];
            update = new byte[UPDATE_SIZE];

            UnityEngine.Debug.Log("Connect Successful");

            player = GameObject.FindGameObjectWithTag("Player");
            opponent = GameObject.FindGameObjectWithTag("Enemy");
            controller = player.GetComponent<PlayerControl>();
            pstatus = player.GetComponent<PlayerStatus>();
            estatus = opponent.GetComponent<PlayerStatus>();

            receiveUpdate = false;
            sendUpdate = false;

            readUpdate = new ManualResetEvent(false);
            updateFin = new ManualResetEvent(true);

            eUpdate = new EnemyUpdate();

            //send the battle guid to the client in order to connect to opponent
            client.Send(BattleID.ToByteArray());

            //gets which spawn to use
            client.Receive(spawn, 1, 0);
            //get player spawn info from index of player given by server
            player.transform.position = spawns[spawn[0]].SpawnPos;
            player.transform.rotation = spawns[spawn[0]].SpawnRot;
            //spawn opponent at the other location
            opponent.transform.position = spawns[(spawn[0] + 1) % 2].SpawnPos;
            opponent.transform.rotation = spawns[(spawn[0] + 1) % 2].SpawnRot;

            

            //Debug.Log("Send GUID Successful");

            //initial update
            client.Send(getUpdate());
            
            //Debug.Log("Send Update Successful");

            //start receiving updates from server
            client.BeginReceive(isClient, 0, 1, 0, new AsyncCallback(updateDriver), null);
            //Debug.Log("Start async successful");
        }
        //catch exception if fail to connect
        catch (Exception e)
        {
            UnityEngine.Debug.Log(e.ToString());
            UnityEngine.Debug.Log("Connection Failure");
        }
    }


    private void updateDriver(IAsyncResult ar)
    {
        //complete async data read
        client.EndReceive(ar);

        UnityEngine.Debug.Log(responseTime.ElapsedMilliseconds);

        //send updates until both players verify the battle is over, then disconnect
        if(!(eUpdate.BattleEnd && controller.BattleEnd))
        {
            //Debug.Log("begin update");
            //Debug.Log(isClient[0]);

            //check if message from server is an acknowledgement or an update from the enemy
            if (isClient[0] == 1)
            {
                //Debug.Log("ack");
                //getClient.Set();

                //need to delegate to main thread in order to update Unity objects
                //indicate an new update should be sent
                lock(UPDATE_LOCK)
                {
                    sendUpdate = true;
                }
                
                
            }
            else
            {
                //make sure previous update has completed before overwriting
                //add a buffer later for increased performance over phone networks with high jitter
                updateFin.WaitOne();
                updateFin.Reset();
                //asynchronously reveive and handle new update
                client.BeginReceive(update, 0, UPDATE_SIZE, 0, new AsyncCallback(unpackUpdate), null);
                //wait until data read before reading more data
                readUpdate.WaitOne();
                readUpdate.Reset();

            }
            //recursively read data while battle is going
            client.BeginReceive(isClient, 0, 1, 0, new AsyncCallback(updateDriver), null);
            //Debug.Log("end update");
        }
        else
        {
            // Release the socket.
            client.Shutdown(SocketShutdown.Both);
            client.Close();
            UnityEngine.Debug.Log("Disconnected");
            
        }


        
    }



    private void unpackUpdate(IAsyncResult ar)
    {
        //Debug.Log("begin unpack");
        //complete reading data
        client.EndReceive(ar);
        //signal update read has completed
        readUpdate.Set();

        //first byte contains flags
        byte flags = update[0];
        //ensure not overwriting an update being performed (probably unnecessary due to updateFin, just to be safe)
        lock (I_FLAG_LOCK)
        {
            //unpack update into EnemyUpdate object
            eUpdate.BattleEnd = (flags & 1) == 1 ? true : false;
            eUpdate.Win = ((flags >> 1) & 1) == 1 ? true : false;
            eUpdate.Sf = ((flags >> 2) & 1) == 1 ? true : false;
            eUpdate.Hpr = ((flags >> 3) & 1) == 1 ? true : false;
            eUpdate.Mp = ((flags >> 4) & 1) == 1 ? true : false;
            eUpdate.Mso = ((flags >> 5) & 1) == 1 ? true : false;
            eUpdate.Phit = ((flags >> 6) & 1) == 1 ? true : false;

            eUpdate.XPos = BitConverter.ToSingle(update, 1);
            eUpdate.ZPos = BitConverter.ToSingle(update, 5);
            eUpdate.Rot = BitConverter.ToSingle(update, 9);
            eUpdate.Sfx = BitConverter.ToSingle(update, 13);
            eUpdate.Sfz = BitConverter.ToSingle(update, 17);
            eUpdate.Sfrx = BitConverter.ToSingle(update, 21);
            eUpdate.Sfry = BitConverter.ToSingle(update, 25);
            eUpdate.Sfrz = BitConverter.ToSingle(update, 29);
            eUpdate.Mpx = BitConverter.ToSingle(update, 33);
            eUpdate.Mpz = BitConverter.ToSingle(update, 37);

            //signal to main thread to update opponent
            lock (UPDATE_LOCK)
            {
                receiveUpdate = true;
            }
        }
        
        //Debug.Log("end unpack");
    }



    private void testCallback(IAsyncResult ar)
    {
        UnityEngine.Debug.Log("Callback success, damn unity");
    }

    private void Update()
    {
        //make sure update flags aren't toggled between use and reset
        lock (UPDATE_LOCK)
        {
            //Debug.Log(client.IsBound);
            //ensure update isn't overwritten
            lock(I_FLAG_LOCK)
            {
                //run update if available
                if (receiveUpdate)
                {
                    //run the stored update on the opponent
                    eUpdate.runUpdate(controller, opponent);

                    //Debug.Log("Update run");

                    //signal that the update has been run
                    updateFin.Set();

                }
            }
            
            if (sendUpdate)
            {
                try
                {
                    //send current information on player position
                    client.Send(getUpdate());
                    //Debug.Log("Update sent");

                    if (responseTime.IsRunning)
                    {
                        responseTime.Reset();
                    }
                    else
                    {
                        responseTime.Start();
                    }
                }
                //catch exception if opponent disconnects
                catch (Exception) { }

                //reset flags
                reset();
            }
            
            //reset flags
            receiveUpdate = false;
            sendUpdate = false;
        }
        
    }


    //reset update flags
    private void reset()
    {
        controller.Sf = false;
        controller.Hpr = false;
        controller.Mp = false;
        controller.Mso = false;
        controller.Ehit = false;
    }

    
    //get update for this player
    //what happened since last update?
    public byte[] getUpdate()
    {
        //array to store update
        byte[] up = new byte[UPDATE_SIZE];
        //players current position
        float xPos = player.transform.position.x;
        float zPos = player.transform.position.z;
        //players rotation
        float rot = player.transform.rotation.eulerAngles.y;

        //Debug.Log(rot);

        //can send less data in certain cases, deal with this later
        //set up flags in first byte
        up[0] = setFlags();
        //populate update
        BitConverter.GetBytes(xPos).CopyTo(up, 1);
        BitConverter.GetBytes(zPos).CopyTo(up, 5);
        BitConverter.GetBytes(rot).CopyTo(up, 9);

        BitConverter.GetBytes(controller.Sfx).CopyTo(up, 13);
        BitConverter.GetBytes(controller.Sfz).CopyTo(up, 17);
        BitConverter.GetBytes(controller.Sfrx).CopyTo(up, 21);
        BitConverter.GetBytes(controller.Sfrx).CopyTo(up, 25);
        BitConverter.GetBytes(controller.Sfrx).CopyTo(up, 29);

        BitConverter.GetBytes(controller.Mpx).CopyTo(up, 33);
        BitConverter.GetBytes(controller.Mpz).CopyTo(up, 37);

        //Debug.Log(BitConverter.ToSingle(up, 9));

        return up;
    }

    //set up flags for update
    private byte setFlags()
    {
        byte flags = 0;
        //convert flags into a byte
        if (controller.BattleEnd) flags += 1;
        if (controller.Win) flags += (1 << 1);
        if (controller.Sf) flags += (1 << 2);
        if (controller.Hpr) flags += (1 << 3);
        if (controller.Mp) flags += (1 << 4);
        if (controller.Mso) flags += (1 << 5);
        if (controller.Ehit) flags += (1 << 6);

        return flags;
    }
}

