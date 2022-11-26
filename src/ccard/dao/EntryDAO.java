package ccard.dao;

import ccard.model.DepositOperation;
import ccard.model.InterestOperation;
import ccard.util.SqlRequestConstants;
import framework.dao.DAO;
import framework.interfaces.IEntry;
import framework.model.IModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EntryDAO implements DAO {
    private DataBaseAccess dba;
    final static private String WITHDRAW = "WITHDRAW";
    final static private String DEPOSIT = "DEPOSIT";
    final static private String INTEREST = "INTEREST";

    public EntryDAO() {
        this.dba = new DataBaseAccess();
    }

    @Override
    public void persist(IModel model) {

    }

    public void persist(int accountID, IEntry entry) {
        try {
            String entryType = WITHDRAW;
            if(entry instanceof DepositOperation) entryType = DEPOSIT;
            else if(entry instanceof InterestOperation) entryType = INTEREST;

            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.PERSIST_OPERATION);
            statement.setDouble(1, entry.getAmount());
            statement.setString(2, entry.getDate().format(DateTimeFormatter.ISO_DATE));
            statement.setString(3, entryType);
            statement.setInt(4, accountID);

            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IModel model) {

    }

    @Override
    public IModel getById(int id) {
        return null;
    }

    @Override
    public List<IModel> getAll() {
        return null;
    }

    @Override
    public void delete(IModel model) {

    }
}
