using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Landmine : MonoBehaviour {

    // Variable to hold instance ID of the player who planted the mine
    // Used to keep player from being exploded as soon as they place the mine
    public int placer;

    //private static Dictionary<byte, > numMinesPlaced;
    private static byte numMinesPlaced = 0;

    //for coordination
    private byte mineOrderID;

    // Variables to hold stats such as how much damage the mine does
    public float damage = 75f; // Roughly 3/4 of level 1 player's HP

    // Prefab holding the explosion object
    public GameObject Explosion;

    public byte MineOrderID
    {
        get
        {
            return mineOrderID;
        }

        set
        {
            mineOrderID = value;
        }
    }

    public static byte NumMinesPlaced
    {
        get
        {
            return numMinesPlaced;
        }

        set
        {
            numMinesPlaced = value;
        }
    }

    // Use this for initialization
    void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    // Use this function whenever a player enters into the zone that triggers the landmine
    void OnTriggerEnter( Collider other)
    {
        // If the collider entering the detonation range isn't the placer, create explosion
        if (other.gameObject.GetInstanceID() != placer)
        {
            GameObject explosion = Instantiate(Explosion, transform.position, transform.rotation); // Just the graphics
            Collider[] hitPlayers;
            //player shouldnt move while evaluating who was hit
            lock (PlayerControl.ACTION_LOCK)
            {
                // Find everyone in range
                hitPlayers = Physics.OverlapSphere(transform.position, 6f);
            }

            //stop an update from occuring while setting values
            lock (PlayerControl.MINE_LOCK)
            {
                // Decrease health of everyone/everything hit
                for (int i = 0; i < hitPlayers.Length; i++)
                {
                    PlayerControl controller = GameObject.FindGameObjectWithTag("Player").GetComponent<PlayerControl>();
                    // Check if the object is a player, decrease health if so
                    if (hitPlayers[i].tag == "Player")
                    {
                        hitPlayers[i].gameObject.GetComponent<PlayerStatus>().TakeHP(damage);
                        
                        if (hitPlayers[i].gameObject.GetInstanceID() == other.gameObject.GetInstanceID())
                        {
                            //if I was the one to set off this mine then report to server
                            controller.Mso = true;
                            //Debug.Log("mso set");
                        }
                    }
                    else if (hitPlayers[i].tag == "Enemy")
                    {
                        hitPlayers[i].gameObject.GetComponent<EnemyStatus>().TakeHP(damage);
                        if (hitPlayers[i].gameObject.GetInstanceID() != other.gameObject.GetInstanceID())
                        {
                            //if I was the one to set off this mine, did it also hit the opponent?
                            controller.Mho = true;
                            //Debug.Log(controller.Mho);
                        }
                    }
                }
            }

            // Now remove the landmine
            Destroy(gameObject);
        }
    }
}
