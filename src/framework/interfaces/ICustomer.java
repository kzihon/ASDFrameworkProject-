package framework.interfaces;

import framework.model.Address;
import framework.model.IModel;

import java.util.List;

public interface ICustomer extends IModel {
	String getName();
	void setName(String name);
	String getEmail();
	void setEmail(String email);
	List<IAccount> getAccounts();
	Address getAddress();
	void setAddress(Address address);
	void sendEmail(String message);
}
