package com.redpois0n.panels;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class PanelBase extends JPanel {

	private String title;
	
	public PanelBase(String title) {
		super.setSize(444, 177);
		
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void opened() {
		
	}

}
