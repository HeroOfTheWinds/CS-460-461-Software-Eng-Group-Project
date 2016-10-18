using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Login : MonoBehaviour {
    private string username, password;
    public Text userInput, ErrorText;
    private string url = "http://132.160.49.90:7001/login.php";
    public void checkLogin()
    {
        
        username = userInput.text;
        //password = passwordInput.text;
        var form = new WWWForm();
        form.AddField("usernameFromUnity", username);
        //form.AddField("passwordFromUnity", password);
        WWW send = new WWW(url, form);
        StartCoroutine(WaitForRequest(send));
        
        if (!string.IsNullOrEmpty(send.error))
        {
            print("There was an error logging in: " + send.error);
        }
        WWW recieve = new WWW(url);
        StartCoroutine(WaitForRequest(recieve));
        Debug.Log(recieve.text);
        if (!string.IsNullOrEmpty(recieve.error))
        {
            print("There was an error logging in:  " + recieve.error);
        }
        ErrorText.text = " working ";
    }
    IEnumerator WaitForRequest(WWW www)
    {
        yield return www;

        // check for errors
        if (www.error == null)
        {
            Debug.Log("WWW Ok!: " + www.text);
        }
        else
        {
            Debug.Log("WWW Error: " + www.error);
        }
    }
}
