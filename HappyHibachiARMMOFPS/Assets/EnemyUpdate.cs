using UnityEngine;
using System.Collections;
using UnityStandardAssets.CrossPlatformInput;

public class EnemyUpdate
{
    private float xPos = 0;
    private float zPos = 12;
    private float rot = 180;
    
    //flags, least sig to most sig bit
    private bool battleEnd = false;
    private bool win = false;

    private bool sf = false;
    private bool hpr = false;
    private bool mp = false;
    private bool mso = false;
    private bool phit = false;

    private float sfx = 0;
    private float sfz = 0;
    private float sfrx = 0;
    private float sfry = 0;
    private float sfrz = 0;

    private float mpx = 0;
    private float mpz = 0;


    public void runUpdate(PlayerControl controller, GameObject enemy)
    {
        //Debug.Log("update run start");
        //set is a method on vector 3, your setting a copy, so the actual isnt updating
        //enemy.transform.position.Set(xPos, 0, ZPos);
        //Debug.Log(enemy.transform.position.x);
        //enemy.transform.rotation.Set(0, rot, 0, 0);

        //Debug.Log(rot);

        Vector3 updateVector = new Vector3(xPos, 0, zPos);
        Quaternion updateQuat = Quaternion.Euler(0, rot, 0);

        enemy.transform.position = updateVector;
        enemy.transform.rotation = updateQuat;

        //Debug.Log("update run end");


        if (sf)
        {
            //get position of shot
            Vector3 sPos = new Vector3(sfx, Camera.main.transform.position.y, sfz); //camera y axis should be identical for each player
            // Get direction of raycast
            Vector3 shotDir = new Vector3(sfrx, sfry, sfrz);
            //get rotation of shot
            Quaternion shotRot = Quaternion.FromToRotation(sPos, shotDir);

            // Fire a shot by instantiating a bullet and calculating with a raycast
            // First get orientation of camera and adjust laser's start position so it's outside the player's collider
            Vector3 shotPos = enemy.transform.TransformDirection(1f, -0.5f, 1f) + sPos;
            

            // Make a raycast from the camera to check for target hit
            RaycastHit hit; // Var to store info on what got hit
            // Location in world space of the ray's endpoint
            Vector3 endPoint = Vector3.zero;

            //Debug.DrawRay(cam.transform.position, cam.transform.forward*200f, Color.red, 20f, true);

            // Test if the raycast hits anything
            if (Physics.Raycast(sPos, shotDir, out hit, 200f))  //if (Physics.Raycast(sPos, shotRot * shotPos, out hit, 200f))
            {
                // Retrieve endpoint
                endPoint = hit.point;

                // Check what we hit and act accordingly
                switch (hit.collider.tag)
                {
                    case "Player":
                        // Get that player's stats and take off some HP
                        //USING PHIT TO TELL IF HIT FOR NOW (until raycasting bugs fixed)
                        //hit.collider.gameObject.GetComponent<PlayerStatus>().TakeHP(8f);
                        Debug.Log("Player hit");
                        break;
                    default:
                        // Other cases to consider: wall, arena border, ground
                        break;
                }
            }

            // Change origin of shot to make it look like it's coming from the gun
            //shotPos = transform.TransformDirection(1f, -0.5f, 1f) + cam.transform.position;

            // Instantiate shot
            controller.makeShot(shotPos, shotRot, endPoint);

            //was the player hit?
            if(phit)
            {
                controller.hit();
            }

        }
        //rotation of mine doesnt really matter, so just use current rotation
        //instantiate mine if placed by enemy
        if (mp)
        {
            controller.PlaceLandmine(new Vector3(mpx, 0, mpz), updateQuat, enemy.GetInstanceID());

        }
        
        //deal with healing when implemented
    }


    //getters and setters
    public bool BattleEnd
    {
        get
        {
            return battleEnd;
        }

        set
        {
            battleEnd = value;
        }
    }

    public bool Win
    {
        get
        {
            return win;
        }

        set
        {
            win = value;
        }
    }

    public bool Sf
    {
        get
        {
            return sf;
        }

        set
        {
            sf = value;
        }
    }

    public bool Hpr
    {
        get
        {
            return hpr;
        }

        set
        {
            hpr = value;
        }
    }

    public bool Mp
    {
        get
        {
            return mp;
        }

        set
        {
            mp = value;
        }
    }

    public bool Mso
    {
        get
        {
            return mso;
        }

        set
        {
            mso = value;
        }
    }

    public float Sfx
    {
        get
        {
            return sfx;
        }

        set
        {
            sfx = value;
        }
    }

    public float Sfz
    {
        get
        {
            return sfz;
        }

        set
        {
            sfz = value;
        }
    }

    public float Mpx
    {
        get
        {
            return mpx;
        }

        set
        {
            mpx = value;
        }
    }

    public float Mpz
    {
        get
        {
            return mpz;
        }

        set
        {
            mpz = value;
        }
    }

    public float XPos
    {
        get
        {
            return xPos;
        }

        set
        {
            xPos = value;
        }
    }

    public float ZPos
    {
        get
        {
            return zPos;
        }

        set
        {
            zPos = value;
        }
    }

    public float Rot
    {
        get
        {
            return rot;
        }

        set
        {
            rot = value;
        }
    }

    public float Sfrx
    {
        get
        {
            return sfrx;
        }

        set
        {
            sfrx = value;
        }
    }

    public float Sfry
    {
        get
        {
            return sfry;
        }

        set
        {
            sfry = value;
        }
    }

    public float Sfrz
    {
        get
        {
            return sfrz;
        }

        set
        {
            sfrz = value;
        }
    }

    public bool Phit
    {
        get
        {
            return phit;
        }

        set
        {
            phit = value;
        }
    }
}