package Server;
//This class is used to populate the config file for the first time with the proper information
public class ConfigPopulator {
    public static void main(String[] args) throws ConfigNotInitializedException, InvalidAttributeValueException {
        Config.initializeConfig("ServerConfiguration");
        Config.setEmailUsername("YourEmailHere");
        Config.setEmailPassword("YourPasswordHere");
        Config.setUserDatabaseServerAddress("jdbc:mysql://localhost:3306/userdb");
        Config.setDatabaseUsername("ClientServerProject");
        Config.setDatabasePassword("");
        Config.saveConfig();
    }
}
