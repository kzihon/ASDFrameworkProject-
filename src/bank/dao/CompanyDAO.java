package bank.dao;

import bank.model.Company;
import bank.model.CustomerFactory;
import bank.util.SqlRequestConstants;
import framework.dao.DAO;
import framework.model.IModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements DAO {
    private static final String COMPANY = "COMPANY";
    private DataBaseAccess dba;

    public CompanyDAO() {
        this.dba = new DataBaseAccess();
    }

    @Override
    public void persist(IModel model) {
        try {
            Company company = (Company) model;
            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.PERSIST_COMPANY);
            statement.setString(1, company.getName());
            statement.setString(2, company.getEmail());
            statement.setString(3, company.getAddress().getStreet());
            statement.setString(4, company.getAddress().getCity());
            statement.setString(5, company.getAddress().getState());
            statement.setString(6, company.getAddress().getZip());
            statement.setInt(7, company.getNumberOfEmployees());

            statement.execute();
            company.setId(getLastId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IModel model) {
        try {
            Company company = (Company) model;
            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.UPDATE_COMPANY);
            statement.setString(1, company.getName());
            statement.setString(2, company.getEmail());
            statement.setInt(3, company.getNumberOfEmployees());
            statement.setString(4, company.getAddress().getStreet());
            statement.setString(5, company.getAddress().getCity());
            statement.setString(6, company.getAddress().getState());
            statement.setString(7, company.getAddress().getZip());
            statement.setInt(8, company.getId());

            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IModel getById(int id) {

        try {
            String sql = SqlRequestConstants.SELECT_COMPANY_BY_ID + id;

            CustomerFactory factory = new CustomerFactory();
            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                String name = rs.getString(2);
                String email = rs.getString(3);
                String street = rs.getString(4);
                String city = rs.getString(5);
                String state = rs.getString(6);
                String zip = rs.getString(7);
                int number_of_employees = rs.getInt(8);
                Company company = (Company) factory.createCompany(name, number_of_employees, street, city, state, zip, email);
                company.setId(id);
                return company;
            }
            return  null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IModel> getAll() {

        try {
            List<IModel> data = new ArrayList<>();
            String sql = SqlRequestConstants.ALL_COMPANIES;

            CustomerFactory factory = new CustomerFactory();
            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                String street = rs.getString(4);
                String city = rs.getString(5);
                String state = rs.getString(6);
                String zip = rs.getString(7);
                int number_of_employees = rs.getInt(8);
                Company company = (Company) factory.createCompany(name, number_of_employees, street, city, state, zip, email);
                company.setId(id);
                data.add(company);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(IModel model) {

        try {
            Company company = (Company) model;
            String sql = SqlRequestConstants.DELETE_CUSTOMER + company.getId() + " AND customer_type = '" + COMPANY + "'";
            this.dba.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getLastId() {
        try {
            String sql = SqlRequestConstants.LAST_CUSTOMER_ID;
            ResultSet rs = this.dba.executeQuery(sql);
            while(rs.next()) {
                return rs.getInt(1);
            }
            return  0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    static public IModel getCompanyByEmail(String email) {
        try {
            email = "%" + email + "%";
            CustomerFactory factory = new CustomerFactory();

            Connection connection = new DataBaseAccess().getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.SELECT_COMPANY_BY_EMAIL);
            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String oldEmail = rs.getString(3);
                String street = rs.getString(4);
                String city = rs.getString(5);
                String state = rs.getString(6);
                String zip = rs.getString(7);
                int number_of_employees = rs.getInt(8);
                Company company = (Company) factory.createCompany(name, number_of_employees, street, city, state, zip, email);
                company.setId(id);
                return company;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
