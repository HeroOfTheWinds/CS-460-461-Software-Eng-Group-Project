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

        //Constructor
        public DatabaseConnect()
        {
            Initialize();
        }

        //Initialize values
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
                Console.WriteLine("testing connection");
                connection.Open();
                return true;
            }
            catch (MySqlException ex)
            {
                //When handling errors, you can your application's response based 
                //on the error number.
                //The two most common error numbers when connecting are as follows:
                //0: Cannot connect to server.
                //1045: Invalid user name and/or password.
                switch (ex.Number)
                {
                    default:
                        Console.WriteLine("Error: " + ex.Number);
                        break;
                    case 0:
                        Console.WriteLine("Cannot connect to server.  Contact administrator");
                        break;

                    case 1045:
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

        //Select statement
        public void findNearbyObjects(List<float> nearbyC, List<Guid> nearbyID)
        {
            string query = "SELECT GUID, LAT, LON, TYPE FROM PLAYER UNION SELECT GUID, LAT, LON, TYPE FROM LANDMARK UNION SELECT GUID, LAT, LON, TYPE FROM COLOSSEUM";

            //Open connection
            if (this.OpenConnection() == true)
            {
                //Create Command
                MySqlCommand cmd = new MySqlCommand(query, connection);
                //Create a data reader and Execute the command
                MySqlDataReader dataReader = cmd.ExecuteReader();
                //int i = 0;
                //Read the data and store them in the list
                while (dataReader.Read())
                {
                    //store GUID into nearbyID
                    Console.WriteLine(dataReader["GUID"].ToString());
                    Guid GUID = Guid.Parse(dataReader["GUID"].ToString());
                    nearbyID.Add(GUID);

                    //use latitude to include the type of object it is. Determine if object is a (player, colloseum, landmark) and add (0, 1, 2) * 181 to latitude respectively
                    //latitudes range is -90 - 90, so by doing this the type of object can be determined without sending additional data (value < 91: player, 90 < value < 272: colloseum, 271 < value: colloseum)
                    nearbyC.Add(float.Parse(dataReader["LAT"].ToString())  + float.Parse(dataReader["TYPE"].ToString()) * 181);
                    nearbyC.Add(float.Parse(dataReader["LON"].ToString()));

                    //i++;

                }

                //close Data Reader
                dataReader.Close();

                //close Connection
                this.CloseConnection();
            }
            else
            {
                Console.WriteLine("Cannot connect to server.  Contact administrator");
            }
        }

        //Update statement
        public void Update(byte[] update, Guid clientID)
        {
            float lat = System.BitConverter.ToSingle(update, 0);
            float lon = System.BitConverter.ToSingle(update, 4);

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
            }
        }
    }
}


