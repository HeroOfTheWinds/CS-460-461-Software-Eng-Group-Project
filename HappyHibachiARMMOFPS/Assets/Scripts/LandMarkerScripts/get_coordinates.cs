using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class get_coordinates : MonoBehaviour
{
    //string x = "5", y = "6";
    private void Start()
    {
        Input.location.Start();
    }   
    private void Update()
    {
        UpdateLocation();
    }

    public void UpdateLocation()
    {
        if (Input.location.isEnabledByUser)
        {
            float lat = Input.location.lastData.latitude;
            float lon = Input.location.lastData.longitude;

            Text DisplayLat = GameObject.Find("Lat").GetComponent<Text>();
            Text DisplayLon = GameObject.Find("Lon").GetComponent<Text>();

            DisplayLat.text = lat.ToString();
            DisplayLon.text = lon.ToString();
            //Input.location.Stop();
        }
        else
        {
            Text DisplayLat = GameObject.Find("Lat").GetComponent<Text>();
            Text DisplayLon = GameObject.Find("Lon").GetComponent<Text>();
            DisplayLat.text = "null";
            DisplayLon.text = "null";
        }
    }

    public void StartGPS()
    {
        Input.location.Stop();
    }
    public void StopGPS()
    {
        Input.location.Stop();
    }
}