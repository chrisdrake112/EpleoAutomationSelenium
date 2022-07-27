import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AcceptanceTest {
        @Test
        public void Test() throws Exception {

            WebDriver driver = new ChromeDriver();
            String[] currencies = {"EUR","USD","GBP","JPY","CAD"};
            System.setProperty("webdriver.chrome.driver", "opt/homebrew/bin/chromedriver");
            driver.get("https://www.xe.com/currencyconverter/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            for(int i = 0;i<currencies.length;i++) {

                for(int j = 0;j<currencies.length;j++) {

                    int k = j;
                    k ++;

                    WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("amount")));
                    //amount = driver.findElement(By.id("amount"));
                    new Actions(driver).sendKeys(amount, "1"/*String.valueOf(k)*/).perform();

                    WebElement toButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("midmarketToCurrency")));
                    new Actions(driver).click(toButton).perform();
                    new Actions(driver).sendKeys(toButton,currencies[i] + Keys.ENTER).perform();

                    WebElement fromButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("midmarketFromCurrency")));
                    new Actions(driver).click(fromButton).perform();
                    new Actions(driver).sendKeys(fromButton, currencies[j] + Keys.ENTER).perform();

                    //WebElement convertBtn;
                    WebElement convertBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/div[2]/section/div[2]/div/main/form/div[2]/button")));
                    new Actions(driver).click(convertBtn).perform();

                    String result = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/section/div[2]/div/main/form/div[2]/div[1]/p[2]"))).getText();

                    String conversionRate = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/section/div[2]/div/main/form/div[2]/div[3]/div[1]/div[1]/p"))).getText();

                    result = result.replaceAll("([aA-zZ])","");

                    conversionRate = conversionRate.split("=")[1];
                    conversionRate = conversionRate.replaceAll("([aA-zZ])","");

                    Double resultRounded = (double) Math.round(Float.parseFloat(result) * 100000d) / 10000d;
                    Double doubleConversionRate = Double.parseDouble(conversionRate);
                    Double ValidationResult = 1 * doubleConversionRate;
                    System.out.println(resultRounded);
                    System.out.println(ValidationResult);

                    driver.get("https://www.xe.com/currencyconverter/");

                }

            }
            driver.quit();
    }
}