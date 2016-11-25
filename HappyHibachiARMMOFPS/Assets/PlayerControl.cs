using UnityEngine;
using System.Collections;
using UnityStandardAssets.CrossPlatformInput;
using System;
using System.Net;
using System.Net.Sockets;

public class PlayerControl : MonoBehaviour {


    //public GameObject ARCam; // Link to the main camera
    public GameObject shotPrefab; // What does the bullet look like?
    public GameObject minePrefab; // Object containing the landmine

    //Camera for game view
    public Camera cam;
    //Camera offset
    public Vector3 offset;

    // Movement speed for AR Off mode
    public float MoveSpeed = 5.0f; // Normal walking speed is around 1.8 m/s, up to 2.5 m/s

    // Cooldown limit for firing shots
    public float Cooldown = 0.5f; // Kinda fast, but not broken. Half a second is no gatling gun.
    private float LastShotTime; // Counter since last shot, referenced against Cooldown.

    public float RotSpeed = 75f; // Speed of camera rotation using touch, degrees/sec

    // Prefabs containing win and lose canvas graphics to display at end of battle
    public GameObject WinCanvas;
    public GameObject LoseCanvas;


    //flags, least sig to most sig bit
    private bool battleEnd = false;
    private bool win = false;

    private bool sf = false;
    private bool hpr = false;
    private bool mp = false;
    private bool mso = false;

    private float sfx = 0;
    private float sfz = 0;
    private float sfr = 0;

    private float mpx = 0;
    private float mpy = 0;



    // Use this for initialization
    void Start()
    {
        LastShotTime = Cooldown; // Make it so player can fire as soon as they're ready after scene loads
        //Set to main camera
        cam = Camera.main;
        //Set offset to head of player
        offset = new Vector3(0f, 2.8f, 0f);
        
    }
    /*
    // Called when player gets spawned on the server.  Only affects local player
    public override void OnStartLocalPlayer()
    {
        Camera.main.transform.rotation = transform.rotation;

        // Set the camera to render the HUD to
        // Find the HUD among children first
        Canvas HUD = gameObject.GetComponentInChildren<Canvas>();

        // Set camera
        HUD.worldCamera = Camera.main;
    }
    */
    // Update is called once per frame
    void Update () {

        // Add time since last frame to LastShotTime to advance the cooldown
        LastShotTime += Time.deltaTime;

        // Get player's CharacterController so we can move them around
        CharacterController player = GetComponent<CharacterController>();

        // Get touch joystick input as horizontal and vertical components
        float hor = CrossPlatformInputManager.GetAxis("Horizontal");
        float vert = CrossPlatformInputManager.GetAxis("Vertical");

        // Move player based on input and speed
        player.SimpleMove(transform.forward * vert * MoveSpeed);
        player.SimpleMove(transform.right * hor * MoveSpeed);

        //Move camera with player
        cam.transform.position = transform.position + offset;

        // ------------- Camera rotation based on gyroscope input ----------------

        // Grab the reference matrix for quaternions, as a base
        Quaternion referenceRotation = Quaternion.identity;
        // Get the current rotation of the phone 
        Quaternion deviceRotation = DeviceRotation.Get();

        if (Input.gyro.enabled)
        {
            // Rotate Camera based on gyroscope (more free)
            cam.transform.rotation = deviceRotation;

            // Rotate the player's Y axis to match the camera's
            Quaternion newRot = transform.rotation;
            Vector3 euler = newRot.eulerAngles;
            euler.y = deviceRotation.eulerAngles.y;
            newRot.eulerAngles = euler;
            transform.rotation = newRot;
            // This wouldn't be so wasteful if Unity let you actually edit returned quaternions directly
        }

        // ------------- Alternate camera controls: swipe to rotate --------------

        float rotX = CrossPlatformInputManager.GetAxis("CamHorizontal");
        
        // Only rotate on Y axis (Prevents camera clip issues)
        //cam.transform.Rotate(new Vector3(0f, -rotX * RotSpeed * Time.deltaTime, 0f));
        transform.Rotate(new Vector3(0f, -rotX * RotSpeed * Time.deltaTime, 0f));
        
        // ------------- Firing shots ----------------

        // Check if player pressed Fire, and cooldown has expired
        if (CrossPlatformInputManager.GetButtonDown("Fire") && LastShotTime >= Cooldown)
        {
            sf = true;
            sfx = transform.position.x;
            sfz = transform.position.z;
            sfr = transform.rotation.y;

            // Fire a shot by instantiating a bullet and calculating with a raycast
            // First get orientation of camera and adjust laser's start position so it's outside the player's collider
            Vector3 shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;
            Quaternion shotRot = cam.transform.rotation;

            // Make a raycast from the camera to check for target hit
            RaycastHit hit; // Var to store info on what got hit
            // Location in world space of the ray's endpoint
            Vector3 endPoint = Vector3.zero;

            // Test if the raycast hits anything
            if (Physics.Raycast(shotPos, transform.forward, out hit, 200f))
            {
                // Retrieve endpoint
                endPoint = hit.point;
                
                // Check what we hit and act accordingly
                switch (hit.collider.tag)
                {
                    case "Player":
                        // Get that player's stats and take off some HP
                        hit.collider.gameObject.GetComponent<EnemyStatus>().TakeHP(8f);
                        Debug.Log("Hit player");
                        break;
                    default:
                        // Other cases to consider: wall, arena border, ground
                        break;
                }
            }

            // Change origin of shot to make it look like it's coming from the gun
            //shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;

            // Instantiate shot where camera is
            GameObject shot = (GameObject) Instantiate(shotPrefab, shotPos, shotRot);

            // Re-orient shot to travel toward hit point, if applicable
            if (endPoint != Vector3.zero)
            {
                shot.transform.LookAt(endPoint);
            }
            
            // Reset time since last shot to enforce cooldown
            LastShotTime = 0f;
        }
    }

    // Function to check how many of a given item the player has in their inventory
    // If they do, return true to proceed using the item, and tell server to remove one from inventory
    public bool CheckUseItem(string name)
    {
        // Code to connect to server goes here

        // Checks if item is available
        if (true)
        {
            // Tell server to decrement amount held by one

            // Return true to allow user to use the item
            return true;
        }
        else
        {
            // User doesn't have the item
            return false;
        }
    }

    // Function to try placing a landmine where the player stands
    // Won't work if player has none in inventory
    public void PlaceLandmine()
    {
        // First check if the player has any landmines
        if (CheckUseItem("Landmine"))
        {
            // Instantiate a landmine at the player's feet
            GameObject mine = (GameObject) Instantiate(minePrefab, transform.position, transform.rotation);

            // Tell the Landmine who placed it so it doesn't instantly blow up in their face (literally)
            // We're using the instance ID so that it is a unique identifier
            mine.GetComponent<Landmine>().placer = gameObject.GetInstanceID();

            return;
        }
        else
        {
            // No good, return empty handed
            return;
        }
    }

    public void DisplayLoss()
    {
        // Battle was lost, so create a lose screen overlay
        GameObject loss = (GameObject)Instantiate(LoseCanvas);

        // Set it to render over the local Main Camera
        loss.GetComponent<Canvas>().worldCamera = Camera.main;
    }

    public void DisplayWin()
    {
        // Battle was won, so create a win screen overlay
        GameObject win = (GameObject)Instantiate(WinCanvas);

        // Set it to render over the local Main Camera
        win.GetComponent<Canvas>().worldCamera = Camera.main;
    }

    //getters and setters
    public bool BattleEnd
    {
        get
        {
            return battleEnd;
        }

        set
        {
            battleEnd = value;
        }
    }

    public bool Win
    {
        get
        {
            return win;
        }

        set
        {
            win = value;
        }
    }

    public bool Sf
    {
        get
        {
            return sf;
        }

        set
        {
            sf = value;
        }
    }

    public bool Hpr
    {
        get
        {
            return hpr;
        }

        set
        {
            hpr = value;
        }
    }

    public bool Mp
    {
        get
        {
            return mp;
        }

        set
        {
            mp = value;
        }
    }

    public bool Mso
    {
        get
        {
            return mso;
        }

        set
        {
            mso = value;
        }
    }

    public float Sfx
    {
        get
        {
            return sfx;
        }

        set
        {
            sfx = value;
        }
    }

    public float Sfz
    {
        get
        {
            return sfz;
        }

        set
        {
            sfz = value;
        }
    }

    public float Sfr
    {
        get
        {
            return sfr;
        }

        set
        {
            sfr = value;
        }
    }

    public float Mpx
    {
        get
        {
            return mpx;
        }

        set
        {
            mpx = value;
        }
    }

    public float Mpy
    {
        get
        {
            return mpy;
        }

        set
        {
            mpy = value;
        }
    }
}
