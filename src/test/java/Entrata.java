import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Entrata {
    public WebDriver driver;
    public WebDriverWait wait;
    JavascriptExecutor jsExecutor;
    @Before
    public void setUp() throws InterruptedException {
        ChromeOptions chromeoptions =new ChromeOptions();
        chromeoptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driver=new ChromeDriver(chromeoptions);
        driver.manage().window().maximize();
        String url="https://www.entrata.com/";
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,Duration.ofSeconds(30));
        testBaseCamp(driver, wait);
    }

    public void waitForElement(WebDriverWait wait){
    }
    public void switchTodWindow(String windows){
        Set<String> window =driver.getWindowHandles();
        Iterator<String> iterator=window.iterator();
        String parentID=iterator.next();
        String childId= iterator.next();
        switch (windows){
            case "parent":
                driver.switchTo().window(parentID);
                break;
            case "child":
                driver.switchTo().window(childId);
                break;
        }
    }
    public void waitAndWebdriverClick(WebElement element){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Actions action= new Actions(driver);
            action.moveToElement(element).build().perform();
            try{
                element.click();
            }catch (ElementClickInterceptedException e){
                wait.until(ExpectedConditions.elementToBeClickable(element));
                waitAndJSClick(element);
            }

            System.out.println("Successfully clicked on "+element);
        }catch (Exception e){
            System.out.println("Unable to clicked on element <"+element.toString()+">");
            System.out.println("Throws Expection :" +e);
            throw e;
        }
    }
    public void waitAndJSClick(WebElement element){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Actions action= new Actions(driver);
            action.moveToElement(element).build().perform();
            jsExecutor.executeScript("arguments[0].click();",element);
            System.out.println("Successfully clicked on "+element);
        }catch (Exception e){
            System.out.println("Unable to clicked on element <"+element.toString()+">");
            System.out.println("Throws Expection :" +e);
            throw e;
        }
    }
    public void sendValueToTextfield(WebElement element,String value){
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(value);
            System.out.println("Successfully send " + value + " to Textfield <" + element.toString() + ">");
        }
        catch(Exception e){
            System.out.println("Unable to send " + value + " to Textfield <" + element.toString() + ">");
            throw e;
        }
    }
    public void waitUntilElementTobeClickable(WebElement element){
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    public void testBaseCamp(WebDriver driver,WebDriverWait wait) throws InterruptedException {
        Actions actions = new Actions(driver);
        wait =new WebDriverWait(driver,Duration.ofSeconds(30));
//        Test Product
        WebElement productHead=driver.findElement(By.xpath("(//*[@class='main-nav-link'])[1]"));
        actions.moveToElement(productHead).perform();
        Thread.sleep(2000);
        WebElement resisdentPay= driver.findElement(By.xpath("//div[@class='nav-group']/a[contains(text(),'ResidentPay')]"));
        wait.until(ExpectedConditions.elementToBeClickable(resisdentPay));
        resisdentPay.click();
        WebElement resisdentHeadline= driver.findElement(By.xpath("//div[@class='product-text']"));
        Assert.assertTrue(resisdentHeadline.isDisplayed());

//       Test Base Camp Scenarios
        driver.findElement(By.xpath("//*[@class='header-nav-item']/*[contains(text(),'Base Camp')]")).click();
        System.out.println("Test if switching to correct window");

//        Switch control to child window
        switchTodWindow("child");
        driver.findElement(By.xpath("(//a/*[contains(text(),'Register Now')])[1]")).click();
        Thread.sleep(3000);
//      Fill the form
        WebElement firstName=driver.findElement(By.xpath("//input[@aria-label='First Name']"));
        wait.until(ExpectedConditions.elementToBeClickable(firstName));
//        actions.moveToElement(firstName).perform();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstName);
        firstName.sendKeys("snehal");
        WebElement lastName=driver.findElement(By.xpath("//input[@aria-label='Last Name']"));
        lastName.sendKeys("snehal");
        WebElement companyName=driver.findElement(By.xpath("//input[@aria-label='Company']"));
        String company ="coditas";
        companyName.sendKeys(company.toUpperCase());
        WebElement titleField=driver.findElement(By.xpath("//input[@aria-label='Title']"));
        String title ="sdet";
        titleField.sendKeys(title.toUpperCase());
        WebElement emailField=driver.findElement(By.xpath("//input[@aria-label='Email Address']"));
        emailField.sendKeys("snehal.pimpalkar");
        WebElement mobField= driver.findElement(By.xpath("//input[@aria-label='Mobile']"));
        mobField.sendKeys("7798455465");

// Asserting Error message for incorrect email id
        WebElement errormsg= driver.findElement(By.xpath("//*[@role='alert']"));
        Assert.assertTrue(errormsg.isDisplayed());
        driver.findElement(By.xpath("//*[@id='exit']")).click();
//        actions.moveToElement(element).perform();
        WebElement agendaHead= driver.findElement(By.xpath("//div/h2[contains(text(),'Agenda')]"));
        Assert.assertTrue(agendaHead.isDisplayed());
    }
    @After
    public void quit(){
        driver.quit();
    }
}
