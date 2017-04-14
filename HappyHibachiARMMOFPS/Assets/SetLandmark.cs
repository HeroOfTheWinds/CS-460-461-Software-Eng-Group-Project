using UnityEngine;
using UnityEngine.UI;
using System.Collections;
//192.168.200.64
public class SetLandmark : MonoBehaviour {

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
            float lat = Input.location.lastData.latitude; //display 
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
            //testing, set back to null later
            DisplayLat.text = "";
            DisplayLon.text = "";
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
    private string url = "http://13.84.163.243/setLandmark.php";

    public Text NameString, LatString, LonString, debug;

    //send a player's landmark to database, status : unlimited, no duplicates locations
    public void sendlandmark()
    {
        Debug.Log("sendlandmarks called");
        WWWForm form = new WWWForm();
        //check if name is not empty and less than 16
        if (NameString.text != "" && NameString.text.Length < 16)
        {
            if (LatString.text != "" && LonString.text != "")
            {
                form.AddField("Name", NameString.GetComponent<Text>().text);
                form.AddField("PlayerID", Player.playername);
                form.AddField("Lon", LonString.GetComponent<Text>().text);
                form.AddField("Lat", LatString.GetComponent<Text>().text);
                WWW uploadLandmark = new WWW(url, form);
                StartCoroutine(WaitForRequest(uploadLandmark));
            }
            else
            {
                debug.text = "Location services must be on to work.";
            }
        }
        else
        {
            debug.text = "Name cannot be blank or more than 16 characters";
        }
    }

    IEnumerator WaitForRequest(WWW www)
    {
            yield return www;
            debug.text = www.error;
            if (!string.IsNullOrEmpty(www.error))
            {
                Debug.Log(www.error);
                Debug.Log(www.text);
            }
            else
            {
                Debug.Log(www.text);
                Debug.Log("Finished Uploading Landmark");
            }
    }
}
