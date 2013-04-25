package com.redpois0n.panels;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.jar.JarFile;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.redpois0n.ClassSelectEvent;
import com.redpois0n.DialogSelectClass;
import com.redpois0n.Frame;


@SuppressWarnings("serial")
public class Panel2MainClass extends PanelBase {
	
	private JComboBox<Object> cbMainClass;
	
	public JComboBox<Object> getComboBox() {
		return cbMainClass;
	}

	public Panel2MainClass() {
		super("Program Entry Class");
		
		JLabel lblMainClassName = new JLabel("Main class name:");
		
		cbMainClass = new JComboBox<Object>();
		cbMainClass.setEditable(true);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogSelectClass d = new DialogSelectClass(new ClassSelectEvent() {
					public void onSelect(String clazz) {
						getComboBox().setSelectedItem(clazz);
					}
				});
				d.setVisible(true);
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(cbMainClass, GroupLayout.PREFERRED_SIZE, 332, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnBrowse))
						.addComponent(lblMainClassName))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addComponent(lblMainClassName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbMainClass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse))
					.addContainerGap(113, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}

	public String getMainClass() {
		return cbMainClass.getSelectedItem().toString().trim();
	}

	@Override
	public void opened() {
		try {
			Panel1SelectJar panel = (Panel1SelectJar) Frame.panels.get(0);
			
			JarFile jar = new JarFile(panel.getFile());
			Map<Object, Object> map = jar.getManifest().getMainAttributes();
			
			String mainClass = null;
			
			for (Object obj : map.keySet()) {
				if (obj.toString().equalsIgnoreCase("main-class")) {
					mainClass = map.get(obj).toString();
					break;
				}
			}
			
			Panel2MainClass panelMainClass = (Panel2MainClass) Frame.panels.get(1);
			
			panelMainClass.getComboBox().setSelectedItem(mainClass);
			
			jar.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed loading manifest: " + ex.getClass().getSimpleName() + ": " + ex.getMessage(), "jCrypt", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
