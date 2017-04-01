using UnityEngine;
using System.Collections;
using UnityStandardAssets.CrossPlatformInput;
using System;

public class EnemyUpdate
{
    //where was the opponent when it sent the update?
    //private float xPos = 0;
    //private float zPos = 0;
    //private float rot = 0;
    
    //flags, least sig to most sig bit
    //is the battle over?
    private bool battleEnd = false;
    //did the enemy win? (unused)
    private bool win = false;

    // Attack stat of the enemy player
    private int attack;

    //was a shot fired?
    private bool sf = false;
    //was hp regenerated?
    private bool hpr = false;
    //was a mine placed?
    private bool mp = false;
    //was a mine set off? (unused)
    private bool mso = false;
    //did a shot hit the player?
    private bool phit = false;

    //where was the opponent when it fired the shot?
    //x and z coords
    private float sfx = 0;
    private float sfz = 0;
    //camera rotation
    private float sfrx = 0;
    private float sfry = 0;
    private float sfrz = 0;
    //where was the opponent when it placed the mine?
    private float mpx = 0;
    private float mpz = 0;

    private static readonly int MAX_RECORDS = 5;
    private int numRecords;
    //store time as double precision to ensure minimal loss of precision (battle should never last long enough to overflow a float, but use doubles just to be safer)
    private double[] t;
    private float[] x;
    private float[] z;
    private float[] r;
    //store last time for interpolation
    private double lastT;

    private bool updateRun;

    public void addUpdate(float xPos, float zPos, float rot, double time)
    {
        Debug.Log("num records = " + numRecords);
        //if buffer isn't full, store update in next position
        if(numRecords < MAX_RECORDS)
        {
            x[numRecords] = xPos;
            z[numRecords] = zPos;
            r[numRecords] = rot;
            t[numRecords] = time;
            numRecords++;
        }
        //if buffer full, shift updates to the left and place current update at the end
        else
        {
            Array.Copy(x, 1, x, 0, MAX_RECORDS - 1);
            x[MAX_RECORDS - 1] = xPos;
            Array.Copy(z, 1, z, 0, MAX_RECORDS - 1);
            z[MAX_RECORDS - 1] = zPos;
            Array.Copy(r, 1, r, 0, MAX_RECORDS - 1);
            r[MAX_RECORDS - 1] = rot;
            Array.Copy(t, 1, t, 0, MAX_RECORDS - 1);
            t[MAX_RECORDS - 1] = time;
        }
    }

    public EnemyUpdate()
    {
        // Retrieve Attack stat
        attack = GameObject.FindGameObjectWithTag("Enemy").GetComponent<EnemyStatus>().getAttack();
        t = new double[MAX_RECORDS];
        x = new float[MAX_RECORDS];
        z = new float[MAX_RECORDS];
        r = new float[MAX_RECORDS];
        //initialize first record to initial opponent position at time 0;
        t[0] = 0;
        x[0] = 0;
        z[0] = 12;
        r[0] = 180;
        lastT = 0;
        numRecords = 1;
        updateRun = false;
    }

    public void extrapolateMotion(double time, GameObject enemy)
    {
        //do not extrapolate motion until at least one update is received and run
        if(updateRun)
        {
            float xPos, zPos, rot;
            double[] a = new double[numRecords + 1];
            double[] h = new double[numRecords + 1];
            bSpline2Coef(numRecords, x, ref a, ref h);
            xPos = bSpline2Eval(numRecords, a, h, time);
            bSpline2Coef(numRecords, z, ref a, ref h);
            zPos = bSpline2Eval(numRecords, a, h, time);
            bSpline2Coef(numRecords, r, ref a, ref h);
            rot = bSpline2Eval(numRecords, a, h, time);

            //set up enemy's position and rotation from extrapolation
            Vector3 updateVector = new Vector3(xPos, 0, zPos);
            Quaternion updateQuat = Quaternion.Euler(0, rot, 0);

            //set enemy position and rotation
            enemy.transform.position = updateVector;
            enemy.transform.rotation = updateQuat;

            lastT = time;
        }
        
    }

    public void runUpdate(PlayerControl controller, GameObject enemy)
    {
        //Debug.Log(battleEnd);
        //Debug.Log("update run start");
        //Debug.Log(rot);
        updateRun = true;
        double time = t[numRecords - 1];
        //interpolate 4 subintervals between last position and this position
        double interval = (time - lastT) / 5;

        float xPos, zPos, rot;
        double[] a = new double[numRecords + 1];
        double[] h = new double[numRecords + 1];
        Vector3 updateVector;
        Quaternion updateQuat;

        //only estimate for 4 subintervals, use exact position for end point
        for (int i = 0; i < 4; i++)
        {
            //start at the first time interval after current position
            lastT += interval;
            bSpline2Coef(numRecords, x, ref a, ref h);
            xPos = bSpline2Eval(numRecords, a, h, lastT);
            bSpline2Coef(numRecords, z, ref a, ref h);
            zPos = bSpline2Eval(numRecords, a, h, lastT);
            bSpline2Coef(numRecords, r, ref a, ref h);
            rot = bSpline2Eval(numRecords, a, h, lastT);
            updateVector = new Vector3(xPos, 0, zPos);
            updateQuat = Quaternion.Euler(0, rot, 0);
            enemy.transform.position = updateVector;
            enemy.transform.rotation = updateQuat;
        }


        //set up enemy's position and rotation from update
        updateVector = new Vector3(x[numRecords - 1], 0, z[numRecords - 1]);
        updateQuat = Quaternion.Euler(0, r[numRecords - 1], 0);

        //set enemy position and rotation
        enemy.transform.position = updateVector;
        enemy.transform.rotation = updateQuat;

        //set the last time to the update time
        lastT = time;

        //Debug.Log("update run end");

        //check if enemy fired a shot
        if (sf)
        {
            //get position of shot
            Vector3 sPos = new Vector3(sfx, Camera.main.transform.position.y, sfz); //camera y axis should be identical for each player
            // Get direction of raycast
            Vector3 shotDir = new Vector3(sfrx, sfry, sfrz);
            //get rotation of shot
            Quaternion shotRot = Quaternion.FromToRotation(sPos, shotDir);

            // Fire a shot by instantiating a bullet and calculating with a raycast
            // First get orientation of enemy and adjust laser's start position so it's outside the player's collider
            Vector3 shotPos = enemy.transform.TransformDirection(1f, -0.5f, 1f) + sPos;
            

            // Make a raycast from the enemy to check for target hit
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
                        //hit.collider.gameObject.GetComponent<EnemyStatus>().TakeHP(8f);
                        Debug.Log("Player hit");
                        break;
                    default:
                        // Other cases to consider: wall, arena border, ground
                        break;
                }
            }

            // Instantiate shot
            controller.makeShot(shotPos, shotRot, endPoint);

            //was the player hit?
            if(phit)
            {
                controller.hit(attack);
            }

        }
        //rotation of mine doesnt really matter, so just use current rotation
        //instantiate mine if placed by enemy
        if (mp)
        {
            controller.PlaceLandmine(new Vector3(mpx, 0, mpz), updateQuat, enemy.GetInstanceID());

        }
        
        if(hpr)
        {
            enemy.GetComponent<EnemyStatus>().RestoreHP(50f);
        }
    }

    void bSpline2Coef(int n, float[] y, ref double[] a, ref double[] h)
    {
        int i;
        double delta, gamma, p, q, r;
        for (i = 1; i < n; i++)
        {
            h[i] = t[i] - t[i - 1];
        }

        h[0] = h[1];
        h[n] = h[n - 1];
        delta = -1;
        gamma = 2 * y[0];
        p = delta * gamma;
        q = 2;
        for (i = 1; i < n; i++)
        {
            r = h[i + 1] / h[i];
            delta = -r * delta;
            gamma = -r * gamma + y[i] * (r + 1);
            p = p + gamma * delta;
            q = q + Math.Pow(delta, 2);
        }
        a[0] = -p / q;
        for (i = 1; i < n + 1; i++)
        {
            a[i] = ((h[i - 1] + h[i]) * y[i - 1] - h[i] * a[i - 1]) / h[i - 1];
        }
    }

    //compute approximate value of function at given point
    float bSpline2Eval(int n, double[] a, double[] h, double x)
    {
        int i;
        double d, e;
        for (i = n - 2; n >= 0; i--)
        {
            if (x - t[i] >= 0)
            {
                break;
            }
        }
        i++;
        d = (a[i + 1] * (x - t[i - 1]) + a[i] * (t[i] - x + h[i + 1])) / (h[i] + h[i + 1]);
        e = (a[i] * (x - t[i - 1] + h[i - 1]) + a[i - 1] * (t[i - 1] - x + h[i])) / (h[i - 1] + h[i]);
        //need a value in single precision for usage with unity methods
        return (float)((d * (x - t[i - 1]) + e * (t[i] - x)) / h[i]);
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
    /*
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
    */
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

    /*
    public int NumRecords
    {
        set
        {
            if(value <= MAX_RECORDS)
            {
                numRecords = value;
            }
            else
            {
                numRecords = MAX_RECORDS;
            }
        }
    }
    */
}