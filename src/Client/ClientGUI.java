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
    private User clientUser;
    private final Font timesRoman = new Font("TimesRoman", Font.PLAIN, 15);
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
        //stops the program when the close button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //prevents the window from being resized
        setResizable(false);
        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(0, 0));
        //create new WindowTitle
        this.windowTitle = new Label();
        //create control panel area
        control = new ControlArea();
        //set the data panel to display the connect panel.
        Data = new ClientGUI.newConnection(false);

//          x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));
        //set the title panel to display the Connect text
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
                        String ipFormat = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
                        Pattern ipPattern = Pattern.compile(ipFormat);
                        String portFormat = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
                        Pattern portPattern = Pattern.compile(portFormat);
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
                        Matcher matcher = ipPattern.matcher(host);
                        if (matcher.find()) {
                            matcher = portPattern.matcher(portnum.getText());
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
    private class SavedConnections extends displayPanel{
//        private final JTextField IP;
        private String IP;
        private int port;
private final ArrayList<String> ips;
        private final ArrayList<Integer> ports;
        private final JButton connect;
        private final JButton newConnection;
        private final JButton edit;
        private final JButton back;
        private final JLabel errorMessage;
        private final JComboBox<String> connectionList;

        SavedConnections(Boolean error, int index) {
            setLayout(new GridBagLayout());
//            setLayout(new BorderLayout(0,0));
            this.setPanelName("Saved Connections");
            this.setSpaces("                                                                                                              ");
            windowTitle.setText(this.getLabel());
            JLabel title = new JLabel("Saved Connections");
            title.setFont(timesRoman);
            JLabel i = new JLabel("HostName");
            i.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            ArrayList<String> tempIps;
            try {
                tempIps = Connections.getIps();
            } catch (Connections.ConnectionsNotInitialized e) {
                tempIps = new ArrayList<>();
                e.printStackTrace();
            }
            ips = tempIps;
            ArrayList<Integer> tempPorts;
            try{
                tempPorts = Connections.getPorts();
            } catch (Connections.ConnectionsNotInitialized connectionsNotInitialized) {
                tempPorts = new ArrayList<>();
                connectionsNotInitialized.printStackTrace();
            }
            ports = tempPorts;
            ArrayList<String> tempNames;
            try {
                tempNames =  Connections.getNames();
            } catch (Connections.ConnectionsNotInitialized e) {
                tempNames = new ArrayList<>();
            }
            ArrayList<String> names = tempNames;
            if(names.size() == 0){
                names.add(0,"Local host               ");
                ips.add("");
                ports.add(8000);
                saveConnections(names, ports, ips);
            }
            connectionList = new JComboBox<>(getStringArray(names.toArray()));
            connectionList.setBounds(50,50,90,20);
            //update IP and port to be the values of the first entry in the combo box
            IP = ips.get(index);
            port = ports.get(index);
            connectionList.setSelectedIndex(index);

            connect = new JButton("Connect");
            newConnection = new JButton("New");
            back = new JButton("Back");
            this.errorMessage = new JLabel("Server is unreachable");
            this.errorMessage.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.errorMessage.setForeground(Color.RED);
            this.edit = new JButton("Edit");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 5;
            gbc.ipady = 5;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 2;
            this.add(errorMessage,gbc);
            gbc.gridy = 1;
            this.add(Box.createVerticalStrut(20), gbc);
            gbc.gridy = 2;
            this.add(title, gbc);
            gbc.gridy = 3;
            this.add(Box.createVerticalStrut(20),gbc);
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            this.add(i, gbc);
            gbc.gridx = 1;
            this.add(connectionList,gbc);
            gbc.gridy = 5;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            this.add(Box.createVerticalStrut(25),gbc);
            gbc.gridy = 6;
            this.add(newConnection,gbc);
            gbc.gridx = 1;
            this.add(edit,gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
//            this.add(errorMessage, gbc);
            this.errorMessage.setVisible(false);
            PrepareActionListeners();
            prepareKeyListener();
            if(error){
                cannotConnect();
            }
            client = null;

        }
        protected String[] getStringArray(Object[] array){
            String[] result = new String[array.length];
            for(int i = 0; i< array.length; i++){
                if(array[i] instanceof String){
                    result[i] = (String) array[i];
                }
            }
            return result;
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

        public void PrepareActionListeners() {


            connect.addActionListener(e -> {
                if (client == null) {
                    try {
                        String ipFormat = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
                        Pattern ipPattern = Pattern.compile(ipFormat);
                        String portFormat = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
                        Pattern portPattern = Pattern.compile(portFormat);
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
                        Matcher matcher = ipPattern.matcher(host);
                        if (matcher.find()) {
                            matcher = portPattern.matcher(port + "");
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
//                        m.printStackTrace();
                        cannotConnect();
                    }
                }
            });
            newConnection.addActionListener(actionEvent -> {
                System.out.println("Open new connections");
                updateData(new newConnection(false));
            });
            connectionList.addActionListener(actionEvent -> {
                    int index = connectionList.getSelectedIndex();
                    if(index == 0){
                        IP = "";
                        port = 800;
                    }
                    else{
                        IP = ips.get(index);
                        port = ports.get(index);
                    }
            });
            edit.addActionListener(actionEvent -> {
                System.out.println("Edit connection window");
                updateData(new EditConnection(connectionList.getSelectedIndex(),0));
            });
            back.addActionListener(actionEvent -> {
                System.out.println("Open new connections");
                updateData(new newConnection(false));
            });

            updateControl(back,connect);

        }
        private void prepareKeyListener(){
        }
    }

    private void saveConnections(ArrayList<String> names, ArrayList<Integer> ports, ArrayList<String> ips) {
        try {
            Connections.setNames(names);
            Connections.setIps(ips);
            Connections.setPorts(ports);
            Connections.saveConnections();
        } catch (Connections.ConnectionsNotInitialized e) {
            e.printStackTrace();
        }

    }
private class EditConnection extends displayPanel{
        private ArrayList<String> names;
        private ArrayList<String> ips;
        private ArrayList<Integer> ports;
        private JButton save;
        private JButton cancel;
        private JButton delete;
        private JTextField hostText;
        private JTextField ipText;
        private JTextField portText;
        private JLabel title;
        private int index;
        private int origin;

        //constructor
        public EditConnection(int index, int origin){
            try {
                this.index = index;
                this.origin = origin;
                this.setLayout(new GridBagLayout());
                this.setPanelName("Edit Connection");
                this.setSpaces("                                                                                                              ");
                windowTitle.setText(this.getLabel());
                this.names = Connections.getNames();
                this.ips = Connections.getIps();
                this.ports = Connections.getPorts();
                this.save = new JButton("Save");
                this.cancel = new JButton("Cancel");
                this.delete = new JButton("Delete");
                this.hostText = new JTextField(names.get(index),25);
                this.ipText = new JTextField(ips.get(index), 25);
                this.portText = new JTextField(ports.get(index) + "",25);
                JLabel hostLabel = new JLabel("Host Name");
                hostLabel.setFont(timesRoman);
                JLabel ipLabel = new JLabel("IP");
                ipLabel.setFont(timesRoman);
                title = new JLabel("Connection to be edited");
                title.setFont(timesRoman);
                JLabel portLabel = new JLabel("Port");
                portLabel.setFont(timesRoman);
                prepareActionListeners();
                prepareKeyListeners();
                //Add components to window
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = 2;
                this.add(title,gbc);
                gbc.gridwidth = 1;
                gbc.gridy = 1;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridy = 2;
                this.add(hostLabel,gbc);
                gbc.gridx = 1;
                this.add(hostText,gbc);
                gbc.gridy = 3;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridy = 4;
                gbc.gridx = 0;
                this.add(ipLabel,gbc);
                gbc.gridx = 1;
                this.add(ipText,gbc);
                gbc.gridy = 5;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridx = 0;
                gbc.gridy = 6;
                this.add(portLabel,gbc);
                gbc.gridx = 1;
                this.add(portText,gbc);
                gbc.gridy = 7;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridy = 8;
                gbc.gridwidth = 2;
                gbc.gridx = 0;
                this.add(delete,gbc);
                if(origin == 1){
                    delete.setVisible(false);
                }
                this.setVisible(true);


            } catch (Connections.ConnectionsNotInitialized connectionsNotInitialized) {
                connectionsNotInitialized.printStackTrace();
            }
        }

    private void prepareKeyListeners() {
          KeyListener k = new KeyListener() {
              @Override
              public void keyTyped(KeyEvent keyEvent) {

              }

              @Override
              public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 10){
                    save.doClick();
                }
              }

              @Override
              public void keyReleased(KeyEvent keyEvent) {

              }
          };
          ipText.addKeyListener(k);
          hostText.addKeyListener(k);
          portText.addKeyListener(k);
    }

    private void prepareActionListeners() {
            save.addActionListener(actionEvent -> {
                if(names.contains(hostText.getText())){
                    int testIndex = names.indexOf(hostText.getText());
                    if(testIndex != index){
                        title.setForeground(Color.RED);
                        title.setText("That name is already taken");
                    }
                    else{
                        saveConnection();
                    }
                }
                else saveConnection();

            });
            cancel.addActionListener(actionEvent -> {
                System.out.println("Saved connections");
                if(origin == 0) {
                    updateData(new SavedConnections(false, index));
                }
                else if(origin == 1){
                    names.remove(names.size()-1);
                    ips.remove(ips.size()-1);
                    ports.remove(ports.size()-1);
                    saveConnections(names,ports,ips);
                    updateData(new newConnection(false));
                }
            });
            delete.addActionListener(actionEvent -> {
                System.out.println("Delete connection");
                updateData(new ConfirmDelete(index,origin));
            });
            updateControl(cancel,save);
    }
    private void saveConnection(){
        names.remove(index);
        ips.remove(index);
        ports.remove(index);
        names.add(index,hostText.getText());
        ips.add(index,ipText.getText());
        ports.add(index,Integer.parseInt(portText.getText()));
        try {
            Connections.setNames(names);
            Connections.setPorts(ports);
            Connections.setIps(ips);
            Connections.saveConnections();
            if(origin == 0) {
                updateData(new SavedConnections(false, index));
            }
            else if( origin == 1){
                updateData(new newConnection(false));
            }
        } catch (Connections.ConnectionsNotInitialized e) {
            e.printStackTrace();
        }
    }

}
    private class ConfirmDelete extends displayPanel{
        private JButton confirm;
        private JButton cancel;
        private int index;
        private int origin;
        private ArrayList<String> names;
        private ArrayList<String> ips;
        private ArrayList<Integer> ports;

        public ConfirmDelete(int index,int origin){
            try{
                this.index = index;
                this.origin = origin;
                names = Connections.getNames();
                ips = Connections.getIps();
                ports = Connections.getPorts();
                this.setLayout(new GridBagLayout());
                this.setPanelName("Delete Connection");
                this.setSpaces("                                                                                                              ");
                windowTitle.setText(this.getLabel());
                this.confirm = new JButton("Yes Delete");
                this.cancel = new JButton("Cancel");
                JLabel hostLabel = new JLabel("HostName: " + names.get(index));
                JLabel ipLabel = new JLabel("IP: " + ips.get(index));
                JLabel portLabel = new JLabel("Port: " + ports.get(index));
                JLabel areYouSure = new JLabel("Are you sure you want to delete this connection?");
                hostLabel.setFont(timesRoman);
                ipLabel.setFont(timesRoman);
                portLabel.setFont(timesRoman);
                areYouSure.setFont(timesRoman);
                prepareActionListeners();
                //add elements to gui
                GridBagConstraints gbc = new GridBagConstraints();
                this.add(areYouSure,gbc);
                gbc.gridy = 1;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridy = 2;
                this.add(hostLabel,gbc);
                gbc.gridy = 3;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridy = 4;
                this.add(ipLabel,gbc);
                gbc.gridy = 5;
                this.add(Box.createVerticalStrut(20),gbc);
                gbc.gridy = 6;
                this.add(portLabel,gbc);


            } catch (Connections.ConnectionsNotInitialized connectionsNotInitialized) {
                connectionsNotInitialized.printStackTrace();
            }
        }

        private void prepareActionListeners() {
            confirm.addActionListener(actionEvent -> {
                try {
                    names.remove(index);
                    ips.remove(index);
                    ports.remove(index);
                    Connections.setNames(names);
                    Connections.setIps(ips);
                    Connections.setPorts(ports);
                    Connections.saveConnections();
                    if (origin == 0) {
                        updateData(new SavedConnections(false, 0));
                    } else if (origin == 1) {
                        updateData(new newConnection(false));
                    }
                } catch (Connections.ConnectionsNotInitialized connectionsNotInitialized) {
                    connectionsNotInitialized.printStackTrace();
                }
            });
            cancel.addActionListener(actionEvent -> updateData(new EditConnection(index,origin)));
            updateControl(cancel,confirm);
        }
    }
    private class newConnection extends  displayPanel{
        private final JTextField IP;
        private final JButton Adv;
        private final JLabel Portn;
        private final JTextField portnum;
        private final JButton connect;
        private boolean t = false;
        private final JLabel errorMessage;
        private final JButton savedConnections;
        private final JButton save;

        public newConnection(Boolean error) {
            setLayout(new GridBagLayout());
//            setLayout(new BorderLayout(0,0));
            this.setPanelName("Connect");
            this.setSpaces("                                                                                                              ");
            windowTitle.setText(this.getLabel());
            JLabel i = new JLabel("IP");
            i.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            IP = new JTextField("", 25);

            Portn = new JLabel("Port Number");
            Portn.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            portnum = new JTextField("8000", 25);

            Adv = new JButton("Advanced...");
            connect = new JButton("Connect");
            savedConnections = new JButton("Saved Connections");
            save = new JButton("Save");
            this.errorMessage = new JLabel("Server is unreachable");
            this.errorMessage.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.errorMessage.setForeground(Color.RED);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridwidth = 3;
            this.add(errorMessage,gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            this.add(i, gbc);
            gbc.gridx = 1;
            this.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridx = 2;
            this.add(IP, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            Portn.setVisible(t);
            this.add(Portn, gbc);
            gbc.gridx = 1;
            this.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridx = 2;
//            gbc.fill = GridBagConstraints.HORIZONTAL;
            portnum.setVisible(false);
            this.add(portnum, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 3;
            this.add(save, gbc);
            gbc.gridy = 5;
            this.add(Box.createVerticalStrut(30),gbc);
            gbc.gridy = 6;
            this.add(savedConnections,gbc);
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
                        String ipFormat = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
                        Pattern ipPattern = Pattern.compile(ipFormat);
                        String portFormat = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
                        Pattern portPattern = Pattern.compile(portFormat);
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
                        Matcher matcher = ipPattern.matcher(host);
                        if (matcher.find()) {
                            matcher = portPattern.matcher(portnum.getText());
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
//                        m.printStackTrace();
                        cannotConnect();
                    }
                }
            });
            save.addActionListener(actionEvent -> {
                try {
                    Connections.getNames().add("");
                    Connections.getIps().add(IP.getText());
                    Connections.getPorts().add(Integer.parseInt(portnum.getText()));
                    Connections.saveConnections();
                    updateData(new EditConnection(Connections.getNames().size()-1,1));
                } catch (Connections.ConnectionsNotInitialized connectionsNotInitialized) {
                    connectionsNotInitialized.printStackTrace();
                }
            });
            savedConnections.addActionListener(actionEvent -> updateData(new SavedConnections(false,0)));

//            updateControl(savedConnections,Adv,connect);
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
                        if(!t) {
                            connect.doClick();
                        }
                        else{
                            portnum.requestFocus();
                        }
                    }
                    else{
                        errorMessage.setVisible(false);
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
            this.portnum.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if(keyEvent.getKeyCode() == 10){
                        connect.doClick();
                    }
                    else{
                        errorMessage.setVisible(false);
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }
}
    private class Login extends displayPanel{
        private final JLabel username;
        private final JLabel password;
        private final JTextField usrName;
//        private final JTextField pasWord;
        private final JPasswordField pasWord;
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
             //Set windowTitle to the label of the data panel
             windowTitle.setText(this.getLabel());
             //create the Jlabels
             username = new JLabel("Username");
             username.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             password = new JLabel("Password");
             password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             status = new JLabel("");
             status.setFont(new Font("TimesRoman", Font.PLAIN, 15));
             status.setVisible(false);
             //create the JTextFields
             usrName = new JTextField("", 25);
//             pasWord = new JTextField("", 25);
             pasWord = new JPasswordField(25);
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
             //Set windowTitle to the label of the data panel
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
             //create the JTextFields
             usrName = new JTextField("", 25);
//             pasWord = new JTextField("", 25);
             pasWord = new JPasswordField(25);
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
                 updateData(new newConnection(false));

             });
             register.addActionListener(e -> {
                 System.out.println("Register");
                 if(client != null) {
                     if (client.networkaccess.testConnection()) {
                         updateData(new Register(0));
                     } else {
                         updateData(new SavedConnections(true,0));
                     }
                 }
                 else{
                     updateData(new SavedConnections(true,0));
                 }
             });
             login.addActionListener(e -> {
                 System.out.println("Login");
                 String username = usrName.getText();
//                 String password = pasWord.getText();
                 String password = getPassword(pasWord.getPassword());
                 System.out.println("Password: " + password);
                 try {
                     if (client.networkaccess.testConnection()) {
                         if ((!username.equals("")) && (!password.equals(""))) {
                             Message msg = client.login(username.toLowerCase(),password);
                             if(msg.message.equals("success")){
                                 clientUser = new User(username.toLowerCase());
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
                         updateData(new SavedConnections(true,0));
                     }
                 } catch(Exception n){
                     updateData(new SavedConnections(true,0));
                 }
             });
             recover.addActionListener(e -> {
                 if(client != null){
                     if(client.networkaccess.testConnection()){
                        updateData(new Recover());
                     }
                     else{
                         updateData(new SavedConnections(true,0));
                     }
                 }
                 else{
                     updateData(new SavedConnections(true,0));
                 }
                 System.out.println("Recover Password");

             });

             updateControl(disconnect,register,recover,login);
         }
         private String getPassword(char[] text){
             StringBuilder s = new StringBuilder();
             for (char c : text) {
                 s.append(c);
             }
             return s.toString();
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
            //Set windowTitle to the label of the data panel
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
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
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

        Register(int source){
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
            if(source == 1){
                usrName.setText(clientUser.getUsername());
                pasWord.setText(clientUser.getPassword());
                eMailText.setText(clientUser.getEmail());
                rePassText.setText(pasWord.getText());
                nameText.setText(clientUser.getName());
            }

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
                    System.out.println(client.networkaccess.testConnection());
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
//                            String result = client.register(usrName.getText().toLowerCase(), eMailText.getText(), pasWord.getText(), rePassText.getText(),nameText.getText());
//                            String result = client.sendVerificationCode(eMailText.getText()).message;
                            String result = client.validateUser(usrName.getText().toLowerCase(), eMailText.getText(), pasWord.getText(), rePassText.getText(),nameText.getText());
                            if (result.equals("success")) {
//                                updateData(new Login(true, "Account successfully created!"));
                                User usr = new User();
                                usr.setName(nameText.getText());
                                usr.setUsername(usrName.getText().toLowerCase());
                                usr.setPassword(pasWord.getText());
                                usr.setEmail(eMailText.getText());
                                clientUser = usr;
                                client.sendVerificationCode(usr.getEmail());
                                updateData(new VerifyEmail(usr));
                            } else {
                                status.setForeground(Color.RED);
                                status.setText(result);
                                status.setVisible(true);
                                repaint();
                            }
                        }

                    }
                    else{
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
                }


            });

            updateControl(cancel,submit);
        }
    }
    private class VerifyEmail extends displayPanel{
        private JTextField codeText;
        private JButton resendEmail;
        private JButton back;
        private JButton submit;
        private JLabel status;
        private User usr;

        //constructor
        VerifyEmail(User usr){
            this.setLayout(new GridBagLayout());
            this.setPanelName("Verify Email");
            this.setSpaces("                                                                                                              ");
            //Set the window title label
            windowTitle.setText(this.getLabel());
            //prepare components
            this.usr = usr;
            codeText = new JTextField(25);
            resendEmail = new JButton("Resend");
            back = new JButton("Back");
            submit = new JButton("Submit");
            codeText.setFont(timesRoman);
            resendEmail.setFont(timesRoman);
            back.setFont(timesRoman);
            submit.setFont(timesRoman);
            status = new JLabel("");
            resetStatus();
            prepareButtonHandlers();
            prepareKeyListener();
            //add components to window
            GridBagConstraints gbc = new GridBagConstraints();
            this.add(status,gbc);
            gbc.gridy = 1;
            this.add(codeText,gbc);
            gbc.gridy = 2;
            this.add(resendEmail,gbc);
            this.setVisible(true);
            repaint();
        }
        private void resetStatus(){
            status.setText("A verification code has been sent to " + usr.getEmail() + " please enter the code below.");
            status.setForeground(Color.BLACK);
            repaint();
        }
        private void error(String s){
            status.setText(s);
            status.setForeground(Color.RED);
            repaint();
        }

        private void prepareKeyListener() {
            codeText.addKeyListener(new KeyListener() {
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

        private void prepareButtonHandlers() {
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(client != null){
                        System.out.println(client.networkaccess.testConnection());
                        if(client.networkaccess.testConnection()){
                            updateData(new Register(1));
                        }
                        else{
                            System.out.println("Connection test failed");
                            updateData(new newConnection(true));
                        }
                    }
                    else{
                        System.out.println("client is null");
                        updateData(new newConnection(true));
                    }
//                    if(client != null && client.networkaccess.testConnection()){
//                        updateData(new Register());
//                    }
//                    else{
//                            updateData(new newConnection(true));
//                    }
                }
            });
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(client != null && client.networkaccess.testConnection()){
                        Message result = client.checkCode(codeText.getText());
                        if(result.message.equals("success")){
                            client.register();
                            updateData(new Login(true, "Account successfully created!"));
                        }
                        else{
                            error(result.message);
                        }
                    }
                    else{
                        updateData(new newConnection(true));
                    }
                }
            });
            resendEmail.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if(client != null && client.networkaccess.testConnection()){
                        client.sendVerificationCode(usr.getEmail());
                    }
                    else{
                        updateData(new newConnection(true));
                    }
                }
            });
            updateControl(back,submit);
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
        JTextArea wishlist;
        private ArrayList<String> myWishList = client.getWishList(clientUser);
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
            prepareButtonHandlers();
            //add components
            this.add(wishListSelect,BorderLayout.NORTH);
            this.add(wishListArea, BorderLayout.WEST);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridheight = 1;
            gbc.ipady = 10;
            gbc.fill = GridBagConstraints.NONE;
            dataArea.add(error, gbc);
            gbc.gridy = 1;
            dataArea.add(Box.createVerticalStrut(5),gbc);
            gbc.gridy = 2;
            dataArea.add(add, gbc);
            gbc.gridy = 3;
            dataArea.add(Box.createVerticalStrut(5),gbc);
            gbc.gridy = 4;
            dataArea.add(remove, gbc);
            gbc.gridy = 5;
            dataArea.add(Box.createVerticalStrut(5),gbc);
            gbc.gridy = 6;
            dataArea.add(clear, gbc);
            gbc.gridy = 7;
            dataArea.add(Box.createVerticalStrut(5),gbc);
            gbc.gridy = 8;
            dataArea.add(status,gbc);
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
//                            recipient.setVisible(false);
                            add.setVisible(true);
                            remove.setVisible(true);
                            clear.setVisible(true);
                            status.setVisible(true);
                            confirmWishlist.setVisible(true);
                            clientUser.setPassword("wishlist");
                            myWishList = client.getWishList(clientUser);
                            clientUser.setEntry("null");
                            System.out.println(clientUser);
                            updateWishList(myWishList,false);
                        }
                        else{
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
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
                                wishlist.setText(msg.message);
                                this.repaint();
                            }


                        }
                        else{
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
                    }



                });
                add.addActionListener(e -> {
                    System.out.println("Add item");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new AddItem());
                        }
                        else {
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
                    }
                });
                remove.addActionListener(e -> {
                    System.out.println("Remove Item");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new RemoveItem());
                        }
                        else {
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
                    }
                });
                clear.addActionListener(e -> {
                    System.out.println("Clear Wish List");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new ClearWishList());
                        }
                        else {
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
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
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
                    }

                });
                logOut.addActionListener(e -> {
                    System.out.println("Log Out");
                    System.out.println(clientUser.getUsername());
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            if(client.logout(clientUser)){
                                clientUser = new User(); //clearing the global User variable when the client logs out
                                updateData(new Login());
                            }
                            else {
                                System.out.println("Cannot Logout User");
                            }
                        }
                        else {
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
                    }
                });
                accountSettings.addActionListener(e -> {
                    System.out.println("Account Settings");
                    if(client != null){
                        if(client.networkaccess.testConnection()){
                            updateData(new AccountSettings());
                        }
                        else {
                            updateData(new SavedConnections(true,0));
                        }
                    }
                    else{
                        updateData(new SavedConnections(true,0));
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
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
                }
            });
            cancel.addActionListener(e -> {
                System.out.println("Cancel");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        updateData(new Interaction());
                    }
                    else {
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
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
            clientUser.setWishList(client.getWishList(clientUser));
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
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
                }
            });
            cancel.addActionListener(e -> {
                System.out.println("Cancel");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        updateData(new Interaction());
                    }
                    else {
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
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
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
                }
            });
            cancel.addActionListener(actionEvent -> {
                System.out.println("Cancel");
                if(client != null){
                    if(client.networkaccess.testConnection()){
                        updateData(new Interaction());
                    }
                    else {
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
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
            clientUser = client.getUser(clientUser);
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
            passText = new JTextField(clientUser.getPassword(), 25);
            emailText = new JTextField(clientUser.getEmail(), 25);
            nameText = new JTextField(clientUser.getName(), 25);
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
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
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
                            status.setForeground(Color.black);
                        }
                        else{
                            status.setText(result);
                            status.setForeground(Color.red);
                        }
                        status.setVisible(true);
                    }
                    else {
                        updateData(new SavedConnections(true,0));
                    }
                }
                else{
                    updateData(new SavedConnections(true,0));
                }
            });
            updateControl(cancel,apply);
        }
    }

    //method to shut off client properly
    private void shutDown(){
        if(Data instanceof Connect || Data instanceof ClientGUI.SavedConnections || Data instanceof EditConnection || Data instanceof newConnection || Data instanceof ConfirmDelete){
            System.out.println("connect window");
//            System.exit(0);
        }
        else if((Data instanceof Login)||(Data instanceof Register) || (Data instanceof Recover)||(Data instanceof VerifyEmail)){
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
        client.logout(clientUser);
        client.disconnect();
//        System.exit(0);
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}
