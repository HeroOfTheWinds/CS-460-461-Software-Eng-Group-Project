using UnityEngine;
using System.Collections;

public class Landmine : MonoBehaviour {

    // Variable to hold instance ID of the player who planted the mine
    // Used to keep player from being exploded as soon as they place the mine
    public int placer;

    // Variables to hold stats such as how much damage the mine does
    public float damage = 75f; // Roughly 3/4 of level 1 player's HP

    // Prefab holding the explosion object
    public GameObject Explosion;

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

            // Find everyone in range
            Collider[] hitPlayers = Physics.OverlapSphere(transform.position, 6f);

            // Decrease health of everyone/everything hit
            for (int i = 0; i < hitPlayers.Length; i++)
            {
                // Check if the object is a player, decrease health if so
                if (hitPlayers[i].tag == "Player")
                {
                    hitPlayers[i].gameObject.GetComponent<PlayerStatus>().TakeHP(damage);
                }
                else if (hitPlayers[i].tag == "Enemy")
                {
                    hitPlayers[i].gameObject.GetComponent<EnemyStatus>().TakeHP(damage);
                }
            }

            // Now remove the landmine
            Destroy(gameObject);
        }
    }
}
