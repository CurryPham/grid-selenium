package selenium;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class TestGrid {
    WebDriver driver;
    String emailAddress;
    String projectPath = System.getProperty("user.dir");


    @Parameters({"browser", "ipAddress", "port"})
    @BeforeClass
    public void beforeClass(String browserName, String ipAddress, String portNumber) {
        DesiredCapabilities capability = null;

        switch (browserName) {
            case "firefox" :

                capability = DesiredCapabilities.firefox();
                capability.setBrowserName("firefox");


                FirefoxOptions fOptions = new FirefoxOptions();
                fOptions.merge(capability);
                break;
            case "chrome" :
                System.setProperty("webdriver.chrome.driver", projectPath + "\\browserDrivers\\chromedriver.exe");
                capability = DesiredCapabilities.chrome();
                capability.setBrowserName("chrome");

                ChromeOptions cOptions = new ChromeOptions();
                cOptions.merge(capability);
                break;
            case "edge" :
                System.setProperty("webdriver.edge.driver", projectPath + "\\browserDrivers\\msedgedriver.exe");
                capability = DesiredCapabilities.edge();
                capability.setBrowserName("edge");

                EdgeOptions eOptions = new EdgeOptions();
                eOptions.merge(capability);
                break;
            default :
                throw new RuntimeException("Browser is not valid!");
        }

        try {
            driver = new RemoteWebDriver(new URL(String.format("http://%s:%s/wd/hub", ipAddress, portNumber)), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        emailAddress = "curry" + generateFakeNumber() + "@gmail.com.vn";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);
        driver.manage().window().maximize();
        driver.get("https://demo.nopcommerce.com/");
    }



    @Test
    public void TC_01_Register_With_Empty_Data() {

        driver.findElement(By.xpath("//a[@class='ico-register']")).click();
        driver.findElement(By.xpath("//button[@id='register-button']")).click();

        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='FirstName-error']")).getText(), "First name is required.");
        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='LastName-error']")).getText(), "Last name is required.");
        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='Email-error']")).getText(), "Email is required.");
        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='Password-error']")).getText(), "Password is required.");
        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='ConfirmPassword-error']")).getText(), "Password is required.");
    }

    @Test
    public void TC_02_Register_With_Invalid_Email() {

        driver.findElement(By.xpath("//a[@class='ico-register']")).click();

        driver.findElement(By.xpath("//input[@id='FirstName']")).sendKeys("Curry");
        driver.findElement(By.xpath("//input[@id='LastName']")).sendKeys("Pham");
        driver.findElement(By.xpath("//input[@id='Email']")).sendKeys("curry.test#23443alpaca.com");
        driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("123456");
        driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("123456");

        driver.findElement(By.xpath("//button[@id='register-button']")).click();

        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='Email-error']")).getText(), "Wrong email");

    }

    @Test
    public void TC_03_Register_With_Correct_Email() {
        driver.findElement(By.xpath("//a[@class='ico-register']")).click();

        driver.findElement(By.xpath("//input[@id='FirstName']")).sendKeys("Curry");
        driver.findElement(By.xpath("//input[@id='LastName']")).sendKeys("Pham");
        driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(emailAddress);
        driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("123456");
        driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("123456");

        driver.findElement(By.xpath("//button[@id='register-button']")).click();

        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='result']")).getText(), "Your registration completed");

    }

    @Test
    public void TC_04_Register_With_Email_Exist() {

        driver.findElement(By.xpath("//a[@class='ico-register']")).click();

        driver.findElement(By.xpath("//input[@id='FirstName']")).sendKeys("Curry");
        driver.findElement(By.xpath("//input[@id='LastName']")).sendKeys("Pham");
        driver.findElement(By.xpath("//input[@id='Email']")).sendKeys(emailAddress);
        driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("123456");
        driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("123456");

        driver.findElement(By.xpath("//button[@id='register-button']")).click();

        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='message-error validation-summary-errors']//li")).getText(), "The specified email already exists");
    }

    @Test
    public void TC_05_Register_With_Password_Less_Than_6_Character() {

        driver.findElement(By.xpath("//a[@class='ico-register']")).click();

        driver.findElement(By.xpath("//input[@id='FirstName']")).sendKeys("Curry");
        driver.findElement(By.xpath("//input[@id='LastName']")).sendKeys("Pham");
        driver.findElement(By.xpath("//input[@id='Email']")).sendKeys("curry.test434535@alpaca.com");
        driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("123");
        driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("123456");

        driver.findElement(By.xpath("//button[@id='register-button']")).click();

        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='Password-error']")).getText(), "Password must meet the following rules:\nmust have at least 6 characters");
    }

    @Test
    public void TC_06_Register_With_Invalid_Confirm_Password() {

        driver.findElement(By.xpath("//a[@class='ico-register']")).click();

        driver.findElement(By.xpath("//input[@id='FirstName']")).sendKeys("Curry");
        driver.findElement(By.xpath("//input[@id='LastName']")).sendKeys("Pham");
        driver.findElement(By.xpath("//input[@id='Email']")).sendKeys("curry.test434535@alpaca.com");
        driver.findElement(By.xpath("//input[@id='Password']")).sendKeys("123456");
        driver.findElement(By.xpath("//input[@id='ConfirmPassword']")).sendKeys("654342");

        driver.findElement(By.xpath("//button[@id='register-button']")).click();

        Assert.assertEquals(driver.findElement(By.xpath("//span[@id='ConfirmPassword-error']")).getText(), "The password and confirmation password do not match.");
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    public int generateFakeNumber() {
        Random rand = new Random();
        return rand.nextInt(9999);

    }

}