using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System.Xml;
using System.Xml.Serialization;
using System.IO;
using System;
using System.Text;
using UnityEngine.SceneManagement;

public class Leaderboard : MonoBehaviour {
    public GameObject playerCellPrefab;
    public class PlayerCont
    {
        public int rank { get; set; }
        public string name { get; set; }
        public string faction { get; set; }
        public int level { get; set; }
        public int exp { get; set; }
        public override string ToString()
        {
            return rank + " NAME: " + name + "   FACTION: " + faction + "\nLEVEL: " + level + " | EXP: " + exp + "\n";
        }
    }
    private char currentFilter;
    private int loadmoreG = 0, loadmoreP = 0, loadmoreS = 0, loadmoreH = 0;
    private int loadamount = 25;
    private bool loaded = false;
    private bool error = false;

    //list to hold filter results
    public List<PlayerCont> globalList = new List<PlayerCont>();
    public List<PlayerCont> paragonList = new List<PlayerCont>();
    public List<PlayerCont> slayerList = new List<PlayerCont>();
    public List<PlayerCont> hunterList = new List<PlayerCont>();
    public Text leaderboardText;
    public GameObject scrollview;
    public Button ParagonButton, SlayerButton, HunterButton, Local_Global, Load_More, BackToOverWorld;
    XmlNodeList player;

    //For scroll view
    Vector2 scrollPosition = Vector2.zero;

    //set the URLs for the different filters....
    private string globalURL = "http://13.84.163.243/leaderboardGlobal.php";
    private string paragonURL = "http://13.84.163.243/leaderboardParagon.php";
    private string slayerURL = "http://13.84.163.243/leaderboardSlayer.php";
    private string hunterURL = "http://13.84.163.243/leaderboardHunter.php";

    void Start()
    {
        currentFilter = 'g';
        StartCoroutine(WaitForGlobal());
        StartCoroutine(WaitForParagon());
        StartCoroutine(WaitForSlayer());
        StartCoroutine(WaitForHunter());

        //if the global button is pressed
        Local_Global.onClick.AddListener(delegate {
            currentFilter = 'g';
            clearCells();
            if (globalList.Count == 0) //if first time using filter, grab first 25
            {
                StartCoroutine(WaitForGlobal()); //load by the load_more amount
            }
            showList(globalList);
        });

        //if the paragon button is pressed
        ParagonButton.onClick.AddListener(delegate {
            currentFilter = 'p';
            clearCells();
            if (paragonList.Count == 0) //if first time using filter, grab first 25
            {
                StartCoroutine(WaitForParagon()); //load by the load_more amount
            }
            showList(paragonList);
        });


        SlayerButton.onClick.AddListener(delegate{
            currentFilter = 's';
            clearCells();
            if (slayerList.Count == 0)
            {
                StartCoroutine(WaitForSlayer());
            }
            showList(slayerList);
        });
        HunterButton.onClick.AddListener(delegate
        {
            currentFilter = 'h';
            clearCells();
            if (hunterList.Count == 0)
            {
                StartCoroutine(WaitForHunter());
            }
            showList(hunterList);
        });

        //Load_More.onClick.AddListener(delegate {
        //    if (currentFilter == 'g')
        //    {
        //        Debug.Log("load more global players: " + loadmoreG);
        //        StartCoroutine(WaitForGlobal());
        //        showList(globalList);
        //    }
        //    if (currentFilter == 'p')
        //    {
        //        Debug.Log("load more paragons: " + loadmoreP);
        //        StartCoroutine(WaitForParagon());
        //        showList(globalList);
        //    }
        //    if (currentFilter == 'h')
        //    {
        //        Debug.Log("load more hunters: " + loadmoreH);
        //        StartCoroutine(WaitForHunter());
        //        showList(globalList);
        //    }
        //    if (currentFilter == 's')
        //    {
        //        Debug.Log("load more slayers: " + loadmoreS);
        //        StartCoroutine(WaitForSlayer());
        //        showList(globalList);
        //    }
        //});

        BackToOverWorld.onClick.AddListener(delegate
        {
            SceneManager.LoadScene("Overworld");
        });

    }
    void Update()
    {
        Debug.Log("current filter = " + currentFilter);
        Debug.Log(loaded);
        if (currentFilter == 'g')
        {
            if (!loaded)
            {
                showList(globalList);
                loaded = true;
            }
        }
        //if (currentFilter == 'p')
        //{
        //    showList(paragonList);
        //}
        //if (currentFilter == 'h')
        //{
        //    showList(hunterList);
        //}
        //if (currentFilter == 's')
        //{
        //    showList(slayerList);
        //}
    }

    //grab global leaderboard infomation
    IEnumerator WaitForGlobal()
    {
        var form = new WWWForm();
        form.AddField("load_more", loadmoreG);
  
        //send form to leaderboard.php
        WWW www = new WWW(globalURL, form);
      
        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
            loaded = true;
         
            XmlDocument xmlDoc = new XmlDocument(); // xmlDoc is the new xml document.
            xmlDoc.LoadXml(www.text.TrimStart()); // load the file.
            player = xmlDoc.GetElementsByTagName("player"); // array of the level nodes.
            setLeaderboard(player, globalList);
            loadmoreG++;
        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
        }
    }

    //grab paragon leaderboard infomation
    IEnumerator WaitForParagon()
    {
        var form = new WWWForm();
        form.AddField("load_more", loadmoreP);

        //send form to leaderboard.php
        WWW www = new WWW(paragonURL, form);

        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
            loaded = true;

            XmlDocument xmlDoc = new XmlDocument(); // xmlDoc is the new xml document.
            xmlDoc.LoadXml(www.text.TrimStart()); // load the file.
            player = xmlDoc.GetElementsByTagName("player"); // array of the level nodes.
            setLeaderboard(player, paragonList);
            loadmoreP++;
        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
        }
    }

    //grab slayer leaderboard infomation
    IEnumerator WaitForSlayer()
    {
        var form = new WWWForm();
        form.AddField("load_more", loadmoreS);

        //send form to leaderboard.php
        WWW www = new WWW(slayerURL, form);

        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
            loaded = true;

            XmlDocument xmlDoc = new XmlDocument(); // xmlDoc is the new xml document.
            xmlDoc.LoadXml(www.text.TrimStart()); // load the file.
            player = xmlDoc.GetElementsByTagName("player"); // array of the level nodes.
            setLeaderboard(player, slayerList);
            loadmoreS++;
        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
        }
    }

    IEnumerator WaitForHunter()
    {
        var form = new WWWForm();
        form.AddField("load_more", loadmoreH);

        //send form to leaderboard.php
        WWW www = new WWW(hunterURL, form);

        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
            loaded = true;

            XmlDocument xmlDoc = new XmlDocument(); // xmlDoc is the new xml document.
            xmlDoc.LoadXml(www.text.TrimStart()); // load the file.
            player = xmlDoc.GetElementsByTagName("player"); // array of the level nodes.
            setLeaderboard(player, hunterList);
            loadmoreH++;
        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
        }
    }

    public void setLeaderboard(XmlNodeList player, List<PlayerCont> List)
    {
        foreach (XmlNode playerInfo in player)
        {
                PlayerCont p = new PlayerCont();
                p.rank = Convert.ToInt32(playerInfo.SelectSingleNode("rank").InnerText);
                p.name = playerInfo.SelectSingleNode("name").InnerText;
                p.faction = playerInfo.SelectSingleNode("faction").InnerText;
                p.level = Convert.ToInt32(playerInfo.SelectSingleNode("level").InnerText);
                p.exp = Convert.ToInt32(playerInfo.SelectSingleNode("exp").InnerText);
                Debug.Log(p.ToString()+'\n');
                List.Add(p);
        }
    }

    void clearCells()
    {
        //clear leaderboard player cells in vertical scroll
        foreach (Transform child in leaderboardText.transform)
        {
            GameObject.Destroy(child.gameObject);
        }
    }

    void showList(List<PlayerCont> List)
    {
        //visit each player in list
        foreach (PlayerCont p in List)
        {
            //create new cell and assign player info
            GameObject newCell = Instantiate(playerCellPrefab) as GameObject;
            newCell.GetComponentInChildren<Text>().transform.Find("Rank").GetComponent<Text>().text = p.rank.ToString();
            newCell.GetComponentInChildren<Text>().transform.Find("Name").GetComponent<Text>().text = p.name;
            newCell.GetComponentInChildren<Text>().transform.Find("Level").GetComponent<Text>().text = "Level: " + p.level.ToString();
            newCell.GetComponentInChildren<Text>().transform.Find("Faction").GetComponent<Text>().text = p.faction;
            newCell.GetComponentInChildren<Text>().transform.Find("FactionLevel").GetComponent<Text>().text = ""; //implement later
            newCell.GetComponentInChildren<Text>().transform.Find("Exp").GetComponent<Text>().text = "EXP: " + p.exp.ToString();
            
            //set child to fit box
            newCell.transform.SetParent(leaderboardText.gameObject.transform, false); 
        }
    }

}
