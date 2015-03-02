import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.openqa.selenium.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//Test Image
//http://i.imgur.com/xY3TaEV.png


public class webtests { 
	WebDriver driver = new FirefoxDriver();
	
	@Test
	//This test makes sure that the homepage shows a verity of images
	public void HomePagePictures() throws Exception {
		driver.get("http://imgur.com/");
		boolean dups = true;
		List<WebElement> posts = driver.findElements(By.className("post"));
		List<String> ids = new ArrayList<String>();
		for ( WebElement post: posts) {
			System.out.println(post.getAttribute("id"));
			if(ids.contains(post.getAttribute("id"))){
			dups = false;
			}
			else{
			ids.add(post.getAttribute("id"));
			}		
		}
		Assert.assertTrue(dups);
		driver.quit();
	  }
	
	
	@Test
	//This test makes sure the the sidebar works properly 
	public void ShowSidebar() throws Exception {
		driver.get("http://imgur.com/");
		WebElement test;
		test = driver.findElement(By.id("outside-tagging-showhide"));
		test.click();
		test = driver.findElement(By.id("outside-tagging"));
		String opacity = test.getAttribute("style");
		System.out.println(opacity);
		Assert.assertEquals(opacity,"opacity: 1;");
		driver.quit();
	  }
	
	
	@Test
	// Tests that a user can upload a picture
	//FOR THIS TEST TO WORK ON OTHER OS Keys.COMMAND CAN BE REPLACED WITH Keys.CONTROL
	public void UploadTest() throws Exception {
		StringSelection selection = new StringSelection("http://i.imgur.com/xY3TaEV.png");
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, selection);
		driver.get("http://imgur.com/");
		driver.findElement(By.className("upload-button")).click();
		driver.findElement(By.id("upload-global-link-input")).sendKeys(Keys.COMMAND + "v");
		driver.findElement(By.id("upload-global-start-button")).submit();
		WebElement nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
		driver.getCurrentUrl();
		String code = driver.getCurrentUrl().substring(17);
		System.out.println(code);
		driver.get("http://i.imgur.com/"+code+".png");
		String s = driver.findElement(By.className("shrinkToFit")).getAttribute("src");
		System.out.println(s);
		URL url = new URL(s);
	    System.out.println(url);
	    BufferedImage link = ImageIO.read(url);
	    BufferedImage base = ImageIO.read(new File("base.png"));
	    Assert.assertTrue(imagesAreEqual(link,base));
	    driver.quit();
	  }
	  
	
	@Test
	//Tests that a timestap advances after a picture had been uploaded
	//FOR THIS TEST TO WORK ON OTHER OS Keys.COMMAND CAN BE REPLACED WITH Keys.CONTROL
	public void TimeStampTest() throws Exception {
		StringSelection selection = new StringSelection("http://i.imgur.com/xY3TaEV.png");
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, selection);
		driver.get("http://imgur.com/");
		driver.findElement(By.className("upload-button")).click();
		driver.findElement(By.id("upload-global-link-input")).sendKeys(Keys.COMMAND + "v");
		driver.findElement(By.id("upload-global-start-button")).submit();
		WebElement nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
		String time1 = nicetime.getText();
		String pageurl = driver.getCurrentUrl();
		Thread.sleep(3000);
		driver.get(pageurl);
		nicetime = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("nicetime")));
		String time2 = nicetime.getText();
		System.out.println(time1);
		System.out.println(time2);
	    Assert.assertFalse(time1.equals(time2));
	    driver.quit();
	  }
	  
	  
	@Test
	//Makes sure that when you cick an image you are taken to that page
	public void BrowseTest() throws Exception {
		driver.get("http://imgur.com/");
		WebElement test;
		test = driver.findElement(By.className("post"));
		String PostName = test.getAttribute("id");
		System.out.println(PostName);
		test = test.findElement(By.className("image-list-link"));
		test.click();
		String currurl = driver.getCurrentUrl();
		System.out.println(currurl);
		Assert.assertEquals(currurl,"http://imgur.com/gallery/"+PostName);
		driver.quit();
	  }
	
	@Test
	//Tests that the random button works
	public void RandomTest() throws Exception {
		driver.get("http://imgur.com/");
		WebElement test;
		test = driver.findElement(By.id("random-button"));
		test.click();
		String currurl = driver.getCurrentUrl();
		Assert.assertTrue(currurl.contains("gallery"));
		driver.quit();
	  }
	@Test
	//Tests that the random button works twice in a row
	public void RandomTest2() throws Exception {
		driver.get("http://imgur.com/");
		WebElement test;
		test = driver.findElement(By.id("random-button"));
		test.click();
		String url1 = driver.getCurrentUrl();
		driver.get("http://imgur.com/");
		test = driver.findElement(By.id("random-button"));
		test.click();
		String url2 = driver.getCurrentUrl();
		Assert.assertFalse(url1.equals(url2));
		driver.quit();
	  }
	@Test
	//Checks that  the image at this link does not change over time
	public void ImageCheck() throws Exception {
		driver.get("http://i.imgur.com/FkVYvno.png");
		String s = driver.findElement(By.className("shrinkToFit")).getAttribute("src");
		System.out.println(s);
		URL url = new URL(s);
	    System.out.println(url);
	    BufferedImage link = ImageIO.read(url);
	    BufferedImage base = ImageIO.read(new File("base.png"));
	    Assert.assertTrue(imagesAreEqual(link,base));
	    driver.quit();
	  }

	@Test
	//Checks that clicking the imgur logo takes you back to the homepage
	public void HomePageCheck() throws Exception {
		driver.get("http://imgur.com/FkVYvno");
		WebElement test;
		test = driver.findElement(By.className("logo "));
		test.click();
	    Assert.assertTrue(driver.getCurrentUrl().equals("http://imgur.com/"));
	    driver.quit();
	  }
	
	@Test
	//Tests that scrolling to the bottem of the page loads more pictures
	public void ScrollCheck() throws Exception {
		driver.get("http://imgur.com");
		WebElement test;
		test = driver.findElement(By.id("content"));
		Dimension orignal = test.getSize();
		System.out.println(orignal);
		Thread.sleep(3000);
		JavascriptExecutor jsx = (JavascriptExecutor)driver;
		jsx.executeScript("window.scrollBy(0,2566)", "");
		Thread.sleep(3000);
		test = driver.findElement(By.id("content"));
		Dimension scroll = test.getSize();
		System.out.println(scroll);
		Assert.assertTrue((orignal.getHeight()!= scroll.getHeight())&&(orignal.getWidth()==scroll.getWidth()));
		driver.quit();
	  }
	
	
	
//Helper function to compare image buffers 
// This is based on a stackoverflow thread
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


