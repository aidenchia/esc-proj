package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class StudentLoginTryAdminPage {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\shaun\\eclipse-workspace\\Testing_Week10\\src\\Week10\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("https://sutd-scheduler.herokuapp.com/");

		// Login
		String admin_username = "tianDuo";
		String admin_pass = "password";

		WebElement username = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));

		Thread.sleep(300);
		username.sendKeys(admin_username);
		password.sendKeys(admin_pass);

		Thread.sleep(300);
		WebElement log_in = driver.findElement(By.xpath("/html/body/form/div/div/p[3]/input"));
		log_in.click();

		Thread.sleep(3000);

		// home page
		String currentURL = driver.getCurrentUrl();
		driver.get(currentURL);

		driver.get("https://sutd-scheduler.herokuapp.com/editRooms");
		
		Thread.sleep(30);
		
		System.out.println("Page on "+ driver.getCurrentUrl());
		
		if (driver.getCurrentUrl() != "https://sutd-scheduler.herokuapp.com/editRooms") {
			System.out.println("Access not granted");
			
		}

	}
}
