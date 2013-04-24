package com.redpois0n.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.Util;


@SuppressWarnings("serial")
public class Panel1SelectJar extends PanelBase {
	
	private JComboBox<Object> cbPath;
	
	public File getFile() {
		return new File(cbPath.getSelectedItem().toString());
	}

	public Panel1SelectJar() {
		super("Select JAR file");
		
		JLabel lblFilePathTo = new JLabel("File path to JAR file");
		
		cbPath = new JComboBox<Object>();
		
		cbPath.setEditable(true);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = Util.showOpenDialog();
				if (file != null) {
					cbPath.setSelectedItem(file.getAbsolutePath());					
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFilePathTo)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(cbPath, 0, 340, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBrowse)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblFilePathTo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse))
					.addContainerGap(123, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}
}
