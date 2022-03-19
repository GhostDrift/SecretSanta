package Client;

import Common.ControlArea;
import Common.Message;
import Common.User;
import Common.displayPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientGUI extends JFrame {
    Client client = null;
    displayPanel Data;
    private final Label windowTitle;
    private ControlArea control;
    private User usr;
    private Font timesRoman = new Font("TimesRoman", Font.PLAIN, 15);
//    Client.ConnectGUI.BottomPanel Bot;

    //main method
    public ClientGUI(){
        //initialize the connection list
        Connections.initialize();
//        Connections.printConnections();
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
        Data = new ClientGUI.savedConnections(false);

//          x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));
        //set the title pannel to display the Connect text
//        this.windowTitle = new Label(Data.getLabel());
        this.add(windowTitle, BorderLayout.NORTH);
        this.add(Data, BorderLayout.CENTER);
        this.add(control,BorderLayout.SOUTH);

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
        center.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
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
            setLayout(new FlowLayout(FlowLayout.CENTER, 10,10));
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
            this.errorMessage = new JLabel("Server is unreachable");
            this.errorMessage.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.errorMessage.setForeground(Color.RED);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 2;
            this.add(errorMessage,gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(i, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            this.add(IP, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            Portn.setVisible(t);
            this.add(Portn, gbc);
            gbc.gridx = 1;
            gbc.gridy = 2;
//            gbc.fill = GridBagConstraints.HORIZONTAL;
            portnum.setVisible(false);
            this.add(portnum, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
//            this.add(errorMessage, gbc);
            this.errorMessage.setVisible(false);
            PrepareButtons();
            prepareKeyListener();
            if(error){
                cannotConnect();
            }
            client = null;

        }

        protected void cannotConnect(){
            this.errorMessage.setText("Server is unreachable");
            this.errorMessage.setVisible(true);
            this.repaint();
        }
        protected void invalidIP(){
            cannotConnect();
            this.errorMessage.setText("Invalid IP Address");
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
                                client = new Client(host, port);
                                updateData(new Login());
//                                    Log = new LoginGUI();
                            }
                        }
                        else
                        {
                            System.out.println("Invalid IP address");
                            invalidIP();
                        }

                    } catch (Exception m) {
                        m.printStackTrace();
                        cannotConnect();
                    }
                }
            });

            updateControl(Adv,connect);

        }
        private void prepareKeyListener(){
            this.IP.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    System.out.println(keyEvent.getKeyCode());
                    if(keyEvent.getKeyCode() == 10){
                        connect.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }
    }
    private class savedConnections extends displayPanel{
//        private final JTextField IP;
        private final String IP = "";
        private final int port = 8000;
//        private final JButton Adv;
//        private final JLabel Portn;
//        private final JTextField portnum;
        private final JButton connect;
        private final JButton newConnection;
        private boolean t = false;
        private final JLabel errorMessage;
        private final JComboBox connectionList;

        savedConnections(Boolean error) {
            setLayout(new GridBagLayout());
//            setLayout(new BorderLayout(0,0));
            this.setPanelName("Connect");
            this.setSpaces("                                                                                                              ");
            windowTitle.setText(this.getLabel());
            JLabel title = new JLabel("Saved Connections");
            title.setFont(timesRoman);
            JLabel i = new JLabel("HostName");
            i.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            ArrayList<String> names;
            try {
                names =  Connections.getNames();
            } catch (Connections.ConnectionsNotInitialized e) {
                names = new ArrayList<String>();
            }
//            if(names.equals(null)){
//                names = new ArrayList<>();
//            }
            names.add(0,"");
            connectionList = new JComboBox(names.toArray());
//            IP = new JTextField("", 25);

//            Portn = new JLabel("Port Number");
//            Portn.setFont(new Font("TimesRoman", Font.PLAIN, 15));
//            portnum = new JTextField("8000", 25);

//            Adv = new JButton("Advanced...");
            connect = new JButton("Connect");
            newConnection = new JButton("New connection");
            this.errorMessage = new JLabel("Server is unreachable");
            this.errorMessage.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.errorMessage.setForeground(Color.RED);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 2;
            this.add(errorMessage,gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(i, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
//            this.add(IP, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
//            Portn.setVisible(t);
//            this.add(Portn, gbc);
            gbc.gridx = 1;
            gbc.gridy = 2;
//            gbc.fill = GridBagConstraints.HORIZONTAL;
//            portnum.setVisible(false);
//            this.add(portnum, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
//            this.add(errorMessage, gbc);
            this.errorMessage.setVisible(false);
            PrepareButtons();
            prepareKeyListener();
            if(error){
                cannotConnect();
            }
            client = null;

        }

        protected void cannotConnect(){
            this.errorMessage.setText("Server is unreachable");
            this.errorMessage.setVisible(true);
            this.repaint();
        }
        protected void invalidIP(){
            cannotConnect();
            this.errorMessage.setText("Invalid IP Address");
            this.repaint();
        }

        public void PrepareButtons() {


//            Adv.addActionListener(e -> {
//                if (!t)
//                {
////                    portnum.setText("8000");
//                }
//                t = !t;
////                Portn.setVisible(t);
////                portnum.setVisible(t);
//
//            });
            connect.addActionListener(e -> {
                if (client == null) {
                    try {
                        String ipformat = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
                        Pattern ippattern = Pattern.compile(ipformat);
                        String portformat = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
                        Pattern portpattern = Pattern.compile(portformat);
                        String host;
                        //checks to see if host box is empty
                        if(!IP.equals("")) {
                            host = IP;
                        }
                        else{
                            host = "127.0.0.1";
                        }
//                        int port = Integer.parseInt(portnum.getText());
                        System.out.println(host);
                        System.out.println(port);
                        Matcher matcher = ippattern.matcher(host);
                        if (matcher.find()) {
//                            matcher = portpattern.matcher(portnum.getText());
                            matcher = portpattern.matcher(port + "");
                            if (matcher.find()) {
                                client = new Client(host, port);
                                updateData(new Login());
//                                    Log = new LoginGUI();
                            }
                        }
                        else
                        {
                            System.out.println("Invalid IP address");
                            invalidIP();
                        }

                    } catch (Exception m) {
                        m.printStackTrace();
                        cannotConnect();
                    }
                }
            });
            newConnection.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("Open new connections");
                }
            });

//            updateControl(Adv,connect);
            updateControl(newConnection,connect);

        }
        private void prepareKeyListener(){
//            this.IP.addKeyListener(new KeyListener() {
//                @Override
//                public void keyTyped(KeyEvent keyEvent) {
//
//                }
//
//                @Override
//                public void keyPressed(KeyEvent keyEvent) {
//                    System.out.println(keyEvent.getKeyCode());
//                    if(keyEvent.getKeyCode() == 10){
//                        connect.doClick();
//                    }
//                }
//
//                @Override
//                public void keyReleased(KeyEvent keyEvent) {
//
//                }
//            });
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
             this.usrName.requestFocus();

             prepareButtonHandlers();
             prepareKeyListener();


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
             this.usrName.requestFocus();

             prepareButtonHandlers();
             prepareKeyListener();


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
        private void prepareKeyListener(){
            this.pasWord.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
//                    System.out.println(keyEvent.getKeyCode());

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        login.doClick();
                    }

                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            this.usrName.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        pasWord.requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
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
            JLabel instructions = new JLabel("Enter the username of the account you would like to recover");
            instructions.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.userText = new JTextField("", 25);
            this.submit = new JButton("Submit");
            this.cancel = new JButton("Cancel");
            prepareButtonHandlers();
            prepareKeyListeners();
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
            this.add(instructions,gbc);
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            this.add(username, gbc);
            gbc.gridx = 1;
            this.add(userText, gbc);
            this.userText.requestFocus();


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
                        System.out.println("result: " + result);
                        if(result.equals("success")){
                            updateData(new Login(true,"Recovery email has been sent"));
                        }else{
                            displayError(result);
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
        private void prepareKeyListeners(){
            this.userText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        submit.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }
    }

    private class Register extends displayPanel{
        private final JTextField usrName;
        private final JTextField eMailText;
        private final JTextField pasWord;
        private final JTextField rePassText;
        private final JTextField nameText;
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
            JLabel name = new JLabel("Your name");
            name.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            usrName = new JTextField("",25);
            pasWord = new JTextField("", 25);
            eMailText = new JTextField("", 25);
            rePassText = new JTextField("", 25);
            nameText = new JTextField("",25);
            status = new JLabel("Errors will be displayed here");
//            status.setForeground(Color.RED);
            status.setVisible(false);
            //prepare buttons
            cancel = new JButton("Cancel");
            submit = new JButton("Submit");
            prepareButtonHandlers();
            prepareKeyListeners();
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
            gbc.gridy = 5;
            gbc.gridx = 0;
            this.add(name,gbc);
            gbc.gridx = 1;
            this.add(nameText,gbc);

        }

        private void prepareKeyListeners() {
            this.usrName.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        eMailText.requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            this.pasWord.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        rePassText.requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            this.rePassText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        nameText.requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            this.eMailText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        pasWord.requestFocus();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            nameText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        submit.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
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
                        if(nameText.getText().equals("") ){
                            status.setForeground(Color.RED);
                            status.setText("You must enter a name");
                            status.setVisible(true);
                            repaint();
                        }else if(nameText.getText().toLowerCase(Locale.ROOT).equals("null")){
                            status.setForeground(Color.RED);
                            status.setText("Name cannot be null");
                            status.setVisible(true);
                            repaint();
                        }
                        else {
                            String result = client.register(usrName.getText().toLowerCase(), eMailText.getText(), pasWord.getText(), rePassText.getText(),nameText.getText());
                            if (result.equals("success")) {
                                updateData(new Login(true, "Account successfully created!"));
                            } else {
                                status.setForeground(Color.RED);
                                status.setText(result);
                                status.setVisible(true);
                                repaint();
                            }
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
        private final JLabel recipient = new JLabel("recipient's name");
        private final JButton logOut;
        private final JButton accountSettings;
        private final JButton confirmWishlist;
        JTextArea wishlist;
        private ArrayList<String> myWishList = client.getWishList(usr);
        private final JLabel error;

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
            wishlist = new JTextArea(5, 40);
            wishlist.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JScrollPane wishListArea = new JScrollPane(wishlist);
            wishListArea.createVerticalScrollBar();
            add = new JButton("Add Item");
            remove = new JButton("Remove Item");
            clear = new JButton("Clear List");
//            status = new JLabel("Status:Unconfirmed");
            status = new JLabel();
            status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            logOut = new JButton("Log Out");
            accountSettings = new JButton("Account Settings");
//            confirmWishlist = new JButton("Confirm Wish List");
            confirmWishlist = new JButton();
            JPanel dataArea = new JPanel();
            dataArea.setLayout(new GridBagLayout());
            error = new JLabel("Errors will be displayed here");
            error.setForeground(Color.RED);
            error.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            error.setVisible(false);
            recipient.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            recipient.setVisible(false);
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
            dataArea.add(error,gbc);
            gbc.gridy = 5;
            dataArea.add(recipient,gbc);
//            dataArea.add(confirmWishlist,gbc);
            this.add(dataArea,BorderLayout.CENTER);
            updateWishList(myWishList,false);
            updateWishListStatus();


        }
        //method to display error messages
        private void updateError(){
            this.error.setText("Cannot confirm an empty list");
            this.error.setVisible(true);
            this.repaint();
        }
        //method to clear an error message
        private void clearError(){
            this.error.setVisible(false);
            this.repaint();
        }
        //method to update wishlist conformation status
        private void updateWishListStatus(){
            if(client.getWishListConformation()){
                status.setText("Status: Confirmed");
                confirmWishlist.setText("De-confirm Wish List");
            }
            else{
                status.setText("Status: Unconfirmed");
                confirmWishlist.setText("Confirm Wish List");
            }
        }
        //method to update the wishList area
        private void updateWishList(ArrayList<String> list,boolean rl){
            StringBuilder s = new StringBuilder();
            if(rl){
                s = new StringBuilder(client.getRecipient().user.getName() + "'s wish list\n");
            }
            if(list == null||list.size() == 0){
                s.append("List is empty");
            }
            else {
                for (int i = 0; i < list.size(); i++) {
                    s.append(i).append(1).append(". ").append(list.get(i)).append("\n");
                }
            }
//            System.out.println(s);
            wishlist.setText(s.toString());
        }

        private void prepareButtonHandlers(){
                myWishListButton.addActionListener(e -> {
                    System.out.println("My Wish List");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            recipientWishListButton.setBackground(null);
                            myWishListButton.setBackground(Color.WHITE);
                            recipient.setVisible(false);
                            add.setVisible(true);
                            remove.setVisible(true);
                            clear.setVisible(true);
                            status.setVisible(true);
                            confirmWishlist.setVisible(true);
                            usr.setPassword("wishlist");
                            myWishList = client.getWishList(usr);
                            usr.setEntry("null");
                            System.out.println(usr);
                            updateWishList(myWishList,false);
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
                            Message msg = client.getRecipientWishList();
                            if(msg.message.equals("success")){
                                updateWishList(msg.user.getWishList(),true);
                                clearError();
                            }
                            else if(msg.message.equals("Unconfirmed")){
                                wishlist.setText(client.getRecipient().user.getName() + "'s wish list is unconfirmed");
                            }
                            else{
                                if(!msg.message.equals("Names have not been drawn")){
                                    recipient.setText(client.getRecipient().user.getName() + "'s wish list");
                                }
                                wishlist.setText(msg.message);
                                this.repaint();
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
                            updateData(new ClearWishList());
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
//                            System.out.println("Wishlist Confirmed");
                            if(confirmWishlist.getText().equals("Confirm Wish List")){
                                if(client.confirmWishList()) {
                                    updateWishListStatus();
                                    clearError();
                                }
                                else{
                                    updateError();
                                }
                            }
                            else{
                                client.unconfirmWishList();
                                updateWishListStatus();
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
        private final JTextField itemEntry;
        private final JLabel status;

        //constructor
        protected AddItem(){
            this.setPanelName("Add an Item");
            //prepare components
            windowTitle.setText(this.getLabel());
            status = new JLabel("Status will be displayed here");
            status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            status.setVisible(false);
            add = new JButton("Add Item");
            cancel = new JButton("Cancel");
            JLabel addHere = new JLabel("Enter the item you would like to put on your wish list");
            addHere.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            itemEntry = new JTextField("", 25);
            prepareButtonHandlers();
            prepareKeyListeners();
            //add components
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(status, gbc);
            gbc.gridy = 1;
            this.add(addHere, gbc);
            gbc.gridy = 2;
            this.add(itemEntry, gbc);
        }
        private void error(String error){
            this.status.setForeground(Color.RED);
            this.status.setText(error);
            this.status.setVisible(true);
            this.repaint();
        }

        private void prepareKeyListeners() {
            this.itemEntry.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        add.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }

        private void prepareButtonHandlers() {
            add.addActionListener(e -> {
                System.out.println("Add item");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        User usr = new User();
                        usr.setEntry(itemEntry.getText());
                        System.out.println(usr.getEntry());
                        String result = client.addItem(usr);
                        System.out.println(usr);
                        if(result.equals("success")){
                        updateData(new Interaction());
                        }
                        else {
                            error(result);
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
            cancel.addActionListener(e -> {
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
            });
            updateControl(cancel,add);
        }
    }
    private class RemoveItem extends displayPanel{
        private final JTextField itemToRemove;
        private final JButton remove;
        private final JButton cancel;
        private final JLabel status;
        //Constructor
        protected RemoveItem(){
            this.setPanelName("Remove an Item");
            this.setLayout(new GridBagLayout());
            windowTitle.setText(this.getLabel());
            usr.setWishList(client.getWishList(usr));
            //prepare components
            JLabel removeHere = new JLabel("Enter the number of the item on the list you would like to remove");
            removeHere.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            status = new JLabel("Status will be displayed here");
            status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            status.setVisible(false);
            itemToRemove = new JTextField(25);
            remove = new JButton("Remove");
            cancel = new JButton("Cancel");
            prepareButtonHandlers();
            prepareKeyListeners();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.gridx = 0;
            this.add(status, gbc);
            gbc.gridy = 1;
            this.add(removeHere, gbc);
            gbc.gridy = 2;
            this.add(itemToRemove, gbc);
        }
        private void error(String error){
            this.status.setForeground(Color.RED);
            this.status.setText(error);
            this.status.setVisible(true);
            this.repaint();
        }
        private void prepareKeyListeners() {
            this.itemToRemove.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        remove.doClick();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }

        private void prepareButtonHandlers() {
            remove.addActionListener(e -> {
                System.out.println("Remove Item");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        try{
                            User usr = new User();
                            int index =  Integer.parseInt(itemToRemove.getText()) - 1;
                            System.out.println("index of item to be removed: " + (index));
                            System.out.println("Item to be removed: " + index);
                            String entry = "" + index;
                            usr.setEntry(entry);
                            System.out.println("Entry in usr: " + usr.getEntry());
                            String result = client.removeItem(usr);
                            if(result.equals("success")){
                                updateData(new Interaction());
                            }
                            else {
                                System.out.println(result);
                            }
                        }
                        catch(IndexOutOfBoundsException ex){
//                                ex.printStackTrace();
                            error("You must enter the number of an item on your list");
                        }
                        catch(NumberFormatException ex){
                            error("you must enter a number ex: 1");
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
            cancel.addActionListener(e -> {
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
            });
            updateControl(cancel,remove);
        }
    }
    private class ClearWishList extends displayPanel{
        private final JButton confirm;
        private final JButton cancel;

        //constructor
        public ClearWishList(){
            this.setPanelName("Clear Wish List");
            windowTitle.setText(this.getLabel());
            this.setLayout(new GridBagLayout());
            JLabel message = new JLabel("Are you sure you want to clear your wish list?");
            message.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            message.setForeground(Color.RED);
            confirm = new JButton("Yes");
            confirm.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            cancel = new JButton("No");
            cancel.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            prepareButtonHandlers();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            this.add(message, gbc);
            updateControl(cancel,confirm);

        }

        private void prepareButtonHandlers() {
            confirm.addActionListener(actionEvent -> {
                System.out.println("Clear list");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        String result = client.clearWishList();
                        if(result.equals("success")) {
                            updateData(new Interaction());
                            client.unconfirmWishList();
                        }
                        else {
                            System.out.println(result);
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
            cancel.addActionListener(actionEvent -> {
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
            });
        }
    }
    private class AccountSettings extends displayPanel{
        private final JTextField passText;
        private final JTextField emailText;
        private final JTextField nameText;
        private final JButton cancel;
        private final JButton apply;
        private final JLabel status;

        //constructor
        protected AccountSettings(){
            usr = client.getUser(usr);
            this.setPanelName("Account Settings");
            windowTitle.setText(this.getLabel());
            this.setLayout(new GridBagLayout());
            //prepare components
            JLabel password = new JLabel("Password");
            password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JLabel email = new JLabel("Email");
            email.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            JLabel name = new JLabel("Your name");
            name.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            passText = new JTextField(usr.getPassword(), 25);
            emailText = new JTextField(usr.getEmail(), 25);
            nameText = new JTextField(usr.getName(), 25);
            cancel = new JButton("Back");
            apply = new JButton("Apply");
            status = new JLabel("Status will be displayed here");
            status.setVisible(false);
            status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            status.setForeground(Color.BLACK);
            prepareButtonHandlers();
            prepareKeyListeners();
            //add components to frame
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            this.add(status, gbc);
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            this.add(password, gbc);
            gbc.gridx = 1;
            this.add(passText, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(email, gbc);
            gbc.gridx = 1;
            this.add(emailText, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            this.add(name, gbc);
            gbc.gridx = 1;
            this.add(nameText, gbc);
        }

        //method to add key listeners
        private void prepareKeyListeners(){
            passText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        apply.doClick();
                    }
                    else{
                        status.setVisible(false);
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            emailText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        apply.doClick();
                    }
                    else{
                        status.setVisible(false);
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            nameText.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        apply.doClick();
                    }
                    else{
                        status.setVisible(false);
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }

        private void prepareButtonHandlers() {
            cancel.addActionListener(e -> {
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

            });
            apply.addActionListener(e -> {
                System.out.println("Apply");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        User usr = new User();
                        usr.setPassword(passText.getText());
                        usr.setEmail(emailText.getText());
                        usr.setName(nameText.getText());
                        String result = client.updateSettings(usr);
                        if(result.equals("success")) {
                            status.setText("Account Updated");
                        }
                        else{
                            status.setText(result);
                            status.setForeground(Color.red);
                        }
                        status.setVisible(true);
                    }
                    else {
                        updateData(new Connect(true));
                    }
                }
                else{
                    updateData(new Connect(true));
                }
            });
            updateControl(cancel,apply);
        }
    }

    //method to shut off client properly
    private void shutDown(){
        if(Data instanceof Connect || Data instanceof savedConnections){
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
        new ClientGUI();
    }
}
