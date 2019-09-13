package edu.udacity.java.nano;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class WebSocketChatApplicationTest {

    private WebDriver webDriver;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }

    @Test
    public void getLoginPage() {
        webDriver.get("http://localhost:8085");
        WebElement element = webDriver.findElement(By.id("username"));
        assertNotNull(element);
        element.sendKeys("test-login");
        WebElement button = webDriver.findElement(By.id("login-button"));
        button.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement username = webDriver.findElement(By.id("username"));
        assertEquals("test-login", username.getText());
        webDriver.close();
    }

    @Test
    public void testUserJoin() {
        webDriver.get("http://localhost:8085/index?username=test-name");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int beforeUserNum = Integer.parseInt(webDriver.findElement(By.id("chat-num")).getText());
        WebDriver newWebDriver = new ChromeDriver();
        newWebDriver.get("http://localhost:8085/index?username=test-name-2");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int afterUserNum = Integer.parseInt(webDriver.findElement(By.id("chat-num")).getText());
        assertEquals(beforeUserNum + 1, afterUserNum);
        webDriver.close();
        newWebDriver.close();
    }

    @Test
    public void testChat() {
        webDriver.get("http://localhost:8085/index?username=test-name");
        WebElement textContainer = webDriver.findElement(By.id("message-container"));
        int textContainerSize = textContainer.findElements(By.className("mdui-card")).size();
        WebElement sendMessage =webDriver.findElement(By.id("msg"));
        sendMessage.sendKeys("test message");
        webDriver.findElement(By.id("send-message-button")).click();
        List<WebElement> messages = textContainer.findElements(By.id("message-card"));
        WebElement lastMessage = messages.get(messages.size() - 1);
        assertEquals("test-name：test message", lastMessage.getText());
        webDriver.close();
    }

    @Test
    public void testLogOut() {
        webDriver.get("http://localhost:8085/index?username=test-name");
        WebElement logoutButton = webDriver.findElement(By.id("exit-button"));
        logoutButton.click();
        assertNotNull(webDriver.findElement(By.id("login-button")));
        webDriver.close();
    }

}