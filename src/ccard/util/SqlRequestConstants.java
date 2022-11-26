package ccard.util;

public class SqlRequestConstants {
    final private static String CUSTOMER_TABLE = "CCCustomer";
    final private static String ACCOUNT_TABLE = "CCAccount";
    final private static String ENTRY_TABLE = "CCEntry";
    final private static String CREDITCARD_TABLE = "CCCreditCard";

    final public static String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  account_number varchar(45) DEFAULT NULL," +
            "  balance double DEFAULT NULL," +
            "  interest_rate double DEFAULT NULL," +
            "  customer_id int DEFAULT NULL," +
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
            "  birthdate date DEFAULT NULL," +
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
    final static public String CREATE_CREDITCARD_TABLE = "CREATE TABLE IF NOT EXISTS " + CREDITCARD_TABLE + " (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  expiration_date date NOT NULL," +
            "  last_month_balance double NOT NULL," +
            "  cctype varchar(45) NOT NULL," +
            "  account_id int NOT NULL," +
            "  ccard_number varchar(45) NOT NULL," +
            "  PRIMARY KEY (id)" +
            " )";

    final public static String PERSIST_PERSON = "INSERT INTO " + CUSTOMER_TABLE + "(id, name, email, street, city, state, zip, birthdate) " +
            "values(null, ?, ?, ?, ?, ?, ?, ?)";

    final public static String UPDATE_PERSON = "UPDATE " + CUSTOMER_TABLE + " SET name =?, email =?, birthdate =?, "
            +"street =?, city =?, state =?, zip =? WHERE id =?";

    final static public String SELECT_PERSON_BY_ID = "SELECT id, name, email, street, city, state, zip, birthdate FROM "
            + CUSTOMER_TABLE + " WHERE id = ";

    final public static  String SELECT_PERSON_BY_EMAIL = "SELECT id, name, email, street, city, state, zip, birthdate FROM "
            + CUSTOMER_TABLE + " WHERE email LIKE ?";

    final static public String ALL_PERSONS = "SELECT id, name, email, street, city, state, zip, birthdate FROM "
            + CUSTOMER_TABLE;

    final static public String DELETE_CUSTOMER = "DELETE FROM " + CUSTOMER_TABLE + " WHERE id = ";

    final static public String LAST_CUSTOMER_ID = "SELECT MAX(id) AS value FROM " + CUSTOMER_TABLE;


    final  public  static  String PERSIST_ACCOUNT_FOR_NEW_CUSTOMER = "INSERT INTO " + ACCOUNT_TABLE + "(id, account_number, balance, interest_rate, customer_id) "
            + "VALUES(null, ?, 0, ?, LAST_INSERT_ID())";

    final  public static String PERSIST_ACCOUNT_FOR_OLD_CUSTOMER = "INSERT INTO " + ACCOUNT_TABLE + "(id, account_number, balance, interest_rate, customer_id) "
            + "VALUES(null, ?, 0, ?, ?);";

    final  public static String UPDATE_ACCOUNT = "UPDATE " + ACCOUNT_TABLE +
            " SET account_number=?, balance=?, interest_rate=? WHERE id=?";

    final public static String LAST_ACCOUNT_ID = "SELECT max(id) as value from " + ACCOUNT_TABLE;

    final public static String ALL_ACCOUNTS = "SELECT *  FROM " + ACCOUNT_TABLE + " as acc " +
            " JOIN " + CUSTOMER_TABLE + " as cus ON acc.customer_id = cus.id";

    final public static String DELETE_ACCOUNT = "DELETE FROM " + ACCOUNT_TABLE + " WHERE id = ";

    final static public String SELECT_ACCOUNT_BY_ID = "SELECT *  FROM " + ACCOUNT_TABLE + " as acc " +
            " JOIN " + CUSTOMER_TABLE + " as cus ON acc.customer_id = cus.id WHERE acc.id = ";

    final static public String SELECT_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT *  FROM " + ACCOUNT_TABLE + " as acc " +
            " JOIN " + CUSTOMER_TABLE + " as cus ON acc.customer_id = cus.id WHERE acc.account_number LIKE ? ";

    final static public String SELECT_ACCOUNT_BY_CUSTOMER = "SELECT *  FROM " + ACCOUNT_TABLE +
            " WHERE customer_id = ? ";

    final static public String UPDATE_BALANCE = "UPDATE " + ACCOUNT_TABLE + " SET balance = ? WHERE id = ?";


    final  public  static  String PERSIST_CREDITCARD_FOR_NEW_CUSTOMER = "INSERT INTO " + CREDITCARD_TABLE + "(id, ccard_number, expiration_date, last_month_balance, cctype, account_id) "
            + "VALUES(null, ?, ?, 0, ?, LAST_INSERT_ID())";

    final  public static String PERSIST_CREDITCARD_FOR_OLD_CUSTOMER = "INSERT INTO " + CREDITCARD_TABLE + "(id, ccard_number, expiration_date, last_month_balance, cctype, account_id) "
            + "VALUES(null, ?, ?, ?, ?, ?);";

    final  public static String UPDATE_CREDITCARD = "UPDATE " + CREDITCARD_TABLE +
            " SET expiration_date=?, last_month_balance=?, cctype=? WHERE id=?";

    final public static String LAST_CREDITCARD_ID = "SELECT max(id) as value from " + CREDITCARD_TABLE;

    final public static String ALL_CREDITCARDS = "SELECT *  FROM " + CREDITCARD_TABLE + " as cc " +
            " JOIN " + ACCOUNT_TABLE + " as acc ON cc.account_id = cc.id" +
            " JOIN " + CUSTOMER_TABLE + " as cus ON acc.customer_id = cus.id" +
            " WHERE expiration_date NOT BETWEEN '1930-01-01' AND CURRENT_DATE()";

    final public static String DELETE_CREDITCARD = "DELETE FROM " + CREDITCARD_TABLE + " WHERE id = ";

    final static public String SELECT_CREDITCARD_BY_ID = "SELECT *  FROM " + CREDITCARD_TABLE + " as cc " +
            " JOIN " + ACCOUNT_TABLE + " as acc ON cc.account_id = cc.id" +
            " JOIN " + CUSTOMER_TABLE + " as cus ON acc.customer_id = cus.id WHERE acc.id = ";

    final static public String SELECT_CREDITCARD_BY_ACCOUNT_NUMBER = "SELECT *  FROM " + CREDITCARD_TABLE + " as cc " +
            " JOIN " + ACCOUNT_TABLE + " as acc ON cc.account_id = cc.id" +
            " JOIN " + CUSTOMER_TABLE + " as cus ON acc.customer_id = cus.id " +
            " WHERE acc.account_number LIKE ? AND expiration_date NOT BETWEEN '1930-01-01' AND CURRENT_DATE()";


    final static public String PERSIST_OPERATION = "INSERT INTO " + ENTRY_TABLE + "(id, amount, date, entry_type, account_id) "
            + "VALUES(null, ?, ?, ?, ?)";
    final static public String ALL_OPERATIONS = "SELECT * FROM " + ENTRY_TABLE;
    final static public String ALL_OPERATIONS_BY_ACCOUNT = "SELECT * FROM " + ENTRY_TABLE +
            " WHERE account_id = ";
}
