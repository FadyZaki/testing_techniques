package discount.app;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

//
// Note that this demo is just to provide you with a framework for your test implementation,
//    and allow you to make sure selenium/testng is running correctly before you start.
//
// You may use what you need out of this program; but you don't have to use any of it.
//
// The tests here all expect to start in the "Adventure Trip Discounts" (main) window,
//    so every tests (if it passes) leaves the application in the window. This is checked
//    using @BeforeMethod and @AfterMethod. You do not need to use this approach in your
//    own tests, but you may if you wish.
//
// A pause() method is also included, so you can slow down your test execution by calling it
//    in suitable places, If you do use this, you need to set the variable gap to a suitable
//    value. Make sure to define gap=0 before you submit your work, but you may leave any
//    calls to pause in your code.
//
// Make sure each test can be clearly matched by its test data (I suggest by using the
//    test ID in the method name). Include comments for anything which is not obvious in
//    your code. You may use my comments in your answer, but they must make sense in the
//    context of your own code. Do NOT leave any unused methods in your own code.
//
// You may write utility methods, or just implement the tests inline as shown here.
//    However, If you have multiple tests with exactly the same structure, but different
//    data, I expect to see @DataProvider used.
//
// Note: tested under linux and windows using selenium 3.0.1 and:
//    firefox 50.0 with geckodriver 0.11.1
//    chrome  54.0 with chromedriver 2.25
//

public class DiscountDemoWebTest {

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

	//
	// Note:
	//    1. these tests all expect to start at the main window ("Adventure Trip Discounts")
	//    2. so every test must end with the application at the main window
	//
	// You may use this approach in your testing, but it is not required.
	//

	// Demonstration test: test for 'other members' discount at 10%
	
	@Test(timeOut=20000)
	public void demoTest1() throws Exception {
		// Goto the details screen
		driver.findElement(By.id("details")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Enter Applicant Details"));
		// wait until age is displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("age")));
		// Enter 30 into age
		driver.findElement(By.id("age")).sendKeys("30");
		// Make sure member is selected
		// Note: use isSelected() to decide whether to click or not
		if (!driver.findElement(By.id("member")).isSelected())
			driver.findElement(By.id("member")).click();
		// Select Less than 1 year for years of membership
		// Use selectByVisibleText() to select a pulldown menu (HTML <select>) option
		new Select(driver.findElement(By.id("years"))).selectByVisibleText("Less than 1 year");
		// Click on the submit button
		driver.findElement(By.id("submitButton")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Discount Details"));
		// wait until result displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("discount")));
		// Check discount is correct
		assertEquals( driver.findElement(By.id("discount")).getText(), "10%" );
		// return to the main window at the end of the test
		driver.findElement(By.id("continue")).click();
		wait.until(ExpectedConditions.titleIs("Adventure Trip Discounts"));
	}

	// Demonstration test: test for invalid input (age)
	@Test(timeOut=20000)
	public void demoTest2() throws Exception {
		// Goto the details screen
		driver.findElement(By.id("details")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Enter Applicant Details"));
		// wait until age is displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("age")));
		// Enter xxx into age
		driver.findElement(By.id("age")).sendKeys("xxx");
		// Click on the submit button
		driver.findElement(By.id("submitButton")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Discount Details"));
		// wait until result displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
		// Check error message is correct
		assertEquals( driver.findElement(By.id("result")).getText(), "There is an error in the details you have provided." );
		// return to the main window at the end of the test
		driver.findElement(By.id("continue")).click();
		wait.until(ExpectedConditions.titleIs("Enter Applicant Details"));
		driver.findElement(By.id("return")).click();
		wait.until(ExpectedConditions.titleIs("Adventure Trip Discounts"));
	}

	// Demonstration test: test for unsupported age
	// This also demonstrates the use of pause
	@Test(timeOut=20000)
	public void demoTest3() throws Exception {
		// Goto the details screen
		pause(); // Note I don't expect to see pauses in your final test code!
		driver.findElement(By.id("details")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Enter Applicant Details"));
		// wait until age is displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("age")));
		// Enter 75 into age
		driver.findElement(By.id("age")).sendKeys("75");
		pause();
		// Click on the submit button
		driver.findElement(By.id("submitButton")).click();
		// Wait for new window
		wait.until(ExpectedConditions.titleIs("Discount Details"));
		// wait until result displayed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result")));
		// Check error message is correct
		assertEquals( driver.findElement(By.id("result")).getText(), "Sorry, but you must be between 18 and 65 to attend Adventure Trips events." );
		pause();
		// return to the main window at the end of the test
		driver.findElement(By.id("continue")).click();
		wait.until(ExpectedConditions.titleIs("Adventure Trip Discounts"));
		pause();
	}

}
