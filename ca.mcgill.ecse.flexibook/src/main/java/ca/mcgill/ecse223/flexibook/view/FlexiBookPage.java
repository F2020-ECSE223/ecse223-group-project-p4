package ca.mcgill.ecse223.flexibook.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.Customer;
import ca.mcgill.ecse223.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse223.flexibook.controller.TOAppointment;
import ca.mcgill.ecse223.flexibook.controller.TOBusiness;
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
	private JButton datePickerButon;
	private JButton datePickerButtonInPage;
	private JButton backDatePickerButton;
	private JTable overviewTableOfAppointmentStatus;
	private JLabel appOverviewLabelOfAppointmentStatus;
	private String[][] rowDataStatus;
	
	//appointment data
	ArrayList<String> availableServices = new ArrayList<>();
	ArrayList<TOAppointment> existingAppointments = new ArrayList<>();


	//setupBusinessInfo
	private JTextField setBusinessName = new JTextField();
	private JTextField setBusinessAddress = new JTextField();
	private JTextField setBusinessEmail = new JTextField();
	private JTextField setBusinessPhone = new JTextField();
	

	//addVacationSlot
//	private JTextField startVacationDate = new JTextField();
	private JTextField startVacationTime = new JTextField();
//	private JTextField endVacationDate = new JTextField();
	private JTextField endVacationTime = new JTextField();
	private JDatePickerImpl pickStartVacDate;
	private JDatePickerImpl pickEndVacDate;
	
	//updateVacation
	private JDatePickerImpl pickOldStartVacDate;
	private JTextField oldStartVacationTime = new JTextField();
	private JTextField newStartVacationTime = new JTextField();
	private JTextField newEndVacationTime = new JTextField();
	private JDatePickerImpl pickNewStartVacDate;
	private JDatePickerImpl pickNewEndVacDate;
	
	//addHolidaySlot
//	private JTextField startHolidayDate = new JTextField();
	private JTextField startHolidayTime = new JTextField();
//	private JTextField endHolidayDate = new JTextField();
	private JTextField endHolidayTime = new JTextField();
	private JDatePickerImpl pickStartHolDate;
	private JDatePickerImpl pickEndHolDate;
	
	//updateHoliday
	private JDatePickerImpl pickOldStartHolDate;
	private JTextField oldStartHolidayTime = new JTextField();
	private JTextField newStartHolidayTime = new JTextField();
	private JTextField newEndHolidayTime = new JTextField();
	private JDatePickerImpl pickNewStartHolDate;
	private JDatePickerImpl pickNewEndHolDate;
	
	//addBusinessHour
	private JTextField addBusinessHourDay = new JTextField();
	private JTextField addBusinessHourStart = new JTextField();
	private JTextField addBusinessHourEnd = new JTextField();
	private String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private JComboBox selectBusinessHourDay = new JComboBox<String>(days);
	
	//updateBusinessHour
	private JTextField updateBusinessHourOldStart = new JTextField();
	private JTextField updateBusinessHourNewStart = new JTextField();
	private JTextField updateBusinessHourNewEnd = new JTextField();
	private JComboBox selectOldBusinessHourDay = new JComboBox<String>(days);
	private JComboBox selectNewBusinessHourDay = new JComboBox<String>(days);
	
	//updateBusinessInfo
	private JTextField updateBusinessName = new JTextField();
	private JTextField updateBusinessAddress = new JTextField();
	private JTextField updateBusinessEmail = new JTextField();
	private JTextField updateBusinessPhone = new JTextField();
	
	//Update owner account
	
	private JTextField newOwnerPassword = new JTextField();
//	private JTextField oldOwnerUsername = new JTextField();
//	private JLabel oldOwnerUsernameLabel;
	
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
	private JButton deleteAccount;
	
	//login
	private JTextField usernameLogin;
    private JTextField passwordLogin;
    private JLabel errorMessage;
    private JTextField passwordSignup;
    private JTextField usernameSignup;
    JLabel lblWelcomeToBlock;
  
	//view business info
    JLabel businessNameLabel = new JLabel();
	JLabel businessAddressLabel = new JLabel();
	JLabel businessEmailLabel = new JLabel();
	JLabel businessPhoneLabel = new JLabel();
	
	JLabel businessName = new JLabel();
	JLabel businessAddress = new JLabel();
	JLabel businessEmail = new JLabel();
	JLabel businessPhone = new JLabel();
	
	JButton viewBInfBackButton = new JButton();
	JButton viewBInfViewBHButton = new JButton();
	
	
	 // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private JButton back;
    // End of variables declaration
	public FlexiBookPage() {
		FlexiBookApplication.getFlexiBook().delete();
		
		initializeLoginPage();
		setTitle("FlexiBook System P04");
	}
	
	
	
	/**
     * Initialize the contents of the frame.
     */
    private void initializeLoginPage() {
    	getContentPane().removeAll(); 
		getContentPane().repaint();
       
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBounds(350, 150, 700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        panel.setBackground(Color.RED);
        panel.setPreferredSize(new Dimension(1000, 1000));

        getContentPane().add(panel, BorderLayout.NORTH);

        if(FlexiBookApplication.getFlexiBook().getBusiness() != null) {
        	String bName = FlexiBookApplication.getFlexiBook().getBusiness().getName();
            lblWelcomeToBlock = new JLabel("Welcome to " + bName);
        }
        else {
        	lblWelcomeToBlock = new JLabel("Welcome to FlexiBook");
        }
        
     
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
                                                 .addComponent(errorMessage, GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(145)
                                                .addComponent(lblLogin)
                                                .addGap(325)
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
//                                        .addGroup(groupLayout.createSequentialGroup()
//                                                .addContainerGap()
//                                                .addComponent(errorMessage, GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)))
                               ) )
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
		setBounds(350, 150, 700, 500);

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
				chooseDatePage(e);
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
		setBounds(350,150,700,500);
		
		appBookingButton = new JButton();
		appBookingButton.setText("Appoinment booking");
//		manageCustomerAccountButton = new JButton();
//		manageCustomerAccountButton.setText("Manage Account");
		updateCustomerButton = new JButton();
		updateCustomerButton.setText("Manage customer account");
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
	 *
	 */
	private void initAppointmentBookingPage() {

		getContentPane().removeAll(); 
		getContentPane().repaint();

		//refreshDataForAppointmentBooking();
		setBounds(100, 100, 700, 400);

		// elements for error message
		message = new JLabel();
		makeAppBackButton = new JButton();
		makeAppBackButton.setText("Back");

		//elements for make appointment
		makeAppTime = new JTextField();
		makeAppTime.setText("hh:mm");
		makeAppDate = new JTextField();
		makeAppDate.setText("yyyy-mm-dd");
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
		updateAppNewTime.setText("hh:mm");
		updateAppNewDate = new JTextField();
		updateAppNewDate.setText("yyyy-mm-dd");
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
				message.setText("");
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
	 * 
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
			
			String newStartTime;
			if(updateAppNewTime.getText().equals("hh:mm")) {
				newStartTime = "";
			}else {
				newStartTime = updateAppNewTime.getText();
			}
			
			String newStartDate;
			if(updateAppNewDate.getText().equals("yyyy-mm-dd")) {
				newStartDate = "";
			}else {
				newStartDate = updateAppNewDate.getText();
			}
			
			Date todaysDate = FlexiBookApplication.getSystemDate();
			Time currentTime = FlexiBookApplication.getSystemTime();
			int selectedService = updateAppServiceList.getSelectedIndex();

			if(selectedService != -1) {		//new service is selected

				String newService = availableServices.get(selectedService);
				if(!newStartDate.equals("") && !newStartTime.equals("")) {		// updating to a new time slot
					FlexiBookController.updateAppointmentTime(username, newStartTime, newStartDate, startTime, startDate, todaysDate, currentTime);
					FlexiBookController.cancelAndBookNewService(username,  newService, null, newStartTime, newStartDate, todaysDate, currentTime);
					success = "Appointment updated to  " + newStartDate + " at " + startTime + " for new service " + newService; 
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
		setBounds(350,150,700,500);		
		JButton setupBusinessInfoButton = new JButton();
		setupBusinessInfoButton.setText("Setup Business Info");
		JButton updateBusinessInfoButton = new JButton();
		updateBusinessInfoButton.setText("Update Business Info");
		JButton businessInfoViewButton = new JButton();
		businessInfoViewButton.setText("View Business Info");
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
						.addComponent(businessInfoViewButton)
						.addComponent(businessInfoBackButton))
				);

		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {setupBusinessInfoButton, updateBusinessInfoButton, businessInfoBackButton, businessInfoViewButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {setupBusinessInfoButton, updateBusinessInfoButton, businessInfoBackButton, businessInfoViewButton});

		layout.setVerticalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGap(75)
						.addComponent(setupBusinessInfoButton)
						.addGap(15)
						.addComponent(updateBusinessInfoButton)
						.addGap(15)
						.addComponent(businessInfoViewButton)
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
				message.setText("");
				initOwnerMenu();
			}
		});
		
		businessInfoViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initViewBusinessInfoPage();
			}
		});

	//	pack();
	}
	
	private void initViewBusinessInfoPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(350,150,700,500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
		
		businessNameLabel.setText("Business Name:");
		businessAddressLabel.setText("Business Address:");
		businessEmailLabel.setText("Business Email:");
		businessPhoneLabel.setText("Business Phone Number:");
		
		viewBInfBackButton.setText("Back");
		viewBInfViewBHButton.setText("View Business Hours");
		
		TOBusiness b = FlexiBookController.getBusiness();
		if (b == null) {
			businessName.setText("N/A");
			businessAddress.setText("N/A");
			businessEmail.setText("N/A");
			businessPhone.setText("N/A");
		} else {
			businessName.setText(b.getName());
			businessAddress.setText(b.getAddress());
			businessEmail.setText(b.getEmail());
			businessPhone.setText(b.getPhoneNumber());
		}
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				
				.addGroup(layout.createParallelGroup()
						.addComponent(businessNameLabel)
						.addComponent(businessAddressLabel)
						.addComponent(businessEmailLabel)
						.addComponent(businessPhoneLabel)
						.addComponent(viewBInfViewBHButton))
				.addGroup(layout.createParallelGroup()
						//.addComponent(message)
						.addComponent(businessName)
						.addComponent(businessAddress)
						.addComponent(businessEmail)
						.addComponent(businessPhone)
						.addComponent(viewBInfBackButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessNameLabel, businessAddressLabel, businessEmailLabel, businessPhoneLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {businessName, businessAddress, businessEmail, businessPhone});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {viewBInfBackButton, viewBInfViewBHButton, businessName, businessAddress, businessEmail, businessPhone});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(25)
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
						.addComponent(viewBInfViewBHButton)
						.addComponent(viewBInfBackButton))
				);
		

		
		viewBInfBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initBusinessInfoPage();
			}
		});
	}
	
	private void initSetupBusinessInfoPage() {
		
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(350,150,700,500);
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
						//.addComponent(message)
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
				message.setText("");
				initBusinessInfoPage();
			}
		});

		businessInfoSetInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				setInfoButtonPressed(e);
			}
		});
		
		businessInfoAddVacationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				initAddVacationPage();
			}
		});
		
		businessInfoAddHolidayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				initAddHolidayPage();
			}
		});
		
		businessInfoAddBusinessHourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
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
			success = "The business " + name + " is setted successfully"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshSetupBusinessInfoPage();
	}

	private void refreshSetupBusinessInfoPage() {
		setBounds(350,150,700,500);

		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}
		setBusinessName.setText(""	);
		setBusinessAddress.setText("");
		setBusinessPhone.setText("");
		setBusinessEmail.setText("");

		//pack();
	}
	
	private void initAddVacationPage() {
		getContentPane().removeAll();
		getContentPane().repaint();

		setBounds(350,150,700,500);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel startVacationDateLabel = new JLabel();
		JLabel startVacationTimeLabel = new JLabel();
		JLabel endVacationDateLabel = new JLabel();
		JLabel endVacationTimeLabel = new JLabel();
		
		JButton addVacationBackButton = new JButton();
		JButton addVacationAddButton = new JButton();

		LocalDate now = LocalDate.now();
		Properties pO = new Properties();
		pO.put("text.today", "Today");
		pO.put("text.month", "Month");
		pO.put("text.year", "Year");
		
		SqlDateModel overviewModelStartVac = new SqlDateModel();
		overviewModelStartVac.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelStartVac.setSelected(true);
		JDatePanelImpl overviewDatePanelStartVac = new JDatePanelImpl(overviewModelStartVac, pO);
		pickStartVacDate = new JDatePickerImpl(overviewDatePanelStartVac, new DateLabelFormatter());
		
		SqlDateModel overviewModelEndVac = new SqlDateModel();
		overviewModelEndVac.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelEndVac.setSelected(true);
		JDatePanelImpl overviewDatePanelEndVac = new JDatePanelImpl(overviewModelEndVac, pO);
		pickEndVacDate = new JDatePickerImpl(overviewDatePanelEndVac, new DateLabelFormatter());

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
						.addComponent(pickStartVacDate)
						.addComponent(startVacationTime)
						.addComponent(pickEndVacDate)
						.addComponent(endVacationTime)
						.addComponent(addVacationAddButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {startVacationDateLabel, startVacationTimeLabel, endVacationDateLabel, endVacationTimeLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {pickStartVacDate, startVacationTime, pickEndVacDate, endVacationTime});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addVacationBackButton, addVacationAddButton, pickStartVacDate, startVacationTime, pickEndVacDate, endVacationTime});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(startVacationDateLabel)
						.addComponent(pickStartVacDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(startVacationTimeLabel)
						.addComponent(startVacationTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(endVacationDateLabel)
						.addComponent(pickEndVacDate))
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
				message.setText("");
			}
		});
		
		addVacationAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVacationButtonPressed(e);
				error = "";
				success = "";
//				message.setText("");
			}
		});

	//	pack();
	}
	
	private void addVacationButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			
			int year = pickStartVacDate.getModel().getYear();
			int month = pickStartVacDate.getModel().getMonth()+1;
			int day = pickStartVacDate.getModel().getDay();
			String startDate = year +"-"+month+"-" +day;
			
			year = pickEndVacDate.getModel().getYear();
			month = pickEndVacDate.getModel().getMonth()+1;
			day = pickEndVacDate.getModel().getDay();
			String endDate = year +"-"+month+"-" +day;
			
			String startTime = startVacationTime.getText();
			String endTime = endVacationTime.getText();
			FlexiBookController.addVacationSlot(startDate, startTime, endDate, endTime);
			success = "Vacation slot added sucessfully"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshAddVacationPage();

	}
	
	private void refreshAddVacationPage() {
		setBounds(350,150,700,500);

		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		startVacationTime.setText("");
		endVacationTime.setText("");

//		pack();
	}
	
	private void initAddHolidayPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(350,150,700,500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel startHolidayDateLabel = new JLabel();
		JLabel startHolidayTimeLabel = new JLabel();
		JLabel endHolidayDateLabel = new JLabel();
		JLabel endHolidayTimeLabel = new JLabel();
		
		JButton addHolidayBackButton = new JButton();
		JButton addHolidayAddButton = new JButton();

		LocalDate now = LocalDate.now();
		Properties pO = new Properties();
		pO.put("text.today", "Today");
		pO.put("text.month", "Month");
		pO.put("text.year", "Year");
		
		SqlDateModel overviewModelStartHol = new SqlDateModel();
		overviewModelStartHol.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelStartHol.setSelected(true);
		JDatePanelImpl overviewDatePanelStartHol = new JDatePanelImpl(overviewModelStartHol, pO);
		pickStartHolDate = new JDatePickerImpl(overviewDatePanelStartHol, new DateLabelFormatter());
		
		SqlDateModel overviewModelEndHol = new SqlDateModel();
		overviewModelEndHol.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelEndHol.setSelected(true);
		JDatePanelImpl overviewDatePanelEndHol = new JDatePanelImpl(overviewModelEndHol, pO);
		pickEndHolDate = new JDatePickerImpl(overviewDatePanelEndHol, new DateLabelFormatter());

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
						.addComponent(pickStartHolDate)
						.addComponent(startHolidayTime)
						.addComponent(pickEndHolDate)
						.addComponent(endHolidayTime)
						.addComponent(addHolidayAddButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {startHolidayDateLabel, startHolidayTimeLabel, endHolidayDateLabel, endHolidayTimeLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {pickStartHolDate, startHolidayTime, pickEndHolDate, endHolidayTime});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addHolidayBackButton, addHolidayAddButton, pickStartHolDate, startHolidayTime, pickEndHolDate, endHolidayTime});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(startHolidayDateLabel)
						.addComponent(pickStartHolDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(startHolidayTimeLabel)
						.addComponent(startHolidayTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(endHolidayDateLabel)
						.addComponent(pickEndHolDate))
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
				message.setText("");
			}
		});
		
		addHolidayAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addHolidayButtonPressed(e);
				error = "";
				success = "";
//				message.setText("");
			}
		});

//		pack();
	}
	
	private void addHolidayButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {

			int year = pickStartVacDate.getModel().getYear();
			int month = pickStartVacDate.getModel().getMonth()+1;
			int day = pickStartVacDate.getModel().getDay();
			String startDate = year +"-"+month+"-" +day;
			
			year = pickEndVacDate.getModel().getYear();
			month = pickEndVacDate.getModel().getMonth()+1;
			day = pickEndVacDate.getModel().getDay();
			String endDate = year +"-"+month+"-" +day;
			
			String startTime = startHolidayTime.getText();
			String endTime = endHolidayTime.getText();
			FlexiBookController.addHolidaySlot(startDate, startTime, endDate, endTime);
			success = "Holiday slot added successfully"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshAddHolidayPage();

	}
	
	private void refreshAddHolidayPage() {
		setBounds(350,150,700,500);
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

//		startHolidayDate.setText("");
		startHolidayTime.setText("");
//		endHolidayDate.setText("");
		endHolidayTime.setText("");

		pack();
	}
	
	private void initAddBusinessHourPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(350,150,700,500);
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
						//.addComponent(addBusinessHourDay)
						.addComponent(selectBusinessHourDay)
						.addComponent(addBusinessHourStart)
						.addComponent(addBusinessHourEnd)
						.addComponent(addBusinessHourAddButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {addBusinessHourDayLabel, addBusinessHourStartLabel, addBusinessHourEndLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {selectBusinessHourDay, addBusinessHourStart, addBusinessHourEnd});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addBusinessHourBackButton, addBusinessHourAddButton, selectBusinessHourDay, addBusinessHourStart, addBusinessHourEnd});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(addBusinessHourDayLabel)
						//.addComponent(addBusinessHourDay))
						.addComponent(selectBusinessHourDay))
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
				message.setText("");
				error = "";
				success = "";
			}
		});
		
		addBusinessHourAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBusinessHourButtonPressed(e);
				error = "";
				success = "";
//				message.setText("");

			}
		});

		//pack();
	}
	
	private void addBusinessHourButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String day = (String) selectBusinessHourDay.getSelectedItem();
			String startTime = addBusinessHourStart.getText();
			String endTime = addBusinessHourEnd.getText();
			FlexiBookController.addBusinessHour(day, startTime, endTime);
			success = "Business Hour added"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshAddBusinessHourPage();

	}
	
	private void refreshAddBusinessHourPage() {
		setBounds(350,150,700,500);
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		addBusinessHourStart.setText("");
		addBusinessHourEnd.setText("");

		//pack();
	}
	
	private void initUpdateBusinessInfoPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(350,150,700,500);
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
				message.setText("");
			}
		});

		businessInfoUpdateInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				updateInfoButtonPressed(e);
			}
		});
		
		businessInfoUpdateVacationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				initUpdateVacationPage();
			}
		});
		
		businessInfoUpdateHolidayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				initUpdateHolidayPage();
			}
		});
		
		businessInfoUpdateBusinessHourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				message.setText("");
				initUpdateBusinessHourPage();
			}
		});

	
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

			success = "The business" + name + " set successfully."; 


		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshUpdateBusinessInfoPage();
	}

	private void refreshUpdateBusinessInfoPage() {
		setBounds(350,150,700,500);
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
	
	private void initUpdateVacationPage() {
		getContentPane().removeAll();
		getContentPane().repaint();

		setBounds(350,150,700,500);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel oldStartVacationDateLabel = new JLabel();
		JLabel oldStartVacationTimeLabel = new JLabel();
		JLabel newStartVacationDateLabel = new JLabel();
		JLabel newStartVacationTimeLabel = new JLabel();
		JLabel newEndVacationDateLabel = new JLabel();
		JLabel newEndVacationTimeLabel = new JLabel();
		
		JButton updateVacationBackButton = new JButton();
		JButton updateVacationUpdateButton = new JButton();

		LocalDate now = LocalDate.now();
		Properties pO = new Properties();
		pO.put("text.today", "Today");
		pO.put("text.month", "Month");
		pO.put("text.year", "Year");
		
		SqlDateModel overviewModelOldStartVac = new SqlDateModel();
		overviewModelOldStartVac.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelOldStartVac.setSelected(true);
		JDatePanelImpl overviewDatePanelOldStartVac = new JDatePanelImpl(overviewModelOldStartVac, pO);
		pickOldStartVacDate = new JDatePickerImpl(overviewDatePanelOldStartVac, new DateLabelFormatter());
		
		SqlDateModel overviewModelNewStartVac = new SqlDateModel();
		overviewModelNewStartVac.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelNewStartVac.setSelected(true);
		JDatePanelImpl overviewDatePanelNewStartVac = new JDatePanelImpl(overviewModelNewStartVac, pO);
		pickNewStartVacDate = new JDatePickerImpl(overviewDatePanelNewStartVac, new DateLabelFormatter());
		
		SqlDateModel overviewModelNewEndVac = new SqlDateModel();
		overviewModelNewEndVac.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelNewEndVac.setSelected(true);
		JDatePanelImpl overviewDatePanelNewEndVac = new JDatePanelImpl(overviewModelNewEndVac, pO);
		pickNewEndVacDate = new JDatePickerImpl(overviewDatePanelNewEndVac, new DateLabelFormatter());

		oldStartVacationDateLabel.setText("Current Start Date");
		oldStartVacationTimeLabel.setText("Current Start Time");
		newStartVacationDateLabel.setText("New Start Date");
		newStartVacationTimeLabel.setText("New Start Time");
		newEndVacationDateLabel.setText("New End Date");
		newEndVacationTimeLabel.setText("New End Time");
		updateVacationBackButton.setText("Back");
		updateVacationUpdateButton.setText("Update Vacation Slot");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(oldStartVacationDateLabel)
						.addComponent(oldStartVacationTimeLabel)
						.addComponent(newStartVacationDateLabel)
						.addComponent(newStartVacationTimeLabel)
						.addComponent(newEndVacationDateLabel)
						.addComponent(newEndVacationTimeLabel)
						.addComponent(updateVacationBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(pickOldStartVacDate)
						.addComponent(oldStartVacationTime)
						.addComponent(pickNewStartVacDate)
						.addComponent(newStartVacationTime)
						.addComponent(pickNewEndVacDate)
						.addComponent(newEndVacationTime)
						.addComponent(updateVacationUpdateButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldStartVacationDateLabel, oldStartVacationTimeLabel, newStartVacationDateLabel, newStartVacationTimeLabel, newEndVacationDateLabel, newEndVacationTimeLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {pickOldStartVacDate, oldStartVacationTime, pickNewStartVacDate, newStartVacationTime, pickNewEndVacDate, newEndVacationTime});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateVacationBackButton, updateVacationUpdateButton, pickOldStartVacDate, oldStartVacationTime, pickNewStartVacDate, newStartVacationTime, pickNewEndVacDate, newEndVacationTime});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(oldStartVacationDateLabel)
						.addComponent(pickOldStartVacDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(oldStartVacationTimeLabel)
						.addComponent(oldStartVacationTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(newStartVacationDateLabel)
						.addComponent(pickNewStartVacDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(newStartVacationTimeLabel)
						.addComponent(newStartVacationTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(newEndVacationDateLabel)
						.addComponent(pickNewEndVacDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(newEndVacationTimeLabel)
						.addComponent(newEndVacationTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateVacationBackButton)
						.addComponent(updateVacationUpdateButton))
				
				);
		

		
		updateVacationBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initUpdateBusinessInfoPage();
				message.setText("");
			}
		});
		
		updateVacationUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateVacationButtonPressed(e);
				error = "";
				success = "";
//				message.setText("");
			}
		});

	//	pack();
	}
	
	private void updateVacationButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			
			int year = pickOldStartVacDate.getModel().getYear();
			int month = pickOldStartVacDate.getModel().getMonth()+1;
			int day = pickOldStartVacDate.getModel().getDay();
			String oldStartDate = year +"-"+month+"-" +day;
			
			year = pickNewStartVacDate.getModel().getYear();
			month = pickNewStartVacDate.getModel().getMonth()+1;
			day = pickNewStartVacDate.getModel().getDay();
			String newStartDate = year +"-"+month+"-" +day;
			
			year = pickNewEndVacDate.getModel().getYear();
			month = pickNewEndVacDate.getModel().getMonth()+1;
			day = pickNewEndVacDate.getModel().getDay();
			String newEndDate = year +"-"+month+"-" +day;
			
			String oldStartTime = oldStartVacationTime.getText();
			String newStartTime = newStartVacationTime.getText();
			String newEndTime = newEndVacationTime.getText();
			FlexiBookController.updateVacation(oldStartDate, oldStartTime, newStartDate, newStartTime, newEndDate, newEndTime);
			success = "Success"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshUpdateVacationPage();

	}
	
	private void refreshUpdateVacationPage() {
		setBounds(350,150,700,500);

		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}
		
		oldStartVacationTime.setText("");
		newStartVacationTime.setText("");
		newEndVacationTime.setText("");

//		pack();
	}
	
	private void initUpdateHolidayPage() {
		getContentPane().removeAll();
		getContentPane().repaint();

		setBounds(350,150,700,500);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel oldStartHolidayDateLabel = new JLabel();
		JLabel oldStartHolidayTimeLabel = new JLabel();
		JLabel newStartHolidayDateLabel = new JLabel();
		JLabel newStartHolidayTimeLabel = new JLabel();
		JLabel newEndHolidayDateLabel = new JLabel();
		JLabel newEndHolidayTimeLabel = new JLabel();
		
		JButton updateHolidayBackButton = new JButton();
		JButton updateHolidayUpdateButton = new JButton();

		LocalDate now = LocalDate.now();
		Properties pO = new Properties();
		pO.put("text.today", "Today");
		pO.put("text.month", "Month");
		pO.put("text.year", "Year");
		
		SqlDateModel overviewModelOldStartHol = new SqlDateModel();
		overviewModelOldStartHol.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelOldStartHol.setSelected(true);
		JDatePanelImpl overviewDatePanelOldStartHol = new JDatePanelImpl(overviewModelOldStartHol, pO);
		pickOldStartHolDate = new JDatePickerImpl(overviewDatePanelOldStartHol, new DateLabelFormatter());
		
		SqlDateModel overviewModelNewStartHol = new SqlDateModel();
		overviewModelNewStartHol.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelNewStartHol.setSelected(true);
		JDatePanelImpl overviewDatePanelNewStartHol = new JDatePanelImpl(overviewModelNewStartHol, pO);
		pickNewStartHolDate = new JDatePickerImpl(overviewDatePanelNewStartHol, new DateLabelFormatter());
		
		SqlDateModel overviewModelNewEndHol = new SqlDateModel();
		overviewModelNewEndHol.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModelNewEndHol.setSelected(true);
		JDatePanelImpl overviewDatePanelNewEndHol = new JDatePanelImpl(overviewModelNewEndHol, pO);
		pickNewEndHolDate = new JDatePickerImpl(overviewDatePanelNewEndHol, new DateLabelFormatter());

		oldStartHolidayDateLabel.setText("Current Start Date");
		oldStartHolidayTimeLabel.setText("Current Start Time");
		newStartHolidayDateLabel.setText("New Start Date");
		newStartHolidayTimeLabel.setText("New Start Time");
		newEndHolidayDateLabel.setText("New End Date");
		newEndHolidayTimeLabel.setText("New End Time");
		updateHolidayBackButton.setText("Back");
		updateHolidayUpdateButton.setText("Update Holiday Slot");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(oldStartHolidayDateLabel)
						.addComponent(oldStartHolidayTimeLabel)
						.addComponent(newStartHolidayDateLabel)
						.addComponent(newStartHolidayTimeLabel)
						.addComponent(newEndHolidayDateLabel)
						.addComponent(newEndHolidayTimeLabel)
						.addComponent(updateHolidayBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(pickOldStartHolDate)
						.addComponent(oldStartHolidayTime)
						.addComponent(pickNewStartHolDate)
						.addComponent(newStartHolidayTime)
						.addComponent(pickNewEndHolDate)
						.addComponent(newEndHolidayTime)
						.addComponent(updateHolidayUpdateButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldStartHolidayDateLabel, oldStartHolidayTimeLabel, newStartHolidayDateLabel, newStartHolidayTimeLabel, newEndHolidayDateLabel, newEndHolidayTimeLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {pickOldStartHolDate, oldStartHolidayTime, pickNewStartHolDate, newStartHolidayTime, pickNewEndHolDate, newEndHolidayTime});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateHolidayBackButton, updateHolidayUpdateButton, pickOldStartHolDate, oldStartHolidayTime, pickNewStartHolDate, newStartHolidayTime, pickNewEndHolDate, newEndHolidayTime});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(oldStartHolidayDateLabel)
						.addComponent(pickOldStartHolDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(oldStartHolidayTimeLabel)
						.addComponent(oldStartHolidayTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(newStartHolidayDateLabel)
						.addComponent(pickNewStartHolDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(newStartHolidayTimeLabel)
						.addComponent(newStartHolidayTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(newEndHolidayDateLabel)
						.addComponent(pickNewEndHolDate))
				.addGroup(layout.createParallelGroup()
						.addComponent(newEndHolidayTimeLabel)
						.addComponent(newEndHolidayTime))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateHolidayBackButton)
						.addComponent(updateHolidayUpdateButton))
				
				);
		

		
		updateHolidayBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initUpdateBusinessInfoPage();
				message.setText("");
			}
		});
		
		updateHolidayUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateHolidayButtonPressed(e);
				error = "";
				success = "";
//				message.setText("");
			}
		});

	//	pack();
	}
	
	private void updateHolidayButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			
			int year = pickOldStartHolDate.getModel().getYear();
			int month = pickOldStartHolDate.getModel().getMonth()+1;
			int day = pickOldStartHolDate.getModel().getDay();
			String oldStartDate = year +"-"+month+"-" +day;
			
			year = pickNewStartHolDate.getModel().getYear();
			month = pickNewStartHolDate.getModel().getMonth()+1;
			day = pickNewStartHolDate.getModel().getDay();
			String newStartDate = year +"-"+month+"-" +day;
			
			year = pickNewEndHolDate.getModel().getYear();
			month = pickNewEndHolDate.getModel().getMonth()+1;
			day = pickNewEndHolDate.getModel().getDay();
			String newEndDate = year +"-"+month+"-" +day;
			
			String oldStartTime = oldStartHolidayTime.getText();
			String newStartTime = newStartHolidayTime.getText();
			String newEndTime = newEndHolidayTime.getText();
			FlexiBookController.updateHoliday(oldStartDate, oldStartTime, newStartDate, newStartTime, newEndDate, newEndTime);
			success = "Success"; 
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshUpdateHolidayPage();

	}
	
	private void refreshUpdateHolidayPage() {
		setBounds(350,150,700,500);

		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}
		
		oldStartHolidayTime.setText("");
		newStartHolidayTime.setText("");
		newEndHolidayTime.setText("");

//		pack();
	}
	
	private void initUpdateBusinessHourPage() {
		getContentPane().removeAll();
		getContentPane().repaint();
		setBounds(350,150,700,500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel updateBusinessHourOldDayLabel = new JLabel();
		JLabel updateBusinessHourOldStartLabel = new JLabel();
		JLabel updateBusinessHourNewDayLabel = new JLabel();
		JLabel updateBusinessHourNewStartLabel = new JLabel();
		JLabel updateBusinessHourNewEndLabel = new JLabel();
		
		JButton updateBusinessHourBackButton = new JButton();
		JButton updateBusinessHourUpdateButton = new JButton();

		updateBusinessHourOldDayLabel.setText("Current Day");
		updateBusinessHourOldStartLabel.setText("Current Start Time");
		updateBusinessHourNewDayLabel.setText("New Day");
		updateBusinessHourNewStartLabel.setText("New Start Time");
		updateBusinessHourNewEndLabel.setText("New End Time");
		updateBusinessHourBackButton.setText("Back");
		updateBusinessHourUpdateButton.setText("Update Business Hour");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(190)
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(updateBusinessHourOldDayLabel)
						.addComponent(updateBusinessHourOldStartLabel)
						.addComponent(updateBusinessHourNewDayLabel)
						.addComponent(updateBusinessHourNewStartLabel)
						.addComponent(updateBusinessHourNewEndLabel)
						.addComponent(updateBusinessHourBackButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(message)
						.addComponent(selectOldBusinessHourDay)
						.addComponent(updateBusinessHourOldStart)
						.addComponent(selectNewBusinessHourDay)
						.addComponent(updateBusinessHourNewStart)
						.addComponent(updateBusinessHourNewEnd)
						.addComponent(updateBusinessHourUpdateButton))
				);
		
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateBusinessHourOldDayLabel, updateBusinessHourOldStartLabel, updateBusinessHourNewDayLabel, updateBusinessHourNewStartLabel, updateBusinessHourNewEndLabel});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {selectOldBusinessHourDay, updateBusinessHourOldStart, selectNewBusinessHourDay, updateBusinessHourNewStart, updateBusinessHourNewEnd});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {updateBusinessHourBackButton, updateBusinessHourUpdateButton, selectOldBusinessHourDay, updateBusinessHourOldStart, selectNewBusinessHourDay, updateBusinessHourNewStart, updateBusinessHourNewEnd});


		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(35)
				.addComponent(message)
				.addGroup(layout.createParallelGroup()
						.addComponent(updateBusinessHourOldDayLabel)
						.addComponent(selectOldBusinessHourDay))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateBusinessHourOldStartLabel)
						.addComponent(updateBusinessHourOldStart))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateBusinessHourNewDayLabel)
						.addComponent(selectNewBusinessHourDay))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateBusinessHourNewStartLabel)
						.addComponent(updateBusinessHourNewStart))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateBusinessHourNewEndLabel)
						.addComponent(updateBusinessHourNewEnd))
				.addGroup(layout.createParallelGroup()
						.addComponent(updateBusinessHourBackButton)
						.addComponent(updateBusinessHourUpdateButton))
				
				);
		

		
		updateBusinessHourBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initUpdateBusinessInfoPage();
				message.setText("");
				error = "";
				success = "";
			}
		});
		
		updateBusinessHourUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBusinessHourButtonPressed(e);
//				message.setText("");
				error = "";
				success = "";
			}
		});

		//pack();
	}
	
	private void updateBusinessHourButtonPressed(ActionEvent evt) {
		error = null;
		success = null;
		try {
			String oldDay = (String) selectOldBusinessHourDay.getSelectedItem();
			String newDay = (String) selectNewBusinessHourDay.getSelectedItem();
			String oldStartTime = updateBusinessHourOldStart.getText();
			String newStartTime = updateBusinessHourNewStart.getText();
			String newEndTime = updateBusinessHourNewEnd.getText();
			FlexiBookController.updateBusinessHour(oldDay, oldStartTime, newDay, newStartTime, newEndTime);
			success = "Success";
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		refreshUpdateBusinessHourPage();

	}
	
	private void refreshUpdateBusinessHourPage() {
		setBounds(350,150,700,500);
		if (error != null) {
			message.setText(error);
			message.setForeground(Color.RED);

		}
		else if (success != null) {
			message.setText(success);
			message.setForeground(Color.GREEN);

		}

		updateBusinessHourOldStart.setText("");
		updateBusinessHourNewStart.setText("");
		updateBusinessHourNewEnd.setText("");

		//pack();
	}
	



	private void manageAppActionPerformed(ActionEvent evt) {
		//initManageAppAcionPerformed();
	}



	private void viewCalenderActionPerformed(ActionEvent evt) {
		initCalenderComponents();
	}


	/**
	 * @author Sneha
	 * @param evt
	 */
	private void manageServiceActionPerformed (ActionEvent evt) {
		//addServicePage

		getContentPane().removeAll(); 
		getContentPane().repaint();
		setBounds(350,150,700,500);
		
		error= "";
		success = "";
		

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
		setBounds(350,150,700,500);
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
				error = "";
				success = "";
				message.setText("");
			}
		});

		refreshServicePage();
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
		message.setText("");
		String name = null;
		int duration = 0 ;
		int dtDuration = 0; 
		int dtStart = 0;


		if (error == null || error.length() == 0) {
			name = serviceName.getText();
			if (name.equals("")) {
				error = "Please enter a name.";
			}
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
		setBounds(100, 100, 1000, 500);

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
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {updateExistingService, newServiceName, newServiceDuration, newDowntimeStart, newDowntimeDuration});
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
				error = "";
				success = "";
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
		setBounds(350,150,700,500);
		int selectedService = updateExistingService.getSelectedIndex();
		
		if (selectedService < 0) {
			error = "Please select a service to update."; 
			//refreshServicePage();
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
				error = "Please enter a different name.";
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
				FlexiBookController.updateService(name, newName, duration, dtStart, dtDur);
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
		setBounds(350,150,700,500);
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


		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {deleteExistingServiceLabel, deleteExistingService});
		layout.setVerticalGroup(
				
				layout.createSequentialGroup()
				.addGap(100)
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
				error = "";
				success = "";
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
		setBounds(350,150,700,500);
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
		
		setBounds(350,150,700,500);
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

		Date ced= FlexiBookApplication.getSystemDate();
		Time dube= FlexiBookApplication.getSystemTime();
		
		setBounds(350,150,700,500);
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
		
		rowDataStatus = new String[0][3];
	
		appOverviewLabelOfAppointmentStatus = new JLabel();
		appOverviewLabelOfAppointmentStatus.setText("Appointments that have started:");
		String[] columnNames = { "Start Time", "Customer", "Service" };
		overviewTableOfAppointmentStatus = new JTable(rowDataStatus, columnNames);
		JScrollPane appointmentStatusTable = new JScrollPane(overviewTableOfAppointmentStatus);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()

					.addGap(50)
					
					
					.addGroup(layout.createParallelGroup()
							
							.addComponent(message)
							.addComponent(appointmentListLabel)
							.addComponent(appOverviewLabelOfAppointmentStatus))	

					.addGroup(layout.createParallelGroup()
																
							
							.addComponent(appointmentList)
							.addComponent(startAppointmentButton)	
							.addComponent(endAppointmentButton)
							.addComponent(noShowButton)
							
							.addComponent(appointmentStatusTable,10,40,40)
							
							//.addComponent(appointmentStatusTable,200,200,400)
							
							.addComponent(backToMenuButton)	
							)
							

						)	
					
				);
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,appOverviewLabelOfAppointmentStatus,appointmentStatusTable,backToMenuButton});
//		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		//layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList});
		layout.linkSize(SwingConstants.VERTICAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,appOverviewLabelOfAppointmentStatus,appointmentStatusTable,backToMenuButton});
		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {});
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				
				.addGroup(layout.createSequentialGroup()
						.addGap(30)
						.addComponent(message))				
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
						.addComponent(appOverviewLabelOfAppointmentStatus)
						.addComponent(appointmentStatusTable,10,40,40))
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
				chooseDatePage(evt);
				message.setText("");
		
			}

		});
		
		
		
	
		refreshAppointmentStatusPage();
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

		if(error.equals("")) {
			try {

				
				ArrayList<TOAppointment> ar = new ArrayList<TOAppointment>();
				for(Customer cust : FlexiBookApplication.getFlexiBook().getCustomers()) {
				for(TOAppointment app : FlexiBookController.getCustomerAppointments(cust.getUsername())) {
					ar.add(app);
				}
				}
				TOAppointment  toap = ar.get(selectedAppointment);
			FlexiBookController.startAppointment(toap.getCustomerName(),
					toap.getStartTime(),
					toap.getStartDate(),
					FlexiBookApplication.getSystemDate(),
					FlexiBookApplication.getSystemTime());
			
//			rowDataStatus[0][1]= toap.getStartTime();
//			rowDataStatus[0][2]= toap.getCustomerName();
//			rowDataStatus[0][3]= toap.getService();
			success = "The appointment with the customer " +toap.getCustomerName() + " have started";
				
			}
			catch(InvalidInputException e) {
				message.setText("");
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
//		
		if(error.equals("")) {
		try {
			ArrayList<TOAppointment> ar = new ArrayList<TOAppointment>();
			for(Customer cust : FlexiBookApplication.getFlexiBook().getCustomers()) {
			for(TOAppointment app : FlexiBookController.getCustomerAppointments(cust.getUsername())) {
				ar.add(app);
			}
			}
			TOAppointment  toap = ar.get(selectedAppointment);
		FlexiBookController.endAppointment(toap.getCustomerName(),
				toap.getStartTime(),
				toap.getStartDate(),
				FlexiBookApplication.getSystemDate(),
				FlexiBookApplication.getSystemTime());
		success = "The appointment with the customer " + toap.getCustomerName() + " have ended."  ;

		}
		catch(InvalidInputException e) {
			message.setText("");
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
		
		if(error.equals("")) {
		try {
			ArrayList<TOAppointment> ar = new ArrayList<TOAppointment>();
			for(Customer cust : FlexiBookApplication.getFlexiBook().getCustomers()) {
			for(TOAppointment app : FlexiBookController.getCustomerAppointments(cust.getUsername())) {
				ar.add(app);
			}
			}
			TOAppointment  toap = ar.get(selectedAppointment);
			
		FlexiBookController.registerNoShow(toap.getCustomerName(), 
				toap.getStartDate().toString(),
				toap.getStartTime().toString(),
				FlexiBookApplication.getSystemDate(),
				FlexiBookApplication.getSystemTime());
		success = "You have successfully registered a no show for the appointment " + toap.getService() + " with the customer " + toap.getCustomerName();
				
		}
		catch(InvalidInputException e) {
		
			error += e.getMessage();
		}
		}
		refreshAppointmentStatusPage();
		
	}
	/**
	 * @author yasminamatta
	 */
	
	private void refreshAppointmentStatusPage() {
		setBounds(350,150,700,500);
	
		
		if(!error.equals("")) {
			message.setText(error);
			message.setForeground(Color.RED);
		}

		if(!success.equals("")) {
			message.setText(success);
			message.setForeground(Color.GREEN);
		}
		if(appointmentList!=null) {
			appointmentList.removeAllItems();
		}
		
		
		
		int year = appointmentDatePicker.getModel().getYear();
		int month = appointmentDatePicker.getModel().getMonth()+1;
		int day = appointmentDatePicker.getModel().getDay();
		
		String fulldate = year +"-"+month+"-" +day;
		Date dateOfPicker = Date.valueOf(fulldate);
		ArrayList <String> appInOrderOfDate = new ArrayList<String>();
		error="";
		success="";
		boolean flag = false;
		for(Customer user : FlexiBookApplication.getFlexiBook().getCustomers()) {
			
			for(TOAppointment app : FlexiBookController.getCustomerAppointments(user.getUsername())) {
				if(app.getStartDate().equals(dateOfPicker.toString())){
					flag = true;
				String fullInfo = app.getStartTime()  + "|" + app.getService() +"|"+ app.getCustomerName()+".";
				
//				if(appInOrderOfDate.isEmpty()) {
					appInOrderOfDate.add(fullInfo);
//				}
//				else {
//						appInOrderOfDate.add(fullInfo);
//						appInOrderOfDate = sortArray(appInOrderOfDate);
//					
//						}
					}
				
				
			}
			
			
				}
	
				for (String str : appInOrderOfDate) {
					appointmentList.addItem(str);
				}
			if(flag) {
				appointmentList.setSelectedIndex(-1);
			}
		
		//pack();
	}
	
	/**
	 * @author yasminamatta
	 */
	private static ArrayList<String> sortArray (ArrayList<String> list) {
		for(int i=0 ; i<list.size()-1; i++) {
			String inputTime = list.get(i).substring(0, 5);
			Time timeFirst = Time.valueOf(inputTime+":00");
			String nextInput = list.get(i+1).substring(0, 5);
			Time timeNext = Time.valueOf(nextInput+":00");
			
			if(timeNext.before(timeFirst)) {
				String temp = list.get(i+1);
				list.set(i+1, list.get(i));
				list.set(i, temp);
				return sortArray(list);
			}
			
		}
		
		return list;
		
	}
	
	private void chooseDatePage(ActionEvent evt) {
		
		error = "";
		success = ""; 

		getContentPane().removeAll(); 
		getContentPane().repaint();
		setBounds(350,150,700,500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		
		
		datePickerButtonInPage = new JButton();
		datePickerButtonInPage.setText("Manage appointments of this day");
		appointmentDateLabel = new JLabel();
		appointmentDateLabel.setText("Choose a day:");
		SqlDateModel overviewModel = new SqlDateModel();
		LocalDate now = LocalDate.now();
		overviewModel.setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
		overviewModel.setSelected(true);
		Properties pO = new Properties();
		pO.put("text.today", "Today");
		pO.put("text.month", "Month");
		pO.put("text.year", "Year");
		JDatePanelImpl overviewDatePanel = new JDatePanelImpl(overviewModel, pO);
		appointmentDatePicker = new JDatePickerImpl(overviewDatePanel, new DateLabelFormatter());
		backDatePickerButton = new JButton();
		backDatePickerButton.setText("Back");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()

					.addGap(50)
					
					
					.addGroup(layout.createParallelGroup()
							
							.addComponent(message)
							.addComponent(appointmentDateLabel)	)

					.addGroup(layout.createParallelGroup()
																
							
							.addComponent(appointmentDatePicker)
							
							.addComponent(datePickerButtonInPage)
							.addComponent(backDatePickerButton))

							

						)	
					
				);
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentDateLabel,appointmentDateLabel,datePickerButtonInPage,backDatePickerButton});
	//	layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
//		//layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		//layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {appointmentListLabel,appointmentList,startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {appointmentDateLabel,appointmentDateLabel,datePickerButtonInPage,backDatePickerButton});
		//layout.linkSize(SwingConstants.VERTICAL,new java.awt.Component[] {startAppointmentButton,endAppointmentButton,noShowButton,backToMenuButton});
//		
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				
				.addGroup(layout.createSequentialGroup()
						.addGap(30)
						.addComponent(message))
				
				.addGroup(layout.createParallelGroup()
						.addComponent(appointmentDateLabel)
						.addComponent(appointmentDatePicker))						
				.addGroup(layout.createParallelGroup()
						
				
						.addComponent(datePickerButtonInPage))
				.addGroup(layout.createParallelGroup()
						.addComponent(backDatePickerButton))
					
				);
		 
		
		datePickerButtonInPage.addActionListener(new ActionListener(){ 
			
			public void actionPerformed(ActionEvent e) {
				
				managedAppointmentStatus(e);		
			}
		});
		backDatePickerButton.addActionListener(new ActionListener(){ 
			
			public void actionPerformed(ActionEvent e) {
				
				initOwnerMenu();		
			}
		});
		
		refreshAppointmentStatusPage();
	}
	
	/**
	 * @author artus
	 * 
	 */

	private void updateOwnerAccount() {
			
			getContentPane().removeAll(); 
			getContentPane().repaint();
			setBounds(350,150,700,500);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			//error="";
			//success ="";
			
			
//			oldOwnerUsernameLabel = new JLabel();
//			oldOwnerUsernameLabel.setText("Owner Username :");
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
//						        .addComponent(oldOwnerUsernameLabel)
								.addComponent(newOwnerPasswordLabel))
						.addGroup(layout.createParallelGroup()
								.addComponent(message)
//								.addComponent(oldOwnerUsername)
								.addComponent(newOwnerPassword)
								.addComponent(ownerUpdateButton)
								.addComponent(backOwnerButton))
					);
			

			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newOwnerPasswordLabel});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newOwnerPassword, ownerUpdateButton, backOwnerButton});
			layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {newOwnerPassword, ownerUpdateButton, backOwnerButton});




			layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addGap(40)
					.addComponent(message)
					//.addGroup(layout.createParallelGroup()
							//.addComponent(oldOwnerUsernameLabel))
							///.addComponent(oldOwnerUsername))
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
//				    String username = oldOwnerUsername.getText();
				    String password = newOwnerPassword.getText();
				    error="";
				    success="";
//				    if (!username.equals("owner")) {
//				    	error = "The username should be 'owner'";
//				    }
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
			setBounds(350,150,700,500);
			JButton customerUpdateButton;
			oldCustomerUsernameLabel = new JLabel();
			oldCustomerUsernameLabel.setText("Old Username :");
			newCustomerUsernameLabel = new JLabel();
			newCustomerUsernameLabel.setText("New Username :");
			newCustomerPasswordLabel = new JLabel();
			newCustomerPasswordLabel.setText("New Password :");
			customerUpdateButton = new JButton();
			customerUpdateButton.setText("Update Account");
			deleteAccount = new JButton();
			deleteAccount.setText("Delete account");
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
								.addComponent(deleteAccount)
								.addComponent(backCustomerButton))
					);
			

			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.HORIZONTAL,new java.awt.Component[] {message});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldCustomerUsernameLabel, newCustomerUsernameLabel,newCustomerPasswordLabel});
			layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {oldCustomerUsername, newCustomerUsername,newCustomerPassword, customerUpdateButton, deleteAccount, backCustomerButton});
			layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {oldCustomerUsername, newCustomerUsername,newCustomerPassword, customerUpdateButton, deleteAccount, backCustomerButton});




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
					.addComponent(deleteAccount)
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
			
			deleteAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					message.setText("");
					deleteAccount();
					String username1 = FlexiBookApplication.getCurrentUser().getUsername();
					initializeLoginPage();
					
					
				}
			});
			
			//pack();
		
	}
	private void deleteAccount() {
		String username = FlexiBookApplication.getCurrentUser().getUsername();
		try {
			FlexiBookController.deleteCustomerAccount(username);
			initializeLoginPage();
		} catch (InvalidInputException e1) {
			error = e1.getMessage();					
		}
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
		setBounds(350,150,700,500);
		
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
		setBounds(350,150,700,500);
		
		if(!error.equals("")) {
			message.setForeground(Color.RED);
			message.setText(error);
			
		}

		if(!success.equals("")) {
			message.setForeground(Color.GREEN);
			message.setText(success);
			
		}
		if(error.equals("")) {
			//oldOwnerUsername.setText("");
			newOwnerPassword.setText("");
			
		
			
		}
	}
	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initCalenderComponents() {
    	setBounds(350,150,700,500);

    	getContentPane().removeAll();
    	getContentPane().repaint();
    	
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 255));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel1.setText("FlexiBook");
        jLabel1.setForeground(Color.WHITE);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel4.setText("View Appointment Calendar");
        jLabel4.setForeground(Color.WHITE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(118, 118, 118))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addContainerGap(27, Short.MAX_VALUE))
        );

        jCalendar1.setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initOwnerMenu();
            }
        });

        jLabel2.setText("View available timeSlots");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jScrollPane3.setViewportView(jScrollPane2);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList2);

        jScrollPane5.setViewportView(jScrollPane4);

        jLabel3.setText("View unavailable timeSlots");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3)
                                        .addComponent(jScrollPane5)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 18, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                        .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
        );

       // pack();
    }// </editor-fold>

   /*
    
   
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FlexiBookPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FlexiBookPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FlexiBookPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FlexiBookPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        
        //</editor-fold>

//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new viewAppointment().setVisible(true);
//            }
//        });
    
    
    
	
}