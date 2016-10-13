using UnityEngine;
using System.Collections;

public class menuAppear : MonoBehaviour {
	
	private Canvas menu; // Assign in inspector
	private bool isShowing;

	// Use this for initialization
	void Start () {
		menu = GetComponent<Canvas> ();
		menu.enabled = false;
	}

	void Update() {
		if (Input.GetKeyUp(KeyCode.Escape)) 
		{
			menu.enabled = !menu.enabled;
		}
	}
}
