package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    static ChromeDriver driver;
    
        /*
         * TODO: Write your tests here with testng @Test annotation. 
         * Follow `testCase01` `testCase02`... format or what is provided in instructions
         */
    
         
        /*
         * Do not change the provided methods unless necessary, they will help in automation and assessment
         */
        @BeforeTest
        public void startBrowser()
        {
            System.setProperty("java.util.logging.config.file", "logging.properties");
    
            // NOT NEEDED FOR SELENIUM MANAGER
            // WebDriverManager.chromedriver().timeout(30).setup();
    
            ChromeOptions options = new ChromeOptions();
            LoggingPreferences logs = new LoggingPreferences();
    
            logs.enable(LogType.BROWSER, Level.ALL);
            logs.enable(LogType.DRIVER, Level.ALL);
            options.setCapability("goog:loggingPrefs", logs);
            options.addArguments("--remote-allow-origins=*");
    
            System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 
    
            driver = new ChromeDriver(options);
    
            driver.manage().window().maximize();
        }
    
        @AfterTest
        public void endTest()
        {
            driver.close();
            driver.quit();
    
        }
        @BeforeMethod 
        //Open the website before each test case 
        public static void openURL() throws InterruptedException {
    
            System.out.println("Maximize the window");
            driver.manage().window().maximize();

        System.out.println("Opening Website -->https://www.scrapethissite.com/pages/");
        //Open the website
        driver.get("https://www.scrapethissite.com/pages/");
        Thread.sleep(3000);
    }
        @Test
        public void testCase01() throws InterruptedException, JsonProcessingException 
        {
        System.out.println("Start Test case: testCase01");
        Duration timeout=Duration.ofMillis(1000);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Hockey Teams: Forms, Searching and Pagination']")));
        
        //Locate the element Hockey Teams: Forms, Searching and Pagination
        // WebElement linkHockey=Wrappers.FindElement(driver,"//a[text()='Hockey Teams: Forms, Searching and Pagination']");
        WebElement linkHockey=Wrappers.FindElement(driver,"//a[contains(text(),'Hockey Teams')]");
        //Click on the link Hockey Teams: Forms, Searching and Pagination
        Wrappers.btn_click(linkHockey);
        Thread.sleep(3000);
        //Get the team details for win% less tha 0.4 for 4 pages
        ArrayList<HashMap<String,String>> Teams = Wrappers.getTeamDetails(driver, 4,0.4);
        //Create a JSON file
        Wrappers.CreateJSON(Teams,"hockey-team-data.json");        
        System.out.println("end Test case: testCase01");

    }
    @Test
    public void testCase02() throws InterruptedException, JsonProcessingException 
    {
    System.out.println("Start Test case: testCase02");
    Duration timeout=Duration.ofMillis(1000);
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='Oscar Winning Films: AJAX and Javascript']")));
    //Locate the element Oscar Winning Films: AJAX and Javascript
    // WebElement linkOscar=Wrappers.FindElement(driver,"//a[text()='Oscar Winning Films: AJAX and Javascript']");
    WebElement linkOscar=Wrappers.FindElement(driver,"//a[contains(text(),'Oscar Winning Films')]");
    //Click on Oscar Winning Films: AJAX and Javascript
    Wrappers.btn_click(linkOscar);
    Thread.sleep(3000);
    //Get first 5 films of all the years displayed
    ArrayList<HashMap<String,String>> Films = Wrappers.getFilmDetails(driver);
    //Create a JSON file
    Wrappers.CreateJSON(Films,"oscar-winner-data.json");
    System.out.println("end Test case: testCase02");
}
}
