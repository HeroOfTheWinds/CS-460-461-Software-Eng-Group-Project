using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;


public class nearbyPlayers : MonoBehaviour {

	public GameObject nearby;
	public GameObject prefabButton;
	public RectTransform nearbyButtonsPanel;
	public GameObject confirm;
	public Text textOne;
	public Text textTwo;
	public Text confirmPrompt;

	// Use this for initialization
	void Start () {
		nearby.SetActive (false);
		confirm.SetActive (false);
	}



	public void nearbyCanvasActivate()
	{

		nearby.SetActive (true);
		for(int i = 0; i < 15; i++)
		{
			textOne.text = "Player ID";
			textTwo.text = i.ToString();
			GameObject goButton = (GameObject)Instantiate(prefabButton);
			goButton.transform.SetParent(nearbyButtonsPanel, false);
			goButton.transform.localScale = new Vector3(1, 1, 1);

			Button tempButton = goButton.GetComponent<Button>();

			string tes = textTwo.text;
			int tempInt = int.Parse(tes);

			tempButton.onClick.AddListener(() => ButtonClicked(tempInt));
		}

		prefabButton.SetActive (false);
		
	}

	void ButtonClicked(int buttonNo)
	{
		confirm.SetActive (true);
		confirmPrompt.text = "Do you want to challenge Player" + buttonNo + "?";
	}

	//Function for loading the battle scene
	public void openBattle()
	{
		//Loads the battle scence
		//SceneManager.LoadScene ("Battle");

		//Active Nearby Players Canvas
		nearbyCanvasActivate();
	}

	//Function for when the yes button is pressed
	public void yes()
	{
		Debug.Log ("hello");
		SceneManager.LoadScene ("Battle");
	}

	//Function for when the no button is pressed
	public void no()
	{
		confirm.SetActive (false);
	}
}
