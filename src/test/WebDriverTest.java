package test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import crawlertest.DBHelper;

public class WebDriverTest {
	public static void main(String[] args) {
	System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
	WebDriver driver = new FirefoxDriver();

	driver.get("http://www.dianping.com/shop/66085429");

	
	
	try {
		WebElement more=driver.findElement(By.cssSelector("div#comment>h2>a:nth-of-type(2)"));
		more.click();
		System.out.println(more.getText());
		
	}
	catch(NoSuchElementException e){
		e.printStackTrace();
	}
	//System.out.println(driver.getTitle());
	//driver.quit();
}

}
