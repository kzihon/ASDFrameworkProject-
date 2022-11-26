package ccard.dao;

import ccard.util.SqlRequestConstants;

import java.sql.*;

public class DataBaseAccess {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASS = "";

    static private boolean isInit = false;

    private void init() {
        if(DataBaseAccess.isInit) return;
        try {
            Statement statement = this.conn.createStatement();

            String sql = SqlRequestConstants.CREATE_ACCOUNT_TABLE;
            statement.executeUpdate(sql);

            sql = SqlRequestConstants.CREATE_CUSTOMER_TABLE;
            statement.executeUpdate(sql);

            sql = SqlRequestConstants.CREATE_ENTRY_TABLE;
            statement.executeUpdate(sql);

            sql = SqlRequestConstants.CREATE_CREDITCARD_TABLE;
            statement.executeUpdate(sql);

            statement.close();
            DataBaseAccess.isInit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static final private String JDBC_PROPERTIES_FILE = System.getProperty("user.dir")
            + "/src/bank/resources/JdbcConfig.xml";
    final private Connection conn;

    public DataBaseAccess() {
        this.conn = connect();
        this.init();
    }

    private static Connection connect() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void execute(String sql) throws SQLException {
        try {
            this.conn.setAutoCommit(false);
            Statement statement = this.conn.createStatement();
            statement.execute(sql);
            this.conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.conn.rollback();
        }
    }

    public void execute(String ... sqls) throws SQLException {
        try {
            this.conn.setAutoCommit(false);
            Statement statement = this.conn.createStatement();
            for (String sql: sqls) {
                statement.addBatch(sql);
            }
            statement.executeBatch();
            this.conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            this.conn.rollback();
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            Statement statement = this.conn.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
