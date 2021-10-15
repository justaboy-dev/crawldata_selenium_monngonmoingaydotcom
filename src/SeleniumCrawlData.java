
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import org.json.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class SeleniumCrawlData {
    
    
    public static String getname(String url)
    {
        String[] listStr = url.split("/");
        return listStr[listStr.length-1];
    }
    
    public static void saveImage(String url, String desFolder) throws Exception{
        String name = getname(url);
        URL imgurl = new URL(url);
        InputStream is = imgurl.openStream();
        OutputStream os = new FileOutputStream(desFolder + "/" + name);
        byte[] buff = new byte[2048];
        int lenght;
        while((lenght = is.read(buff))!=-1)
        {
            os.write(buff,0,lenght);
        }
        is.close();
        os.close();
    }
    
    public static void main(String[] args) throws Exception {
        ArrayList<FoodObject> arrList = new ArrayList<FoodObject>();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement nextbutton;
        int pages = 1;
        boolean hasNextPage = true;
        do{
            try {
                driver.get(String.format("https://monngonmoingay.com/tim-kiem-mon-ngon/page/%d/", pages));
                Thread.sleep(3000);
                ArrayList<WebElement> listElement = (ArrayList<WebElement>)driver.findElements(By.className("col-sm-3"));
                Thread.sleep(1000);
                for (WebElement webElement : listElement) {
                    String src = webElement.findElement(By.tagName("img")).getAttribute("src");
                    //saveImage(src, "images");
                    
                    String url = webElement.findElement(By.tagName("a")).getAttribute("href");
                    ((JavascriptExecutor)driver).executeScript("window.open()");
                    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(1));
                    driver.get(url);
                    
                    //something to do
                    
                    
                    
                    Thread.sleep(1000);
                    ((JavascriptExecutor)driver).executeScript("window.close()");
                    driver.switchTo().window(tabs.get(0));
                }
                pages++;
            } catch (Exception e) {
                e.printStackTrace();
                hasNextPage = false;
            }
        }while (hasNextPage);
       JSONArray Jsonarr = new JSONArray();
    }
    
}
