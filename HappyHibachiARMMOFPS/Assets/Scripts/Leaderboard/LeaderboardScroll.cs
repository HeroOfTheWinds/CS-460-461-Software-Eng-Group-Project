using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System.Xml;
using System.Xml.Serialization;
using System.IO;
using System;
using System.Text;

public class LeaderboardScroll : MonoBehaviour {
    public class Player
    {
        public override string ToString()
        {
            return "NAME: " + name + "   FACTION: " + faction + "\nLEVEL: " + level + " | SCORE: " + score + "\n";
        }

        public string name { get; set; }

        public string faction { get; set; }
        public int level { get; set; }
        public int score { get; set; }
        
    }

    private bool loaded = false;
    private bool error = false;
    //public List<Player> players;
    StringBuilder buildLeaderboard = new StringBuilder();
    
    public Text leaderboardText;

    //For scroll view
    Vector2 scrollPosition = Vector2.zero;

    //Set the URL in the inspector....
    public string url = "http://132.160.49.90:7001/xmltest.php";

    void Start()
    {
        WWW www = new WWW(url);
        StartCoroutine(WaitForRequest(www));
    }

    IEnumerator WaitForRequest(WWW www)
    {
        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
            loaded = true;
         
            XmlDocument xmlDoc = new XmlDocument(); // xmlDoc is the new xml document.
            xmlDoc.LoadXml(www.text); // load the file.
            XmlNodeList player = xmlDoc.GetElementsByTagName("player"); // array of the level nodes.
            foreach (XmlNode playerInfo in player)
            {
                Player p = new Player();
                p.name = playerInfo.SelectSingleNode("name").InnerText;
                p.faction = playerInfo.SelectSingleNode("faction").InnerText;
                p.level = Convert.ToInt32(playerInfo.SelectSingleNode("level").InnerText);
                p.score = Convert.ToInt32(playerInfo.SelectSingleNode("score").InnerText);
                Debug.Log(p.ToString());
                buildLeaderboard.Append(p.ToString());
                buildLeaderboard.Append("--------------------------\n");
            }
            leaderboardText.text = buildLeaderboard.ToString();
        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
        }
    }
}
