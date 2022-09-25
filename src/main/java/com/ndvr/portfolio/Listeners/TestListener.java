package com.ndvr.portfolio.Listeners;

import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.ndvr.portfolio.extentReport.ExtentManager;
import com.ndvr.portfolio.extentReport.ExtentTestManager;

import org.apache.commons.lang3.SystemUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

import java.io.IOException;

public class TestListener implements ITestListener {

    public void onStart(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " started ***");
    }

    public void onFinish(ITestContext context) {
        System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }

    public void onTestStart(ITestResult result) {
        System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
        ExtentTestManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");
        ExtentTestManager.getTest().log(Status.FAIL, MarkupHelper.createCodeBlock(String.valueOf(result.getThrowable())));
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
        ExtentTestManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
    }

}
