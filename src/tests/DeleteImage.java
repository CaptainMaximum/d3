/*
* This class tests deleting an image that has been uploaded to an account.
* Setup: Login + Upload an image
*/

package tests;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DeleteImage {
    private WebDriver driver;
    private String baseUrl;
    private StringBuffer verificationErrors = new StringBuffer();
    private String imageToRemove;
    
    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://imgur.com";
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        login();
        StringSelection selection = new StringSelection("http://i.imgur.com/xY3TaEV.png");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        
        driver.findElement(By.className("upload-button")).click();
        WebElement textArea = driver.findElement(By.id("upload-global-link-input"));
        new Actions( driver ).contextClick(textArea).sendKeys( "p" ).perform();
        driver.findElement(By.id("upload-global-start-button")).submit();
        WebElement nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
        String code = driver.getCurrentUrl().substring(17);
        imageToRemove = code;
        driver.get("http://cs1699testing.imgur.com/all");
    }
    
    /*
    * Scenario: Delete a picture
    *   Given I am logged in
    *       and I have a picture uploaded on my account
    *   When I delete that picture
    *   Then it should not appear in my uploads
    */
    @Test
    public void testDeleteImage() throws Exception {
        driver.get("http://cs1699testing.imgur.com/all/");
        WebElement hoverElement = driver.findElement(By.id(imageToRemove));
        Actions builder = new Actions(driver);
        builder.moveToElement(hoverElement).perform();
        WebElement cross = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#" + imageToRemove + " > div:nth-child(3) > div:nth-child(2)")));
        cross.click();
        driver.findElement(By.id("delete-button")).click();
        driver.get("http://cs1699testing.imgur.com/all/");
        List<WebElement> list = driver.findElements(By.id(imageToRemove));
        assertTrue(list.size() == 0);
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
        // The logout button is only accessible after 'secondary-nav' has been hovered over
        WebElement hoverElement = driver.findElement(By.id("secondary-nav"));
        Actions builder = new Actions(driver);
        builder.moveToElement(hoverElement).perform();
        driver.findElement(By.xpath("//div[@class='dropdown-footer']//a[contains(text(),'logout')]")).click();
    }
}