package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class AdminEditUser {

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

		driver.get("https://sutd-scheduler.herokuapp.com/editUsers");
		//Add in details for new user
		
		WebElement add_username = driver.findElement(By.id("username"));
		WebElement add_Fullname = driver.findElement(By.id("fullname"));
		WebElement add_password = driver.findElement(By.id("password"));
		WebElement add_email = driver.findElement(By.id("email"));
		
		Select usergroup = new Select(driver.findElement(By.id("user_group")));
		
		WebElement add_pillar = driver.findElement(By.id("pillar"));
		WebElement add_term = driver.findElement(By.id("term"));
		WebElement add_student_id = driver.findElement(By.id("student_id"));
		WebElement add_professor_id = driver.findElement(By.id("professor_id"));
		WebElement add_coursetable = driver.findElement(By.id("coursetable"));
		
		//Checkbox remove user 
		//driver.findElement(By.id("delete")).click();
		
		//press go button 
		WebElement press_go = driver.findElement(By.xpath("/html/body/form/p[12]/input"));
		
		add_username.sendKeys("eugene");
		add_Fullname.sendKeys("eugene chia");
		add_password.sendKeys("password1");
		add_email.sendKeys("@email.com");
		
		usergroup.selectByVisibleText("student");
		
		add_pillar.sendKeys("ISTD");
		add_term.sendKeys("6");
		add_student_id.sendKeys("9000");
		
		press_go.click();
		
		
		
		

		
	}
}
