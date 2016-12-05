using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System.Collections;

public class menuScript : MonoBehaviour {


	public Canvas menu; // Assign in inspector
	//Used for checking if menu is active
	private bool isShowing;
	
	//Use this for initialization
	void Start () {
		//Disables the menu
		menu.enabled = false;
	}


	void Update() {
		
	}

	//Function for when the yes button is pressed
	public void yes()
	{
	}

	//Function for when the no button is pressed
	public void no()
	{
		
	}

	//Function for loading the battle scene
	public void openBattle()
	{
		//Loads the battle scence
		SceneManager.LoadScene ("Battle");
	}

}
