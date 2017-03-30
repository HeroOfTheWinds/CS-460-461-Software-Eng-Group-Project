using UnityEngine;
using System.Collections;

// Wrapper script for dealing with gyroscope 
// Taken from http://answers.unity3d.com/questions/434096/lock-rotation-of-gyroscope-controlled-camera-to-fo.html

public static class DeviceRotation {

    private static bool gyroInitialized = false;

    public static bool HasGyroscope
    {
        get
        {
            return SystemInfo.supportsGyroscope;
        }
    }

    public static Quaternion Get()
    {
        if (!gyroInitialized)
        {
            InitGyro();
        }

        return HasGyroscope
            ? ReadGyroscopeRotation()
            : Quaternion.identity;
    }

    private static void InitGyro()
    {
        if (HasGyroscope)
        {
            Input.gyro.enabled = true;                // enable the gyroscope
            Input.gyro.updateInterval = 0.0167f;    // set the update interval to it's highest value (60 Hz)
        }
        gyroInitialized = true;
    }

    private static Quaternion ReadGyroscopeRotation()
    {
        return new Quaternion(0.5f, 0.5f, -0.5f, 0.5f) * Input.gyro.attitude * new Quaternion(0, 0, 1, 0);
    }

    public static float GetAngleByDeviceAxis(Vector3 axis)
    {
        Quaternion deviceRotation = DeviceRotation.Get();
        Quaternion eliminationOfOthers = Quaternion.Inverse(
            Quaternion.FromToRotation(axis, deviceRotation * axis)
        );
        Vector3 filteredEuler = (eliminationOfOthers * deviceRotation).eulerAngles;

        float result = filteredEuler.z;
        if (axis == Vector3.up)
        {
            result = filteredEuler.y;
        }
        if (axis == Vector3.right)
        {
            // incorporate different euler representations.
            result = (filteredEuler.y > 90 && filteredEuler.y < 270) ? 180 - filteredEuler.x : filteredEuler.x;
        }
        return result;
    }
}
