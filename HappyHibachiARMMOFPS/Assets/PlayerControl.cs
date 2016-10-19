using UnityEngine;
using System.Collections;
using UnityEngine.Networking;
using UnityStandardAssets.CrossPlatformInput;

public class PlayerControl : NetworkBehaviour {


    //public GameObject ARCam; // Link to the main camera
    public GameObject shotPrefab; // What does the bullet look like?

    //Camera for game view
    public Camera cam;
    //Camera offset
    public Vector3 offset;

    // Movement speed for AR Off mode
    public float MoveSpeed = 5.0f; // Normal walking speed is around 1.8 m/s, up to 2.5 m/s

    // Cooldown limit for firing shots
    public float Cooldown = 0.5f; // Kinda fast, but not broken. Half a second is no gatling gun.
    private float LastShotTime; // Counter since last shot, referenced against Cooldown.

    public float RotSpeed = 0.0001f; // Speed of camera rotation using touch

    // Use this for initialization
    void Start()
    {
        LastShotTime = Cooldown; // Make it so player can fire as soon as they're ready after scene loads
        //Set to main camera
        cam = Camera.main;
        //Set offset to head of player
        offset = new Vector3(0f, 2.8f, 0f);
        
    }

    // Update is called once per frame
    void Update () {

        //Checks if trying to move local player, else returns (can't move other player)
        if(!isLocalPlayer)
        {
            return;
        }

        // Add time since last frame to LastShotTime to advance the cooldown
        LastShotTime += Time.deltaTime;

        // Get player's CharacterController so we can move them around
        CharacterController player = GetComponent<CharacterController>();

        // Get touch joystick input as horizontal and vertical components
        float hor = CrossPlatformInputManager.GetAxis("Horizontal");
        float vert = CrossPlatformInputManager.GetAxis("Vertical");

        //Create transform vector in world space
        Vector3 worldTransform = /*transform.TransformDirection(*/new Vector3(hor, 0f, vert);//);
        
        // Move player based on input and speed
        player.SimpleMove(transform.forward * vert * MoveSpeed);
        player.SimpleMove(transform.right * hor * MoveSpeed);

        //Move camera with player
        cam.transform.position = transform.position + offset;

        // ------------- Camera rotation based on gyroscope input ----------------

        // Grab the reference matrix for quaternions, as a base
        Quaternion referenceRotation = Quaternion.identity;
        // Get the current rotation of the phone 
        Vector3 deviceRotation = DeviceRotation.Get().eulerAngles;

        // Rotate player based on gyroscope
        transform.Rotate(new Vector3(0f, deviceRotation.y, 0f));
        // Rotate Camera based on gyroscope (more free)
        cam.transform.Rotate(deviceRotation);

        // ------------- Alternate camera controls: swipe to rotate --------------

        float rotX = CrossPlatformInputManager.GetAxis("CamHorizontal");
        
        // Only rotate on Y axis (Prevents camera clip issues)
        cam.transform.Rotate(new Vector3(0f, -rotX * RotSpeed, 0f));
        transform.Rotate(new Vector3(0f, -rotX * RotSpeed, 0f));
        
        // ------------- Firing shots ----------------

        // Check if player pressed Fire, and cooldown has expired
        if (CrossPlatformInputManager.GetButtonDown("Fire") && LastShotTime >= Cooldown)
        {
            // Fire a shot by instantiating a bullet and calculating with a raycast
            // First get orientation of camera and adjust laser's start position so it's outside the player's collider
            Vector3 shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;
            Quaternion shotRot = cam.transform.rotation;
            

            // Make a raycast from the camera to check for target hit
            RaycastHit hit; // Var to store info on what got hit
            // Location in world space of the ray's endpoint
            Vector3 endPoint = Vector3.zero;

            // Test if the raycast hits anything
            if (Physics.Raycast(shotPos, Vector3.forward, out hit, 200f))
            {
                // Retrieve endpoint
                endPoint = hit.point;

                // Check what we hit and act accordingly
                switch (hit.collider.tag)
                {
                    case "Player":
                        // Get that player's stats and take off some HP
                        hit.collider.gameObject.GetComponent<PlayerStatus>().TakeHP(8f);
                        Debug.Log("Hit player");
                        break;
                    default:
                        // Other cases to consider: wall, arena border, ground
                        break;
                }
            }

            
                
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
}
