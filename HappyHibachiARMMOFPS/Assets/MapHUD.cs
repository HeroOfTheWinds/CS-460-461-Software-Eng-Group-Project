using System;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;
using UnityEngine.UI;

public class MapHUD : MonoBehaviour {

    // public objects to change on the HUD
    public Canvas HUD;
    public RawImage XP_Bar;
    public Text pText;
    public Text levelText;

	// Use this for initialization
	void Start () {
        // Load player info here
        StartCoroutine(GrabPlayerInfo());
    }
	
	// Update is called once per frame
	void Update () {
		
	}

    
    // Add function to update display with new data at will (better than pinging update constantly)
    public void UpdateHUD(int level, int exp)
    {
        // Calculate a summation needed for experience determination
        int sum = 1;
        for (int i = level; i > 1; i--)
        {
            sum += i;
        }
        XP_Bar.rectTransform.localScale = new Vector3((float)exp / (float)(sum * 100), 1f, 1f);
        levelText.text = level.ToString();
    }

    //Grab Player Info from 
    IEnumerator GrabPlayerInfo()
    {
        // Variables to store data from server
        string pName = "";
        int pLevel = 1;
        int EXP = 0;
        int Faction = 0;

        var form = new WWWForm();
        form.AddField("guid", Player.playerID.ToString()); 
        //form.AddField("guid", "f519a533-e9d5-11e6-87d2-00155d2a070d"); // tester

        //send form to leaderboard.php
        WWW www = new WWW("http://13.84.163.243/playerInfo.php", form);

        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
            string info = www.text;
            info = info.TrimStart(); // remove begining whitespace

            //split incoming string and store in variables
            string[] splitString = info.Split(new string[] { "?" }, StringSplitOptions.None); //split fields on ?
            pName = splitString[0];
            EXP = Int32.Parse(splitString[1]);
            pLevel = Int32.Parse(splitString[2]);
            Faction = Int32.Parse(splitString[3]);

            // Reflect changes
            pText.text = pName;
            // Calculate a summation needed for experience determination
            int sum = 1;
            for (int i = pLevel; i > 1; i--)
            {
                sum += i;
            }
            XP_Bar.rectTransform.localScale = new Vector3((float)EXP / (float)(sum * 100), 1f, 1f);
            levelText.text = pLevel.ToString();

            // change color based on faction
            switch (Faction)
            {
                case 0: // factionless
                    XP_Bar.color = Color.white;
                    break;
                case 1: // paragon
                    XP_Bar.color = Color.cyan;
                    break;
                case 2: // slayer
                    XP_Bar.color = Color.red;
                    break;
                case 3: // hunter
                    XP_Bar.color = Color.green;
                    break;
                default:
                    break;
            }

        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
            //display info from login
            pName = Player.playername;
            EXP = Player.EXP;
            pLevel = Player.level;
            Faction = Player.faction;
        }
    }
}
