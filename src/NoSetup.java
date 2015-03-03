/*
* This class tests scenarios which need no special setup or teardown.
*/

package tests;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NoSetup {
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
    * Scenario: Search
    *   When I search for "funny,"
    *   Then I should find images that are tagged as funny.
    */
    @Test
    public void testSearch() throws Exception {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("div.search-icon-container-icon")).click();
        driver.findElement(By.name("q")).clear();
        driver.findElement(By.name("q")).sendKeys("funny");
        driver.findElement(By.xpath("//form[@class='search-form']")).submit();
        driver.findElement(By.cssSelector("a.image-list-link > img")).click();
        driver.findElement(By.cssSelector("#tags-underbar > span")).click();
        List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + "funny" + "')]"));
        try {
            assertTrue(list.size() > 0);
            } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }
    
    /*
    * Scenario: Login
    *   Given I am not logged in
    *   When I click on "log in"
    *       and enter my credentials
    *   Then I shall be logged in.
    */
    @Test
    public void testLogin() throws Exception {
        login();
        String accountName = driver.findElement(By.xpath("//a[@class='account-user-name']")).getText();
        assertTrue(accountName.equals("cs1699testing"));
    }
    
    /*
    * Scenario: Front page duplicates
    *   When I check the front page
    *   Then I should see no duplicate picture or albums
    */
    @Test
    public void testHomePagePictures() throws Exception {
        driver.get("http://imgur.com/");
        boolean dups = true;
        List<WebElement> posts = driver.findElements(By.className("post"));
        List<String> ids = new ArrayList<String>();
        for ( WebElement post: posts) {
            if(ids.contains(post.getAttribute("id"))){
                dups = false;
            }
            else{
                ids.add(post.getAttribute("id"));
            }
        }
        Assert.assertTrue(dups);
    }
    
    /*
    * Scenario: Sidebar
    *   Given that I am on the homepage and the sidebar is closed
    *   When I click show sidebar
    *   Then the sidebar should appear.
    */
    @Test
    public void testShowSidebar() throws Exception {
        driver.get("http://imgur.com/");
        WebElement test;
        test = driver.findElement(By.id("outside-tagging-showhide"));
        test.click();
        test = driver.findElement(By.id("outside-tagging"));
        String opacity = test.getAttribute("style");
        Assert.assertEquals(opacity,"opacity: 1;");
    }
    
    /*
    * Scenario: Upload a file
    *   Given I upload some content
    *   When I check to see the content
    *   Then I should be able to see the content
    */
    @Test
    public void testUpload() throws Exception {
        // Copy the image URL of what you want to upload
        StringSelection selection = new StringSelection("http://i.imgur.com/xY3TaEV.png");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        driver.get("http://imgur.com/");
        driver.findElement(By.className("upload-button")).click();
        WebElement textArea = driver.findElement(By.id("upload-global-link-input"));
        // Paste the copied URL into the upload form
        new Actions( driver ).contextClick(textArea).sendKeys( "p" ).perform();
        driver.findElement(By.id("upload-global-start-button")).submit();
        WebElement nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
        driver.getCurrentUrl();
        String code = driver.getCurrentUrl().substring(17);
        driver.get("http://i.imgur.com/" + code + ".png");
        String s = driver.findElement(By.className("shrinkToFit")).getAttribute("src");
        URL url = new URL(s);
        BufferedImage link = ImageIO.read(url);
        BufferedImage base = ImageIO.read(new File("base.png"));
        Assert.assertTrue(imagesAreEqual(link,base));
    }
    
    /*
    * Scenario: Test that time is accurate
    *   Given I upload content
    *   When time advances
    *   Then I want to see the time on the site updated
    */
    @Test
    public void testTimeStamp() throws Exception {
        StringSelection selection = new StringSelection("http://i.imgur.com/xY3TaEV.png");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        driver.get("http://imgur.com/");
        driver.findElement(By.className("upload-button")).click();
        WebElement textArea = driver.findElement(By.id("upload-global-link-input"));
        new Actions( driver ).contextClick(textArea).sendKeys( "p" ).perform();
        driver.findElement(By.id("upload-global-start-button")).submit();
        WebElement nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
        String time1 = nicetime.getText();
        String pageurl = driver.getCurrentUrl();
        Thread.sleep(3000);
        driver.get(pageurl);
        nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
        String time2 = nicetime.getText();
        Assert.assertFalse(time1.equals(time2));
    }
    
    /*
    * Scenario: Click a picture
    *   When I click a picture
    *   Then the picture should be taken to that page
    */
    @Test
    public void testBrowse() throws Exception {
        driver.get("http://imgur.com/");
        WebElement test;
        test = driver.findElement(By.className("post"));
        String PostName = test.getAttribute("id");
        test = test.findElement(By.className("image-list-link"));
        test.click();
        String currurl = driver.getCurrentUrl();
        Assert.assertEquals(currurl,"http://imgur.com/gallery/"+PostName);
    }
    
    /*
    * Scenario: Random
    *   Given that I am on the homepage
    *   When I click the random button
    *   Then I should be taken to a random image
    */
    @Test
    public void testRandom() throws Exception {
        driver.get("http://imgur.com/");
        WebElement test;
        test = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("random-button")));driver.findElement(By.id("random-button"));
        test.click();
        String currurl = driver.getCurrentUrl();
        Assert.assertTrue(currurl.contains("gallery"));
    }
    
    /*
    * Scenario: Random is random
    *   Given that I return to the homepage from a random image
    *   When I click the random button again
    *   Then I should be taken to a different random image
    */
    @Test
    public void testDifferentRandom() throws Exception {
        driver.get("http://imgur.com/");
        WebElement test;
        // Sometimes the random button takes a while to appear on the page, so we should wait for it
        test = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("random-button")));driver.findElement(By.id("random-button"));
        test.click();
        String url1 = driver.getCurrentUrl();
        driver.get("http://imgur.com/");
        test = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("random-button")));driver.findElement(By.id("random-button"));
        test.click();
        String url2 = driver.getCurrentUrl();
        Assert.assertFalse(url1.equals(url2));
    }

    /*
    * Scenario: Test a link references the correct file
    *   Given I share a link to my content
    *   When the person I shared it with clicks the link
    *   Then they should see my content
    */
    @Test
    public void testImageCheck() throws Exception {
        driver.get("http://i.imgur.com/FkVYvno.png");
        String s = driver.findElement(By.className("shrinkToFit")).getAttribute("src");
        URL url = new URL(s);
        BufferedImage link = ImageIO.read(url);
        BufferedImage base = ImageIO.read(new File("base.png"));
        Assert.assertTrue(imagesAreEqual(link,base));
    }

    /*
    * Scenario: Homepage
    *   Given I am not on the homepage
    *   When I click the imgur logo
    *   Then I should return to the home page
    */
    @Test
    public void testHomePageCheck() throws Exception {
        driver.get("http://imgur.com/FkVYvno");
        WebElement test;
        test = driver.findElement(By.className("logo "));
        test.click();
        Assert.assertTrue(driver.getCurrentUrl().equals("http://imgur.com/"));
    }

    /*
    * Scenario: Scroll
    *   When I scroll to the bottom of the page
    *   Then the page should load more pictures
    */
    @Test
    public void testScrollCheck() throws Exception {
        driver.get("http://imgur.com");
        WebElement test;
        test = driver.findElement(By.id("content"));
        Dimension orignal = test.getSize();
        Thread.sleep(3000);
        JavascriptExecutor jsx = (JavascriptExecutor)driver;
        jsx.executeScript("window.scrollBy(0,2566)", "");
        Thread.sleep(3000);
        test = driver.findElement(By.id("content"));
        Dimension scroll = test.getSize();
        Assert.assertTrue((orignal.getHeight()!= scroll.getHeight())&&(orignal.getWidth()==scroll.getWidth()));
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
    
    //Helper function to compare image buffers
    public boolean imagesAreEqual(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            return false;
        }
        for (int x = 1; x < image2.getWidth(); x++) {
            for (int y = 1; y < image2.getHeight(); y++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}