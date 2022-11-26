package bank.dao;

import bank.model.CheckingAccount;
import bank.model.InterestOperation;
import bank.model.SavingsAccount;
import bank.util.SqlRequestConstants;
import framework.interfaces.IEntry;
import framework.model.Account;
import framework.model.IModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountTool {
    static final public void addInterest() {
        Connection connection = new DataBaseAccess().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.UPDATE_BALANCE);
            PreparedStatement operationStatement = connection.prepareStatement(SqlRequestConstants.PERSIST_OPERATION);

            for (IModel item: new SavingsAccountDAO().getAll()) {
                SavingsAccount account = (SavingsAccount) item;

                IEntry entry = new InterestOperation(account.getBalance() * account.getInterestRate() / 100, LocalDate.now());
                operationStatement.setDouble(1, entry.getAmount());
                operationStatement.setString(2, entry.getDate().format(DateTimeFormatter.ISO_DATE));
                operationStatement.setString(3, "INTEREST");
                operationStatement.setInt(4, account.getId());

                account.addInterest();

                statement.setDouble(1, account.getBalance());
                statement.setInt(2, account.getId());

                statement.executeUpdate();
                operationStatement.executeUpdate();
            }

            for (IModel item: new CheckingAccountDAO().getAll()) {
                CheckingAccount account = (CheckingAccount) item;

                IEntry entry = new InterestOperation(account.getBalance() * account.getInterestRate() / 100, LocalDate.now());
                operationStatement.setDouble(1, entry.getAmount());
                operationStatement.setString(2, entry.getDate().format(DateTimeFormatter.ISO_DATE));
                operationStatement.setString(3, "INTEREST");
                operationStatement.setInt(4, account.getId());

                account.addInterest();

                statement.setDouble(1, account.getBalance());
                statement.setInt(2, account.getId());

                statement.executeUpdate();
                operationStatement.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            try {
                if(connection != null) connection.rollback();
            } catch (Exception exception) {}

            throw new RuntimeException(e);
        }
    }
}
