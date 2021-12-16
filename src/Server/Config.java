package Server;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Config implements Serializable {
    private static Config config;
    private static String configFilePath;

    // Username information
    private int minUsernameLength;
    private int maxUsernameLength;
    private char[] illegalUsernameCharacters;

    // Password information
    private int minPasswordLength;
    private int maxPasswordLength;
    private String illegalPasswordCharacters;
    private boolean[] requiredCharacterSets; // lowercaseLetters, uppercaseLetters, numbers, symbols
    private boolean enforcePasswordHistory;

    // Email information
    private String validEmailFormat;

    private String emailUsername;
    private String emailPassword;

    // Database information
    private String userDatabaseServerAddress;
    private String systemDatabaseServerAddress;
    private String databaseUsername;
    private String databasePassword;

    // Other information
    private int lockoutThreshold;

    private Config() {
    }

    public static void printConfig() {
        if (config == null) {
            System.out.println("Config has not been initialized.");
        } else {
            System.out.println("Global values:");
            System.out.println("configFilePath = " + configFilePath);
            System.out.println();
            System.out.println("Username Information:");
            System.out.println("minUsernameLength = " + config.minUsernameLength);
            System.out.println("maxUsernameLength = " + config.maxUsernameLength);
            System.out.println("illegalUsernameCharacters = " + Arrays.toString(config.illegalUsernameCharacters));
            System.out.println();
            System.out.println("Password Information:");
            System.out.println("minPasswordLength = " + config.minPasswordLength);
            System.out.println("maxPasswordLength = " + config.maxPasswordLength);
            System.out.println("illegalPasswordCharacters = " + config.illegalPasswordCharacters);
            System.out.println("requiredCharacterSets = " + Arrays.toString(config.requiredCharacterSets));
            System.out.println("enforcePasswordHistory = " + config.enforcePasswordHistory);
            System.out.println();
            System.out.println("Email Information:");
            System.out.println("validEmailFormat = " + config.validEmailFormat);

            System.out.println("emailFromUsername = " + config.emailUsername);
            System.out.println("emailFromPassword = " + config.emailPassword);
            System.out.println();
            System.out.println("Database Information:");
            System.out.println("databaseFilePath = " + config.userDatabaseServerAddress);
            System.out.println("databaseFilePath = " + config.systemDatabaseServerAddress);
            System.out.println("databaseUsername = " + config.databaseUsername);
            System.out.println("databasePassword = " + config.databasePassword);
            System.out.println();
            System.out.println("Other Information:");
            System.out.println("lockoutThreshold = " + config.lockoutThreshold);
        }
    }

    public static void initializeConfig() {
        if (config == null)
            config = new Config();
    }

    public static void initializeConfig(String filePath) {

        if (config == null) {
            config = new Config();
            setConfigFilePath(filePath);
            loadConfig();
        }

    }

    public static void setConfigFilePath(String filePath) {
        configFilePath = filePath;
    }

    public static void assignDefaultValues() {
        config.minUsernameLength = 1;
        config.maxUsernameLength = 64;
        config.illegalUsernameCharacters = new char[0];

        config.minPasswordLength = 1;
        config.maxPasswordLength = 64;
        config.illegalPasswordCharacters = "";
        config.requiredCharacterSets = new boolean[]{true, true, true, false};
        config.enforcePasswordHistory = false;

        config.validEmailFormat = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        config.emailUsername = null;
        config.emailPassword = null;

        config.userDatabaseServerAddress = null;
        config.systemDatabaseServerAddress = null;

        config.databaseUsername = null;
        config.databasePassword = null;

        config.lockoutThreshold = 3;
    }

    // Read in config file
    public static void loadConfig() {
        try {
            FileInputStream fileIn = new FileInputStream(configFilePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            config = (Config) in.readObject();
            in.close();
            fileIn.close();
        }
        //I added this catch block so that if there isn't a config file of the name provided
        // it creates one and gives it the default values - Jessica
        catch (FileNotFoundException e) {
            assignDefaultValues();
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    // Write out config file
    public static void saveConfig() {
        try {
            FileOutputStream fileOut = new FileOutputStream(configFilePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(config);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int getMinUsernameLength() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.minUsernameLength;
    }

    public static void setMinUsernameLength(int length) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (length < 1)
            throw new InvalidAttributeValueException("Min length must be greater than 0.");
        config.minUsernameLength = length;
    }

    public static int getMaxUsernameLength() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.maxUsernameLength;
    }

    public static void setMaxUsernameLength(int length) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (length < config.minUsernameLength)
            throw new InvalidAttributeValueException("Max length must be greater than min length.");
        config.maxUsernameLength = length;
    }

    public static char[] getIllegalUsernameCharacters() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.illegalUsernameCharacters;
    }

    public static void setIllegalUsernameCharacters(char[] characters) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (characters == null)
            throw new InvalidAttributeValueException("Character array cannot be null");
        config.illegalUsernameCharacters = characters;
    }

    public static int getMinPasswordLength() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.minPasswordLength;
    }

    public static void setMinPasswordLength(int length) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (length < 1)
            throw new InvalidAttributeValueException("Min length must be greater than 0.");
        config.minPasswordLength = length;
    }

    public static int getMaxPasswordLength() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.maxPasswordLength;
    }

    public static void setMaxPasswordLength(int length) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (length < config.minPasswordLength)
            throw new InvalidAttributeValueException("Max length must be greater than min length.");
        config.maxPasswordLength = length;
    }

    public static String getIllegalPasswordCharacters() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.illegalPasswordCharacters;
    }

    public static void setIllegalPasswordCharacters(String characters) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (characters == null)
            throw new InvalidAttributeValueException("Characters cannot be null");
        config.illegalPasswordCharacters = characters;
    }

    public static boolean[] getRequiredCharacterSets() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.requiredCharacterSets;
    }

    public static void setRequiredCharacterSets(boolean[] characterSets) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (characterSets == null)
            throw new InvalidAttributeValueException("Character array cannot be null");
        if (characterSets.length != 4)
            throw new InvalidAttributeValueException("Character sets array must be length 4.");
        config.requiredCharacterSets = characterSets;
    }

    public static boolean getEnforcePasswordHistory() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.enforcePasswordHistory;
    }

    public static void setRequiredCharacterSets(boolean enforceHistory) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        config.enforcePasswordHistory = enforceHistory;
    }

    public static String getValidEmailFormat() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.validEmailFormat;
    }

    public static void setValidEmailFormat(String format) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (format == null)
            throw new InvalidAttributeValueException("Email format cannot be null");
        config.validEmailFormat = format;
    }

    public static String getEmailUsername() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.emailUsername;
    }

    public static void setEmailUsername(String email) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (email == null)
            throw new InvalidAttributeValueException("Email cannot be null");
        config.emailUsername = email;
    }

    public static String getEmailPassword() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.emailPassword;
    }

    public static void setEmailPassword(String password) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (password == null)
            throw new InvalidAttributeValueException("Password cannot be null");
        config.emailPassword = password;
    }

    public static String getUserDatabaseServerAddress() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.userDatabaseServerAddress;
    }

    public static void setUserDatabaseServerAddress(String filePath) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (filePath == null)
            throw new InvalidAttributeValueException("Database file path cannot be null");
        config.userDatabaseServerAddress = filePath;
    }

    public static String getSystemDatabaseServerAddress() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.systemDatabaseServerAddress;
    }

    public static void setSystemDatabaseServerAddress(String filePath) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (filePath == null)
            throw new InvalidAttributeValueException("Database file path cannot be null");
        config.systemDatabaseServerAddress = filePath;
    }

    public static String getDatabaseUsername() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        return config.databaseUsername;
    }

    public static void setDatabaseUsername(String username) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (username == null)
            throw new InvalidAttributeValueException("The database file path cannot be null");
        config.databaseUsername = username;
    }

    public static String getDatabasePassword() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The database username cannot be null.");
        return config.databasePassword;
    }

    public static void setDatabasePassword(String password) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (password == null)
            throw new InvalidAttributeValueException("The database password cannot be null");
        config.databasePassword = password;
    }

    public static int getLockoutThreshold() throws ConfigNotInitializedException {
        if (config == null)
            throw new ConfigNotInitializedException("The database username cannot be null.");
        return config.lockoutThreshold;
    }

    public static void setLockoutThreshold(int threshold) throws ConfigNotInitializedException, InvalidAttributeValueException {
        if (config == null)
            throw new ConfigNotInitializedException("The config file has not been initialized.");
        if (threshold < 1)
            throw new InvalidAttributeValueException("The lockout threshold must be greater than 0.");
        config.lockoutThreshold = threshold;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean isValidPassword(String password) {
        String passwordregex = "^";
        if (config.requiredCharacterSets[0])
            passwordregex += "(?=.*[a-z])";
        if (config.requiredCharacterSets[1])
            passwordregex += "(?=.*[A-Z])";
        if (config.requiredCharacterSets[2])
            passwordregex += "(?=.*[0-9])";
        if (config.requiredCharacterSets[3])
            passwordregex += "(?=.*[@#$%^&+=])";
        if (config.illegalPasswordCharacters.length() > 0)
            passwordregex += "(?=^[^" + config.illegalPasswordCharacters + "]+$)";
        passwordregex += "(?=\\S+$)";
        passwordregex += ".{" + config.minPasswordLength + "," + config.maxPasswordLength + "}";
        passwordregex += "$";

        Pattern passwordpattern = Pattern.compile(passwordregex);
        Matcher matcher = passwordpattern.matcher(password);
        return matcher.find();
    }

    public static boolean isValidEmail(String emailAddress) {
        String emailregex = config.validEmailFormat;
        Pattern emailpattern = Pattern.compile(emailregex);
        Matcher matcher = emailpattern.matcher(emailAddress);
        return matcher.find();
    }
}

class InvalidAttributeValueException extends Exception {
    public InvalidAttributeValueException(String errorMessage) {
        super(errorMessage);
    }
}

class ConfigNotInitializedException extends Exception {
    public ConfigNotInitializedException(String errorMessage) {
        super(errorMessage);
    }
}
