using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;

public class menuScript : MonoBehaviour {

    public Canvas menu; // Assign in inspector
    //Used for checking if menu is active
    private bool isShowing;

    //Use this for initialization
    void Start()
    {
        //Disables the menu
        menu.enabled = false;
        isShowing = true;
    }

    public void callBattle()
    {
        SceneManager.LoadScene("Battle");
    }

}
