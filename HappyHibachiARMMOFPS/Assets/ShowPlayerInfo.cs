//using UnityEngine;
//using UnityEngine.UI;
//using System.Collections;
//using System.Runtime.Serialization.Formatters.Binary;
//using System.IO;

//public class ShowPlayerInfo : MonoBehaviour
//{
//    //text field displaying player attributes
//    public Text PlayerNameText, EXPText, LevelText, FactionText, FactionLevelText, FactionEXPText, MaxHPText, AttackText, DefenseText, ShotSpeedText;

//    public void ShowPlayerInformation()
//    {
//        if (File.Exists(Application.persistentDataPath + "/playerInfo.hibachiproject"))
//        {
//            //commented lines below to use if want to load player info from binary file
//            //BinaryFormatter bf = new BinaryFormatter();
//            //FileStream stream = new FileStream(Application.persistentDataPath + "/playerInfo.hibachiproject", FileMode.Open);
//            //PlayerData player = bf.Deserialize(stream) as PlayerData;
//            //stream.Close();

//            //grab player info from Player class for now, must succeed successful login
//            PlayerNameText.text = Player.playername;
//            EXPText.text = Player.EXP.ToString();
//            LevelText.text = Player.level.ToString();
//            if (Player.faction == 1) FactionText.text = "Paragon";
//            if (Player.faction == 2) FactionText.text = "Slayer";
//            if (Player.faction == 3) FactionText.text = "Hunter";
//            if (Player.faction == 0) FactionText.text = "None";
//            FactionEXPText.text = Player.factionEXP.ToString();
//            FactionLevelText.text = Player.factionLevel.ToString();
//            //default 
//            MaxHPText.text = "100"; //need to reflect database or reflect in game class/ item held
//            AttackText.text = "15";
//            DefenseText.text = "10";
//            ShotSpeedText.text = "0.5";
//        }
//    }
//    void Start()
//    {
//        ShowPlayerInformation();
//    }
//}
