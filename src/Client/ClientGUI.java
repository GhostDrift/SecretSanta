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
        this.add(Data);
        this.remove(windowTitle);
        this.windowTitle = new Label(Data.getLabel());
    }

    private class Label extends JPanel{
        private JLabel title;

        Label(String text){
            setLayout(new FlowLayout(1, 10,10));
            Border labelBorder = BorderFactory.createLineBorder(Color.black);
            title = new JLabel(text);
            title.setBorder(labelBorder);
            title.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            this.add(title);
        }
        protected void setText(String newText){
            this.title.setText(newText);
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

            this.controlArea = getControl();
            controlArea.setLeft(Adv);
            controlArea.setRight(connect);

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
        private GridBagConstraints gbc = new GridBagConstraints();

         Login(){
            setLayout(new GridBagLayout());
             this.setPanelName("Login");
             this.setSpaces("                                                                                                              ");
             windowTitle = new Label(this.getLabel());

         }
    }
    private class ControlArea extends JPanel{
        private JButton left;
        private JButton right;
        private JButton center;
        protected ControlArea(){
            setLayout(new BorderLayout(5, 10));
        }
        protected void setLeft(JButton left){
            this.left = left;
            this.add(left, BorderLayout.WEST);
        }
        protected void setRight(JButton right){
            this.right = right;
            this.add(right,BorderLayout.EAST);
        }
        protected void setCenter(JButton center){
            this.center = center;
            this.add(center,BorderLayout.CENTER);
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
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}
