package ccard.model;

import framework.interfaces.ICompany;
import framework.interfaces.IPerson;

import java.time.LocalDate;

/**
 *
 * Factory to create a customer
 * @author Group 3
 */
public class SimpleCustomerFactory {

    /**
     * Concrete customer factory
     *
     * @param name customer's name
     * @param email customer's
     * @param street
     * @param city
     * @return
     */
    public IPerson createPerson(String name, LocalDate birthdate, String street, String city, String state, String zip, String email) {
    	return new Person(name, email, birthdate, street, city, state, zip);
    }

}
