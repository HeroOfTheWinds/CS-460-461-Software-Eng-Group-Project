using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System.Collections;

public class CallLandmark : MonoBehaviour
{
    public Canvas landmark; // Assign in inspector
    //Used for checking if menu is active
    private bool isShowing;

    //Use this for initialization
    void Start()
    {
        //Disables the menu
        landmark.enabled = false;
        isShowing = false;
    }

    public void callLandmark()
    {
        SceneManager.LoadScene("ViewLandmark");
    }
}
