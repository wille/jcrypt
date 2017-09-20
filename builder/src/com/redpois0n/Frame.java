package com.redpois0n;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.redpois0n.panels.Panel1SelectJar;
import com.redpois0n.panels.Panel2MainClass;
import com.redpois0n.panels.Panel3Encryption;
import com.redpois0n.panels.Panel4Create;
import com.redpois0n.panels.Panel5Build;
import com.redpois0n.panels.PanelBase;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	
	public static final List<PanelBase> panels = new ArrayList<PanelBase>();
	public static int currentPanel = 0;
	public static Frame instance;

	private JPanel contentPane;
	private JButton btnBack;
	private JButton btnNext;
	private JPanel panel;
	private JLabel lblTitle;

	public Frame() {
		instance = this;
		setIconImage(Toolkit.getDefaultToolkit().getImage(Frame.class.getResource("/com/redpois0n/icons/icon.png")));
		setTitle("jCrypt " + Main.getVersion() + " - 1/5");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				next();
			}
		});
		btnNext.setBounds(295, 238, 55, 23);
		
		btnBack = new JButton("Back");
		btnBack.setEnabled(false);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});
		btnBack.setBounds(230, 238, 55, 23);
		contentPane.setLayout(null);
		contentPane.add(btnNext);
		contentPane.add(btnBack);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(360, 238, 74, 23);
		contentPane.add(btnCancel);
		
		panel = new JPanel();
		panel.setBounds(0, 52, 444, 173);
		contentPane.add(panel);		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setForeground(Color.WHITE);
		panel_1.setBounds(0, 0, 444, 50);
		contentPane.add(panel_1);
		
		lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle)
					.addContainerGap(388, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTitle)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 50, 444, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 225, 444, 2);
		contentPane.add(separator_1);
		
		loadPanels();
		
		panelChanged(panels.get(0));
	}
	
	public void loadPanels() {
		panels.clear();
		panels.add(new Panel1SelectJar());
		panels.add(new Panel2MainClass());
		panels.add(new Panel3Encryption());
		panels.add(new Panel4Create());
		panels.add(new Panel5Build());
	}
	
	public void next() {
		if (currentPanel < panels.size() - 1) {
			panelChanged(panels.get(++currentPanel));
		}
		
		btnNext.setEnabled(currentPanel < panels.size() - 1);
		btnBack.setEnabled(currentPanel > 0);
	}
	
	public void back() {
		if (currentPanel > 0) {
			panelChanged(panels.get(--currentPanel));
		}
		btnNext.setEnabled(currentPanel < panels.size() - 1);
		btnBack.setEnabled(currentPanel > 0);
	}
	
	public void panelChanged(Component com) {
		panel.removeAll();
		panel.add(com);
		panel.revalidate();
		panel.repaint();
		
		lblTitle.setText(((PanelBase)com).getTitle());
		
		((PanelBase)com).opened();
		
		setTitle("jCrypt " + Main.getVersion() + " - " + (currentPanel + 1) + "/" + panels.size());
	}
}
