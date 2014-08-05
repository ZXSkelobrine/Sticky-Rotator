package com.github.zxskelobrine.reddit.bots.sticky.rotator.windows;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.cd.reddit.RedditException;
import com.github.zxskelobrine.reddit.bots.sticky.rotator.reddit.RedditManager;
import com.github.zxskelobrine.reddit.bots.sticky.rotator.tray.TrayManager;

public class Login extends JFrame {

	private static final long serialVersionUID = -7740829370570981136L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField pswdPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		setIconImage(TrayManager.createImage(TrayManager.ICON_LOCATION, "Icon Image"));
		setTitle("Sticky Rotator - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 333, 183);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setBounds(135, 11, 46, 19);
		contentPane.add(lblLogin);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 44, 83, 14);
		contentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 85, 83, 14);
		contentPane.add(lblPassword);

		txtUsername = new JTextField();
		txtUsername.setBounds(103, 41, 204, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		pswdPassword = new JPasswordField();
		pswdPassword.setBounds(103, 82, 204, 20);
		contentPane.add(pswdPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RedditManager.configureReddit(txtUsername.getText(), pswdPassword.getPassword());
					Rotator.main(null);
					dispose();
				} catch (RedditException e1) {
					pswdPassword.setText("");
				}
			}
		});
		btnLogin.setBounds(113, 113, 89, 23);
		contentPane.add(btnLogin);
	}
}
