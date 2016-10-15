using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class ARCamera : MonoBehaviour {

    public RawImage rawImage;

	public void Start()
    {
        Debug.Log("Initialize");
        /*GUITexture BackgroundTexture = gameObject.AddComponent<GUITexture>();
        BackgroundTexture.pixelInset = new Rect(0, 0, Screen.width, Screen.height);*/

        //set up camera
        WebCamDevice[] devices = WebCamTexture.devices;
        string backCamName = "";
        for (int i = 0; i < devices.Length; i++)
        {
            Debug.Log("Device:" + devices[i].name + "IS FRONT FACING:" + devices[i].isFrontFacing);

            if (!devices[i].isFrontFacing)
            {
                backCamName = devices[i].name;
            }
        }

        WebCamTexture CameraTexture = new WebCamTexture(backCamName);//, 10000, 10000, 30);
        rawImage.texture = CameraTexture;
        rawImage.material.mainTexture = CameraTexture;
        CameraTexture.Play();
    }
}
