package discount.app;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DiscountWebNavTest {

	private int gap=0; // Set to 1000 to create a 1-second gap when pause is called

	String browser="CHROME"; // Change to CHROME if required
	static WebDriver driver;
	static Wait<WebDriver> wait;
	static String url="http://webcourse.cs.nuim.ie/~sbrown/cs608/discounter.html";

	// Method to slow down execution in case a test is not working as expected
	// To use this, add a pause() after every selenium call
	private void pause() {
		try { Thread.sleep(gap); } catch (Exception e) {}
	}

	// Start up the browser before all the tests, and create a 'wait'
	@BeforeClass public void setupDriver() throws Exception {
		// webdriver.chrome.driver gives the full path of the executable
		// this code selects the executable name for windows, linux, and mac os x systems
		// Note: the Mac OS X support has not been tested!
		if (browser.equals("CHROME")) {
			if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
				System.setProperty("webdriver.chrome.driver", "selenium/chromedriver.exe");
			else if (System.getProperty("os.name").toLowerCase().startsWith("linux"))
				System.setProperty("webdriver.chrome.driver","selenium/chromedriver");
			else if (System.getProperty("os.name").toLowerCase().startsWith("mac"))
				System.setProperty("webdriver.chrome.driver","selenium/chromedriver");
			else
				assertTrue("os.name".equals("windows or linux or mac os x"));
			driver = new ChromeDriver();
		}
		else if (browser.equals("FIREFOX")) {
			if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
				System.setProperty("webdriver.gecko.driver", "selenium/geckodriver.exe");
			else if (System.getProperty("os.name").toLowerCase().startsWith("linux"))
				System.setProperty("webdriver.gecko.driver","selenium/geckodriver");
			else if (System.getProperty("os.name").toLowerCase().startsWith("mac"))
				System.setProperty("webdriver.gecko.driver","selenium/geckodriver");
			else
				assertTrue("os.name".equals("windows or linux or mac os x"));
			driver = new FirefoxDriver();
		}
		else {
			assertEquals( browser, "CHROME or FIREFOX" );
		}
		wait = new WebDriverWait( driver, 10 );
		driver.get( url );
	}

	// Shut down the browser after all the tests
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.quit();
	}

	// Make sure we are on the main screen before every test
	@BeforeMethod public void setUp() throws Exception {
		assertEquals( driver.getTitle(), "Adventure Trip Discounts" );
	}

	// Make sure every test leaves the application at the main window
	@AfterMethod
	public void tearDown() throws Exception {
		assertTrue( driver.getTitle().equals("Adventure Trip Discounts") ); 
	}
	
	private void waitForTitle(String name) {
		wait.until(ExpectedConditions.titleIs(name));		
	}
	
	private void waitForElement(String name) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(name)));
	}
	
	//retrieve webElement by its id
	private WebElement id(String name) {
		waitForElement(name);
		return driver.findElement(By.id(name));
	}
	
	private String value(String name) {
		return id(name).getAttribute("value");
	}
	
	// Check at start title, click on button; wait for end title, and wait for end button (to make sure page loaded)
	// The waits will generate exceptions if they timeout
	private void navTest( String startTitle,
			               String clickableElementId,
			               String endTitle,
			               String uniqueComponentId,
			               String uniqueComponentText ) throws Exception {
		assertEquals(driver.getTitle(), startTitle );
		id(clickableElementId).click();
		waitForTitle(endTitle);
		waitForElement(uniqueComponentId);
		assertEquals(id(uniqueComponentId).getText(), uniqueComponentText);
	}
	
	@Test(timeOut=30000,groups="NT") public void test2_1() throws Exception {
		
		navTest( "Adventure Trip Discounts", "details", "Enter Applicant Details", "return", "here" );
		navTest( "Enter Applicant Details", "return", "Adventure Trip Discounts", "details", "here" );
		navTest( "Adventure Trip Discounts", "details", "Enter Applicant Details", "return", "here" );
		
		
		id("age").clear();
		id("age").sendKeys("20");
		navTest( "Enter Applicant Details", "submitButton", "Discount Details", "result", "Your discount is calculated below." );
		navTest( "Discount Details", "continue", "Adventure Trip Discounts","details", "here" );
		navTest( "Adventure Trip Discounts", "details", "Enter Applicant Details", "return", "here" );
		
		
		id("age").clear();
		id("age").sendKeys("-20");
		navTest( "Enter Applicant Details", "submitButton", "Discount Details","result", "There is an error in the details you have provided." );
		navTest( "Discount Details", "continue", "Enter Applicant Details", "return", "here");
		
		
		id("age").clear();
		id("age").sendKeys("12");
		navTest( "Enter Applicant Details", "submitButton", "Discount Details","result", "Sorry, but you must be between 18 and 65 to attend Adventure Trips events." );
		navTest( "Discount Details", "continue", "Adventure Trip Discounts","details", "here" );	
	}
}
