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

    private bool appClosed = false;

    private static bool started = false;

    private void Awake()
    {
        if(started)
        {
            DestroyImmediate(gameObject);
        }
    }

    // Use this for initialization
    void Start () {
        if (!started)
        {
            started = true;
            Debug.Log("Started");
            DontDestroyOnLoad(gameObject);
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

                Debug.Log("Connect Timeout Successful");

                client.Send(update);

                Debug.Log("Update Sent");

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
    }

    private void updateDriver(IAsyncResult ar)
    {
        try
        {
            if(client.EndReceive(ar) == 0)
            {
                if (!appClosed)
                {
                    started = false;
                    Start();
                }
                return;
            }
            client.Send(update);
            client.BeginReceive(update, 0, 1, 0, new AsyncCallback(updateDriver), null);
        }
        catch(Exception e)
        {
            Debug.Log(e.ToString());
            Debug.Log("Connection Failure");
            if(!appClosed)
            {
                started = false;
                Start();
            }
        }
    }

    // Update is called once per frame
    void Update () {
        //Debug.Log("update");
	}

    private void OnApplicationQuit()
    {
        appClosed = true;
        try
        {
            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }
        catch (Exception) { }
    }
}
