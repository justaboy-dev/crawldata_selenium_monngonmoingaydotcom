
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import org.json.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class SeleniumCrawlData extends Thread {

    static int startPages;
    static int endPages;

    public SeleniumCrawlData(int startPages, int endPages) {
        this.startPages = startPages;
        this.endPages = endPages;
    }

    public static String getname(String url) {
        String[] listStr = url.split("/");
        return listStr[listStr.length - 1];
    }

    public static void saveImage(String url, String desFolder) throws Exception {
        String name = getname(url);
        URL imgurl = new URL(url);
        InputStream is = imgurl.openStream();
        OutputStream os = new FileOutputStream(desFolder + "/" + name);
        byte[] buff = new byte[2048];
        int lenght;
        while ((lenght = is.read(buff)) != -1) {
            os.write(buff, 0, lenght);
        }
        is.close();
        os.close();
    }

    public static WebElement checkExitElement(WebElement ele, String tagname) {
        WebElement el;
        try {
            el = ele.findElement(By.tagName(tagname));
            return el;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getContent(ArrayList<WebElement> rows, int index) {
        String returnStr = "";
        ArrayList<WebElement> content;
        try {
            content = (ArrayList<WebElement>) rows.get(index)
                    .findElement(By.className("col"))
                    .findElement(By.tagName("div"))
                    .findElement(By.tagName("ul"))
                    .findElements(By.tagName("li"));
        } catch (Exception e) {
            content = (ArrayList<WebElement>) rows.get(index)
                    .findElement(By.className("col"))
                    .findElement(By.tagName("div"))
                    .findElements(By.tagName("p"));
        }

        for (WebElement contentitem : content) {
            if (checkExitElement(contentitem, "span") != null) {
                returnStr += contentitem.findElement(By.tagName("span")).getAttribute("innerText") + ";";
            } else {
                returnStr += contentitem.getAttribute("innerText") + ";";
            }
        }
        return returnStr;
    }

    public static JSONArray convertToJson(ArrayList<FoodObject> arr) throws Exception {
        JSONArray json = new JSONArray();

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = new JSONObject();
            JSONObject objitem = new JSONObject();
            objitem.put("id", String.valueOf(i));
            objitem.put("name", arr.get(i).name);
            objitem.put("description", arr.get(i).description);
            objitem.put("ingredients", arr.get(i).ingredients);
            objitem.put("preprocessing", arr.get(i).preprocessing);
            objitem.put("perform", arr.get(i).perform);
            objitem.put("eating", arr.get(i).eating);
            objitem.put("tip", arr.get(i).tip);
            objitem.put("levelOfDifficult", arr.get(i).levelOfDifficult);
            objitem.put("imageUrl", arr.get(i).imageUrl);
            obj.put("Food", objitem);
            json.put(obj);
        }

        return json;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread: " + Thread.currentThread().getId());
            ArrayList<FoodObject> arrList = new ArrayList<FoodObject>();
            ChromeOptions op = new ChromeOptions();
            op.addArguments("--headless");
            WebDriver driver = new ChromeDriver(op);
            boolean hasNextPage = true;
            //int test = 1; //this varible for testing.
            do {
                try {
                    System.out.println("Go to page: " + startPages + "......");
                    driver.get(String.format("https://monngonmoingay.com/tim-kiem-mon-ngon/page/%d/", startPages));
                    Thread.sleep(2000);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    Thread.sleep(4000);
                    ArrayList<WebElement> listElement = (ArrayList<WebElement>) driver.findElements(By.className("col-sm-3"));
                    Thread.sleep(2000);
                    for (WebElement webElement : listElement) {
                        FoodObject foodObject = new FoodObject();
                        String src = webElement.findElement(By.tagName("img")).getAttribute("src");
                        foodObject.name = webElement.findElement(By.className("info-list")).findElement(By.tagName("h3")).getText();
                        System.out.println("Getting info food: " + foodObject.name + ".....");
                        System.out.println("Saving image......");
                        saveImage(src, "images");
                        String url = webElement.findElement(By.className("info-list")).findElement(By.tagName("a")).getAttribute("href");
                        ((JavascriptExecutor) driver).executeScript("window.open()");
                        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                        driver.switchTo().window(tabs.get(1));
                        driver.get(url);
                        Thread.sleep(2000);
                        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                        Thread.sleep(4000);
                        //something to do

                        foodObject.imageUrl = ("images/" + getname(src));
                        foodObject.description = driver.findElement(By.cssSelector("[class='pt-2 pb-4']")).getAttribute("innerText");
                        ArrayList<WebElement> rows = (ArrayList<WebElement>) driver.findElements(By.cssSelector("[class='row mb-3']"));
                        ArrayList<WebElement> temp = (ArrayList<WebElement>) driver.findElements(By.cssSelector("[class='nav justify-content-center']"));
                        String diff = temp.get(0).getAttribute("innerText");
                        foodObject.levelOfDifficult = diff.substring(0, diff.indexOf("\n"));
                        foodObject.ingredients = getContent(rows, 0);
                        foodObject.preprocessing = getContent(rows, 1);
                        foodObject.perform = getContent(rows, 2);
                        foodObject.eating = getContent(rows, 3);
                        foodObject.tip = getContent(rows, 4);
                        arrList.add(foodObject);
                        System.out.println("Getting success: " + foodObject.name);
                        Thread.sleep(2000);

                        ((JavascriptExecutor) driver).executeScript("window.close()");

                        Thread.sleep(2000);
                        driver.switchTo().window(tabs.get(0));
                    }
                    startPages++;
                    //test++; testing
                } catch (Exception e) {
                    System.err.println("Error !!!!!!!!!!!!!!");
                    e.printStackTrace();
                    hasNextPage = false;
                }
            } while (hasNextPage && startPages <= endPages);
            //while (hasNextPage && test <1); //this while for testing
            JSONArray Jsonarr = convertToJson(arrList);
            try (FileWriter file = new FileWriter("output/" + startPages + "data.json")) {
                System.out.println("Saving JSON file.......");
                file.write(Jsonarr.toString());
                System.out.println("Saving file successfull !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
