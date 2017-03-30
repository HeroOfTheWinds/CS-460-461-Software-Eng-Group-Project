using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MapHUD : MonoBehaviour {

    // public objects to change on the HUD
    public Canvas HUD;
    public RawImage XP_Bar;
    public Text pText;
    public Text levelText;

	// Use this for initialization
	void Start () {
        // Variables to store data from server
        string pName = "";
        int pLevel = 1;
        int EXP = 0;
        int Faction = 0;

        // Load player info here


        // Reflect changes
        pText.text = pName;
        XP_Bar.rectTransform.localScale = new Vector3((float)EXP / (float)(pLevel * 100), 1f, 1f);
        levelText.text = pLevel.ToString();

        // change color based on faction
        switch (Faction)
        {
            case 0: // factionless
                XP_Bar.color = Color.white;
                break;
            case 1: // paragon
                XP_Bar.color = Color.cyan;
                break;
            case 2: // slayer
                XP_Bar.color = Color.red;
                break;
            case 3: // hunter
                XP_Bar.color = Color.green;
                break;
            default:
                break;
        }
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    // Add function to update display with new data at will (better than pinging update constantly)
    public void UpdateHUD(int level, int exp)
    {
        XP_Bar.rectTransform.localScale = new Vector3((float)exp / (float)(level * 100), 1f, 1f);
        levelText.text = level.ToString();
    }
}
