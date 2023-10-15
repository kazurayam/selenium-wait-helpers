package com.kazurayam.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.kazurayam.selenium.WaitHelpers.waitForDomModificationsToCease;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WaitHelpersTest {
    private WebDriver driver;
    private String ACCOUNT;
    private String EMAIL;
    private String PASSWD;
    private WebDriverWait wait;

    @Test
    public void testGetObserverJs() {
        String code = WaitHelpers.getObserverJS();
        //System.out.println(code);
        assertNotNull(code);
    }

    @Test
    public void testWaitForDomModificationsToCease() throws Exception {
        String url = String.format("https://dev.azure.com/%s", ACCOUNT);
        driver.get(url);
        wait.until(webDriver ->
                "complete".equals(((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")));
        Thread.sleep(1000);
        //
        waitForDomModificationsToCease(driver);

        // type the EMAIL as my DevOps account id
        driver.findElement(By.xpath("//input[@name='loginfmt']")).sendKeys(EMAIL);
        waitForDomModificationsToCease(driver);   // wait for the DOM to be static

        // click the Next button
        driver.findElement(By.xpath("//input[@id='idSIButton9']")).click();
        waitForDomModificationsToCease(driver);

        // type the password of my DevOps account
        driver.findElement(By.xpath("//input[@id='idSIButton9']")).sendKeys(PASSWD);
        waitForDomModificationsToCease(driver);

        // click the Sing-in button
        driver.findElement(By.xpath("//input[@id='idSIButton9']")).click();
        waitForDomModificationsToCease(driver);

        // click the Yes button
        driver.findElement(By.xpath("//input[@id='idSIButton9']")).click();
        waitForDomModificationsToCease(driver);
    }

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(25,25));
        driver.manage().window().setSize(new Dimension(800,800));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ACCOUNT = "kazuakiurayama";
        EMAIL = "kazuaki.urayama@gmail.com";
        PASSWD = "Sq682myE";
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }
}
