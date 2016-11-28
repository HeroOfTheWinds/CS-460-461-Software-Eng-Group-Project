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

	public void openOverworld()
	{
		//Application.LoadLevel ("Overworld");
		SceneManager.LoadScene ("Overworld");
	}

}
