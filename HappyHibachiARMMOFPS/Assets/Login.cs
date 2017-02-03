using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;
using System;
using System.Text.RegularExpressions;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

public class Login : MonoBehaviour
{
    private string username, password; //holds info to ask database
    public InputField userInput, passwordInput; //user input fields
    public Text ErrorText; //outputs errors to screen
    private string url = "http://132.160.49.90:7001/login.php"; //script that handles login

    void Start()
    {
        userInput.text = Register.username; //if coming from successful Register, username field displays choosen username
    }

    public void checkLogin() //sends username and password to login.php
    {
        username = userInput.text;
        password = passwordInput.text;
        //create form
        var form = new WWWForm();
        form.AddField("usernameFromUnity", username);
        form.AddField("passwordFromUnity", password);
        //send form to login.pp
        WWW send = new WWW(url, form);
        StartCoroutine(WaitForRequest(send));
    }

    IEnumerator WaitForRequest(WWW www)
    {
        yield return www;
        if (www.error == null) //connection is good and string recieved from server
        {
            //Debug.Log("Connection good.");
            string text = Regex.Replace(www.text, @"\s", ""); //strip www.text of any whitespace
            //Debug.Log(text);
            if (text == "1")
            {
                Debug.Log("Username not found.");
                //give login error
                ErrorText.text = "Username or password incorrect.";
            }
            if (text == "2")
            {
                Debug.Log("Password incorrect.");
                //give login error
                ErrorText.text = "Username or password incorrect.";
            }
            else
            {
                Debug.Log("Logged In");
                //display welcome
                ErrorText.text = "Welcome, " + username;
                yield return new WaitForSeconds(2);
                parsePlayerInfo(text);
                Debug.Log(Player.playerID.ToString());
                goToOverworld(); // proceed to Overworld
            }
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }

    public void parsePlayerInfo(string text) //string from login.php, splits string on '?' and stores into Player
    {
        //Player player = new Player(); // create new player to store info from server
        string[] splitString = text.Split(new string[] { "?" }, StringSplitOptions.None); //split fields on ?
        //stores info into Player Class
        Player.playername = splitString[0];
        Player.EXP = int.Parse(splitString[1]);
        Player.level = int.Parse(splitString[2]);
        Player.faction = int.Parse(splitString[3]);
        Player.factionEXP = int.Parse(splitString[4]);
        Player.factionLevel = int.Parse(splitString[5]);
        //Player.playerID = new Guid(splitString[6]);
    }


    //next scene helpers
    public void goToRegister()
    {
        SceneManager.LoadScene("Register");
    }
    public void goToOverworld()
    {
        SceneManager.LoadScene("Overworld");
    }
}
