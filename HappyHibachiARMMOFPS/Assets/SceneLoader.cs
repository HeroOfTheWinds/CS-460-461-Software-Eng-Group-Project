using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class SceneLoader : MonoBehaviour {

    // These variables drive the script as it pertains to the loading screen

    // This is the whole canvas, so that it can be enabled or disabled as needed
    public Canvas LoadingScreen;

    // Store the UI Image that scales as the progress bar
    public Image pbar;

    // Store the UI Text object that displays tip text
    public Text tipText;

    // Store a collection of (sometimes) helpful tips
    private string[] tips = {
        "A quick way to end a battle is to drop a mine at point blank. Check your HP first though!",
        "Don't die",
        "In the dowsing puzzle, move your device towards where the circle's pulse is fastest",
        "Remember, if you fail the disk puzzle, you'll be considered a Slayer",
        "Use cover to your advantage! Walls block bullets, but not explosions",
        "When in doubt, try not to doubt.  It's really a bad habit to form",
        "What limps and is full of holes? Hopefully your foe, not you",
        "Speed limits on overworld travel? Why would we, that's inhumane!",
        "The bar above this text shows how long before the action starts.  But you knew that"
    };
    // Update this to match the number of lines above
    private int numTips = 9;

	// Use this for initialization
	void Start () {
        // Disable the canvas so it doesn't show
        LoadingScreen.enabled = false;

        // Set the tip string to a random quote from the array
        // Get a random number
        Random.InitState(System.DateTime.Now.Millisecond); // Seed the RNG
        int rand = Mathf.RoundToInt(Random.Range(0f, (float)numTips - 1f));

        // Set the text to an array element
        tipText.text = tips[rand];
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void LoadScene(string scene)
    {
        LoadingScreen.enabled = true;
        StartCoroutine(AsynchronousLoad(scene));
    }

    // Create a coroutine to load scenes in the background
    // This way, we can display a progress bar to give the user
    // feedback while they wait
    IEnumerator AsynchronousLoad(string scene)
    {
        // pause to let other routines run
        yield return null;

        // Start loading the next scene asynchronously, and get a reference so we can track it
        AsyncOperation loader = SceneManager.LoadSceneAsync(scene);

        // This will prevent the scene from performing the final steps
        // That way, we can tie up any loose ends before removing the current scene and activating the next
        loader.allowSceneActivation = false;

        // Do the following while still waiting for things to load
        while (!loader.isDone)
        {
            // Note: when allowSceneActivation is false, progress stops at 0.9f.
            // No values between 0.9 and 1.0 (exclusive) are ever used regardless

            // Get a variable to store progress
            float progress = Mathf.Clamp01(loader.progress / 0.9f); // scale so that 0.9 registers as 100%

            // Update progress bar image
            pbar.rectTransform.localScale = new Vector3(progress, 1f, 1f);
            Debug.Log(progress);

            // Check if loading is complete.  Note that the full value is 0.9 exact, so equality is fine despite floating representation
            if (loader.progress == 0.9f)
            {
                // If anything needs to be done before committing to the final switch, do it now

                // Start the final sequence
                loader.allowSceneActivation = true;
            }

            // Yield the thread before looping again so that other processes can run
            yield return null;
        }
    }
}
