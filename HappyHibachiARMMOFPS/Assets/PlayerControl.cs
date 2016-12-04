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
    //need to set these in the code
    private bool battleEnd = false;
    private bool win = false;

    private bool sf = false;
    private bool hpr = false;
    private bool mp = false;
    private bool mso = false;
    private bool ehit = false;

    private float sfx = 0;
    private float sfz = 0;
    private float sfrx = 0;
    private float sfry = 0;
    private float sfrz = 0;

    private float mpx = 0;
    private float mpz = 0;



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

        //never mind (keep for now just in case)
        //offset device rotation's y axis by -90 degrees because it defaults to a 90 degree rotation for some reason
        //deviceRotation.eulerAngles.Set(deviceRotation.eulerAngles.x, deviceRotation.eulerAngles.y - 90, deviceRotation.eulerAngles.z);

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
        //don't want both
        else
        {
            // ------------- Alternate camera controls: swipe to rotate --------------

            float rotX = CrossPlatformInputManager.GetAxis("CamHorizontal");

            // Only rotate on Y axis (Prevents camera clip issues)
            //cam.transform.Rotate(new Vector3(0f, -rotX * RotSpeed * Time.deltaTime, 0f));
            transform.Rotate(new Vector3(0f, -rotX * RotSpeed * Time.deltaTime, 0f));
        }
        // ------------- Firing shots ----------------

        // Check if player pressed Fire, and cooldown has expired
        if (CrossPlatformInputManager.GetButtonDown("Fire") && LastShotTime >= Cooldown)
        {
            sf = true;
            sfx = cam.transform.position.x;
            sfz = cam.transform.position.z;
            sfrx = cam.transform.forward.x; //cam.transform.rotation.eulerAngles.x;
            sfry = cam.transform.forward.y; //cam.transform.rotation.eulerAngles.y;
            sfrz = cam.transform.forward.z; //cam.transform.rotation.eulerAngles.z;

            // Fire a shot by instantiating a bullet and calculating with a raycast
            // First get orientation of camera and adjust laser's start position so it's outside the player's collider
            Vector3 shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;
            Quaternion shotRot = cam.transform.rotation;

            // Make a raycast from the camera to check for target hit
            RaycastHit hit; // Var to store info on what got hit
            // Location in world space of the ray's endpoint
            Vector3 endPoint = Vector3.zero;

            //Debug.DrawRay(cam.transform.position, cam.transform.forward*200f, Color.red, 20f, true);

            // Test if the raycast hits anything
            if (Physics.Raycast(cam.transform.position, cam.transform.forward, out hit, 200f))
            {
                // Retrieve endpoint
                endPoint = hit.point;
                
                // Check what we hit and act accordingly
                switch (hit.collider.tag)
                {
                    case "Enemy":
                        // Get that player's stats and take off some HP
                        hit.collider.gameObject.GetComponent<EnemyStatus>().TakeHP(8f);
                        ehit = true;
                        Debug.Log("Hit enemy");
                        break;
                    default:
                        // Other cases to consider: wall, arena border, ground
                        break;
                }
            }

            // Change origin of shot to make it look like it's coming from the gun
            //shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;

            // Instantiate shot where camera is
            makeShot(shotPos, shotRot, endPoint);
            
            // Reset time since last shot to enforce cooldown
            LastShotTime = 0f;
        }
    }

    

    // Function to try placing a landmine where the player stands
    // Won't work if player has none in inventory
    //returns landmine object
    public void PlaceLandmine(Vector3 position, Quaternion rotation, int placerID)
    {
        
            // Instantiate a landmine at specified location
            GameObject mine = (GameObject) Instantiate(minePrefab, position, rotation);

            // Tell the Landmine who placed it so it doesn't instantly blow up in their face (literally)
            // We're using the instance ID so that it is a unique identifier
            mine.GetComponent<Landmine>().placer = placerID;
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

    //used to draw enemy shots
    public void makeShot(Vector3 shotPos, Quaternion shotRot, Vector3 endPoint)
    {
        GameObject shot = (GameObject)Instantiate(shotPrefab, shotPos, shotRot);

        // Re-orient shot to travel toward hit point, if applicable
        if (endPoint != Vector3.zero)
        {
            shot.transform.LookAt(endPoint);
        }
    }

    //temp function for hitting player
    public void hit()
    {
        gameObject.GetComponent<PlayerStatus>().TakeHP(8f);
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

    public float Mpz
    {
        get
        {
            return mpz;
        }

        set
        {
            mpz = value;
        }
    }

    public float Sfrx
    {
        get
        {
            return sfrx;
        }

        set
        {
            sfrx = value;
        }
    }

    public float Sfry
    {
        get
        {
            return sfry;
        }

        set
        {
            sfry = value;
        }
    }

    public float Sfrz
    {
        get
        {
            return sfrz;
        }

        set
        {
            sfrz = value;
        }
    }

    public bool Ehit
    {
        get
        {
            return ehit;
        }

        set
        {
            ehit = value;
        }
    }
}

public class Spawn
{
    private Vector3 spawnPos;
    private Quaternion spawnRot;

    public Spawn(Vector3 spawnPos, Quaternion spawnRot)
    {
        this.spawnPos = spawnPos;
        this.spawnRot = spawnRot;
    }


    public Vector3 SpawnPos
    {
        get
        {
            return spawnPos;
        }

        set
        {
            spawnPos = value;
        }
    }

    public Quaternion SpawnRot
    {
        get
        {
            return spawnRot;
        }

        set
        {
            spawnRot = value;
        }
    }
}
