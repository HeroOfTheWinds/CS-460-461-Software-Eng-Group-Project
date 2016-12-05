using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System.Collections;

public class start : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	//Opens the game
	public void openGame()
	{
		//Loads the battle scence
		SceneManager.LoadScene ("Battle");
	}

}
