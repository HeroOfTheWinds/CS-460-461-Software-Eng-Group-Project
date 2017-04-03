using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ARDowsing : MonoBehaviour {

    // This script creates a dowsing machine minigame
    // The object to to search AR until within a certain threshold of 
    // a target camera angle, at which point a secret is revealed.

    // Create the target angle
    public Vector3 targetAngle = new Vector3(30f, 120f, 0f); // z should always be zero
    // z rotation on a camera is like turning a steering wheel.

    // Make object references
    public GameObject cam; // Main camera
    public Image dowser; // UI image used as the dowsing indicator
    public GameObject beacon; // what we want to show up when found
    public Canvas choiceDialogue; // Menu to display when the beacon is found
    public GameObject explosion; // particle system to show if user decides to destroy the beacon

    // Control variables
    public float thres = 15f; // tolerance in degrees for a correct matching angle
    float speed = 3f; // Base speed at which dowsing indicator grows
    // gets faster the closer to the correct angle you are.

    bool isAnimating = false;
    float animTime = 5f; // 3 seconds
    float animSpeed = 0f;
    bool found = false;

    float totalTime = 0f;

    GameObject beaconInstance;
    Animator anim;

    // DEBUG ZONE
    /*public Text xx;
    public Text yy;
    public Text zz;
    */

    // TODO: change circle's color in addition to speed

	// Use this for initialization
	void Start () {
        choiceDialogue.enabled = false;
        anim = dowser.GetComponent<Animator>();
		// Randomly generate target angle
	}
	
	// Update is called once per frame
	void Update () {
		// Get user tap input to do a dowse
        //if (Input.GetTouch(0).phase == TouchPhase.Ended)
        //{
            // check how close the angle is to the camera's angle
            Vector3 curAngle = cam.transform.rotation.eulerAngles;

        /* // DEBUG
        xx.text = curAngle.x.ToString();
        yy.text = curAngle.y.ToString();
        zz.text = curAngle.z.ToString();
        */

        // xy deviation
        float xdev, ydev;

        // Calc how far off we are
        xdev = Mathf.Abs(curAngle.x - targetAngle.x);
        ydev = Mathf.Abs(curAngle.y - targetAngle.y);

        // Check if we're within tolerance range
        if (xdev <= thres && ydev <= thres && !found)
        {
            // Hey, we found it! Display what needs to be shown
            GameObject newObj = (GameObject)Instantiate(beacon, cam.transform.position, cam.transform.rotation);
            newObj.transform.Translate(newObj.transform.forward * 3);
            beaconInstance = newObj; // store for later use

            // Display the prompt
            choiceDialogue.enabled = true;
            found = true;
        }
        else // Animate to show how close we are
        {
            // derive how fast to make the dowse
            // should weight between 0 and 1
            float modifier = ((xdev + ydev) / 2f) / 180f;
            if (modifier < 0.1f)
                modifier = 0.1f;

            //animSpeed = modifier * speed;

            // state that we're animating now
            isAnimating = true;

            anim.speed = 1f / modifier;
        }
        //}

        // if we're still animating from the last touch
        /*if (isAnimating)
        {
            // increment timer
            totalTime += Time.deltaTime;

            // Check if time is expired, if so reset size and end timer.
            /*if (totalTime > animTime)
            {
                isAnimating = false;
                dowser.rectTransform.localScale = new Vector3(0.1f, 0.1f, 1);
                totalTime = 0f;
            }
            else
            {
                // scale up the dowsing image
                Vector3 curScale = dowser.rectTransform.localScale;
                curScale.x += animSpeed * Time.deltaTime;
                curScale.y = curScale.x;

                // Check that we haven't exceeding 1.1x scale, reset if so
                if (curScale.x > 1.1f)
                {
                    curScale.x = 0.1f;
                    curScale.y = 0.1f;
                }

                dowser.rectTransform.localScale = curScale;
            //}            
        }*/
	}

    // Functions for each choice of what to do to the beacon
    public void RepairBeacon()
    {
        // Code here to award Paragon experience and return to overworld
    }

    public void IgnoreBeacon()
    {
        // Code here to award Hunter experience and return to overworld
    }

    public void DestroyBeacon()
    {
        // Create an explosion where the beacon was
        GameObject boom = (GameObject)Instantiate(explosion, beaconInstance.transform.position, Quaternion.identity);
        // Destroy the beacon
        Destroy(beaconInstance);

        // Code here to award Slayer experience and return to overworld
    }
}
