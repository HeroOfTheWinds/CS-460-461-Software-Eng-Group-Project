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
    private string url = "http://13.84.163.243/login.php"; //script that handles login

    // Store a reference to the loading screen
    public GameObject loadingScreen;

    [Serializable]
    public class LoginSave
    {
        public string username { get; set; }
        public string password { get; set; }
    }

    void Start()
    {
        userInput.text = Register.username; //if coming from successful Register, username field displays choosen username
        LoginSave Info = new LoginSave();
        Info = LoadLogin();
        userInput.text = Info.username;
        passwordInput.text = Info.password;
    }

    public LoginSave LoadLogin()
    {
        //check if login save file exist
        if (File.Exists(Application.persistentDataPath + "/login.dat")) //true, insest username and password
        {
            BinaryFormatter bf = new BinaryFormatter();
            FileStream file = File.Open(Application.persistentDataPath + "/login.dat", FileMode.Open);
            LoginSave LoginSaved = (LoginSave)bf.Deserialize(file);
            file.Close();
            return LoginSaved;
        }
        else //false do nothing
        {
            Debug.Log(string.Format("Login file doesn't exist at path: {0}{1}", Application.persistentDataPath, "/login.dat"));
            return null;
        }

    }

    public void SaveLogin(LoginSave Info)
    {
        BinaryFormatter bf = new BinaryFormatter();
        FileStream file = File.Create(Application.persistentDataPath + "/login.dat");
        bf.Serialize(file, Info);
        file.Close();
    }

    public void checkLogin() //sends username and password to login.php
    {
        username = userInput.text;
        password = passwordInput.text;
        //create form
        var form = new WWWForm();
        form.AddField("usernameFromUnity", username);
        form.AddField("passwordFromUnity", password);
        //send form to login.php
        WWW send = new WWW(url, form);
        StartCoroutine(WaitForRequest(send, password));
    }

    IEnumerator WaitForRequest(WWW www, string passw)
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
                //yield return new WaitForSeconds(2);
                parsePlayerInfo(text);
                LoginSave Info = new LoginSave();
                Info.username = Player.playername;
                Info.password = passw;
                SaveLogin(Info);
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
        Guid test = new Guid(splitString[6]);
        Player.playerID = (test);
    }


    //next scene helpers
    public void goToRegister()
    {
        //SceneManager.LoadScene("Register");
        Debug.Log("loading");
        loadingScreen.GetComponent<SceneLoader>().LoadScene("Register");
    }
    public void goToOverworld()
    {
        //SceneManager.LoadScene("Overworld");
        Debug.Log("loading");
        loadingScreen.GetComponent<SceneLoader>().LoadScene("Overworld");
    }
}
