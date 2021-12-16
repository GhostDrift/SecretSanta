package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoginGUI extends JFrame {
    private final int WIDTH = 750;
    private final int HEIGHT = 250;
    private boolean Visibility;
    Client client = null;

    public LoginGUI() {
        setTitle("Client.Login Screen");

        // -- size of the frame: width, height
        setSize(WIDTH, HEIGHT);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- set the layout manager and add items
        // 5, 5 is the border around the edges of the areas
        setLayout(new BorderLayout(5, 5));
        FieldPanel con = new FieldPanel();
        this.add(con, BorderLayout.CENTER);


        BottomPanel low = new BottomPanel();
        this.add(low, BorderLayout.SOUTH);
        this.setVisible(true);
        Visibility = true;

    }

    private void SetVis() {
        this.setVisible(Visibility);
    }

    public class FieldPanel extends JPanel {

        //private JButton loadButton;
        private final JLabel Us;
        private final JTextField User;
        private final JLabel pas;
        private final JTextField Pass;

        public FieldPanel() {
            setLayout(new FlowLayout(20, 20, 10));
            Us = new JLabel("Username");
            User = new JTextField("", 20);
            pas = new JLabel("Password");
            Pass = new JTextField("", 20);


            this.add(Us);
            this.add(User);
            this.add(pas);
            this.add(Pass);
        }


    }

    public class BottomPanel extends JPanel {
        private final JButton Close;
        private final JButton Log;
        private final JButton Forgot;
        private final JButton CreateA;
        private final JButton Disconnect;
        private CreateAccountGUI A;

        public BottomPanel() {

            Forgot = new JButton("Recover Password");
            Close = new JButton("Close");
            Log = new JButton("Client.Login");
            CreateA = new JButton("Create Account");
            Disconnect = new JButton("Disconnect");

            this.add(Forgot, BorderLayout.WEST, -1);
            this.add(Log);
            this.add(CreateA);
            this.add(Disconnect);
            this.add(Close);

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
            Forgot.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {

                }
            });

            // Log Button Action
            Log.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {

                }
            });

            // Help Button Action
            CreateA.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    Visibility = false;
                    SetVis();
                    A = new CreateAccountGUI();

                }
            });

            Disconnect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    client.disconnect();
                    client = null;
                    requestFocus();
                }
            });


        }


    }


    public static void main(String[] args) {
        new LoginGUI();
    }
}