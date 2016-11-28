using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;
using System;
using System.Text.RegularExpressions;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

public class Login : MonoBehaviour
{
    private string username, password;
    public InputField userInput, passwordInput;
    public Text ErrorText;
    private string url = "http://132.160.49.90:7001/login.php";
    private string pullPlayerDataurl = "http://132.160.49.90:7001/playerInfo.php";

    void Start()
    {
        userInput.text = Register.username;
    }

    public void checkLogin()
    {
        username = userInput.text;
        password = passwordInput.text;
        var form = new WWWForm();
        form.AddField("usernameFromUnity", username);
        form.AddField("passwordFromUnity", password);
        WWW send = new WWW(url, form);
        StartCoroutine(WaitForRequest(send));
    }

    IEnumerator WaitForRequest(WWW www)
    {
        yield return www;

        if (www.error == null)
        {
            Debug.Log("Connection good.");
            string text = Regex.Replace(www.text, @"\s", ""); //strip www.text of any whitespace
            Debug.Log(text);
            if (text == "1")
            {
                Debug.Log("Username not found.");
                ErrorText.text = "Username or password incorrect.";
            }

            if (text == "2")
            {
                Debug.Log("Password incorrect.");
                ErrorText.text = "Username or password incorrect.";
            }
            else
            {
                Debug.Log("Logged In");
                //ErrorText.text = "Welcome, " + username;
                parsePlayerInfo(text);
                SceneManager.LoadScene("PlayerStats");
            }
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }

    public void parsePlayerInfo(string text)
    {
        Player player = new Player(); // create new player to store info from server
        string[] splitString = text.Split(new string[] { "?" }, StringSplitOptions.None); //split fields on ?
        Player.playername = splitString[0];
        Player.EXP = int.Parse(splitString[1]);
        Player.level = int.Parse(splitString[2]);
        Player.faction = int.Parse(splitString[3]);
        Player.factionEXP = int.Parse(splitString[4]);
        Player.factionLevel = int.Parse(splitString[5]);
        Debug.Log(Player.playername);
        Debug.Log(Player.EXP);
    }
    //public static void savePlayer(Player player)
    //{
    //    //#if UNITY_WP8 || UNITY_WP8_1 || UNITY_WSA || UNITY_WSA_8_0 || UNITY_WSA_8_1 || UNITY_WSA_10_0
    //    //var serializer = new SharpSerializer(mode) //use sharp serializer
    //    //#else
    //    BinaryFormatter bf = new BinaryFormatter();                 //use built-in serializer
    //    //#endif

    //    FileStream stream = new FileStream(Application.persistentDataPath + "/playerInfo.hibachiproject", FileMode.Create);


    //    PlayerData data = new PlayerData(player);
    //    bf.Serialize(stream, data);
    //    stream.Close();
    //    Debug.Log("Player data successfull pulled from Database");
    //}

    public void goToRegister()
    {
        SceneManager.LoadScene("Register");
    }
}

//[Serializable]
//public class PlayerData
//{
//    public string playername;
//    public int EXP;
//    public int level;
//    public int faction;
//    public int factionEXP;
//    public int factionLevel;
//    public int[] stats;

//    public PlayerData(Player player)
//    {
//        playername = Player.playername;
//        EXP = player.EXP;
//        level = player.level;
//        faction = player.faction;
//        factionEXP = player.factionEXP;
//        factionLevel = player.factionLevel;
//        stats = new int[4];
//        stats[0] = player.maxHealthStat;
//        stats[1] = player.attackStat;
//        stats[2] = player.defenseStat;
//        stats[3] = player.shotSpeedStat;
//    }
//}
