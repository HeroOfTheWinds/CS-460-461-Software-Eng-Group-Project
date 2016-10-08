using UnityEngine;
using System.Collections;

public class PlayerStatus : MonoBehaviour {

    // Create private variables for player stats
    private int level = 1;
    private int maxHP = 100;
    private int attack = 15;
    private int defense = 10;
    private float atkSpeed = 0.5f;

    private float currentHP = 100f;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
        // Should we let players invest points, or just do auto-level based points?
        // Should we change base growths for different factions?
	
	}

    public void TakeHP(float hp)
    {
        // Decrease player's current HP by given amount
        currentHP -= hp;

        // Check if HP <= 0, lose battle if so
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
    private void CalcStats()
    {
        // Using linear for now, will do something robust later
        maxHP = 100 + level * 21;
        attack = 15 + level * 4;
        defense = 10 + level * 3;
        atkSpeed = 0.5f + 0.05f * level;
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
