using System;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;

public class FactionUpdate : MonoBehaviour
{
    //function to update faction, if action causes faction EXP to become neg, faction is changed to action type, else stays null
    public void updateFaction(int action_type)
    {
        if (Player.factionEXP < 0)
        {
            Player.faction = action_type;
            Player.factionEXP = Math.Abs(Player.factionEXP); //convert negative factionexp to positive
        }
        if (Player.factionEXP == 0)
        {
            Player.faction = 0; //changes faction back to none
        }

        updateFactionLevel();

        //DEBUG ZONE
        if (Player.faction == 1) { Debug.Log(Player.playername + " is a Paragon"); }
        if (Player.faction == 2) { Debug.Log(Player.playername + " is a Slayer"); }
        if (Player.faction == 3) { Debug.Log(Player.playername + " is a Hunter"); }
        //send changes to db

        StartCoroutine(UpdateFactionDB());
    }

    //updates player's faction level based on faction exp
    public void updateFactionLevel()
    {
        //three tiers 
        //each successive tier requires more faction exp for each faction level
        if (Player.factionEXP >= 0 && Player.factionEXP < 10000) //tier 1, faction lvls 1-10, exp 0 to 9999
        {
            Player.factionLevel = Convert.ToInt32(Math.Floor(Convert.ToDouble(Player.factionEXP) / 1000.0)) + 1;
        }
        if (Player.factionEXP >= 10000 && Player.factionEXP < 25000) //tier 2 faction lvl 11-20, exp 10000 to 24999
        {
            Player.factionLevel = Convert.ToInt32(Math.Floor(Convert.ToDouble(Player.factionEXP - 10000) / 1500.0)) + 10;
        }
        if (Player.factionEXP >= 25000 && Player.factionEXP < 60000)//tier 3 faction lvl 21-30, exp 25000 to 60000
        {
            Player.factionLevel = Convert.ToInt32(Math.Floor(Convert.ToDouble(Player.factionEXP - 25000) / 3500.0)) + 20;
        }
    }

    //send updates to database
    public IEnumerator UpdateFactionDB()
    {
        //create form
        Debug.Log("FactionUpdate called");
        string url = "http://13.84.163.243/factionUpdate.php"; //script that handles faction update
        var form = new WWWForm();

        //vars to send to script
        form.AddField("guid", Player.playerID.ToString());
        form.AddField("faction", Player.faction);
        form.AddField("f_level", Player.factionLevel);
        form.AddField("f_exp", Player.factionEXP);

        //send form to factionUpdate.php
        WWW update = new WWW(url, form);

        yield return update;
        if (update.error == null) //connection is good and string recieved from server
        {
            //Debug.Log("Connection good.");
            string text = Regex.Replace(update.text, @"\s", ""); //strip www.text of any whitespace
            //Debug.Log(text);
            if (text == "0")
            {
                //successful
                Debug.Log("Faction EXP successful");
            }
            if (text == "1")
            {
                //unsuccessful
                Debug.Log("Faction EXP unsuccessful");
            }
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }
}
