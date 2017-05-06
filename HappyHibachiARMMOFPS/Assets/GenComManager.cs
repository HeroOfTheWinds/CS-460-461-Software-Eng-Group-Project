using UnityEngine;
using UnityEngine.UI;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text;
using System.Collections.Generic;
using Assets;
using UnityEngine.SceneManagement;

public class GenComManager : MonoBehaviour {

    // Loading Screen
    public GameObject LoadingScreen;

    //ip address to connect to
    private static readonly IPAddress IP = IPAddress.Parse("13.84.163.243");
    //port to connect to
    public const int GC_PORT = 6003;
    //size of update packets in bytes
    public const int UPDATE_SIZE = 17;

    //0 index of update array will hold type of communication, rest is guid of interacted object
    private static byte[] update;
    private Guid id;

    private byte[] type;
    private byte[] data;

    private static readonly object UPDATE_LOCK = new object();
    private static readonly object TIMEOUT_LOCK = new object();
    private Timer timeout;

    private byte outAccepted;
    private static bool inAccepted;
    private LandmarkInfo landmarkInfo;
    private ColloseumInfo colloseumInfo;
    private List<byte> itemInfo;
    private BattleInfo battleInfo;
    public Canvas items;
    private static Guid questID = new Guid();

    private ManualResetEvent processed;

    private bool ackReceived;

    //connected socket
    private Socket client;

    private bool appClosed = false;
    private static bool started = false;

    public static Guid getQuestID()
    {
        return questID;
    }

    //call on event where player touches something
    //for type 5 com (battle response) objID should be the ID of the player that challenged me (stored in BattleNetManager.OpponentID)
    public static void setUpdate(byte type, Guid objID)
    {
        lock(UPDATE_LOCK)
        {
            update[0] = type;
            Buffer.BlockCopy(objID.ToByteArray(), 0, update, 1, 16);
        }
    }

    public static void respondMatch(bool response)
    {
        inAccepted = response;
    }

    private void Awake()
    {
        if (started)
        {
            DestroyImmediate(gameObject);
        }
        else
        {
            ItemList.populateItemList();
        }
    }

    // Use this for initialization
    void Start () {
        if (!started)
        {
            items = items.GetComponent<Canvas>();

            try
            {
                //DontDestroyOnLoad(gameObject);

                //remote endpoint of the server
                IPEndPoint remoteEP = new IPEndPoint(IP, GC_PORT);

                //create TCP socket
                client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                //connect to remote endpoint
                client.Connect(remoteEP);
                //send the players id to the server
                client.Send(Player.playerID.ToByteArray());

                Debug.Log("Connect GenCom Successful");

                update = new byte[UPDATE_SIZE];
                update[0] = 255;
                ackReceived = false;
                type = new byte[1];
                landmarkInfo = new LandmarkInfo();
                colloseumInfo = new ColloseumInfo();
                battleInfo = new BattleInfo();
                processed = new ManualResetEvent(true);

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
    }

    private void updateDriver(IAsyncResult ar)
    {
        try
        { 
            //need to deal with multiple things at once
            //eg a battle request while in a colloseum
            //send some sort of busy signal if cannot accept


            if(client.EndReceive(ar) == 0)
            {
                return;
            }

            byte[] infoBuf;
            //size of incoming data in bytes
            byte[] sizebuf = new byte[2];
            short size;

            switch (type[0])
            {
                case 0:
                    //what will be sent? one thing will be battleID
                    //if accept remember to set battleID in battlenetmanager before connecting
                    //add flags that indicate user not available to battle (send a 3), respond availability first

                    infoBuf = new byte[16];
                    client.Receive(infoBuf, 16, 0);
                    BattleNetManager.OpponentID = new Guid(infoBuf);
                    client.Receive(infoBuf, 16, 0);
                    BattleNetManager.BattleID = new Guid(infoBuf);

                    //for now just tell user they have been challenged
                    //later add challenge details

                    processed.Reset();
                    processed.WaitOne();
                    break;
                case 1:
                    client.Receive(sizebuf, 0, 2, 0);
                    size = BitConverter.ToInt16(sizebuf, 0);
                    infoBuf = new byte[size];
                    client.Receive(infoBuf, 0, size, 0);
                    colloseumInfo.Name = Encoding.ASCII.GetString(infoBuf);
                    client.Receive(sizebuf, 0, 2, 0);
                    size = BitConverter.ToInt16(sizebuf, 0);
                    infoBuf = new byte[size];
                    client.Receive(infoBuf, 0, size, 0);
                    colloseumInfo.Description = Encoding.ASCII.GetString(infoBuf);

                    //receive info relevant to colloseum (rankings, etc)
                    //for now just name and description

                    processed.Reset();
                    processed.WaitOne();
                    break;
                case 2:
                    client.Receive(sizebuf, 0, 2, 0);
                    size = BitConverter.ToInt16(sizebuf, 0);
                    infoBuf = new byte[size];
                    client.Receive(infoBuf, 0, size, 0);
                    landmarkInfo.Name = Encoding.ASCII.GetString(infoBuf);
                    client.Receive(sizebuf, 0, 2, 0);
                    size = BitConverter.ToInt16(sizebuf, 0);
                    infoBuf = new byte[size];
                    client.Receive(infoBuf, 0, size, 0);
                    landmarkInfo.Description = Encoding.ASCII.GetString(infoBuf);
                    client.Receive(sizebuf, 0, 2, 0);
                    size = BitConverter.ToInt16(sizebuf, 0);
                    infoBuf = new byte[size];
                    client.Receive(infoBuf, 0, size, 0);
                    //add image stored in infobuf to landMarkInfo when implemented (right now nothing there, size always 0)

                    processed.Reset();
                    processed.WaitOne();
                    break;
                case 3:
                    processed.Reset();
                    processed.WaitOne();
                    break;
                case 4:
                    client.Receive(sizebuf, 0, 2, 0);
                    size = BitConverter.ToInt16(sizebuf, 0);
                    infoBuf = new byte[size];
                    client.Receive(infoBuf, 0, size, 0);
                    itemInfo = new List<byte>(infoBuf);
                    processed.Reset();
                    processed.WaitOne();
                    break;
                case 5:
                    lock(TIMEOUT_LOCK)
                    {
                        ackReceived = true;
                    }
                    infoBuf = new byte[16];
                    //get battle id first
                    client.Receive(infoBuf, 16, 0);
                    Guid battleID = new Guid(infoBuf);

                    client.Receive(infoBuf, 1, 0);
                    //check if expected battle id and set outAccepted accordingly
                    outAccepted = battleID == BattleNetManager.BattleID ? infoBuf[0] : (byte)3;
                    processed.Reset();
                    processed.WaitOne();
                    break;
                default:
                    Debug.Log("Unexpected type: " + type[0]);
                    break;
            }

            client.BeginReceive(type, 0, 1, 0, new AsyncCallback(updateDriver), null);
        }
        catch(Exception)
        {
        }
    }

    // Update is called once per frame
    void Update() {
        try
        {
            lock (UPDATE_LOCK)
            {
                //when object touched, pack 
                if (update[0] < 255)
                {
                    //Debug.Log("Update detected: " + update[0].ToString());
                    client.Send(update, UPDATE_SIZE, 0);

                    if (update[0] == 0)
                    {
                        Guid battleID = Guid.NewGuid();
                        BattleNetManager.BattleID = battleID;
                        BattleNetManager.OpponentID = getUpdateID(update);
                        client.Send(battleID.ToByteArray());
                        //one second wait period before starting timer should account for network latency
                        timeout = new Timer(setTimeout, null, 1000, 15000);

                        //might want to display timer to screen
                    }
                    else if (update[0] == 5)
                    {
                        byte[] ack = new byte[1];
                        byte[] temp = new byte[17];
                        Array.Copy(BattleNetManager.BattleID.ToByteArray(), temp, 16);
                        temp[16] = inAccepted ? (byte)1 : (byte)0;
                        client.Send(temp, 17, 0);
                        if(inAccepted)
                        {
                            //get acknowledgment that opponent still connected
                            client.Receive(ack, 1, 0);
                            if(ack[0] == 1)
                            {
                                //send user to battle scene
                                update[0] = 255;
                                //SceneManager.LoadScene("Battle");
                                LoadingScreen.GetComponent<SceneLoader>().LoadScene("Battle");
                            }
                            else
                            {

                                //indicate that opponent no longer available

                            }
                        }
                    }
                }
                update[0] = 255;

            }

            if (!processed.WaitOne(0))
            {
                //Debug.Log(type[0] + "------------------------------------");
                //before drawing stuff make sure they are in the expected screen (eg dont draw landmark info over the overworld map if they exit out of it before processed)
                switch (type[0])
                {
                    case 0:
                        //tell user they have been challenged and ask if accept
                        //display for 15 seconds
                        //store whether they accept or not using method respondMatch(bool accepted)
                        //after user responds to match, call setUpdate(5, BattleNetManager.OpponenetID)
                        //if user accepted the request switch to the battle scene (will add final stage of verification both users connecting later)
                        //details of battle will be implemented later

                        //-----------------TEMPORARY TEST CODE----------------------

                        Debug.Log("Received Match request");
                        respondMatch(true);
                        setUpdate(5, BattleNetManager.OpponentID);

                        //----------------------------------------------------------

                        break;
                    case 1:
                        //draw colloseum info to screen, stored in colloseumInfo
                        Debug.Log("Colloseum name: " + colloseumInfo.Name);
                        Debug.Log("Colloseum description: " + colloseumInfo.Name);
                        break;
                    case 2:
                        //draw landmark info to screen, stored in landmarkInfo
                        //this gui.box thingy doesnt seem to do anything
                        //GUI.Box(new Rect(0, 0, Screen.width, Screen.height), landmarkInfo.Name);
                        Debug.Log("Colloseum name: " + landmarkInfo.Name);
                        Debug.Log("Colloseum description: " + landmarkInfo.Name);

                        break;
                    case 3:
                        //Debug.Log("quest?????????????????????????????????");
                        GameObject canvas = GameObject.FindGameObjectWithTag("wantedCanvas");
                        //Debug.Log(canvas);
                        WantedNotice notice = (WantedNotice)canvas.GetComponent(typeof(WantedNotice));
                        
                        //only set the quest if there is a target available
                        if (OverworldNetManager.getQuestEnemy(out notice.level, out notice.name, out questID))
                        {
                            canvas.SetActive(true);
                        }
                        break;
                    case 4:
                        //show user items they received, stored in itemInfo (list of byte item ids)
                        //use ItemList.getDestails(byte id) to get an ItemDetails object containing the items name and a path to its image
                        //for now can just put name or something
                        //if itemInfo is empty, display try again later message (can't get items)
                        //place items in users inventory (even if not drawn to screen), can store as a mapping of the items id and the number held or something like that

                        //-----------------TEMPORARY TEST CODE----------------------

                        items.enabled = true;
                        Debug.Log("Received Items");
                        ItemDetails receivedItem;
                        string info = "";
                        foreach (byte item in itemInfo)
                        {
                            receivedItem = ItemList.getDetails(item);
                            info += receivedItem.Name + "\n";
                            Debug.Log("Received Item: " + receivedItem.Name);
                        }
                        items.GetComponentInChildren<Text>().text = info;

                        //----------------------------------------------------------

                        break;
                    case 5:
                        Debug.Log("Received Match response");
                        if (outAccepted == 1)
                        {
                            //pause for two seconds to give user time to comprehend what's going on in case ack received quickly
                            Thread.Sleep(2000);
                            //send user to battle scene
                            //SceneManager.LoadScene("Battle");
                            LoadingScreen.GetComponent<SceneLoader>().LoadScene("Battle");
                        }
                        else if (outAccepted == 0)
                        {
                            //what happens if match declined?
                            //prolly just display notice match declined for now
                        }
                        else
                        {
                            //should anything happen if an old request comes through?
                            //do nothing for now
                        }
                        break;
                    default:
                        Debug.Log("Unexpected type");
                        break;
                }

                processed.Set();
            }
        }
        catch(Exception e) {
            Debug.Log(e);
        }
	}

    private static Guid getUpdateID(byte[] update)
    {
        byte[] temp = new byte[16];
        Array.Copy(update, 1, temp, 0, 16);
        return new Guid(temp);
    }

    private void setTimeout(object state)
    {
        lock(TIMEOUT_LOCK)
        {
            if (!ackReceived)
            {
                BattleNetManager.BattleID = new Guid();
                timeout.Dispose();
                timeout = null;

                //tell user the battle request timed out
            }
        } 
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

    /*
    private void battleAck(IAsyncResult ar)
    {
        if(accepted[0] == 0)
        {
            //what happens if match declined?
            //prolly just display notice match declined for now
        }
        else
        {
            //change to battle scene
        }
    }
    */

    //contains info needed to populate landmark
    private class LandmarkInfo
    {
        private string description;
        private string name;
        //something to put picture in, for some reason System.Drawing isnt working?

        public string Description
        {
            get
            {
                return description;
            }

            set
            {
                description = value;
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
    }

    //contains info needed to populate colloseum
    private class ColloseumInfo
    {
        private string description;
        private string name;
        //something to put picture in, for some reason System.Drawing isnt working?

        public string Description
        {
            get
            {
                return description;
            }

            set
            {
                description = value;
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
    }

    //contains info needed to populate colloseum
    private class BattleInfo
    {
        //what information should be told to the user when challenged?

    }
}
