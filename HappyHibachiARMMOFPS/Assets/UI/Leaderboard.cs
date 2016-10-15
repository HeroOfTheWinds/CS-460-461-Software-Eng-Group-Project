using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Leaderboard : MonoBehaviour {
    private string url = "http://localhost/happyhibachi/retrieveLeaderboard.php";
    public Text leaderboard;
    // Use this for initialization
    

    // Update is called once per frame
    IEnumerator Start() {
        //Access database and request leaderboard info
        WWW www = new WWW(url);
        yield return www;
        //split string on *
        //string[] temp = www.text.Split("*".ToCharArray());
        string output = www.text; 
        
        leaderboard.text = output;
        
        // now value 1 has the first value 0 in it.
    }

}
