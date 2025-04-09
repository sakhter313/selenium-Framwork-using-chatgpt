import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.json.Json;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class CustomListener implements ITestListener {
    @Override
    public void onTestSuccess(ITestResult result) {
        JsonDataTest.extentTest.log(Status.PASS, MarkupHelper.createLabel(result.getName().toUpperCase()
                +":PASS", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        //JsonDataTest.extentTest.log(Status.FAIL, result.getThrowable().getMessage());
        JsonDataTest.extentTest.log(Status.FAIL, MarkupHelper.createLabel(result.getName().toUpperCase()
                +":FAIL", ExtentColor.RED));
        try {
            JsonDataTest.extentTest.addScreenCaptureFromPath(JsonDataTest.captureScreenshot(JsonDataTest.driver));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
