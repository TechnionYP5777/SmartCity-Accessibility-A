package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Login;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import SMExceptions.CommonExceptions.CriticalError;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
 * @author idan atias
 * @since 2017-03-25
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginTest {
	private ICustomer customer;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void loginSuccessfulTest() {
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
						Serialization.serialize(new Login("Guest", "Guest"))).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(new CustomerProfile("Guest"))).serialize());
			
		} catch (IOException ¢) {
			fail();
		}
		try {
			customer.login("Guest", "Guest", false);
		} catch (CriticalError | AuthenticationError ¢) {
			fail();
		}
	}
	
	@Test
	public void loginCriticalErrorTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
							Serialization.serialize(new Login("Guest", "Guest"))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			customer.login("Guest", "Guest", false);
		} catch (AuthenticationError ¢) {
			fail();
		} catch (CriticalError ¢) {
		/* Test Passed */
		}
	}
	
	@Test
	public void AuthenticationErrorPasswordIncorrect() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
							Serialization.serialize(new Login("Guest", "GuesT"))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			customer.login("Guest", "GuesT", false);
		} catch (CriticalError ¢) {
			fail();
		} catch (AuthenticationError ¢) {
		/* Test Passed */
		}
	}
	
	@Test
	public void AuthenticationErrorUserNameIncorrect() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
							Serialization.serialize(new Login("Guesttt", "Guest"))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			customer.login("Guesttt", "Guest", false);
		} catch (CriticalError ¢) {
			fail();
		} catch (AuthenticationError ¢) {
		/* Test Passed */
		}
	}
	
	@Test
	public void ConnectionFailure() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
							Serialization.serialize(new Login("Guesttt", "Guest"))).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		try {
			customer.login("Guesttt", "Guest", false);
		} catch (AuthenticationError ¢) {
			fail();
		} catch (CriticalError ¢) {
		/* Test Passed */
		}
	}
	
	@Test
	public void IllegalResultTest() {
		try {
			Mockito.when(
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
							Serialization.serialize(new Login("Guesttt", "Guest"))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			customer.login("Guesttt", "Guest", false);
		} catch (AuthenticationError ¢) {
			fail();
		} catch (CriticalError ¢) {
		/* Test Passed */
		}
	}
}
