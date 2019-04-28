package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class AdminAddStudent {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\shaun\\eclipse-workspace\\Testing_Week10\\src\\Week10\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("https://sutd-scheduler.herokuapp.com/");

		String admin_username = "felicePeck";
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

		driver.get("https://sutd-scheduler.herokuapp.com/register");
		//Add in details for new user
		
		WebElement add_username = driver.findElement(By.id("username"));
		WebElement add_password = driver.findElement(By.id("password"));
		WebElement add_confirmPassword = driver.findElement(By.id("confirmPassword"));
		WebElement add_fullname = driver.findElement(By.id("fullname"));
		WebElement add_email = driver.findElement(By.id("email"));
		
		
		Select usergroup = new Select(driver.findElement(By.id("user_group")));
		Select pillar = new Select(driver.findElement(By.id("pillar")));
		
		//Student
		Select term = new Select(driver.findElement(By.id("term")));
		WebElement student_id = driver.findElement(By.id("student_id"));
		Select student_group = new Select(driver.findElement(By.id("student_group")));
		
		
		
		//press go button 
		WebElement press_go = driver.findElement(By.xpath("/html/body/form/p[15]/input"));
		
		add_username.sendKeys("eugene");
		add_password.sendKeys("password1");
		add_confirmPassword.sendKeys("password1");
		add_fullname.sendKeys("eugene chia");
		add_email.sendKeys("@email.com");
		usergroup.selectByVisibleText("student");
		pillar.selectByVisibleText("ISTD");
	
		term.selectByVisibleText("6");
		student_id.sendKeys("900892");
		student_group.selectByVisibleText("p4t5c1");
		
		press_go.click();
		
		
		
		

		
	}
}
