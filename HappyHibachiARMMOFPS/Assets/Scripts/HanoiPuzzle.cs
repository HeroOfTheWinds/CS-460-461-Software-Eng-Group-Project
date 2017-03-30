using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class HanoiPuzzle : MonoBehaviour {

    // This script implements a modified Towers of Hanoi AR puzzle

    // GameObjects of the disks for the puzzle (small to large)
    public GameObject DiskXS; //1
    public GameObject DiskS; //2
    public GameObject DiskM; //3
    public GameObject DiskL; //4
    public GameObject DiskXL; //5

    public Renderer EnergyBarRenderer;

    // Canvases to show win and loss screens
    public GameObject WinCanvas;
    public GameObject LoseCanvas;

    // Speed for dragging disks via touch
    public float dragSpeed = 0.05f;

    // Materials used to mark if a disk is selected or not
    public Material normal;
    public Material selected;

    // Make three stacks to hold what each peg as on it (logically)
    private Stack<int> peg1 = new Stack<int>();
    private Stack<int> peg2 = new Stack<int>();
    private Stack<int> peg3 = new Stack<int>();

    // Status of the generator
    private float energy = 0f;

    // Store where the last touched disk is located
    private float diskX = 0f;
    private float diskY = 0f;

    // most recently touched disk
    private GameObject lastDisk;
    private int lastIndex;
    // Still moving it?
    private bool moving = false;

    // Use this for initialization
    void Start () {
        peg1.Push(100);
        peg2.Push(100);
        peg3.Push(100);
        // Start with a half-finished puzzle
        peg1.Push(3);
        peg1.Push(2);
        peg1.Push(1);

        peg3.Push(5);
        peg3.Push(4);

        // Show the energy guage 40% full (currently broken)
        /*Material[] mats = EnergyBarRenderer.materials;
        mats[2].mainTextureScale = new Vector2(0f, 0.4f);
        EnergyBarRenderer.materials = mats;
        energy = 0.4f;*/
	}
	
	// Update is called once per frame
	void Update () {
	    // Get touch input and process it
        // First check only one touch
        /*if (Input.touchCount == 1)
        {
            // Check if the touch is just starting
            if (Input.GetTouch(0).phase == TouchPhase.Began)
            {
                // Check if the user touched a disk
                Ray ray = Camera.main.ScreenPointToRay(Input.GetTouch(0).position);
                RaycastHit hit;
                Debug.DrawRay(ray.origin, ray.direction, Color.red, 20f, true);
                if (Physics.Raycast(ray, out hit))
                {
                    if (hit.collider.tag == "Hanoi Disk")
                    {
                        // Record where this disk is
                        diskX = hit.transform.position.x;
                        diskY = hit.transform.position.y;
                        // z won't change

                        // which disk was it?
                        lastDisk = hit.collider.gameObject;

                        // peek at the stacks to see if this disk is on top of one
                        if (lastDisk == DiskXS)
                        {
                            if (peg1.Peek() == 1 || peg2.Peek() == 1 || peg3.Peek() == 1)
                            {
                                // legal move, allow dragging
                                moving = true;
                                lastIndex = 1;
                            }
                        }
                        else if (lastDisk == DiskS)
                        {
                            if (peg1.Peek() == 2 || peg2.Peek() == 2 || peg3.Peek() == 2)
                            {
                                // legal move, allow dragging
                                moving = true;
                                lastIndex = 2;
                            }
                        }
                        else if (lastDisk == DiskM)
                        {
                            if (peg1.Peek() == 3 || peg2.Peek() == 3 || peg3.Peek() == 3)
                            {
                                // legal move, allow dragging
                                moving = true;
                                lastIndex = 3;
                            }
                        }
                        else if (lastDisk == DiskL)
                        {
                            if (peg1.Peek() == 4 || peg2.Peek() == 4 || peg3.Peek() == 4)
                            {
                                // legal move, allow dragging
                                moving = true;
                                lastIndex = 4;
                            }
                        }
                        else if (lastDisk == DiskXL)
                        {
                            if (peg1.Peek() == 5 || peg2.Peek() == 5 || peg3.Peek() == 5)
                            {
                                // legal move, allow dragging
                                moving = true;
                                lastIndex = 5;
                            }
                        }
                    }
                }
            }
            else if (Input.GetTouch(0).phase == TouchPhase.Moved)
            {
                // Move the disk if we have been touching one
                if (moving)
                {
                    // Get movement of the finger since last frame
                    Vector2 touchDelta = Input.GetTouch(0).deltaPosition;

                    // Move the most recent disk accordingly
                    lastDisk.transform.Translate(new Vector3(touchDelta.x * dragSpeed, touchDelta.y * dragSpeed, 0f));

                    // update variables
                    diskX = diskX + touchDelta.x * dragSpeed;
                    diskY = diskY + touchDelta.y * dragSpeed;
                }
            }
            else if (Input.GetTouch(0).phase == TouchPhase.Ended)
            {
                // check if we just finished moving a disk
                if (moving)
                {
                    // Determine which peg to fit it on
                    if (diskX > 0.25f)
                    {
                        // left peg
                        // Snap location to fall onto peg
                        Vector3 tempPos = lastDisk.transform.position;
                        tempPos.x = 0.5f;
                        tempPos.y = 2.35f;
                        lastDisk.transform.position = tempPos;

                        // Update the stack
                        peg1.Push(lastIndex);
                    }
                    else if (diskX <= 0.25f && diskX >= -0.25)
                    {
                        // center peg
                        // Snap location to fall onto peg
                        Vector3 tempPos = lastDisk.transform.position;
                        tempPos.x = 0f;
                        tempPos.y = 2.35f;
                        lastDisk.transform.position = tempPos;

                        // Update the stack
                        peg2.Push(lastIndex);
                    }
                    else if (diskX < -0.25f)
                    {
                        // right peg
                        // Snap location to fall onto peg
                        Vector3 tempPos = lastDisk.transform.position;
                        tempPos.x = -0.5f;
                        tempPos.y = 2.35f;
                        lastDisk.transform.position = tempPos;

                        // Update the stack
                        peg3.Push(lastIndex);
                    }

                    // Say we're done moving
                    moving = false;
                }
            }
        }*/
	}

    // Functions for the buttons
    // These handle moving disks between the pegs
    public void ButtonL()
    {
        // Check if we already have a disk selected
        if (moving)
        {
            // Try placing the currently held disk onto the Left peg
            peg1.Push(lastIndex);
            Vector3 tempPos = lastDisk.transform.position;
            tempPos.x = 0.5f;
            tempPos.y = 2.35f;
            lastDisk.transform.position = tempPos;
            // Return material to normal
            lastDisk.GetComponent<Renderer>().material = normal;

            // Did you fail the puzzle?
            int upper, lower;
            upper = peg1.Pop();
            lower = peg1.Peek();
            if (upper > lower)
            {
                // put a larger disk on a smaller one, FAIL
                peg1.Push(upper);
                DisplayLoss();
            }
            else
            {
                // put it back, we're clear
                peg1.Push(upper);
            }

            // done moving
            moving = false;
        }
        else
        {
            // set this as the disk we're moving
            lastIndex = peg1.Pop();

            // check if empty peg
            if (lastIndex == 100)
            {
                peg1.Push(lastIndex);
                return;
            }

            // get the gameObject corresponding to this disk
            switch (lastIndex)
            {
                case 1:
                    lastDisk = DiskXS;
                    break;
                case 2:
                    lastDisk = DiskS;
                    break;
                case 3:
                    lastDisk = DiskM;
                    break;
                case 4:
                    lastDisk = DiskL;
                    break;
                case 5:
                    lastDisk = DiskXL;
                    break;
                default:
                    break;
            }

            // Show this disk is selected
            lastDisk.GetComponent<Renderer>().material = selected;

            // say we're moving something
            moving = true;
        }
    }

    public void ButtonC()
    {
        // Check if we already have a disk selected
        if (moving)
        {
            // Try placing the currently held disk onto the Left peg
            peg2.Push(lastIndex);
            Vector3 tempPos = lastDisk.transform.position;
            tempPos.x = 0f;
            tempPos.y = 2.35f;
            lastDisk.transform.position = tempPos;
            // Return material to normal
            lastDisk.GetComponent<Renderer>().material = normal;

            // Did you fail the puzzle?
            int upper, lower;
            upper = peg2.Pop();
            lower = peg2.Peek();
            if (upper > lower)
            {
                // put a larger disk on a smaller one, FAIL
                peg2.Push(upper);
                DisplayLoss();
            }
            else
            {
                // put it back, we're clear
                peg2.Push(upper);
            }

            // done moving
            moving = false;
        }
        else
        {
            // set this as the disk we're moving
            lastIndex = peg2.Pop();

            // check if empty peg
            if (lastIndex == 100)
            {
                peg2.Push(lastIndex);
                return;
            }

            // get the gameObject corresponding to this disk
            switch (lastIndex)
            {
                case 1:
                    lastDisk = DiskXS;
                    break;
                case 2:
                    lastDisk = DiskS;
                    break;
                case 3:
                    lastDisk = DiskM;
                    break;
                case 4:
                    lastDisk = DiskL;
                    break;
                case 5:
                    lastDisk = DiskXL;
                    break;
                default:
                    break;
            }

            // Show this disk is selected
            lastDisk.GetComponent<Renderer>().material = selected;

            // say we're moving something
            moving = true;
        }
    }

    public void ButtonR()
    {
        // Check if we already have a disk selected
        if (moving)
        {
            // Try placing the currently held disk onto the Left peg
            peg3.Push(lastIndex);
            Vector3 tempPos = lastDisk.transform.position;
            tempPos.x = -0.5f;
            tempPos.y = 2.35f;
            lastDisk.transform.position = tempPos;
            // Return material to normal
            lastDisk.GetComponent<Renderer>().material = normal;

            // Did you fail the puzzle?
            int upper, lower;
            upper = peg3.Pop();
            lower = peg3.Peek();
            if (upper > lower)
            {
                // put a larger disk on a smaller one, FAIL
                peg3.Push(upper);
                DisplayLoss();
            }
            else
            {
                // put it back, we're clear
                peg3.Push(upper);

                // Now check if the puzzle is finished
                if (peg3.Count == 6)
                {
                    // all moves til now were legal, so if we have five disks, it is finished.
                    DisplayWin();
                }
            }

            // done moving
            moving = false;
        }
        else
        {
            // set this as the disk we're moving
            lastIndex = peg3.Pop();

            // check if empty peg
            if (lastIndex == 100)
            {
                peg3.Push(lastIndex);
                return;
            }

            // get the gameObject corresponding to this disk
            switch (lastIndex)
            {
                case 1:
                    lastDisk = DiskXS;
                    break;
                case 2:
                    lastDisk = DiskS;
                    break;
                case 3:
                    lastDisk = DiskM;
                    break;
                case 4:
                    lastDisk = DiskL;
                    break;
                case 5:
                    lastDisk = DiskXL;
                    break;
                default:
                    break;
            }

            // Show this disk is selected
            lastDisk.GetComponent<Renderer>().material = selected;

            // say we're moving something
            moving = true;
        }
    }

    public void DisplayLoss()
    {
        // Battle was lost, so create a lose screen overlay
        GameObject loss = (GameObject)Instantiate(LoseCanvas);

        // Set it to render over the local Main Camera
        loss.GetComponent<Canvas>().worldCamera = Camera.main;
    }

    public void DisplayWin()
    {
        // Battle was won, so create a win screen overlay
        GameObject win = (GameObject)Instantiate(WinCanvas);

        // Set it to render over the local Main Camera
        win.GetComponent<Canvas>().worldCamera = Camera.main;
    }
}
