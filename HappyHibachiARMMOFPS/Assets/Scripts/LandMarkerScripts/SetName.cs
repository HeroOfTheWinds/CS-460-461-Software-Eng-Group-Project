using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class SetName : MonoBehaviour {
    public Text NameText;
    public void setname()
    {
        Debug.Log("set_name called");
        Text Name = NameText.GetComponent<Text>();
        Debug.Log(Name.text + " saved.");

    }
}
