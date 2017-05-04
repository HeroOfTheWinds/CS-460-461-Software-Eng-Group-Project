using UnityEngine;
using System.Collections;

public class ARControl : MonoBehaviour {

    //Camera for game view
    public Camera cam;
    public int AndroidVer;

    // Use this for initialization
    void Start () {
        //Set to main camera
        cam = Camera.main;

        // Store the android version we're running on for later use
        AndroidVer = GetSDKLevel();
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
            // Rotate the player's Y axis to match the camera's
            Quaternion newRot = transform.rotation;
            Vector3 euler = newRot.eulerAngles;

            // Android SDK level is represented as an int
            // Android 6.0 Marshmallow is represented as 23
            // Since Unity can't use gyroscope.attitude in Android 6.0, check if we need
            // to use a clunky workaround on this device
            if (AndroidVer >= 23)
            {
                // We need to workaround this
                yRotation += -Input.gyro.rotationRateUnbiased.y * 2.5f;
                xRotation += -Input.gyro.rotationRateUnbiased.x * 2.5f;

                cam.transform.eulerAngles = new Vector3(xRotation, yRotation, 0f);
                //euler.y = cam.transform.eulerAngles.y;
            }
            else
            {
                // Use the nice solution
                // Rotate Camera based on gyroscope (more free)
                cam.transform.rotation = deviceRotation;
                //euler.y = deviceRotation.eulerAngles.y;
            }
            
            //newRot.eulerAngles = euler;
            //transform.rotation = newRot;
            // This wouldn't be so wasteful if Unity let you actually edit returned quaternions directly
        }
    }

    // Function to return which Android version the app is running on
    private static int GetSDKLevel() {
        System.IntPtr clazz = AndroidJNI.FindClass("android/os/Build$VERSION");
        System.IntPtr fieldID = AndroidJNI.GetStaticFieldID(clazz, "SDK_INT", "I");
        int sdkLevel = AndroidJNI.GetStaticIntField(clazz, fieldID);
        return sdkLevel;
    }
}
