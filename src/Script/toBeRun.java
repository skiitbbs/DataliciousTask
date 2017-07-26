package Script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class toBeRun {
	
	
		//Declared a function which is going to read the (.js) file from local system.
		public static String readFile(String fileName) throws IOException {
		    BufferedReader br = new BufferedReader(new FileReader(fileName));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append("\n");
		            line = br.readLine();
		        }
		        return sb.toString();	
		    } finally {
		        br.close();
		    }
		}	

		public static void main(String[] args) throws InterruptedException {
			
			/*
			This Script is executing by two driver. 1. Chrome Driver(driver1) & 2. PhantomJS(driver).
			Chrome driver contain actions of opening Google. Typing Datalicious in google field. Then Clicking on the Datalicios link.
			
			Once clicked on Datalicious link, Same link is getting copied into PhantomJS driver.
			This driver is going to execute all javascript code. Which contain Script (jscode) for filtering GA and dc.optimahub.com actions.
			and next JS (jscode2) will capture the parameter of GA events dt and dp.
			
			*/
			
			
			System.setProperty("webdriver.chrome.driver","//home//sumit//Downloads//chromedriver");
			WebDriver driver1 = new ChromeDriver();
			//Setting up the properties for Chrome driver. Same will be used in this program.
			
			
			
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "//home//sumit//Downloads//Selenium Required file//phantomjs-2.1.1-linux-x86_64//bin//phantomjs");
			WebDriver driver = new PhantomJSDriver(caps);
			driver.manage().window().setPosition(new Point(0, 0));
			driver.manage().window().setSize(new Dimension(800, 600));
			//Setting up the properties for PhantomJS driver. Same will be used in this program.
			
			driver1.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); //Same is going to wait till 30 seconds for elements to Load.
			
			
			
			//These line of code is going to open google. then type Datalicios and click on the first link. 
			driver1.get("https://www.google.co.in/");
			driver1.manage().window().maximize();
			driver1.findElement(By.id("lst-ib")).sendKeys("Datalicious");
			driver1.findElement(By.xpath("//*[@id='sbtc']/div[2]/div[2]/div[1]/div/ul/li[5]/div/span[1]/span/input")).click();
			driver1.findElement(By.className("r")).click();
			System.out.println("Dtatalicious has been successfully opened.!");
			
			//These lines of code will be take .js file path, and later on execute the same.
			//jscode = is for filtering the Google Analytics and dc.optimahub.com request made from Datalicious.
			String jscode="";
			try {
				jscode=readFile("/home/sumit/workspace/DataliciousTask/js/javascriptcode.js");
			} catch (IOException e) {
				e.printStackTrace();
			}

			((PhantomJSDriver) driver).executePhantomJS(jscode);
			
			String url = driver1.getCurrentUrl();
			driver.get(url);
			System.out.println("Cureent URL has been passed to PhantomJS and JavaScript also executed");
			driver.manage().window().maximize();
			
			
			//jscode2 = is for getting the parameters like dt and dp (I did not see this param in GA) captured by the Google Analytics.	
			String jscode2="";
			try {
				jscode2=readFile("/home/sumit/workspace/DataliciousTask/js/javascriptcode2.js");
			} catch (IOException e) {
				e.printStackTrace();
			}

			((PhantomJSDriver) driver).executePhantomJS(jscode2);
			
			driver.get(url);
			System.out.println("JavaScript for GA parameter is successfully executed");
			driver1.close();			
		}


	}

