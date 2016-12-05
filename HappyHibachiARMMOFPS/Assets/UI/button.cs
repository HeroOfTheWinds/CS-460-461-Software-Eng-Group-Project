using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System.Collections;

public class button : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}


	//Function to open overworld
	public void openOverworld()
	{
		//Application.LoadLevel ("Overworld");
		//This opens the overworld
		SceneManager.LoadScene ("Overworld");
	}

}
