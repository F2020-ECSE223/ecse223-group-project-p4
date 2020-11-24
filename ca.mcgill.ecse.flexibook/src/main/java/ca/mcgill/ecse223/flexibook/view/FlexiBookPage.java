package ca.mcgill.ecse223.flexibook.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse223.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse223.flexibook.controller.TOAppointment;
import ca.mcgill.ecse223.flexibook.controller.TOService;

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
	private JButton customerLogOutButton;

	//owner menu
	private JButton businessInfoButton;
	private JButton manageServiceButton;
	private JButton viewAppCalenderButton;
	private JButton manageAppointmentStatusButton;
	private JButton ownerLogOutButton;

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
	private JDatePickerImpl appointmentDatePicker;
	private JLabel appointmentDateLabel;
	private ArrayList<TOAppointment> appointmentWithSpecificDate = new ArrayList<TOAppointment>();

	//appointment data
	ArrayList<String> availableServices = new ArrayList<>();
	ArrayList<TOAppointment> existingAppointments = new ArrayList<>();


	//setupBusinessInfo
	private JTextField setBusinessName = new JTextField();
	private JTextField setBusinessAddress = new JTextField();
	private JTextField setBusinessEmail = new JTextField();
	private JTextField setBusinessPhone = new JTextField();
	

	//addVacationSlot
	private JTextField startVacationDate = new JTextField();
	private JTextField startVacationTime = new JTextField();
	private JTextField endVacationDate = new JTextField();
	private JTextField endVacationTime = new JTextField();
	
	//addHolidaySlot
	private JTextField startHolidayDate = new JTextField();
	private JTextField startHolidayTime = new JTextField();
	private JTextField endHolidayDate = new JTextField();
	private JTextField endHolidayTime = new JTextField();
	
	//addBusinessHour
	private JTextField addBusinessHourDay = new JTextField();
	private JTextField addBusinessHourStart = new JTextField();
	private JTextField addBusinessHourEnd = new JTextField();
	
	//updateBusinessInfo
	private JTextField updateBusinessName = new JTextField();
	private JTextField updateBusinessAddress = new JTextField();
	private JTextField updateBusinessEmail = new JTextField();
	private JTextField updateBusinessPhone = new JTextField();
	
	//Update owner account
	
	private JTextField newOwnerPassword = new JTextField();
	private JTextField oldOwnerUsername = new JTextField();
	private JLabel oldOwnerUsernameLabel;
	
	private JLabel newOwnerPasswordLabel;
	private JButton updateOwnerButton;
	private JButton backOwnerButton;
	private JButton ownerUpdateButton;
	
	//update user account
	private JTextField oldCustomerUsername = new JTextField();
	private JTextField newCustomerUsername = new JTextField();
	private JTextField newCustomerPassword = new JTextField();
	private JButton updateCustomerButton;
	private JLabel oldCustomerUsernameLabel;
	private JLabel newCustomerPasswordLabel = new JLabel();
	private JLabel newCustomerUsernameLabel = new JLabel();
	private JButton backCustomerButton;
	//login
	private JTextField usernameLogin;
    private JTextField passwordLogin;
    private JLabel errorMessage;
    private JTextField passwordSignup;
    private JTextField usernameSignup;
    
  
	

	public FlexiBookPage() {
		
		initializeLoginPage();
	
	}
	
	
	
	/**
     * Initialize the contents of the frame.
     */
    private void initializeLoginPage() {
    	getContentPane().removeAll(); 
		getContentPane().repaint();
       
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBounds(100, 100, 700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        panel.setBackground(Color.RED);
        panel.setPreferredSize(new Dimension(1000, 1000));

        getContentPane().add(panel, BorderLayout.NORTH);


        JLabel lblWelcomeToBlock = new JLabel("Welcome to FlexiBook");
        lblWelcomeToBlock.setBackground(Color.BLACK);
        lblWelcomeToBlock.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        lblWelcomeToBlock.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLogin = new JTextField();
        usernameLogin.setHorizontalAlignment(SwingConstants.LEFT);
        usernameLogin.setColumns(10);

        passwordLogin = new JPasswordField();
        passwordLogin.setColumns(10);

        JLabel loginUsername = new JLabel("Username : ");
        loginUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 10));

        JLabel loginPassword = new JLabel("Password : ");
        loginPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 10));

        JLabel signupPassword = new JLabel("Password : ");
        signupPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 10));

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Lucida Grande", Font.BOLD, 13));

        JLabel lblSignup = new JLabel("Sign Up");
        lblSignup.setFont(new Font("Lucida Grande", Font.BOLD, 13));

        JLabel lblDesignedByTeam = new JLabel("Designed by Team P04");

        passwordSignup = new JPasswordField();
        passwordSignup.setColumns(10);

        usernameSignup = new JTextField();
        usernameSignup.setColumns(10);

        JLabel Username = new JLabel("Username :");
        Username.setFont(new Font("Lucida Grande", Font.PLAIN, 10));


        errorMessage = new JLabel("     ");
        errorMessage.setForeground(Color.RED);

        JButton login = new JButton("Login");

        JButton signup = new JButton("Sign Up");

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        
        signup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                signupActionPerformed(evt);
            }
        });

        //layout

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(117)
                                                .addComponent(lblLogin)
                                                .addGap(357)
                                                .addComponent(lblSignup, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblWelcomeToBlock, GroupLayout.PREFERRED_SIZE, 687, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(18)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGap(9)
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                                .addComponent(loginUsername)
                                                                                .addGap(18)
                                                                                .addComponent(usernameLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                                .addComponent(loginPassword)
                                                                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(passwordLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(login, Alignment.TRAILING))
                                                                .addPreferredGap(ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                                        .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                                                .addComponent(signupPassword, Alignment.LEADING)
                                                                                .addComponent(Username, Alignment.LEADING))))
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGap(276)
                                                                .addComponent(lblDesignedByTeam)))
                                                .addGap(36)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                                .addComponent(usernameSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                                                .addComponent(passwordSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(signup, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(errorMessage, GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)))
                                )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(14)
                                .addComponent(lblWelcomeToBlock, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(lblDesignedByTeam)
                                                .addGap(26)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(lblSignup)
                                                        .addComponent(lblLogin))
                                                .addGap(80)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(loginPassword)
                                                        .addComponent(passwordLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(signupPassword)
                                                        .addComponent(passwordSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(ComponentPlacement.RELATED))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(usernameLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(loginUsername)
                                                        .addComponent(usernameSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Username, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE))
                                                .addGap(53)))
                                .addGap(13)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(login)
                                        .addComponent(signup))
                                .addGap(76)
                                .addComponent(errorMessage)
                                .addGap(557))
        );
        getContentPane().setLayout(groupLayout);
        
        

    }

    private void refreshLoginData() {
        errorMessage.setText(error);
        if (error == null || error.length() == 0) {
            usernameSignup.setText("");
            passwordSignup.setText("");

            usernameLogin.setText("");
            passwordLogin.setText("");
        }
    }
    
    
    private void signupActionPerformed(ActionEvent evt) {
        error = "";
        
        if (error.length() == 0) {

            String username = usernameSignup.getText();
            String Password = passwordSignup.getText();
            try {
            	//if (username.equals("owner") && Password.contentEquals("owner")) error = "owner cannot sign up";
            	FlexiBookController.signUpCustomer(username, Password);
            } catch (InvalidInputException e) {
                error = e.getMessage();
            }
        }
        refreshLoginData();
    }

    

   private void loginActionPerformed(ActionEvent evt) {

 
            String username = usernameLogin.getText();
            String password = passwordLogin.getText();
            
            try {
            	//FlexiBookApplication.setCurrentUser(null);
            	
                FlexiBookController.logIn(username, password);
                if(FlexiBookApplication.getCurrentUserType().equals("owner")) {
        			initOwnerMenu();
        		}
        		else if(FlexiBookApplication.getCurrentUserType().equals("customer")) {
        			initCustomerMenu();
        		}

            } catch (InvalidInputException e) {
                error = e.getMessage();
            }
        
        
        refreshLoginData();

    }
	
	
	
	

	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initOwnerMenu() {
		getContentPane().removeAll(); 
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);

		//I added this to test the UI, will delete after if everything is working fine -- Sneha
	

		businessInfoButton = new JButton();
		businessInfoButton.setText("Manage Business Information");
	
		manageServiceButton = new JButton();
		manageServiceButton.setText("Manage Services");

		viewAppCalenderButton = new JButton();
		viewAppCalenderButton.setText("View Appointment Calender");

		manageAppointmentStatusButton = new JButton();
		manageAppointmentStatusButton.setText("Manage Appointment Status");
//	
		updateOwnerButton = new JButton();
		updateOwnerButton.setText("Update Owner Account");
//		
		ownerLogOutButton = new JButton();
		ownerLogOutButton.setText("Log Out");
		


		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(

				layout.createParallelGroup()
			
				.addGroup(layout.createSequentialGroup()
						.addGap(120)
						.addGroup(layout.createParallelGroup()
								.addComponent(businessInfoButton)
								.addComponent(manageServiceButton)
								.addComponent(viewAppCalenderButton))
			
						.addGroup(layout.createParallelGroup()
							
								.addComponent(manageAppointmentStatusButton)
								.addComponent(updateOwnerButton)
								.addComponent(ownerLogOutButton)))
						

				);
		


		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessInfoButton, manageServiceButton, viewAppCalenderButton, manageAppointmentStatusButton,updateOwnerButton, ownerLogOutButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {businessInfoButton, manageServiceButton, viewAppCalenderButton, manageAppointmentStatusButton, updateOwnerButton, ownerLogOutButton});

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()		
										.addGap(75)
										.addComponent(businessInfoButton)
										.addComponent(manageServiceButton)
										.addComponent(viewAppCalenderButton))
										
								.addGroup(layout.createSequentialGroup()
						
										.addGap(75)
										.addComponent(manageAppointmentStatusButton)
										.addComponent(updateOwnerButton)
										.addComponent(ownerLogOutButton))

				)
							
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
		
		ownerLogOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoutActionPerformed(e);
			}
		});	
		
		updateOwnerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manageOwnerAccountActionPerformed(e);
			}
		});
		



		//pack();

	}


	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initCustomerMenu() {
		
		getContentPane().removeAll(); 
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);
		
		appBookingButton = new JButton();
		appBookingButton.setText("Appoinment booking");
//		manageCustomerAccountButton = new JButton();
//		manageCustomerAccountButton.setText("Manage Account");
		updateCustomerButton = new JButton();
		updateCustomerButton.setText("Update customer account");
		customerLogOutButton = new JButton();
		customerLogOutButton.setText("Log Out");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGap(230)
						.addGroup(layout.createParallelGroup()
						
						.addComponent(appBookingButton)
						.addComponent(updateCustomerButton)
						.addComponent(customerLogOutButton)))

				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appBookingButton, updateCustomerButton, customerLogOutButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appBookingButton, updateCustomerButton, customerLogOutButton});

		layout.setVerticalGroup(
				layout.createSequentialGroup()
				
				.addGroup(layout.createParallelGroup()
						
						.addGroup(layout.createSequentialGroup()
								.addGap(70)
						.addComponent(appBookingButton)
						.addComponent(updateCustomerButton)
						.addComponent(customerLogOutButton)))
				);



		appBookingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookAppActionPerformed(e);
			}
		});
		
		customerLogOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logoutActionPerformed(e);
			}
		});	
		
		updateCustomerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCustomerAccount();
			}
		});

		//pack();
	}
	
	
	
	
	private void logoutActionPerformed(ActionEvent evt) {
		
		try {
			FlexiBookController.logOut();
			initializeLoginPage();
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
	}
	


	/**
	 * @author Shaswata
	 * @param evt
	 */
	private void initAppointmentBookingPage() {

		getContentPane().removeAll(); 
		getContentPane().repaint();

		//refreshDataForAppointmentBooking();
		setBounds(100, 100, 700, 300);

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

		refreshDataForAppointmentBooking();
		

		//overview table
		String[][] rowData = new String[existingAppointments.size()][3];
		if(!existingAppointments.isEmpty()) {
	        for(int i = 0; i < existingAppointments.size(); i++) {
	        	TOAppointment thisAppointment = existingAppointments.get(i);
	        	rowData[i][0] = thisAppointment.getService();
	        	rowData[i][1] = thisAppointment.getStartDate();
	        	rowData[i][2] = thisAppointment.getStartTime();
	        }
		}
        
		
		appOverviewLabel = new JLabel();
		appOverviewLabel.setText("Your Appointments");
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
		initAppointmentBookingPage();

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
		initAppointmentBookingPage();

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
		initAppointmentBookingPage();
	}



	//menu buttons
	private void bookAppActionPerformed(ActionEvent evt) {
		initAppointmentBookingPage();
	}


	private void manageOwnerAccountActionPerformed(ActionEvent evt) {
		updateOwnerAccount();
	}

	private void businessInfoActionPerformed(ActionEvent evt) {
		initBusinessInfoPage();
	}

	private void initBusinessInfoPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);
		
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
				.addGap(250)
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
						.addGap(75)
						.addComponent(setupBusinessInfoButton)
						.addGap(15)
						.addComponent(updateBusinessInfoButton)
						.addGap(15)
						.addComponent(businessInfoBackButton))
				);

		setupBusinessInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initSetupBusinessInfoPage();
			}
		});

		updateBusinessInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initUpdateBusinessInfoPage();
			}

		});

		businessInfoBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initOwnerMenu();
			}
		});

	//	pack();
	}

	private void initSetupBusinessInfoPage() {
		
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);
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
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(businessNameLabel)
						.addComponent(businessAddressLabel)
						.addComponent(businessEmailLabel)
						.addComponent(businessPhoneLabel)
						.addComponent(businessInfoAddVacationButton)
						.addComponent(businessInfoAddHolidayButton)
						.addComponent(businessInfoAddBusinessHourButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(setBusinessName)
						.addComponent(setBusinessAddress)
						.addComponent(setBusinessEmail)
						.addComponent(setBusinessPhone)
						.addComponent(businessInfoBackButton)
						.addComponent(businessInfoSetInfoButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessNameLabel, businessAddressLabel, businessEmailLabel, businessPhoneLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {setBusinessName, setBusinessAddress, setBusinessEmail, setBusinessPhone});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {businessInfoAddHolidayButton, businessInfoAddVacationButton, businessInfoSetInfoButton, businessInfoBackButton, businessInfoAddBusinessHourButton, setBusinessName, setBusinessAddress, setBusinessEmail, setBusinessPhone});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(25)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						
						.addComponent(businessNameLabel)
						.addComponent(setBusinessName))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessAddressLabel)
						.addComponent(setBusinessAddress))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessEmailLabel)
						.addComponent(setBusinessEmail))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessPhoneLabel)
						.addComponent(setBusinessPhone))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoAddVacationButton)
						.addComponent(businessInfoSetInfoButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoAddHolidayButton)
						.addComponent(businessInfoBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoAddBusinessHourButton))
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
		
		businessInfoAddVacationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAddVacationPage();
			}
		});
		
		businessInfoAddHolidayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAddHolidayPage();
			}
		});
		
		businessInfoAddBusinessHourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAddBusinessHourPage();
			}
		});

		//pack();
	}

	

	private void setInfoButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String name = setBusinessName.getText();
			String address = setBusinessAddress.getText();
			String phoneNumber = setBusinessPhone.getText();
			String email = setBusinessEmail.getText();
			FlexiBookController.setupBusinessInfo(name, address, phoneNumber, email);
			success = "There is a Business named " + name + " at " + address + "with phone number: " + phoneNumber +" and email: " + email; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshSetupBusinessInfoPage();
	}

	private void refreshSetupBusinessInfoPage() {
		setBounds(100, 100, 700, 300);

		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		setBusinessName.setText("");
		setBusinessAddress.setText("");
		setBusinessPhone.setText("");
		setBusinessEmail.setText("");

		//pack();
	}
	
	private void initAddVacationPage() {
		getContentPane().removeAll();
		getContentPane().repaint();

		setBounds(100, 100, 700, 300);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel startVacationDateLabel = new JLabel();
		JLabel startVacationTimeLabel = new JLabel();
		JLabel endVacationDateLabel = new JLabel();
		JLabel endVacationTimeLabel = new JLabel();
		
		JButton addVacationBackButton = new JButton();
		JButton addVacationAddButton = new JButton();



		startVacationDateLabel.setText("Start Date");
		startVacationTimeLabel.setText("Start Time");
		endVacationDateLabel.setText("End Date");
		endVacationTimeLabel.setText("End Time");
		addVacationBackButton.setText("Back");
		addVacationAddButton.setText("Add Vacation Slot");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(startVacationDateLabel)
						.addComponent(startVacationTimeLabel)
						.addComponent(endVacationDateLabel)
						.addComponent(endVacationTimeLabel)
						.addComponent(addVacationBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(startVacationDate)
						.addComponent(startVacationTime)
						.addComponent(endVacationDate)
						.addComponent(endVacationTime)
						.addComponent(addVacationAddButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {startVacationDateLabel, startVacationTimeLabel, endVacationDateLabel, endVacationTimeLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {startVacationDate, startVacationTime, endVacationDate, endVacationTime});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addVacationBackButton, addVacationAddButton, startVacationDate, startVacationTime, endVacationDate, endVacationTime});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(startVacationDateLabel)
						.addComponent(startVacationDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(startVacationTimeLabel)
						.addComponent(startVacationTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(endVacationDateLabel)
						.addComponent(endVacationDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(endVacationTimeLabel)
						.addComponent(endVacationTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(addVacationBackButton)
						.addComponent(addVacationAddButton))
				
				);
		

		
		addVacationBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initSetupBusinessInfoPage();
			}
		});
		
		addVacationAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVacationButtonPressed(e);
			}
		});

	//	pack();
	}
	
	private void addVacationButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String startDate = startVacationDate.getText();
			String startTime = startVacationTime.getText();
			String endDate = endVacationDate.getText();
			String endTime = endVacationTime.getText();
			FlexiBookController.addVacationSlot(startDate, startTime, endDate, endTime);
			success = "Vacation slot starting on " + startDate + " at " + startTime + " ending on " + endDate + " at " + endTime + " added"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshAddVacationPage();

	}
	
	private void refreshAddVacationPage() {
		setBounds(100, 100, 700, 300);

		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		startVacationDate.setText("");
		startVacationTime.setText("");
		endVacationDate.setText("");
		endVacationTime.setText("");

//		pack();
	}
	
	private void initAddHolidayPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel startHolidayDateLabel = new JLabel();
		JLabel startHolidayTimeLabel = new JLabel();
		JLabel endHolidayDateLabel = new JLabel();
		JLabel endHolidayTimeLabel = new JLabel();
		
		JButton addHolidayBackButton = new JButton();
		JButton addHolidayAddButton = new JButton();



		startHolidayDateLabel.setText("Start Date");
		startHolidayTimeLabel.setText("Start Time");
		endHolidayDateLabel.setText("End Date");
		endHolidayTimeLabel.setText("End Time");
		addHolidayBackButton.setText("Back");
		addHolidayAddButton.setText("Add Holiday Slot");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(startHolidayDateLabel)
						.addComponent(startHolidayTimeLabel)
						.addComponent(endHolidayDateLabel)
						.addComponent(endHolidayTimeLabel)
						.addComponent(addHolidayBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(startHolidayDate)
						.addComponent(startHolidayTime)
						.addComponent(endHolidayDate)
						.addComponent(endHolidayTime)
						.addComponent(addHolidayAddButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {startHolidayDateLabel, startHolidayTimeLabel, endHolidayDateLabel, endHolidayTimeLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {startHolidayDate, startHolidayTime, endHolidayDate, endHolidayTime});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addHolidayBackButton, addHolidayAddButton, startHolidayDate, startHolidayTime, endHolidayDate, endHolidayTime});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(startHolidayDateLabel)
						.addComponent(startHolidayDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(startHolidayTimeLabel)
						.addComponent(startHolidayTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(endHolidayDateLabel)
						.addComponent(endHolidayDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(endHolidayTimeLabel)
						.addComponent(endHolidayTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(addHolidayBackButton)
						.addComponent(addHolidayAddButton))
				
				);
		

		
		addHolidayBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initSetupBusinessInfoPage();
			}
		});
		
		addHolidayAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addHolidayButtonPressed(e);
			}
		});

//		pack();
	}
	
	private void addHolidayButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String startDate = startHolidayDate.getText();
			String startTime = startHolidayTime.getText();
			String endDate = endHolidayDate.getText();
			String endTime = endHolidayTime.getText();
			FlexiBookController.addHolidaySlot(startDate, startTime, endDate, endTime);
			success = "Holiday slot starting on " + startDate + " at " + startTime + " ending on " + endDate + " at " + endTime + " added"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshAddHolidayPage();

	}
	
	private void refreshAddHolidayPage() {
		setBounds(100, 100, 700, 300);
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		startHolidayDate.setText("");
		startHolidayTime.setText("");
		endHolidayDate.setText("");
		endHolidayTime.setText("");

		pack();
	}
	
	private void initAddBusinessHourPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel addBusinessHourDayLabel = new JLabel();
		JLabel addBusinessHourStartLabel = new JLabel();
		JLabel addBusinessHourEndLabel = new JLabel();
		
		JButton addBusinessHourBackButton = new JButton();
		JButton addBusinessHourAddButton = new JButton();

		addBusinessHourDayLabel.setText("Day");
		addBusinessHourStartLabel.setText("Start Time");
		addBusinessHourEndLabel.setText("End Time");
		addBusinessHourBackButton.setText("Back");
		addBusinessHourAddButton.setText("Add Business Hour");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(addBusinessHourDayLabel)
						.addComponent(addBusinessHourStartLabel)
						.addComponent(addBusinessHourEndLabel)
						.addComponent(addBusinessHourBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(addBusinessHourDay)
						.addComponent(addBusinessHourStart)
						.addComponent(addBusinessHourEnd)
						.addComponent(addBusinessHourAddButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {addBusinessHourDayLabel, addBusinessHourStartLabel, addBusinessHourEndLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {addBusinessHourDay, addBusinessHourStart, addBusinessHourEnd});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addBusinessHourBackButton, addBusinessHourAddButton, addBusinessHourDay, addBusinessHourStart, addBusinessHourEnd});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(addBusinessHourDayLabel)
						.addComponent(addBusinessHourDay))
				.addGroup(layout.createParallelGroup()
						.addComponent(addBusinessHourStartLabel)
						.addComponent(addBusinessHourStart))
				.addGroup(layout.createParallelGroup()
						.addComponent(addBusinessHourEndLabel)
						.addComponent(addBusinessHourEnd))
				.addGroup(layout.createParallelGroup()
						.addComponent(addBusinessHourBackButton)
						.addComponent(addBusinessHourAddButton))
				
				);
		

		
		addBusinessHourBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initSetupBusinessInfoPage();
			}
		});
		
		addBusinessHourAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBusinessHourButtonPressed(e);
			}
		});

		//pack();
	}
	
	private void addBusinessHourButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String day = addBusinessHourDay.getText();
			String startTime = addBusinessHourStart.getText();
			String endTime = addBusinessHourEnd.getText();
			FlexiBookController.addBusinessHour(day, startTime, endTime);
			success = "Business Hour on " + day + " from " + startTime + " to " + endTime + " added"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshAddBusinessHourPage();

	}
	
	private void refreshAddBusinessHourPage() {
		setBounds(100, 100, 700, 300);
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		addBusinessHourDay.setText("");
		addBusinessHourStart.setText("");
		addBusinessHourEnd.setText("");

		//pack();
	}
	
	private void initUpdateBusinessInfoPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(100, 100, 700, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel businessNameLabel = new JLabel();
		JLabel businessAddressLabel = new JLabel();
		JLabel businessEmailLabel = new JLabel();
		JLabel businessPhoneLabel = new JLabel();

		JButton businessInfoBackButton = new JButton();
		JButton businessInfoUpdateInfoButton = new JButton();
		JButton businessInfoUpdateBusinessHourButton = new JButton();
		JButton businessInfoUpdateVacationButton = new JButton();
		JButton businessInfoUpdateHolidayButton = new JButton();

		businessNameLabel.setText("Business Name");
		businessAddressLabel.setText("Business Address");
		businessEmailLabel.setText("Business Email");
		businessPhoneLabel.setText("Business Phone Number");
		businessInfoBackButton.setText("Back");
		businessInfoUpdateInfoButton.setText("Update Business Info");
		businessInfoUpdateBusinessHourButton.setText("Update Business Hour");
		businessInfoUpdateVacationButton.setText("Update Vacation slot");
		businessInfoUpdateHolidayButton.setText("Update Holiday Slot");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(businessNameLabel)
						.addComponent(businessAddressLabel)
						.addComponent(businessEmailLabel)
						.addComponent(businessPhoneLabel)
						.addComponent(businessInfoUpdateVacationButton)
						.addComponent(businessInfoUpdateHolidayButton)
						.addComponent(businessInfoUpdateBusinessHourButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(updateBusinessName)
						.addComponent(updateBusinessAddress)
						.addComponent(updateBusinessEmail)
						.addComponent(updateBusinessPhone)
						.addComponent(businessInfoBackButton)
						.addComponent(businessInfoUpdateInfoButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessNameLabel, businessAddressLabel, businessEmailLabel, businessPhoneLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateBusinessName, updateBusinessAddress, updateBusinessEmail, updateBusinessPhone});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {businessInfoUpdateHolidayButton, businessInfoUpdateVacationButton, businessInfoUpdateInfoButton, businessInfoBackButton, businessInfoUpdateBusinessHourButton, updateBusinessName, updateBusinessAddress, updateBusinessEmail, updateBusinessPhone});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(25)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(businessNameLabel)
						.addComponent(updateBusinessName))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessAddressLabel)
						.addComponent(updateBusinessAddress))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessEmailLabel)
						.addComponent(updateBusinessEmail))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessPhoneLabel)
						.addComponent(updateBusinessPhone))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoUpdateVacationButton)
						.addComponent(businessInfoUpdateInfoButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoUpdateHolidayButton)
						.addComponent(businessInfoBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(businessInfoUpdateBusinessHourButton))
				);
		

		
		businessInfoBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initBusinessInfoPage();
			}
		});

		businessInfoUpdateInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateInfoButtonPressed(e);
			}
		});
		
		businessInfoUpdateVacationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAddVacationPage();
			}
		});
		
		businessInfoUpdateHolidayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAddHolidayPage();
			}
		});
		
		businessInfoUpdateBusinessHourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAddBusinessHourPage();
			}
		});

		//pack();
	}
	
	private void updateInfoButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String name = updateBusinessName.getText();
			String address = updateBusinessAddress.getText();
			String phoneNumber = updateBusinessPhone.getText();
			String email = updateBusinessEmail.getText();
			FlexiBookController.updateBusinessInfo(name, address, phoneNumber, email);
			success = "There is a Business named " + name + " at " + address + "with phone number: " + phoneNumber +" and email: " + email; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshUpdateBusinessInfoPage();
	}

	private void refreshUpdateBusinessInfoPage() {
		
		setBounds(100, 100, 700, 300);
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		updateBusinessName.setText("");
		updateBusinessAddress.setText("");
		updateBusinessPhone.setText("");
		updateBusinessEmail.setText("");

		//pack();
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
		setBounds(100, 100, 700, 300);

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
				.addGap(270)
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
						.addGap(60)
						.addComponent(addServPageButton)
						.addGap(15)
						.addComponent(updateServPageButton)
						.addGap(15)
						.addComponent(deleteServPageButton)
						.addGap(15)
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

	//	pack();
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
		setBounds(100, 100, 700, 300);
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
					.addGap(100)
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
				.addGap(35)
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


		refreshServicePage();

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
		//pack();
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
		//pack();
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
		setBounds(100, 100, 700, 300);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//page components


		newServiceNameLabel.setText("New name:");
		newServiceDurationLabel.setText("New duration (mins):");
		newDowntimeStartLabel.setText("New downtime start (mins):");
		newDowntimeDurationLabel.setText("New downtime duration (mins):");
		updateExistingServiceLabel.setText("Choose service to update:");
		updateServiceButton.setText("Update Service");
		updateServiceBackButton.setText("Back");
		
		refreshServicePage();

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
				.addGap(25)
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
				
				manageServiceActionPerformed(e);
				message.setText("");
			}
		});

		refreshServicePage();
		//resize page to fit all components 
		//pack();
	}

	private void updateServiceButtonPressed (ActionEvent evt) {
		error = "";
		success = ""; 
		setBounds(100, 100, 700, 300);
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
		//pack();
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
		setBounds(100, 100, 700, 300);
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
	//	pack();
	}


	/**
	 * @author Sneha
	 * @param evt
	 */
	private void deleteServiceButtonPressed (ActionEvent evt) {
		error = "";
		success = ""; 
		setBounds(100, 100, 700, 300);
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
		//pack();
	}

	/**
	 * @author Sneha
	 */
	private void refreshServicePage() {
		
		setBounds(100, 100, 700, 300);
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

		//pack();
	}
	
	

	/**
	 * @author yasminamatta
	 * @param evt
	 */
	private void managedAppointmentStatus(ActionEvent evt) {
		getContentPane().removeAll(); 
		getContentPane().repaint();
		
		setBounds(100, 100, 700, 300);
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
		appointmentListLabel.setText("Choose an appointment:");
		appointmentDateLabel = new JLabel();
		appointmentDateLabel.setText("Choose a date:");
		
		SqlDateModel model = new SqlDateModel();
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");

		JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
		appointmentDatePicker = new JDatePickerImpl(datePanel , new DateLabelFormatter());
		
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()

					.addGap(140)
					.addGroup(layout.createParallelGroup()
							.addComponent(message)
							.addComponent(appointmentDateLabel)
							.addComponent(appointmentListLabel))	

					.addGroup(layout.createParallelGroup()
																
							.addComponent(appointmentDatePicker)
							.addComponent(appointmentList,10,20,30)
							.addComponent(startAppointmentButton)	
							.addComponent(endAppointmentButton)
							.addComponent(noShowButton)
							.addComponent(backToMenuButton))	

							

						)	
					
				);
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,appointmentDatePicker,backToMenuButton});
//		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		//layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList});
		layout.linkSize(SwingConstants.VERTICAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,appointmentDatePicker,backToMenuButton});
//		
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addGap(30)
						.addComponent(message))
				.addGroup(layout.createParallelGroup()
						.addComponent(appointmentDateLabel)
						.addComponent(appointmentDatePicker))
				.addGroup(layout.createParallelGroup()
						.addComponent(appointmentListLabel)
						.addComponent(appointmentList))						
				.addGroup(layout.createParallelGroup()
						
						.addComponent(startAppointmentButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(endAppointmentButton)	)
				.addGroup(layout.createParallelGroup()		
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
				message.setText("");
		
			}
		});
	//pack();
		
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
			if(appointmentDatePicker.getModel().getValue()== null) {
				error = "Please enter a date";
			}
			if(error.isEmpty()) {
			try {
				int day = appointmentDatePicker.getModel().getDay();
				int month = appointmentDatePicker.getModel().getMonth();
				int year = appointmentDatePicker.getModel().getYear();
				String fullDate = year+"-"+month+"-"+day;
			//	Date date = Date.valueOf(fullDate);
			for(TOAppointment appointment: existingAppointments) {
				if(appointment.getStartDate().toString().equals(fullDate)) {
					appointmentWithSpecificDate.add(appointment);
				}
			}
			FlexiBookController.startAppointment(appointmentWithSpecificDate.get(selectedAppointment).getCustomerName(),
					appointmentWithSpecificDate.get(selectedAppointment).getStartTime(),
					appointmentWithSpecificDate.get(selectedAppointment).getStartDate(),
					FlexiBookApplication.getSystemDate(),
					FlexiBookApplication.getSystemTime());
			success = "You have successfully started the appointment " + appointmentWithSpecificDate.get(selectedAppointment).getService() + "with the customer" + appointmentWithSpecificDate.get(selectedAppointment).getCustomerName();

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
		if(appointmentDatePicker.getModel().getValue()== null) {
			error = "Please enter a date";
		}
		if(error.isEmpty()) {
		try {
			
			int day = appointmentDatePicker.getModel().getDay();
			int month = appointmentDatePicker.getModel().getMonth();
			int year = appointmentDatePicker.getModel().getYear();
			String fullDate = year+"-"+month+"-"+day;
//			Date date = Date.valueOf(fullDate);
		for(TOAppointment appointment: existingAppointments) {
			if(appointment.getStartDate().toString().equals(fullDate)) {
				appointmentWithSpecificDate.add(appointment);
			}
		}
		FlexiBookController.endAppointment(appointmentWithSpecificDate.get(selectedAppointment).getCustomerName(),
				appointmentWithSpecificDate.get(selectedAppointment).getStartTime(),
				appointmentWithSpecificDate.get(selectedAppointment).getStartDate(),
				FlexiBookApplication.getSystemDate(),
				FlexiBookApplication.getSystemTime());
		success = "You have successfully ended the appointment " + appointmentWithSpecificDate.get(selectedAppointment).getService() + "with the customer" + appointmentWithSpecificDate.get(selectedAppointment).getCustomerName();

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
		if(appointmentDatePicker.getModel().getValue()== null) {
			error = "Please enter a date";
		}
		if(error.isEmpty()) {
		try {
			int day = appointmentDatePicker.getModel().getDay();
			int month = appointmentDatePicker.getModel().getMonth();
			int year = appointmentDatePicker.getModel().getYear();
			String fullDate = year+"-"+month+"-"+day;
//			Date date = Date.valueOf(fullDate);
		for(TOAppointment appointment: existingAppointments) {
			if(appointment.getStartDate().toString().equals(fullDate)) {
				appointmentWithSpecificDate.add(appointment);
			}
		}
		FlexiBookController.registerNoShow(appointmentWithSpecificDate.get(selectedAppointment).getCustomerName(), 
				appointmentWithSpecificDate.get(selectedAppointment).getStartDate().toString(),
				appointmentWithSpecificDate.get(selectedAppointment).getStartTime().toString(),
				FlexiBookApplication.getSystemDate(),
				FlexiBookApplication.getSystemTime());
		success = "You have successfully registered a no show for the appointment " + appointmentWithSpecificDate.get(selectedAppointment).getService() + "with the customer" + appointmentWithSpecificDate.get(selectedAppointment).getCustomerName();
				
		}
		catch(InvalidInputException e) {
			error += e.getMessage();
		}
		}
		refreshAppointmentStatusPage();
	}
	
	private void refreshAppointmentStatusPage() {
		setBounds(100, 100, 700, 300);
	
		if(!error.equals("")) {
			message.setText(error);
			message.setForeground(Color.RED);
		}

		if(!success.equals("")) {
			message.setText(success);
			message.setForeground(Color.GREEN);
		}
		if(error.equals("") || error.length()==0) {
			appointmentList.removeAllItems();
		}
		//pack();
	}
	
	/**
	 * @author artus
	 * 
	 */

	private void updateOwnerAccount() {
			
			getContentPane().removeAll(); 
			getContentPane().repaint();
			setBounds(100, 100, 700, 300);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			//error="";
			//success ="";
			
			
			oldOwnerUsernameLabel = new JLabel();
			oldOwnerUsernameLabel.setText("Owner Username :");
			newOwnerPasswordLabel = new JLabel();
			newOwnerPasswordLabel.setText("New Password :");
			newOwnerPassword = new JPasswordField();
			newOwnerPassword.setColumns(10);
			ownerUpdateButton = new JButton();
			ownerUpdateButton.setText("Update Account");
			backOwnerButton = new JButton();
			backOwnerButton.setText("Back");
			
			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);			
			
			layout.setHorizontalGroup(
					layout.createSequentialGroup()
					.addGap(190)
						.addGroup(layout.createParallelGroup()
								.addComponent(message)
								.addComponent(oldOwnerUsernameLabel)
								.addComponent(newOwnerPasswordLabel))
						.addGroup(layout.createParallelGroup()
								.addComponent(message)
								.addComponent(oldOwnerUsername)
								.addComponent(newOwnerPassword)
								.addComponent(ownerUpdateButton)
								.addComponent(backOwnerButton))
					);
			

			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldOwnerUsernameLabel,newOwnerPasswordLabel});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldOwnerUsername,newOwnerPassword, ownerUpdateButton, backOwnerButton});
			layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {oldOwnerUsername,newOwnerPassword, ownerUpdateButton, backOwnerButton});




			layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addGap(40)
					.addComponent(message)
					.addGroup(layout.createParallelGroup()
							.addComponent(oldOwnerUsernameLabel)
							.addComponent(oldOwnerUsername))
					.addGroup(layout.createParallelGroup()
							.addComponent(newOwnerPasswordLabel)
							.addComponent(newOwnerPassword))
					.addComponent(ownerUpdateButton)
					.addComponent(backOwnerButton)
			);		
			
			backOwnerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					message.setText("");
					initOwnerMenu();
				}
			});
			
			ownerUpdateButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	            	updatePasswordOwner(evt);
	            }
	        });
			
		//	pack();
		
	}
			
		private void updatePasswordOwner(ActionEvent evt) {		
				    String username = oldOwnerUsername.getText();
				    String password = newOwnerPassword.getText();
				    error="";
				    success="";
				    if (!username.equals("owner")) {
				    	error = "The username should be 'owner'";
				    }
				    if(password.equals("")) {
				    	error = "Please enter a password.";
				    }
				    else {
				    	
				    try {
				    	FlexiBookController.accountUpdate("owner","owner", password);
				    	success ="Account updated.";
				    	
				    } catch (InvalidInputException e) {
				        error = e.getMessage();
				    }
				}
				    refreshUpdateOwnerAccount();
		}
			
	private void updateCustomerAccount() {
			
			
			getContentPane().removeAll(); 
			getContentPane().repaint();
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			//error="";
			//success ="";
			setBounds(100, 100, 700, 300);
			JButton customerUpdateButton;
			oldCustomerUsernameLabel = new JLabel();
			oldCustomerUsernameLabel.setText("Old Username :");
			newCustomerUsernameLabel = new JLabel();
			newCustomerUsernameLabel.setText("New Username :");
			newCustomerPasswordLabel = new JLabel();
			newCustomerPasswordLabel.setText("New Password :");
			customerUpdateButton = new JButton();
			customerUpdateButton.setText("Update Account");
			backCustomerButton = new JButton();
			backCustomerButton.setText("Back");
			
			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);			
			
			layout.setHorizontalGroup(
					layout.createSequentialGroup()
					.addGap(190)
						.addGroup(layout.createParallelGroup()
								.addComponent(message)
								.addComponent(oldCustomerUsernameLabel)
								.addComponent(newCustomerUsernameLabel)
								.addComponent(newCustomerPasswordLabel))
						.addGroup(layout.createParallelGroup()
								.addComponent(message)
								.addComponent(oldCustomerUsername)
								.addComponent(newCustomerUsername)
								.addComponent(newCustomerPassword)
								.addComponent(customerUpdateButton)
								.addComponent(backCustomerButton))
					);
			

			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldCustomerUsernameLabel, newCustomerUsernameLabel,newCustomerPasswordLabel});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldCustomerUsername, newCustomerUsername,newCustomerPassword, customerUpdateButton, backCustomerButton});
			layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {oldCustomerUsername, newCustomerUsername,newCustomerPassword, customerUpdateButton, backCustomerButton});




			layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addGap(40)
					.addComponent(message)
					.addGroup(layout.createParallelGroup()
							.addComponent(oldCustomerUsernameLabel)
							.addComponent(oldCustomerUsername))
					.addGroup(layout.createParallelGroup()
							.addComponent(newCustomerUsernameLabel)
							.addComponent(newCustomerUsername))
					.addGroup(layout.createParallelGroup()
							.addComponent(newCustomerPasswordLabel)
							.addComponent(newCustomerPassword))
					.addComponent(customerUpdateButton)
					.addComponent(backCustomerButton)
			);		
			
			backCustomerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					message.setText("");
					initCustomerMenu();
					
				}
			});
			
			customerUpdateButton.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	updateCustomerActionPerformed(e);
	            	
	            }
	        });
			
			//pack();
		
	}
	
	private void updateCustomerActionPerformed(ActionEvent evt) {	
		success ="";
		error="";
		String oldUname = oldCustomerUsername.getText();
	    String username = newCustomerUsername.getText();
	    String password = newCustomerPassword.getText();
	   
	    	if(oldUname.equals(""))  {
	    		error = "Please enter the old username.";
	    	}
	    	if(username.equals(""))  {
	    		error = "Please enter the new username.";
	    	}
	    	if(password.equals(""))  {
	    		error = "Please enter the password.";
	    	}
	    	if(error.equals("")){
	    		 try {
	    	FlexiBookController.accountUpdate(oldUname,username, password);
	    	success = "Account updated";
	    	
	    } catch (InvalidInputException e) {
	        error = e.getMessage();
	    }}
	    refreshUpdateCustomerAccount();
	
	}
	
	private void refreshUpdateCustomerAccount() {
		setBounds(100, 100, 700, 300);
		
		if(!error.equals("")) {
			message.setForeground(Color.RED);
			message.setText(error);
			
		}

		if(!success.equals("")) {
			message.setForeground(Color.GREEN);
			message.setText(success);
			
		}
		if(error.equals("")|| error.length()==0) {
			oldCustomerUsername.setText("");
			newCustomerUsername.setText("");
			newCustomerPassword.setText("");
		
			
		}
	}
	private void refreshUpdateOwnerAccount() {
		setBounds(100, 100, 700, 300);
		
		if(!error.equals("")) {
			message.setForeground(Color.RED);
			message.setText(error);
			
		}

		if(!success.equals("")) {
			message.setForeground(Color.GREEN);
			message.setText(success);
			
		}
		if(error.equals("")) {
			oldOwnerUsername.setText("");
			newOwnerPassword.setText("");
			
		
			
		}
	}
	
}