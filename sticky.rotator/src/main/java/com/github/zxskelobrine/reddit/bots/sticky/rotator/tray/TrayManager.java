package com.github.zxskelobrine.reddit.bots.sticky.rotator.tray;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.github.zxskelobrine.reddit.bots.sticky.rotator.windows.Rotator;

public class TrayManager {

	public static final String ICON_LOCATION = "/resources/icons/icon.png";

	// public static final String ICON_LOCATION = "/icons/icon.png";

	public static boolean initializeTray() {
		if (!SystemTray.isSupported()) return false;
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage(ICON_LOCATION, "tray icon"));
		final SystemTray tray = SystemTray.getSystemTray();
		trayIcon.addMouseListener(new ClickListener() {
			@Override
			public void doubleClick(MouseEvent e) {
				Rotator.instance.setVisible(true);
			}
		});
		trayIcon.setPopupMenu(popup);
		try {
			tray.add(trayIcon);
			return true;
		} catch (AWTException e) {
			return false;
		}
	}

	public static BufferedImage createImage(String path, String description) {
		URL imageURL = TrayManager.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			try {
				BufferedImage bufferedImage = ImageIO.read(imageURL);
				BufferedImage returnable = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
				returnable.createGraphics().drawImage(bufferedImage, 0, 0, 16, 16, null);
				return returnable;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}

class ClickListener extends MouseAdapter implements ActionListener {
	private final static int clickInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

	MouseEvent lastEvent;
	Timer timer;

	public ClickListener() {
		this(clickInterval);
	}

	public ClickListener(int delay) {
		timer = new Timer(delay, this);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 2) return;

		lastEvent = e;

		if (timer.isRunning()) {
			timer.stop();
			doubleClick(lastEvent);
		} else {
			timer.restart();
		}
	}

	public void actionPerformed(ActionEvent e) {
		timer.stop();
		singleClick(lastEvent);
	}

	public void singleClick(MouseEvent e) {
	}

	public void doubleClick(MouseEvent e) {
	}

}
