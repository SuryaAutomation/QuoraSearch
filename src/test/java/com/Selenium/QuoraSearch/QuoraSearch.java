package com.Selenium.QuoraSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class QuoraSearch {
	WebDriver driver = null;

	
	@BeforeMethod
	@Parameters("browser")
	public void OpenBrowser(String browser) throws InterruptedException {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\ELCOT\\eclipse-workspace\\QuoraSearch\\Drivers\\chromedriver.exe");//ChromeDriver 93.0.4577.63
			Thread.sleep(5000);
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver",
					"C:\\Users\\ELCOT\\eclipse-workspace\\QuoraSearch\\Drivers\\msedgedriver.exe");//MSEdgeDriver 96.0.1054.62
			driver = new EdgeDriver();
		} else {
			System.out.println("Only chrome and edge browsers are available for now");
		}
		
		// maximize the window
		driver.manage().window().maximize();
		// wait
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void quitBrowser() {
		driver.quit(); //quit the browser
	}

	@Test
	public void QuoraVerification() throws InterruptedException, IOException {

		// opening quora site
		String URL = "https://www.quora.com/profile/Quora";
		driver.get(URL);

		// search for topic 'Testing'
		driver.findElement(By
				.xpath("/html/body/div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div/form/div/div/div/span/span"))
				.sendKeys("Testing");
		Thread.sleep(3000);
		driver.findElement(By
				.xpath("/html/body/div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div/form/div/div/div/span/span"))
				.sendKeys(Keys.chord(Keys.ENTER));

		// search the first option that appear in search suggestion
		driver.findElement(
				By.xpath("//*[@id=\"mainContent\"]/div/div/div[2]/a[1]/div/div/div[2]/div/div")).click();

		// after clicking the first search result,the tab changes
		// moving back to the previous tab
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));

		// Finding number of search results for 'Testing' keyword
		List<WebElement> searches = driver.findElements(By.className("q-text"));
		Thread.sleep(3000);
		System.out.println("Number of search results for 'Testing' keyword are:" + searches.size());


		// verify the Title of page which contains 'Testing'
		String pageTitle = driver.getTitle();
		Assert.assertEquals(pageTitle, "Search");
		System.out.println("Title of the Page:" + pageTitle);
		
		// verify if the text "Result for testing" is displayed in the result page
		String s = driver.findElement(By.xpath("//div[@class='q-box'][2]/div/div/div[2]/div/div/div")).getText();
		System.out.println(s);
		Assert.assertEquals(s, "Results for Testing");
		

		// Click on “sign In” link
		driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[2]/div/div[2]/div/button/div/div/div"))
				.click();
		
		// click on “Sign up with email” link
		WebElement signup = driver
				.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div[2]/div/span/span[4]/div"));
		signup.click();
		
		// Verify if the “Sign Up” button is disabled
		System.out.println(signup.isEnabled());
		
		// Enter a name in sign up prompt
		driver.findElement(By.id("profile-name")).sendKeys("Surya");
		
		WebDriverWait wait=new WebDriverWait(driver,50);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("email"))));
		
        // Enter an invalid mail In Email field (ex: abc@abc)
		driver.findElement(By.id("email")).sendKeys("suryaj2021@.com");
       // Thread.sleep(1000);
		
		// Verify and capture the respective error message shown
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		String path = "C:\\eclipse-workspace\\QuoraSearch\\Screenshot\\error.png";
		File DestFile = new File(path);
		FileUtils.copyFile(SrcFile, DestFile);

	}

}
