package tests;

import java.util.concurrent.TimeUnit;


import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewAlbum {
  private WebDriver driver;
  private String baseUrl;
  private StringBuffer verificationErrors = new StringBuffer();
  private String albumTitle;

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://imgur.com";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    login();
  }
  


  @Test
  public void testNewAlbum() throws Exception {
	  // Get the album page
	  driver.get("http://cs1699testing.imgur.com/");
	  driver.findElement(By.cssSelector(".album-create-link")).click();
	  String currTime = "" + System.currentTimeMillis();
	  albumTitle = currTime;
	  driver.findElement(By.cssSelector("#album-title")).sendKeys(currTime);
	  driver.findElement(By.xpath("//div[@id='create-album-form']//input[@type='submit' and @class='button-medium']")).click();
	  driver.get("http://cs1699testing.imgur.com/");
	  WebElement newAlbum = driver.findElement(By.xpath("//div[@data-title='" + currTime + "']"));
	  assertTrue(newAlbum != null);
	  logout();
  }
  
  @After
  public void tearDown() throws Exception {
	login();
	//Clean up: Delete the album we just created
    driver.get("http://cs1699testing.imgur.com/");
    WebElement hoverElement = driver.findElement(By.xpath("//div[@data-title='" + albumTitle + "']"));
  	Actions builder = new Actions(driver);
  	builder.moveToElement(hoverElement).perform();
    driver.findElement(By.xpath("//div[@data-title='" + albumTitle + "']/div[4]")).click(); 
    new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(driver
    		.findElement(By.cssSelector("div.item.album-delete-link")))); 
    
    driver.findElement(By.cssSelector("div.item.album-delete-link")).click();
    driver.findElement(By.id("delete-album-yes")).click();
	
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
