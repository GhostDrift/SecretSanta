package Server;

import Common.displayPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
//import Server.java;


public class ServerGUI extends JFrame {

    private Server server;
    private  FieldPanel con;
    private displayPanel data;
    private final Label windowTitle;
//    private ClientGUI.Label windowTitle;


    public ServerGUI() {
        setTitle("Secret Santa Management System Server");

        // -- size of the frame: width, height
        int WIDTH = 680;
        int HEIGHT = 500;
        setSize(WIDTH, HEIGHT);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(1, 1));
        this.windowTitle = new Label();
        this.data = new ServerControl();
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
                        server.removeServerSocket();
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


            BottomPanel low = new BottomPanel();
            this.add(low, BorderLayout.SOUTH);

            // MENU Settings
            JMenuBar MenBar = new JMenuBar();
            JButton Act = new JButton("Activate Server");
            //  JButton DeAct = new JButton("Deactivate Server");
            JButton Conf = new JButton("Edit Config");
            JButton AConnect = new JButton("Number of Active Connections");
            JButton drawNames = new JButton("Draw Names");
            try {
                if(Config.getNamesDrawn()){
                    drawNames.setText("Clear Names");
                }
            } catch (ConfigNotInitializedException e) {
                e.printStackTrace();
            }


            // MenBar.add(Menu1);
            MenBar.add(Act);
            // MenBar.add(DeAct);
            MenBar.add(Conf);
            MenBar.add(AConnect);
            AConnect.setVisible(false);
            MenBar.add(drawNames);
            drawNames.setVisible(false);

            drawNames.addActionListener(actionEvent -> {
                System.out.println("Draw Names");
                if (drawNames.getText().equals("Draw Names")) {
                    server.drawNames(con,drawNames);
                    try {
                        Config.setNamesDrawn(true);
                        Config.saveConfig();
                    } catch (ConfigNotInitializedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    server.resetRecipientIDS();
                    addToTextArea("Names have been reset");
                    drawNames.setText("Draw Names");
                    try {
                        Config.setNamesDrawn(false);
                        Config.saveConfig();
                    } catch (ConfigNotInitializedException e) {
                        e.printStackTrace();
                    }
                }
                repaint();
            });

            JMenuBar MenBar2 = new JMenuBar();
            JButton WhoLog = new JButton("Who is Logged in ");
            JButton NumLog = new JButton("Number Logged in ");
            JButton NumReg = new JButton("Number Registered");
            JButton WhoLock = new JButton("Who is Locked Out");

            //Action listeners for buttons in MenBar2
            NumLog.addActionListener(e -> {
                System.out.println("Number of logged in");
                int numLoggedIn = server.getNumLoggedIn();
                addToTextArea( "Number of logged in clients: " + numLoggedIn);
                requestFocus();
            });
            WhoLog.addActionListener(e -> {
                System.out.println("Logged in accounts");
                StringBuilder result = new StringBuilder("Logged in accounts: \n");
                try {
                    ArrayList<String> loggedInAccounts =  server.getWhoLoggedIn();

                    for (String loggedInAccount : loggedInAccounts) {
//                       addToTextArea(loggedInAccounts.get(i) + "\n");
                        result.append(loggedInAccount).append("\n");
                    }
                    addToTextArea(result.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                requestFocus();
            });
            NumReg.addActionListener(e -> {
                System.out.println("Number registered");
                int numRegistered = 0;
                try {
                    numRegistered = server.getNumRegistered();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                addToTextArea( "Number of registered accounts: " + numRegistered);
                requestFocus();
            });
            WhoLock.addActionListener(e -> {
                System.out.println("Locked out accounts");
                StringBuilder result = new StringBuilder("Locked out accounts: \n");
                try {
                    ArrayList<String> LockedOutAccounts =  server.getWhoLockedOut();

                    for (String lockedOutAccount : LockedOutAccounts) {
//                       addToTextArea(loggedInAccounts.get(i) + "\n");
                        result.append(lockedOutAccount).append("\n");
                    }
                    addToTextArea(result.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                requestFocus();
            });

            MenBar2.add(WhoLog);
            MenBar2.add(NumLog);
            MenBar2.add(NumReg);
            MenBar2.add(WhoLock);


            this.add(MenBar2, BorderLayout.SOUTH);
            MenBar2.setVisible(false);


            // Activate Server
            Act.addActionListener(e -> {
                if(Act.getText().equals("Activate Server")) {
                    server = new Server(null);
                    server.start();
                    //server.stop();
                    Act.setText("Deactivate Server");
                    Conf.setVisible(false);
                    AConnect.setVisible(true);
                    MenBar2.setVisible(true);
                    drawNames.setVisible(true);
                    addToTextArea("Server is running");

                }
                else if(Act.getText().equals("Deactivate Server")){
                    server.stopServer();
                    server.removeServerSocket();
                    Act.setText("Activate Server");
                    Conf.setVisible(true);
                    drawNames.setVisible(false);
                    addToTextArea("Server has stopped");
                    AConnect.setVisible(false);
                    MenBar2.setVisible(false);
                }
                requestFocus();

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
            Conf.addActionListener(e -> {
                try {
                    updateData(new EditConfig());
                } catch (ConfigNotInitializedException ex) {
                    ex.printStackTrace();
                    System.out.println("Config has not been initialized");
                }
            });


            AConnect.addActionListener(e -> {
                System.out.println(server);
                String p = "Number of active connections: " + server.getConnections();
                addToTextArea(p + "");
                requestFocus();
            });


            this.add(MenBar, BorderLayout.NORTH);




            setVisible(true);



        }
    }
    //innerclass for holding the window title
    private static class Label extends JPanel{
        private final JLabel title;


        Label(){
            setLayout(new FlowLayout(FlowLayout.CENTER, 10,10));
            Border labelBorder = BorderFactory.createLineBorder(Color.black);
            String text = "";
            title = new JLabel(text);
            title.setBorder(labelBorder);
            title.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.add(title);
        }

        protected void setText(String newText){
            this.title.setText(newText);
            System.out.println("Label new text: " + newText);
        }
    }

    //innerclass for editing config
    protected class EditConfig extends displayPanel implements ItemListener {
//        private ControlArea control;
        private final JButton apply;
        private final JButton cancel;
        private final JTextField minUsernameValue;
        private final JTextField maxUsernameValue;
        private final JTextField illegalUsernameCharsList;
        private final JTextField minPasswordValue;
        private final JTextField maxPasswordValue;
        private final JTextField illegalPasswordCharList;
        private final JCheckBox uppercaseLetters;
        private final JCheckBox lowercaseLetters;
        private final JCheckBox numbers;
        private final JCheckBox symbols;
        private final JCheckBox enforcePassHistoryValue;
        private final JTextField validEmailFormatValue;
        private final JTextField systemEmailValue;
        private final JTextField systemEmailPasswordValue;
        private final JTextField userDatabaseFilePathValue;
        private final JTextField systemDatabaseFilePathValue;
        private final JTextField databaseUsernameValue;
        private final JTextField databasePasswordValue;
        private final JTextField lockoutThresholdValue;
        private boolean uppercaseValue;
        private boolean lowercaseValue;
        private boolean numbersValue;
        private boolean symbolsValue;
        private boolean passHistoryValue;

        protected EditConfig() throws ConfigNotInitializedException {
            this.setPanelName("Config Editor");
            setSize(WIDTH, HEIGHT);
//            setLayout(new FlowLayout(1,10,10));
            setLayout(new GridBagLayout());
            windowTitle.setText(this.getLabel());
            //initialize components
            JLabel usernameSettings = new JLabel("Username Settings");
            Font timesNewRoman = new Font("TimesRoman", Font.PLAIN, 15);
            usernameSettings.setFont(timesNewRoman);
            JLabel passwordSettings = new JLabel("Password Settings");
            passwordSettings.setFont(timesNewRoman);
            JLabel emailSettings = new JLabel("Email Settings");
            emailSettings.setFont(timesNewRoman);
            JLabel databaseAndOtherSettings = new JLabel("Database and other settings");
            databaseAndOtherSettings.setFont(timesNewRoman);
            JLabel minUsernameLength = new JLabel("Minimum Username Length");
            minUsernameLength.setFont(timesNewRoman);
            this.minUsernameValue = new JTextField(String.valueOf(Config.getMinUsernameLength()), 25);
            JLabel maxUsernameLength = new JLabel("Maximum Username Length");
            maxUsernameLength.setFont(timesNewRoman);
            this.maxUsernameValue = new JTextField(String.valueOf(Config.getMaxUsernameLength()), 25);
            JLabel illegalUsernameCharacters = new JLabel("Illegal Username Characters");
            illegalUsernameCharacters.setFont(timesNewRoman);
            String badUserChars;
            if (Config.getIllegalUsernameCharacters().length < 1){
                badUserChars = "";
            }
            else{
                badUserChars = Utilities.getStringFromArray(Config.getIllegalUsernameCharacters());
            }
            this.illegalUsernameCharsList = new JTextField(badUserChars,25);
            JLabel minPasswordLength = new JLabel("Minimum Password Length: ");
            minPasswordLength.setFont(timesNewRoman);
            this.minPasswordValue = new JTextField(String.valueOf(Config.getMinPasswordLength()), 25);
            JLabel maxPasswordLength = new JLabel("Maximum Password Length");
            maxPasswordLength.setFont(timesNewRoman);
            this.maxPasswordValue = new JTextField(String.valueOf(Config.getMaxPasswordLength()), 25);
            JLabel illegalPasswordCharacters = new JLabel("Illegal Password Characters");
            illegalPasswordCharacters.setFont(timesNewRoman);
            this.illegalPasswordCharList = new JTextField(Config.getIllegalPasswordCharacters(),25);
            JLabel requiredCharSets = new JLabel("Required Character sets:");
            requiredCharSets.setFont(timesNewRoman);
            boolean[] charSetRequirements = Config.getRequiredCharacterSets();
            this.lowercaseLetters = new JCheckBox("Lowercase",charSetRequirements[0]);
            this.lowercaseValue = charSetRequirements[0];
            this.lowercaseLetters.setMnemonic(KeyEvent.VK_C);
            this.uppercaseLetters = new JCheckBox("Uppercase",charSetRequirements[1]);
            this.uppercaseValue = charSetRequirements[1];
            this.uppercaseLetters.setMnemonic(KeyEvent.VK_G);
            this.numbers = new JCheckBox("Numbers", charSetRequirements[2]);
            this.numbersValue = charSetRequirements[2];
            this.numbers.setMnemonic(KeyEvent.VK_H);
            this.symbols = new JCheckBox("Special", charSetRequirements[3]);
            this.symbolsValue = charSetRequirements[3];
            this.symbols.setMnemonic(KeyEvent.VK_T);
            this.enforcePassHistoryValue = new JCheckBox("",Config.getEnforcePasswordHistory());
            this.enforcePassHistoryValue.setMnemonic(KeyEvent.VK_0);
            JLabel enforcePassHistory = new JLabel("Enforce Password History:");
            enforcePassHistory.setFont(timesNewRoman);
            JLabel validEmailFormat = new JLabel("Valid Email Format");
            validEmailFormat.setFont(timesNewRoman);
            this.validEmailFormatValue = new JTextField(Config.getValidEmailFormat(),25);
            JLabel systemEmail = new JLabel("System Email: ");
            systemEmail.setFont(timesNewRoman);
            this.systemEmailValue = new JTextField(Config.getEmailUsername(),25);
            JLabel systemEmailPassword = new JLabel("System Email Password");
            systemEmailPassword.setFont(timesNewRoman);
            this.systemEmailPasswordValue = new JTextField(Config.getEmailPassword(),25);
            JLabel userDatabaseFilePath = new JLabel("User Database File Path");
            userDatabaseFilePath.setFont(timesNewRoman);
            this.userDatabaseFilePathValue = new JTextField(Config.getUserDatabaseServerAddress(),25);
            JLabel systemDatabaseFilePath = new JLabel("System Database File Path");
            systemDatabaseFilePath.setFont(timesNewRoman);
            this.systemDatabaseFilePathValue = new JTextField(Config.getSystemDatabaseServerAddress(),25);
            JLabel databaseUsername = new JLabel("Database Username");
            databaseUsername.setFont(timesNewRoman);
            this.databaseUsernameValue = new JTextField(Config.getDatabaseUsername(),25);
            JLabel databasePassword = new JLabel("Database Password");
            databasePassword.setFont(timesNewRoman);
            this.databasePasswordValue = new JTextField(Config.getDatabasePassword(),25);
            JLabel lockoutThreshold = new JLabel("Lockout Threshold");
            lockoutThreshold.setFont(timesNewRoman);
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
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 5;
            this.add(usernameSettings, gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(minUsernameLength, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(minUsernameValue, gbc);
            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            this.add(maxUsernameLength, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(maxUsernameValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            this.add(illegalUsernameCharacters, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(illegalUsernameCharsList, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 5;
            this.add(passwordSettings, gbc);
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            this.add(minPasswordLength, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(minPasswordValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            this.add(maxPasswordLength, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(maxPasswordValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 1;
            this.add(illegalPasswordCharacters, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(illegalPasswordCharList, gbc);
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 1;
            this.add(requiredCharSets, gbc);
            gbc.gridx = 1;
            this.add(lowercaseLetters, gbc);
            gbc.gridx = 2;
            this.add(uppercaseLetters, gbc);
            gbc.gridx = 3;
            this.add(numbers, gbc);
            gbc.gridx = 4;
            this.add(symbols, gbc);
            gbc.gridx = 0;
            gbc.gridy = 9;
            this.add(enforcePassHistory, gbc);
            gbc.gridx = 1;
            this.add(enforcePassHistoryValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 10;
            gbc.gridwidth = 5;
            this.add(emailSettings, gbc);
            gbc.gridy = 11;
            gbc.gridwidth = 1;
            this.add(validEmailFormat, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(validEmailFormatValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 12;
            gbc.gridwidth = 1;
            this.add(systemEmail, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(systemEmailValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 13;
            gbc.gridwidth = 1;
            this.add(systemEmailPassword, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(systemEmailPasswordValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 14;
            gbc.gridwidth = 5;
            this.add(databaseAndOtherSettings, gbc);
            gbc.gridy = 15;
            gbc.gridwidth = 1;
            this.add(userDatabaseFilePath, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(userDatabaseFilePathValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 16;
            gbc.gridwidth = 1;
            this.add(systemDatabaseFilePath, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(systemDatabaseFilePathValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 17;
            gbc.gridwidth = 1;
            this.add(databaseUsername, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(databaseUsernameValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 18;
            gbc.gridwidth = 1;
            this.add(databasePassword, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(databasePasswordValue, gbc);
            gbc.gridx = 0;
            gbc.gridy = 19;
            gbc.gridwidth = 1;
            this.add(lockoutThreshold, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 4;
            this.add(lockoutThresholdValue, gbc);
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
private void saveChanges() throws ConfigNotInitializedException{
            int flag = 0;
            try {
                Config.setMinUsernameLength(Integer.parseInt(minUsernameValue.getText()));
                minUsernameValue.setForeground(Color.BLACK);
            } catch (Exception e) {
                minUsernameValue.setForeground(Color.RED);
                minUsernameValue.setText(minPasswordValue.getText() + " Must be an integer");
//                e.printStackTrace();
                flag = 1;
            }
            try {
                Config.setMaxUsernameLength(Integer.parseInt(maxUsernameValue.getText()));
            } catch (Exception e) {
                maxUsernameValue.setForeground(Color.RED);
                maxUsernameValue.setText(maxPasswordValue.getText() + " Must be an integer");
//                e.printStackTrace();
                flag = 1;
            }
            try {
                Config.setIllegalUsernameCharacters(getCharArray(illegalUsernameCharsList.getText()));
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                illegalUsernameCharsList.setForeground(Color.RED);
                illegalUsernameCharsList.setText(illegalUsernameCharsList.getText() + " Must only include Chars");
                flag = 1;
            }
            try{
                Config.setMinPasswordLength(Integer.parseInt(minPasswordValue.getText()));
            }
            catch(Exception e){
//                e.printStackTrace();
                minPasswordValue.setForeground(Color.RED);
                minPasswordValue.setText(minPasswordValue.getText() + " Must be an integer");
                flag = 1;
            }
            try{
                Config.setMaxPasswordLength(Integer.parseInt(maxPasswordValue.getText()));
            }
            catch(Exception e){
                maxPasswordValue.setForeground(Color.RED);
                maxPasswordValue.setText(maxPasswordValue.getText() + " Must be an integer");
                flag = 1;
            }
            try{
                Config.setIllegalPasswordCharacters(illegalPasswordCharList.getText());
            }
            catch(Exception e){
                illegalPasswordCharList.setForeground(Color.RED);
                flag = 1;
            }
            try{
                Config.setValidEmailFormat(validEmailFormatValue.getText());
            } catch (InvalidAttributeValueException e) {
                validEmailFormatValue.setForeground(Color.RED);
                flag = 1;
            }
            try {
                Config.setEmailUsername(systemEmailValue.getText());
            } catch (InvalidAttributeValueException e) {
//                e.printStackTrace();
                systemEmailValue.setForeground(Color.RED);
                flag = 1;
            }
            try {
                Config.setEmailPassword(systemEmailPasswordValue.getText());
            } catch (InvalidAttributeValueException e) {
//                e.printStackTrace();
                systemEmailPasswordValue.setForeground(Color.RED);
                flag = 1;
            }
            try {
                Config.setUserDatabaseServerAddress(userDatabaseFilePathValue.getText());
            } catch (InvalidAttributeValueException e) {
//                e.printStackTrace();
                userDatabaseFilePathValue.setForeground(Color.RED);
                flag = 1;
            }
            try {
                Config.setSystemDatabaseServerAddress(systemDatabaseFilePathValue.getText());
            } catch (InvalidAttributeValueException e) {
//                e.printStackTrace();
                systemDatabaseFilePathValue.setForeground(Color.RED);
                flag = 1;
            }
            try {
                Config.setDatabaseUsername(databaseUsernameValue.getText());
            } catch (InvalidAttributeValueException e) {
//                e.printStackTrace();
                databaseUsernameValue.setForeground(Color.RED);
                flag = 1;
            }
            try {
                Config.setDatabasePassword(databasePasswordValue.getText());
            } catch (InvalidAttributeValueException e) {
//                e.printStackTrace();
                databasePasswordValue.setForeground(Color.RED);
                flag = 1;
            }
            try{
                Config.setLockoutThreshold(Integer.parseInt(lockoutThresholdValue.getText()));
            }
            catch (Exception e){
                lockoutThresholdValue.setForeground(Color.RED);
                lockoutThresholdValue.setText(lockoutThresholdValue.getText() + " Must be an integer");
                flag = 1;
            }
            if(Config.getEnforcePasswordHistory() != passHistoryValue){
                UserDatabase usrDB = new UserDatabase(
                        Config.getUserDatabaseServerAddress(),
                        Config.getDatabaseUsername(),
                        Config.getDatabasePassword());
                if(passHistoryValue){
                    usrDB.populatePassHistory();
                }
                else{
                    usrDB.clearPassHistory();
                }
            }
            Config.setEnforcePasswordHistory(passHistoryValue);
            boolean[] requiredChars = Config.getRequiredCharacterSets();
            boolean value;
            for(int i = 0; i< requiredChars.length ; i ++){
                if(i ==0){
                    value = lowercaseValue;
                }
                else if( i ==1 ){
                    value = uppercaseValue;
                }
                else if(i == 2){
                    value = numbersValue;
                }
                else {
                    value = symbolsValue;
                }
                requiredChars[i] = value;
            }

            if(flag == 0) {
                Config.saveConfig();
                updateData(new ServerControl());
            }
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
                i = 2;
                value = true;
            }
            else if(source == symbols){
                i = 3;
                value = true;
            }
            else if(source == enforcePassHistoryValue){
                value = true;
                flag =4;
            }
            if(e.getStateChange()==ItemEvent.DESELECTED){
                value = false;
            }
            if(flag == 0) {
                if(i == 0){
                    lowercaseValue = value;
                }
                else if (i ==1){
                    uppercaseValue = value;
                }
                else if( i == 2){
                    numbersValue = value;
                }
                else{
                    symbolsValue = value;
                }
            }
            else{
                passHistoryValue = value;
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
                        illegalUsernameCharsList.setText(Utilities.getStringFromArray(Config.getIllegalUsernameCharacters()));
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
            cancel.addActionListener(e -> {
                System.out.println("Cancel");
                updateData(new ServerControl());
            });
            apply.addActionListener(actionEvent -> {
                System.out.println("Apply");
                try {
                    saveChanges();
                } catch (ConfigNotInitializedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void addToTextArea(String s) {
        con.addToTextArea(s);
    }


    public static class FieldPanel extends JPanel {

        //private JButton loadButton;
        private final TextArea Text;

        public FieldPanel() {
//            setLayout(new FlowLayout(20, 20, 10));
            setLayout(new FlowLayout(FlowLayout.CENTER));

            Text = new TextArea("Information Will be Displayed Here", 20, 50);

            this.add(Text);


        }

        public void addToTextArea(String x) {
            Text.setText("");
            Text.append(x + "\n");
        }


    }

    public static class BottomPanel extends JPanel {
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
            Close.addActionListener(arg0 -> System.exit(0));

            // Forgot Button Action
            Done.addActionListener(arg0 -> {

            });
        }

    }


    public static void main(String[] args) throws ConfigNotInitializedException {
        Config.initializeConfig("ServerConfiguration.conf");
        new ServerGUI();
    }
}


