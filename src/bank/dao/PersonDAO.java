package bank.dao;

import bank.model.CustomerFactory;
import bank.model.Person;
import bank.util.SqlRequestConstants;
import framework.dao.DAO;
import framework.model.IModel;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO implements DAO {
    private DataBaseAccess dba = new DataBaseAccess();

    public PersonDAO() {
        this.dba = new DataBaseAccess();
    }

    @Override
    public void persist(IModel model) {
        try {
            Person person = (Person) model;
            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.PERSIST_PERSON);
            statement.setString(1, person.getName());
            statement.setString(2, person.getEmail());
            statement.setString(3, person.getAddress().getStreet());
            statement.setString(4, person.getAddress().getCity());
            statement.setString(5, person.getAddress().getState());
            statement.setString(6, person.getAddress().getZip());
            statement.setString(7, person.getBirthdate().format(DateTimeFormatter.ISO_DATE));

            statement.execute();
            person.setId(getLastId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(IModel model) {
        try {
            Person person = (Person) model;

            Connection connection = this.dba.getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.UPDATE_PERSON);
            statement.setString(1, person.getName());
            statement.setString(2, person.getEmail());
            statement.setString(3, person.getBirthdate().format(DateTimeFormatter.ISO_DATE));
            statement.setString(4, person.getAddress().getStreet());
            statement.setString(5, person.getAddress().getCity());
            statement.setString(6, person.getAddress().getState());
            statement.setString(7, person.getAddress().getZip());
            statement.setInt(8, person.getId());

            statement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IModel getById(int id) {

        try {
            String sql = SqlRequestConstants.SELECT_PERSON_BY_ID + id;

            CustomerFactory factory = new CustomerFactory();
            ResultSet rs = dba.executeQuery(sql);
            while(rs.next()) {
                String name = rs.getString(2);
                String email = rs.getString(3);
                String street = rs.getString(4);
                String city = rs.getString(5);
                String state = rs.getString(6);
                String zip = rs.getString(7);
                LocalDate birthdate = LocalDate.parse(rs.getString(8));
                Person person = (Person) factory.createPerson(name, birthdate, street, city, state, zip, email);
                person.setId(id);
                return person;
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
            String sql = SqlRequestConstants.ALL_PERSONS;

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
                LocalDate birthdate = LocalDate.parse(rs.getString(8));
                Person person = (Person) factory.createPerson(name, birthdate, street, city, state, zip, email);
                person.setId(id);
                data.add(person);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(IModel model) {

        try {
            Person person = (Person) model;
            String sql = SqlRequestConstants.DELETE_CUSTOMER + person.getId() + " AND customer_type = 'PERSON'" ;
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    static public IModel getPersonByEmail(String email) {
        try {
            email = "%" + email + "%";
            CustomerFactory factory = new CustomerFactory();

            Connection connection = new DataBaseAccess().getConnection();
            PreparedStatement statement = connection.prepareStatement(SqlRequestConstants.SELECT_PERSON_BY_EMAIL);
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
                LocalDate birthdate = LocalDate.parse(rs.getString(8));
                Person person = (Person) factory.createPerson(name, birthdate, street, city, state, zip, email);
                person.setId(id);
                return person;
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
