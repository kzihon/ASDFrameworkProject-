package framework.view;

import framework.controller.IController;
import framework.interfaces.IAccount;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class FinancialOperationModalView extends JDialog implements IView {
	private static final long serialVersionUID = -5928761689396415117L;
	
	private MainFrame parentPage;
	private IController controller;
	private IAccount model;
	
	//{{DECLARE_CONTROLS
	private JLabel JLabelAccountNumber = new JLabel();
	private JLabel JLabelAmount = new JLabel();
	private JTextField JTextField_AccountNumber = new JTextField();
	private JButton JButton_OK = new JButton();
	private JButton JButton_Cancel = new JButton();
	private JTextField JTextField_Amount = new JTextField();
	//}}
	
	public FinancialOperationModalView(MainFrame parent, String operationTitle, String accountLabelTitle) {
		super(parent);
		this.parentPage = parent;
		setTitle(operationTitle);
		setModal(true);
		getContentPane().setLayout(null);
		setSize(300,140);
		setVisible(false);

		JLabelAccountNumber.setText(accountLabelTitle);
		getContentPane().add(JLabelAccountNumber);
		JLabelAccountNumber.setForeground(java.awt.Color.black);
		JLabelAccountNumber.setBounds(12,12,48,24);
		
		JLabelAmount.setText("Amount");
		getContentPane().add(JLabelAmount);
		JLabelAmount.setForeground(java.awt.Color.black);
		JLabelAmount.setBounds(12,48,48,24);
		
		JTextField_AccountNumber.setEditable(false);
		getContentPane().add(JTextField_AccountNumber);
		JTextField_AccountNumber.setBounds(84,12,144,24);
		
		getContentPane().add(JTextField_Amount);
		JTextField_Amount.setBounds(84,48,144,24);
		
		JButton_OK.setText("OK");
		JButton_OK.setActionCommand("OK");
		getContentPane().add(JButton_OK);		
		JButton_OK.setBounds(36,84,84,24);
		
		JButton_Cancel.setText("Cancel");
		JButton_Cancel.setActionCommand("Cancel");
		getContentPane().add(JButton_Cancel);
		JButton_Cancel.setBounds(156,84,84,24);

		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		JButton_OK.addActionListener(lSymAction);
		JButton_Cancel.addActionListener(lSymAction);
		//}}
	}
	
	private class SymAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();
			if (object == JButton_OK) JButtonOK_actionPerformed(event);
			else if (object == JButton_Cancel) JButtonCalcel_actionPerformed(event);
		}
	}

	private void JButtonOK_actionPerformed(ActionEvent event) {
		if(this.controller == null) {
			JOptionPane.showMessageDialog(JButton_OK, "The controller to handle this request was not set!", "Warning: null controller found", JOptionPane.WARNING_MESSAGE);
			return;
		}
        this.controller.add(this);
	}

	private void JButtonCalcel_actionPerformed(ActionEvent event) {
		this.refresh();
		dispose();
	}
	
	final public void setController(IController controller) {
		this.controller = controller;
	}
		
	final public void init() {
		JTextField_AccountNumber.setText(this.parentPage.dataListView.getAccountNumberOfTheSelectedItem());
	}
	
	final public String getAmount() {
		return JTextField_Amount.getText().trim();
	}
	
	final public String getAccountNumber() {
		return JTextField_AccountNumber.getText().trim();
	}
	
	final public void setModel(IAccount model) {
		this.model = model;
	}

	@Override public void update() {
		if(this.model == null) {
			JOptionPane.showMessageDialog(JButton_OK, "The account model to handle the update request was not set!", "Warning: null model found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		this.parentPage.dataListView
		.updateAmountValueForTheSelectedItem(
				String.valueOf(this.model.getBalance())
		);
		this.refresh();
	}
	
	final protected void refresh() {
		JTextField_Amount.setText("");
	}
}
