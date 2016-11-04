using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Register : MonoBehaviour {
    private string email, username, password1, password2;
    public Text emailInput, userInput, passwordInput1, passwordInput2, ErrorText;
    bool emailIsValid = false, usernameIsValid = false, passwordIsValid = false, passwordsAreSame;
    private string url = "http://132.160.49.90:7001/register.php";
    public void checkRegisterInputs()
    {
        email = emailInput.text;
        username = userInput.text;
        password1 = passwordInput1.text;
        password2 = passwordInput2.text;
        emailIsValid = validateEmail(email);
        if (!emailIsValid)
        {
            Debug.Log("Invalid email, please try again.");
            
        }
        usernameIsValid = validateUsername(username);
        if (!usernameIsValid)
        {
            Debug.Log("Invalid username, please try again.");
            
        }
        passwordIsValid = validatePassword(password1);
        if (!passwordIsValid)
        {
            Debug.Log("Invalid password, please try again.");
            
        }
        passwordsAreSame = comparePasswords(password1, password2);
        if (!passwordsAreSame)
        {
            Debug.Log("Passwords do not match, please try again.");
            
        }
        if (emailIsValid && usernameIsValid && passwordIsValid && passwordsAreSame)
        {
            var form = new WWWForm();
            form.AddField("emailFromUnity", email);
            form.AddField("usernameFromUnity", username);
            form.AddField("passwordFromUnity", password1);
            WWW send = new WWW(url, form);
            StartCoroutine(WaitForRequest(send));

            // WWW recieve = new WWW(url);
            //StartCoroutine(WaitForRequest(recieve));
            //Debug.Log(recieve.text.ToString());
        }
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
                Debug.Log("Registration successful");
                ErrorText.text = "Welcome, " + username;
                //go to login scene to log in
            }

            if (text == "1")
            {
                Debug.Log("Email already taken.");
                ErrorText.text = "Email already taken.";
            }

            if (text == "2")
            {
                Debug.Log("Username taken.");
                ErrorText.text = "Username taken.";
            }
            Debug.Log("Connection good.");
        }
        else
        {
            Debug.Log("Connection error.");
        }
    }
    private bool validateEmail(string email)
    {
        if (email.Length == 0)
        {
            Debug.Log("Email empty");
            return false;
        }
        return true;
    }
    private bool validateUsername(string username)
    {
        if (username.Length == 0)
        {
            Debug.Log("Username empty");
            return false;
        }
        return true;
    }
    private bool validatePassword(string password1)
    {
        if (password1.Length == 0)
        {
            Debug.Log("Password empty");
            return false;
        }
        return true;
    }
    private bool comparePasswords(string password1, string password2)
    {
        if (password1 == password2)
        {
            return true;
        }
        return false;
    }
}
