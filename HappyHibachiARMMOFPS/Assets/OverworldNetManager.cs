using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;
using System.Diagnostics;
using UnityEngine.SceneManagement;
using System.Text;

public class OverworldNetManager : MonoBehaviour {


    //ip address to connect to
    private static readonly IPAddress IP = IPAddress.Parse("13.84.163.243");
    //port to connect to
    public const int OVERWORLD_PORT = 6004;
    //size of update packets in bytes
    public const int UPDATE_SIZE = 8;

    private byte[] coords;

    private float latitude;
    private float longtitude;
    private bool update;
    public OnlineMaps map;
    public Canvas Land;
    public Canvas Colosseum;
    public Dropdown enemyRadar;
    List<NearbyObject> enemyList;

    //private bool upNearbyObj;
    private ManualResetEvent waitUpdate;

    private List<NearbyObject> nearbyObjects;
    public List<Texture> textures;

    private byte[] size;

    private Timer t;

    private LocationService locService;
    private LocationInfo loc;

    private bool appClosed = false;

    private static bool started = false;

    //connected socket
    private Socket client;

    //for response timing
    private Stopwatch responseTime = new Stopwatch();

    private bool restart = false;

    private void Awake()
    {
        if (started)
        {
            DestroyImmediate(gameObject);
        }
    }

    // Use this for initialization
    void Start()
    {
        if (!started)
        {
            started = true;
            UnityEngine.Debug.Log(Player.playerID);
            try
            {
                locService = Input.location;
                if (locService.status == LocationServiceStatus.Stopped)
                {
                    if (!locService.isEnabledByUser)
                    {
                        //display some sort of error message telling user location services must be enabled
                    }

                    locService.Start();

                    //isnt working so remove location startup stuff so dont have to wait
                    //MAKE SURE YOU CHANGE BACK LATER (SHOULD BE 30)
                    int timeout = 30;

                    //potentially display some sort of loading screen while waiting for location services, etc.
                    while (Input.location.status == LocationServiceStatus.Initializing && timeout > 0)
                    {
                        Thread.Sleep(1000);
                        timeout--;
                    }

                    if (timeout < 1 || Input.location.status == LocationServiceStatus.Failed)
                    {
                        //display some sort of error message, failed to start location services
                    }
                }

                //DontDestroyOnLoad(gameObject);

                coords = new byte[UPDATE_SIZE];
                size = new byte[4];

                //remote endpoint of the server
                IPEndPoint remoteEP = new IPEndPoint(IP, OVERWORLD_PORT);

                //create TCP socket
                client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                //connect to remote endpoint
                client.Connect(remoteEP);

                UnityEngine.Debug.Log("Connect Overworld Successful");

                //send the players id to the server
                client.Send(Player.playerID.ToByteArray());

                //upNearbyObj = false;
                waitUpdate = new ManualResetEvent(false);


                UnityEngine.Debug.Log("Connect Successful");


                t = new Timer(setUpdate, null, 0, 12000);

                //start receiving updates from server
                client.BeginReceive(size, 0, 4, 0, new AsyncCallback(updateDriver), null);
            }
            //catch exception if fail to connect
            catch (Exception e)
            {
                UnityEngine.Debug.Log(e.ToString());
                UnityEngine.Debug.Log("Connection Failure");
                if (!appClosed)
                {
                    started = false;
                    Start();
                }
            }
        }
    }

    //later on add mechanism to avoid redundant object sending (at least for fixed position objects), and smooth landmark/colloseums drawing since they are in a fixed position
    private void updateDriver(IAsyncResult ar)
    {
        try
        {
            //complete async data read
            if (client.EndReceive(ar) == 0)
            {
                if (!appClosed)
                {
                    started = false;
                    Start();
                }
                return;
            }

            //UnityEngine.Debug.Log(responseTime.ElapsedMilliseconds);

            int nearby = BitConverter.ToInt32(size, 0);
            int numObjects;
            byte[] numObjectsArr = new byte[4];
            client.Receive(numObjectsArr, 4, 0);
            numObjects = BitConverter.ToInt32(numObjectsArr, 0);
            byte[] buf = new byte[nearby];

            int nearbyDetailsIndex = numObjects * 24;

            //int numNearbyPlayers = 0;

            //read in each nearby items
            client.Receive(buf, nearby, 0);

            byte[] idBytes = new byte[16];

            nearbyObjects = new List<NearbyObject>();

            float lat;

            for (int i = 0; i < numObjects; i++)
            {
                NearbyObject o = new NearbyObject();

                lat = BitConverter.ToSingle(buf, i * 8);
                if (lat < 91)
                {
                    o.Type = 0;
                    o.Latitude = lat;
                    //char temp = 't';
                    int strLen = 0;
                    //name strings are null terminated, find where the players name ends
                    while(buf[nearbyDetailsIndex + strLen] != '\0')
                    {
                        strLen++;
                    }
                    o.Name = Encoding.ASCII.GetString(buf, nearbyDetailsIndex, strLen);
                    UnityEngine.Debug.Log(o.Name);
                    //add one to move past null character
                    o.FactionNo = buf[nearbyDetailsIndex + strLen + 1];
                    UnityEngine.Debug.Log(o.FactionNo);
                    o.PlayerLevel = buf[nearbyDetailsIndex + strLen + 2];
                    UnityEngine.Debug.Log(o.PlayerLevel);
                    o.FactionLevel = buf[nearbyDetailsIndex + strLen + 3];
                    UnityEngine.Debug.Log(o.FactionLevel);
                    //move the index past the current player's details
                    nearbyDetailsIndex += strLen + 4;
                    //numNearbyPlayers++;

                }
                else if (lat < 272)
                {
                    o.Type = 1;
                    o.Latitude = lat - 181;
                }
                else
                {
                    o.Type = 2;
                    o.Latitude = lat - 362;
                }


                o.Longtitude = BitConverter.ToSingle(buf, i * 8 + 4);
                Buffer.BlockCopy(buf, 8 * numObjects + i * 16, idBytes, 0, 16);
                o.Id = new Guid(idBytes);

                UnityEngine.Debug.Log(o.Id);

                /*-----------------TEMPORARY TEST CODE----------------------

                if (o.Type == 0 && o.Id != Player.playerID)
                {
                    menuScript.opponentID = o.Id;
                    UnityEngine.Debug.Log("Opponent ID set: " + o.Id);
                }

                //----------------------------------------------------------*/

                nearbyObjects.Add(o);
                //UnityEngine.Debug.Log(o.Longtitude);
            }



            //upNearbyObj = true;
            waitUpdate.Reset();
            waitUpdate.WaitOne();

            //recursively read data while battle is going
            client.BeginReceive(size, 0, 4, 0, new AsyncCallback(updateDriver), null);
        }
        catch(Exception)
        {
            //attempt to restart on failure
            if (!appClosed)
            {
                started = false;
                Start();
            }
        }

    }



    private void Update()
    {
       
        try
        {
            if (update)
            {
                //UnityEngine.Debug.Log("Test");
                loc = locService.lastData;
                latitude = loc.latitude;
                longtitude = loc.longitude;

                BitConverter.GetBytes(latitude).CopyTo(coords, 0);
                BitConverter.GetBytes(longtitude).CopyTo(coords, 4);

                client.Send(coords);

                if (responseTime.IsRunning)
                {
                    responseTime.Reset();
                }
                else
                {
                    responseTime.Start();
                }


                update = false;
            }

            if (!waitUpdate.WaitOne(0))
            {
                UnityEngine.Debug.Log(SceneManager.GetActiveScene().name);
                //place objects on screen or update position if guid already present, store respective guid with object for later use
                //objects stored in list nearbyObjects which is a list of NearbyObjects that contain lat, long, object type, and id
                //note: should check which scene is active before drawing, should be able to do that with scenemanager.getactivescene
                if (SceneManager.GetActiveScene().name.Equals("Overworld"))
                {
                    //UnityEngine.Debug.Log("???????????");

                    enemyList = new List<NearbyObject>();
                    enemyRadar.options.Clear();
                    enemyRadar.options.Add(new Dropdown.OptionData("(Select)"));
                    //enemyRadar.options.Add(new Dropdown.OptionData("Test"));
                    foreach (NearbyObject nearbyObject in nearbyObjects)
                    {
                        OnlineMapsMarker3D newMarker = new OnlineMapsMarker3D();
                        newMarker.id = nearbyObject.Id;
                        newMarker.lat = nearbyObject.Latitude;
                        newMarker.lon = nearbyObject.Longtitude;
                        //UnityEngine.Debug.Log("???????????" + nearbyObject.Type);
                        switch (nearbyObject.Type)
                        {
                            case 0:
                                //newMarker.prefab = (GameObject)Instantiate(Resources.Load("EnemyPlayer"));
                                enemyList.Add(nearbyObject);
                                break;
                            case 1:
                                //newMarker.prefab = (GameObject)Instantiate(Resources.Load("Colosseum"));
                                break;
                            case 2:
                                //newMarker.prefab = (GameObject)Instantiate(Resources.Load("Landmark"));
                                break;
                            default:
                                break;
                        }
                    }
                }

                // Add the enemies to the dropdown menu
                foreach (NearbyObject enemy in enemyList)
                {
                    if (enemy.Id != Player.playerID)
                    {
                        enemyRadar.options.Add(new Dropdown.OptionData(enemy.Id.ToString()));
                    }
                }

                waitUpdate.Set();
            }
        }
        catch(Exception) {
            //UnityEngine.Debug.Log(e.ToString());
            waitUpdate.Set();
        }

        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Began)
        {
            RaycastHit hit;
            Ray ray = Camera.main.ScreenPointToRay(Input.GetTouch(0).position);
            //Ray ray2 = Camera.main.ScreenPointToRay(Input.mousePosition);
            if (Physics.Raycast(ray, out hit))
            {
                if (hit.collider.CompareTag("Landmark"))
                {
                    Land.enabled = true;
                    GenComManager.setUpdate(4, Player.playerID);
                }
                else if (hit.collider.CompareTag("Colosseum"))
                {
                    Colosseum.enabled = true;
                }
            }
        }
    }

    private void setUpdate(object state)
    {
        update = true;
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

    private class NearbyObject
    {
        private float latitude;
        private float longtitude;
        private Guid id;
        private byte type;
        private string name;
        private byte factionNo;
        private byte playerLevel;
        private byte factionLevel;

        public float Latitude
        {
            get
            {
                return latitude;
            }

            set
            {
                latitude = value;
            }
        }

        public float Longtitude
        {
            get
            {
                return longtitude;
            }

            set
            {
                longtitude = value;
            }
        }

        public Guid Id
        {
            get
            {
                return id;
            }

            set
            {
                id = value;
            }
        }

        public byte Type
        {
            get
            {
                return type;
            }

            set
            {
                type = value;
            }
        }

        public string Name
        {
            get
            {
                return name;
            }

            set
            {
                name = value;
            }
        }

        public byte FactionNo
        {
            get
            {
                return factionNo;
            }

            set
            {
                factionNo = value;
            }
        }

        public byte PlayerLevel
        {
            get
            {
                return playerLevel;
            }

            set
            {
                playerLevel = value;
            }
        }

        public byte FactionLevel
        {
            get
            {
                return factionLevel;
            }

            set
            {
                factionLevel = value;
            }
        }
    }

}