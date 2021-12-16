package Server;

import java.awt.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
//import Server.java;


public class ServerGUI extends JFrame {

    private final int WIDTH = 680;
    private final int HEIGHT = 450;
    private final ServerGUI owner = null;

    private Server server;
    private final BottomPanel low;
    private final FieldPanel con;


    public ServerGUI() {
        setTitle("Server");

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
        JButton Act = new JButton("Activate Surver");
        //  JButton DeAct = new JButton("Deactivate Server");
        JButton Conf = new JButton("Edit Config");
        JButton AConnect = new JButton("Number of Active Connections");


        // MenBar.add(Menu1);
        MenBar.add(Act);
        // MenBar.add(DeAct);
        MenBar.add(Conf);
        MenBar.add(AConnect);


        // Activate Server
        Act.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                server = new Server(owner);
                server.start();
                //server.stop();
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

        setVisible(true);

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


