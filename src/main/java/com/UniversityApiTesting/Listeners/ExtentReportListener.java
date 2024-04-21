package com.UniversityApiTesting.Listeners;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.UniversityApiTesting.Utilities.PropertyUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportListener extends TestListenerAdapter {
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;

	public void onStart(ITestContext tr) {
		String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		String repName = "Report_" + timeStamp + ".html";

		htmlReporter = new ExtentHtmlReporter("./reports/" + repName);

		htmlReporter.config().setDocumentTitle("University API Test Report");
		htmlReporter.config().setReportName(repName);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setProtocol(Protocol.HTTPS);
		htmlReporter.config().setTimeStampFormat(timeStamp);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter); // attach extent to htmlrepoter becuase extend builds html reprort

		// extent report configurations
		extent.setSystemInfo("Name of tester: ", "Parul");
		extent.setSystemInfo("Environment", "Testing");
		extent.setSystemInfo("University API",
				PropertyUtil.getProperty("src/test/resources/config.properties", "baseUrl"));

	}

	public void onTestSuccess(ITestResult tr) {
		test = extent.createTest(tr.getMethod().getMethodName());
		test.log(Status.PASS, MarkupHelper.createLabel(tr.getName(), ExtentColor.GREEN));

	}

	public void onTestFailure(ITestResult tr) {
		test = extent.createTest(tr.getMethod().getMethodName());
		test.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED));

	}

	public void onTestSkipped(ITestResult tr) {
		test = extent.createTest(tr.getMethod().getMethodName());
		test.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));

	}

	public void onFinish(ITestContext tr) {
		extent.flush();		
		 tr.getPassedTests().getAllResults().stream()
         .map(ITestResult::getName) // Map ITestResult to test names
         .forEach(testName -> System.out.println("PASSED - " + testName));
		 
		 tr.getFailedTests().getAllResults().stream()
         .map(ITestResult::getName) 
         .forEach(testName -> System.out.println("FAILED - " + testName));
		 
		 tr.getSkippedTests().getAllResults().stream()
         .map(ITestResult::getName)
         .forEach(testName -> System.out.println("SKIPPED - " + testName));
		 
	}

}
