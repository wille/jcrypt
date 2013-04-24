package com.redpois0n.panels;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import com.redpois0n.ClassSelectEvent;
import com.redpois0n.DialogSelectClass;


@SuppressWarnings("serial")
public class Panel4Exclude extends PanelBase {
	
	private JList<String> list;
	private DefaultListModel<String> model = new DefaultListModel<String>();

	public Panel4Exclude() {
		super("Excluded Classes");
		
		JLabel lblClassesToNot = new JLabel("Classes to not encrypt, load normally");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblClassesToNot)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblClassesToNot)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		
		list = new JList<String>(model);
		list.setCellRenderer(new DefaultListCellRenderer() {
			@Override
		    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        label.setIcon(DialogSelectClass.CLASS_ICON);
		        return label;
		    }
		});
		scrollPane.setViewportView(list);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		JMenuItem mntmAdd = new JMenuItem("Add");
		mntmAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DialogSelectClass dialog = new DialogSelectClass(new ClassSelectEvent() {
					@Override
					public void onSelect(String clazz) {
						model.addElement(clazz);
					}			
				});
				
				dialog.setVisible(true);
			}
		});
		popupMenu.add(mntmAdd);
		
		popupMenu.addSeparator();
		
		JMenuItem mntmRemove = new JMenuItem("Remove");
		mntmRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int[] lines = list.getSelectedIndices();
				
				for (int i : lines) {
					model.remove(i);
				}
			}
		});
		popupMenu.add(mntmRemove);
		
		JMenuItem mntmRemoveAll = new JMenuItem("Remove all");
		mntmRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (model.getSize() > 0) {
					model.remove(0);
				}
			}
		});
		popupMenu.add(mntmRemoveAll);
		setLayout(groupLayout);
	}

	public String[] getExcluded() {
		List<String> al = new ArrayList<String>();
		for (int x = 0; x < model.size(); x++) {
			String mo = (String) model.elementAt(x);
			al.add(mo);
		}
		
		return al.toArray(new String[0]);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
