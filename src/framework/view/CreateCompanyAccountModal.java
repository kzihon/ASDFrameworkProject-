package framework.view;

import java.util.List;

public class CreateCompanyAccountModal extends CreateAccountModal {
	private static final long serialVersionUID = -1428456648026568261L;

	public CreateCompanyAccountModal(MainFrame parent, String operationTitle,
			List<RadioButtonTypeAccount> listOfRadiosTypeAccount) {
		super(parent, operationTitle, listOfRadiosTypeAccount);
		
		this.JLabelSpecific.setText("No of employees");
	}
	
	public String getNumberOfEmployees() {
		return JTextField_SPECIFIC.getText().trim();
	}

}
