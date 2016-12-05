using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;

public class Register : MonoBehaviour {
    private string email, password1, password2; //info to validate and ask from database
    public static string username; //if successful, passed to login screen
    public Text ErrorText; //displays errors
    public InputField emailInput, userInput, passwordInput1, passwordInput2; // user input fields
    private string url = "http://132.160.49.90:7001/register.php"; //script to check email + username in database
    public void checkRegisterInputs()
    {
        bool emailIsValid = false, usernameIsValid = false, 
             passwordIsValid = false, passwordsAreSame = false; //intialize validation vars 
        
        //collect inputs from user
        email = emailInput.text;
        username = userInput.text;
        password1 = passwordInput1.text;
        password2 = passwordInput2.text;

        //check inputs and return bool
        emailIsValid = validateEmail(email);
        if (emailIsValid) {
            usernameIsValid = validateUsername(username);
        }

        if (usernameIsValid)  {
            passwordIsValid = validatePassword(password1);
        }

        if (passwordIsValid) {
            passwordsAreSame = comparePasswords(password1, password2);
        }

        //changes color of field if good (green) or bad (red)
        showErrors(emailIsValid, usernameIsValid, passwordIsValid, passwordsAreSame);

        //create form if all inputs good
        if (emailIsValid && usernameIsValid && passwordIsValid && passwordsAreSame)
        {
            var form = new WWWForm();
            form.AddField("emailFromUnity", email);
            form.AddField("usernameFromUnity", username);
            form.AddField("passwordFromUnity", password1);
            //send form to register.php
            WWW send = new WWW(url, form);
            StartCoroutine(WaitForRequest(send));
        }
    }

    IEnumerator WaitForRequest(WWW www)
    {
        yield return www;
        if (www.error == null)
        {
            string text = www.text.Substring(www.text.Length - 1, 1);
            //Register.php returns 0,1,2 based on whether email and/or username available
            //Debug.Log(text);
            if (text == "0")
            {
                Debug.Log("Registration successful");
                //go to login scene to log in
                SceneManager.LoadScene("Login");
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

    //returns true if email valid: need to create better regex fro emails
    private bool validateEmail(string email) 
    {
        string local_part = ".";
        string domain = ".";
        string emailPattern = local_part + "@" + domain;
        if (System.Text.RegularExpressions.Regex.IsMatch(email, emailPattern)) {
            return true;
        }
        ErrorText.text = "Invalid Email. Please try again.";
        return false;
    }

    //returns true if username valid: accepts 1-32 characters, numbers, letters, underscores only
    private bool validateUsername(string username)
    {
        string usernamePattern = "^[a-zA-Z0-9_]+$";
        if (username.Length < 32 && System.Text.RegularExpressions.Regex.IsMatch(username, usernamePattern))
        {
            userInput.GetComponent<Image>().color = Color.green;
            return true;
        }
        ErrorText.text = "Invalid Username.\n- 1-32 characters\n- Alphanumeric and underscores allowed\n Please try again.";
        userInput.GetComponent<Image>().color = Color.red;
        return false;
    }

    //returns true if valid password: // at least one lowercase, one uppercase, one number, one special character, 8-16 length
    private bool validatePassword(string password1)
    {
        string passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&)_(*-]).{8,16}$";
        if (System.Text.RegularExpressions.Regex.IsMatch(password1, passwordPattern))
        {
            passwordInput1.GetComponent<Image>().color = Color.green;
            return true;
        }
        ErrorText.text = "Invalid Password. \n-8-16 characters\n-At least:\none lowercase letter\none uppercase letter\none number\none special character(#?!@$%^&)_(*-])\n.Please try again.";
        passwordInput1.GetComponent<Image>().color = Color.red;
        return false;
    }

    // returns true if passwords are the same
    private bool comparePasswords(string password1, string password2)
    {
        if (password1 == password2)
        {
            passwordInput2.GetComponent<Image>().color = Color.green;
            return true;
        }
        passwordInput2.GetComponent<Image>().color = Color.red;
        ErrorText.text = "Passwords do not match. Please try again.";
        return false;
    }
    // changes color of input fields if inputs are good or bad
    void showErrors(bool e, bool u, bool p, bool p2)
    {
        if(e) emailInput.GetComponent<Image>().color = Color.green;
        else emailInput.GetComponent<Image>().color = Color.red;

        if (u) userInput.GetComponent<Image>().color = Color.green;
        else userInput.GetComponent<Image>().color = Color.red;

        if (p) passwordInput1.GetComponent<Image>().color = Color.green;
        else passwordInput1.GetComponent<Image>().color = Color.red;

        if (p2) passwordInput2.GetComponent<Image>().color = Color.green;
        else passwordInput2.GetComponent<Image>().color = Color.red;
    }
}
