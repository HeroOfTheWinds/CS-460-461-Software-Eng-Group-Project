using UnityEngine;
using System.Collections;

public class itemScript : MonoBehaviour {

	//Used to store the items menu
	public Canvas itemMenu;
	//Used for the items in
    public GameObject player;
    //Used for the sound to play when opening up menu
    public AudioSource showMenu;
    //Used for the sound to play when closing menu
    public AudioSource cancelMenu;
    //Used for the sound to place Landmine
    public AudioSource landminePlace;
    //Used for the sound to use health restoration item
    public AudioSource healthItemUse;


    //This runs at the start of the program
    void Start()
	{
		//Links the items menu canvas
		itemMenu = itemMenu.GetComponent<Canvas>();
		//Disables the items menu
		itemMenu.enabled = false;
	}
	
	// Update is called once per frame
	void Update () {
	 
	}

	//Function to open the items menu
	public void itemMenuShow()
	{
		//enables the items menu
		itemMenu.enabled = true;
        showMenu.Play();
	}

	//Function to hide the items menu
	public void itemMenuHide()
	{
		//Disables the items menu
		itemMenu.enabled = false;
        cancelMenu.Play();
    }

	public void ok()
	{
		//Code for OK button in items menu
	}


	//Function to set the land mines
    public void placeMine()
    {

        // First check if the player has any landmines
        if (CheckUseItem("Landmine"))
        {
            // Call function to place mine at player's feet
            PlayerControl controller = player.GetComponent<PlayerControl>();
            controller.PlaceLandmine(player.transform.position, player.transform.rotation, player.GetInstanceID());
            //indicate a mine was placed and where
            controller.Mp = true;
            //stop player motion while checking position
            lock(PlayerControl.ACTION_LOCK)
            {
                controller.Mpx = player.transform.position.x;
                controller.Mpz = player.transform.position.z;
            }
            landminePlace.Play();
        }
        // Auto-close menu
        itemMenu.enabled = false;
    }

    // Function to use the health restoration item
    public void useHPRefill()
    {
        // First check if the player has any HP refills
        if (CheckUseItem("HP Refill"))
        {
            // Call function to refill the player's health by 50 points
            PlayerStatus status = player.GetComponent<PlayerStatus>();
            status.RestoreHP(50f);
            healthItemUse.Play();
        }
        // Auto-close menu
        itemMenu.enabled = false;
    }

    // Function to check how many of a given item the player has in their inventory
    // If they do, return true to proceed using the item, and tell server to remove one from inventory
    public bool CheckUseItem(string name)
    {
        // Code to connect to server goes here

        // Checks if item is available
        if (true)
        {
            // Tell server to decrement amount held by one

            // Return true to allow user to use the item
            return true;
        }
        else
        {
            // User doesn't have the item
            return false;
        }
    }
}
