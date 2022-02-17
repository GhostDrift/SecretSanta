package Client;
import Common.ControlArea;
import Common.Message;
import Common.User;
import Common.displayPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.swing.*;
import javax.swing.border.Border;

public class ClientGUI extends JFrame {
    Client client = null;
    displayPanel Data;
    private final Label windowTitle;
    private ControlArea control;
    private User usr;
//    Client.ConnectGUI.BottomPanel Bot;

    //main method
    public ClientGUI(){
        //set's the title of the application
        setTitle("Secret Santa Management System");
        //set the size of the window
        //Global variables
        int WIDTH = 725;
        int HEIGHT = 500;
        setSize(WIDTH, HEIGHT);
        // -- center the frame on the screen
        setLocationRelativeTo(null);
        //stops the program when the clise button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //prevents the window from being resized
        setResizable(false);
        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(0, 0));
        //create new WindowTitle
        this.windowTitle = new Label();
        //create control pannel area
        control = new ControlArea();
        //set the data panel to display the connect panel.
        Data = new ClientGUI.Connect(false);

//          x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));
        //set the title pannel to display the Connect text
//        this.windowTitle = new Label(Data.getLabel());
        this.add(windowTitle, BorderLayout.NORTH);
        this.add(Data, BorderLayout.CENTER);
        this.add(control,BorderLayout.SOUTH);

//        Bot = new Client.ConnectGUI.BottomPanel();
//        this.add(Bot, BorderLayout.SOUTH);
        setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            //modified code from
            //https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Close button has been pressed");
                shutDown();
                System.exit(0);
            }
        });
    }

    protected void updateData(displayPanel dataNew){
        this.remove(Data);
        this.Data = dataNew;
        this.add(Data,BorderLayout.CENTER);

//        System.out.println( "Login " +Data.getLabel());
//        this.remove(windowTitle);
//        this.windowTitle = new Label(Data.getLabel());
//        this.add(windowTitle, BorderLayout.NORTH);
//        this.windowTitle.repaint();
//        System.out.println("Label " + windowTitle.getText());
        this.repaint();
//        shutDown();
    }
    //method to update the control panel
    protected void updateControl(JButton left, JButton center, JButton right){
        this.remove(control);
        this.control = new ControlArea();
        this.control.setLeft(left);
        this.control.setCenter(center);
        this.control.setRight(right);
//        System.out.println(left.getText() + " " + center.getText() + " " + right.getText() );
        this.add(control, BorderLayout.SOUTH);
//        this.control.repaint();
        this.repaint();
    }
    protected void updateControl(JButton left, JButton right){
        this.remove(control);
        this.control = new ControlArea();
        this.control.setLeft(left);
        this.control.setRight(right);
        this.add(control, BorderLayout.SOUTH);
        this.repaint();
    }
    protected void updateControl(JButton left, JButton centerLeft, JButton centerRight, JButton right){
        this.remove(control);
        this.control = new ControlArea();
        JPanel center = new JPanel();
        center.setLayout(new FlowLayout(1,10,0));
        center.add(centerLeft);
        center.add(centerRight);
        this.control.setLeft(left);
        this.control.setCenter(center);
        this.control.setRight(right);
        this.add(control, BorderLayout.SOUTH);
        this.repaint();
    }

    private static class Label extends JPanel{
        private JLabel title;


        Label(){
            setLayout(new FlowLayout(1, 10,10));
            Border labelBorder = BorderFactory.createLineBorder(Color.black);
            String text = "";
            title = new JLabel(text);
            title.setBorder(labelBorder);
            title.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.add(title);
        }

        Label(String text){
            super();
            this.setText(text);
        }
        protected void setText(String newText){
            this.title.setText(newText);
            System.out.println("Label new text: " + newText);
        }
    }
//    private abstract class displayPanel extends JPanel{
//        private  String panelName;
//        private  String spaces = "                                                                                                              ";
//
//        public void setPanelName(String panelName){
//            this.panelName = panelName;
//        }
//        public void setSpaces(String spaces){
//            this.spaces = spaces;
//        }
//        public String getLabel(){
//            return spaces + panelName + spaces;
//        }
//
//
//    }
    private class Connect extends displayPanel {
    private final JTextField IP;
        private final JButton Adv;
        private final JLabel Portn;
        private final JTextField portnum;
        private final JButton connect;
        private boolean t = false;
        private final JLabel errorMessage;

        Connect(Boolean error) {
            setLayout(new GridBagLayout());
//            setLayout(new BorderLayout(0,0));
            this.setPanelName("Connect");
            this.setSpaces("                                                                                                              ");
            windowTitle.setText(this.getLabel());
            JLabel i = new JLabel("HostName");
            i.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            IP = new JTextField("", 25);

            Portn = new JLabel("Port Number");
            Portn.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            portnum = new JTextField("8000", 25);

            Adv = new JButton("Advanced...");
            connect = new JButton("Connect");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.fill = GridBagConstraints.NONE;
            this.add(i, gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            this.add(IP, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            Portn.setVisible(t);
            this.add(Portn, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
//            gbc.fill = GridBagConstraints.HORIZONTAL;
            portnum.setVisible(false);
            this.add(portnum, gbc);
            this.errorMessage = new JLabel("Server is unreachable");
            this.errorMessage.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.errorMessage.setForeground(Color.RED);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            this.add(errorMessage, gbc);
            this.errorMessage.setVisible(false);
//            gbc.gridx = 0;
//            gbc.gridy = 3;
//            gbc.anchor = GridBagConstraints.LAST_LINE_START;
//            this.add(Adv);
//            gbc.gridx = 1;
//            gbc.gridy = 3;
//            gbc.anchor = GridBagConstraints.LAST_LINE_END;
//            this.add(connect);
            PrepareButtons();
            if(error){
                cannotConnect();
            }
            client = null;

        }

        protected void cannotConnect(){
            this.errorMessage.setVisible(true);
            this.repaint();
        }

    public void PrepareButtons() {


            Adv.addActionListener(e -> {
                if (!t)
                {
                    portnum.setText("8000");
                }
                t = !t;
                Portn.setVisible(t);
                portnum.setVisible(t);

            });
            connect.addActionListener(e -> {
                if (client == null) {
                    try {
                        String ipformat = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
                        Pattern ippattern = Pattern.compile(ipformat);
                        String portformat = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
                        Pattern portpattern = Pattern.compile(portformat);
//                            String host = "127.0.0.1";
//                            int port = 8000;
                        String host;
                        //checks to see if host box is empty
                        if(!IP.getText().equals("")) {
                            host = IP.getText();
                        }
                        else{
                            host = "127.0.0.1";
                        }
                        int port = Integer.parseInt(portnum.getText());
                        System.out.println(host);
                        System.out.println(port);
                        Matcher matcher = ippattern.matcher(host);
                        if (matcher.find()) {
                            matcher = portpattern.matcher(portnum.getText());
                            if (matcher.find()) {
//                                    Visibility = false;
//                                    SetVis();
                                client = new Client(host, port);
                                updateData(new Login());
//                                    Log = new LoginGUI();
                            }
                        }
                        else
                        {
                            System.out.println("Invalid IP address");
                        }

                    } catch (Exception m) {
                        System.out.println(m);
                        cannotConnect();
                    }
                }
            });

            updateControl(Adv,connect);

        }
    }
    private class Login extends displayPanel{
        private final JLabel username;
        private final JLabel password;
        private final JTextField usrName;
        private final JTextField pasWord;
        private final JButton disconnect;
        private final JButton register;
        private final JButton login;
        private final JButton recover;
        private final GridBagConstraints gbc = new GridBagConstraints();
        private final JLabel status;

         Login(){
            setLayout(new GridBagLayout());
             this.setPanelName("Login");
             this.setSpaces("                                                                                                              ");
             //Set windowTitle to the label of the datapanel
             windowTitle.setText(this.getLabel());
             //create the Jlabels
             username = new JLabel("Username");
             username.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             password = new JLabel("Password");
             password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             status = new JLabel("");
             status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             status.setVisible(false);
             //create the JTextfields
             usrName = new JTextField("", 25);
             pasWord = new JTextField("", 25);
             //create the JButtons for the control panel
             disconnect = new JButton("Disconnect");
             register = new JButton("Register");
             login = new JButton("Login");
             recover = new JButton("Recover");
             //add components to the window
             gbc.gridx = 0;
             gbc.gridy = 0;
             gbc.ipadx = 10;
             gbc.ipady = 10;
             gbc.fill = GridBagConstraints.NONE;
             gbc.gridwidth = 2;
             this.add(status,gbc);
             gbc.gridwidth = 1;
             gbc.gridy = 1;
             this.add(username,gbc);
             gbc.gridx = 1;
             this.add(usrName,gbc);
             gbc.gridx = 0;
             gbc.gridy = 2;
             this.add(password, gbc);
             gbc.gridx = 1;
             this.add(pasWord, gbc);

             prepareButtonHandlers();


         }
         //overloaded constructor
         Login(Boolean display,String message){
             setLayout(new GridBagLayout());
             this.setPanelName("Login");
             this.setSpaces("                                                                                                              ");
             //Set windowTitle to the label of the datapanel
             windowTitle.setText(this.getLabel());
             //create the Jlabels
             username = new JLabel("Username");
             username.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             password = new JLabel("Password");
             password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             if (message == null){
                 message = "";
             }
             status = new JLabel(message);
             status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             status.setVisible(display);
             //create the JTextfields
             usrName = new JTextField("", 25);
             pasWord = new JTextField("", 25);
             //create the JButtons for the control panel
             disconnect = new JButton("Disconnect");
             register = new JButton("Register");
             login = new JButton("Login");
             recover = new JButton("Recover");
             //add components to the window
             gbc.gridx = 0;
             gbc.gridy = 0;
             gbc.ipadx = 10;
             gbc.ipady = 10;
             gbc.fill = GridBagConstraints.NONE;
             gbc.gridwidth = 2;
             this.add(status,gbc);
             gbc.gridwidth = 1;
             gbc.gridy = 1;
             this.add(username,gbc);
             gbc.gridx = 1;
             this.add(usrName,gbc);
             gbc.gridx = 0;
             gbc.gridy = 2;
             this.add(password, gbc);
             gbc.gridx = 1;
             this.add(pasWord, gbc);

             prepareButtonHandlers();


         }


         private void prepareButtonHandlers(){
             disconnect.addActionListener(e -> {
                 System.out.println("Disconnect");
                 client.disconnect();
                 client = null;
                 updateData(new Connect(false));

             });
             register.addActionListener(e -> {
                 System.out.println("Register");
                 if(client != null) {
                     if (client.networkaccess.testConnection()) {
                         updateData(new Register());
                     } else {
                         updateData(new Connect(true));
                     }
                 }
                 else{
                     updateData(new Connect(true));
                 }
             });
             login.addActionListener(e -> {
//                     System.out.println("Connection Status: " + client.networkaccess.testConnection());
                 System.out.println("Login");
                 String username = usrName.getText();
                 String password = pasWord.getText();
                 try {
                     if (client.networkaccess.testConnection()) {
                         if ((!username.equals("")) && (!password.equals(""))) {
                             Message msg = client.login(username.toLowerCase(),password);
                             if(msg.message.equals("success")){
                                 usr = new User(username.toLowerCase());
                                 updateData(new Interaction());
                             }
                             else{
                                 status.setVisible(true);
                                 status.setText(msg.message);
                                 status.setForeground(Color.RED);
                                 repaint();
                             }
//                             if (client.login(username.toLowerCase(), password)) {
//                                 usr = new User(username.toLowerCase());
//                                 updateData(new Interaction());
//                             } else {
//                                 status.setVisible(true);
//                                 status.setText("Incorrect Username or Password");
//                                 status.setForeground(Color.RED);
//                             }
                         } else {
                             status.setVisible(true);
                             status.setText("Username and password cannot be empty");
                             status.setForeground(Color.RED);
                         }
                     } else {
                         updateData(new Connect(true));
                     }
                 } catch(Exception n){
                     updateData(new Connect(true));
                 }
             });
             recover.addActionListener(e -> {
                 if(client != null){
                     if(client.networkaccess.testConnection()){
                        updateData(new Recover());
                     }
                     else{
                         updateData(new Connect(true));
                     }
                 }
                 else{
                     updateData(new Connect(true));
                 }
                 System.out.println("Recover Password");

             });

             updateControl(disconnect,register,recover,login);
         }
    }

    private class Recover extends displayPanel{
        private final JButton submit;
        private final JButton cancel;
        private final JLabel status;
        private final JTextField userText;

        Recover(){
            setPanelName("Recover");
            this.setSpaces("                                                                                                              ");
            this.setLayout(new GridBagLayout());
            //Set windowTitle to the label of the datapanel
            windowTitle.setText(this.getLabel());
            //prepare components
            this.status = new JLabel("status will be displayed here");
            this.status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.status.setVisible(false);
            JLabel username = new JLabel("Username");
            username.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JLabel instructions = new JLabel("Enter the username of the account you would like to recover, an email will be sent.");
            instructions.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.userText = new JTextField("", 25);
            this.submit = new JButton("Submit");
            this.cancel = new JButton("Cancel");
            prepareButtonHandlers();
            //add components
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            this.add(status,gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(username, gbc);
            gbc.gridx = 1;
            this.add(userText, gbc);


        }
        private void displayError(String error){
            this.status.setVisible(true);
            status.setForeground(Color.red);
            status.setText(error);
            repaint();
        }

        private void prepareButtonHandlers() {
            this.submit.addActionListener(e -> {
                System.out.println("submit");
                if(client!= null){
                    if(client.networkaccess.testConnection()){
                        String result = client.recover(userText.getText());
                        if(result.equals("success")){
                            updateData(new Login(true,"Recovery email has been sent"));
                        }else{
                            this.displayError(result);
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
                else{
                    updateData(new Connect(true));
                }

            });
            this.cancel.addActionListener(e -> {
                System.out.println("Cancel");
                updateData(new Login());

            });
            updateControl(cancel,submit);
        }
    }

    private class Register extends displayPanel{
        private final JTextField usrName;
        private final JTextField eMailText;
        private final JTextField pasWord;
        private final JTextField rePassText;
        private final JButton cancel;
        private final JButton submit;
        private final JLabel status;

        Register(){
            this.setLayout(new GridBagLayout());
            this.setPanelName("Register");
            this.setSpaces("                                                                                                              ");
            //Set the window title label
            windowTitle.setText(this.getLabel());
            //prepare components
            JLabel username = new JLabel("Username");
            username.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JLabel eMail = new JLabel("E-Mail");
            eMail.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JLabel password = new JLabel("Password");
            password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JLabel rePassword = new JLabel("Re-enter Password");
            rePassword.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            usrName = new JTextField("",25);
            pasWord = new JTextField("", 25);
            eMailText = new JTextField("", 25);
            rePassText = new JTextField("", 25);
            status = new JLabel("Errors will be displayed here");
//            status.setForeground(Color.RED);
            status.setVisible(false);
            //prepare buttons
            cancel = new JButton("Cancel");
            submit = new JButton("Submit");
            prepareButtonHandlers();
            //add components to the JPanel
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            this.add(status, gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(username, gbc);
            gbc.gridx = 1;
            this.add(usrName, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(eMail, gbc);
            gbc.gridx = 1;
            this.add(eMailText, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            this.add(password, gbc);
            gbc.gridx = 1;
            this.add(pasWord, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            this.add(rePassword, gbc);
            gbc.gridx = 1;
            this.add(rePassText, gbc);

        }
        private void prepareButtonHandlers(){
            cancel.addActionListener(e -> {
                System.out.println("cancel");
                updateData(new Login());

            });
            submit.addActionListener(e -> {
                System.out.println("submit");
                if(client != null){
                    status.setText("Working...");
                    status.setVisible(true);
                    repaint();
                    if(client.networkaccess.testConnection()){
                        String result =client.register(usrName.getText().toLowerCase(),eMailText.getText(),pasWord.getText(),rePassText.getText());
                        if(result.equals("success")){
                            updateData(new Login(true,"Account successfully created!"));
                        }
                        else{
                            status.setForeground(Color.RED);
                            status.setText(result);
                            status.setVisible(true);
                            repaint();
                        }

                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
                else{
                    updateData(new Connect(true));
                }


            });

            updateControl(cancel,submit);
        }
    }

    private class Interaction extends displayPanel{
        private final JPanel wishListSelect;
        private final JButton myWishListButton;
        private final JButton recipientWishListButton;
        private final JButton add;
        private final JButton remove;
        private final JButton clear;
        private final JLabel status;
        private final JButton logOut;
        private final JButton accountSettings;
        private final JButton confirmWishlist;
        private ArrayList<String> myWishList;
        private ArrayList<String> recipientWishList;

        //constructor
        Interaction(){
            setLayout(new BorderLayout());
            setPanelName("Secret Santa Management System");
            //prepare components
            windowTitle.setText(this.getLabel());
            wishListSelect = new JPanel();
            wishListSelect.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            myWishListButton = new JButton("                                        My Wish List                                       ");
            myWishListButton.setBackground(Color.WHITE);
            recipientWishListButton = new JButton("                    Recipient's Wish List                    ");
//            recipientWishListButton.setSize(new Dimension(100,10));
            JTextArea wishlist = new JTextArea(5, 40);
            JScrollPane wishListArea = new JScrollPane(wishlist);
            wishListArea.createVerticalScrollBar();
            add = new JButton("Add Item");
            remove = new JButton("Remove Item");
            clear = new JButton("Clear List");
            status = new JLabel("Status:Unconfirmed");
            status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            logOut = new JButton("Log Out");
            accountSettings = new JButton("Account Settings");
            confirmWishlist = new JButton("Confirm Wish List");
            JPanel dataArea = new JPanel();
            dataArea.setLayout(new GridBagLayout());
            prepareButtonHandlers();
            //add components
            this.add(wishListSelect,BorderLayout.NORTH);
            this.add(wishListArea, BorderLayout.WEST);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridheight = 1;
            gbc.ipady = 20;
            gbc.ipadx = 10;
            gbc.fill = GridBagConstraints.NONE;
            dataArea.add(add, gbc);
            gbc.gridy = 1;
            dataArea.add(remove, gbc);
            gbc.gridy = 2;
            dataArea.add(clear, gbc);
            gbc.gridy = 3;
            dataArea.add(status, gbc);
            gbc.gridy = 4;
//            dataArea.add(confirmWishlist,gbc);
            this.add(dataArea,BorderLayout.CENTER);



        }

        private void prepareButtonHandlers(){
                myWishListButton.addActionListener(e -> {
                    System.out.println("My Wish List");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            recipientWishListButton.setBackground(null);
                            myWishListButton.setBackground(Color.WHITE);
                            add.setVisible(true);
                            remove.setVisible(true);
                            clear.setVisible(true);
                            status.setVisible(true);
                            confirmWishlist.setVisible(true);
                        }
                        else{
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                });
                recipientWishListButton.addActionListener(e -> {
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            System.out.println("Recipient's Wishlist");
                            myWishListButton.setBackground(null);
                            recipientWishListButton.setBackground(Color.WHITE);
                            add.setVisible(false);
                            remove.setVisible(false);
                            clear.setVisible(false);
                            status.setVisible(false);
                            confirmWishlist.setVisible(false);
                        }
                        else{
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }



                });
                add.addActionListener(e -> {
                    System.out.println("Add item");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new AddItem());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                });
                remove.addActionListener(e -> {
                    System.out.println("Remove Item");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new RemoveItem());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                });
                clear.addActionListener(e -> {
                    System.out.println("Clear Wish List");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            System.out.println("wishlist cleared");
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }

                });
                confirmWishlist.addActionListener(e -> {
                    System.out.println("Confirm Wish List");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            System.out.println("Wishlist Confirmed");
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }

                });
                logOut.addActionListener(e -> {
                    System.out.println("Log Out");
                    System.out.println(usr.getUsername());
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            if(client.logout(usr)){
                                usr = new User(); //clearing the global User variable when the client logs out
                                updateData(new Login());
                            }
                            else {
                                System.out.println("Cannot Logout User");
                            }
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                });
                accountSettings.addActionListener(e -> {
                    System.out.println("Account Settings");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new AccountSettings());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                });
                updateControl(logOut,accountSettings,confirmWishlist);
                wishListSelect.add(myWishListButton);
                wishListSelect.add(recipientWishListButton);
        }
    }
    private class AddItem extends displayPanel{
        private final JButton add;
        private final JButton cancel;
        private final GridBagConstraints gbc = new GridBagConstraints();

        //constructor
        protected AddItem(){
            this.setPanelName("Add an Item");
            //prepare components
            windowTitle.setText(this.getLabel());
            add = new JButton("Add Item");
            cancel = new JButton("Cancel");
            JLabel addHere = new JLabel("Enter the item you would like to put on your wish list");
            addHere.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JTextField itemEntry = new JTextField("", 25);
            prepareButtonHandlers();
            //add components
            this.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(addHere,gbc);
            gbc.gridy = 1;
            this.add(itemEntry,gbc);
        }

        private void prepareButtonHandlers() {
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Add item");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new Interaction());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cancel");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new Interaction());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
            });
            updateControl(cancel,add);
        }
    }
    private class RemoveItem extends displayPanel{
        private JLabel removeHere;
        private JTextField itemToRemove;
        private JButton remove;
        private JButton cancel;
        private GridBagConstraints gbc = new GridBagConstraints();
        //Constructor
        protected RemoveItem(){
            this.setPanelName("Remove an Item");
            this.setLayout(new GridBagLayout());
            windowTitle.setText(this.getLabel());
            //prepare components
            removeHere = new JLabel("Enter the number of the item on the list you would like to remove");
            removeHere.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            itemToRemove = new JTextField(25);
            remove = new JButton("Remove");
            cancel = new JButton("Cancel");
            prepareButtonHandlers();
            gbc.gridy = 0;
            gbc.gridx = 0;
            this.add(removeHere,gbc);
            gbc.gridy = 1;
            this.add(itemToRemove,gbc);
        }

        private void prepareButtonHandlers() {
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Remove Item");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new Interaction());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cancel");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new Interaction());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
            });
            updateControl(cancel,remove);
        }
    }
    private class AccountSettings extends displayPanel{
        private JLabel password;
        private JLabel email;
        private JTextField passText;
        private JTextField emailText;
        private JButton cancel;
        private JButton apply;
        private JLabel status;
        private GridBagConstraints gbc = new GridBagConstraints();

        //constructor
        protected AccountSettings(){
            usr = client.getUser(usr);
            this.setPanelName("Account Settings");
            windowTitle.setText(this.getLabel());
            this.setLayout(new GridBagLayout());
            //prepare components
            password = new JLabel("Password");
            password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            email = new JLabel("Email");
            email.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            passText = new JTextField(usr.getPassword(), 25);
            emailText = new JTextField(usr.getEmail(), 25);
            cancel = new JButton("Cancel");
            apply = new JButton("Apply");
            status = new JLabel("Status will be displayed here");
            status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            status.setForeground(Color.BLACK);
            prepareButtonHandlers();
            //add components to frame
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            this.add(status, gbc);
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            this.add(password,gbc);
            gbc.gridx = 1;
            this.add(passText,gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(email,gbc);
            gbc.gridx = 1;
            this.add(emailText,gbc);


        }

        private void prepareButtonHandlers() {
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cancel");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new Interaction());
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }

                }
            });
            apply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Apply");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            User usr = new User();
                            usr.setPassword(passText.getText());
                            usr.setEmail(emailText.getText());
                            String result = client.updateSettings(usr);
                            if(result.equals("success")) {
                                status.setText("Account Updated");
                                status.setVisible(true);
                            }
                            else{
                                status.setText(result);
                                status.setForeground(Color.red);
                                status.setVisible(true);
                            }
                        }
                        else {
                            updateData(new Connect(true));
                        }
                    }
                    else{
                        updateData(new Connect(true));
                    }
                }
            });
            updateControl(cancel,apply);
        }
    }
//    private class ControlArea extends JPanel{
//        private JButton left;
//        private JButton right;
//        private JButton center;
//        protected ControlArea(){
//            setLayout(new BorderLayout(5, 10));
//        }
//        protected void setLeft(JButton newLeft){
//            if(this.left != null) {
//                this.remove(left);
//            }
//            this.left = newLeft;
//            this.add(newLeft, BorderLayout.WEST);
//            this.repaint();
//        }
//        protected void setRight(JButton newRight){
//            if(this.right != null){
//                this.remove(right);
//            }
//            this.right = newRight;
//            this.add(newRight,BorderLayout.EAST);
//            this.repaint();
//        }
//        protected void setCenter(JButton newCenter){
//            if(this.center != null){
//                this.remove(center);
//            }
//            this.center = newCenter;
//            this.add(newCenter,BorderLayout.CENTER);
//            this.repaint();
//        }
//        protected void delButton(int i){
//            if(i == 0){
//                this.remove(left);
//            }
//            else if(i == 1){
//                this.remove(center);
//            }
//            else{
//                this.remove(right);
//            }
//        }
//
//        public void setCenter(JPanel center) {
//            this.add(center, BorderLayout.CENTER);
//        }
//    }

    //method to shut off client properly
    private void shutDown(){
        if(Data instanceof Connect){
            System.out.println("connect window");
//            System.exit(0);
        }
        else if((Data instanceof Login)||(Data instanceof Register) || (Data instanceof Recover)){
            System.out.println("Login window");
            client.disconnect();
//            System.exit(0);
        }
        else{
//            System.out.println("Interaction window");
            logoutAndDisconnect();
        }

    }
    private void logoutAndDisconnect(){
        client.logout(usr);
        client.disconnect();
//        System.exit(0);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}
