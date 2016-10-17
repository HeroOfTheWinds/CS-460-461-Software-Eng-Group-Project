using UnityEngine;
using System.Collections;
using UnityEngine.Networking;
using UnityStandardAssets.CrossPlatformInput;

public class PlayerControl : NetworkBehaviour {


    public GameObject ARCam; // Link to the main camera
    public GameObject shotPrefab; // What does the bullet look like?

    // Movement speed for AR Off mode
    public float MoveSpeed = 5.0f; // Normal walking speed is around 1.8 m/s, up to 2.5 m/s

    // Cooldown limit for firing shots
    public float Cooldown = 0.5f; // Kinda fast, but not broken. Half a second is no gatling gun.
    private float LastShotTime; // Counter since last shot, referenced against Cooldown.

    public float RotSpeed = 10f; // Speed of camera rotation using touch

    // Use this for initialization
    void Start()
    {
        LastShotTime = Cooldown; // Make it so player can fire as soon as they're ready after scene loads
    }

    // Update is called once per frame
    void Update () {
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
        
        // Move player based on input and speed
        player.SimpleMove(new Vector3(hor, 0f, vert) * MoveSpeed);
        // This is sloppy, will use a more thought out method that includes rotation later

        // ------------- Firing shots ----------------

        // Check if player pressed Fire, and cooldown has expired
        if (CrossPlatformInputManager.GetButtonDown("Fire") && LastShotTime >= Cooldown)
        {
            // Fire a shot by instantiating a bullet and calculating with a raycast
            // First get orientation of camera
            Vector3 shotPos = ARCam.transform.position;
            Quaternion shotRot = ARCam.transform.rotation;

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

            // Adjust laser's start position so it's outside the player's collider
            shotPos.x += 1f;
            shotPos.y += -0.5f;
            shotPos.z += 1f;
                
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

        // ------------- Camera rotation based on gyroscope input ----------------

        // Grab the reference matrix for quaternions, as a base
        Quaternion referenceRotation = Quaternion.identity;
        // Get the current rotation of the phone 
        Quaternion deviceRotation = DeviceRotation.Get();

        // Rotate camera based on gyroscope
        ARCam.transform.rotation = deviceRotation;

        // ------------- Alternate camera controls: swipe to rotate --------------

        float rotX = CrossPlatformInputManager.GetAxis("Mouse X");
        float rotY = CrossPlatformInputManager.GetAxis("Mouse Y");
        
        ARCam.transform.Rotate(new Vector3(rotY * RotSpeed, -rotX * RotSpeed, 0f));
	}
}
