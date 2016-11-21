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

    Dictionary<string, int> Items;

    public void addEXP()
    {

    }

    public void addItem(string i)
    {
        Items[i] += 1;
    }

    public void addFactionEXP()
    {

    }
}
