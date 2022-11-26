package framework.view;

import framework.model.IModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

abstract public class ListView extends JTable implements IView {
	private static final long serialVersionUID = -2596634554218077128L;
	private DefaultTableModel tableModel;
	
	private int indexOfAmountColumn = -1;
	private int indexOfAccountNumberColumn = -1;
	
	IModel model;
	
	
	public ListView() {
		super();
		this.tableModel = new DefaultTableModel();
		this.setModel(this.tableModel);
	}
	
	final public void setIndexOfAmountColumn(int indexOfAmountColumn) {
		this.indexOfAmountColumn = indexOfAmountColumn;
	}

	final public void setIndexOfAccountNumberColumn(int indexOfAccountNumberColumn) {
		this.indexOfAccountNumberColumn = indexOfAccountNumberColumn;
	}

	final protected void addColumn(String columnTitle) {
		this.tableModel.addColumn(columnTitle);
	}
	
	final public boolean doesAnItemHaveBeenSelected() {
		return this.getPositionOfTheSelectedItem() >=0;
	}
	
	private int getPositionOfTheSelectedItem() {
		return this.getSelectionModel().getMinSelectionIndex();
	}
	
	final public String getAccountNumberOfTheSelectedItem() {
		if(this.indexOfAccountNumberColumn == -1) {
			JOptionPane.showMessageDialog(this.getParent(), "The index of the account number column was not set!", "Warning: negative index found for account number column", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		int positionOfSelectedItem = this.getPositionOfTheSelectedItem(); 
		return this.tableModel.getValueAt(positionOfSelectedItem, indexOfAccountNumberColumn).toString();
	}
	
	final public void updateAmountValueForTheSelectedItem(String newAmount) {
		if(this.indexOfAmountColumn == -1) {
			JOptionPane.showMessageDialog(this.getParent(), "The index of the amount column was not set!", "Warning: negative index found for amount column", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int positionOfSelectedItem = this.getPositionOfTheSelectedItem();
		this.tableModel.setValueAt(newAmount, positionOfSelectedItem, indexOfAmountColumn);
	}
	
	final protected void addAnItem(Object ...valuesForColumns) {
		Object[] rowData = new Object[valuesForColumns.length];
		int index = 0;
		for(Object value: valuesForColumns) rowData[index++] = value;
		this.tableModel.addRow(rowData);
	}
	
	abstract public void addAnItem(IModel model);
	
	abstract public void update();

	final public void clearList() {
		while (this.tableModel.getRowCount() > 0) this.tableModel.removeRow(0);
	}
}
