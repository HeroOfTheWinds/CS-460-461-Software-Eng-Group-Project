using System;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;

public class ColosseumFaction : MonoBehaviour
{

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

    // Holds the colosseums guid when instantiated by Overworld
    public Guid coloseumID = new Guid();

    // Global faction holder
    int faction_num = 0;

    // Use this for initialization
    void Start()
    {
        // Get the material on this colosseum that shows the faction symbol
        factionMat = rend.materials[2];

        // Set the initial texture based on the server
        UpdateFaction();
    }

    // Update is called once per frame
    void Update()
    {
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
    public void UpdateFaction()
    {
        // Retrieve the colosseum's current faction from the server
        faction_num = 0;

        // Server data retrieval code here
        // Check to see if guid is set
        if (coloseumID != Guid.Empty)
        {
            RetrieveColosseumFactionDB();
        }

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

    // Function to retrieve the faction num of a colosseum based on it's guid
    void RetrieveColosseumFactionDB()
    {
        //coloseumID = new Guid("d5857650-e9d5-11e6-87d2-00155d2a070d"); //testing
        string url = "http://13.84.163.243/returnColosseumFaction.php"; //script that handles colloseum faction
        var form = new WWWForm();
        form.AddField("guid", coloseumID.ToString());
        
        WWW send = new WWW(url, form);
        StartCoroutine(GrabFactionInfo(send));
        Debug.Log(faction_num);
    }

    // Waits for faction information from DB
    IEnumerator GrabFactionInfo(WWW www)
    {
        yield return www;
        if (www.error == null) // Connection is good and string recieved from server
        {
            //Debug.Log("Connection good.");
            string text = Regex.Replace(www.text, @"\s", ""); //strip www.text of any whitespace
            //Debug.Log(text);
            faction_num = System.Int32.Parse(text.TrimStart());
        }
        else
        {
            faction_num = 0;
            Debug.Log("Connection error. Setting colosseum faction to Null");
        }
    }
}
