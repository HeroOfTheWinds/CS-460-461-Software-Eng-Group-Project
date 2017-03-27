using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Timers;

namespace HappyHibachiServer
{
    static class ConnectedPlayers
    {
        public static Dictionary<Guid, ClientState> playerDetails = new Dictionary<Guid, ClientState>();
        public static readonly object DICTIONARY_LOCK = new object();
    }

    internal class ClientState
    {
        private Socket timeoutSocket;
        private Socket genComSocket;
        private Socket overworldSocket;
        private Socket battleSocket;
        //initialize lat and lon to negative infinity to make sure that proximity calculations dont pick them up until coordinates are received from the player (and an exception isnt thrown if not set)
        private float[] longtitude = { float.NegativeInfinity };
        private float[] latitude = { float.NegativeInfinity };
        public readonly object GENCOM_WRITE_LOCK = new object();

        public Socket BattleSocket
        {
            get
            {
                return BattleSocket1;
            }

            set
            {
                BattleSocket1 = value;
            }
        }

        public Socket OverworldSocket
        {
            get
            {
                return overworldSocket;
            }

            set
            {
                overworldSocket = value;
            }
        }

        public Socket GenComSocket
        {
            get
            {
                return genComSocket;
            }

            set
            {
                genComSocket = value;
            }
        }

        

        public Socket TimeoutSocket
        {
            get
            {
                return timeoutSocket;
            }

            set
            {
                timeoutSocket = value;
            }
        }

        public Socket BattleSocket1
        {
            get
            {
                return battleSocket;
            }

            set
            {
                battleSocket = value;
            }
        }

        public float[] Latitude
        {
            get
            {
                return latitude;
            }

            set
            {
                latitude = value;
            }
        }

        public float[] Longtitude
        {
            get
            {
                return longtitude;
            }

            set
            {
                longtitude = value;
            }
        }

        public ClientState(Socket gcsocket, Socket osocket, Socket bsocket, Socket tsocket)
        {
            genComSocket = gcsocket;
            overworldSocket = osocket;
            BattleSocket1 = bsocket;
            timeoutSocket = tsocket;
        }
    }
}
