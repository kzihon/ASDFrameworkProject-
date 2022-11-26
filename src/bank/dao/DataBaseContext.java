package bank.dao;

import bank.resources.JdbcConfigReader;
import bank.resources.JdbcConnect;

import javax.xml.parsers.ParserConfigurationException;
import java.sql.*;
import java.util.Map;

public class DataBaseContext {
    static final private String JDBC_PROPERTIES_FILE = System.getProperty("user.dir")
            + "/src/bank/resources/JdbcConfig.xml";
    final private Connection conn;

    public DataBaseContext() {
        try {
            this.conn = connect();
        } catch (ParserConfigurationException e) {
            System.out.println("Error during database access initialization in " + getClass().getSimpleName());
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection connect() throws ParserConfigurationException, SQLException {
        Map<String,String> config = JdbcConfigReader.readConfig(JDBC_PROPERTIES_FILE);
        return new JdbcConnect(config.get("driverName"), config.get("userName"), config.get("password"),
                config.get("host"), config.get("port") ,
                config.get("dbName")).initJdbc();
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
