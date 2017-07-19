package com.stu.infra.cdc.sms;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class MainUI {

	private JFrame frmSmsCdcApp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.frmSmsCdcApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSmsCdcApp = new JFrame();
		frmSmsCdcApp.setTitle("SMS CDC App");
		frmSmsCdcApp.setBounds(100, 100, 450, 300);
		frmSmsCdcApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
