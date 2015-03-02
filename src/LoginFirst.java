package tests;

import java.util.concurrent.TimeUnit;


import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class LoginFirst {
  private WebDriver driver;
  private String baseUrl;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://imgur.com";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    login();
  }
  
  /*
   * Given I am logged in
   * When I click on "log out"
   * Then I shall be logged out
   */
  @Test
  public void testLogout() throws Exception {
	  logout();
	  try {
		  WebElement loginLink = driver.findElement(By.xpath("//li[@class='signin-link']"));
		  assertTrue(loginLink != null);
	  }
	  catch(NoSuchElementException nsee) {
		  fail("No element found: " + nsee.toString());
	  }
  }

  /*
   * Given I am logged in 
   *     and I have a picture uploaded on my account
   * When I change the title of a picture
   * Then I should see the new title when I view the image.
   */  
  @Test 
  public void testTitleChange() throws Exception {
	  driver.get("http://cs1699testing.imgur.com/all");
	  driver.findElement(By.cssSelector("#MNhewWN > a:nth-child(1)")).click();
	  driver.findElement(By.cssSelector(".js-edit-title")).click();
	  driver.findElement(By.cssSelector("#title-input")).clear();
	  String newTitle = "" + System.currentTimeMillis();
	  driver.findElement(By.cssSelector("#title-input")).sendKeys(newTitle);
	  driver.findElement(By.cssSelector("#title-description-button")).click();
	  driver.get("http://cs1699testing.imgur.com/all");
	  driver.findElement(By.cssSelector("#MNhewWN > a:nth-child(1)")).click();
	  String title = driver.findElement(By.cssSelector("#info-image-title")).getText();
	  assertTrue(title.equals(newTitle));
  }
  
  /*
   * Given that I am logged in
   * When I comment on a picture
   * Then my comment should appear on the picture's page.
   */
  @Test 
  public void testComment() throws Exception {
	  driver.get("http://imgur.com/gallery/eK94abP");
	  String xpath_val = "//div[@class='author']//a[contains(text(),'cs1699testing')]"; 
	  int numCommentsBefore = driver.findElements(By.xpath(xpath_val)).size();
	  long currTime = System.currentTimeMillis();
	  driver.findElement(By.cssSelector("#caption_textarea")).sendKeys("" + currTime);
	  driver.findElement(By.cssSelector(".submit-caption-button")).click();
	  driver.get("http://imgur.com/gallery/eK94abP");
	  int numCommentsAfter = driver.findElements(By.xpath(xpath_val)).size();
	  assertTrue(numCommentsBefore < numCommentsAfter);
  }
  
  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
 
  
  private void login() {
	  driver.get(baseUrl + "/signin");
	  driver.findElement(By.id("username")).sendKeys("cs1699testing");
	  driver.findElement(By.id("password")).sendKeys("laboontesting");
	  driver.findElement(By.name("submit")).click();
  }
  
  public void logout() {
	  WebElement hoverElement = driver.findElement(By.id("secondary-nav"));
	  Actions builder = new Actions(driver);
	  builder.moveToElement(hoverElement).perform();
	  driver.findElement(By.xpath("//div[@class='dropdown-footer']//a[contains(text(),'logout')]")).click();
  }  
}
