using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CloseDialogue : MonoBehaviour {

    //public GameObject dialogue;
    public Canvas dCanvas;

    public void closeDialogue()
    {
        // Destroy(dialogue);
        dCanvas.enabled = false;
    }
}
