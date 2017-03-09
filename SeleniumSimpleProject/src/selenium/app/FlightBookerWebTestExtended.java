package selenium.app;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlightBookerWebTestExtended {

	// Works with Firefox Version 50.0.0, and Selenium 3.0.1
	//            Chrome Version 54.0.2840.59 m, Chromedriver version 2.25, and Selenium 3.0.1
	String browser="CHROME";
	WebDriver driver;
	Wait<WebDriver> wait;
	String url="http://www.softwaretestingbook.org/flightbooker/FlightBooker.html";
	String mainTitle="Flight Booker";
	
	// Note add a pause after any action to slow down execution for monitoring
	private void pause(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Groups: BT=BehaviourTest, NT=NavTest, FT=FeatureTest, ST=ScenarioTest
	
	@BeforeClass(groups={"BT","NT","FT","ST"}) public void setupDriver() throws Exception {
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
	
	@AfterClass(groups={"BT","NT","FT","ST"})
	public void shutdown() {
		driver.quit();
	}
	
	private void waitForTitle(String name) {
		wait.until(ExpectedConditions.titleIs(name));		
	}
	
	private void waitForElement(String name) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(name)));
	}

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

	@Test(timeOut=30000,groups="BT") public void test1_1() {
		// check at correct window
		assertEquals( driver.getTitle(), "Flight Booker" );
		// enter 60 into passengers
		id("passengers").clear();
		id("passengers").sendKeys("60");
		// check 60 in text field
		assertEquals( value("passengers"), "60" );
		// clear the text box before enter 100 into passengers
		id("passengers").clear();
		id("passengers").sendKeys("100");
		// check 100 in text field
		assertEquals( value("passengers"), "100" );      
	}
	
	@Test(timeOut=30000,groups="BT") public void test1_2() {
		// check at correct window
		assertEquals( driver.getTitle(), "Flight Booker" );
		select("comfort", false);
		id("comfort").click();
		assertTrue( id("comfort").isSelected() );
		id("comfort").click();
		assertFalse( id("comfort").isSelected() );
	}

	// Check at start title, click on button; wait for end title, and wait for end button (to make sure page loaded)
	// The waits will generate exceptions if they timeout
	private void navTest( String startTitle,
			               String button,
			               String endTitle,
			               String endButton ) throws Exception {
		assertEquals(driver.getTitle(), startTitle );
		id(button).click();
		waitForTitle(endTitle);
		waitForElement(endButton);
	}
	
	@Test(timeOut=30000,groups="NT") public void testNT1() throws Exception {
		navTest( "Flight Booker", "AvailChecker", "Flight Booker", "Continue" );
		navTest( "Flight Booker", "Continue", "Flight Booker", "AvailChecker" );
		navTest( "Flight Booker", "Info", "Flight Information", "goback" );
		navTest( "Flight Information", "goback", "Flight Booker", "AvailChecker" );
		navTest( "Flight Booker", "exitlink", "Confirm exit", "exitno" );
		navTest( "Confirm exit", "exitno", "Flight Booker", "AvailChecker" );
    }	
	
	@Test(timeOut=30000,groups="NT") public void testNT2() throws Exception {
		navTest( "Flight Booker", "AvailChecker", "Flight Booker", "Continue" );
		navTest( "Flight Booker", "Info", "Flight Information", "goback" );
		navTest( "Flight Information", "goback", "Flight Booker", "AvailChecker" );
    }	

	void testResult(String p, Boolean c, String r) throws Exception {
		assertEquals( driver.getTitle(), "Flight Booker" );
		id("passengers").clear();
		id("passengers").sendKeys(p);
		select( "comfort", c);
		id("AvailChecker").click();
		assertEquals( driver.getTitle(), "Flight Booker" );
		assertEquals( value("result"), r );
		id("Continue").click();
		waitForTitle("Flight Booker");
	}
	
	@DataProvider(name = "FeatureTestData")
	public Object[][] createData() {
		return new Object[][] {
			// test, passengers, extra-comfort, expected output
			{ "FT1",  "40", false, "SUCCESS" },
			{ "FT2", "101",  true, "FAILURE" },
			{ "FT3", "200", false, "FAILURE" },
			{ "FT4", "-40", false,   "ERROR" },
			{ "FT5", "xyz", false,   "ERROR" }
		};
	}
	
	@Test(timeOut=30000,groups="FT",dataProvider = "FeatureTestData")
	public void testFeatures( String tid, String passengers, boolean extra, String expected ) throws Exception {
		testResult( passengers, extra, expected );
	}
	
	@Test(timeOut=30000,groups="ST") public void testST1() throws Exception {
		assertEquals( driver.getTitle(), "Flight Booker" );
		id("passengers").clear();
		id("passengers").sendKeys("60");
		if (id("comfort").isSelected())
			id("comfort").click();
		id("AvailChecker").click();
		waitForTitle("Flight Booker");
		assertEquals(value("result"), "SUCCESS" );
		id("Continue").click();
		waitForTitle("Flight Booker");
		waitForElement("AvailChecker");
	}
	@Test(timeOut=30000,groups="ST") public void testST2() throws Exception {
		assertEquals( driver.getTitle(), "Flight Booker" );
		id("passengers").clear();
		id("passengers").sendKeys("40");
		if (!id("comfort").isSelected())
			id("comfort").click();
		id("AvailChecker").click();
		waitForTitle("Flight Booker");
		assertEquals(value("result"), "SUCCESS" );
		id("Continue").click();
		waitForTitle("Flight Booker");
		waitForElement("AvailChecker");		
	}
	@Test(timeOut=30000,groups="ST") public void testST3() throws Exception {
		assertEquals( driver.getTitle(), "Flight Booker" );
		id("passengers").clear();
		id("passengers").sendKeys("100");
		if (!id("comfort").isSelected())
			id("comfort").click();
		id("AvailChecker").click();
		waitForTitle("Flight Booker");
		waitForElement("result");		
		assertEquals(value("result"), "FAILURE" );
		id("Info").click();
		waitForTitle("Flight Information");
		id("goback").click();
		waitForTitle("Flight Booker");
		waitForElement("AvailChecker");				
	}
	@Test(timeOut=30000,groups="ST") public void testST4() throws Exception {
		assertEquals( driver.getTitle(), "Flight Booker" );
		id("passengers").clear();
		id("passengers").sendKeys("r0");
		if (!id("comfort").isSelected())
			id("comfort").click();
		waitForElement("AvailChecker");				
		id("AvailChecker").click();
		waitForTitle("Flight Booker");
		waitForElement("result");		
		assertEquals(value("result"), "ERROR" );
		id("Continue").click();
		waitForTitle("Flight Booker");
		id("passengers").sendKeys("40");
		if (!id("comfort").isSelected())
			id("comfort").click();
		id("AvailChecker").click();
		waitForTitle("Flight Booker");
		assertEquals(value("result"), "SUCCESS" );
		id("Continue").click();
		waitForTitle("Flight Booker");
		waitForElement("AvailChecker");						
	}
	
}
