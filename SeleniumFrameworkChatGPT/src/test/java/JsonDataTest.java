import Utils.JsonTestDataFetcher;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class JsonDataTest {

    public  static ExtentSparkReporter sparkReporter;
    public static ExtentReports extentReports;
    public static ExtentTest extentTest;
    public static WebDriver driver;
    private final String baseUrl = "https://qa-practice.netlify.app/bugs-form.html";

    @BeforeMethod
    public void setUp() {
        // Use WebDriverManager to setup ChromeDriver
        WebDriverManager.firefoxdriver().setup();

        // Initialize ChromeDriver instance
        driver = new FirefoxDriver();
        // Maximize the browser window
        driver.manage().window().maximize();

        String methodName = new Exception().getStackTrace()[0].getMethodName();

        String className = new Exception().getStackTrace()[0].getClassName();

        extentTest = extentReports.createTest(methodName, "Enter Person Name");

        extentTest.log(Status.INFO, "Filling the details of the user and getting error");

        extentTest.assignCategory("Regression");


        // Set implicit wait,
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get(baseUrl);

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();

        // Verify if the URL is opened successfully using TestNG assertion
        Assert.assertEquals(currentUrl, baseUrl, "URL is not opened successfully");
    }

    @BeforeTest
    public  void startReport(){
        initializeReport();
    }

/*   @Test
       public void sampleFailedTest(){
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        extentTest = extentReports.createTest(methodName, "Sample Failed Test Case");
        extentTest.log(Status.INFO, "Starting second fail test run");
        extentTest.assignCategory("Sanity");
        Assert.assertTrue(false);
    }

 */


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


            highlightElement(phoneNumber,driver);

            Assert.assertEquals(phoneNumber, phoneNumber.getAttribute("value"), "Phone number is not entered correctly.");

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
            //boolean isEnabled = checkbox.isEnabled();
            boolean isEnabled = checkbox.isSelected();
            Assert.assertTrue(false, "Checkbox Not Clicked");

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

        public void initializeReport(){
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"//Reports//extentSparkReport.html");
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Automation Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd,yyyy, hh:mm a'('zzz')'");
        extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
        }

        public static String captureScreenshot(WebDriver driver)throws IOException {
        String FileSeperator = System.getProperty("file.separator");
        String Extent_report_path = "."+FileSeperator+"report";
        String ScreenshotPath = Extent_report_path+FileSeperator+"screenshot";
        File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String screenshotName = "screenshot"+Math.random()+".png";
        String screenshotPath = ScreenshotPath+FileSeperator+screenshotName;
        FileUtils.copyFile(src, new File(screenshotPath));
            InputStream is = Files.newInputStream(Paths.get(screenshotPath));
            byte[] ssBytes = IOUtils.toByteArray(is);
            String base64 = Base64.getEncoder().encodeToString(ssBytes);
            JsonDataTest.extentTest.log(Status.FAIL, (Markup) JsonDataTest.extentTest.addScreenCaptureFromBase64String("data:image/png;base64," +base64));
            return screenshotPath;
        }
        void highlightElement(WebElement e, WebDriver driver){
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].style.border = '2px solid red'", e);
        }

        @AfterTest
        public void endReport(){
        extentReports.flush();
        }

        @AfterMethod
        public void tearDown() {
            // Quit the WebDriver instance
            driver.quit();
        }
}
