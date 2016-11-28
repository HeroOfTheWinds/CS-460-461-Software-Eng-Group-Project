using UnityEngine;
using System.Collections;

public class itemScript : MonoBehaviour {

	public Canvas itemMenu;

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

}
