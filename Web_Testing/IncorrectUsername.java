package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;

public class IncorrectUsername {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\shaun\\eclipse-workspace\\Testing_Week10\\src\\Week10\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("https://sutd-scheduler.herokuapp.com/");

		String admin_username = "Potato";
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

	}
}
