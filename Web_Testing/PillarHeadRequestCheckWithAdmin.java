package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class PillarHeadRequestCheckWithAdmin {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\shaun\\eclipse-workspace\\Testing_Week10\\src\\Week10\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("https://sutd-scheduler.herokuapp.com/");

		String pillhead_username = "tonyQuek";
		String pillhead_pass = "tonyQuek";

		WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));

		Thread.sleep(300);
		username.sendKeys(pillhead_username);
		password.sendKeys(pillhead_pass);

		Thread.sleep(300);
		WebElement log_in = driver.findElement(By.xpath("/html/body/form/div/div/p[3]/input"));
		log_in.click();

		Thread.sleep(3000);

		driver.get("https://sutd-scheduler.herokuapp.com/request");
		
		Select day = new Select(driver.findElement(By.id("day")));
		Select time = new Select(driver.findElement(By.id("time")));
		Select room = new Select(driver.findElement(By.id("room")));
		
		WebElement request_go_buttom = driver.findElement(By.xpath("/html/body/form/p[4]/input"));
		
		day.selectByVisibleText("Friday");
		time.selectByVisibleText("4pm-5pm");
		room.selectByVisibleText("Lab 59");
		
		request_go_buttom.click();
		Thread.sleep(300);
		WebElement logout = driver.findElement(By.xpath("/html/body/header/div/nav/ul/li[3]/a"));
		logout.click();
		
		driver.get("https://sutd-scheduler.herokuapp.com");

		String admin_username = "felicePeck";
		String admin_pass = "password";

		

		WebElement admin_username_entry = driver.findElement(By.id("username"));
		WebElement admin_password_entry = driver.findElement(By.id("password"));
		
		Thread.sleep(300);
		admin_username_entry.sendKeys(admin_username);
		admin_password_entry.sendKeys(admin_pass);
		
		WebElement admin_log_in = driver.findElement(By.xpath("/html/body/form/div/div/p[3]/input"));

		Thread.sleep(300);
		admin_log_in.click();
		
		driver.get("https://sutd-scheduler.herokuapp.com/viewRequests");
		
		
		
		
		

		

	}
}
