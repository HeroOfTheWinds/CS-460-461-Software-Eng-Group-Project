// Modified from:
/*     INFINITY CODE 2013-2017      */
/*   http://www.infinity-code.com   */

using System.Collections;
using System.Text.RegularExpressions;
using UnityEngine;

public class FactionTileOverlay : MonoBehaviour
{
    // Overlay transparency
    [Range(0, 1)]
    public float alpha = 0.65f;

    private float _alpha = 1f;

    // Backtextures
    public Texture2D ParagonTex;
    public Texture2D SlayerTex;
    public Texture2D HunterTex;

    //global faction holder
    int faction_num = 0;

    private void Start()
    {
        // Subscribe to the tile download event.
        OnlineMaps.instance.OnStartDownloadTile += OnStartDownloadTile;
    }

    private void OnStartDownloadTile(OnlineMapsTile tile)
    {
        // Load overlay for tile from Resources.
        //tile.overlayBackTexture = Resources.Load<Texture2D>(string.Format("OnlineMapsOverlay/{0}/{1}/{2}", tile.zoom, tile.x, tile.y));

        // Custom code to load a faction texture overlay for this tile, based on faction in power

        // Insert code to retrieve faction int (0=neutral, 1=paragon, 2=slayer, 3=hunter) from server
        // Using tile.x and tile.y as indexing keys
        int tileFaction = 0;
        //-----server code-----//
        // If we have reached the maximum zoom, get the tile's faction from the server
        if (tile.zoom == 18)
        {
            // Use tile.x and tile.y to construct the query to the dictionary
            // Server's dictionary should have ints from 0 to 3, and thousands of entries
            //create form
            var form = new WWWForm();
            form.AddField("xtile", tile.x);
            Debug.Log("x = " + tile.x);
            form.AddField("ytile", tile.y);
            Debug.Log("y = " + tile.y);
            //send form to returnFactionTile.php
            string url = "http://13.84.163.243/returnFactionTile.php"; //script that handles faction tiles
            WWW send = new WWW(url, form);
            StartCoroutine(GrabTileInfo(send));
            // Set texture based on faction in power
            tileFaction = faction_num;
        }
        // Otherwise, leave 0 and do nothing
        
        switch (tileFaction)
        {
            case 0: // Neutral
                break;
            case 1: // Paragon
                tile.overlayBackTexture = ParagonTex;
                break;
            case 2: // Slayer
                tile.overlayBackTexture = SlayerTex;
                break;
            case 3: // Hunter
                tile.overlayBackTexture = HunterTex;
                break;
            default: // Should not reach, error if so
                // Don't put an overlay
                break;
        }

        tile.overlayBackAlpha = alpha;

        // Load the tile using a standard loader.
        OnlineMaps.instance.StartDownloadTile(tile);
    }

    private void Update()
    {
        // Update the transparency of overlay.
        if (_alpha != alpha)
        {
            _alpha = alpha;
            lock (OnlineMapsTile.tiles)
            {
                foreach (OnlineMapsTile tile in OnlineMapsTile.tiles) tile.overlayBackAlpha = alpha;
            }
        }
    }

    IEnumerator GrabTileInfo(WWW www)
    {
        yield return www;
        if (www.error == null) //connection is good and string recieved from server
        {
            Debug.Log("Faction Tile Connection good.");
            string text = Regex.Replace(www.text, @"\s", ""); //strip www.text of any whitespace
            Debug.Log(text);
            faction_num = System.Int32.Parse(text.TrimStart());
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }
}