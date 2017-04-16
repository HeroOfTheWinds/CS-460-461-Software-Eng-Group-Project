using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;
using System;

public class menuScript : MonoBehaviour {

    public GameObject enemyRadar;
    public Canvas menu; // Assign in inspector
    //Used for checking if menu is active
    private bool isShowing;

    //-----------------TEMPORARY TEST CODE----------------------

    public static Guid opponentID;
    //public static bool response = false;

    //----------------------------------------------------------

    //Use this for initialization
    void Start()
    {
        //Disables the menu
        menu.enabled = true;
        isShowing = true;
    }

    public void callBattle()
    {
        enemyRadar = GameObject.Find("Dropdown");
        string id = enemyRadar.GetComponent<Dropdown>().captionText.text;
        if (id != "(Select)")
        {
            opponentID = new Guid(id);
        }
        GenComManager.setUpdate(0, opponentID);

        //-----------------TEMPORARY TEST CODE----------------------

        //enemyRadar = GameObject.Find("Dropdown");
        //SceneManager.LoadScene("Battle");


        //use random id, not currently used for anything, should be id of landmark receiving items from
        //GenComManager.setUpdate(4, Guid.NewGuid());
        //GenComManager.setUpdate(1, new Guid("d585781ee9d521e687d200155d2a070e"));
        //GenComManager.setUpdate(2, new Guid("e5cadae5-e9d5-11e6-55d2-00155d2a070d"));


        //----------------------------------------------------------


        //SceneManager.LoadScene("Battle");
    }

}
