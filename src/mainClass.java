
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 * 
 * 
 */
public class mainClass {
    public static void main(String[] args) throws Exception{
//        WebDriver driver = new ChromeDriver();
//        driver.get("https://monngonmoingay.com/tim-kiem-mon-ngon/");
//        WebElement e = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div/div[3]/a[8]"));
//        e.click();
//        e = driver.findElement(By.cssSelector("[class='page-numbers current']"));
//        int maxPage = Integer.getInteger(e.getAttribute("innerText"));
        int maxPage = 99;
        int inputThread = 0;
        
        do
        {
            try {
                System.out.print("Input number of Thread: ");
                Scanner sc = new Scanner(System.in);
                inputThread = sc.nextInt();
            } catch (Exception ex) {
            }
        }
        while(inputThread <=0 || inputThread >= Math.sqrt(maxPage));
        int thread = maxPage / inputThread;
        int end = 1;
        for(int i = 0; i < inputThread; i++)
        {
            int temp = end;
            end += thread;
            //
            SeleniumCrawlData runThread = new SeleniumCrawlData(temp, end);
            runThread.start();
            //
            end++;
        }
    }
}
