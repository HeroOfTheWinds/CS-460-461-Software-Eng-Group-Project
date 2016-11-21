using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;

public class Login : MonoBehaviour {
    private string username, password;
    public InputField userInput, passwordInput;
    public Text ErrorText;
    private string url = "http://132.160.49.90:7001/login.php";
    void Start()
    {
        userInput.text = Register.username;
    }
    public void checkLogin()
    {
        
        username = userInput.text;
        password = passwordInput.text;
        var form = new WWWForm();
        form.AddField("usernameFromUnity", username);
        form.AddField("passwordFromUnity", password);
        WWW send = new WWW(url, form);
        StartCoroutine(WaitForRequest(send));        
    }

    IEnumerator WaitForRequest(WWW www)
    {
        yield return www;
        //wait for 
        if (www.error == null)
        {
            string text = www.text.Substring(www.text.Length - 1, 1);
            //Debug.Log(text);
            if (text == "0")
            {
                Debug.Log("Logged In");
                ErrorText.text = "Welcome, " + username;
                //go to next scene
                SceneManager.LoadScene("Overworld");
            }

            if (text == "1")
            {
                Debug.Log("Username not found.");
                ErrorText.text = "Username or password incorrect.";
            }

            if (text == "2")
            {
                Debug.Log("Password incorrect.");
                ErrorText.text = "Username or password incorrect.";
            }
            Debug.Log("Connection good.");
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }
    public void goToRegister()
    {
        SceneManager.LoadScene("Register");
    }
}
