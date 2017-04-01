using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace Assets
{
    public class ItemList
    {
        private static Dictionary<byte, ItemDetails> items;

        public static void populateItemList()
        {
            //add to dictionary all items and the corresponding path location of their image

            items.Add(0, new ItemDetails("Health Potion", "Assets\\Sprite Assets\\HP_Can_WIP.png"));
            items.Add(1, new ItemDetails("Health Potion", "Assets\\Sprite Assets\\Landmine_WIP2.png"));
        }

        public static ItemDetails getDetails(byte id)
        {
            ItemDetails details = null;
            items.TryGetValue(id, out details);
            return details;
        }
    }


    public class ItemDetails
    {
        private string name;
        private string path;

        public ItemDetails(string name, string path)
        {
            this.name = name;
            this.path = path;
        }

        public string Name
        {
            get
            {
                return name;
            }

            set
            {
                name = value;
            }
        }

        public string Path
        {
            get
            {
                return path;
            }

            set
            {
                path = value;
            }
        }
    }
}
