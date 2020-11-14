package ca.mcgill.ecse223.flexibook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

public class FlexiBookPage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2107256386071878946L;
	private JLabel errorMessage;
	
	//pop-up window set up
	private JScrollPane setUpScrollPane;
	private JTable setUpTable;
	private JLabel setUpLabel;
	
	public FlexiBookPage() {
		initComponents();
		refreshData();
	}
	private void refreshData() {
		
		
	}
	
	
	
	
	
	
	private void initComponents() {
		//error message
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		
		//pane
		setUpScrollPane = new JScrollPane(setUpTable);
		this.add(setUpScrollPane);

		setUpScrollPane.setPreferredSize(new Dimension(50000,50000));
		setUpScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("FlexiBook Application");
	}

}
