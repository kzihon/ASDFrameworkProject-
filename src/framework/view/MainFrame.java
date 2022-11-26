package framework.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements IView {
	private static final long serialVersionUID = -8034570039932382815L;
	
	protected ListView dataListView;
	protected JScrollPane scrollContainer;
	protected JPanel pageContainer;
	
	private int numberOfCreationalAction = 0;
	private int numberOfFinancialAction = 0;
	private boolean interestActionAdded = false;
	private JButton JButton_Exit = new JButton();
	
	public MainFrame(String pageTitle) {
		super();
		this.setTitle(pageTitle);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0,0));
		setSize(640,480);
		setVisible(false);		
		
		this.pageContainer = new JPanel();
		this.pageContainer.setLayout(null);
		getContentPane().add(BorderLayout.CENTER, this.pageContainer);
		this.pageContainer.setBounds(0,0,576,310);
		
		this.scrollContainer = new JScrollPane();
		this.pageContainer.add(this.scrollContainer);
		this.scrollContainer.setBounds(12,92,444,160);
		
		JButton_Exit.setText("Exit");
		this.pageContainer.add(JButton_Exit);
		JButton_Exit.setBounds(468,248,96,34);
		JButton_Exit.addActionListener(new SymAction());
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
	}
	
	public void setListView(ListView listView) {
		this.scrollContainer.getViewport().add(listView);
        listView.setBounds(0, 0, 420, 0);
        this.dataListView = listView;
	}
	
	public void addFinancialAction(FinancialAction action) {
		if(action == null) return;
		this.pageContainer.add(action);
		action.setBounds(468, 100 + (this.numberOfFinancialAction * 50), 90, 35);
		this.numberOfFinancialAction++;
	}
	
	public void addInterestAction(InterestAction action) {
		if(this.interestActionAdded) return;
		
		this.pageContainer.add(action);
		action.setBounds(448,20,106,33);
		this.interestActionAdded = true;
	}
	
	public void addCreationalAction(CreationalAction action) {
		if(action == null) return;
		this.pageContainer.add(action);
		action.setBounds(24 + (this.numberOfCreationalAction * 190), 20, 180, 34);
		this.numberOfCreationalAction++;
	}
	
	public void update() {
		
	}
	
	protected class SymWindow extends WindowAdapter {
		public void windowClosing(WindowEvent event)
		{
			Object object = event.getSource();
			if (object == MainFrame.this) MainFrame_windowClosing(event);
		}
	}

	private void MainFrame_windowClosing(WindowEvent event) {
		MainFrame_windowClosing_Interaction(event);
	}

	private void MainFrame_windowClosing_Interaction(WindowEvent event) {
		try {
			this.exitApplication();
		} catch (Exception e) {
		}
	}
	
	private void exitApplication() {
		try {
		    	this.setVisible(false);    // hide the Frame
		    	this.dispose();            // free the system resources
		    	System.exit(0);            // close the application
		} catch (Exception e) {}
	}
	
	private class SymAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();
			if (object == JButton_Exit) JButtonExit_actionPerformed(event);
		}
	}
    
    //When the Exit button is pressed this code gets executed
    //this will exit from the system
    private void JButtonExit_actionPerformed(ActionEvent event) {
		System.exit(0);
	}

}
