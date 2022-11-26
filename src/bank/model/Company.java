package bank.model;

import bank.dao.CompanyDAO;
import framework.interfaces.ICompany;
import framework.model.Customer;

public class Company extends Customer implements ICompany {
	private int id;
    private int numberOfEmployees;

    protected Company(String name, String email, int numberOfEmployees,String street, String city, String state, String zip) {
        super(name, email, street,city,state,zip);
        this.numberOfEmployees = numberOfEmployees;
    }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    @Override
    public void sendEmail(String message) {
		System.out.println("Email: " + message + ". Sent to " + this.getEmail());
    }

	@Override
	public void save() {
		CompanyDAO dao = new CompanyDAO();
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
		CompanyDAO dao = new CompanyDAO();
		dao.delete(this);
		this.notifyViews();
	}
}
