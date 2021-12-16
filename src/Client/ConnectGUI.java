package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ConnectGUI extends JFrame {

    private final int WIDTH = 740;
    private final int HEIGHT = 250;
    Client client = null;
    DataPanel Data;
    private boolean Visibility;
    BottomPanel Bot;



    public ConnectGUI() {
        setTitle("Client.Connect");

        // -- size of the frame: width, height
        setSize(WIDTH, HEIGHT);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(15, 5));
        Data = new DataPanel();
        //  x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));
        this.add(Data, BorderLayout.CENTER);

        Bot = new BottomPanel();
        this.add(Bot, BorderLayout.SOUTH);
        setVisible(true);
        Visibility = true;

    }
    private void SetVis(){
        setVisible(Visibility);
    }


    public class DataPanel extends JPanel {
        private final JLabel I;
        private final JTextField IP;
        private final JButton Adv;
        private final JLabel Portn;
        private final JTextField portnum;
        private boolean t = false;

        DataPanel() {
            setLayout(new FlowLayout(1, 1, 1));


            I = new JLabel("HostName");

            IP = new JTextField("", 25);

            Portn = new JLabel("Port Number");

            portnum = new JTextField("8000", 10);

            Adv = new JButton("Advanced...");

            this.add(I);
            this.add(IP);
            this.add(Portn);
            this.add(portnum);
            this.add(Adv);

            PrepareButtons();


        }
        public void PrepareButtons() {


            Adv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (t == false)
                    {
                        portnum.setText("8000");
                    }


                    Portn.setVisible(t);
                    portnum.setVisible(t);
                   t = !t;

                }



                });

        }


    }

    public class BottomPanel extends JPanel {
        private final JButton Connect;
        private final JButton Close;
        private LoginGUI Log;


        public BottomPanel() {


            Connect = new JButton("Client.Connect");
            Close = new JButton("Close");


            this.add(Connect);
            this.add(Close);

            PrepareButtons();
        }

        public void PrepareButtons() {
            //Action Handlers here

            Connect.addActionListener(new ActionListener() {
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
                            String host = Data.IP.getText();
                            int port = Integer.parseInt(Data.portnum.getText());
                            System.out.println(host);
                            System.out.println(port);
                            Matcher matcher = ippattern.matcher(host);
                            if (matcher.find()) {
                                matcher = portpattern.matcher(Data.portnum.getText());
                                if (matcher.find()) {
                                    Visibility = false;
                                    SetVis();
                                    client = new Client(host, port);
                                    Log = new LoginGUI();
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




        }
    }


    public static void main(String[] args) {
        new ConnectGUI();
    }
}


