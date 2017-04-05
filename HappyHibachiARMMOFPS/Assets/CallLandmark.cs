using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System.Collections;

public class CallLandmark : MonoBehaviour
{
    public Canvas landmark; // Assign in inspector
    public Canvas items;
    //Used for checking if menu is active
    private bool isShowing;
    private RaycastHit hit;

    //Use this for initialization
    void Start()
    {
        //Disables the menu
        landmark.enabled = false;
        isShowing = false;
    }

    private void Update()
    {
    }

    public void callLandmark()
    {
        SceneManager.LoadScene("ViewLandmark");
    }
    public void exitLandmark()
    {
        landmark.enabled = false;
        items.enabled = false;
    }
}
