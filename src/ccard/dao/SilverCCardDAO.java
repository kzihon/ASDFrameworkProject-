package ccard.dao;


import ccard.interfaces.CCardFactory;
import ccard.interfaces.ICreditCard;
import ccard.model.*;
import ccard.util.SqlRequestConstants;
import framework.dao.DAO;
import framework.interfaces.IEntry;
import framework.model.IModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SilverCCardDAO implements DAO {
    private static final String SILVER = "SILVER";
    private static final String PERSON = "PERSON";
    final static private String WITHDRAW = "WITHDRAW";
    final static private String DEPOSIT = "DEPOSIT";
    private DataBaseAccess dba;

    public SilverCCardDAO() {
        this.dba = new DataBaseAccess();
    }
    @Override
    public void persist(IModel model) {
        Connection connection = this.dba.getConnection();
        try {
            connection.setAutoCommit(false);
            ICreditCard ccard = (CreditCard)model;

            CCAccount account = ccard.getCCAccount();
            Person person = (Person) ccard.getCustomer();

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

                PreparedStatement ccardStatement = connection.prepareStatement(SqlRequestConstants.PERSIST_CREDITCARD_FOR_NEW_CUSTOMER);
                ccardStatement.setString(1, ccard.getCcNumber());
                ccardStatement.setString(2, ccard.getExpirationDate().format(DateTimeFormatter.ISO_DATE));
                ccardStatement.setString(3, SILVER);
                ccardStatement.executeUpdate();

                connection.commit();
                ccard.setId(this.getLastId());
                account.setId(new CCAccountDAO().getLastId());
                ((Person) ccard.getCustomer()).setId(new PersonDAO().getLastId());
            } else {
                PreparedStatement ccardStatement = connection.prepareStatement(SqlRequestConstants.PERSIST_CREDITCARD_FOR_OLD_CUSTOMER);
                ccardStatement.setString(1, ccard.getCcNumber());
                ccardStatement.setString(2, ccard.getExpirationDate().format(DateTimeFormatter.ISO_DATE));
                ccardStatement.setDouble(3, account.getBalance());
                ccardStatement.setString(4, SILVER);
                ccardStatement.setInt(5, account.getId());
                ccardStatement.executeUpdate();

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
            ICreditCard ccard = (CreditCard) model;

            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.UPDATE_CREDITCARD);
            statement.setString(1, ccard.getExpirationDate().format(DateTimeFormatter.ISO_DATE));
            statement.setDouble(2, ccard.getLastMonthBalance());
            statement.setString(3, SILVER);
            statement.setInt(4, ccard.getId());

            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IModel getById(int id) {
        try {
            String sql = SqlRequestConstants.SELECT_CREDITCARD_BY_ID + id + " AND cc.cctype = '" + SILVER + "'";

            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory accountFactory = new SimpleAccountFactory();
            CCardFactory factory = SimpleCCardFactory.getSilverCCardFactory();

            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                int ccId = rs.getInt(1);
                String ccExp = rs.getString(2);
                LocalDate ccExpDate = LocalDate.parse(ccExp, DateTimeFormatter.ISO_DATE);
                double ccLastBalance = rs.getDouble(3);
                String ccNum = rs.getString(6);

                int accId = rs.getInt(7);
                String accNum = rs.getString(8);
                double accBalance = rs.getDouble(9);
                double accIntRate = rs.getDouble(10);

                int custID = rs.getInt(12);
                String name = rs.getString(13);
                String email = rs.getString(14);
                String street = rs.getString(15);
                String city = rs.getString(16);
                String state = rs.getString(17);
                String zip = rs.getString(18);
                String cusBirth = rs.getString(19);

                LocalDate birthdate = LocalDate.parse(cusBirth, DateTimeFormatter.ISO_DATE);
                Person customer = (Person) customerFactory.createPerson(name, birthdate, street, city, state, zip, email);
                customer.setId(custID);

                CCAccount account = (CCAccount) accountFactory.createAccount(customer, accNum);
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

                ICreditCard creditCard = factory.createCCard(account, ccNum, ccExpDate);
                creditCard.setId(ccId);
                creditCard.setLastMonthBalance(ccLastBalance);
                return creditCard;
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
            String sql = SqlRequestConstants.ALL_CREDITCARDS + " AND cc.cctype = '" + SILVER + "'";

            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory accountFactory = new SimpleAccountFactory();
            CCardFactory factory = SimpleCCardFactory.getSilverCCardFactory();

            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                int ccId = rs.getInt(1);
                String ccExp = rs.getString(2);
                LocalDate ccExpDate = LocalDate.parse(ccExp, DateTimeFormatter.ISO_DATE);
                double ccLastBalance = rs.getDouble(3);
                String ccNum = rs.getString(6);

                int accId = rs.getInt(7);
                String accNum = rs.getString(8);
                double accBalance = rs.getDouble(9);
                double accIntRate = rs.getDouble(10);

                int custID = rs.getInt(12);
                String name = rs.getString(13);
                String email = rs.getString(14);
                String street = rs.getString(15);
                String city = rs.getString(16);
                String state = rs.getString(17);
                String zip = rs.getString(18);
                String cusBirth = rs.getString(19);

                LocalDate birthdate = LocalDate.parse(cusBirth, DateTimeFormatter.ISO_DATE);
                Person customer = (Person) customerFactory.createPerson(name, birthdate, street, city, state, zip, email);
                customer.setId(custID);

                CCAccount account = (CCAccount) accountFactory.createAccount(customer, accNum);
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

                ICreditCard creditCard = factory.createCCard(account, ccNum, ccExpDate);
                creditCard.setId(ccId);
                creditCard.setLastMonthBalance(ccLastBalance);

                data.add(creditCard);
            }
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLastId() {
        try {
            String sql = SqlRequestConstants.LAST_CREDITCARD_ID;
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
            ICreditCard ccard = (CreditCard) model;
            String sql = SqlRequestConstants.DELETE_CREDITCARD + ccard.getId() ;
            this.dba.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public IModel getCCardByNumber(String accountNumber) {
        try {
            accountNumber = "%" + accountNumber + "%";

            SimpleCustomerFactory customerFactory = new SimpleCustomerFactory();
            SimpleAccountFactory accountFactory = new SimpleAccountFactory();
            CCardFactory factory = SimpleCCardFactory.getSilverCCardFactory();

            DataBaseAccess db = new DataBaseAccess();
            Connection connection = db.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    SqlRequestConstants.SELECT_CREDITCARD_BY_ACCOUNT_NUMBER + " AND cc.cctype = '" + SILVER + "'");
            statement.setString(1, accountNumber);

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                int ccId = rs.getInt(1);
                String ccExp = rs.getString(2);
                LocalDate ccExpDate = LocalDate.parse(ccExp, DateTimeFormatter.ISO_DATE);
                double ccLastBalance = rs.getDouble(3);
                String ccNum = rs.getString(6);

                int accId = rs.getInt(7);
                String accNum = rs.getString(8);
                double accBalance = rs.getDouble(9);
                double accIntRate = rs.getDouble(10);

                int custID = rs.getInt(12);
                String name = rs.getString(13);
                String email = rs.getString(14);
                String street = rs.getString(15);
                String city = rs.getString(16);
                String state = rs.getString(17);
                String zip = rs.getString(18);
                String cusBirth = rs.getString(19);

                LocalDate birthdate = LocalDate.parse(cusBirth, DateTimeFormatter.ISO_DATE);
                Person customer = (Person) customerFactory.createPerson(name, birthdate, street, city, state, zip, email);
                customer.setId(custID);

                CCAccount account = (CCAccount) accountFactory.createAccount(customer, accNum);
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

                ICreditCard creditCard = factory.createCCard(account, ccNum, ccExpDate);
                creditCard.setId(ccId);
                creditCard.setLastMonthBalance(ccLastBalance);
                return creditCard;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
