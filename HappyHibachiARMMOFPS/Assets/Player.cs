using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Player{
    public static string playername;
    public static int EXP;
    public static int level;
    public static int faction;
    public static int factionEXP;
    public static int factionLevel;
    public static int maxHealthStat;
    public static int attackStat;
    public static int defenseStat;
    public static int shotSpeedStat;

    public static Dictionary<string, int> Items = new Dictionary<string, int>();

    public void addEXP(int value)
    {
        EXP += value;
        updatelevel();
    }

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

    public static void addItem(string i)
    {
        if (Items.ContainsKey(i)) Items[i] += 1;
        else Items.Add(i, 1);
    }

    public static void itemToString()
    {
        Debug.Log("Items now in inventory:");
        foreach (KeyValuePair<string, int> entry in Items)
        {
            // do something with entry.Value or entry.Key
            Debug.Log(entry.Key + ": " + entry.Value.ToString());
        }
    }

    public static void increaseLevel()
    {
        level++;
    }

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
