using UnityEngine;
using System.Collections;

public class itemScript : MonoBehaviour {

	public Canvas itemMenu;
    public GameObject player;

	void Start()
	{
		itemMenu = itemMenu.GetComponent<Canvas>();
		itemMenu.enabled = false;
	}
	
	// Update is called once per frame
	void Update () {
	 
	}


	public void itemMenuShow()
	{
		itemMenu.enabled = true;

	}

	public void itemMenuHide()
	{
		itemMenu.enabled = false;
	}

	public void ok()
	{
		//Code for OK button in items menu
	}

    public void placeMine()
    {
        // Call function to place mine using the player
        player.GetComponent<PlayerControl>().PlaceLandmine();

        // Auto-close menu
        itemMenu.enabled = false;
    }
}
