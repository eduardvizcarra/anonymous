import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class AmazonTest {
    private WebDriver driver;
    private long animationDelay = 1500;

    @BeforeTest
    public void setup() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.navigate().to("https://www.amazon.com/");
        driver.manage().window().maximize();
        Thread.sleep(animationDelay);
    }

    @BeforeMethod
    public void login() throws Exception{

        driver.findElement(By.partialLinkText("Sign in"))
                .click();

        Thread.sleep(animationDelay);
        driver.findElement(By.name("email"))
                .sendKeys("slavepriest123456@gmail.com");
        driver.findElement(By.name("password"))
                .sendKeys("asdf1234**");

        driver.findElement(By.id("signInSubmit"))
                .submit();

        Thread.sleep(animationDelay);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        if (driver != null) {
            Thread.sleep(animationDelay);
            driver.quit();
        }
    }

    @Test
    public void addItemsOnCart() throws Exception{

        driver.findElement(By.cssSelector("img[alt='Deals & Promotions']"))
                .click();

        driver.findElement(By.id("100_dealView_0"))
                .isDisplayed();

            driver.findElement(By.cssSelector("div[id='100_dealView_0']"))
                    .findElement(By.cssSelector("button[type='button']"))
                    .click();

        Thread.sleep(animationDelay);
        driver.findElement(By.className("a-alert-content"))
                .isDisplayed();
    }

    @Test(dependsOnMethods = "addItemsOnCart")
    public void checkingItemsOnCart() {

        driver.findElement(By.id("nav-cart"))
                .click();

        Assert.assertEquals(driver.getTitle(), "Amazon.com Shopping Cart");
    }

    @Test(dependsOnMethods = "checkingItemsOnCart")
    public void proceedToCheckout() {
        checkingItemsOnCart();

        driver.findElement(By.name("proceedToCheckout"))
                .click();

        Assert.assertEquals(driver.getTitle(), "Select a shipping address");
    }

    @Test
    public void removedAnItemOnTheCart() throws Exception{
        addItemsOnCart();

        driver.findElement(By.id("nav-cart"))
                .click();
        driver.findElement(By.cssSelector("input[value='Delete']"))
                .click();

        driver.findElement(By.cssSelector("div[data-name='Active Items']"))
                .isDisplayed();
    }

    @Test
    public void logout() {
        Actions action = new Actions(driver);
        WebElement dropdown = driver.findElement(By.partialLinkText("Account & List"));

        action.moveToElement(dropdown).build().perform();

        driver.findElement(By.id("nav-item-signout"))
                .findElement(By.className("nav-text"))
                .click();

        Assert.assertEquals(driver.getTitle(), "Amazon Sign-In");
    }
}
