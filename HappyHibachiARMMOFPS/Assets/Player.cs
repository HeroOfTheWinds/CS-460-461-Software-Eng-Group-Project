using System;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Player : MonoBehaviour
{
    //player attributes
    public static string playername;
    public static Guid playerID = new Guid("f519af91e9d511e681d200155d2a070d");
    public static int EXP;
    public static int level;
    public static int faction;
    public static int factionEXP;
    public static int factionLevel;
    public static int maxHealthStat;
    public static int attackStat;
    public static int defenseStat;
    public static int shotSpeedStat;

    // map to hold player items
    public static Dictionary<string, int> Items = new Dictionary<string, int>();

    //function to increase player EXP
    public static void addEXP(int value, bool clientSide)
    {
        //if exp is awarded on server side then just update local player exp and update player lvl in db if needed
        Debug.Log("Awarding Player exp: " + value);
        EXP += value;
        GameObject fu = new GameObject();
        fu.AddComponent<ExperienceUpdate>();
        fu.GetComponent<ExperienceUpdate>().updateEXP(clientSide);
    }

    //function to increase/decrease faction EXP based on action, tenative
    public static void addFactionEXP(int action_type, int value)
    {
        if (action_type == faction) //increase faction EXP
        {
            factionEXP += value;
        }
        else //decrease faction EXP
        {
            factionEXP -= value;
        }
        //DEBUG ZONE 
        //later display to screen
        if (action_type == 1) { Debug.Log(Player.playername + " " + value + " " + " Paragon Exp gained!"); }
        if (action_type == 2) { Debug.Log(Player.playername + " " + value + " " + " Slayer Exp gained!"); }
        if (action_type == 3) { Debug.Log(Player.playername + " " + value + " " + "Hunter Exp gained!"); }
        GameObject fu = new GameObject();
        fu.AddComponent<FactionUpdate>();
        fu.GetComponent<FactionUpdate>().updateFaction(action_type);
    }

    //functin to add item to inventory, if item already exist amount is incresed
    public static void addItem(string i)
    {
        if (Items.ContainsKey(i)) Items[i] += 1;
        else Items.Add(i, 1);
    }

    //prints inventory
    public static void itemToString()
    {
        Debug.Log("Items now in inventory:");
        foreach (KeyValuePair<string, int> entry in Items)
        {
            // do something with entry.Value or entry.Key
            Debug.Log(entry.Key + ": " + entry.Value.ToString());
        }
    }

    //function to increase level by one
    public static void increaseLevel()
    {
        level++;
    }

    //function to update faction, if action causes faction EXP to become neg, faction is changed to action type, else stays null
    public static void updateFaction(int action_type)
    {
        if (factionEXP < 0)
        {
            faction = action_type;
        }
        if (factionEXP == 0)
        {
            faction = 0;
        }
    }

    //functions to change plaer battle stats
    public static void changeMaxHP(int value)
    {
        maxHealthStat += value;
    }
    public static void changeAttack(int value)
    {
        attackStat += value;
    }
    public static void changeDefense(int value)
    {
        attackStat += value;
    }
    public static void changeShotSpeed(int value)
    {
        shotSpeedStat += value;
    }

    
}
