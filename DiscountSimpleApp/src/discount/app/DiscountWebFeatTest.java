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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DiscountWebFeatTest {

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
	
	private void select(String name, boolean state) {
		if (id(name).isSelected() != state)
			id(name).click();
	}
	private WebElement id(String name) {
		waitForElement(name);
		return driver.findElement(By.id(name));
	}
	private void waitForElement(String name) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(name)));
	}
	
	@Test(timeOut=20000,groups="FT",dataProvider = "FeatureTestData")
	public void testFeatures(String testId, String age, boolean member, String numberOfYears, String expectedOutput) throws Exception {
		// Goto the details screen
		driver.findElement(By.id("details")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Enter Applicant Details"));
		// wait until age is displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("age")));
		
		driver.findElement(By.id("age")).sendKeys(age);
		// select membership based on boolean member
		select( "member", member);

		// Select Less than 1 year for years of membership
		// Use selectByVisibleText() to select a pulldown menu (HTML <select>) option
		new Select(driver.findElement(By.id("years"))).selectByVisibleText(numberOfYears);
		// Click on the submit button
		driver.findElement(By.id("submitButton")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Discount Details"));
		// wait until result displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("discount")));
		// Check discount is correct
		assertEquals( driver.findElement(By.id("discount")).getText(), expectedOutput );
		// return to the main window at the end of the test
		driver.findElement(By.id("continue")).click();
		wait.until(ExpectedConditions.titleIs("Adventure Trip Discounts"));
	}
	
	@DataProvider(name = "FeatureTestData")
	public Object[][] createData() {
		return new Object[][] {
			// test, age, member, number of years, expected output
			{ "T3_1",  "20", false, "Not a member", "0%" },
			{ "T3_2", "40",  true, "Less than 1 year", "10%" },
			{ "T3_3", "20",  true, "Less than 1 year", "5%"},
			{ "T3_4", "20",  true, "Less than 5 years", "15%"},
			{ "T3_5", "20",  true, "More than 5 years", "25%"}
		};
	}
}
