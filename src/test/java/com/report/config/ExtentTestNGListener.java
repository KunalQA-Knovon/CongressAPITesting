package com.report.config;
import com.aventstack.extentreports.ExtentReports;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNGListener implements ITestListener {
    ExtentReports extent = ExtentManager.getReporter();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentManager.setTest(extent.createTest(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("✅ Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getTest().fail("❌ Test Failed: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}