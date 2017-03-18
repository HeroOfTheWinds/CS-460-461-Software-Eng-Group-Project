using System;
using System.Collections;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using UnityEngine;

public class TimeoutManager : MonoBehaviour {

    //ip address to connect to
    private static readonly IPAddress IP = IPAddress.Parse("13.84.163.243");
    //port to connect to
    public const int T_PORT = 6002;
    //size of update packets in bytes
    public const int UPDATE_SIZE = 1;

    //0 index of update array will hold type of communication, rest is guid of interacted object
    private static byte[] update = { 0 };

    //connected socket
    private Socket client;

    // Use this for initialization
    void Start () {

        try
        {
            //remote endpoint of the server
            IPEndPoint remoteEP = new IPEndPoint(IP, T_PORT);

            //create TCP socket
            client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            //connect to remote endpoint
            client.Connect(remoteEP);
            //send the players id to the server
            client.Send(Player.playerID.ToByteArray());

            Debug.Log("Connect GenCom Successful");

            client.Send(update);

            //start receiving updates from server
            client.BeginReceive(update, 0, 1, 0, new AsyncCallback(updateDriver), null);
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
        try
        {
            client.EndReceive(ar);
            client.Send(update);
            client.BeginReceive(update, 0, 1, 0, new AsyncCallback(updateDriver), null);
        }
        catch(Exception e)
        {
            Debug.Log(e.ToString());
            Debug.Log("Connection Failure");
            
            //TRY TO RECONNECT ON CONNECTION TIMEOUT OR FAILURE (NEED TO PUT CODE IN ALL CATCH BLOCKS FOR VARIOUS CONNECTIONS PROBABLY)
        }
    }

    // Update is called once per frame
    void Update () {
		
	}
}
