using UnityEngine;
using System.Collections;

public class menuScript : MonoBehaviour {
	private Canvas menu; // Assign in inspector
	private bool isShowing;
	
	// Use this for initialization
	void Start () {
		//menu = ;
		menu.enabled = false;
	}
	
	void Update() {
		if (Input.GetKeyUp(KeyCode.Escape)) 
		{
			menu.enabled = !menu.enabled;
		}
	}

	public void yes()
	{
		Debug.Log ("Yes");
	}

	public void no()
	{
		Debug.Log ("No");
	}

}
