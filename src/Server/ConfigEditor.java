package Server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConfigEditor extends JFrame{
    private final int WIDTH = 680;
    private final int HEIGHT = 450;
    private JButton apply;
    private JButton cancel;
    private JLabel minUsernameLength;
    private JTextField minUsernameValue;
    private JLabel maxUsernameLength;
    private JTextField maxUsernameValue;
    private JLabel illegalUsernameCharacters;
    private JTextField illegalUsernameChars;
    private ArrayList illegalUsernameCharsList;
    private JLabel minPasswordLength;
    private JTextField minPasswordValue;
    private JLabel maxPasswordLength;
    private JTextField maxPasswordValue;
    private JLabel illegalPasswordCharacters;
    private JTextField illegalPasswordChars;
    private ArrayList illegalPasswordCharList;
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
    protected ConfigEditor() throws ConfigNotInitializedException {
        // -- size of the frame: width, height
        setSize(WIDTH, HEIGHT);

        // -- center the frame on the screen
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Config.initializeConfig("ServerConfiguration.conf");
        setTitle("Config Editor");
        setLayout(new FlowLayout(1,10,10));
        this.minPasswordLength = new JLabel("Minimum Password Length: ");
        this.minPasswordLength.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        this.minPasswordValue = new JTextField(String.valueOf(Config.getMinPasswordLength()), 25);
        this.add(minPasswordLength);
        this.add(minPasswordValue);
        this.setVisible(true);
        this.repaint();
    }

    public static void main(String[] args) {
        try {
            new ConfigEditor();
        } catch (ConfigNotInitializedException e) {
            e.printStackTrace();
        }
    }
}
