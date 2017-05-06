using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ColosseumFaction : MonoBehaviour {

    // Variables to store the textures of the faction symbols
    public Texture SlayerTex;
    public Texture HunterTex;
    public Texture ParagonTex;

    // Store the renderer used by the colosseum instance
    public Renderer rend;

    // Variable for the material showing the faction symbol
    private Material factionMat;

    // Set a variable to control how often the colosseum's texture display should be updated
    public int updateQuantum = 45; // seconds
    private float timer = 0f; // counts time up to quantum then resets

    // Use this for initialization
    void Start () {
        // Get the material on this colosseum that shows the faction symbol
        factionMat = rend.materials[2];

        // Set the initial texture based on the server
        UpdateFaction();
	}
	
	// Update is called once per frame
	void Update () {
        // Update the timer
        timer += Time.deltaTime;

        // Check if it's time to update the colosseum's faction
        if (timer >= updateQuantum)
        {
            // Update the faction and reset the timer
            UpdateFaction();
            timer = 0f;
        }
	}

    // Function to change the faction texture
    void UpdateFaction()
    {
        // Retrieve the colosseum's current faction from the server
        int faction_num = 0;

        // Server data retrieval code here

        // Change the texture based on the faction
        switch (faction_num)
        {
            case 0: // Neutral
                // Set no texture
                factionMat.mainTexture = null;
                break;
            case 1: // Paragon
                // Set texture to Paragon texture
                factionMat.mainTexture = ParagonTex;
                break;
            case 2: // Slayer
                factionMat.mainTexture = SlayerTex;
                break;
            case 3: // Hunter
                factionMat.mainTexture = HunterTex;
                break;
            default:
                // Set no texture
                factionMat.mainTexture = null;
                break;
        }
    }
}
