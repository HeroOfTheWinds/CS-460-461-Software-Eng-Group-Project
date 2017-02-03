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

    // Speed for dragging disks via touch
    public float dragSpeed = 0.05f;

    // Make three stacks to hold what each peg as on it (logically)
    private Stack<int> peg1;
    private Stack<int> peg2;
    private Stack<int> peg3;

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
        // Start with a half-finished puzzle
        peg1.Push(3);
        peg1.Push(2);
        peg1.Push(1);

        peg3.Push(5);
        peg3.Push(4);

        // Show the energy guage 40% full (currently broken)
        Material[] mats = EnergyBarRenderer.materials;
        mats[2].mainTextureScale = new Vector2(0f, 0.4f);
        EnergyBarRenderer.materials = mats;
        energy = 0.4f;
	}
	
	// Update is called once per frame
	void Update () {
	    // Get touch input and process it
        // First check only one touch
        if (Input.touchCount == 1)
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
        }
	}
}
