package ca.mcgill.ecse223.flexibook.view;


import java.awt.*;

import javax.swing.*;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.Customer;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.Owner;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse223.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse223.flexibook.controller.TOAppointment;
import ca.mcgill.ecse223.flexibook.controller.TOService;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;


import java.awt.event.ActionEvent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;



public class FlexiBookLogin {

    public JFrame frame;
    private JTextField UsernameLogin;
    private JTextField PasswordLogin;

    private JLabel errorMessage;
    private String error = "";
    private JTextField PasswordSignup;
    private JTextField UsernameSignup;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FlexiBookLogin window = new FlexiBookLogin();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public FlexiBookLogin() {
        FlexiBookPersistence.load();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {


       frame = new JFrame();
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        frame.setBounds(100, 100, 700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        panel.setBackground(Color.RED);
        panel.setPreferredSize(new Dimension(1000, 1000));

        frame.add(panel, BorderLayout.NORTH);


        JLabel lblWelcomeToBlock = new JLabel("Welcome to FlexiBook");
        lblWelcomeToBlock.setBackground(Color.BLACK);
        lblWelcomeToBlock.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        lblWelcomeToBlock.setHorizontalAlignment(SwingConstants.CENTER);

        UsernameLogin = new JTextField();
        UsernameLogin.setHorizontalAlignment(SwingConstants.LEFT);
        UsernameLogin.setColumns(10);

        PasswordLogin = new JPasswordField();
        PasswordLogin.setColumns(10);

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

        PasswordSignup = new JTextField();
        PasswordSignup.setColumns(10);

        UsernameSignup = new JTextField();
        UsernameSignup.setColumns(10);

        JLabel Username = new JLabel("Username :");
        Username.setFont(new Font("Lucida Grande", Font.PLAIN, 10));


        errorMessage = new JLabel("     ");
        errorMessage.setForeground(Color.RED);

        JButton Login = new JButton("Login");

        JButton Signup = new JButton("Sign Up");

        Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginActionPerformed(evt);
            }
        });
        Signup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignupActionPerformed(evt);
            }
        });

        //layout

        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
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
                                                                                .addComponent(UsernameLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                                .addComponent(loginPassword)
                                                                                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(PasswordLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(Login, Alignment.TRAILING))
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
                                                                .addComponent(UsernameSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)

                                                                .addComponent(PasswordSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(Signup, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)))
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
                                                        .addComponent(PasswordLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(signupPassword)
                                                        .addComponent(PasswordSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(ComponentPlacement.RELATED))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(UsernameLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(loginUsername)
                                                        .addComponent(UsernameSignup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Username, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE))
                                                .addGap(53)))
                                .addGap(13)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(Login)
                                        .addComponent(Signup))
                                .addGap(76)
                                .addComponent(errorMessage)
                                .addGap(557))
        );
        frame.getContentPane().setLayout(groupLayout);

    }

    public void refreshData() {
        errorMessage.setText(error);
        if (error == null || error.length() == 0) {
            UsernameSignup.setText("");
            PasswordSignup.setText("");

            UsernameLogin.setText("");
            PasswordLogin.setText("");
        }


    }
    public void SignupActionPerformed(ActionEvent evt) {
        error = "";
        if (error.length() == 0) {

            String username = UsernameSignup.getText();
            String Password = PasswordSignup.getText();
            try {
            	if (username.equals("owner") && Password.contentEquals("owner")) error = "owner cannot sign up";
            	else FlexiBookController.signUpCustomer(username, Password);
            } catch (InvalidInputException e) {
                error = e.getMessage();
            }
        }
        refreshData();
    }

   public void LoginActionPerformed(java.awt.event.ActionEvent evt) {
        error = "";
        if (error.length() == 0) {
            String username = UsernameLogin.getText();
            String password = PasswordLogin.getText();
            try {
                FlexiBookController.logIn(username, password);
                for(int i = 0; i< FlexiBookController.getCustomers().size(); i++) {
                    if (FlexiBookApplication.getCurrentUser() instanceof Customer) {
                        FlexiBookApplication.login.refreshData();
                        FlexiBookApplication.mainPage.setVisible(true);
                    }
                }

                if (FlexiBookApplication.getCurrentUser() instanceof Owner){
                    FlexiBookApplication.login.refreshData();
                    FlexiBookApplication.mainPage.setVisible(true);
                }
                FlexiBookApplication.login.frame.setVisible(false);

            } catch (InvalidInputException e) {
                error = e.getMessage();
            }
        }
        refreshData();

    }
}
