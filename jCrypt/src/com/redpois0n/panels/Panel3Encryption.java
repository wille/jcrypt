package com.redpois0n.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class Panel3Encryption extends PanelBase {
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnEncryptResourcesAnd;
	private JRadioButton rdbtnEncryptClassesresources;

	public Panel3Encryption() {
		super("Encryption");
		
		rdbtnEncryptClassesresources = new JRadioButton("Encrypt classes (Resources will be in .jar)");
		buttonGroup.add(rdbtnEncryptClassesresources);
		rdbtnEncryptClassesresources.setSelected(true);
		
		rdbtnEncryptResourcesAnd = new JRadioButton("Encrypt resources and classes");
		buttonGroup.add(rdbtnEncryptResourcesAnd);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnEncryptClassesresources)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnEncryptResourcesAnd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
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
					.addContainerGap(113, Short.MAX_VALUE))
		);
		
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
	}

	public boolean shouldEncryptAll() {
		return rdbtnEncryptResourcesAnd.isSelected();
	}
}
