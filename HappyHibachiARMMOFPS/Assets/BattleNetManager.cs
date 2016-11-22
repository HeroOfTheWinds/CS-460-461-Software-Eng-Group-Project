using UnityEngine;
using System.Collections;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

public class BattleNetManager //: ScriptableObject
{

    private static Guid testGUID = new Guid();
    private static readonly IPAddress testIP = IPAddress.Parse("127.0.0.1");
    public const int BATTLE_PORT = 2224;
    public const int UPDATE_SIZE = 33;

    private byte[] isClient;
    private ManualResetEvent getClient = new ManualResetEvent(false);

    // Use this for initialization
    void Start()
    {
        try
        {
            IPEndPoint remoteEP = new IPEndPoint(testIP, BATTLE_PORT);

            // Create a TCP/IP socket.
            Socket client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            // Connect to the remote endpoint.
            client.Connect(remoteEP);

            isClient = new byte[1];

            Debug.Log("Connect Successful");

            GameObject player = GameObject.FindGameObjectWithTag("Player");
            PlayerControl controller = player.GetComponent<PlayerControl>();

            client.Send(testGUID.ToByteArray());

            Debug.Log("Send GUID Successful");

            //initial update
            client.Send(getUpdate());

            Debug.Log("Send Update Successful");

            while (!controller.BattleEnd)
            {
                getClient.Reset();
                client.BeginReceive(isClient, 0, 1, 0, new AsyncCallback(dataHandle), client);
                Debug.Log("BeginReceive Successful");
                getClient.WaitOne();
            }
                
            // Release the socket.
            client.Shutdown(SocketShutdown.Both);
            client.Close();
            Debug.Log("Disconnected");
        }

        catch (Exception e)
        {
            Debug.Log(e.ToString());
            Debug.Log("Connection Failure");
        }
    }

    private void dataHandle(IAsyncResult ar)
    {
        Socket client = (Socket)ar.AsyncState;
        ar.AsyncWaitHandle.WaitOne();
        client.EndReceive(ar);

        if(isClient[0] == 1)
        {
            Debug.Log("isClient Successful");
            getClient.Set();
            client.Send(getUpdate());
        }
        else
        {
            State state = new State();
            state.ClientSocket = client;
            state.Update = new byte[UPDATE_SIZE];

            GameObject player = GameObject.FindGameObjectWithTag("Player");
            PlayerControl controller = player.GetComponent<PlayerControl>();

            state.Player = player;
            state.Enemy = GameObject.FindGameObjectWithTag("Enemy");
            client.BeginReceive(state.Update, 0, UPDATE_SIZE, 0, new AsyncCallback(updateOpponent), state);

        }
        

        


    }

    private void updateOpponent(IAsyncResult ar)
    {
        State state = (State)ar.AsyncState;
        ar.AsyncWaitHandle.WaitOne();
        state.ClientSocket.EndReceive(ar);

        getClient.Set();

        unpackUpdate(state);
        state.Enemy.GetComponent<EnemyUpdate>().runUpdate();
    }

    private void unpackUpdate(State state)
    {
        EnemyUpdate enemy = state.Enemy.GetComponent<EnemyUpdate>();
        //UNPACK UPDATE INTO ENEMY
    }

    //STILL NEED TO ACTUALLY UPDATE VALUES IN PLAYER CONTROLLER
    public byte[] getUpdate()
    {
        byte[] update = new byte[UPDATE_SIZE];
        GameObject player = GameObject.FindGameObjectWithTag("Player");
        PlayerControl controller = player.GetComponent<PlayerControl>();
        float xPos = player.transform.position.x;
        float zPos = player.transform.position.z;
        float rot = player.transform.rotation.y;

        //can send less data in certain cases, deal with this later, much later
        update[0] = setFlags(controller);

        BitConverter.GetBytes(xPos).CopyTo(update, 1);
        BitConverter.GetBytes(zPos).CopyTo(update, 5);
        BitConverter.GetBytes(rot).CopyTo(update, 9);

        BitConverter.GetBytes(controller.Sfx).CopyTo(update, 13);
        BitConverter.GetBytes(controller.Sfz).CopyTo(update, 17);
        BitConverter.GetBytes(controller.Sfr).CopyTo(update, 21);

        BitConverter.GetBytes(controller.Mpx).CopyTo(update, 25);
        BitConverter.GetBytes(controller.Mpy).CopyTo(update, 29);


        return update;
    }

    private byte setFlags(PlayerControl controller)
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
