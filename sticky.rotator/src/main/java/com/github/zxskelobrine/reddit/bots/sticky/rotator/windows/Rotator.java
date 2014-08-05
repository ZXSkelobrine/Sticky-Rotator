package com.github.zxskelobrine.reddit.bots.sticky.rotator.windows;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.cd.reddit.RedditException;
import com.github.zxskelobrine.reddit.bots.sticky.rotator.StickyPost;
import com.github.zxskelobrine.reddit.bots.sticky.rotator.reddit.RedditManager;
import com.github.zxskelobrine.reddit.bots.sticky.rotator.tray.TrayManager;

public class Rotator extends JFrame {

	private JPanel rotationalSettings;
	private JPanel contentPane;
	private JPanel panel;
	private JPanel stickyChooser;

	private JTextField txtSubreddit;
	private JTextField txtRotationTime;

	private JButton btnSet;
	private JButton btnStopRotation;
	private JButton btnStartRotation;
	private JButton btnListPosts;
	private JButton btnAdd;
	private JButton btnRemove;

	private JComboBox<String> cmbTimeScale;

	private String[] cmbValues;

	private JLabel lblRotationalDelay;
	private JLabel lblr;
	private JLabel lblSubreddit;

	private JCheckBox chckbxCurrentRotationStatus;

	private JList<StickyPost> lstSelected;
	private JList<StickyPost> lstPosts;

	private GridBagLayout gbl_contentPane;
	private GridBagLayout gbl_panel;
	private GridBagLayout gbl_rotationalSettings;
	private GridBagLayout gbl_stickyChooser;

	private GridBagConstraints gbc_stickyChooser;
	private GridBagConstraints gbc_lstSelected;
	private GridBagConstraints gbc_btnRemove;
	private GridBagConstraints gbc_btnAdd;
	private GridBagConstraints gbc_lstPosts;
	private GridBagConstraints gbc_rotationalSettings;
	private GridBagConstraints gbc_lblSubreddit;
	private GridBagConstraints gbc_lblr;
	private GridBagConstraints gbc_txtSubreddit;
	private GridBagConstraints gbc_btnListPosts;
	private GridBagConstraints gbc_btnStartRotation;
	private GridBagConstraints gbc_btnStopRotation;
	private GridBagConstraints gbc_chckbxCurrentRotationStatus;
	private GridBagConstraints gbc_panel;
	private GridBagConstraints gbc_lblRotationalDelay;
	private GridBagConstraints gbc_txtRotationTime;
	private GridBagConstraints gbc_cmbTimeScale;
	private GridBagConstraints gbc_btnSet;

	private static final long serialVersionUID = 1L;
	private long millisDelay = -1;

	private Thread thread;

	private boolean threadRunning = false;

	private List<StickyPost> selectedPosts = new ArrayList<StickyPost>();
	private List<StickyPost> subredditPosts = new ArrayList<StickyPost>();

	public static Rotator instance;
	private JTextPane txtpnLog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Rotator frame = new Rotator();
					instance = frame;
					System.out.println(TrayManager.initializeTray() ? "Tray initialized successfully" : "Tray initiaization failed.");
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
	public Rotator() {
		setIconImage(TrayManager.createImage(TrayManager.ICON_LOCATION, "Icon Image"));
		setTitle("Sticky Rotator - Control");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 837, 521);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 497, 176, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		stickyChooser = new JPanel();
		gbc_stickyChooser = new GridBagConstraints();
		gbc_stickyChooser.gridheight = 2;
		gbc_stickyChooser.insets = new Insets(0, 0, 0, 5);
		gbc_stickyChooser.fill = GridBagConstraints.BOTH;
		gbc_stickyChooser.gridx = 0;
		gbc_stickyChooser.gridy = 0;
		contentPane.add(stickyChooser, gbc_stickyChooser);
		gbl_stickyChooser = new GridBagLayout();
		gbl_stickyChooser.columnWidths = new int[] { 225, 0, 225, 0 };
		gbl_stickyChooser.rowHeights = new int[] { 0, 237, 0 };
		gbl_stickyChooser.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_stickyChooser.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		stickyChooser.setLayout(gbl_stickyChooser);

		lstSelected = new JList<StickyPost>();
		gbc_lstSelected = new GridBagConstraints();
		gbc_lstSelected.gridheight = 2;
		gbc_lstSelected.insets = new Insets(0, 0, 0, 5);
		gbc_lstSelected.fill = GridBagConstraints.BOTH;
		gbc_lstSelected.gridx = 0;
		gbc_lstSelected.gridy = 0;
		stickyChooser.add(lstSelected, gbc_lstSelected);

		btnRemove = new JButton(">>");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lstSelected.getSelectedValue() != null) {
					log("Post: " + lstSelected.getSelectedValue().getPostID() + " added.");
					subredditPosts.add(lstSelected.getSelectedValue());
					selectedPosts.remove(lstSelected.getSelectedValue());
					lstPosts.setListData(RedditManager.getArrayFromList(subredditPosts));
					lstSelected.setListData(RedditManager.getArrayFromList(selectedPosts));
				}
			}
		});
		gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.anchor = GridBagConstraints.SOUTH;
		gbc_btnRemove.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 0;
		stickyChooser.add(btnRemove, gbc_btnRemove);

		btnAdd = new JButton("<<");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lstPosts.getSelectedValue() != null) {
					log("Post: " + lstPosts.getSelectedValue().getPostID() + " added.");
					selectedPosts.add(lstPosts.getSelectedValue());
					subredditPosts.remove(lstPosts.getSelectedValue());
					lstPosts.setListData(RedditManager.getArrayFromList(subredditPosts));
					lstSelected.setListData(RedditManager.getArrayFromList(selectedPosts));
				}
			}
		});
		gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.NORTH;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 1;
		stickyChooser.add(btnAdd, gbc_btnAdd);

		lstPosts = new JList<StickyPost>();
		lstPosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gbc_lstPosts = new GridBagConstraints();
		gbc_lstPosts.gridheight = 2;
		gbc_lstPosts.fill = GridBagConstraints.BOTH;
		gbc_lstPosts.gridx = 2;
		gbc_lstPosts.gridy = 0;
		stickyChooser.add(lstPosts, gbc_lstPosts);

		rotationalSettings = new JPanel();
		gbc_rotationalSettings = new GridBagConstraints();
		gbc_rotationalSettings.fill = GridBagConstraints.BOTH;
		gbc_rotationalSettings.gridx = 1;
		gbc_rotationalSettings.gridy = 0;
		contentPane.add(rotationalSettings, gbc_rotationalSettings);
		gbl_rotationalSettings = new GridBagLayout();
		gbl_rotationalSettings.columnWidths = new int[] { 89, 27, 218, 0 };
		gbl_rotationalSettings.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_rotationalSettings.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_rotationalSettings.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		rotationalSettings.setLayout(gbl_rotationalSettings);

		lblSubreddit = new JLabel("Subreddit:");
		gbc_lblSubreddit = new GridBagConstraints();
		gbc_lblSubreddit.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSubreddit.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubreddit.gridx = 0;
		gbc_lblSubreddit.gridy = 0;
		rotationalSettings.add(lblSubreddit, gbc_lblSubreddit);

		lblr = new JLabel("/r/");
		gbc_lblr = new GridBagConstraints();
		gbc_lblr.anchor = GridBagConstraints.EAST;
		gbc_lblr.insets = new Insets(0, 0, 5, 5);
		gbc_lblr.gridx = 1;
		gbc_lblr.gridy = 0;
		rotationalSettings.add(lblr, gbc_lblr);

		txtSubreddit = new JTextField();
		gbc_txtSubreddit = new GridBagConstraints();
		gbc_txtSubreddit.insets = new Insets(0, 0, 5, 0);
		gbc_txtSubreddit.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSubreddit.gridx = 2;
		gbc_txtSubreddit.gridy = 0;
		rotationalSettings.add(txtSubreddit, gbc_txtSubreddit);
		txtSubreddit.setColumns(10);

		btnListPosts = new JButton("List Posts");
		btnListPosts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subredditPosts = RedditManager.getSubredditListing(txtSubreddit.getText());
					lstPosts.setListData(RedditManager.getArrayFromList(subredditPosts));
					log("Posts listed.");
				} catch (RedditException e1) {
					e1.printStackTrace();
				}
			}
		});
		gbc_btnListPosts = new GridBagConstraints();
		gbc_btnListPosts.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnListPosts.insets = new Insets(0, 0, 5, 5);
		gbc_btnListPosts.gridx = 0;
		gbc_btnListPosts.gridy = 1;
		rotationalSettings.add(btnListPosts, gbc_btnListPosts);

		btnStartRotation = new JButton("Start Rotation");
		btnStartRotation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (millisDelay != -1 && selectedPosts.size() > 0) {
					btnStartRotation.setEnabled(false);
					btnListPosts.setEnabled(false);
					btnAdd.setEnabled(false);
					btnRemove.setEnabled(false);
					btnSet.setEnabled(false);

					btnStopRotation.setEnabled(true);
					txtRotationTime.setEnabled(false);
					txtSubreddit.setEnabled(false);
					cmbTimeScale.setEnabled(false);
					lstPosts.setEnabled(false);
					lstSelected.setEnabled(false);

					chckbxCurrentRotationStatus.setSelected(true);
					startRotationalThread();
					log("Rotation started.");
				}
			}
		});
		gbc_btnStartRotation = new GridBagConstraints();
		gbc_btnStartRotation.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartRotation.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartRotation.gridwidth = 3;
		gbc_btnStartRotation.gridx = 0;
		gbc_btnStartRotation.gridy = 3;
		rotationalSettings.add(btnStartRotation, gbc_btnStartRotation);

		btnStopRotation = new JButton("Stop Rotation");
		btnStopRotation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnStopRotation.setEnabled(false);

				txtRotationTime.setEnabled(true);
				txtSubreddit.setEnabled(true);
				cmbTimeScale.setEnabled(true);
				btnStartRotation.setEnabled(true);
				btnListPosts.setEnabled(true);
				btnAdd.setEnabled(true);
				btnRemove.setEnabled(true);
				btnSet.setEnabled(true);
				lstPosts.setEnabled(true);
				lstSelected.setEnabled(true);

				chckbxCurrentRotationStatus.setSelected(false);
				stopRotationalThread();
				log("Rotation stopped.");
			}
		});
		gbc_btnStopRotation = new GridBagConstraints();
		gbc_btnStopRotation.insets = new Insets(0, 0, 5, 0);
		gbc_btnStopRotation.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStopRotation.gridwidth = 3;
		gbc_btnStopRotation.gridx = 0;
		gbc_btnStopRotation.gridy = 4;
		rotationalSettings.add(btnStopRotation, gbc_btnStopRotation);

		chckbxCurrentRotationStatus = new JCheckBox("Current Rotation Status");
		chckbxCurrentRotationStatus.setEnabled(false);
		gbc_chckbxCurrentRotationStatus = new GridBagConstraints();
		gbc_chckbxCurrentRotationStatus.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxCurrentRotationStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxCurrentRotationStatus.gridwidth = 3;
		gbc_chckbxCurrentRotationStatus.gridx = 0;
		gbc_chckbxCurrentRotationStatus.gridy = 5;
		rotationalSettings.add(chckbxCurrentRotationStatus, gbc_chckbxCurrentRotationStatus);

		panel = new JPanel();
		gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 6;
		rotationalSettings.add(panel, gbc_panel);

		gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 80, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		lblRotationalDelay = new JLabel("Rotational Delay:");
		gbc_lblRotationalDelay = new GridBagConstraints();
		gbc_lblRotationalDelay.insets = new Insets(0, 0, 5, 5);
		gbc_lblRotationalDelay.anchor = GridBagConstraints.EAST;
		gbc_lblRotationalDelay.gridx = 0;
		gbc_lblRotationalDelay.gridy = 0;
		panel.add(lblRotationalDelay, gbc_lblRotationalDelay);

		txtRotationTime = new JTextField();
		gbc_txtRotationTime = new GridBagConstraints();
		gbc_txtRotationTime.insets = new Insets(0, 0, 5, 5);
		gbc_txtRotationTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRotationTime.gridx = 1;
		gbc_txtRotationTime.gridy = 0;
		panel.add(txtRotationTime, gbc_txtRotationTime);
		txtRotationTime.setColumns(10);

		cmbValues = new String[] { "Seconds", "Minutes", "Hours", "Days" };
		DefaultComboBoxModel<String> cmbModel = new DefaultComboBoxModel<>(cmbValues);

		cmbTimeScale = new JComboBox<String>();
		cmbTimeScale.setModel(cmbModel);
		gbc_cmbTimeScale = new GridBagConstraints();
		gbc_cmbTimeScale.insets = new Insets(0, 0, 5, 0);
		gbc_cmbTimeScale.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbTimeScale.gridx = 2;
		gbc_cmbTimeScale.gridy = 0;
		panel.add(cmbTimeScale, gbc_cmbTimeScale);

		btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			private final long SECOND_MULTIPLIER = 1000;
			private final long MINUTE_MULTIPIER = SECOND_MULTIPLIER * 60;
			private final long HOUR_MULTIPLIER = MINUTE_MULTIPIER * 60;
			private final long DAY_MUTIPLIER = HOUR_MULTIPLIER * 24;

			public void actionPerformed(ActionEvent e) {
				if (parseable(txtRotationTime.getText())) {
					int textTimeValue = Integer.parseInt(txtRotationTime.getText());
					switch ((String) cmbTimeScale.getSelectedItem()) {
					case "Seconds":
						millisDelay = textTimeValue * SECOND_MULTIPLIER;
						break;
					case "Minutes":
						millisDelay = textTimeValue * MINUTE_MULTIPIER;
						break;
					case "Hours":
						millisDelay = textTimeValue * HOUR_MULTIPLIER;
						break;
					case "Days":
						millisDelay = textTimeValue * DAY_MUTIPLIER;
						break;
					}
					log("Time set.");
				}
			}
		});
		gbc_btnSet = new GridBagConstraints();
		gbc_btnSet.insets = new Insets(0, 0, 5, 0);
		gbc_btnSet.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSet.gridx = 2;
		gbc_btnSet.gridy = 1;
		panel.add(btnSet, gbc_btnSet);

		txtpnLog = new JTextPane();
		txtpnLog.setEditable(false);
		GridBagConstraints gbc_txtpnLog = new GridBagConstraints();
		gbc_txtpnLog.gridwidth = 3;
		gbc_txtpnLog.insets = new Insets(0, 0, 0, 5);
		gbc_txtpnLog.fill = GridBagConstraints.BOTH;
		gbc_txtpnLog.gridx = 0;
		gbc_txtpnLog.gridy = 2;
		panel.add(txtpnLog, gbc_txtpnLog);

		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == ICONIFIED) {
					dispose();
				}
			}
		});
	}

	public void startRotationalThread() {
		if (thread == null) setupThread();
		threadRunning = true;
		thread.start();
	}

	public void stopRotationalThread() {
		if (thread != null) {
			threadRunning = false;
			thread.interrupt();
		}
	}

	public void setupThread() {
		thread = new Thread() {
			int currentIndex = 0;

			@Override
			public void run() {
				while (threadRunning) {
					if (currentIndex == selectedPosts.size()) currentIndex = 0;
					try {
						RedditManager.stickyPost(selectedPosts.get(currentIndex));
						log("Stick post update: successful.");
					} catch (RedditException e1) {
						log("Stick post update: failed - " + e1.getMessage());
					}
					currentIndex++;
					try {
						Thread.sleep(millisDelay);
					} catch (InterruptedException e) {
					}
				}
			}
		};
	}

	private boolean parseable(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void log(String message) {
		txtpnLog.setText(txtpnLog.getText() + message + "\n");

	}

}
