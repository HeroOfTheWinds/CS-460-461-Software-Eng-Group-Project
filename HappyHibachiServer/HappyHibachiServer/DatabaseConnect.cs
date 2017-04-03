using System;
using MySql.Data;
using MySql.Data.MySqlClient;
using System.Collections.Generic;

namespace HappyHibachiServer
{
    class DatabaseConnect
    {
        private MySqlConnection connection;
        private string server;
        private string database;
        private string uid;
        private string password;

        private static readonly int LANDMARK_TYPE = 2;
        private static readonly int COLOSSEUM_TYPE = 1;
        private static readonly int PLAYER_TYPE = 0;

        //Constructor
        public DatabaseConnect()
        {
            Initialize();
        }

        //Initialize database creds
        private void Initialize()
        {
            server = "localhost";
            database = "happyhibachi_armmofps";
            uid = "root";
            password = "T@rantul@50!";
            string connectionString;
            connectionString = "SERVER=" + server + ";" + "DATABASE=" +
            database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";

            connection = new MySqlConnection(connectionString);
        }

        //open connection to database
        private bool OpenConnection()
        {
            try
            {
                //Console.WriteLine("testing connection");
                connection.Open();
                return true;
            }
            catch (MySqlException ex)
            {
                //When handling errors, you can your application's response based 
                //on the error number.
                switch (ex.Number)
                {
                    default:
                        Console.WriteLine("Error: " + ex.Number);
                        break;
                    case 0:
                        //0: Cannot connect to server.
                        Console.WriteLine("Cannot connect to server.  Contact administrator"); 
                        break;

                    case 1045:
                        //1045: Invalid user name and/or password.
                        Console.WriteLine("Invalid username/password, please try again");
                        break;
                }
                return false;
            }
        }

        //Close connection
        private bool CloseConnection()
        {
            try
            {
                connection.Close();
                return true;
            }
            catch (MySqlException ex)
            {
                Console.WriteLine(ex.Message);
                return false;
            }
        }

        //Select GUID, Lat, Lon and Type attributes from Player, landmark, and colosseum tables
        public void findNearbyObjects(float lat, float lon, List<float> nearbyC, List<Guid> nearbyID)
        {
            string queryLandmark = "SELECT GUID, LAT, LON FROM LANDMARK";
            string queryColosseum = "SELECT GUID, LAT, LON FROM COLOSSEUM";
            try
            {
                //Open connection
                if (OpenConnection() == true)
                {
                    //Create Command
                    MySqlCommand cmd = new MySqlCommand(queryLandmark, connection);
                    //Create a data reader and Execute the command
                    MySqlDataReader dataReader = cmd.ExecuteReader();
                    //int i = 0;
                    //Read the data and store them in the list
                    //Console.WriteLine("Finding nearby objects:\n");
                    while (dataReader.Read())
                    {
                        //store GUID into nearbyID
                        //Console.WriteLine(dataReader["GUID"].ToString());
                        Guid GUID = Guid.Parse(dataReader["GUID"].ToString());
                        nearbyID.Add(GUID);

                        //use latitude to include the type of object it is. Determine if object is a (player, colloseum, landmark) and add (0, 1, 2) * 181 to latitude respectively
                        //latitudes range is -90 - 90, so by doing this the type of object can be determined without sending additional data (value < 91: player, 90 < value < 272: colloseum, 271 < value: colloseum)
                        nearbyC.Add(float.Parse(dataReader["LAT"].ToString()) + LANDMARK_TYPE * 181);
                        nearbyC.Add(float.Parse(dataReader["LON"].ToString()));

                    }

                    //close Data Reader
                    dataReader.Close();

                    //get elements in colosseum db
                    cmd = new MySqlCommand(queryColosseum, connection);
                    dataReader = cmd.ExecuteReader();
                    while (dataReader.Read())
                    {
                        //store GUID into nearbyID
                        //Console.WriteLine(dataReader["GUID"].ToString());
                        Guid GUID = Guid.Parse(dataReader["GUID"].ToString());
                        nearbyID.Add(GUID);

                        //use latitude to include the type of object it is. Determine if object is a (player, colloseum, landmark) and add (0, 1, 2) * 181 to latitude respectively
                        //latitudes range is -90 - 90, so by doing this the type of object can be determined without sending additional data (value < 91: player, 90 < value < 272: colosseum, 271 < value: landmark)
                        nearbyC.Add(float.Parse(dataReader["LAT"].ToString()) + COLOSSEUM_TYPE * 181);
                        nearbyC.Add(float.Parse(dataReader["LON"].ToString()));

                    }

                    //Console.WriteLine("\n");
                    //close Data Reader
                    dataReader.Close();

                    //close Connection
                    CloseConnection();

                    foreach(KeyValuePair<Guid, ClientState> cs in ConnectedPlayers.playerDetails)
                    {
                        //Console.WriteLine(cs.Key);
                        nearbyID.Add(cs.Key);
                        nearbyC.Add(cs.Value.Latitude[0]);
                        nearbyC.Add(cs.Value.Longtitude[0]);
                        //LATER ADD MECHANISM TO ONLY GET NEARBY ONES USING LAT AND LON (FOR DB STUFF AS WELL)
                    }
                }
                else
                {
                    Console.WriteLine("Cannot connect to server.  Contact administrator");
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                Console.WriteLine("Error accessing database");
            }
            
        }
        /*
        //Update player coors based on guid
        public void UpdatePlayerCoor(byte[] update, Guid clientID)
        {
            float lat = System.BitConverter.ToSingle(update, 0);
            float lon = System.BitConverter.ToSingle(update, 4);
            //Console.WriteLine("Updating player coordinates\n");
            //Console.WriteLine("player guid: " + clientID.ToString() + "\n");
            //Console.WriteLine("lat: " + lat);
            //Console.WriteLine("lon: " + lon);
            string query = "UPDATE PLAYER SET LAT= '" + lat.ToString() + "', LON = '" + lon.ToString() + "' WHERE GUID = '" + clientID.ToString() + "';";

            //Open connection
            if (this.OpenConnection() == true)
            {
                //create command and assign the query and connection from the constructor
                MySqlCommand cmd = new MySqlCommand(query, connection);

                //Execute command
                cmd.ExecuteNonQuery();

                //close connection
                this.CloseConnection();
                //Console.WriteLine("Player coordinates updated sucessfully");
            }
        }
        */

        //insert client id into db, use this as the key for identifying clients
        public void insertClientIdintoDB(Guid client_id)
        {

        }

        //select landmark's name from landmark based on guid
        public void provideLandmarkInfoFromDB(Guid landmark_id, ref string name, ref string description, ref string image)
        {
            string query = "SELECT LANDMARK_NAME FROM LANDMARK WHERE GUID ='" + landmark_id.ToString() + "';";
            //Open connection
            if (OpenConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, connection);
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();

                //Read the data and store them in the list
                while (dataReader.Read())
                {
                    name = dataReader["LANDMARK_NAME"].ToString();
                }
                description = "Description of Landmark";
                image = name + ".jpg";
                //close Data Reader
                dataReader.Close();

                //close Connection
                CloseConnection();
            }
        }

        //select colosseum's name from colosseum based on guid
        public void provideColosseumInfoFromDB(Guid colosseum_id, ref string name, ref string description, ref string image)
        {
            string query = "SELECT COLOSSEUM_NAME FROM COLOSSEUM WHERE GUID ='" + colosseum_id.ToString() + "';";
            //Open connection
            if (OpenConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, connection);
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();

                //Read the data and store them in the list
                while (dataReader.Read())
                {
                    name = dataReader["COLOSSEUM_NAME"].ToString();
                }
                description = "Description of Landmark";
                image = name + ".jpg";
                //close Data Reader
                dataReader.Close();

                //close Connection
                CloseConnection();
            }
        }

        //select colosseum's name from colosseum based on guid
        public void updatePlayerExpAfterBattle(Guid winner_id)
        {
            //exp is given only to winner of a battle
            //the exp is currently set to increment by a value of 50 
            //Later to implement: 
            //  The level of both players will factor into how much exp the winner gets
            //  If a player is 10 levels higher than the other then no exp is given
            // the winner will get more exp if at a lower level and les if higher: need to know opponent_id

            string query = "UPDATE PLAYER SET PLAYER_EXP = PLAYER_EXP + 50 WHERE GUID ='" + winner_id.ToString() + "';";
            //Open connection
            if (OpenConnection() == true)
            {
                //create command and assign the query and connection from the constructor
                MySqlCommand cmd = new MySqlCommand(query, connection);

                //Execute command
                cmd.ExecuteNonQuery();

                //close connection
                CloseConnection();
                Console.WriteLine("Player exp updated sucessfully");
            }
            else
            {
                Console.WriteLine("Player exp update unsucessful");
            }
        }

        public int getPlayerLevel(Guid playerID)
        {
            //exp is given only to winner of a battle
            //the exp is currently set to increment by a value of 50 
            //Later to implement: 
            //  The level of both players will factor into how much exp the winner gets
            //  If a player is 10 levels higher than the other then no exp is given
            // the winner will get more exp if at a lower level and les if higher: need to know opponent_id

            //for testing return default 0 if not in db
            int level = 0;

            string query = "SELECT PLAYER_LEVEL FROM PLAYER WHERE GUID ='" + playerID.ToString() + "';";
            //Open connection
            if (OpenConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, connection);
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();

                //Read the player's level
                if(dataReader.Read())
                {
                    level = (int)dataReader["PLAYER_LEVEL"];
                }
                //close Data Reader
                dataReader.Close();

                //close Connection
                CloseConnection();
            }

            return level;
        }

    }
}
