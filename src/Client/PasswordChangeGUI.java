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

public class PasswordChangeGUI extends JFrame {


    private InteractiveGUI y;

    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private boolean Visibility;

    public PasswordChangeGUI() {
        setTitle("Password Change");

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
        setVisible(true);
        Visibility = true;

    }
    private void SetVis(){
        setVisible(Visibility);
    }

    public class FieldPanel extends JPanel {

        //private JButton loadButton;
        private final JLabel Us;
        private final JTextField User;
        private final JLabel pas;
        private final JTextField Pass;

        public FieldPanel() {
            setLayout(new FlowLayout(20, 20, 10));


            Us = new JLabel("Password");
            User = new JTextField("", 10);
            pas = new JLabel("Re-Enter Password");
            Pass = new JTextField("", 10);


            this.add(Us);
            this.add(User);
            this.add(pas);
            this.add(Pass);
        }


    }

    public class BottomPanel extends JPanel {
        private final JButton Close;
        private final JButton Done;


        public BottomPanel() {


            Close = new JButton("Close");
            Done = new JButton("Change");


            this.add(Close);
            this.add(Done);

            PrepareButtons();
        }

        public void PrepareButtons() {

            // Close Button Action
            Close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                   Visibility = false;
                    SetVis();
                    y = new InteractiveGUI();
                }
            });

            // Forgot Button Action
            Done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {

                }
            });
        }


    }


    public static void main(String[] args) {
        new PasswordChangeGUI();
    }
}

