using UnityEngine;
using System.Collections;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

public class OverworldNetManager : MonoBehaviour {


    //ip address to connect to
    private static readonly IPAddress IP = IPAddress.Parse("132.160.49.90");
    //port to connect to
    public const int OVERWORLD_PORT = 1234;
    //size of update packets in bytes
    public const int UPDATE_SIZE = 8;

    private byte[] coords;

    private float latitude;
    private float longtitude;
    private bool update;

    private byte[] size;

    private Timer t;

    private LocationService locService;
    private LocationInfo loc;


    //connected socket
    private Socket client;


    // Use this for initialization
    void Start()
    {
        try
        {
            locService = Input.location;
            if(!locService.isEnabledByUser)
            {
                //display some sort of error message telling user location services must be enabled
            }

            locService.Start();

            int timeout = 30;

            //potentially display some sort of loading screen while waiting for location services, etc.
            while(Input.location.status == LocationServiceStatus.Initializing && timeout > 0)
            {
                Thread.Sleep(1000);
                timeout--;
            }

            if(timeout < 1)
            {
                //display some sort of error message, failed to start location services
            }

            coords = new byte[UPDATE_SIZE];
            size = new byte[4];

            //remote endpoint of the server
            IPEndPoint remoteEP = new IPEndPoint(IP, OVERWORLD_PORT);

            //create TCP socket
            client = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);

            //connect to remote endpoint
            client.Connect(remoteEP);


            Debug.Log("Connect Successful");


            t = new Timer(setUpdate, null, 0, 12000);

            //start receiving updates from server
            client.BeginReceive(size, 0, 4, 0, new AsyncCallback(updateDriver), null);
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
        //complete async data read
        client.EndReceive(ar);

        int nearby = BitConverter.ToInt32(size, 0);
        int numObjects = nearby / 24;
        byte[] buf = new byte[nearby];

        //read in each nearby items
        client.Receive(buf, nearby, 0);

        float lat;
        float lon;
        byte[] idBytes = new byte[16];
        Guid id;

        for(int i = 0; i < numObjects; i++)
        {
            lat = BitConverter.ToSingle(buf, i * 8);
            lon = BitConverter.ToSingle(buf, i * 8 + 4);
            Buffer.BlockCopy(buf, 8 * numObjects + i, idBytes, 0, 16);
            id = new Guid(idBytes);

            //place object on screen or update position if guid already present, store respective guid with object
        }
            
        //recursively read data while battle is going
        client.BeginReceive(size, 0, 4, 0, new AsyncCallback(updateDriver), null);

    }



    private void Update()
    {
        if(update)
        {
            loc = locService.lastData;
            latitude = loc.latitude;
            longtitude = loc.longitude;

            BitConverter.GetBytes(latitude).CopyTo(coords, 0);
            BitConverter.GetBytes(longtitude).CopyTo(coords, 4);

            client.Send(coords);

            update = false;
        }

    }

    private void setUpdate(object state)
    {
        update = true;
    }

}
