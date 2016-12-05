using UnityEngine;
using System.Collections;

public class itemScript : MonoBehaviour {

	//Used to store the items menu
	public Canvas itemMenu;
	//Used for the items in
    public GameObject player;


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

	}

	//Function to hide the items menu
	public void itemMenuHide()
	{
		//Disables the items menu
		itemMenu.enabled = false;
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
            controller.Mpx = player.transform.position.x;
            controller.Mpz = player.transform.position.z;
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
