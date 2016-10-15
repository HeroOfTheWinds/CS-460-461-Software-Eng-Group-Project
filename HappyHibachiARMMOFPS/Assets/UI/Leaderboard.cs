using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Leaderboard : MonoBehaviour {
    private string url = "http://localhost/happyhibachi/retrieveLeaderboard.php";
    public Text leaderboard;
    // Use this for initialization
    void Start () {
        getLeaderboard();
	}

    // Update is called once per frame
    public void getLeaderboard () {
        //assume www is the object and is yielded with the value from php bfore this step...
        Debug.Log("value1");
        WWW www = new WWW(url);
        //yield return www;
        string[] temp = www.text.Split("*".ToCharArray());

        // and in the temp array you can use parse to get the value back...

        string value1 = temp[0];
        Debug.Log("value1");
        leaderboard.text = value1;
        
        // now value 1 has the first value 0 in it.
    }

}
