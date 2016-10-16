using UnityEngine;
using System.Collections;
using UnityEngine.Networking;
using UnityStandardAssets.CrossPlatformInput;

public class PlayerControl : NetworkBehaviour {

    private Quaternion CameraRot;

	// Use this for initialization
	void Start () {
        CameraRot = ARCam.transform.rotation;
	}

    private Vector3 dir = Vector3.zero;
    public GameObject ARCam;

    // Movement speed for AR Off mode
    public float MoveSpeed = 5.0f; // Normal walking speed is around 1.8 m/s, up to 2.5 m/s

    // Update is called once per frame
    void Update () {
        if(!isLocalPlayer)
        {
            //return;
        }

        // Get player's CharacterController so we can move them around
        CharacterController player = GetComponent<CharacterController>();

        // Get touch joystick input as horizontal and vertical components
        float hor = CrossPlatformInputManager.GetAxis("Horizontal");
        float vert = CrossPlatformInputManager.GetAxis("Vertical");
        
        // Move player based on input and speed
        player.SimpleMove(new Vector3(hor, 0f, vert) * MoveSpeed);

        // This is sloppy, will use a more thought out method that includes rotation later

        // Now rotate the camera based on gyroscope input
        // Grab the reference matrix for quaternions, as a base
        Quaternion referenceRotation = Quaternion.identity;
        // Get the current rotation of the phone 
        Quaternion deviceRotation = DeviceRotation.Get();

        // Rotate camera based on gyroscope
        ARCam.transform.rotation = deviceRotation;
	}
}
