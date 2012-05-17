package org.openrecommender.client;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import org.openrecommender.OpenRecommender;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * OpenRecommenderDesktop.java 
 * 
 * This application is intended to demonstrate the loading of image files into icons
 * for use in a Swing user interface. It creates a toolbar with a thumbnail preview
 * of each image.  Clicking on the thumbnail will show the full image
 * in the main display area. Clicking on the full image will launch the default
 * browser (for supported systems) to the specified URL.
 * This is based on original demos by Sun Microsystems / Oracle, provided for 
 * fair use as demonstrations of SystemTray and Swing GUI functionalities.
 *
 * This program uses 5 files as image fallbacks. <br>
 * The following files are copyright 2006 spriggs.net and licensed under a
 * Creative Commons License (http://creativecommons.org/licenses/by-sa/3.0/)
 * <br>
 * images/1.jpg <br>
 * images/2.jpg <br>
 * images/3.jpg <br>
 * images/4.jpg <br>
 * images/5.jpg <br>
 *
 * @author bcmoney
 * @date 04/30/2012
 * @version 2.0
 */
public class OpenRecommenderDesktop extends JFrame {
    
    private boolean getProperties(String path) {
        InputStream in = this.getClass().getResourceAsStream(path);
        try {
            properties.load(in);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
      
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = OpenRecommenderDesktop.class.getResource(path);        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }    
    
    /* 
     * MissingIcon
     *  Internal class for how to recover from missing icons.
     *  (displays empty image placeholder with red X and white background)
     */
    class MissingIcon implements Icon {

        private int width = 32;
        private int height = 32;

        private BasicStroke stroke = new BasicStroke(4);

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x +1 ,y + 1,width -2 ,height -2);

            g2d.setColor(Color.RED);

            g2d.setStroke(stroke);
            g2d.drawLine(x +10, y + 10, x + width -10, y + height -10);
            g2d.drawLine(x +10, y + height -10, x + width -10, y + 10);

            g2d.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }

    /**
     * Default constructor for the demo.
     */
    public OpenRecommenderDesktop() {        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(APP_TITLE);

        // A label for displaying the pictures
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // We add two glue components. Later in process() we will add thumbnail buttons
        // to the toolbar inbetween thease glue compoents. This will center the
        // buttons in the toolbar.
        buttonBar.add(Box.createGlue());
        buttonBar.add(Box.createGlue());

        add(buttonBar, BorderLayout.SOUTH);
        add(photographLabel, BorderLayout.CENTER);

        setSize(400, 300);

        ////////////////////////////////////////////////////////////////////////
        // this centers the frame on the screen
        setLocationRelativeTo(null);

        // start the image loading SwingWorker in a background thread
        loadimages.execute();
        
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage(IMAGE_DIR+APP_ICON, "OpenRecommender"));
        final SystemTray tray = SystemTray.getSystemTray();
        
        // Create popup menu components
        MenuItem openItem = new MenuItem(MENU_OPEN);
        MenuItem aboutItem = new MenuItem(MENU_ABOUT);
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu(MENU_SETTINGS);
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem(MENU_EXIT);
        
        //Add components to popup menu
        popup.add(openItem);        
        popup.addSeparator();
        popup.add(aboutItem);
        popup.add(cb1);
        popup.add(cb2);        
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.addSeparator();        
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("System Tray Icon could not be added.");
            return;
        }
        
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
            }
        });        
        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });
        
        cb1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED){
                    trayIcon.setImageAutoSize(true);
                } else {
                    trayIcon.setImageAutoSize(false);
                }
            }
        });
        
        cb2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb2Id = e.getStateChange();
                if (cb2Id == ItemEvent.SELECTED){
                    trayIcon.setToolTip("Sun TrayIcon");
                } else {
                    trayIcon.setToolTip(null);
                }
            }
        });
        
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Error".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);                    
                } else if ("Warning".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);                    
                } else if ("Info".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.INFO;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);                    
                } else if ("None".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.NONE;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);
                }
            }
        };
        
        errorItem.addActionListener(listener);
        warningItem.addActionListener(listener);
        infoItem.addActionListener(listener);
        noneItem.addActionListener(listener);
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });        
    }

    /**
     * SwingWorker class that loads the images a background thread and calls publish
     * when a new one is ready to be displayed.
     *
     * We use Void as the first SwingWroker param as we do not need to return
     * anything from doInBackground().
     */
    private SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {      
        
        /**
         * Creates full size and thumbnail versions of the target image files.
         */
        @Override
        protected Void doInBackground() throws Exception {

          /* Use OpenRecommender parser library to process recommendations */
            OpenRecommender orec = new OpenRecommender(); 
            Document xml = orec.load(null); //load the default local recommendations
            NodeList recommendations = orec.getRecommendations(xml);
            for (int i = 0; i < recommendations.getLength(); i++) {
                Element recommendation = orec.getRecommendation(recommendations, i);
                thumbs[i] = orec.getRecommendationImage(recommendation);
                links[i] = orec.getRecommendationLink(recommendation);
                titles[i] = orec.getRecommendationTitle(recommendation);
                descs[i] = orec.getRecommendationDescription(recommendation);
            }            

            for (int i = 0; i < titles.length; i++) {
                ImageIcon icon;
                if (thumbs[i].indexOf("http") != -1) {                    
                    icon = new ImageIcon(new URL(thumbs[i]));
                }
                else {
                    icon = createImageIcon(IMAGE_DIR + thumbs[i], titles[i]);
                }
                ThumbnailAction thumbAction;
                if(icon != null) {
                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 32, 32));
                    thumbAction = new ThumbnailAction(icon, thumbnailIcon, titles[i], links[i], descs[i]);
                } else {
                    // the image failed to load for some reason, so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, titles[i], links[i], descs[i]);
                }
                publish(thumbAction);
            }
            // unfortunately we must return something, and only null is valid to return when the return type is void.
            return null;
        }

        /**
         * Process all loaded images.
         */
        @Override
        protected void process(List<ThumbnailAction> chunks) {
            for (ThumbnailAction thumbAction : chunks) {
                JButton thumbButton = new JButton(thumbAction);
                // add the new button BEFORE the last glue this centers the buttons in the toolbar
                buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);
            }
        }
    };

    /**
     * Creates an ImageIcon if the path is valid.
     * @param String - resource path
     * @param String - description of the file
     */
    protected ImageIcon createImageIcon(String path, String description) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } 
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }    

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction implements MouseListener {        
        
        private Icon displayPhoto; // icon of the full image we want to display.
        
        /**
         * @param Icon - The full size photo to show in the button.
         * @param Icon - The thumbnail to show in the button.
         * @param String - The descriptioon of the icon.
         * @param String - The link to launch.
         */
        public ThumbnailAction(Icon photo, Icon thumb, String title, String link, String desc) {                                  
            displayPhoto = photo;                                    
            putValue(SHORT_DESCRIPTION, title); // The short description becomes the title of the window
            putValue(LARGE_ICON_KEY, thumb); // The LARGE_ICON_KEY is the key for setting the icon when an Action is applied to a button
            putValue(DEFAULT, link); // The DEFAULT action on click is to launch this URL                        
            putValue(LONG_DESCRIPTION, desc); // The long description becomes the tooltip of a button
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
//            photographLabel.removeMouseListener(this);  //add listener to launch links
//            ((ImageIcon)photographLabel.getIcon()).getImage().flush();
            photographLabel.validate();
            photographLabel.updateUI();
            photographLabel.repaint();            
            photographLabel.setIcon(displayPhoto); //set image
            setTitle("Icon Demo - " + getValue(SHORT_DESCRIPTION).toString()); //set title
            photographLabel.setToolTipText("<html>" + getValue(LONG_DESCRIPTION)  + "</html>"); //set desc overlay
            photographLabel.addMouseListener(this);  //add listener to launch links
        }

        /**
         * Listen for clicks to send user to a URL or launch an application
         * @param e 
         */            
        @Override
        public void mouseClicked(MouseEvent e) {                
            int clicked = e.getClickCount();
            if (clicked > 0) {
                /*************************************/
                /* Code for launching Browser  BEGIN */
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                }         
                URI uri = null;
                try {
                    uri = new URI((String)this.getValue(DEFAULT)); //pass the URL from ImageIcon to Browser
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(uri); //url passed
                    }
                    else {
                        System.out.println("Unable to open default browser to: "+uri);
                    }
                }
                catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                catch(URISyntaxException use) {
                    use.printStackTrace();
                }
                /* Code for launching Browser  END */
                /*************************************/
            }
            else {
                System.out.println("Clicked: " + clicked);
            }
        }        

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("Mouse pressed (# of clicks: " + e.getClickCount() + ") " + e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("Mouse released (# of clicks: " + e.getClickCount() + ") " + e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("Mouse entered: " + e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println("Mouse exited: " + e);
        }        
    }
   
    
    /**
     * Main entry point to the Desktop Application. Loads the Swing elements on
     * the "Event Dispatch Thread".
     *
     * @param args
     */
    public static void main(String args[]) {
        /* Use an appropriate Look and Feel */
        try {
            String theme = (APP_THEME != null && !APP_THEME.equals(""))? APP_THEME : UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(theme);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                OpenRecommenderDesktop app = new OpenRecommenderDesktop();
                app.setVisible(true);
                app.setDefaultCloseOperation(HIDE_ON_CLOSE);
            }
        });
    }    
    
    
    /* define constants/fallbacks */
    private JLabel photographLabel = new JLabel();
    private JToolBar buttonBar = new JToolBar();
    private String IMAGE_DIR = "images/";

    private MissingIcon placeholderIcon = new MissingIcon();    

    // List of all the image files to load.
    private String[] thumbs = { "1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg"};

    // List of all the descriptions of the image files. These correspond one to one with the image file names
    private String[] titles = { "SunLogo", "Clocktower", "ClocktowerWest", "Mansion", "SunAuditorium" };

    // List of all descriptions (descs) to use as tooltips.
    private String[] descs = { "The Original SUNW Logo", "The Clocktower frontal view", "The Clocktower from the West", "The Mansion on the hill!", "The infamouse Sun campus auditorium"};

    // List of all links to send a user to on click action.
    private String[] links = { "http://example.com/#1", "http://example.com/#2", "http://example.com/#3", "http://example.com/#4", "http://example.com/#5"};
    
    //set Application properties
    final static Properties properties = new Properties(); 
    private boolean canGetProperties = getProperties("/org/openrecommender/client/resources/app.properties");
    private String APP_TITLE = properties.getProperty("app.title");
    private static String APP_THEME = properties.getProperty("app.theme");
    private String APP_ICON = properties.getProperty("app.icon");
    private String MENU_OPEN = properties.getProperty("menu.open");
    private String MENU_ABOUT = properties.getProperty("menu.info");
    private String MENU_SETTINGS = properties.getProperty("menu.settings");
    private String MENU_EXIT = properties.getProperty("menu.exit");
}
