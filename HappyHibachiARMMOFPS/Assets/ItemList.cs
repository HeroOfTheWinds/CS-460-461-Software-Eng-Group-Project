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
