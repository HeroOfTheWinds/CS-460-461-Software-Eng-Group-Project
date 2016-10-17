using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class display_coordinates : MonoBehaviour {
    private Text txt;
    public GameObject obj;
    string xs, ys;
    public void displaycoordinates(string x, string y)
    {
        Debug.Log("display_coordinates called");
        txt = obj.GetComponent<Text>();
        txt.text = x + " " + y;
        //return txt;
    }
}