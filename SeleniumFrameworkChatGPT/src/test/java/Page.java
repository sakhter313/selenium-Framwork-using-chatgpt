import Utils.JsonTestDataFetcher;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Page extends Base{

    Base base;
    private WebDriver driver;

    public Page(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }



    @Test(dataProvider = "nameData")
    public void verifyFirstAndLastNameJsonData(String firstName, String lastName,String phone, String email, String password) throws InterruptedException {

        // Locate the First Name input field
        WebElement firstNameInput = driver.findElement(By.xpath("//input[@id='firstName']"));

        // Clear any existing value in the First Name input field
        firstNameInput.clear();

        // Enter the First Name value
        firstNameInput.sendKeys(firstName);

        // Locate the Last Name input field
        WebElement lastNameInput = driver.findElement(By.xpath("//input[@id='lastName']"));

        // Clear any existing value in the Last Name input field
        lastNameInput.clear();

        // Enter the Last Name value
        lastNameInput.sendKeys(lastName);

        // Verify the entered First Name and Last Name values
        Assert.assertEquals(firstName, firstNameInput.getAttribute("value"));
        Assert.assertEquals(lastName, lastNameInput.getAttribute("value"));

        // Locate the phoneNumber input field
        WebElement phoneNumber= driver.findElement(By.xpath("//input[@type='tel' and @class='form-control' and @id='phone' and @placeholder='Enter phone number']\n"));
        // Clear any existing value in the Last Name input field
        phoneNumber.clear();
        // Enter the phoneNumber value
        phoneNumber.sendKeys(phone);

        //Assert.assertEquals(phoneNumber, phoneNumber.getAttribute("value"), "Phone number is not entered correctly.");

        // Find the dropdown menu for countries
        WebElement countryDropdown = driver.findElement(By.xpath("//select[@id='countries_dropdown_menu']"));

        // Select "India" from the dropdown menu
        WebElement indiaOption = countryDropdown.findElement(By.xpath("//option[text()='India']"));

        indiaOption.click();

        // Verify that "India" is selected
        String selectedCountry = indiaOption.getText();
        if (selectedCountry.equals("India")) {
            System.out.println("India is selected from the dropdown menu.");
        } else {
            System.out.println("Failed to select India from the dropdown menu.");
        }

        // Find the email input field using XPath
        WebElement emailInput = driver.findElement(By.xpath("//input[@id='emailAddress']"));

        emailInput.clear();

        // Enter the email
        emailInput.sendKeys(email);

        // Find the password input field using XPath
        WebElement passwordInput = driver.findElement(By.xpath("//input[@id='password']"));

        passwordInput.clear();

        // Enter the email
        passwordInput.sendKeys(password);

        // Scroll down the page
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500)");



        // Find the checkbox input using XPath
        WebElement checkbox = driver.findElement(By.xpath("//input[@id='exampleCheck1']"));

        // Check if the checkbox is enabled
        boolean isEnabled = checkbox.isEnabled();

        // Print the status of the checkbox
        System.out.println("Is checkbox enabled? " + isEnabled);

        // Click on the checkbox if it is enabled
        if (isEnabled) {
            checkbox.click();
            System.out.println("Checkbox clicked successfully.");
        } else {
            System.out.println("Checkbox is not enabled, cannot click.");
        }


    }

    @DataProvider(name = "nameData")
    public Object[][] provideData() {
        return JsonTestDataFetcher.fetchTestData();
    }

    @AfterMethod
    public void tearDown() {
        // Quit the WebDriver instance
        driver.quit();
    }
}
