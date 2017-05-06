using UnityEngine;
using System.Collections;
using UnityStandardAssets.CrossPlatformInput;
using System;
using System.Net;
using System.Net.Sockets;

public class PlayerControl : MonoBehaviour {


    public static readonly object ACTION_LOCK = new object();
    //no need stop other motion while processing mine colliders (after setting them)
    public static readonly object MINE_LOCK = new object();

    //public GameObject ARCam; // Link to the main camera
    public GameObject shotPrefab; // What does the bullet look like?
    public GameObject minePrefab; // Object containing the landmine
    public GameObject exitButton;       // Button to exit the Battle Scene

    //Sound Effects
    public AudioSource FireSFX; //sound to play when shot is fired
    public AudioSource enemyHitSFX; //sound to play when enemy is hit
    public AudioSource otherHitSFX; //sound for other objects being hit
    public AudioSource winJingle; //jingle for win
    public AudioSource loseJingle; //jingle for lost

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

    // Prefab with the particle system for a sparks flying effect when hit
    public GameObject SparkSys;
    // Prefab with the part. sys. for a cloud of dust where a bullet hits
    public GameObject DustSys;

    // Variable to store the animator component used to control animation transitions
    public Animator animator;

    // Some variables to store key stats about players after retrieval
    private int attack; // Player's attack stat
    private int defense; // Player's defense stat
    private int e_defense; // Foe's defense stat

    //flags, least sig to most sig bit
    //is the battle over?
    //private bool battleEnd = false;
    //did the player win? (unused)
    //private bool win = false;

    //was a shot fired?
    private bool sf = false;
    //was hp regenerated?
    private bool hpr = false;
    //was a mine placed?
    private bool mp = false;
    //was a mine set off (by me)?
    private bool mso = false;
    //did a shot hit the player?
    private bool ehit = false;
    //did a set off mine hit the opponent as well (player reports when they set off a mine)
    private bool mho = false;

    //where was the player when it fired the shot?
    //x and z coords
    private float sfx = 0;
    private float sfz = 0;
    //camera rotation
    private float sfrx = 0;
    private float sfry = 0;
    private float sfrz = 0;
    //where was the player when it placed the mine?
    private float mpx = 0;
    private float mpz = 0;



    // Use this for initialization
    void Start()
    {
        //Set to main camera
        cam = Camera.main;
        //Set offset to head of player
        offset = new Vector3(0f, 2.8f, 0f);

        // Retrieve some stats
        attack = gameObject.GetComponent<PlayerStatus>().getAttack();
        defense = gameObject.GetComponent<PlayerStatus>().getDefense();
        Cooldown = gameObject.GetComponent<PlayerStatus>().getAtkSpeed();
        LastShotTime = Cooldown; // Make it so player can fire as soon as they're ready after scene loads
    }

    float yRotation = 0f;
    float xRotation = 0f;
            
    // Update is called once per frame
    void Update () {
        //dont want player doing things while updating networking
        lock (ACTION_LOCK)
        {
            // Add time since last frame to LastShotTime to advance the cooldown
            LastShotTime += Time.deltaTime;

            // Get player's CharacterController so we can move them around
            CharacterController player = GetComponent<CharacterController>();

            // Get touch joystick input as horizontal and vertical components
            float hor = CrossPlatformInputManager.GetAxis("Horizontal");
            float vert = CrossPlatformInputManager.GetAxis("Vertical");

            // Set running state based on input
            animator.SetFloat("Moving", Mathf.Abs(vert));

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
                yRotation += -Input.gyro.rotationRateUnbiased.y * 2.5f;
                xRotation += -Input.gyro.rotationRateUnbiased.x * 2.5f;

                cam.transform.eulerAngles = new Vector3(xRotation, yRotation, 0f);

                // Rotate Camera based on gyroscope (more free)
                //cam.transform.rotation = deviceRotation;

                // Rotate the player's Y axis to match the camera's
                Quaternion newRot = transform.rotation;
                Vector3 euler = newRot.eulerAngles;
                //euler.y = deviceRotation.eulerAngles.y;
                euler.y = cam.transform.eulerAngles.y;
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
                FireSFX.Play();

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
                            e_defense = hit.collider.gameObject.GetComponent<EnemyStatus>().getDefense();
                            float damage = attack - e_defense;
                            if (damage < 0)
                                damage = 0f;
                            hit.collider.gameObject.GetComponent<EnemyStatus>().TakeHP(damage);
                            ehit = true;
                            Debug.Log("Hit enemy");
                            // Spawn some sparks to let you know you hit them
                            GameObject sparks = Instantiate(SparkSys, hit.point, Quaternion.identity);
                            // And play sfx
                            enemyHitSFX.Play();
                            break;
                        default:
                            // Other cases to consider: wall, arena border, ground
                            // Spawn a cloud of dust
                            GameObject dust = Instantiate(DustSys, hit.point, Quaternion.identity);
                            // play hit other sfx
                            otherHitSFX.Play();
                            break;
                    }
                }

                // Change origin of shot to make it look like it's coming from the gun
                //shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;

                // Instantiate shot where camera is
                makeShot(shotPos, shotRot, endPoint);

                // Reset time since last shot to enforce cooldown
                LastShotTime = 0f;

                // Animate the player
                animator.SetTrigger("FireT");
            }
        }
    }

    

    // Function to try placing a landmine where the player stands
    // Won't work if player has none in inventory
    //returns landmine object
    public void PlaceLandmine(Vector3 position, Quaternion rotation, int placerID)
    {
        
        // Instantiate a landmine at specified location
        GameObject mine = Instantiate(minePrefab, position, rotation);

        // Tell the Landmine who placed it so it doesn't instantly blow up in their face (literally)
        // We're using the instance ID so that it is a unique identifier
        mine.GetComponent<Landmine>().placer = placerID;
        //for coord
        mine.GetComponent<Landmine>().MineOrderID = ++Landmine.NumMinesPlaced;

    }

    public void DisplayLoss()
    {
        //REMOVE WHEN DONE

        //UnityEngine.SceneManagement.SceneManager.LoadScene("Overworld");

        //////////////////////


        // Battle was lost, so create a lose screen overlay
        GameObject loss = Instantiate(LoseCanvas);
        // and play lose jingle
        loseJingle.Play();

        // Set it to render over the local Main Camera
        loss.GetComponent<Canvas>().worldCamera = Camera.main;

        // Bring up the exit button
        exitButton.SetActive(true);
    }

    public void DisplayWin()
    {
        // Battle was won, so create a win screen overlay
        GameObject win = Instantiate(WinCanvas);
        // and play win jingle
        winJingle.Play();

        // Set it to render over the local Main Camera
        win.GetComponent<Canvas>().worldCamera = Camera.main;

        if(GenComManager.getQuestID() == BattleNetManager.OpponentID)
        {
            GenComManager.setUpdate(3, BattleNetManager.OpponentID);
        }

        // Bring up the exit button
        exitButton.SetActive(true);
    }

    //used to draw enemy shots
    public void makeShot(Vector3 shotPos, Quaternion shotRot, Vector3 endPoint)
    {
        GameObject shot = Instantiate(shotPrefab, shotPos, shotRot);

        // Re-orient shot to travel toward hit point, if applicable
        if (endPoint != Vector3.zero)
        {
            shot.transform.LookAt(endPoint);
        }
    }

    //temp function for hitting player
    public void hit(float e_attack)
    {
        float damage = e_attack - defense;
        if (damage < 0)
            damage = 0f;
        gameObject.GetComponent<PlayerStatus>().TakeHP(damage);
        animator.SetTrigger("Damage");
    }


    //getters and setters
    /*
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
    */
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

    public bool Mho
    {
        get
        {
            return mho;
        }

        set
        {
            mho = value;
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
