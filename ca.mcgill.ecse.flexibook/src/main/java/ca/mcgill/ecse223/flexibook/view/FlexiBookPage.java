package ca.mcgill.ecse223.flexibook.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellRenderer;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse223.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse223.flexibook.controller.TOAppointment;
import ca.mcgill.ecse223.flexibook.controller.TOService;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;

public class FlexiBookPage extends JFrame{

	private static final long serialVersionUID = -4426310869335015542L;
	private String error;
	private String success;

	// UI elements
	//JPanel panel = new JPanel();

	// message
	private JLabel message = new JLabel();

	//customer menu
	private JButton appBookingButton;
	private JButton manageCustomerAccountButton;

	//owner menu
	private JButton businessInfoButton;
	private JButton manageServiceButton;
	private JButton manageAppButton;
	private JButton viewAppCalenderButton;
	private JButton manageAppointmentStatusButton;

	//appointment overview
	private JTable overviewTable;
	private JLabel appOverviewLabel;
	private JButton makeAppBackButton;

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


	//init manage service page
	private JButton addServPageButton;
	private JButton updateServPageButton; 
	private JButton deleteServPageButton; 
	private JButton backToMainPage; 

	//add service
	private JTextField serviceName = new JTextField(); 
	private JLabel serviceNameLabel = new JLabel();
	private JTextField serviceDuration = new JTextField(); 
	private JLabel serviceDurationLabel = new JLabel(); 
	private JTextField downtimeStart = new JTextField();
	private JLabel downtimeStartLabel = new JLabel();
	private JTextField downtimeDuration = new JTextField();
	private JLabel downtimeDurationLabel = new JLabel();
	private JButton addServiceButton = new JButton();
	private JButton addServiceBackButton = new JButton();



	//update service 
	private JLabel updateExistingServiceLabel = new JLabel(); 
	private JButton updateServiceButton = new JButton();
	private JButton updateServiceBackButton = new JButton();
	private JTextField newServiceName = new JTextField();
	private JLabel newServiceNameLabel = new JLabel(); 
	private JTextField newServiceDuration = new JTextField();
	private JLabel newServiceDurationLabel = new JLabel();  
	private JTextField newDowntimeStart = new JTextField();
	private JLabel newDowntimeStartLabel = new JLabel(); 
	private JTextField newDowntimeDuration = new JTextField();
	private JLabel newDowntimeDurationLabel = new JLabel(); 
	private JComboBox<String> updateExistingService = new JComboBox<String>(new String[0]); 

	private JCheckBox updateServiceNameCheckBox = new JCheckBox();
	private JCheckBox updateServiceDowntimeStartCheckBox = new JCheckBox();
	private JCheckBox updateServiceDowntimeDurationCheckBox = new JCheckBox();
	private JCheckBox updateServiceDurationCheckBox = new JCheckBox();

	private HashMap<Integer, TOService> existingServices;


	//delete service 
	private JComboBox<String> deleteExistingService =  new JComboBox<String>(new String[0]);
	private JLabel deleteExistingServiceLabel; 
	private JButton deleteServiceButton;
	private JButton deleteServiceBackButton; 

	//appointment status page
	private JButton startAppointmentButton;
	private JButton endAppointmentButton;
	private JButton noShowButton;
	private JLabel appointmentListLabel;
	private JComboBox<String> appointmentList;
	private JButton backToMenuButton;
	
	

	//appointment data
	ArrayList<String> availableServices = new ArrayList<>();
	ArrayList<TOAppointment> existingAppointments = new ArrayList<>();


	//setupBusinessInfo
	private JTextField businessName = new JTextField();
	private JTextField businessAddress = new JTextField();
	private JTextField businessEmail = new JTextField();
	private JTextField businessPhone = new JTextField();


	public FlexiBookPage() {

		//if the user logged in is a customer 
		//initCustomerMenu();

		//else if the user is the owner
		initOwnerMenu();

		//manageServiceActionPerformed();

		//initAppointmentBookingPage();
	}

	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initOwnerMenu() {
		getContentPane().removeAll(); 
		getContentPane().repaint();

		//I added this to test the UI, will delete after if everything is working fine -- Sneha
		FlexiBookApplication.getFlexiBook().delete();

		businessInfoButton = new JButton();
		businessInfoButton.setText("Manage Business Information");
		manageServiceButton = new JButton();
		manageServiceButton.setText("Manage Services");
		manageAppButton = new JButton();
		manageAppButton.setText("Manage Appointments");
		viewAppCalenderButton = new JButton();
		viewAppCalenderButton.setText("View Appointment Calender");
		manageAppointmentStatusButton = new JButton();
		manageAppointmentStatusButton.setText("Manage Appointment Status");


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
						.addComponent(viewAppCalenderButton)
						.addComponent(manageAppointmentStatusButton))

				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessInfoButton, manageServiceButton, manageAppButton, viewAppCalenderButton, manageAppointmentStatusButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {businessInfoButton, manageServiceButton, manageAppButton, viewAppCalenderButton, manageAppointmentStatusButton});

		layout.setVerticalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(businessInfoButton)
						.addComponent(manageServiceButton)
						.addComponent(manageAppButton)
						.addComponent(viewAppCalenderButton)
						.addComponent(manageAppointmentStatusButton))
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

		manageAppointmentStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				managedAppointmentStatus(e);
			}
		});	



		pack();

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

		pack();
	}


	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initAppointmentBookingPage() {

		getContentPane().removeAll(); 
		getContentPane().repaint();
		refreshDataForAppointmentBooking();

		// elements for error message
		message = new JLabel();
		makeAppBackButton = new JButton();
		makeAppBackButton.setText("Back");

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

		makeAppBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initCustomerMenu();
			}
		});


		//overview table
		/*
        String[][] rowData = new String[existingAppointments.size()][3];
        for(int i = 0; i < existingAppointments.size(); i++) {
        	TOAppointment thisAppointment = existingAppointments.get(i);
        	rowData[i][0] = thisAppointment.getService();
        	rowData[i][1] = thisAppointment.getStartDate();
        	rowData[i][2] = thisAppointment.getStartTime();
        }
		 */
		appOverviewLabel = new JLabel();
		appOverviewLabel.setText("Your Appointments");
		String[][] rowData = {{"A", "B", "C"}, {"D", "E", "F"}};
		String[] columnNames = { "Service", "Date", "Start Time" };
		overviewTable = new JTable(rowData, columnNames);
		JScrollPane appointmentTable = new JScrollPane(overviewTable);



		// horizontal line elements
		JSeparator horizontalLineTop = new JSeparator();
		JSeparator horizontalLineMiddle = new JSeparator();
		JSeparator horizontalLineBottom = new JSeparator();
		JSeparator horizontalLineTable = new JSeparator();
		JSeparator horizontalLineLast = new JSeparator();


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
						.addComponent(horizontalLineTable)
						.addComponent(horizontalLineLast)
						.addComponent(makeAppBackButton, 200, 200, 400)
						.addComponent(appOverviewLabel)
						.addComponent(appointmentTable, 200, 200, 800)
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
						.addComponent(horizontalLineTable)
						.addComponent(appOverviewLabel)
						.addComponent(appointmentTable)
						.addComponent(horizontalLineLast)
						.addComponent(makeAppBackButton)
						)

				);

		pack();


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
		if(makeAppServiceList != null) {
			makeAppServiceList.removeAllItems();
		}
		if(updateAppServiceList != null) {
			updateAppServiceList.removeAllItems();
		}

		for (String thisService : availableServices) {
			makeAppServiceList.addItem(thisService);
			updateAppServiceList.addItem(thisService);
		};

		if(makeAppServiceList != null) {
			makeAppServiceList.setSelectedIndex(-1);
		}
		if(updateAppServiceList != null) {
			updateAppServiceList.setSelectedIndex(-1);
		}

		if(FlexiBookApplication.getCurrentUser() != null) {
			existingAppointments = FlexiBookController.getCustomerAppointments(FlexiBookApplication.getCurrentUser().getUsername());
			updateAppDateList.removeAllItems();
			cancelAppDateList.removeAllItems();
			for(TOAppointment appointment : existingAppointments) {
				String dateTime = appointment.getStartDate() + " " + appointment.getStartTime();
				updateAppDateList.addItem(dateTime);
				cancelAppDateList.addItem(dateTime);
			}
			updateAppDateList.setSelectedIndex(-1);
			cancelAppDateList.setSelectedIndex(-1);
		}


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
			Time currentTime = FlexiBookApplication.getSystemTime();

			FlexiBookController.makeAppointment(username, mainServiceName, null, startTime, startDate, todaysDate, currentTime);
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
			Time startTime = Time.valueOf(existingAppointments.get(selectedAppointment).getStartTime());
			String sTime = existingAppointments.get(selectedAppointment).getStartTime().substring(0,5);
			Date startDate = Date.valueOf(existingAppointments.get(selectedAppointment).getStartDate());
			String sDate = existingAppointments.get(selectedAppointment).getStartDate();
			String newStartTime = updateAppNewTime.getText();
			String newStartDate = updateAppNewDate.getText();
			Date todaysDate = FlexiBookApplication.getSystemDate();
			Time currentTime = FlexiBookApplication.getSystemTime();
			int selectedService = updateAppServiceList.getSelectedIndex();

			if(selectedService != -1) {		//new service is selected

				String newService = availableServices.get(selectedService);
				if(!newStartDate.equals("") && !newStartTime.equals("")) {		// updating to a new time slot
					FlexiBookController.updateAppointmentTime(username, newStartTime, newStartDate, startTime, startDate, todaysDate, currentTime);
					FlexiBookController.cancelAndBookNewService(username,  newService, null, newStartTime, newStartDate, todaysDate, currentTime);
					success = "Appointment updated to  " + newStartDate + " at " + startTime + " for service " + newService; 
				}else {		// not updating to a new time slot
					FlexiBookController.cancelAndBookNewService(username, newService, null, sTime, sDate, todaysDate, currentTime);
					success = "Appointment updated to new service " + newService; 
				}

			}
			else if((selectedService == -1) && !newStartDate.equals("") && !newStartTime.equals("")){			// new service not selected, new date and time selected
				FlexiBookController.updateAppointmentTime(username, newStartTime, newStartDate, startTime, startDate, todaysDate, currentTime);
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
			String startTime = existingAppointments.get(selectedAppointment).getStartTime().substring(0, 5);
			String startDate = existingAppointments.get(selectedAppointment).getStartDate();
			Date todaysDate = FlexiBookApplication.getSystemDate();

			FlexiBookController.cancelAppointment(username, startTime, startDate, todaysDate);
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
		initBusinessInfoPage();
	}

	private void initBusinessInfoPage() {
		getContentPane().removeAll();
		getContentPane().repaint();

		JButton setupBusinessInfoButton = new JButton();
		setupBusinessInfoButton.setText("Setup Business Info");
		JButton updateBusinessInfoButton = new JButton();
		updateBusinessInfoButton.setText("Update Business Info");
		JButton businessInfoBackButton = new JButton();
		businessInfoBackButton.setText("Back");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(setupBusinessInfoButton)
						.addComponent(updateBusinessInfoButton)
						.addComponent(businessInfoBackButton))
				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {setupBusinessInfoButton, updateBusinessInfoButton, businessInfoBackButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {setupBusinessInfoButton, updateBusinessInfoButton, businessInfoBackButton});

		layout.setVerticalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(setupBusinessInfoButton)
						.addComponent(updateBusinessInfoButton)
						.addComponent(businessInfoBackButton))
				);

		setupBusinessInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setupBusinessInfoActionPerformed(e);
			}

		});

		updateBusinessInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBusinessInfoActionPerformed(e);
			}

		});

		businessInfoBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initOwnerMenu();
			}

		});

		pack();
	}

	private void setupBusinessInfoActionPerformed(ActionEvent evt) {
		initSetupBusinessInfoPage();
	}

	private void updateBusinessInfoActionPerformed(ActionEvent evt) {
		initUpdateBusinessInfoPage();
	}

	private void initSetupBusinessInfoPage() {

		getContentPane().removeAll();
		getContentPane().repaint();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel businessNameLabel = new JLabel();
		JLabel businessAddressLabel = new JLabel();
		JLabel businessEmailLabel = new JLabel();
		JLabel businessPhoneLabel = new JLabel();

		JButton businessInfoBackButton = new JButton();
		JButton businessInfoSetInfoButton = new JButton();
		JButton businessInfoAddBusinessHourButton = new JButton();
		JButton businessInfoAddVacationButton = new JButton();
		JButton businessInfoAddHolidayButton = new JButton();


		businessNameLabel.setText("Business Name");
		businessAddressLabel.setText("Business Address");
		businessEmailLabel.setText("Business Email");
		businessPhoneLabel.setText("Business Phone Number");
		businessInfoBackButton.setText("Back");
		businessInfoSetInfoButton.setText("Set Business Info");
		businessInfoAddBusinessHourButton.setText("Add Business Hour");
		businessInfoAddVacationButton.setText("Add Vacation slot");
		businessInfoAddHolidayButton.setText("Add Holiday Slot");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(businessNameLabel)
						.addComponent(businessAddressLabel)
						.addComponent(businessEmailLabel)
						.addComponent(businessPhoneLabel)
						.addComponent(businessInfoBackButton)
						.addComponent(businessInfoSetInfoButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessName)
						.addComponent(businessAddress)
						.addComponent(businessEmail)
						.addComponent(businessPhone)
						.addComponent(businessInfoBackButton)
						.addComponent(businessInfoSetInfoButton))
				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessNameLabel, businessAddressLabel, businessEmailLabel, businessPhoneLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessName, businessAddress, businessEmail, businessPhone});
		layout.linkSize(SwingConstants.HORIZONTAL, businessInfoBackButton);


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(businessNameLabel)
						.addComponent(businessName))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessAddressLabel)
						.addComponent(businessAddress))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessEmailLabel)
						.addComponent(businessEmail))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessPhoneLabel)
						.addComponent(businessPhone))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoSetInfoButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoBackButton))
				);

		businessInfoBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initBusinessInfoPage();
			}
		});

		businessInfoSetInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setInfoButtonPressed(e);
			}
		});

		pack();
	}

	private void initUpdateBusinessInfoPage() {
		//TODO
	}

	private void setInfoButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String name = businessName.getText();
			String address = businessAddress.getText();
			String phoneNumber = businessPhone.getText();
			String email = businessEmail.getText();
			FlexiBookController.setupBusinessInfo(name, address, phoneNumber, email);
			success = "There is a Business named " + name + " at " + address + "with phone number: " + phoneNumber +" and email: " + email; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshSetupBusinessInfoPage();
	}

	private void refreshSetupBusinessInfoPage() {
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		businessName.setText("");
		businessAddress.setText("");
		businessPhone.setText("");
		businessEmail.setText("");

		pack();
	}



	private void manageAppActionPerformed(ActionEvent evt) {
		//initManageAppAcionPerformed();
	}



	private void viewCalenderActionPerformed(ActionEvent evt) {
		//initViewAppCalenderPage();
	}


	/**
	 * @author Sneha
	 * @param evt
	 */
	private void manageServiceActionPerformed (ActionEvent evt) {
		//addServicePage

		getContentPane().removeAll(); 
		getContentPane().repaint();


		JButton addServPageButton = new JButton();
		addServPageButton.setText("Add Service");
		JButton updateServPageButton = new JButton();
		updateServPageButton.setText("Update Service");
		JButton deleteServPageButton = new JButton();
		deleteServPageButton.setText("Delete Service");
		JButton manageServiceBackButton = new JButton();
		manageServiceBackButton.setText("Back");



		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(addServPageButton)
						.addComponent(updateServPageButton)
						.addComponent(deleteServPageButton)
						.addComponent(manageServiceBackButton))

				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {addServPageButton, deleteServPageButton, updateServPageButton, manageServiceBackButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addServPageButton, deleteServPageButton, updateServPageButton, manageServiceBackButton});

		layout.setVerticalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(addServPageButton)
						.addComponent(updateServPageButton)
						.addComponent(deleteServPageButton)
						.addComponent(manageServiceBackButton))
				);



		addServPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addServiceActionPerformed(e);
			}

		});

		updateServPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateServiceActionPerformed(e);
			}
		});

		deleteServPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteServiceActionPerformed(e);
			}
		});

		manageServiceBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initOwnerMenu();
			}
		});

		pack();
	}

	/**
	 * @author Sneha
	 * @param evt
	 */
	private void addServiceActionPerformed(ActionEvent evt) {
		error = "";
		success = ""; 

		getContentPane().removeAll(); 
		getContentPane().repaint();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		serviceNameLabel.setText("Service Name");
		serviceDurationLabel.setText("Service Duration (mins)");
		downtimeStartLabel.setText("Downtime Start (mins)");
		downtimeDurationLabel.setText("Downtime Duration (mins)");
		addServiceBackButton.setText("Back");
		addServiceButton.setText("Add Service");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(serviceNameLabel)
						.addComponent(serviceDurationLabel)
						.addComponent(downtimeStartLabel)
						.addComponent(downtimeDurationLabel)
						.addComponent(addServiceBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(serviceName)
						.addComponent(serviceDuration)
						.addComponent(downtimeStart)
						.addComponent(downtimeDuration)
						.addComponent(addServiceButton))
				);


		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {serviceNameLabel, serviceDurationLabel, downtimeStartLabel, downtimeDurationLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {serviceName, serviceDuration, downtimeStart, downtimeDuration});
		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addServPageButton, deleteServPageButton, updateServPageButton, manageServiceBackButton});

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(serviceNameLabel)
						.addComponent(serviceName))
				.addGroup(layout.createParallelGroup()
						.addComponent(serviceDurationLabel)
						.addComponent(serviceDuration))
				.addGroup(layout.createParallelGroup()
						.addComponent(downtimeStartLabel)
						.addComponent(downtimeStart))
				.addGroup(layout.createParallelGroup()
						.addComponent(downtimeDurationLabel)
						.addComponent(downtimeDuration))
				.addGroup(layout.createParallelGroup()
						.addComponent(addServiceBackButton)
						.addComponent(addServiceButton))
				);



		addServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addServiceButtonPressed(e);
			}

		});

		addServiceBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageServiceActionPerformed(e);
			}
		});

		//refreshServicePage();
		//resize page to fit all components 
		pack();
	}


	/**
	 * @author Sneha
	 * @param evt
	 */
	private void addServiceButtonPressed (ActionEvent evt) {
		error = "";
		success = ""; 
		String name = null;
		int duration = 0 ;
		int dtDuration = 0; 
		int dtStart = 0;
		name = serviceName.getText();

		if (serviceName.getText().length() == 0) {
			error = "Please enter a name.";
		}

		//duration validation check
		if (error == null || error.length() == 0) {
			try {
				duration = Integer.parseInt(serviceDuration.getText());
			}
			catch (NumberFormatException e) {
				error = "Please enter a valid service duration.";
			}
		}

		//downtime start validation check
		if (error == null || error.length() == 0) {
			try {

				dtStart = Integer.parseInt(downtimeStart.getText());
			}
			catch (NumberFormatException e) {
				error = "Please enter a valid downtime start.";
			}
		}

		//downtime duration validation check
		if (error == null || error.length() == 0) {
			try {

				dtDuration = Integer.parseInt(downtimeDuration.getText());

			}
			catch (NumberFormatException e) {
				error = "Please enter a valid downtime duration.";
			}
		}



		if (error == null || error.length() == 0) {

			try {
				//call controller 
				FlexiBookController.addService(name, duration, dtDuration, dtStart);
				success = "Service " +name+ " successfully added";
			}

			catch (InvalidInputException ie){
				error = ie.getMessage();

			}
		}

		refreshServicePage();

		//FlexiBookPersistence.save(flexiBook);
		pack();
	}

	/**
	 * @author Sneha
	 * @param evt
	 */
	private void updateServiceActionPerformed (ActionEvent evt) {
		error = "";
		success = ""; 

		getContentPane().removeAll(); 
		getContentPane().repaint();
		//getContentPane().setSize(dim);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//page components


		newServiceNameLabel.setText("New name:");
		newServiceDurationLabel.setText("New duration (mins):");
		newDowntimeStartLabel.setText("New downtime start (mins):");
		newDowntimeDurationLabel.setText("New downtime duration (mins):");
		updateExistingServiceLabel.setText("Choose service to update:");
		updateServiceButton.setText("Update Service");
		updateServiceBackButton.setText("Back");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);


		layout.setHorizontalGroup(
				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(updateExistingServiceLabel)
						.addComponent(newServiceNameLabel)
						.addComponent(newServiceDurationLabel)
						.addComponent(newDowntimeStartLabel)
						.addComponent(newDowntimeDurationLabel)
						.addComponent(updateServiceBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateServiceNameCheckBox)
						.addComponent(updateServiceDurationCheckBox)
						.addComponent(updateServiceDowntimeStartCheckBox)
						.addComponent(updateServiceDowntimeDurationCheckBox))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateExistingService)
						.addComponent(newServiceName)
						.addComponent(newServiceDuration)
						.addComponent(newDowntimeStart)
						.addComponent(newDowntimeDuration)
						.addComponent(updateServiceButton))
				);
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateServiceNameCheckBox, updateServiceDurationCheckBox, updateServiceDowntimeStartCheckBox, updateServiceDowntimeDurationCheckBox});

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateExistingServiceLabel, newServiceNameLabel, newServiceDurationLabel, newDowntimeStartLabel, newDowntimeDurationLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newServiceName, newServiceDuration, newDowntimeStart, newDowntimeDuration});
		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addServPageButton, deleteServPageButton, updateServPageButton, manageServiceBackButton});

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(updateExistingServiceLabel)
						.addComponent(updateExistingService))
				.addGroup(layout.createParallelGroup()
						.addComponent(newServiceNameLabel)
						.addComponent(updateServiceNameCheckBox)
						.addComponent(newServiceName))
				.addGroup(layout.createParallelGroup()
						.addComponent(newServiceDurationLabel)
						.addComponent(updateServiceDurationCheckBox)
						.addComponent(newServiceDuration))
				.addGroup(layout.createParallelGroup()
						.addComponent(newDowntimeStartLabel)
						.addComponent(updateServiceDowntimeStartCheckBox)
						.addComponent(newDowntimeStart))
				.addGroup(layout.createParallelGroup()
						.addComponent(newDowntimeDurationLabel)
						.addComponent(updateServiceDowntimeDurationCheckBox)
						.addComponent(newDowntimeDuration))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateServiceBackButton)
						.addComponent(updateServiceButton))
				);



		updateServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateServiceButtonPressed(e);
			}

		});

		updateServiceBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				manageServiceActionPerformed(e);
			}
		});

		refreshServicePage();
		//resize page to fit all components 
		pack();
	}

	private void updateServiceButtonPressed (ActionEvent evt) {
		error = "";
		success = ""; 
		
		int selectedService = updateExistingService.getSelectedIndex();
		
		if (selectedService < 0) {
			error = "Please select a service to update."; 
			refreshServicePage();
		}

		
		//original service parameters:
		String name = null; 
		String newName = null;
		int duration = 0;
		int dtDur = 0; 
		int dtStart = 0;
		
		if (error == null || error.length() == 0) {
			name = existingServices.get(selectedService).getServiceName();
			newName = existingServices.get(selectedService).getServiceName();
			duration = existingServices.get(selectedService).getServiceDur();
			dtDur = existingServices.get(selectedService).getDowntimeDur();
			dtStart = existingServices.get(selectedService).getDowntimeDurStart();
		}
		
		if (updateServiceNameCheckBox.isSelected()) {
			if(newServiceName.getText().length() == 0) {
				error = "Please enter a name.";
			}
			newName = newServiceName.getText();
		}

		//duration validation check
		if ((error == null || error.length() == 0) && updateServiceDurationCheckBox.isSelected()) {
			try {
				duration = Integer.parseInt(newServiceDuration.getText());
			}
			catch (NumberFormatException e) {
				error = "Please enter a valid service duration.";
			}
		}

		//downtime start validation check
		if ((error == null || error.length() == 0) && updateServiceDowntimeStartCheckBox.isSelected()) {
			try {
				dtStart = Integer.parseInt(newDowntimeStart.getText());
			}
			catch (NumberFormatException e) {
				error = "Please enter a valid downtime start.";
			}
		}

		//downtime duration validation check
		if ((error == null || error.length() == 0) && updateServiceDowntimeDurationCheckBox.isSelected()) {
			try {

				dtDur = Integer.parseInt(newDowntimeDuration.getText());

			}
			catch (NumberFormatException e) {
				error = "Please enter a valid downtime duration.";
			}
		}


		if (error == null || error.length() == 0) {
			try {
				//call controller 
				FlexiBookController.updateService(name, newName, duration, dtDur, dtStart);
				success = "Service " +name+ " successfully updated";
				refreshServicePage();
			}

			catch (InvalidInputException ie){
				error = ie.getMessage();

			}
		}

		refreshServicePage();

		//FlexiBookPersistence.save(flexiBook);
		pack();
	}


	/**
	 * @author Sneha
	 * @param evt
	 */
	private void deleteServiceActionPerformed (ActionEvent evt) {
		//error = "";
		error = "";
		success = ""; 

		getContentPane().removeAll(); 
		getContentPane().repaint();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//page components

		JLabel deleteExistingServiceLabel = new JLabel();
		JButton deleteServiceButton = new JButton();
		JButton deleteServiceBackButton = new JButton();

		deleteExistingServiceLabel.setText("Choose service to delete:");
		deleteServiceButton.setText("Delete Service");
		deleteServiceBackButton.setText("Back");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);


		layout.setHorizontalGroup(
				layout.createSequentialGroup()

				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(deleteExistingServiceLabel)
						.addComponent(deleteServiceBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(deleteExistingService)
						.addComponent(deleteServiceButton))
				);


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(deleteExistingServiceLabel)
						.addComponent(deleteExistingService))
				.addGroup(layout.createParallelGroup()
						.addComponent(deleteServiceBackButton)
						.addComponent(deleteServiceButton))

				);



		deleteServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteServiceButtonPressed(e);
			}

		});

		deleteServiceBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				manageServiceActionPerformed(e);
			}
		});

		refreshServicePage();
		//resize page to fit all components 
		pack();
	}


	/**
	 * @author Sneha
	 * @param evt
	 */
	private void deleteServiceButtonPressed (ActionEvent evt) {
		error = "";
		success = ""; 

		int selectedService = deleteExistingService.getSelectedIndex(); 
		if (selectedService < 0) {
			error = "Please select a service to delete."; 
			refreshServicePage();
		}

		if (error == null || error.length() == 0) {
			try {
				FlexiBookController.deleteService(existingServices.get(selectedService).getServiceName());
				success = "Service " + existingServices.get(selectedService).getServiceName() + " deleted successfully" ;

			}
			catch (InvalidInputException e){
				error = e.getMessage();
			}
		}

		refreshServicePage();
		pack();
	}

	/**
	 * @author Sneha
	 */
	private void refreshServicePage() {
		if (error != null && error.length() > 0) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		if (error == null || error.length() == 0) {
		
			//reset addService parameters
			serviceName.setText("");
			serviceDuration.setText("");
			downtimeDuration.setText("");
			downtimeStart.setText("");

			//reset updateService parameters
			newServiceName.setText("");
			newServiceDuration.setText("");
			newDowntimeDuration.setText("");
			newDowntimeStart.setText("");
			updateServiceDurationCheckBox.setSelected(false);
			updateServiceDowntimeDurationCheckBox.setSelected(false);
			updateServiceDowntimeStartCheckBox.setSelected(false);
			updateServiceNameCheckBox.setSelected(false);

			

			existingServices = new HashMap<Integer, TOService>();
			updateExistingService.removeAllItems();
			deleteExistingService.removeAllItems();
			int index = 0; 

			for (TOService service: FlexiBookController.getExistingServices()) {
				existingServices.put(index, service);

				updateExistingService.addItem("Service: " + service.getServiceName() + " | Duration: " + service.getServiceDur() + " | Downtime Start: " 
						+ service.getDowntimeDurStart() + " | Downtime Duration: " +service.getDowntimeDur());

				deleteExistingService.addItem("Service: " + service.getServiceName() + " | Duration: " + service.getServiceDur() + " | Downtime Start: " 
						+ service.getDowntimeDurStart() + " | Downtime Duration: " +service.getDowntimeDur());
				index++;
			}
			updateExistingService.setSelectedIndex(-1);
			deleteExistingService.setSelectedIndex(-1);
		}

		pack();
	}

	/**
	 * @author yasminamatta
	 * @param evt
	 */
	private void managedAppointmentStatus(ActionEvent evt) {
		getContentPane().removeAll(); 
		getContentPane().repaint();
		error="";
		success ="";
		
		
		
		
		
		startAppointmentButton = new JButton();
		startAppointmentButton.setText("Start Appointment");
		endAppointmentButton = new JButton();
		endAppointmentButton.setText("End Appointment");
		noShowButton = new JButton();
		noShowButton.setText("No Show");
		backToMenuButton = new JButton();
		backToMenuButton.setText("Back");
		appointmentList = new JComboBox<String>(new String[0]); 
		appointmentListLabel = new JLabel();
		appointmentListLabel.setText("Choose an appointment: ");
		
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
							.addComponent(message)
							.addComponent(appointmentListLabel)			
							.addComponent(startAppointmentButton)	
					.addComponent(backToMenuButton))
					.addGroup(layout.createParallelGroup()
							.addComponent(appointmentList)
							.addComponent(endAppointmentButton))
					.addGroup(layout.createParallelGroup()
							.addComponent(noShowButton))
//					.addGroup(layout.createSequentialGroup()
							

							
					
				);
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
//		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		//layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList});
		layout.linkSize(SwingConstants.VERTICAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
//		
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(message))
				.addGroup(layout.createParallelGroup()
						.addComponent(appointmentListLabel)
						.addComponent(appointmentList))
				.addGroup(layout.createParallelGroup()
						.addComponent(startAppointmentButton)
						.addComponent(endAppointmentButton)				
						.addComponent(noShowButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(backToMenuButton))

					
				);
		 
		
		startAppointmentButton.addActionListener(new ActionListener(){ 
			
			public void actionPerformed(ActionEvent e) {
				
					startAppointmentButtonPressed(e);		
			}
		});
		
		endAppointmentButton.addActionListener(new ActionListener(){ 
			
			public void actionPerformed(ActionEvent e) {
				
					endAppointmentButtonPressed(e);	
			}
		});

		noShowButton.addActionListener(new ActionListener(){ 
	
			public void actionPerformed(ActionEvent e) {
					registerNoShowButtonPressed(e);
			}
		});
		
		
		backToMenuButton.addActionListener(new ActionListener(){ 
			
			public void actionPerformed(ActionEvent e) {
				initOwnerMenu();
		
			}
		});
	pack();
		
	}
	
	/**
	 * @author yasminamatta
	 * @param evt
	 * @throws InvalidInputException 
	 */
	private void startAppointmentButtonPressed(ActionEvent evt) {
		int selectedAppointment = appointmentList.getSelectedIndex();
			if(selectedAppointment == -1) {
				error = "Please enter an appointment";
			}
			if(error.isEmpty()) {
			try {
			FlexiBookController.startAppointment(existingAppointments.get(selectedAppointment).getCustomerName(),
					existingAppointments.get(selectedAppointment).getStartTime(),
					existingAppointments.get(selectedAppointment).getStartDate(),
					FlexiBookApplication.getSystemDate(),
					FlexiBookApplication.getSystemTime());
			success = "You have successfully started the appointment " + existingAppointments.get(selectedAppointment).getService() + "with the customer" + existingAppointments.get(selectedAppointment).getCustomerName();

			}
			catch(InvalidInputException e) {
				error += e.getMessage();
			}
			}
			refreshAppointmentStatusPage();
			
	}

	/**
	 * @author yasminamatta
	 * @param evt
	 * @throws InvalidInputException 
	 */
	private void endAppointmentButtonPressed(ActionEvent evt) {
		int selectedAppointment = appointmentList.getSelectedIndex();
		
		if(selectedAppointment == -1) {
			error = "Please enter an appointment";
		}
		if(error.isEmpty()) {
		try {
		FlexiBookController.endAppointment(existingAppointments.get(selectedAppointment).getCustomerName(),
				existingAppointments.get(selectedAppointment).getStartTime(),
				existingAppointments.get(selectedAppointment).getStartDate(),
				FlexiBookApplication.getSystemDate(),
				FlexiBookApplication.getSystemTime());
		success = "You have successfully ended the appointment " + existingAppointments.get(selectedAppointment).getService() + "with the customer" + existingAppointments.get(selectedAppointment).getCustomerName();

		}
		catch(InvalidInputException e) {
			error += e.getMessage();
		}
		}
		refreshAppointmentStatusPage();
	}



	/**
	 * @author yasminamatta
	 * @param evt
	 * @throws InvalidInputException 
	 */
	private void registerNoShowButtonPressed(ActionEvent evt) {
			int selectedAppointment = appointmentList.getSelectedIndex();
		
		if(selectedAppointment == -1) {
			error = "Please enter an appointment";
		}
		if(error.isEmpty()) {
		try {
		FlexiBookController.registerNoShow(existingAppointments.get(selectedAppointment).getCustomerName(), 
				existingAppointments.get(selectedAppointment).getStartDate().toString(),
				existingAppointments.get(selectedAppointment).getStartTime().toString(),
				FlexiBookApplication.getSystemDate(),
				FlexiBookApplication.getSystemTime());
		success = "You have successfully registered a no show for the appointment " + existingAppointments.get(selectedAppointment).getService() + "with the customer" + existingAppointments.get(selectedAppointment).getCustomerName();
				
		}
		catch(InvalidInputException e) {
			error += e.getMessage();
		}
		}
	refreshAppointmentStatusPage();
	}
	private void refreshAppointmentStatusPage() {
		
		message.setForeground(Color.RED);
		if(!error.isEmpty()) {
			message.setText(error);
		}

		if(!success.isEmpty()) {
			message.setText(success);
		}
		if(error.isEmpty()) {
			appointmentList.removeAllItems();
		}
		pack();
	}



	}






