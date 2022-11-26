package framework.view;

import framework.controller.IController;
import framework.interfaces.IAccount;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract public class CreationalOperationModalView extends JDialog implements IView {
	private static final long serialVersionUID = -5669449254130140106L;
	private RadioButtonMediator radioTypeMediator;
	
	private MainFrame parentPage;
	private IController controller;
	private IAccount model;
	
	//{{DECLARE_CONTROLS
	protected JButton JButton_OK = new JButton();
	protected JButton JButton_Cancel = new JButton();
	//}}
	
	public CreationalOperationModalView(MainFrame parent, String operationTitle) {
		super(parent);

		this.parentPage = parent;
		setTitle(operationTitle);
		setModal(true);
		getContentPane().setLayout(null);
		setSize(301, 450);
		setVisible(false);
		
		this.radioTypeMediator = new RadioButtonMediator();
		
		JButton_OK.setText("OK");
		JButton_OK.setActionCommand("OK");
		getContentPane().add(JButton_OK);
		JButton_OK.setBounds(48,362,84,24);
		
		JButton_Cancel.setText("Cancel");
		JButton_Cancel.setActionCommand("Cancel");
		getContentPane().add(JButton_Cancel);
		JButton_Cancel.setBounds(156,362,84,24);
		
		SymAction lSymAction = new SymAction();
		JButton_OK.addActionListener(lSymAction);
		JButton_Cancel.addActionListener(lSymAction);
	}
	
	final public void addRadioTypeAccount(RadioButtonTypeAccount radioTypeAccount) {
		this.radioTypeMediator.addColleague(radioTypeAccount);
		int positionParameter = this.radioTypeMediator.getNumberOfColleagues();
		
		getContentPane().add(radioTypeAccount);		
		
		if(positionParameter == 1) {
			radioTypeAccount.setSelected(true);
			radioTypeAccount.setBounds(36, 12, 84, 24);
		} else radioTypeAccount.setBounds(36,  ((15 * positionParameter) +  (10 * (positionParameter - 1))), 84, 24);
	}
	
	private class SymAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();
			if (object == JButton_OK) JButtonOK_actionPerformed(event);
			else if (object == JButton_Cancel) JButtonCalcel_actionPerformed(event);
		}
	}
	
	final public void setModel(IAccount model) {
		this.model = model;
	}
	
	final public void setController(IController controller) {
		this.controller = controller;
	}
	
	private void JButtonOK_actionPerformed(ActionEvent event) {
		if(this.controller == null) {
			JOptionPane.showMessageDialog(JButton_OK, "The controller to handle this request was not set!", "Warning: null controller found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		this.controller.add(this);
	}

	private void JButtonCalcel_actionPerformed(ActionEvent event) {
        dispose();
	}
	
	final public String getSelectedType() {
		return this.radioTypeMediator.getSelectedValue();
	}
	
	@Override public void update() {
		if(this.model == null) {
			JOptionPane.showMessageDialog(JButton_OK, "The account model to handle the update request was not set!", "Warning: null model found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		this.parentPage.dataListView.addAnItem(this.model);
		this.refresh();
	}
	
	abstract protected void refresh();
}
