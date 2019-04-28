package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class StudentLoginFindAllLinks {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\shaun\\eclipse-workspace\\Testing_Week10\\src\\Week10\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("https://sutd-scheduler.herokuapp.com/");

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

		String currentURL = driver.getCurrentUrl();
		driver.get(currentURL);

		// get all the links
		java.util.List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println(links.size());

		System.out.println("***Prining all link names***");
		// print all the links
		for (int i = 0; i < links.size(); i = i + 1) {
			System.out.println(i + " " + links.get(i).getText());
		}
		System.out.println("***Prining all link addresses***");
		// print all the hyper links
		for (int i = 0; i < links.size(); i = i + 1) {
			System.out.println(i + " " + links.get(i).getAttribute("href"));
		}

	}
}
