//using UnityEngine;
//using UnityEngine.UI;
//using System.Collections;
//using System.Collections.Generic;
//using System.Xml;
//using System.Xml.Serialization;
//using System.IO;
//using System;
//using System.Text;

//public class LeaderboardScroll : MonoBehaviour {
//    public class Player
//    {
//        public string name { get; set; }
//        public string faction { get; set; }
//        public int level { get; set; }
//        public int score { get; set; }
//        public override string ToString()
//        {
//            return "NAME: " + name + "   FACTION: " + faction + "\nLEVEL: " + level + " | SCORE: " + score + "\n";
//        }
//    }

//    private bool loaded = false;
//    private bool error = false;
//    //public List<Player> players;
//    StringBuilder localLeaderboard = new StringBuilder();
//    StringBuilder paragonList = new StringBuilder();
//    StringBuilder slayerList = new StringBuilder();
//    StringBuilder hunterList = new StringBuilder();
//    public Text leaderboardText;
//    public GameObject scrollview;
//    public Button ParagonButton, SlayerButton, HunterButton, Local_Global;
//    XmlNodeList player;

//    //For scroll view
//    Vector2 scrollPosition = Vector2.zero;

//    //Set the URL in the inspector....
//    public string url = "http://132.160.49.90:7001/xmltest.php";

//    void Start()
//    {
        
//        WWW www = new WWW(url);
//        StartCoroutine(WaitForRequest(www));
        
//    }
//    void Update()
//    {
//        Local_Global.onClick.AddListener(delegate { showList(localLeaderboard); });
//        ParagonButton.onClick.AddListener(delegate { showList(paragonList); });
//        SlayerButton.onClick.AddListener(delegate { showList(slayerList); });
//        HunterButton.onClick.AddListener(delegate { showList(hunterList); });
//    }

//    IEnumerator WaitForRequest(WWW www)
//    {
//        yield return www;

//        // check for errors
//        if (www.error == null)
//        {
//            Debug.Log("WWW Ok!: " + www.text);
//            loaded = true;
         
//            XmlDocument xmlDoc = new XmlDocument(); // xmlDoc is the new xml document.
//            xmlDoc.LoadXml(www.text); // load the file.
//            player = xmlDoc.GetElementsByTagName("player"); // array of the level nodes.
//            setLocalLeaderboard(player);
//            setFactionList(player, paragonList, "Paragon");
//            setFactionList(player, slayerList, "Slayer");
//            setFactionList(player, hunterList, "Hunter");
//            //show local first
//            //showPlayers(player, "Paragon");
//        }
//        else
//        {
//            Debug.Log("WWW Error: " + www.error);
//        }
//    }

//    public void setLocalLeaderboard(XmlNodeList player)
//    {
//        foreach (XmlNode playerInfo in player)
//        {
//                Player p = new Player();
//                p.name = playerInfo.SelectSingleNode("name").InnerText;
//                p.faction = playerInfo.SelectSingleNode("faction").InnerText;
//                p.level = Convert.ToInt32(playerInfo.SelectSingleNode("level").InnerText);
//                p.score = Convert.ToInt32(playerInfo.SelectSingleNode("score").InnerText);
//                Debug.Log(p.ToString());
//                localLeaderboard.Append(p.ToString());
//                localLeaderboard.Append("--------------------------\n");
//        }
//    }

//    public void setFactionList(XmlNodeList player, StringBuilder factionList, string faction)
//    {
//        foreach (XmlNode playerInfo in player)
//        {
//            if (playerInfo.SelectSingleNode("faction").InnerText == faction)
//            {
//                Player p = new Player();
//                p.name = playerInfo.SelectSingleNode("name").InnerText;
//                p.faction = playerInfo.SelectSingleNode("faction").InnerText;
//                p.level = Convert.ToInt32(playerInfo.SelectSingleNode("level").InnerText);
//                p.score = Convert.ToInt32(playerInfo.SelectSingleNode("score").InnerText);
//                Debug.Log(p.ToString());
//                factionList.Append(p.ToString());
//                factionList.Append("--------------------------\n");
//            }
//        }
//    }
    
//    void showList(StringBuilder list)
//    {
       
//        leaderboardText.text = list.ToString();
//    }
//}
