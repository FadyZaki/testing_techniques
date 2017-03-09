package discount.app;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DiscountWebScenarioTest {

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
	
	private void select(String name, boolean state) {
		if (id(name).isSelected() != state)
			id(name).click();
	}
	
	@Test(timeOut=30000,groups="ST") public void test4_1() throws Exception {
		id("details").click();
		// check at correct window
		assertEquals( driver.getTitle(), "Enter Applicant Details" );
		// enter 20 into age
		id("age").clear();
		id("age").sendKeys("20"); 
		
		select( "member", true);

		// Select Less than 1 year for years of membership
		// Use selectByVisibleText() to select a pulldown menu (HTML <select>) option
		new Select(driver.findElement(By.id("years"))).selectByVisibleText("Not a member");
		// Click on the submit button
		driver.findElement(By.id("submitButton")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Discount Details"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
		// Check discount is correct
		assertEquals( driver.findElement(By.id("result")).getText(), "There is an error in the details you have provided." );
		
		id("continue").click();
		wait.until(ExpectedConditions.titleIs("Enter Applicant Details"));
		id("return").click();
		waitForTitle("Adventure Trip Discounts");
	}
}
