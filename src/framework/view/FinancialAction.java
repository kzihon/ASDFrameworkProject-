package framework.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FinancialAction extends JButton {
	private static final long serialVersionUID = 1215265835720157451L;
	
	private MainFrame parentPage;
	private FinancialOperationModalView modalView;
	
	public FinancialAction(MainFrame parentPage,String actionTitle, FinancialOperationModalView modalView) {
		super();
		setText(actionTitle);
		this.modalView = modalView;
		this.parentPage = parentPage;
		
		this.addActionListener(new SymAction());
	}
	
	private class SymAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			FinancialAction.this.actionPerformed(event);
		}
	}

    private void actionPerformed(ActionEvent event) {
		if(!this.parentPage.dataListView.doesAnItemHaveBeenSelected()) {
			JOptionPane.showMessageDialog(this, "You need to select an item for this action!", "Warning: null item selected found", JOptionPane.WARNING_MESSAGE);
			return;
		}
		this.modalView.init();
		this.modalView.setBounds(430, 50, 300, 170);
		this.modalView.setVisible(true);
	}
}
