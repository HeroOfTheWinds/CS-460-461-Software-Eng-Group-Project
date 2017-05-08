using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine;

public class WantedNotice : MonoBehaviour {

    // public vars relating to the objects we want to change
    public Canvas notice;
    public string opname;
    public int level;
    public Text targetName;
    public Text targetLevel;

	// Use this for initialization
	void Start () {
        // Create vars to store info from server
        opname = "";
        level = 0;

        // Get current location
        Input.location.Start();
        double lat, lon;
        if (Input.location.isEnabledByUser)
        {
            lat = Input.location.lastData.latitude;
            lon = Input.location.lastData.longitude;
        }


        // Update the info on the notification
        targetName.text = opname;
        targetLevel.text = level.ToString();
	}
	
	// Update is called once per frame
	void Update () {
        // Update the info on the notification
        targetName.text = opname;
        targetLevel.text = level.ToString();
    }

    // Function to close the view
    public void CloseNotice()
    {
        notice.enabled = false;
    }
}
