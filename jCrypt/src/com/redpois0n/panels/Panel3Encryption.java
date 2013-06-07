package com.redpois0n.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.Util;
import com.redpois0n.crypto.Crypto;

@SuppressWarnings("serial")
public class Panel3Encryption extends PanelBase {
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField cbKey;
	private JRadioButton rdbtnEncryptResourcesAnd;
	private JRadioButton rdbtnEncryptClassesresources;
	private JCheckBox chckbxDefaultKey;

	public Panel3Encryption() {
		super("Encryption");
		
		rdbtnEncryptClassesresources = new JRadioButton("Encrypt classes (Resources will be in .jar)");
		buttonGroup.add(rdbtnEncryptClassesresources);
		rdbtnEncryptClassesresources.setSelected(true);
		
		rdbtnEncryptResourcesAnd = new JRadioButton("Encrypt resources and classes");
		buttonGroup.add(rdbtnEncryptResourcesAnd);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		JLabel lblEncryptionKey = new JLabel("Encryption key");
		
		cbKey = new JTextField();
		cbKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update();	
			}
		});
		cbKey.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				update();
			}
		});
		cbKey.setEditable(true);
		cbKey.setText(Util.randomString(Crypto.KEY_LENGTH));
		
		chckbxDefaultKey = new JCheckBox("Default key");
		chckbxDefaultKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbKey.setEnabled(!chckbxDefaultKey.isSelected());
			}
		});
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnEncryptClassesresources)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEncryptionKey)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(chckbxDefaultKey)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(toolBar_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addComponent(rdbtnEncryptResourcesAnd, Alignment.LEADING))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(cbKey, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(191, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(18)
					.addComponent(rdbtnEncryptClassesresources)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(rdbtnEncryptResourcesAnd)
						.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblEncryptionKey)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(toolBar_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(chckbxDefaultKey, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		
		JButton button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbKey.setText(Util.randomString(Crypto.KEY_LENGTH));
				update();
			}
		});
		button_2.setIcon(new ImageIcon(Panel3Encryption.class.getResource("/com/redpois0n/icons/update.png")));
		button_2.setToolTipText("Random");
		toolBar_1.add(button_2);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "If you are encrypting ALL resources, the encrypted program\n" +
						"cant access Class.getResource() because you cant get an URL from a object in memory that is not written to disk.\n\n" +
						"Only do this if the program does not use that method", "jCrypt", JOptionPane.WARNING_MESSAGE);
			}
		});
		button.setIcon(new ImageIcon(Panel3Encryption.class.getResource("/com/redpois0n/icons/exclamation-circle-frame.png")));
		toolBar.add(button);
		setLayout(groupLayout);
		
		update();
	}

	public String getKey() {
		return chckbxDefaultKey.isSelected() ? null : cbKey.getText().trim();
	}

	public boolean shouldEncryptAll() {
		return rdbtnEncryptResourcesAnd.isSelected();
	}
	
	public void update() {
		String text = cbKey.getText();
		
		if (text.length() == Crypto.KEY_LENGTH) {
			cbKey.setForeground(Color.green);
		} else {
			cbKey.setForeground(Color.red);
		}
	}
}
