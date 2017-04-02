using UnityEngine;
using System.Collections;
using UnityEngine.UI;
using System.Text.RegularExpressions;
using System;

public class PlayerStatus : MonoBehaviour {

    // Create private variables for player stats
    private string playerID;
    private int level = 1;
    private int maxHP = 100;
    private int attack = 18;
    private int defense = 10;
    private float atkSpeed = 0.5f;

    private float currentHP = 100f;

    // Reference the HP bar above the player for updating size
    public GameObject HPBar;
    // HP Bar on the HUD
    public Image InnerHPBar;

	// Use this for initialization
	void Start () {
        // Code for retrieving level from server should go here //
        playerID = Player.playerID.ToString();
        level = retrieveLevelDB(playerID);
        Debug.Log("Player level: " + level);

        // Calculate stats based off received data
        CalcStats();
	}
	
	// Update is called once per frame
	void Update () {
        // Should we let players invest points, or just do auto-level based points?
        // Should we change base growths for different factions?

        // Change HP Bar's width based on health remaining
        HPBar.transform.localScale = new Vector3(currentHP / maxHP, 1f, 1f);

        // Do same for HP Bar on HUD
        InnerHPBar.rectTransform.localScale = new Vector3(currentHP / maxHP, 1f, 1f);
	}

    public void TakeHP(float hp)
    {
        // Decrease player's current HP by given amount
        currentHP -= hp;
        if (currentHP < 0)
            currentHP = 0;

        // Check if HP <= 0, lose battle if so
        //dealt with by server now
        /*
        if (currentHP <= 0)
        {
            gameObject.GetComponent<PlayerControl>().DisplayLoss();

            PlayerControl controller = gameObject.GetComponent<PlayerControl>();
            controller.BattleEnd = true;
            controller.Win = false;
            Debug.Log(currentHP);
        }
        */
        Debug.Log(currentHP);
    }

    public void RestoreHP(float hp)
    {
        PlayerControl controller = gameObject.GetComponent<PlayerControl>();
        controller.Hpr = true;
        // Restore player's HP by given amount
        currentHP += hp;

        // If new HP exceeds max, truncate
        if (currentHP > maxHP)
            currentHP = maxHP;

    }


    // Function to calculate current stats based off current level
    // Very much WIP, gotta decide growth functions
    private void CalcStats()
    {
        // Using linear for now, will do something robust later
        maxHP = 100 + (level - 1) * 21;
        attack = 15 + (level - 1) * 4;
        defense = 10 + (level - 1) * 3;
        atkSpeed = 0.5f + 0.05f * (level - 1);
    }

    //function to retrieve the player's level based off their guid
    public int retrieveLevelDB(string playerID)
    {
        int level = 1;
        var form = new WWWForm();
        form.AddField("playerID", playerID);
        WWW send = new WWW("http://132.160.49.90:7001/level.php", form);
        StartCoroutine(WaitForLevelRequest(send, level));

        return level;
    }

    //function sends guid to level.php to retrieve level from server
    IEnumerator WaitForLevelRequest(WWW www, int level)
    {
        yield return www;
        if (www.error == null) //connection is good and string recieved from server
        {
            Debug.Log("Connection good.");
            Debug.Log(www.text);
            string text = Regex.Replace(www.text, @"\s", ""); //strip www.text of any whitespace
            level = Convert.ToInt32(text);
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }

    /*
    -------------GETTERS---------------
    */

    public int getLevel()
    {
        return level;
    }

    public int getMaxHP()
    {
        return maxHP;
    }

    public float getHP()
    {
        return currentHP;
    }

    public int getAttack()
    {
        return attack;
    }

    public int getDefense()
    {
        return defense;
    }

    public float getAtkSpeed()
    {
        return atkSpeed;
    }
}
