package NormalTest;




import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;

public class AdminAddRoom {

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

		driver.get("https://sutd-scheduler.herokuapp.com/addRooms");
		//Add in details for new user
		
		WebElement room_name = driver.findElement(By.id("room_name"));
		WebElement room_id = driver.findElement(By.id("room_id"));
		WebElement capacity = driver.findElement(By.id("capacity"));
		
		Select room_type = new Select(driver.findElement(By.id("room_type")));
		
		//Checkbox remove user 
		//driver.findElement(By.id("delete")).click();
		
		//press go button 
		WebElement press_go = driver.findElement(By.xpath("/html/body/form/p[5]/input"));

		
		room_name.sendKeys("Lab 578");
		room_id.sendKeys("1.578");
		capacity.sendKeys("105");
		
		room_type.selectByVisibleText("Lab");
		
		
		press_go.click();
		
		
		Thread.sleep(300);
		
		// search page for string 
		if(driver.getPageSource().contains("Lab 578")){
			System.out.println("Lab 578 is generated");
			
		}else{
			System.out.println("Lab 578 is absent");
			}
		//
		
		driver.get("https://sutd-scheduler.herokuapp.com/subjects");

		//Search dropdown
		Select room_dropDown = new Select(driver.findElement(By.id("component-0-classroom")));
		
		Boolean found = false;
	   java.util.List<WebElement> options = room_dropDown.getOptions(); 
	   for(WebElement item:options) 
	        { 
		   		if(item.getText().equals("1.578")){
		   			found = true;
		   			break;
		   		}          
	           }
	   if(found) {
		    System.out.println("Room exists in options");
		}
		
	}
}
