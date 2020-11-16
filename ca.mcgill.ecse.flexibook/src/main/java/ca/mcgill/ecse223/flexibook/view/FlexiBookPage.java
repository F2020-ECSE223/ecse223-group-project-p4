package ca.mcgill.ecse223.flexibook.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse223.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;

public class FlexiBookPage extends JFrame{
	
	private static final long serialVersionUID = -4426310869335015542L;
	private String error;
	private String success;
	
	// UI elements
	
	// message
	private JLabel message;
	
	//customer menu
	private JButton appBookingButton;
	private JButton manageCustomerAccountButton;
	
	//owner menu
	private JButton businessInfoButton;
	private JButton manageServiceButton;
	private JButton manageAppButton;
	private JButton viewAppCalenderButton;
	
	//make appointment
	private JTextField makeAppTime;
	private JTextField makeAppDate;
	private JComboBox<String> makeAppServiceList;
	private JLabel makeAppTimeLabel;
	private JLabel makeAppDateLabel;
	private JLabel makeAppServiceLabel;
	private JButton makeAppButton;
	
	//update appointment
	private JComboBox<String> updateAppDateList;
	private JTextField updateAppNewTime;
	private JTextField updateAppNewDate;
	private JComboBox<String> updateAppServiceList;
	private JLabel updateAppDateLabel;
	private JLabel updateAppNewTimeLabel;
	private JLabel updateAppNewDateLabel;
	private JLabel updateAppServiceLabel;
	private JButton updateAppButton;
	
	//cancel appointment
	private JComboBox<String> cancelAppDateList;
	private JLabel cancelAppDateLabel;
	private JButton cancelAppButton;
	
	//data
	ArrayList<String> availableServices = new ArrayList<>();
	ArrayList<String> existingAppointments = new ArrayList<>();
	
	

	public FlexiBookPage() {
		
		//if the user logged in is a customer 
		//initCustomerMenu();
		
		//else if the user is the owner
		//initOwnerMenu();

	
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initOwnerMenu() {
		getContentPane().removeAll(); 
		getContentPane().repaint();
		
		
		businessInfoButton = new JButton();
		businessInfoButton.setText("Manage Business Information");
		manageServiceButton = new JButton();
		manageServiceButton.setText("Manage Services");
		manageAppButton = new JButton();
		manageAppButton.setText("Manage Appointments");
		viewAppCalenderButton = new JButton();
		viewAppCalenderButton.setText("View Appointment Calender");
		
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoButton)
						.addComponent(manageServiceButton)
						.addComponent(manageAppButton)
						.addComponent(viewAppCalenderButton))
		
		);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessInfoButton, manageServiceButton, manageAppButton, viewAppCalenderButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {businessInfoButton, manageServiceButton, manageAppButton, viewAppCalenderButton});
						
		layout.setVerticalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(businessInfoButton)
							.addComponent(manageServiceButton)
							.addComponent(manageAppButton)
							.addComponent(viewAppCalenderButton))
		);
							
							
		
		businessInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				businessInfoActionPerformed(e);
			}
		});
		
		manageServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageServiceActionPerformed(e);
			}
		});
		
		manageAppButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageAppActionPerformed(e);
			}
		});
		
		viewAppCalenderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewCalenderActionPerformed(e);
			}
		});
		
		
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initCustomerMenu() {
		getContentPane().removeAll(); 
		getContentPane().repaint();
		
		appBookingButton = new JButton();
		appBookingButton.setText("Appoinment booking");
		manageCustomerAccountButton = new JButton();
		manageCustomerAccountButton.setText("Manage Account");
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(appBookingButton)
						.addComponent(manageCustomerAccountButton))
		
		);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appBookingButton, manageCustomerAccountButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appBookingButton, manageCustomerAccountButton});
						
		layout.setVerticalGroup(
				layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(appBookingButton)
							.addComponent(manageCustomerAccountButton))
		);
							
							
		
		appBookingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookAppActionPerformed(e);
			}
		});
		
		manageCustomerAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageAccountActionPerformed(e);
			}
		});
		
		
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initAppointmentBookingPage() {
		
		getContentPane().removeAll(); 
		getContentPane().repaint();
		
		// elements for error message
		message = new JLabel();
		
		//elements for make appointment
		makeAppTime = new JTextField();
		makeAppDate = new JTextField();
		makeAppServiceList = new JComboBox<String>(new String[0]);
		makeAppTimeLabel = new JLabel();
		makeAppTimeLabel.setText("Appointment Time: ");
		makeAppDateLabel = new JLabel();
		makeAppDateLabel.setText("Appointment date: ");
		makeAppServiceLabel = new JLabel();
		makeAppServiceLabel.setText("Service: ");
		makeAppButton = new JButton();
		makeAppButton.setText("Make Appointment");
		
		//elements for make appointment
		updateAppDateList = new JComboBox<String>(new String[0]);
		updateAppNewTime = new JTextField();
		updateAppNewDate = new JTextField();
		updateAppServiceList = new JComboBox<String>(new String[0]);
		updateAppDateLabel = new JLabel();
		updateAppDateLabel.setText("Appointment at: ");
		updateAppNewTimeLabel = new JLabel();
		updateAppNewTimeLabel.setText("New Time: ");
		updateAppNewDateLabel = new JLabel();
		updateAppNewDateLabel.setText("New Date: ");
		updateAppServiceLabel = new JLabel();
		updateAppServiceLabel.setText("New Service: ");
		updateAppButton = new JButton();
		updateAppButton.setText("Update Appointment");
		
		//elements for cancel appointment
		cancelAppDateList = new JComboBox<String>(new String[0]);
		cancelAppDateLabel = new JLabel();
		cancelAppDateLabel.setText("Appointment at: ");
		cancelAppButton = new JButton();
		cancelAppButton.setText("Cancel Appointment");
		
		
		//action listeners
		makeAppButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeAppointmentActionPerformed(e);
			}
		});
		
		updateAppButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAppointmentActionPerformed(e);
			}
		});
		
		cancelAppButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelAppointmentActionPerformed(e);
			}
		});
		
	
		

		
		// horizontal line elements
		JSeparator horizontalLineTop = new JSeparator();
		JSeparator horizontalLineMiddle = new JSeparator();
		JSeparator horizontalLineBottom = new JSeparator();
		
		// layout
		GroupLayout layout = new GroupLayout(getContentPane());
		
		
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(horizontalLineTop)
						.addComponent(horizontalLineMiddle)
						.addComponent(horizontalLineBottom)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(makeAppTimeLabel)
										.addComponent(updateAppDateLabel)
										.addComponent(updateAppNewTimeLabel)
										.addComponent(cancelAppDateLabel))
								.addGroup(layout.createParallelGroup()
										.addComponent(makeAppTime, 200, 200, 400)
										.addComponent(updateAppDateList, 200, 200, 400)
										.addComponent(updateAppNewTime, 200, 200, 400)
										.addComponent(cancelAppDateList, 200, 200, 400))
								.addGroup(layout.createParallelGroup()
										.addComponent(makeAppDateLabel)
										.addComponent(makeAppButton, 200, 200, 400)
										.addComponent(updateAppNewDateLabel)
										.addComponent(updateAppButton, 200, 200, 400)
										.addComponent(cancelAppButton, 200, 200, 400))
								.addGroup(layout.createParallelGroup()
										.addComponent(makeAppDate, 200, 200, 400)
										.addComponent(updateAppNewDate, 200, 200, 400))
								.addGroup(layout.createParallelGroup()
										.addComponent(makeAppServiceLabel)
										.addComponent(updateAppServiceLabel))
								.addGroup(layout.createParallelGroup()
										.addComponent(makeAppServiceList, 200, 200, 400)
										.addComponent(updateAppServiceList, 200, 200, 400))
								)
						
						)
		);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {makeAppTime, makeAppDate, makeAppServiceList});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {makeAppTime, makeAppDate, makeAppServiceList});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateAppDateLabel, updateAppDateList});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateAppDateLabel, updateAppDateList});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateAppNewTime, updateAppNewDate, updateAppServiceList});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateAppNewTime, updateAppNewDate, updateAppServiceList});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {cancelAppDateLabel, cancelAppDateList});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelAppDateLabel, cancelAppDateList});
		
		
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(message)
						.addComponent(horizontalLineTop)
						.addGroup(layout.createParallelGroup()
								.addComponent(makeAppTimeLabel)
								.addComponent(makeAppTime)
								.addComponent(makeAppDateLabel)
								.addComponent(makeAppDate)
								.addComponent(makeAppServiceLabel)
								.addComponent(makeAppServiceList))
						.addComponent(makeAppButton)
						.addComponent(horizontalLineMiddle)
						.addGroup(layout.createParallelGroup()
								.addComponent(updateAppDateLabel)
								.addComponent(updateAppDateList))
						.addGroup(layout.createParallelGroup()
								.addComponent(updateAppNewTimeLabel)
								.addComponent(updateAppNewTime)
								.addComponent(updateAppNewDateLabel)
								.addComponent(updateAppNewDate)
								.addComponent(updateAppServiceLabel)
								.addComponent(updateAppServiceList))
						.addComponent(updateAppButton)
						.addComponent(horizontalLineBottom)
						.addGroup(layout.createParallelGroup()
								.addComponent(cancelAppDateLabel)
								.addComponent(cancelAppDateList))
						.addComponent(cancelAppButton)
						)
							
		);
		
		


		
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void refreshDataForAppointmentBooking() {
		
		if(error != null) {
			message.setText(error);
			message.setForeground(Color.RED);
		}else if(success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);
		}
		
		
		// get the available services
		availableServices = FlexiBookController.getServiceList();
		makeAppServiceList.removeAllItems();
		updateAppServiceList.removeAllItems();
		for (String thisService : availableServices) {
			makeAppServiceList.addItem(thisService);
			updateAppServiceList.addItem(thisService);
		};
		makeAppServiceList.setSelectedIndex(-1);
		updateAppServiceList.setSelectedIndex(-1);
		
		
		existingAppointments = FlexiBookController.getCustomerAppointmentDates(FlexiBookApplication.getCurrentUser().getUsername());
		updateAppDateList.removeAllItems();
		cancelAppDateList.removeAllItems();
		for(String dateTime : existingAppointments) {
			updateAppDateList.addItem(dateTime);
			cancelAppDateList.addItem(dateTime);
		}
		updateAppDateList.setSelectedIndex(-1);
		cancelAppDateList.setSelectedIndex(-1);
		
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void makeAppointmentActionPerformed(ActionEvent evt) {
	
		error = null;
		success = null;
		
		try {
			
			String username = FlexiBookApplication.getCurrentUser().getUsername();
			
			int selectedService = makeAppServiceList.getSelectedIndex();
			if (selectedService < 0) {
				throw new InvalidInputException("Service needs to be selected for making appointment! ");
			}
			String mainServiceName = availableServices.get(selectedService);
			
			String startTime = makeAppTime.getText();
			String startDate = makeAppDate.getText();
			Date todaysDate = FlexiBookApplication.getSystemDate();
			
			FlexiBookController.makeAppointment(username, mainServiceName, null, startTime, startDate, todaysDate);
			success = "Appointment booked for " + mainServiceName + " on " + startDate + " at " + startTime; 
			
		}catch(InvalidInputException e) {
			error = e.getMessage();
		}
	
		//refresh data
		refreshDataForAppointmentBooking();
		
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void updateAppointmentActionPerformed(ActionEvent evt) {
		error = null;
		success = null;
		
		try {
			
			int selectedAppointment = updateAppDateList.getSelectedIndex();
			if(selectedAppointment == -1) {
				throw new InvalidInputException("Need to select an appointment to update!");
			}
			
			String username = FlexiBookApplication.getCurrentUser().getUsername();
			String startTime = existingAppointments.get(selectedAppointment).substring(11, 16);
			String startDate = existingAppointments.get(selectedAppointment).substring(0, 10);
			String newStartTime = updateAppNewTime.getText();
			String newStartDate = updateAppNewDate.getText();
			Date todaysDate = FlexiBookApplication.getSystemDate();
			int selectedService = updateAppServiceList.getSelectedIndex();
			
			if(selectedService != -1) {		//new service is selected
				
				String newService = availableServices.get(selectedService);
				if(!newStartDate.equals("") && !newStartTime.equals("")) {		// updating to a new time slot
					FlexiBookController.updateAppointmentTime(username, newStartTime, newStartDate, startTime, startDate, todaysDate, flexiBook);
					FlexiBookController.cancelAndBookNewService(username,  newService, null, newStartTime, newStartDate, todaysDate, flexiBook);
					success = "Appointment updated to  " + newStartDate + " at " + startTime + " for service " + newService; 
				}else {		// not updating to a new time slot
					FlexiBookController.cancelAndBookNewService(username, newService, null, startTime, startDate, todaysDate, flexiBook);
					success = "Appointment updated to new service " + newService; 
				}
				
			}
			else if((selectedService == -1) && !newStartDate.equals("") && !newStartTime.equals("")){			// new service not selected, new date and time selected
				FlexiBookController.updateAppointmentTime(username, newStartTime, newStartDate, startTime, startDate, todaysDate, flexiBook);
				success = "Appointment updated to " + newStartDate + " at " + startTime; 
			}
			else {		// nothing selected
				
				throw new InvalidInputException("Cannot update appointment without new information!");
				
			}
	
			
		}catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		//refresh data
		refreshDataForAppointmentBooking();
		
	}
	
	
	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void cancelAppointmentActionPerformed(ActionEvent evt) {
		error = null;
		success = null;
		
		try {
			
			int selectedAppointment = cancelAppDateList.getSelectedIndex();
			if(selectedAppointment == -1) {
				throw new InvalidInputException("Need to select an appointment to cancel!");
			}
			
			String username = FlexiBookApplication.getCurrentUser().getUsername();
			String startTime = existingAppointments.get(selectedAppointment).substring(11, 16);
			String startDate = existingAppointments.get(selectedAppointment).substring(0, 10);
			Date todaysDate = FlexiBookApplication.getSystemDate();
			
			FlexiBookController.cancelAppointment(username, startTime, startDate, todaysDate, null);
			success = "Appointment on " + startDate + " at " + startTime + " is cancelled."; 
			
		}catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		//refresh data
		refreshDataForAppointmentBooking();
	}
	
	
	//menu buttons
	
	
	private void bookAppActionPerformed(ActionEvent evt) {
		initAppointmentBookingPage();
	}
	
	
	private void manageAccountActionPerformed(ActionEvent evt) {
		//initManageAccountPage();
	}
	
	
	private void businessInfoActionPerformed(ActionEvent evt) {
		//initBusinessInfoPage();
	}
	
	
	private void manageServiceActionPerformed(ActionEvent evt) {
		//initManageServicePage();
	}
	
	
	private void manageAppActionPerformed(ActionEvent evt) {
		//initManageAppAcionPerformed();
	}
	
	
	private void viewCalenderActionPerformed(ActionEvent evt) {
		//initViewAppCalenderPage();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
