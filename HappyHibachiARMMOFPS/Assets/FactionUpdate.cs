using System;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;

public class FactionUpdate : MonoBehaviour {
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

        //DEBUG ZONE
        if (Player.faction == 1) { Debug.Log(Player.playername + " is a Paragon"); }
        if (Player.faction == 2) { Debug.Log(Player.playername + " is a Slayer"); }
        if (Player.faction == 3) { Debug.Log(Player.playername + " is a Hunter"); }
        //send changes to db
        
        StartCoroutine(UpdateFactionDB());
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
        form.AddField("exp", Player.factionEXP);

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
