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
    public static ArrayList<HashMap<String,String>> getTeamDetails(WebDriver driver,Integer NoofPagestoGet,Double WinPercent)
    {  
        ArrayList<HashMap<String,String>> Teams = new ArrayList<>();
        for(int i=1;i<=NoofPagestoGet;i++)
        {
            WebElement btnNextPage=driver.findElement(By.xpath("//a[@href='/pages/forms/?page_num="+i+"']"));
            btn_click(btnNextPage);
            //Get the rows with Win % less than 40% (0.40) 
            List<WebElement> wins=driver.findElements(By.xpath("//td[contains(@class,'pct') and number(text())<"+WinPercent+"]"));
            
            for(WebElement win:wins)
            {   //locate elements for rows for Team Name,Year and Win %tage
                WebElement EleTeamName=win.findElement(By.xpath("./ancestor::tr/td[@class='name']"));
                WebElement EleYear=win.findElement(By.xpath("./ancestor::tr/td[@class='year']"));
                //Get text for the located elements
                String TeamName=getText(EleTeamName).toString();
                String Year=getText(EleYear).toString();
                String WinPercentage=getText(win).toString();

                System.out.println("TeamName:"+TeamName+",Year:"+Year+",Win Percentage:"+WinPercentage);
                long epochtime=System.currentTimeMillis()/1000;
                String epoch=Long.toString(epochtime);
                HashMap<String, String> TeamHashMap=new HashMap();
                TeamHashMap.put("TeamName",TeamName);
                TeamHashMap.put("Year",Year);
                TeamHashMap.put("Win Percentage",WinPercentage);
                TeamHashMap.put("Epoch Time",epoch);
                Teams.add(TeamHashMap);
                // System.out.println(Teams);
                //Teams.add("TeamName:"+TeamName+",Year:"+Year+",Win Percentage:"+WinPercentage);
            }
        }
       return Teams;
    }
    public static ArrayList<HashMap<String,String>> getFilmDetails(WebDriver driver) throws InterruptedException
    {  
        ArrayList<HashMap<String,String>> Films = new ArrayList<>();
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
                WebElement EleTitle=driver.findElement(By.xpath("(//td[@class='film-title'])["+i+"]"));
                WebElement EleNominations=driver.findElement(By.xpath("(//td[@class='film-nominations'])["+i+"]"));
                WebElement EleAwards=driver.findElement(By.xpath("(//td[@class='film-awards'])["+i+"]"));
                List<WebElement> EleBestPicture=driver.findElements(By.xpath("(//td[@class='film-best-picture'])["+i+"]/i"));
                //Get text for the located elements
                String Title=getText(EleTitle).toString();
                String Nominations=getText(EleNominations).toString();
                String Awards=getText(EleAwards).toString();
                boolean isWinner=EleBestPicture.size()>0;
                // Duration timeout=Duration.ofMillis(1000);
                // WebDriverWait wait = new WebDriverWait(driver, timeout);
                // try 
                // {
                //     wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table//tr/td[4])["+i+"]/i")));
                //     isWinner=true;
                // } catch (Exception e) {
                //     // TODO: handle exception
                //     //.printStackTrace();
                //     isWinner=false;


                // }

                System.out.println("Title:"+Title+",Year:"+Year+",Nominations:"+Nominations+",Awards:"+Awards+",isWinner:"+isWinner);
                long epochtime=System.currentTimeMillis()/1000;
                String epoch=Long.toString(epochtime);
                String isWin=Boolean.toString(isWinner);
                HashMap<String, String> FilmHashMap=new HashMap();
                FilmHashMap.put("Epoch Time",epoch);
                FilmHashMap.put("Title",Title);
                FilmHashMap.put("Year",Year);
                FilmHashMap.put("Nominations",Nominations);
                FilmHashMap.put("isWinner", isWin);
                Films.add(FilmHashMap);
                // System.out.println(Films);
            }
        }
       return Films;
    }
    @Test
    public static void CreateJSON(ArrayList<HashMap<String,String>> Values,String FileName) throws JsonProcessingException {
 
        //Get current epoch time
        // long epochtime;
        ObjectMapper map = new ObjectMapper();
        // Map<String, String> inputMap = new HashMap<String, String>();
        //Running a for loop to put the values of ararylist in HashMap
        // int count=1;
        // System.out.println(Values.size());
        // for(String value:Values)
        // {   
        //     epochtime=System.currentTimeMillis()/1000;
        //     String key="EpochTime:"+epochtime+",Valuecount:"+count;
        //     inputMap.put(key, value);
        //     count++;
        // }

        // Converting map to a JSON payload as string
        try {
            String employeePrettyJson = map.writerWithDefaultPrettyPrinter().writeValueAsString(Values);
            System.out.println(employeePrettyJson);
        } catch (JsonException e) {
            e.printStackTrace();
        }
 
        String userDir = System.getProperty("user.dir");
        File f=new File(userDir + "\\src\\Output\\"+FileName);
        //Writing JSON on a file
        try {
            map.writerWithDefaultPrettyPrinter()
                    .writeValue(f, Values);
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




