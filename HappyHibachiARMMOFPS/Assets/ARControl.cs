using UnityEngine;
using System.Collections;

public class ARControl : MonoBehaviour {

    //Camera for game view
    public Camera cam;

    // Use this for initialization
    void Start () {
        //Set to main camera
        cam = Camera.main;
    }

    float yRotation = 0f;
    float xRotation = 0f;

	// Update is called once per frame
	void Update () {
        // ------------- Camera rotation based on gyroscope input ----------------

        // Grab the reference matrix for quaternions, as a base
        Quaternion referenceRotation = Quaternion.identity;
        // Get the current rotation of the phone 
        Quaternion deviceRotation = DeviceRotation.Get();

        if (Input.gyro.enabled)
        {
            yRotation += -Input.gyro.rotationRateUnbiased.y * 4f;
            xRotation += -Input.gyro.rotationRateUnbiased.x * 4f;

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
    }
}
