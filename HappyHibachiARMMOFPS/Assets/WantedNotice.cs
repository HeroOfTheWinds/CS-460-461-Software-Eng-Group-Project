using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine;

public class WantedNotice : MonoBehaviour {

    // public vars relating to the objects we want to change
    public Canvas notice;
    public Text targetName;
    public Text targetLevel;

	// Use this for initialization
	void Start () {
        // Create vars to store info from server
        string name = "";
        int level = 1;

        // Get current location
        Input.location.Start();
        double lat, lon;
        if (Input.location.isEnabledByUser)
        {
            lat = Input.location.lastData.latitude;
            lon = Input.location.lastData.longitude;
        }

        // Server code here to retrieve current bounty's name and level
        // Needs to send lat and lon to get local results


        // Update the info on the notification
        targetName.text = name;
        targetLevel.text = level.ToString();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    // Function to close the view
    public void CloseNotice()
    {
        notice.enabled = false;
    }
}
