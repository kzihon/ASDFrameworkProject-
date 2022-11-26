package ccard.dao;


import ccard.model.*;
import ccard.util.SqlRequestConstants;
import framework.dao.DAO;
import framework.interfaces.ICustomer;
import framework.interfaces.IEntry;
import framework.model.Customer;
import framework.model.IModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CCAccountDAO implements DAO {
    private static final String CHECKINGS = "CHECKINGS";
    private static final String PERSON = "PERSON";
    final static private String WITHDRAW = "WITHDRAW";
    final static private String DEPOSIT = "DEPOSIT";
    private DataBaseAccess dba;

    public CCAccountDAO() {
        this.dba = new DataBaseAccess();
    }
    @Override
    public void persist(IModel model) {
        Connection connection = this.dba.getConnection();
        try {
            connection.setAutoCommit(false);
            CCAccount account = (CCAccount)model;

            Person person = (Person) account.getCustomer();
            if(person.getId() == 0) {
                PreparedStatement customerStatement = connection.prepareStatement(SqlRequestConstants.PERSIST_PERSON);
                customerStatement.setString(1, person.getName());
                customerStatement.setString(2, person.getEmail());
                customerStatement.setString(3, person.getAddress().getStreet());
                customerStatement.setString(4, person.getAddress().getCity());
                customerStatement.setString(5, person.getAddress().getState());
                customerStatement.setString(6, person.getAddress().getZip());
                customerStatement.setString(7, person.getBirthdate().format(DateTimeFormatter.ISO_DATE));
                customerStatement.executeUpdate();

                PreparedStatement accountStatement = connection.prepareStatement(SqlRequestConstants.PERSIST_ACCOUNT_FOR_NEW_CUSTOMER);
                accountStatement.setString(1, account.getAccountNumber());
                accountStatement.setDouble(2, account.getInterestRate());
                accountStatement.executeUpdate();

                connection.commit();
                account.setId(this.getLastId());
                ((Person) account.getCustomer()).setId(new PersonDAO().getLastId());
            } else {
                PreparedStatement accountStatement = connection.prepareStatement(SqlRequestConstants.PERSIST_ACCOUNT_FOR_OLD_CUSTOMER);
                accountStatement.setString(1, account.getAccountNumber());
                accountStatement.setDouble(2, account.getInterestRate());
                accountStatement.setInt(3, person.getId());
                accountStatement.execute();

                connection.commit();
                account.setId(this.getLastId());
            }
        } catch (Exception e) {
            try {
                if(connection != null) connection.rollback();
            } catch (Exception exception) {}

            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IModel model) {
        try {
            CCAccount account = (CCAccount) model;

            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.UPDATE_ACCOUNT);
            statement.setString(1, account.getAccountNumber());
            statement.setDouble(2, account.getBalance());
            statement.setDouble(3, account.getInterestRate());
            statement.setInt(4, account.getId());

            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IModel getById(int id) {
        try {
            String sql = SqlRequestConstants.SELECT_ACCOUNT_BY_ID + id ;

            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory factory = new SimpleAccountFactory();

            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                int accId = rs.getInt(1);
                String accNum = rs.getString(2);
                double accBalance = rs.getDouble(3);
                double accIntRate = rs.getDouble(4);
                int custID = rs.getInt(5);

                String name = rs.getString(8);
                String email = rs.getString(9);
                String street = rs.getString(10);
                String city = rs.getString(11);
                String state = rs.getString(12);
                String zip = rs.getString(13);
                int numberOfEmployees = rs.getInt(14);
                String cusBirth = rs.getString(15);
                String cusType = rs.getString(16);

                LocalDate birthdate = LocalDate.parse(cusBirth, DateTimeFormatter.ISO_DATE);
                Person customer = (Person) customerFactory.createPerson(name, birthdate, street, city, state, zip, email);
                customer.setId(custID);

                CCAccount account = (CCAccount) factory.createAccount(customer, accNum);
                account.setId(accId);
                account.setInterestRate(accIntRate);
                account.setBalance(accBalance);

                sql = SqlRequestConstants.ALL_OPERATIONS_BY_ACCOUNT + account.getId();
                ResultSet opRs = dba.executeQuery(sql);
                while (opRs.next()) {
                    double opAmount = opRs.getDouble(2);
                    LocalDate opDate = LocalDate.parse(opRs.getString(3));
                    String opType = opRs.getString(4);

                    IEntry operation;
                    if(opType.equals(DEPOSIT)) {
                        operation = new DepositOperation(opAmount, opDate);
                    } else if (opType.equals(WITHDRAW)) {
                        operation = new WithdrawOperation(opAmount, opDate);
                    } else {
                        operation = new InterestOperation(opAmount, opDate);
                    }
                    account.addOperation(operation);
                }
                return account;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IModel> getAll() {
        try {
            List<IModel> data = new ArrayList<>();
            String sql = SqlRequestConstants.ALL_ACCOUNTS;

            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory factory = new SimpleAccountFactory();

            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                int accId = rs.getInt(1);
                String accNum = rs.getString(2);
                double accBalance = rs.getDouble(3);
                double accIntRate = rs.getDouble(4);
                int custID = rs.getInt(5);

                String name = rs.getString(8);
                String email = rs.getString(9);
                String street = rs.getString(10);
                String city = rs.getString(11);
                String state = rs.getString(12);
                String zip = rs.getString(13);
                int numberOfEmployees = rs.getInt(14);
                String cusBirth = rs.getString(15);
                String cusType = rs.getString(16);

                LocalDate birthdate = LocalDate.parse(cusBirth, DateTimeFormatter.ISO_DATE);
                Person customer = (Person) customerFactory.createPerson(name, birthdate, street, city, state, zip, email);
                customer.setId(custID);

                CCAccount account = (CCAccount) factory.createAccount(customer, accNum);
                account.setId(accId);
                account.setInterestRate(accIntRate);
                account.setBalance(accBalance);
                data.add(account);
            }
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLastId() {
        try {
            String sql = SqlRequestConstants.LAST_ACCOUNT_ID;
            ResultSet rs = this.dba.executeQuery(sql);
            while(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void delete(IModel model) {
        try {
            CCAccount account = (CCAccount) model;
            String sql = SqlRequestConstants.DELETE_ACCOUNT + account.getId() ;
            this.dba.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public IModel getAccountByAccountNumber(String accountNumber) {
        try {
            accountNumber = "%" + accountNumber + "%";
            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory factory = new SimpleAccountFactory();

            DataBaseAccess db = new DataBaseAccess();
            Connection connection = db.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.SELECT_ACCOUNT_BY_ACCOUNT_NUMBER);
            statement.setString(1, accountNumber);

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                int accId = rs.getInt(1);
                String accNum = rs.getString(2);
                double accBalance = rs.getDouble(3);
                double accIntRate = rs.getDouble(4);
                int custID = rs.getInt(5);

                String name = rs.getString(8);
                String email = rs.getString(9);
                String street = rs.getString(10);
                String city = rs.getString(11);
                String state = rs.getString(12);
                String zip = rs.getString(13);
                int numberOfEmployees = rs.getInt(14);
                String cusBirth = rs.getString(15);
                String cusType = rs.getString(16);

                LocalDate birthdate = LocalDate.parse(cusBirth, DateTimeFormatter.ISO_DATE);
                Person customer = (Person) customerFactory.createPerson(name, birthdate, street, city, state, zip, email);
                customer.setId(custID);

                CCAccount account = (CCAccount) factory.createAccount(customer, accNum);
                account.setId(accId);
                account.setInterestRate(accIntRate);
                account.setBalance(accBalance);

                String sql = SqlRequestConstants.ALL_OPERATIONS_BY_ACCOUNT + account.getId();
                ResultSet opRs = db.executeQuery(sql);
                while (opRs.next()) {
                    double opAmount = opRs.getDouble(2);
                    LocalDate opDate = LocalDate.parse(opRs.getString(3));
                    String opType = opRs.getString(4);

                    IEntry operation;
                    if(opType.equals(DEPOSIT)) {
                        operation = new DepositOperation(opAmount, opDate);
                    } else if (opType.equals(WITHDRAW)) {
                        operation = new WithdrawOperation(opAmount, opDate);
                    } else {
                        operation = new InterestOperation(opAmount, opDate);
                    }
                    account.addOperation(operation);
                }
                return account;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public IModel getAccountByPerson(ICustomer customer) {
        try {
            Person owner = (Person)customer;
            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory factory = new SimpleAccountFactory();

            DataBaseAccess db = new DataBaseAccess();
            Connection connection = db.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.SELECT_ACCOUNT_BY_CUSTOMER);
            statement.setInt(1, owner.getId());

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                int accId = rs.getInt(1);
                String accNum = rs.getString(2);
                double accBalance = rs.getDouble(3);
                double accIntRate = rs.getDouble(4);

                CCAccount account = (CCAccount) factory.createAccount(customer, accNum);
                account.setId(accId);
                account.setInterestRate(accIntRate);
                account.setBalance(accBalance);

                String sql = SqlRequestConstants.ALL_OPERATIONS_BY_ACCOUNT + account.getId();
                ResultSet opRs = db.executeQuery(sql);
                while (opRs.next()) {
                    double opAmount = opRs.getDouble(2);
                    LocalDate opDate = LocalDate.parse(opRs.getString(3));
                    String opType = opRs.getString(4);

                    IEntry operation;
                    if(opType.equals(DEPOSIT)) {
                        operation = new DepositOperation(opAmount, opDate);
                    } else if (opType.equals(WITHDRAW)) {
                        operation = new WithdrawOperation(opAmount, opDate);
                    } else {
                        operation = new InterestOperation(opAmount, opDate);
                    }
                    account.addOperation(operation);
                }
                return account;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
