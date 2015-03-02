package tests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class Favorites {
  private WebDriver driver;
  private String baseUrl;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://imgur.com";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  /*
   * Given I am logged in
   * When I "favorite" a picture
   * Then that picture should appear on my list of favorites
   */
  @Test
  public void testFavorite() throws Exception {
	  login();
	  driver.get("http://imgur.com/HegrNXq");
	  driver.findElement(By.cssSelector(".favorite-image")).click();
	  driver.get("https://imgur.com/user/cs1699testing/favorites");
	  List<WebElement> list = driver.findElements(By.xpath("//a[@href='/user/cs1699testing/favorites/HegrNXq']"));
	  assertTrue(list.size() > 0);
	  logout();
  } 
  @After
  public void tearDown() throws Exception {
		login();
		driver.get("http://imgur.com/HegrNXq");
		driver.findElement(By.cssSelector(".favorite-image")).click();
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
