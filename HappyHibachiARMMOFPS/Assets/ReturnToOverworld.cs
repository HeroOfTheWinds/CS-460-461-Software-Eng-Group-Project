using UnityEngine;
using System.Collections;
using UnityEngine.SceneManagement;

public class ReturnToOverworld : MonoBehaviour {
    public GameObject exitButton;

    //Use this for initialization
    void Start()
    {
    }

    public void returnToOverworld()
    {
        if (SceneManager.GetActiveScene().name == "Battle")
        {
            SceneManager.LoadScene("AR Scene");
        }
        else
        {
            SceneManager.LoadScene("Overworld");
        }
        
    }
}
