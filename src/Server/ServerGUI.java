package Server;

import Common.ControlArea;
import Common.displayPanel;

import java.awt.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


import javax.swing.*;
import javax.swing.border.Border;
//import Server.java;


public class ServerGUI extends JFrame {

    private final int WIDTH = 680;
    private final int HEIGHT = 500;
    private final ServerGUI owner = null;

    private Server server;
    private  BottomPanel low;
    private  FieldPanel con;
    private displayPanel data;
    private Label windowTitle;
//    private ClientGUI.Label windowTitle;


    public ServerGUI() {
        setTitle("Secret Santa Management System Server");

        // -- size of the frame: width, height
        setSize(WIDTH, HEIGHT);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(1, 1));
        this.windowTitle = new Label();
        this.data = new ServerControl();
//        try {
//            this.data = new EditConfig();
//        } catch (ConfigNotInitializedException e) {
//            e.printStackTrace();
//        }
        this.add(data,BorderLayout.CENTER);
        this.add(windowTitle, BorderLayout.NORTH);
        setVisible(true);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            //modified code from
            //https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Close button has been pressed");
                if(server != null) {
                    if (server.isRunning()) {
                        System.out.println("Logging out and disconnecting clients");
                        server.stopServer();
                        server.removeServersocket();
                    }
                }
                System.exit(0);
            }
        });

    }
    protected void updateData(displayPanel dataNew){
        this.remove(data);
        this.data = dataNew;
        this.add(data);

//        System.out.println( "Login " +Data.getLabel());
//        this.remove(windowTitle);
//        this.windowTitle = new Label(Data.getLabel());
//        this.add(windowTitle, BorderLayout.NORTH);
//        this.windowTitle.repaint();
//        System.out.println("Label " + windowTitle.getText());
        this.repaint();
//        shutDown();
    }
    private class ServerControl extends displayPanel{
        protected ServerControl(){
            this.setPanelName("Server");
            this.setSpaces("                                                                                                              ");
//            setTitle("Server");
            windowTitle.setText(this.getLabel());
            // -- size of the frame: width, height
            setSize(WIDTH, HEIGHT);

            // -- center the frame on the screen
            setLocationRelativeTo(null);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // -- set the layout manager and add items
            // 5, 5 is the border around the edges of the areas
            setLayout(new BorderLayout(15, 5));
            con = new FieldPanel();
            this.add(con, BorderLayout.CENTER);


            low = new BottomPanel();
            this.add(low, BorderLayout.SOUTH);

            // MENU Settings
            JMenuBar MenBar = new JMenuBar();
            JButton Act = new JButton("Activate Server");
            //  JButton DeAct = new JButton("Deactivate Server");
            JButton Conf = new JButton("Edit Config");
            JButton AConnect = new JButton("Number of Active Connections");


            // MenBar.add(Menu1);
            MenBar.add(Act);
            // MenBar.add(DeAct);
            MenBar.add(Conf);
            MenBar.add(AConnect);
            AConnect.setVisible(false);
            JMenuBar MenBar2 = new JMenuBar();
            JButton WhoLog = new JButton("Who is Logged in ");
            JButton NumLog = new JButton("Number Logged in ");
            JButton NumReg = new JButton("Number Registered");
            JButton WhoLock = new JButton("Who is Locked Out");

            //Action listeners for buttons in MenBar2
            NumLog.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Number of logged in");
                    int numLoggedIn = server.getNumLoggedIn();
                    addToTextArea( "Number of logged in clients: " + numLoggedIn);
                    requestFocus();
                }

            });
            WhoLog.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Logged in accounts");
                    String result = "Logged in accounts: \n";
                    try {
                        ArrayList loggedInAccounts =  server.getWhoLoggedIn();

                        for(int i = 0; i< loggedInAccounts.size(); i++){
//                       addToTextArea(loggedInAccounts.get(i) + "\n");
                            result += loggedInAccounts.get(i) + "\n";
                        }
                        addToTextArea(result);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    requestFocus();
                }

            });
            NumReg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Number regestered");
                    int numRegistered = 0;
                    try {
                        numRegistered = server.getNumRegistered();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    addToTextArea( "Number of registered accounts: " + numRegistered);
                    requestFocus();
                }

            });
            WhoLock.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Locked out accounts");
                    String result = "Locked out accounts: \n";
                    try {
                        ArrayList LockedOutAccounts =  server.getWhoLockedOut();

                        for(int i = 0; i< LockedOutAccounts.size(); i++){
//                       addToTextArea(loggedInAccounts.get(i) + "\n");
                            result += LockedOutAccounts.get(i) + "\n";
                        }
                        addToTextArea(result);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    requestFocus();
                }

            });

            MenBar2.add(WhoLog);
            MenBar2.add(NumLog);
            MenBar2.add(NumReg);
            MenBar2.add(WhoLock);


            this.add(MenBar2, BorderLayout.SOUTH);
            MenBar2.setVisible(false);


            // Activate Server
            Act.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Act.getText().equals("Activate Server")) {
                        server = new Server(owner);
                        server.start();
                        //server.stop();
                        Act.setText("Deactivate Server");
                        Conf.setVisible(false);
                        AConnect.setVisible(true);
                        MenBar2.setVisible(true);
                        addToTextArea("Server is running");

                    }
                    else if(Act.getText().equals("Deactivate Server")){
//                    server.disconnectClients();
//                    server.stop();
                        server.stopServer();
                        server.removeServersocket();
                        Act.setText("Activate Server");
                        Conf.setVisible(true);
                        addToTextArea("Server has stopped");
                        AConnect.setVisible(false);
                        MenBar2.setVisible(false);
                    }
                    requestFocus();

                }


            });
            // Deactivate Server
//            DeAct.addActionListener(new ActionListener(){
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                }
//
//            });
// Config File Button
            Conf.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
//                        new ConfigEditor();
//                        dispose();
                        updateData(new EditConfig());
                    } catch (ConfigNotInitializedException ex) {
                        ex.printStackTrace();
                        System.out.println("Config has not been initialized");
                    }
                }

            });


            AConnect.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(server);
                    int p = server.getconnections();
                    addToTextArea(p + "");
                    requestFocus();
                }

            });


            this.add(MenBar, BorderLayout.NORTH);




            setVisible(true);



        }
    }
    //innerclass for holding the window title
    private static class Label extends JPanel{
        private JLabel title;
        private String text;


        Label(){
            setLayout(new FlowLayout(1, 10,10));
            Border labelBorder = BorderFactory.createLineBorder(Color.black);
            this.text = "";
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
        protected String getText(){
            return this.text;
        }
    }

    //innerclass for editing config
    protected class EditConfig extends displayPanel implements ItemListener {
//        private ControlArea control;
        private JButton apply;
        private JButton cancel;
        private JLabel usernameSettings;
        private JLabel passwordSettings;
        private JLabel emailSettings;
        private JLabel databaseAndOtherSettings;
        private JLabel minUsernameLength;
        private JTextField minUsernameValue;
        private JLabel maxUsernameLength;
        private JTextField maxUsernameValue;
        private JLabel illegalUsernameCharacters;
        private JTextField illegalUsernameChars;
        private JTextField illegalUsernameCharsList;
        private JLabel minPasswordLength;
        private JTextField minPasswordValue;
        private JLabel maxPasswordLength;
        private JTextField maxPasswordValue;
        private JLabel illegalPasswordCharacters;
        private JTextField illegalPasswordChars;
        private JTextField illegalPasswordCharList;
        private JLabel requiredCharSets;
        private JCheckBox uppercaseLetters;
        private JCheckBox lowercaseLetters;
        private JCheckBox numbers;
        private JCheckBox symbols;
        private JCheckBox enforcePassHistoryValue;
        private JLabel enforcePassHistory;
        private JLabel validEmailFormat;
        private JTextField validEmailFormatValue;
        private JLabel systemEmail;
        private JTextField systemEmailValue;
        private JLabel systemEmailPassword;
        private JTextField systemEmailPasswordValue;
        private JLabel userDatabaseFilePath;
        private JTextField userDatabaseFilePathValue;
        private JLabel systemDatabaseFilePath;
        private JTextField systemDatabaseFilePathValue;
        private JLabel databaseUsername;
        private JTextField databaseUsernameValue;
        private JLabel databasePassword;
        private JTextField databasePasswordValue;
        private JLabel lockoutThreshold;
        private JTextField lockoutThresholdValue;
        private final Font timesNewRoman = new Font("TimesRoman", Font.PLAIN, 15);
        private GridBagConstraints gbc = new GridBagConstraints();

        protected EditConfig() throws ConfigNotInitializedException {
//            Config.initializeConfig("ServerConfiguration.conf");
//            setTitle("Config Editor");
//            setLayout(new BorderLayout(15, 5));
//            this.minPasswordLength = new JLabel("Minimum Password Length: ");
//            this.minPasswordLength.setFont(new Font("TimesRoman", Font.PLAIN, 15));
//            this.minPasswordValue = new JTextField(String.valueOf(Config.getMinPasswordLength()), 25);
//            this.add(minPasswordLength,BorderLayout.CENTER);
//            this.add(minPasswordValue,BorderLayout.CENTER);
//            this.setVisible(true);
//            this.repaint();
            this.setPanelName("Config Editor");
//            this.setSpaces("                                                                                                              ");
//            setTitle("Config Editor");
            setSize(WIDTH, HEIGHT);
//            setLayout(new FlowLayout(1,10,10));
            setLayout(new GridBagLayout());
            windowTitle.setText(this.getLabel());
            //initialize components
//            this.settings = new JLabel("Username Settings");
//            this.usernameSettings = new JButton("Username Settings");
//            this.passwordSettings = new JButton("Password Settings");
//            this.emailSettings = new JButton("Email Settings");
//            this.databaseAndOtherSettings = new JButton("Database And Other Settings");
//            this.control = new ControlArea();
            this.usernameSettings = new JLabel("Username Settings");
            this.usernameSettings.setFont(timesNewRoman);
            this.passwordSettings = new JLabel("Password Settings");
            this.passwordSettings.setFont(timesNewRoman);
            this.emailSettings = new JLabel("Email Settings");
            this.emailSettings.setFont(timesNewRoman);
            this.databaseAndOtherSettings = new JLabel("Database and other settings");
            this.databaseAndOtherSettings.setFont(timesNewRoman);
            this.minUsernameLength = new JLabel("Minimum Username Length");
            this.minUsernameLength.setFont(timesNewRoman);
            this.minUsernameValue = new JTextField(String.valueOf(Config.getMinUsernameLength()), 25);
            this.maxUsernameLength = new JLabel("Maximum Username Length");
            this.maxUsernameLength.setFont(timesNewRoman);
            this.maxUsernameValue = new JTextField(String.valueOf(Config.getMaxUsernameLength()), 25);
            this.illegalUsernameCharacters = new JLabel("Illegal Username Characters");
            this.illegalUsernameCharacters.setFont(timesNewRoman);
            String badUserChars;
            if (Config.getIllegalUsernameCharacters().length < 1){
                badUserChars = "";
            }
            else{
                badUserChars = getStringFromArray(Config.getIllegalUsernameCharacters());
            }
            this.illegalUsernameCharsList = new JTextField(badUserChars,25);
            this.minPasswordLength = new JLabel("Minimum Password Length: ");
            this.minPasswordLength.setFont(timesNewRoman);
            this.minPasswordValue = new JTextField(String.valueOf(Config.getMinPasswordLength()), 25);
            this.maxPasswordLength = new JLabel("Maximum Password Length");
            this.maxPasswordLength.setFont(timesNewRoman);
            this.maxPasswordValue = new JTextField(String.valueOf(Config.getMaxPasswordLength()), 25);
            this.illegalPasswordCharacters = new JLabel("Illegal Password Characters");
            this.illegalPasswordCharacters.setFont(timesNewRoman);
            this.illegalPasswordCharList = new JTextField(Config.getIllegalPasswordCharacters(),25);
            this.requiredCharSets = new JLabel("Required Character sets:");
            this.requiredCharSets.setFont(timesNewRoman);
            boolean[] charSetRequirements = Config.getRequiredCharacterSets();
            this.lowercaseLetters = new JCheckBox("Lowercase",charSetRequirements[0]);
            this.lowercaseLetters.setMnemonic(KeyEvent.VK_C);
            this.uppercaseLetters = new JCheckBox("Uppercase",charSetRequirements[1]);
            this.uppercaseLetters.setMnemonic(KeyEvent.VK_G);
            this.numbers = new JCheckBox("Numbers", charSetRequirements[2]);
            this.numbers.setMnemonic(KeyEvent.VK_H);
            this.symbols = new JCheckBox("Symbols", charSetRequirements[3]);
            this.symbols.setMnemonic(KeyEvent.VK_T);
            this.enforcePassHistoryValue = new JCheckBox("",Config.getEnforcePasswordHistory());
            this.enforcePassHistoryValue.setMnemonic(KeyEvent.VK_0);
            this.enforcePassHistory = new JLabel("Enforce Password History:");
            this.enforcePassHistory.setFont(timesNewRoman);
            this.validEmailFormat = new JLabel("Valid Email Format");
            this.validEmailFormat.setFont(timesNewRoman);
            this.validEmailFormatValue = new JTextField(Config.getValidEmailFormat(),25);
            this.systemEmail = new JLabel("System Email: ");
            this.systemEmail.setFont(timesNewRoman);
            this.systemEmailValue = new JTextField(Config.getEmailUsername(),25);
            this.systemEmailPassword = new JLabel("System Email Password");
            this.systemEmailPassword.setFont(timesNewRoman);
            this.systemEmailPasswordValue = new JTextField(Config.getEmailPassword(),25);
            this.userDatabaseFilePath = new JLabel("User Database File Path");
            this.userDatabaseFilePath.setFont(timesNewRoman);
            this.userDatabaseFilePathValue = new JTextField(Config.getUserDatabaseServerAddress(),25);
            this.systemDatabaseFilePath = new JLabel("System Database File Path");
            this.systemDatabaseFilePath.setFont(timesNewRoman);
            this.systemDatabaseFilePathValue = new JTextField(Config.getSystemDatabaseServerAddress(),25);
            this.databaseUsername = new JLabel("Database Username");
            this.databaseUsername.setFont(timesNewRoman);
            this.databaseUsernameValue = new JTextField(Config.getDatabaseUsername(),25);
            this.databasePassword = new JLabel("Database Password");
            this.databasePassword.setFont(timesNewRoman);
            this.databasePasswordValue = new JTextField(Config.getDatabasePassword(),25);
            this.lockoutThreshold = new JLabel("Lockout Threshold");
            this.lockoutThreshold.setFont(timesNewRoman);
            this.lockoutThresholdValue = new JTextField(String.valueOf(Config.getLockoutThreshold()),25);
            this.cancel = new JButton("Cancel");
            this.apply = new JButton("Apply");
            this.lowercaseLetters.addItemListener(this);
            this.uppercaseLetters.addItemListener(this);
            this.numbers.addItemListener(this);
            this.symbols.addItemListener(this);
            this.enforcePassHistoryValue.addItemListener(this);
            prepareButtonHandlers();
            prepareMouseHandlers();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 5;
            this.add(usernameSettings,gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(minUsernameLength,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(minUsernameValue,gbc);
            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            this.add(maxUsernameLength, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(maxUsernameValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            this.add(illegalUsernameCharacters,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(illegalUsernameCharsList,gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 5;
            this.add(passwordSettings,gbc);
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            this.add(minPasswordLength,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(minPasswordValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            this.add(maxPasswordLength,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(maxPasswordValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 1;
            this.add(illegalPasswordCharacters,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(illegalPasswordCharList,gbc);
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 1;
            this.add(requiredCharSets,gbc);
            gbc.gridx = 1;
            this.add(lowercaseLetters,gbc);
            gbc.gridx = 2;
            this.add(uppercaseLetters,gbc);
            gbc.gridx = 3;
            this.add(numbers,gbc);
            gbc.gridx = 4;
            this.add(symbols,gbc);
            gbc.gridx = 0;
            gbc.gridy = 9;
            this.add(enforcePassHistory,gbc);
            gbc.gridx = 1;
            this.add(enforcePassHistoryValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 10;
            gbc.gridwidth = 5;
            this.add(emailSettings,gbc);
            gbc.gridy = 11;
            gbc.gridwidth = 1;
            this.add(validEmailFormat,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(validEmailFormatValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 12;
            gbc.gridwidth = 1;
            this.add(systemEmail,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(systemEmailValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 13;
            gbc.gridwidth = 1;
            this.add(systemEmailPassword,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(systemEmailPasswordValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 14;
            gbc.gridwidth = 5;
            this.add(databaseAndOtherSettings,gbc);
            gbc.gridy = 15;
            gbc.gridwidth = 1;
            this.add(userDatabaseFilePath,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(userDatabaseFilePathValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 16;
            gbc.gridwidth = 1;
            this.add(systemDatabaseFilePath,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(systemDatabaseFilePathValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 17;
            gbc.gridwidth = 1;
            this.add(databaseUsername,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(databaseUsernameValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 18;
            gbc.gridwidth = 1;
            this.add(databasePassword,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(databasePasswordValue,gbc);
            gbc.gridx = 0;
            gbc.gridy = 19;
            gbc.gridwidth = 1;
            this.add(lockoutThreshold,gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(lockoutThresholdValue,gbc);
            this.add(cancel);
            this.add(apply);
            this.setVisible(true);
            this.repaint();
            Config.printConfig();
        }
        private char[] getCharArray(String str){
            //modified code from Geeks for Geeks
//            https://www.geeksforgeeks.org/convert-a-string-to-character-array-in-java/
            char[] ch = new char[str.length()];

            // Copy character by character into array
            for (int i = 0; i < str.length(); i++) {
                ch[i] = str.charAt(i);
            }
            return ch;
        }
        private String getStringFromArray(char[] chars){
            String str = "";
            for(int i = 0; i < chars.length; i ++){
                str = str + chars[i];
            }
            return str;
        }
        private void saveChanges() throws ConfigNotInitializedException{
            try {
                Config.setMinUsernameLength(Integer.parseInt(minUsernameValue.getText()));
                minUsernameValue.setForeground(Color.BLACK);
            } catch (Exception e) {
                minUsernameValue.setForeground(Color.RED);
                minUsernameValue.setText(minPasswordValue.getText() + " Must be an integer");
//                e.printStackTrace();
            }
            try {
                Config.setMaxUsernameLength(Integer.parseInt(maxUsernameValue.getText()));
            } catch (Exception e) {
                maxUsernameValue.setForeground(Color.RED);
                maxUsernameValue.setText(maxPasswordValue.getText() + " Must be an integer");
//                e.printStackTrace();
            }
            try {
                Config.setIllegalUsernameCharacters(getCharArray(illegalUsernameCharsList.getText()));
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                illegalUsernameCharsList.setForeground(Color.RED);
                illegalUsernameCharsList.setText(illegalUsernameCharsList.getText() + " Must only include Chars");
            }
            try{
                Config.setMinPasswordLength(Integer.parseInt(minPasswordValue.getText()));
            }
            catch(Exception e){
//                e.printStackTrace();
                minPasswordValue.setForeground(Color.RED);
                minPasswordValue.setText(minPasswordValue.getText() + " Must be an integer");
            }
            try{
                Config.setMaxPasswordLength(Integer.parseInt(maxPasswordValue.getText()));
            }
            catch(Exception e){
                maxPasswordValue.setForeground(Color.RED);
                maxPasswordValue.setText(maxPasswordValue.getText() + " Must be an integer");
            }
            try{
                Config.setIllegalPasswordCharacters(illegalPasswordCharList.getText());
            }
            catch(Exception e){
                illegalPasswordCharList.setForeground(Color.RED);
            }
            try{
                Config.setValidEmailFormat(validEmailFormatValue.getText());
            } catch (InvalidAttributeValueException e) {
                validEmailFormatValue.setForeground(Color.RED);
            }
            Config.saveConfig();
        }
        public void itemStateChanged(ItemEvent e){
            int i = 0;
            int flag = 0;
            boolean value = false;
            Object source = e.getSource();
            if(source == lowercaseLetters){
                value = true;
            }
            else if(source == uppercaseLetters){
                i = 1;
                value = true;
            }
            else if(source == numbers){
                i = 3;
                value = true;
            }
            else if(source == symbols){
                i = 4;
                value = true;
            }
            else if(source == enforcePassHistoryValue){
                value = true;
                flag =1;
            }
            if(e.getStateChange()==ItemEvent.DESELECTED){
                value = false;
            }
            if(flag == 0) {
                try {
                    boolean[] reqChars = Config.getRequiredCharacterSets();
                    reqChars[i] = value;
                    System.out.println(Arrays.toString(Config.getRequiredCharacterSets()));
                } catch (ConfigNotInitializedException ex) {
                    ex.printStackTrace();
                }
            }
            else{
                try {
                    Config.setEnforcePasswordHistory(value);
                    System.out.println("Enforce Password History: " + Config.getEnforcePasswordHistory());
                } catch (ConfigNotInitializedException ex) {
                    ex.printStackTrace();
                }
            }

        }
        private void prepareMouseHandlers(){
            minUsernameValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    minUsernameValue.setForeground(Color.BLACK);
                    minUsernameValue.setText("");
                }
            });
            maxUsernameValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    maxUsernameValue.setForeground(Color.BLACK);
                    maxUsernameValue.setText("");
                }
            });
            illegalUsernameCharsList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    illegalUsernameCharsList.setForeground(Color.BLACK);
                    try {
                        illegalUsernameCharsList.setText(getStringFromArray(Config.getIllegalUsernameCharacters()));
                    } catch (ConfigNotInitializedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            minPasswordValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    minPasswordValue.setForeground(Color.BLACK);
                    minPasswordValue.setText("");
                }
            });
            maxPasswordValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    maxPasswordValue.setForeground(Color.BLACK);
                    maxPasswordValue.setText("");
                }
            });
            illegalPasswordCharList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    illegalPasswordCharList.setForeground(Color.black);
                    try {
                        illegalPasswordCharList.setText(Config.getIllegalPasswordCharacters());
                    } catch (ConfigNotInitializedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            lockoutThresholdValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    lockoutThresholdValue.setForeground(Color.BLACK);
                    lockoutThresholdValue.setText("");
                }
            });
            validEmailFormatValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    validEmailFormatValue.setForeground(Color.BLACK);
                }
            });
            systemEmailValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    systemEmailValue.setForeground(Color.black);
                }
            });
            systemEmailPasswordValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    systemEmailPasswordValue.setForeground(Color.black);
                }
            });
            userDatabaseFilePathValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    userDatabaseFilePathValue.setForeground(Color.BLACK);
                }
            });
            systemDatabaseFilePathValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    systemDatabaseFilePathValue.setForeground(Color.BLACK);
                }
            });
            databaseUsernameValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    databaseUsernameValue.setForeground(Color.BLACK);
                }
            });
            databasePasswordValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    databasePasswordValue.setForeground(Color.BLACK);
                }
            });
            lockoutThresholdValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    lockoutThresholdValue.setForeground(Color.BLACK);
                    lockoutThresholdValue.setText("");
                }
            });
        }

        private void prepareButtonHandlers() {
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cancel");
                    updateData(new ServerControl());
                }

            });
            apply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("Apply");
                    try {
                        saveChanges();
                    } catch (ConfigNotInitializedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void addToTextArea(String s) {
        con.addToTextArea(s);
    }


    public class FieldPanel extends JPanel {

        //private JButton loadButton;
        private final TextArea Text;

        public FieldPanel() {
            setLayout(new FlowLayout(20, 20, 10));

            Text = new TextArea("Information Will be Displayed Here", 20, 50);

            this.add(Text);


        }

        public void addToTextArea(String x) {
            Text.setText("");
            Text.append("SERVER receive: " + x + "\n");
        }


    }

    public class BottomPanel extends JPanel {
        private final JButton Close;
        private final JButton Done;


        public BottomPanel() {


            Close = new JButton("Exit");
            Done = new JButton("Create");


            this.add(Close);
            this.add(Done);

            PrepareButtons();
        }


        public void PrepareButtons() {

            // Close Button Action
            Close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    System.exit(0);
                }
            });

            // Forgot Button Action
            Done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {

                }
            });
        }

    }


    public static void main(String[] args) throws ConfigNotInitializedException {
        Config.initializeConfig("ServerConfiguration.conf");
        UserDatabase usrDB = new UserDatabase(Config.getUserDatabaseServerAddress(), Config.getDatabaseUsername(), Config.getDatabasePassword());
        new ServerGUI();
    }
}


