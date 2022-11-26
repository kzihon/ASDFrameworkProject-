package bank.model;

import bank.dao.PersonDAO;
import framework.interfaces.IPerson;
import framework.model.Customer;

import java.time.LocalDate;

public class Person extends Customer implements IPerson {
	private int id;
    private LocalDate birthdate;

    protected Person(String name, String email,  LocalDate birthdate, String street, String city, String state, String zip) {
        super(name, email, street, city, state, zip);
        this.birthdate = birthdate;
    }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public void sendEmail(String message) {
        System.out.println("Email: " + message + ". Sent to " + this.getEmail());
    }

	@Override
	public void save() {
		PersonDAO dao = new PersonDAO();
		if(this.id == 0) {
			dao.persist(this);
			this.setId(dao.getLastId());
		} else {
			dao.update(this);
		}
		this.notifyViews();
	}

	@Override
	public void delete() {
		PersonDAO dao = new PersonDAO();
		dao.delete(this);
		this.notifyViews();
	}
}
