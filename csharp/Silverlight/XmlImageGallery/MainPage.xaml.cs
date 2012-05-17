/****************************************************************************

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

-- A XML Image Gallery in c#
-- Copyright 2009 Terence Tsang
-- admin@shinedraw.com
-- http://www.shinedraw.com
-- Your Flash vs Silverlight Repository

****************************************************************************/

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Markup;
using System.Windows.Media.Imaging;

namespace XmlImageGallery
{
    public partial class MainPage : UserControl
    {

        ObjectCollection _objectCollection;

        public MainPage()
        {
            InitializeComponent();

            // get the xml file, it is stored inside the ClientBin Folder
            WebClient wc = new WebClient();
            wc.DownloadStringCompleted += new DownloadStringCompletedEventHandler(wc_DownloadStringCompleted);
            wc.DownloadStringAsync(new Uri("./list.xml", UriKind.Relative));
        }

        void wc_DownloadStringCompleted(object sender, DownloadStringCompletedEventArgs e)
        {
            // user XamlReader to saved time in loading the XML as an object
            _objectCollection = XamlReader.Load(e.Result) as ObjectCollection;

            // add thumbnail into stackpanel
            for (int i = 0; i < _objectCollection.Count; i++)
            {
                ImageData imageData = _objectCollection[i] as ImageData;
                Image image = new Image()
                {
                    Width = 60,
                    Height = 45,
                    Source = new BitmapImage(imageData.ThumbnailUri),
                    Margin = new Thickness(0, 0, 10, 0),
                    Cursor = Cursors.Hand,
                    Tag = i
                };
                image.MouseLeftButtonDown +=new MouseButtonEventHandler(image_MouseLeftButtonDown);
                MyStackPanel.Children.Add(image);
            }

            // set the default image
            setImage(0);
        }

        // when mouse is clicked
        void  image_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            int index = (int) (sender as Image).Tag;
            setImage(index);
        }

        // set the original image
        void setImage(int index)
        {
            ImageData imageData = _objectCollection[index] as ImageData;
            MyImage.Source = new BitmapImage(imageData.ImageUri);
        }
    }


}
