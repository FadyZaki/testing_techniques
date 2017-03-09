package selenium.app;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TridentifyWebTest {

	String browser="CHROME"; // Change to firefox if required

	static WebDriver driver;
	static Wait<WebDriver> wait;
	static int delay=0; // optional delay so you can watch progress
	static String url="http://webcourse.cs.nuim.ie/~sbrown/cs608/tridentifyp.html";

	@BeforeClass public void setupDriver() throws Exception {
		// webdriver.chrome.driver gives the full path of the executable
		// this code selects the executable name for windows & linux systems
		if (browser.equals("CHROME")) {
			if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
				System.setProperty("webdriver.chrome.driver", "selenium/chromedriver.exe");
			else
				System.setProperty("webdriver.chrome.driver","selenium/chromedriver");
			driver = new ChromeDriver();
		}
		else if (browser.equals("FIREFOX")) {
			if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
				System.setProperty("webdriver.gecko.driver", "selenium/geckodriver.exe");
			else
				System.setProperty("webdriver.chrome.driver","selenium/geckodriver");
			driver = new FirefoxDriver();
		}
		else {
			assertEquals( browser, "CHROME or FIREFOX" );
		}
		wait = new WebDriverWait( driver, 1 );
		driver.get( url );
		// System.out.println(driver.getPageSource()); // Use to find required names/ids/text
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.close();
		driver.quit();
	}

//	@BeforeMethod
//	public void setUp() throws Exception {
//		// make sure we are on the main page
//		assertEquals( driver.getTitle(), "Tridentify" );
//	}
//
//	// Always leave at main screen after test complete
//	// Every test must finish on the results screen for this to work
//	@AfterMethod
//	public void tearDown() throws Exception {
//		driver.findElement(By.name("Continue-button")).click();
//		Thread.sleep(delay);
//		// click - note this waits for the new page to load & all references are now stale!
//		// wait until displayed
//		waitForTitle("Tridentify");
//		waitForElement("identify-button");
//	}

//	public void test_identify(String l1, String l2, String l3, String expected) throws Exception
//	{
//		driver.findElement(By.name("side1")).sendKeys(l1); Thread.sleep(delay);
//		driver.findElement(By.name("side2")).sendKeys(l2); Thread.sleep(delay);
//		driver.findElement(By.name("side3")).sendKeys(l3); Thread.sleep(delay);
//		// click - note this waits for the new page to load & all references are now stale!
//		driver.findElement(By.name("identify-button")).click(); Thread.sleep(delay);
//		waitForTitle("Tridentify Result");
//		waitForElement("Continue-button");
//		assertEquals( value("Type"), expected );        
//	}
//
//	@Test
//	public void test3() throws Exception {
//		// On same page, so can safely create a reference for the field "side1" to save multiple finds
//		WebElement field1 = driver.findElement(By.name("side1"));
//		field1.sendKeys("1234");
//		assertEquals( field1.getAttribute("value"), "1234" );
//		driver.findElement(By.name("identify-button")).click(); Thread.sleep(delay);
//		wait.until(ExpectedConditions.titleIs("Tridentify Result"));
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Continue-button")));
//	}
	
	private void waitForTitle(String name) {
		wait.until(ExpectedConditions.titleIs(name));		
	}
	
	private void waitForElement(String name) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
	}

	private WebElement name(String name) {
		waitForElement(name);
		return driver.findElement(By.name(name));
	}
	
	private String value(String name) {
		return name(name).getAttribute("value");
	}
	
	@Test(timeOut=30000,groups="BT") public void test1_1() {
		// check at correct window
		assertEquals( driver.getTitle(), "Tridentify" );
		// enter 60 into passengers
		name("side1").clear();
		name("side1").sendKeys("100");
		// check 60 in text field
		assertEquals( value("side1"), "100" );
		// clear the text box before enter 100 into passengers
		name("side1").clear();
		name("side1").sendKeys("200");
		// check 100 in text field
		assertEquals( value("side1"), "200" );     
	}
	
	
	@Test(timeOut=30000,groups="BT") public void test1_2() {
		// check at correct window
		assertEquals( driver.getTitle(), "Tridentify" );
		// enter 60 into passengers
		name("side2").clear();
		name("side2").sendKeys("100");
		// check 60 in text field
		assertEquals( value("side2"), "100" );
		// clear the text box before enter 100 into passengers
		name("side2").clear();
		name("side2").sendKeys("200");
		// check 100 in text field
		assertEquals( value("side2"), "200" );     
	}
	
	@Test(timeOut=30000,groups="BT") public void test1_3() {
		// check at correct window
		assertEquals( driver.getTitle(), "Tridentify" );
		// enter 60 into passengers
		name("side3").clear();
		name("side3").sendKeys("100");
		// check 60 in text field
		assertEquals( value("side3"), "100" );
		// clear the text box before enter 100 into passengers
		name("side3").clear();
		name("side3").sendKeys("200");
		// check 100 in text field
		assertEquals( value("side3"), "200" );     
	}
	
	
	

	// Or use a DataProvider

}
