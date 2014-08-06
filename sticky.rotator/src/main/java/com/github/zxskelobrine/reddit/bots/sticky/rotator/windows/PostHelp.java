package com.github.zxskelobrine.reddit.bots.sticky.rotator.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class PostHelp extends JFrame {

	private static final long serialVersionUID = -7720057422631268774L;

	private final String POST_INFO_PATH = "/resources/icons/postInfo.png";// Release.
	// private final String POST_INFO_PATH = "/icons/postInfo.png";//
	// Development.
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PostHelp frame = new PostHelp();
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
	public PostHelp() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 641, 102);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel label = new JLabel("");
		try {
			label.setIcon(new ImageIcon(ImageIO.read(PostHelp.class.getResource(POST_INFO_PATH))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		contentPane.add(label, BorderLayout.CENTER);
	}

}
