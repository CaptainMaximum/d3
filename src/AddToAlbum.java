package tests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddToAlbum {
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
   *     and I have a picture uploaded on my account
   * When I change the title of a picture
   * Then I should see the new title when I view the image.
   */
  @Test
  public void testAddToAlbum() throws Exception {
	  // Get the album page
	  driver.get("http://cs1699testing.imgur.com/");
	  WebElement hoverElement = driver.findElement(By.xpath("//div[@data-title='Test Album']"));
	  Actions builder = new Actions(driver);
	  builder.moveToElement(hoverElement).perform();
	  driver.findElement(By.xpath("//div[@data-title='Test Album']/div[4]")).click(); 
	  new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(driver
	   		.findElement(By.xpath("//div[@data-title='Test Album']//div[@class='item album-images-link']"))));
	  driver.findElement(By.xpath("//div[@data-title='Test Album']//div[@class='item album-images-link']")).click();
	  driver.findElement(By.xpath("//div[@id='images-menu']/div[2]")).click();
	  driver.findElement(By.id("MNhewWN")).click();
	  driver.findElement(By.name("submit")).click();
	  driver.get("http://imgur.com/a/Yexh0");
	  //driver.findElement(By.xpath("//div[@data-title='Test Album']/div[@class='cover']/a")).click();
	  List<WebElement> list = driver.findElements(By.id("MNhewWN"));
	  assertTrue(list.size() > 0);
	  logout();
  }
  
  @After
  public void tearDown() throws Exception {
	login();
	driver.get("http://cs1699testing.imgur.com/");
    WebElement hoverElement = driver.findElement(By.xpath("//div[@data-title='Test Album']"));

    Actions builder = new Actions(driver);
    builder.moveToElement(hoverElement).perform();
    driver.findElement(By.xpath("//div[@data-title='Test Album']/div[4]")).click();
    new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(driver
   		.findElement(By.xpath("//div[@data-title='Test Album']//div[@class='item album-images-link']"))));
	System.out.println(7);
    driver.findElement(By.xpath("//div[@data-title='Test Album']//div[@class='item album-images-link']")).click();
    driver.findElement(By.xpath("//div[@id='images-menu']/div[1]")).click();
	driver.findElement(By.id("MNhewWN")).click();
	driver.findElement(By.name("submit")).click();
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
