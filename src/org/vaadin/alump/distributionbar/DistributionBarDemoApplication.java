package org.vaadin.alump.distributionbar;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class DistributionBarDemoApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Vaadin Distribution Bar Demo");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
