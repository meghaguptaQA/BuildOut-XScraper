package demo.wrappers;

import demo.TestCases;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.json.JsonException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Wrappers {

    /*
     * Write your selenium wrappers here
     */

    public static String GetCurrentURL(WebDriver driver)
    {   
        try {       
             String url=driver.getCurrentUrl();
             return url;
        
        } catch (Exception e) {
            e.printStackTrace();
        }
                return null;

    }
    public static void enter_Value(WebElement element,String attributeValue)
    {
        try 
        {
            element.clear();
            element.sendKeys(attributeValue);
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
    public static String getText(WebElement element)
    {
        try 
        {
            String text=element.getText();
            return text;
        } catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
        
    }
    public static void btn_click(WebElement element)
    {
        try 
        {
            element.click();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
    public static WebElement FindElement(WebElement res,String xpath)
    {
        try 
        {
            WebElement element=res.findElement(By.xpath(xpath));
            return element;
        } catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }

    }
    public static WebElement FindElement(WebDriver driver,String xpath)
    {
        try 
        {
            WebElement element=driver.findElement(By.xpath(xpath));
            return element;
        } catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }

    }
    public static ArrayList<String> getTeamDetails(WebDriver driver,Integer NoofPagestoGet)
    {  
        ArrayList<String> Teams = new ArrayList<String>();
        for(int i=1;i<=NoofPagestoGet;i++)
        {
            WebElement btnNextPage=driver.findElement(By.xpath("//a[@href='/pages/forms/?page_num="+i+"']"));
            btn_click(btnNextPage);
            //Get the rows with Win % less than 40% (0.40) 
            List<WebElement> wins=driver.findElements(By.xpath("//td[contains(@class,'pct') and number(text())<0.4]"));
            for(WebElement win:wins)
            {   //locate elements for rows for Team Name,Year and Win %tage
                WebElement EleTeamName=win.findElement(By.xpath("./ancestor::tr/td[@class='name']"));
                WebElement EleYear=win.findElement(By.xpath("./ancestor::tr/td[@class='year']"));
                //Get text for the located elements
                String TeamName=EleTeamName.getText().toString();
                String Year=EleYear.getText().toString();
                String WinPercentage=win.getText().toString();
                System.out.println("TeamName:"+TeamName+",Year:"+Year+",Win Percentage:"+WinPercentage);
                Teams.add("TeamName:"+TeamName+",Year:"+Year+",Win Percentage:"+WinPercentage);
            }
        }
       return Teams;
    }
    public static ArrayList<String> getFilmDetails(WebDriver driver) throws InterruptedException
    {  
        ArrayList<String> Films = new ArrayList<String>();
        //Locate the element for year
        List<WebElement> years=driver.findElements(By.xpath("//a[@class='year-link']"));
        for(WebElement year:years)
        {   //click on the respective year
            btn_click(year);
            String Year=year.getText().toString();
            Thread.sleep(5000);            
            //run for loop for getting data for first 5 rows
            for(int i=1;i<=5;i++)
            {   //locate elements Title,Nomination,Awards and Winner for the row in the table
                WebElement EleTitle=driver.findElement(By.xpath("(//table//tr/td[1])["+i+"]"));
                WebElement EleNominations=driver.findElement(By.xpath("(//table//tr/td[2])["+i+"]"));
                WebElement EleAwards=driver.findElement(By.xpath("(//table//tr/td[3])["+i+"]"));
                //Get text for the located elements
                String Title=EleTitle.getText().toString();
                String Nominations=EleNominations.getText().toString();
                String Awards=EleAwards.getText().toString();
                boolean isWinner=false;
                Duration timeout=Duration.ofMillis(1000);
                WebDriverWait wait = new WebDriverWait(driver, timeout);
                try 
                {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table//tr/td[4])["+i+"]/i")));
                    isWinner=true;
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                System.out.println("Title:"+Title+",Year:"+Year+",Nominations:"+Nominations+",Awards:"+Awards+",isWinner:"+isWinner);
                Films.add("Title:"+Title+",Year:"+Year+",Nominations:"+Nominations+",Awards:"+Awards+",isWinner:"+isWinner);
            }
        }
       return Films;
    }
    @Test
    public static void CreateJSON(ArrayList<String> Values,String FileName) throws JsonProcessingException {
 
        //Get current epoch time
        long epochtime;
        ObjectMapper map = new ObjectMapper();
        Map<String, String> inputMap = new HashMap<String, String>();
        //Running a for loop to put the values of ararylist in HashMap
        int count=1;
        System.out.println(Values.size());
        for(String value:Values)
        {   
            epochtime=System.currentTimeMillis()/1000;
            String key="EpochTime:"+epochtime+",Valuecount:"+count;
            inputMap.put(key, value);
            count++;
        }

        // Converting map to a JSON payload as string
        try {
            String employeePrettyJson = map.writerWithDefaultPrettyPrinter().writeValueAsString(inputMap);
            System.out.println(employeePrettyJson);
        } catch (JsonException e) {
            e.printStackTrace();
        }
 
        String userDir = System.getProperty("user.dir");
        File f=new File(userDir + "\\src\\Output\\"+FileName);
        //Writing JSON on a file
        try {
            map.writerWithDefaultPrettyPrinter()
                    .writeValue(f, inputMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean flag=true;
        //Check if file length is 0 or not
        if(f.length()==0)
        {
            flag=false;
        }
        //Assert if file exists and is empty or not
        Assert.assertTrue(f.isFile());
        Assert.assertTrue(f.exists());
        Assert.assertTrue(flag);
 
    }
}




// public static void goToUrlAndWait(WebDriver driver, String url) {
//     driver.get(url); // youtube
//     new WebDriverWait(driver, Duration.ofSeconds(10)).until(
//         webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
//     );
// }

// double value = Double.parseDouble(stringValue.replaceAll("[^0-9]", ""));
//         if (stringValue.endsWith("K")) {
//             return (long) value * 1000;
//         } else if (stringValue.endsWith("M")) {
//             return (long) value * 1000000;
//         } else if (stringValue.endsWith("B")) {
//             return (long) value * 1000000000;
//         } else {
//             return Long.parseLong(stringValue.replaceAll("[^0-9]", ""));
//         }