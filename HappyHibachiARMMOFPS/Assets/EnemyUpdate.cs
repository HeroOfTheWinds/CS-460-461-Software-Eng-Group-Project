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

    private float sfx = 0;
    private float sfz = 0;
    private float sfr = 0;

    private float mpx = 0;
    private float mpy = 0;


    public void runUpdate(GameObject enemy)
    {
        //Debug.Log(xPos);
        //set is a method on vector 3, your setting a copy, so the actual isnt updating
        //enemy.transform.position.Set(xPos, 0, ZPos);
        //Debug.Log(enemy.transform.position.x);
        //enemy.transform.rotation.Set(0, rot, 0, 0);

        Vector3 updateVector = new Vector3(xPos, 0, zPos + 12);
        Quaternion updateQuat = new Quaternion(0, rot + 180, 0, 0);

        enemy.transform.position = updateVector;
        enemy.transform.rotation = updateQuat;

        //deal with shots and mines and whatnot later, for now ensure motion working
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

    public float Sfr
    {
        get
        {
            return sfr;
        }

        set
        {
            sfr = value;
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

    public float Mpy
    {
        get
        {
            return mpy;
        }

        set
        {
            mpy = value;
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
}