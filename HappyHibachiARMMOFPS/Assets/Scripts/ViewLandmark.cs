using UnityEngine;
using System.Collections;
using System;


public class ViewLandmark : MonoBehaviour {
    public double lat, lon;
    public struct location
    {
        public double lat, lon;
    }
    //public double lon, lat;
    //public LandmarkClass a;
    location CH13;
    location myLocation;
    private location landmark;
    
    public void checkproximity()
    {
        //have location be pulled from phones location
        myLocation.lat = 19.703204; //currently set to ch13
        myLocation.lon = -155.080990; //currently set to ch13

        landmark.lat = lat;
        //Debug.Log(landmark.lat);
        landmark.lon = lon;
        //Debug.Log(landmark.lon);
        isClose(myLocation, landmark);

    }
	bool isClose(location myLocation, location landmark) //need to convert to feet
    {
        var R = 6371; // km
        var dLat = ConvertToRadians(landmark.lat - myLocation.lat);
        var dLon = ConvertToRadians(landmark.lon - myLocation.lon);
        var lat1 = ConvertToRadians(myLocation.lat);
        var lat2 = ConvertToRadians(landmark.lat);

        var a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                Math.Sin(dLon / 2) * Math.Sin(dLon / 2) * Math.Cos(lat1) * Math.Cos(lat2);
        var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
        var distance = R * c;
        
        Debug.Log("Landmark is " + distance +  "km away");
        if (distance <= 1) //convert km to feet
        {
            Debug.Log("10 Exp earned.");
            return true;
        }
        else
        {
            Debug.Log("Landmark too far away.");
            return false;
        }

    }
    public double ConvertToRadians(double angle)
    {
        return (Math.PI / 180) * angle;
    }
}
