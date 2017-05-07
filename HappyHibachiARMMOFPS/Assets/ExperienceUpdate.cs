using System;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;

public class ExperienceUpdate : MonoBehaviour {
    //function to update exp, if update causes EXP to gain level then level is updated in db as well
    public void updateEXP(bool clientSide)
    {
        //if client side, update player exp and level in db
        if (clientSide)
        {
            updatePlayerLevel();
            StartCoroutine(UpdateDB(clientSide));
        }

        //else server side
        else {
            if (updatePlayerLevel()) //if level increases update player level in db
            {
                StartCoroutine(UpdateDB(clientSide));
            } //else no need to update db
        }
    }

    //updates player's level based on exp
    bool updatePlayerLevel()
    {
        //calcualte level
        int level = Convert.ToInt32(Math.Floor(Math.Sqrt(Convert.ToDouble(Player.EXP) / 100.0)));
        //if level chnages, update db
        if(level != Player.level)
        {
            Debug.Log("Updating player level in DB");
            Player.level = level;
            return true;
        }
        //else keep the same
        return false;
    }

    //send updates to database
    public IEnumerator UpdateDB(bool clientSide)
    {
        //create form
        Debug.Log("Experience Points Update called");
        string url = "http://13.84.163.243/expUpdate.php"; //script that handles faction update
        var form = new WWWForm();

        //vars to send to script
        form.AddField("guid", Player.playerID.ToString());
        form.AddField("level", Player.level);
        if (clientSide)
        {
            form.AddField("exp", Player.EXP); //script chooses which sql command to run
        }
        else
        {
            form.AddField("exp", -1); //script chooses which sql command to run
        }

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
                Debug.Log("EXP update successful");
            }
            if (text == "1")
            {
                //unsuccessful
                Debug.Log("EXP update unsuccessful");
            }
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }
}
