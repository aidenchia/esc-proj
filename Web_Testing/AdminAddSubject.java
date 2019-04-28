package NormalTest;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class AdminAddSubject {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\shaun\\eclipse-workspace\\Testing_Week10\\src\\Week10\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();

		driver.get("https://sutd-scheduler.herokuapp.com/");
		
		
		//log in
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

		driver.get("https://sutd-scheduler.herokuapp.com/subjects");
		//Add in details for new user
		
		WebElement subject_name = driver.findElement(By.id("subject_name"));
		WebElement subject_id = driver.findElement(By.id("subject_id"));
		WebElement duration = driver.findElement(By.id("component-0-duration"));
		WebElement cohort_num = driver.findElement(By.id("cohort_num"));
		WebElement total_enrol = driver.findElement(By.id("total_enrollment"));
		
		
		Select term_no = new Select(driver.findElement(By.id("term_no")));
		Select sessionType = new Select(driver.findElement(By.id("component-0-session")));
		Select room = new Select(driver.findElement(By.id("component-0-classroom")));
		Select pillar = new Select(driver.findElement(By.id("pillar")));
		Select subject_type = new Select(driver.findElement(By.id("subject_type")));
		
		//press go button 
		WebElement press_go = driver.findElement(By.xpath("/html/body/form/p[11]/input"));
		
		subject_name.sendKeys("Pretty Lights");
		subject_id.sendKeys("17983");
		
		term_no.selectByVisibleText("1");
	
		duration.sendKeys("2");
		
		sessionType.selectByVisibleText("Lab");
		room.selectByVisibleText("1.569");
		pillar.selectByVisibleText("HASS");
		subject_type.selectByVisibleText("Elective");
		
		cohort_num.sendKeys("3");
		total_enrol.sendKeys("150");
		
		
		
	
		press_go.click();
		
		
		
		

		
	}
}
