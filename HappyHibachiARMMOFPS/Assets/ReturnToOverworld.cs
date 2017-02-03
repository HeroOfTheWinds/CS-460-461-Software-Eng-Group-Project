using UnityEngine;
using System.Collections;
using UnityEngine.SceneManagement;

public class ReturnToOverworld : MonoBehaviour {

    //Use this for initialization
    void Start()
    {
    }

    public void returnToOverworld()
    {
        SceneManager.LoadScene("Overworld");
    }
}
