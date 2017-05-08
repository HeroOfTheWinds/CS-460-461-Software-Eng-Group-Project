using UnityEngine;
using System.Collections;
using UnityEngine.UI;
using System.Text.RegularExpressions;
using System;

public class EnemyStatus : MonoBehaviour
{

    // Create private variables for player stats
    private string enemyID;
    private static int level = 1;
    private static int maxHP = 100;
    private static int attack = 15;
    private static int defense = 10;
    private static float atkSpeed = 0.5f;

    private static float currentHP = 100f;

    // Reference the HP bar above the player for updating size
    public GameObject HPBar;
    // HP Bar on the HUD
    //public Image InnerHPBar;

    // Reference to animator for the enemy model
    public Animator animator;

    // Use this for initialization
    void Start()
    {
        // Code for retrieving enemy level from server should go here //
        enemyID = BattleNetManager.OpponentID.ToString();
        //level = retrieveLevelDB(enemyID);
        //Debug.Log("Enemy level: " + level);

        // Calculate stats based off received data
        //CalcStats();
    }

    // Update is called once per frame
    void Update()
    {
        // Should we let players invest points, or just do auto-level based points?
        // Should we change base growths for different factions?

        // Change HP Bar's width based on health remaining
        HPBar.transform.localScale = new Vector3(currentHP / maxHP, 1f, 1f);

        // Do same for HP Bar on HUD
        //InnerHPBar.rectTransform.localScale = new Vector3(currentHP / maxHP, 1f, 1f);
    }

    public void TakeHP(float hp)
    {
        // Decrease player's current HP by given amount
        currentHP -= hp;
        if (currentHP < 0)
            currentHP = 0;
        //is the enemy dead?
        //server keeps track now
        /*
        if (currentHP <= 0)
        {
            GameObject.FindGameObjectWithTag("Player").GetComponent<PlayerControl>().DisplayWin();

            GameObject player = GameObject.FindGameObjectWithTag("Player");
            PlayerControl controller = player.GetComponent<PlayerControl>();
            //indicate the battle's over
            controller.BattleEnd = true;
            controller.Win = true;
        }
        */
        // Play the recoil animation
        animator.SetTrigger("Damage");
    }

    public void RestoreHP(float hp)
    {
        // Restore player's HP by given amount
        currentHP += hp;

        // If new HP exceeds max, truncate
        if (currentHP > maxHP)
            currentHP = maxHP;
    }

    // Function to calculate current stats based off current level
    // Very much WIP, gotta decide growth functions
    private static void CalcStats()
    {
        // Using linear for now, will do something robust later
        maxHP = 100 + level * 21;
        attack = 15 + level * 4;
        defense = 10 + level * 3;
        atkSpeed = 0.5f + 0.05f * level;
        currentHP = maxHP;
    }

    //function to retrieve the opponent's level based off their guid
    //public int retrieveLevelDB(string playerID)
    //{
    //    var form = new WWWForm();
    //    form.AddField("playerID", playerID);
    //    WWW send = new WWW("http://13.84.163.243/level.php", form);
    //    StartCoroutine(WaitForLevelRequest(send, level));

    //    return level;
    //    //return level 0 until complete
    //    //return 0;
    //}

    ////function sends guid to level.php to retrieve level from server
    //IEnumerator WaitForLevelRequest(WWW www, int level)
    //{
    //    yield return www;
    //    if (www.error == null) //connection is good and string recieved from server
    //    {
    //        Debug.Log("Connection good.");
    //        string text = Regex.Replace(www.text, @"\s", ""); //strip www.text of any whitespace
    //        level = Convert.ToInt32(text);
    //        Debug.Log(level);
    //        //CalcStats();
    //    }
    //    else
    //    {
    //        Debug.Log("Connection error.");
    //    }
    //}

    /*
    -------------GETTERS---------------
    */

    public int getLevel()
    {
        return level;
    }
    public static void setLevel(int l)
    {
        level = l;
        CalcStats();
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
