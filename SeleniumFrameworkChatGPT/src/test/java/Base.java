import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Base {

    public Base(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }
    public  static ExtentSparkReporter sparkReporter;
    public static ExtentReports extentReports;
    public static ExtentTest extentTest;
    private WebDriver driver;
    public String baseUrl = "https://qa-practice.netlify.app/bugs-form.html";

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

    public void initializeReport(){
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+ "/Reports/extentSparkReport.html");
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Automation Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd,yyyy, hh:mm a'('zzz')'");
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    public static String captureScreenshot(WebDriver driver)throws IOException {
        String FileSeperator = System.getProperty("file.seperator");
        String Extent_report_path = "."+FileSeperator+"report";
        String ScreenshotPath = Extent_report_path+FileSeperator+"screenshots";
        File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String screenshotName = "screenshot"+Math.random()+".png";
        String screenshotPath = ScreenshotPath+FileSeperator+screenshotName;
        FileUtils.copyFile(src, new File(screenshotPath));
        return "."+FileSeperator+"screenshots"+FileSeperator+screenshotName;
    }

    @AfterTest
    public void endReport(){
        extentReports.flush();
    }
}
