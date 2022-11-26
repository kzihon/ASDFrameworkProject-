package framework.view;

import java.util.List;

public class CreatePersonalAccountModal extends CreateAccountModal {
	private static final long serialVersionUID = -1428456648026568261L;

	public CreatePersonalAccountModal(MainFrame parent, String operationTitle,
			List<RadioButtonTypeAccount> listOfRadiosTypeAccount) {
		super(parent, operationTitle, listOfRadiosTypeAccount);
	}
	
	public String getBirthDate() {
		return JTextField_SPECIFIC.getText().trim();
	}

}
