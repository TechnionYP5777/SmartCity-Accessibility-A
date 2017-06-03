package EmployeeGui;

import org.apache.log4j.PropertyConfigurator;
import com.sun.javafx.application.LauncherImpl;

import CommonDI.CommonDiConfigurator;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeCommon.IEmployeeScreensParameterService;
import EmployeeDI.EmployeeDiConfigurator;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.stage.Stage;

/**
 * EmployeeApplicationScreen - This class is the stage GUI class for Employee
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 */

@SuppressWarnings("restriction")
public class EmployeeApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;
	
	static boolean show;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new EmployeeDiConfigurator(), new CommonDiConfigurator());
			
			IEmployeeScreensParameterService employeeScreensParameterService = InjectionFactory
					.getInstance(EmployeeScreensParameterService.class);
			
			employeeScreensParameterService.setNotShowMainScreenVideo(show);
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();
			setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
				
			stage.show();

		} catch (Exception e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
		}
	}

	public static void main(String[] args) {
			
		if (args.length == 0)
			show = true;
		else {
			if (args.length != 1 || !"notShow".equals(args[0]))
				throw new RuntimeException("Wrong parameters! please " + "choose: notShow");
			show = false;
		}

		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");

		/* lunching program with preloader */
		LauncherImpl.launchApplication(EmployeeApplicationScreen.class, EmployeeGuiPreloader.class, args);
	}

}
