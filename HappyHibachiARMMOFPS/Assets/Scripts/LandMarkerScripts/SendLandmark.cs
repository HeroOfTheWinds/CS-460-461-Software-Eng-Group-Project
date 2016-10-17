using UnityEngine;
using UnityEngine.UI;
using System.Collections;
//192.168.200.64
public class SendLandmark : MonoBehaviour {
    private string url = "http://132.160.49.90:7001/sendToDatabase.php";

    public Text NameString, LatString, LonString, debug;

    public void sendlandmark()
    {
        Debug.Log("sendlandmarks called");
        WWWForm form = new WWWForm();
        //check if name is not empty and less than 16
        if (NameString.text != "" && NameString.text.Length < 16)
        {
            if (LatString.text != "" && LonString.text != "")
            {
                form.AddField("NameFromUnity", NameString.GetComponent<Text>().text);
                form.AddField("LonFromUnity", LonString.GetComponent<Text>().text);
                form.AddField("LatFromUnity", LatString.GetComponent<Text>().text);
                WWW download = new WWW(url, form);
                // yield return download;
                //Debug.Log(download.error);
                if (!string.IsNullOrEmpty(download.error))
                {
                    print(download.error);
                }
                else
                {
                    print("Finished Uploading Landmark");
                }
            }
            else { debug.text = "Lat/Lon not set. Location must be on to work."; }
        }
        else
        {
            debug.text = "Name cannot be blank or more than 16 characters";
        }
    }
}
