package Client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.swing.*;
import javax.swing.border.Border;

public class ClientGUI extends JFrame {
    //Global variables
    private final int WIDTH = 725;
    private final int HEIGHT = 500;
    Client client = null;
    displayPanel Data;
    private Label windowTitle;
    private boolean Visibility;
    private ControlArea control;
//    Client.ConnectGUI.BottomPanel Bot;

    //main method
    public ClientGUI(){
        //set's the title of the application
        setTitle("Secret Santa Management System");
        //set the size of the window
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
        //create control pannel area
        control = new ControlArea();
        //set the data panel to display the connect panel.
        Data = new ClientGUI.Connect();

//          x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));
        //set the title pannel to display the Connect text
        this.windowTitle = new Label(Data.getLabel());
        this.add(windowTitle, BorderLayout.NORTH);
        this.add(Data, BorderLayout.CENTER);
        this.add(control,BorderLayout.SOUTH);

//        Bot = new Client.ConnectGUI.BottomPanel();
//        this.add(Bot, BorderLayout.SOUTH);
        setVisible(true);
        Visibility = true;
    }
    protected ControlArea getControl(){
        return this.control;
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

    private class Label extends JPanel{
        private JLabel title;
        private String text;

        Label(String text){
            setLayout(new FlowLayout(1, 10,10));
            Border labelBorder = BorderFactory.createLineBorder(Color.black);
            this.text = text;
            title = new JLabel(text);
            title.setBorder(labelBorder);
            title.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.add(title);
        }
        protected void setText(String newText){
            this.title.setText(newText);
            System.out.println("Label new text: " + newText);
        }
        protected String getText(){
            return this.text;
        }
    }
    private abstract class displayPanel extends JPanel{
        private  String panelName;
        private  String spaces;

        public void setPanelName(String panelName){
            this.panelName = panelName;
        }
        public void setSpaces(String spaces){
            this.spaces = spaces;
        }
        public String getLabel(){
            return spaces + panelName + spaces;
        }


    }
    private class Connect extends displayPanel{
        private final JLabel I;
        private final JTextField IP;
        private final JButton Adv;
        private final JLabel Portn;
        private final JTextField portnum;
        private JButton connect;
        private boolean t = false;
        private GridBagConstraints gbc = new GridBagConstraints();
        private ControlArea controlArea;
        private LoginGUI Log;

        Connect() {
            setLayout(new GridBagLayout());
//            setLayout(new BorderLayout(0,0));
            this.setPanelName("Connect");
            this.setSpaces("                                                                                                              ");

            I = new JLabel("HostName");
            I.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            IP = new JTextField("", 25);

            Portn = new JLabel("Port Number");
            Portn.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            portnum = new JTextField("8000", 25);

            Adv = new JButton("Advanced...");
            connect = new JButton("Connect");

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.fill = GridBagConstraints.NONE;
            this.add(I,gbc);
            gbc.gridx = 1;
            gbc.gridy = 0;
            this.add(IP,gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            Portn.setVisible(t);
            this.add(Portn,gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
//            gbc.fill = GridBagConstraints.HORIZONTAL;
            portnum.setVisible(false);
            this.add(portnum,gbc);
//            gbc.gridx = 0;
//            gbc.gridy = 3;
//            gbc.anchor = GridBagConstraints.LAST_LINE_START;
//            this.add(Adv);
//            gbc.gridx = 1;
//            gbc.gridy = 3;
//            gbc.anchor = GridBagConstraints.LAST_LINE_END;
//            this.add(connect);
            PrepareButtons();


        }
        protected void setPortVis(Boolean value){
            this.portnum.setVisible(value);
            this.Portn.setVisible(value);
        }
        public void PrepareButtons() {


            Adv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!t)
                    {
                        portnum.setText("8000");
                    }
                    t = !t;
                    Portn.setVisible(t);
                    portnum.setVisible(t);

                }



            });
            connect.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (client == null) {
                        try {
                            String ipformat = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
                            Pattern ippattern = Pattern.compile(ipformat);
                            String portformat = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
                            Pattern portpattern = Pattern.compile(portformat);
//                            String host = "127.0.0.1";
//                            int port = 8000;
                            String host = IP.getText();
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
//                            System.out.println(m);
                        }
                    }
                }

            });

            updateControl(Adv,connect);

        }
    }
    private class Login extends displayPanel{
        private JLabel username;
        private JLabel password;
        private JTextField usrName;
        private JTextField pasWord;
        private JButton disconnect;
        private JButton register;
        private JButton login;
        private JButton recover;
        private GridBagConstraints gbc = new GridBagConstraints();

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
             //create the JTextfields
             usrName = new JTextField("", 25);
             pasWord = new JTextField("", 25);
             //create the JButtons for the control panel
             disconnect = new JButton("Disconnect");
             register = new JButton("Register");
             login = new JButton("Login");
             recover = new JButton("Recover Password");
             //add components to the window
             gbc.gridx = 0;
             gbc.gridy = 0;
             gbc.ipadx = 10;
             gbc.ipady = 10;
             gbc.fill = GridBagConstraints.NONE;
             this.add(username,gbc);
             gbc.gridx = 1;
             this.add(usrName,gbc);
             gbc.gridx = 0;
             gbc.gridy = 1;
             this.add(password, gbc);
             gbc.gridx = 1;
             this.add(pasWord, gbc);

             prepareButtonHandlers();


         }

         private void prepareButtonHandlers(){
             disconnect.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     System.out.println("Disconnect");

                 }
             });
             register.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     System.out.println("Register");
                     updateData(new Register());

                 }
             });
             login.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     System.out.println("Login");

                 }
             });
             recover.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     System.out.println("Recover Password");

                 }
             });

             updateControl(disconnect,register,recover,login);
         }
    }

    private class Register extends displayPanel{
        private JLabel username;
        private JTextField usrName;
        private JLabel eMail;
        private JTextField eMailText;
        private JLabel password;
        private JTextField pasWord;
        private JLabel rePassword;
        private JTextField rePassText;
        private JButton cancel;
        private JButton submit;
        private GridBagConstraints gbc = new GridBagConstraints();

        Register(){
            this.setLayout(new GridBagLayout());
            this.setPanelName("Register");
            this.setSpaces("                                                                                                              ");
            //Set the window title label
            windowTitle.setText(this.getLabel());
            //prepare components
            username = new JLabel("Username");
            username.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            eMail = new JLabel("E-Mail");
            eMail.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            password = new JLabel("Password");
            password.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            rePassword = new JLabel("Re-enter Password");
            rePassword.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            usrName = new JTextField("",25);
            pasWord = new JTextField("", 25);
            eMailText = new JTextField("", 25);
            rePassText = new JTextField("", 25);
            //prepare buttons
            cancel = new JButton("Cancel");
            submit = new JButton("Submit");
            prepareButtonHandlers();
            //add components to the JPanel
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.fill = GridBagConstraints.NONE;
            this.add(username,gbc);
            gbc.gridx = 1;
            this.add(usrName,gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(eMail,gbc);
            gbc.gridx = 1;
            this.add(eMailText,gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(password,gbc);
            gbc.gridx = 1;
            this.add(pasWord,gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            this.add(rePassword,gbc);
            gbc.gridx = 1;
            this.add(rePassText,gbc);

        }
        private void prepareButtonHandlers(){
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("cancel");
                    updateData(new Login());

                }
            });
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("submit");
                    updateData(new Login());

                }
            });

            updateControl(cancel,submit);
        }
    }

    private class ControlArea extends JPanel{
        private JButton left;
        private JButton right;
        private JButton center;
        protected ControlArea(){
            setLayout(new BorderLayout(5, 10));
        }
        protected void setLeft(JButton newLeft){
            if(this.left != null) {
                this.remove(left);
            }
            this.left = newLeft;
            this.add(newLeft, BorderLayout.WEST);
            this.repaint();
        }
        protected void setRight(JButton newRight){
            if(this.right != null){
                this.remove(right);
            }
            this.right = newRight;
            this.add(newRight,BorderLayout.EAST);
            this.repaint();
        }
        protected void setCenter(JButton newCenter){
            if(this.center != null){
                this.remove(center);
            }
            this.center = newCenter;
            this.add(newCenter,BorderLayout.CENTER);
            this.repaint();
        }
        protected void delButton(int i){
            if(i == 0){
                this.remove(left);
            }
            else if(i == 1){
                this.remove(center);
            }
            else{
                this.remove(right);
            }
        }

        public void setCenter(JPanel center) {
            this.add(center, BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}
