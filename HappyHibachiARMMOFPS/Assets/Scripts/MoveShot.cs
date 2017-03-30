using UnityEngine;
using System.Collections;

public class MoveShot : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}

    float timeActive = 0f;
	
	// Update is called once per frame
	void Update () {
        transform.Translate(Vector3.forward * 100f * Time.deltaTime);
        timeActive += Time.deltaTime;

        if (timeActive >= 2.5f)
            Destroy(gameObject);
	}

    // Destroy on collision
    void OnCollisionEnter()
    {
        Destroy(gameObject);
    }
}
