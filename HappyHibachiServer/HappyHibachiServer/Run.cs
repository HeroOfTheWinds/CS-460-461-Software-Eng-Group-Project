using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace HappyHibachiServer
{
    class Run
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hit Ctrl-C to exit.");
            Thread battleThread = new Thread(new ThreadStart(BattleServer.startServer));
            Thread overworldThread = new Thread(new ThreadStart(OverworldServer.startServer));
            Thread genComThread = new Thread(new ThreadStart(GenComServer.startServer));
            battleThread.Start();
            overworldThread.Start();
            genComThread.Start();
        }
    }
}
