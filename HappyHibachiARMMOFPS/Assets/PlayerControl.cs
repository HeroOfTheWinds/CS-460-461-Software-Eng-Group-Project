using UnityEngine;
using System.Collections;

public class PlayerControl : MonoBehaviour {

    private Quaternion CameraRot;

	// Use this for initialization
	void Start () {
        CameraRot = ARCam.transform.rotation;
	}

    private Vector3 dir = Vector3.zero;
    public GameObject ARCam;

    // Update is called once per frame
    void Update () {
        Debug.Log(Input.acceleration);
        /*      

        dir.x += Input.acceleration.x;
        dir.z -= Input.acceleration.z;

        dir *= Time.deltaTime;

        transform.Translate(dir);
        */

        ARCam.transform.Rotate(new Vector3(Input.acceleration.z, Input.acceleration.x, 0));//, Space.World);
	}
}
