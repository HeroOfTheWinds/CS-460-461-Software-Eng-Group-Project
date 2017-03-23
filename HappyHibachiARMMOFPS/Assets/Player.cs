using System;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Player : MonoBehaviour
{
    //player attributes
    public static string playername;
    public static Guid playerID = Guid.NewGuid();
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
    public void addEXP(int value)
    {
        EXP += value;
        updatelevel();
    }

    //function to increse player level based on EXP, tenative 
    public void updatelevel()
    {
        if(EXP > 0 && EXP < 100)
        {
            level = 1;
        }
        if (EXP > 100 && EXP < 300)
        {
            level = 2;
        }
        if (EXP > 300 && EXP < 600)
        {
            level = 3;
        }
        if (EXP > 600 && EXP < 1000)
        {
            level = 4;
        }
    }

    //function to increase/decrease faction EXP based on action, tenative
    public void addFactionEXP(int action_type, int value)
    {
        if (action_type == faction) //increase faction EXP
        {
            factionEXP += value;
        }
        else //decrease faction EXP
        {
            factionEXP -= value;
        }
        updateFaction(action_type);
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
