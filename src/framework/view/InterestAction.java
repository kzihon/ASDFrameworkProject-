package framework.view;

import bank.dao.CheckingAccountDAO;
import bank.dao.SavingsAccountDAO;
import framework.controller.IController;
import framework.model.Account;
import framework.model.IModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final public class InterestAction extends JButton implements IView {
	private static final long serialVersionUID = 1215265835720157451L;

	private MainFrame parent;
	private InterestDialog modal;
	private IController controller;

	@Override
	public void update() {
		this.parent.dataListView.clearList();

		List<IModel> accountList = new ArrayList<>();
		accountList.addAll(new SavingsAccountDAO().getAll());
		accountList.addAll(new CheckingAccountDAO().getAll());

		Comparator<IModel> comparator = (x, y) -> {
			Account a = (Account) x;
			Account b = (Account) y;
			return a.getAccountNumber().compareTo(b.getAccountNumber());
		};
		accountList.sort(comparator);
		for (IModel item: accountList) {
			this.parent.dataListView.addAnItem(item);
		}
	}

	private class InterestDialog extends JDialog {
		private static final long serialVersionUID = -4233769132548895495L;
		
		JScrollPane scrollPanel = new JScrollPane();
		JLabel textLabel_PARAGRAPH = new JLabel();
		
		private JButton JButton_OK = new JButton();
		private JButton JButton_Cancel = new JButton();
		
		public InterestDialog(MainFrame parent, String title, String paragraph) {
			super(parent);
			setTitle("Add compamy account");
			setModal(true);
			getContentPane().setLayout(null);
			setSize(350,150);
			setVisible(false);
			
			getContentPane().add(scrollPanel);
			scrollPanel.setBounds(24,24,345,100);
			
			textLabel_PARAGRAPH.setText(paragraph);
			scrollPanel.getViewport().add(textLabel_PARAGRAPH);
			textLabel_PARAGRAPH.setBounds(0, 0, 340,95);
			
			JButton_OK.setText("OK");
			JButton_OK.setActionCommand("OK");
			getContentPane().add(JButton_OK);		
			JButton_OK.setBounds(36,130,84,24);
			
			JButton_Cancel.setText("Cancel");
			JButton_Cancel.setActionCommand("Cancel");
			getContentPane().add(JButton_Cancel);
			JButton_Cancel.setBounds(156,130,84,24);
			
			//{{REGISTER_LISTENERS
			DialogAction lSymAction = new DialogAction();
			JButton_OK.addActionListener(lSymAction);
			JButton_Cancel.addActionListener(lSymAction);
			//}}
		}

		private class DialogAction implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				Object object = event.getSource();
				if (object == JButton_OK) JButtonOK_actionPerformed(event);
				else if (object == JButton_Cancel) JButtonCalcel_actionPerformed(event);
			}
		}

		private void JButtonOK_actionPerformed(ActionEvent event) {
			if(InterestAction.this.controller == null) {
				JOptionPane.showMessageDialog(JButton_OK, "The controller to handle this request was not set!", "Warning: null controller found", JOptionPane.WARNING_MESSAGE);
				return;
			}
			InterestAction.this.controller.add(InterestAction.this);
			dispose();
		}

		private void JButtonCalcel_actionPerformed(ActionEvent event) {
			dispose();
		}
	}
	
	public InterestAction(MainFrame pageParent, String actionTitle, String paragraph) {
		super();
		this.parent = pageParent;
		setText(actionTitle);

		this.modal = new InterestDialog(pageParent, actionTitle, paragraph);		
		this.addActionListener(new SymAction());
	}
	
	private class SymAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			InterestAction.this.actionPerformed(event);
		}
	}
	
	public void setController(IController controller) {
		this.controller = controller;
	}

    private void actionPerformed(ActionEvent event) {
    	if(this.controller == null) {
			JOptionPane.showMessageDialog(this, "The controller to handle this request was not set!", "Warning: null controller found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		this.modal.setBounds(430, 50, 345,200);
		this.modal.setVisible(true);
	}
}
