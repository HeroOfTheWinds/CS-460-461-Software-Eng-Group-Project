using UnityEngine;
using System.Collections;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

public class GenComManager : MonoBehaviour {

    //ip address to connect to
    private static readonly IPAddress IP = IPAddress.Parse("132.160.49.90");
    //port to connect to
    public const int GC_PORT = 1234;
    //size of update packets in bytes
    public const int UPDATE_SIZE = 17;

    //0 index of update array will hold type of communication, rest is guid of interacted object
    byte[] update;
    Guid id;

    byte[] type;
    byte[] data;

    byte[] accepted;

    //connected socket
    private Socket client;

    // Use this for initialization
    void Start () {

        try
        {
            //remote endpoint of the server
            IPEndPoint remoteEP = new IPEndPoint(IP, GC_PORT);

            //create TCP socket
            client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            //connect to remote endpoint
            client.Connect(remoteEP);

            Debug.Log("Connect Successful");

            update = new byte[UPDATE_SIZE];
            type = new byte[1];
            accepted = new byte[1];

            //Debug.Log("Send Update Successful");

            //start receiving updates from server
            client.BeginReceive(type, 0, 1, 0, new AsyncCallback(updateDriver), null);
            //Debug.Log("Start async successful");
        }
        //catch exception if fail to connect
        catch (Exception e)
        {
            Debug.Log(e.ToString());
            Debug.Log("Connection Failure");
        }
    }

    private void updateDriver(IAsyncResult ar)
    {
        //need to deal with multiple things at once
        //eg a battle request while in a colloseum
        //send some sort of busy signal if cannot accept

        switch(type[0])
        {
            case 0:
                //what will be sent? one thing will be battleID
                //display prompt asking if player accepts battle request
                //if accept remember to set battleID in battlenetmanager before connecting
                break;
            case 1:
                //receive info relevant to colloseum (rankings, etc)
                break;
            case 2:
                //receive info relevant to landmark (items received, etc)
                break;
            case 3:
                //what is the message being sent from server (special com)
                break;
        }
    }

    // Update is called once per frame
    void Update () {

        //need to add mechanism to make sure multiple requests arent sent before finalized with server, primarily for the battle portion

        if (update[0] < 255)
        {
            client.Send(update, UPDATE_SIZE, 0);
        }

        if (update[0] == 0)
        {
            Guid battleID = new Guid(); 
            BattleNetManager.BattleID = battleID;
            client.Send(battleID.ToByteArray());
            client.BeginReceive(accepted, 0, 1, 0, new AsyncCallback(battleAck), null);
        }
        update[0] = 255;

	}

    private void battleAck(IAsyncResult ar)
    {
        if(accepted[0] == 0)
        {
            //what happens if match declined?
        }
        else
        {
            //change to battle scene
        }
    }
}
