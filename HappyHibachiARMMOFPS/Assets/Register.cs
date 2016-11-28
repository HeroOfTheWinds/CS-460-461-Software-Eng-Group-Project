using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using UnityEngine.SceneManagement;

public class Register : MonoBehaviour {
    private string email, password1, password2; 
    public static string username;
    public Text ErrorText; //input fields
    public InputField emailInput, userInput, passwordInput1, passwordInput2;
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
        if (emailIsValid)
        {
            usernameIsValid = validateUsername(username);
        }

        if (usernameIsValid)
        {
            passwordIsValid = validatePassword(password1);
        }

        if (passwordIsValid)
        {
            passwordsAreSame = comparePasswords(password1, password2);
        }

        showErrors(emailIsValid, usernameIsValid, passwordIsValid, passwordsAreSame);

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
    private bool validatePassword(string password1)
    {
        // at least one lowercase, one uppercase, one number, one special character, 8-16 length
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
    void Update()
    {
        
    }
}
