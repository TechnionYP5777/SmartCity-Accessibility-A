package CustomerGuiScreens;

import org.apache.log4j.PropertyConfigurator;

import CustomerDI.CustomerDiConfigurator;
import CustomerImplementations.CustomerDefs;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.stage.Stage;

/**
 * CustomerApplicationScreen - This class is the stage GUI class for Customer
 * 
 * @author Lior Ben Ami
 * @since 2017-01-13 */
public class CustomerApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new CustomerDiConfigurator());
			
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();

			setScene("/CustomerWelcomeScreen/CustomerWelcomeScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
			stage.show();

			
		} catch (Exception ¢) {
			throw new RuntimeException();
		}
	}

	public static void main(String[] args) {
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");
		
		/* Allowing to change IP and port from command line */
		if (args.length >= 1)
			CustomerDefs.host = args[0];
		
		if (args.length >= 2)
			CustomerDefs.port = Integer.parseInt(args[1]);
		
		launch(args);
	}
}
