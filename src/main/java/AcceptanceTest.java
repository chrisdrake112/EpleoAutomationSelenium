import org.junit.Assert;
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
        public void ConvertionTest() {

            //Chome web driver
            WebDriver driver = new ChromeDriver();
            //Two currency lists
            String[] currenciesListOne = {"EUR","USD","GBP","JPY","CAD"};
            String[] currenciesListTwo = {"CAD","JPY","GBP","USD","EUR"};

            //set location of chrome web driver
            System.setProperty("webdriver.chrome.driver", "opt/homebrew/bin/chromedriver");
            driver.get("https://www.xe.com/currencyconverter/");
            //web driver wait duration
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            for(int i = 0;i<currenciesListOne.length;i++) {

                int k = i;
                k ++;

                //find select the first currency value box
                WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("amount")));
                //amount = driver.findElement(By.id("amount"));
                new Actions(driver).sendKeys(amount, "1"/*String.valueOf(k)*/).perform();

                //select the third to currency box
                WebElement toButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("midmarketToCurrency")));
                new Actions(driver).click(toButton).perform();
                new Actions(driver).sendKeys(toButton,currenciesListOne[i] + Keys.ENTER).perform();

                //select the second from currency box
                WebElement fromButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("midmarketFromCurrency")));
                new Actions(driver).click(fromButton).perform();
                new Actions(driver).sendKeys(fromButton, currenciesListTwo[i] + Keys.ENTER).perform();

                //WebElement convertBtn;
                //use xpath to select convert button
                WebElement convertBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/div[2]/section/div[2]/div/main/form/div[2]/button")));
                new Actions(driver).click(convertBtn).perform();

                //select result string
                String result = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/section/div[2]/div/main/form/div[2]/div[1]/p[2]"))).getText();

                //select conversion rate from page
                String conversionRate = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"__next\"]/div[2]/div[2]/section/div[2]/div/main/form/div[2]/div[3]/div[1]/div[1]/p"))).getText();

                //remove characters a-z using regex
                result = result.replaceAll("([aA-zZ])","");
                //remove characters before =
                conversionRate = conversionRate.split("=")[1];
                conversionRate = conversionRate.replaceAll("([aA-zZ])","");

                //round convert string to float and round up to the 4th nearest deciaml
                Double resultRounded = (double) Math.round(Float.parseFloat(result)) * 1000d / 100000d;
                //convert conversion rate to double
                double doubleConversionRate = Double.parseDouble(conversionRate);
                //divide conversion rate by 1
                double validationResult = 1 / doubleConversionRate;
                Double roundedValidationResult = (double) Math.round(validationResult) * 1000d / 100000d;
                System.out.println("Result " + resultRounded);
                System.out.println("converstion rate " + roundedValidationResult);

                //assert to check if both values are equal
                Assert.assertEquals(resultRounded,roundedValidationResult);
                driver.get("https://www.xe.com/currencyconverter/");

            }
            //close the dirver
            driver.quit();
    }
}