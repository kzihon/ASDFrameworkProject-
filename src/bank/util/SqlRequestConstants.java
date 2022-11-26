package bank.util;

public class SqlRequestConstants {
    final private static String CUSTOMER_TABLE = "Customer";
    final private static String ACCOUNT_TABLE = "Account";
    final private static String ENTRY_TABLE = "Entry";

    final public static String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  account_number varchar(45) DEFAULT NULL," +
            "  balance double DEFAULT NULL," +
            "  interest_rate double DEFAULT NULL," +
            "  customer_id int DEFAULT NULL," +
            "  account_type varchar(45) DEFAULT NULL," +
            "  PRIMARY KEY (id)" +
            ")";
    final public static String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE + " (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  name varchar(50) NOT NULL," +
            "  email varchar(100) NOT NULL," +
            "  street varchar(50) NOT NULL," +
            "  city varchar(45) NOT NULL," +
            "  state varchar(20) NOT NULL," +
            "  zip varchar(45) NOT NULL," +
            "  number_of_employees int DEFAULT NULL," +
            "  birthdate date DEFAULT NULL," +
            "  customer_type varchar(45) NOT NULL," +
            "  PRIMARY KEY (id)" +
            ")";
    final static public String CREATE_ENTRY_TABLE = "CREATE TABLE IF NOT EXISTS " + ENTRY_TABLE + " (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  amount double NOT NULL," +
            "  date date NOT NULL," +
            "  entry_type varchar(45) NOT NULL," +
            "  account_id int NOT NULL," +
            "  PRIMARY KEY (id)" +
            ")";

    final public static String PERSIST_PERSON = "INSERT INTO " + CUSTOMER_TABLE + "(id, name, email, street, city, state, zip, birthdate, customer_type) " +
            "values(null, ?, ?, ?, ?, ?, ?, ?, 'PERSON')";

    final public static String UPDATE_PERSON = "UPDATE " + CUSTOMER_TABLE + " SET name =?, email =?, birthdate =?, "
            +"street =?, city =?, state =?, zip =? "
            + "WHERE customer_type LIKE 'PERSON' AND id =?";

    final static public String SELECT_PERSON_BY_ID = "SELECT id, name, email, street, city, state, zip, birthdate FROM " + CUSTOMER_TABLE
            + " WHERE customer_type LIKE 'PERSON' AND id = ";

    final public static  String SELECT_PERSON_BY_EMAIL = "SELECT id, name, email, street, city, state, zip, birthdate FROM " + CUSTOMER_TABLE +
            " WHERE customer_type LIKE 'PERSON' AND email LIKE ?";

    final static public String ALL_PERSONS = "SELECT id, name, email, street, city, state, zip, birthdate FROM " + CUSTOMER_TABLE +
            " WHERE customer_type LIKE 'PERSON'";

    public static String PERSIST_COMPANY = "INSERT INTO " + CUSTOMER_TABLE + "(id, name, email, street, city, state, zip, number_of_employees, customer_type) " +
            "VALUES(null, ?, ?, ?, ?, ?, ?, ?, 'COMPANY')";

    final public static String UPDATE_COMPANY = "UPDATE " + CUSTOMER_TABLE + " SET name =?, email =?, number_of_employees =?, "
            +"street =?, city =?, state =?, zip =? "
            + "WHERE customer_type LIKE 'COMPANY' AND id =?";

    final public static  String SELECT_COMPANY_BY_EMAIL = "SELECT id, name, email, street, city, state, zip, number_of_employees FROM " + CUSTOMER_TABLE +
            " WHERE customer_type LIKE 'COMPANY' AND email LIKE ?";

    final static public String SELECT_COMPANY_BY_ID = "SELECT id, name, email, street, city, state, zip, number_of_employees FROM " + CUSTOMER_TABLE +
            " WHERE customer_type LIKE 'COMPANY' AND id = ";

    final static public String ALL_COMPANIES = "SELECT id, name, email, street, city, state, zip, number_of_employees FROM " + CUSTOMER_TABLE +
            " WHERE customer_type LIKE 'COMPANY'";

    final static public String DELETE_CUSTOMER = "DELETE FROM " + CUSTOMER_TABLE + " WHERE id = ";

    final static public String LAST_CUSTOMER_ID = "SELECT MAX(id) AS value FROM " + CUSTOMER_TABLE;


    final  public  static  String PERSIST_ACCOUNT_FOR_NEW_CUSTOMER = "INSERT INTO " + ACCOUNT_TABLE + "(id, account_number, balance, interest_rate, customer_id, account_type) "
            + "VALUES(null, ?, 0, ?, LAST_INSERT_ID(), ?)";

    final  public static String PERSIST_ACCOUNT_FOR_OLD_CUSTOMER = "INSERT INTO " + ACCOUNT_TABLE + "(id, account_number, balance, interest_rate, customer_id, account_type) "
            + "VALUES(null, ?, 0, ?, ?, ?);";

    final  public static String UPDATE_ACCOUNT = "UPDATE " + ACCOUNT_TABLE +
            " SET account_number=?, balance=?, interest_rate=?" +
            " WHERE id=? AND account_type LIKE ?";

    final public static String LAST_ACCOUNT_ID = "SELECT max(id) as value from " + ACCOUNT_TABLE;
    final public static String ALL_ACCOUNTS = "SELECT *  FROM " + ACCOUNT_TABLE + " as acc " +
            " JOIN Customer as cus ON acc.customer_id = cus.id"
            + " WHERE acc.account_type LIKE ";
    final public static String DELETE_ACCOUNT = "DELETE FROM " + ACCOUNT_TABLE + " WHERE id = ";
    final static public String SELECT_ACCOUNT_BY_ID = "SELECT *  FROM " + ACCOUNT_TABLE + " as acc " +
            " JOIN Customer as cus ON acc.customer_id = cus.id"
            + " WHERE acc.id = ";
    final static public String SELECT_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT *  FROM " + ACCOUNT_TABLE + " as acc " +
            " JOIN Customer as cus ON acc.customer_id = cus.id"
            + " WHERE acc.account_number LIKE ? AND acc.account_type LIKE ? ";

    final static public String UPDATE_BALANCE = "UPDATE " + ACCOUNT_TABLE + " SET balance = ? WHERE id = ?";

    final static public String PERSIST_OPERATION = "INSERT INTO " + ENTRY_TABLE + "(id, amount, date, entry_type, account_id) "
            + "VALUES(null, ?, ?, ?, ?)";
    final static public String ALL_OPERATIONS = "SELECT * FROM " + ENTRY_TABLE;
    final static public String ALL_OPERATIONS_BY_ACCOUNT = "SELECT * FROM " + ENTRY_TABLE +
            " WHERE account_id = ";
}
