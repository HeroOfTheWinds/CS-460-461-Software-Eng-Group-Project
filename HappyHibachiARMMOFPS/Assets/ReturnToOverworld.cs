using UnityEngine;
using System.Collections;
using UnityEngine.SceneManagement;

public class ReturnToOverworld : MonoBehaviour {
    public GameObject exitButton;
    public GameObject LoadingScreen;

    //Use this for initialization
    void Start()
    {
        LoadingScreen = GameObject.FindGameObjectWithTag("Loading Screen");
    }

    public void returnToOverworld()
    {
        if (SceneManager.GetActiveScene().name == "Battle")
        {
            // Randomly select which AR puzzle to go to
            Random.InitState(System.DateTime.Now.Millisecond); // Seed with current millisecond of time
            double rand = Random.value;

            // Use random number to decide
            if (rand > 0.5)
            {
                //SceneManager.LoadScene("AR Scene 2");
                LoadingScreen.GetComponent<SceneLoader>().LoadScene("AR Scene");
            }
            else
            {
                //SceneManager.LoadScene("AR Scene");
                LoadingScreen.GetComponent<SceneLoader>().LoadScene("AR Scene");
            }
        }
        else
        {
            //SceneManager.LoadScene("Overworld");
            LoadingScreen.GetComponent<SceneLoader>().LoadScene("Overworld");
        }
        
    }
}
