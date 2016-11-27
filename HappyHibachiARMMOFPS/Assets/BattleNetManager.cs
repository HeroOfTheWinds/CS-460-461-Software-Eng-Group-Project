using UnityEngine;
using System.Collections;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

public class BattleNetManager : MonoBehaviour
{
    
    private static Guid testGUID = new Guid("dddddddddddddddddddddddddddddddd");
    private static readonly IPAddress testIP = IPAddress.Parse("10.10.10.103");
    public const int BATTLE_PORT = 2224;
    public const int UPDATE_SIZE = 33;

    //buffer to receive whether incoming message is a client ack or enemy update
    private byte[] isClient;

    //buffer to receive incoming updates
    private byte[] update;

    //private ManualResetEvent getClient = new ManualResetEvent(false);

    //double check for race conditions on data, ensure everything resynced
    private Socket client;
    private GameObject player;
    private GameObject opponent;
    private PlayerControl controller;
    private EnemyUpdate eUpdate;
    //signal to main thread that an update has been prepared
    private bool receiveUpdate;
    private bool sendUpdate;


    // Use this for initialization
    void Start()
    {
        try
        {
            IPEndPoint remoteEP = new IPEndPoint(testIP, BATTLE_PORT);

            // Create a TCP/IP socket.
            client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            // Connect to the remote endpoint.
            client.Connect(remoteEP);

            isClient = new byte[1];
            update = new byte[UPDATE_SIZE];

            Debug.Log("Connect Successful");

            player = GameObject.FindGameObjectWithTag("Player");
            opponent = GameObject.FindGameObjectWithTag("Enemy");
            controller = player.GetComponent<PlayerControl>();

            receiveUpdate = false;
            sendUpdate = false;

            eUpdate = new EnemyUpdate();

            client.Send(testGUID.ToByteArray());

            //Debug.Log("Send GUID Successful");

            //initial update
            client.Send(getUpdate());
            
            //Debug.Log("Send Update Successful");

            client.BeginReceive(isClient, 0, 1, 0, new AsyncCallback(updateDriver), null);
            //Debug.Log("Start async successful");
        }

        catch (Exception e)
        {
            Debug.Log(e.ToString());
            Debug.Log("Connection Failure");
        }
    }


    private void updateDriver(IAsyncResult ar)
    {
        client.EndReceive(ar);

        //if says something about not being able to access enemy transform outside main thread, send signal to main thread and update in Update function
        if(!controller.BattleEnd)
        {

            
            
            //getClient.WaitOne();
            if (isClient[0] == 1)
            {
                //Debug.Log("ack");
                //getClient.Set();


                //need to delegate to main thread
                sendUpdate = true;
                
            }
            else
            {
                //Debug.Log("update");
                /*
                State state = new State();
                state.ClientSocket = client;
                state.Update = new byte[UPDATE_SIZE];

                state.Player = player;
                state.Enemy = GameObject.FindGameObjectWithTag("Enemy");
                */

                //MIGHT NEED TO BLOCK ON THESE CALLS UNTIL FINISHED READING (ANY SIMILAR CASE)
                //Debug.Log("What?");

                //SOMETHING WEIRD WITH THE SERVER ON DC (not properly shutting down socket?)

                client.BeginReceive(update, 0, UPDATE_SIZE, 0, new AsyncCallback(unpackUpdate), null);
                

            }
            //recursively check isClient while battle is going
            client.BeginReceive(isClient, 0, 1, 0, new AsyncCallback(updateDriver), null);
        }
        else
        {
            // Release the socket.
            client.Shutdown(SocketShutdown.Both);
            client.Close();
            Debug.Log("Disconnected");
        }


        
    }



    private void unpackUpdate(IAsyncResult ar)
    {
        client.EndReceive(ar);

        byte flags = update[0];

        //unpack update into EnemyUpdate object
        eUpdate.BattleEnd = (flags & 1) == 1 ? true : false;
        eUpdate.Win = ((flags >> 1) & 1) == 1 ? true : false;
        eUpdate.Sf = ((flags >> 2) & 1) == 1 ? true : false;
        eUpdate.Hpr = ((flags >> 3) & 1) == 1 ? true : false;
        eUpdate.Mp = ((flags >> 4) & 1) == 1 ? true : false;
        eUpdate.Mso = ((flags >> 5) & 1) == 1 ? true : false;

        eUpdate.XPos = BitConverter.ToSingle(update, 1);
        eUpdate.ZPos = BitConverter.ToSingle(update, 5);
        eUpdate.Rot = BitConverter.ToSingle(update, 9);
        eUpdate.Sfx = BitConverter.ToSingle(update, 13);
        eUpdate.Sfz = BitConverter.ToSingle(update, 17);
        eUpdate.Sfr = BitConverter.ToSingle(update, 21);
        eUpdate.Mpx = BitConverter.ToSingle(update, 25);
        eUpdate.Mpy = BitConverter.ToSingle(update, 29);

        //signal to main thread to update opponent
        receiveUpdate = true;
    }



    private void testCallback(IAsyncResult ar)
    {
        Debug.Log("Callback success, damn unity");
    }

    private void Update()
    {
        if(sendUpdate)
        {
            //send current information on player position
            client.Send(getUpdate());
            Debug.Log("Update sent");
            //reset flags
            //reset();
        }
        if(receiveUpdate)
        {
            //run the stored update on the opponent
            eUpdate.runUpdate(opponent);
            Debug.Log("Update run");
        }
        receiveUpdate = false;
        sendUpdate = false;
    }

    /*
    private void dataHandle(IAsyncResult ar)
    {
       
        //getClient.Reset();
        client.Receive(isClient, 1, 0);
        dataHandle(client);
        Debug.Log("Receive Successful");
        //getClient.WaitOne();
        client.EndReceive(ar);
        if (isClient[0] == 1)
        {
            Debug.Log("isClient Successful");
            //getClient.Set();
            client.Send(getUpdate());
            reset();
        }
        else
        {
            //State state = new State();
            //state.ClientSocket = client;
            //state.Update = new byte[UPDATE_SIZE];

            //GameObject player = GameObject.FindGameObjectWithTag("Player");
            //PlayerControl controller = player.GetComponent<PlayerControl>();

            //state.Player = player;
            //state.Enemy = GameObject.FindGameObjectWithTag("Enemy");

            //receiveUpdate
            client.BeginReceive(update, 0, UPDATE_SIZE, 0, new AsyncCallback(updateOpponent), update);

        }

    }
    
    //why do you need this?
    private void reset()
    {
        controller.Sf = false;
        controller.Hpr = false;
        controller.Mp = false;
        controller.Mso = false;
    }
    

    private void updateOpponent()
    {
        //State state = (State)ar.AsyncState;
        //ar.AsyncWaitHandle.WaitOne();
        //state.ClientSocket.EndReceive(ar);

        //getClient.Set();

        unpackUpdate(state);
        //state.Enemy.GetComponent<EnemyUpdate>().runUpdate();
    }

    */

    
    
    public byte[] getUpdate()
    {
        byte[] sendUpdate = new byte[UPDATE_SIZE];
        //GameObject player = GameObject.FindGameObjectWithTag("Player");
        //PlayerControl controller = player.GetComponent<PlayerControl>();
        float xPos = player.transform.position.x;
        float zPos = player.transform.position.z;
        float rot = player.transform.rotation.y;

        //can send less data in certain cases, deal with this later, much later
        sendUpdate[0] = setFlags();

        BitConverter.GetBytes(xPos).CopyTo(sendUpdate, 1);
        BitConverter.GetBytes(zPos).CopyTo(sendUpdate, 5);
        BitConverter.GetBytes(rot).CopyTo(sendUpdate, 9);

        BitConverter.GetBytes(controller.Sfx).CopyTo(sendUpdate, 13);
        BitConverter.GetBytes(controller.Sfz).CopyTo(sendUpdate, 17);
        BitConverter.GetBytes(controller.Sfr).CopyTo(sendUpdate, 21);

        BitConverter.GetBytes(controller.Mpx).CopyTo(sendUpdate, 25);
        BitConverter.GetBytes(controller.Mpy).CopyTo(sendUpdate, 29);


        return sendUpdate;
    }

    private byte setFlags()
    {
        byte flags = 0;

        if (controller.BattleEnd) flags += 1;
        if (controller.Win) flags += (1 << 1);
        if (controller.Sf) flags += (1 << 2);
        if (controller.Hpr) flags += (1 << 3);
        if (controller.Mp) flags += (1 << 4);
        if (controller.Mso) flags += (1 << 5);

        return flags;
    }
}

/*
internal class State
{
    private GameObject player;
    private GameObject enemy;

    private Socket clientSocket;
    // Receive buffer.
    private byte[] update;

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

    public GameObject Player
    {
        get
        {
            return player;
        }

        set
        {
            player = value;
        }
    }

    public GameObject Enemy
    {
        get
        {
            return enemy;
        }

        set
        {
            enemy = value;
        }
    }
}
    */

