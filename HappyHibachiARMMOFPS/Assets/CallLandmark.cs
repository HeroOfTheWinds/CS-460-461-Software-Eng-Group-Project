using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using System.Collections;

public class CallLandmark : MonoBehaviour
{
    public Canvas landmark; // Assign in inspector
    //Used for checking if menu is active
    private bool isShowing;
    private RaycastHit hit;

    //Use this for initialization
    void Start()
    {
        //Disables the menu
        landmark.enabled = false;
        isShowing = true;
    }

    private void Update()
    {
        Ray ray = Camera.main.ScreenPointToRay(Input.GetTouch(0).position);
        Physics.Raycast(ray, out hit);
        if (hit.collider.tag == "Landmark")
        {
            isShowing = true;
            landmark.enabled = true;
        }
        if (Input.GetButtonUp("Exit"))
        {
            isShowing = false;
            landmark.enabled = false;
        }
    }

    public void callLandmark()
    {
        SceneManager.LoadScene("ViewLandmark");
    }
}
