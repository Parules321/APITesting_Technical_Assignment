package com.UniversityApiTesting.Listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	int cnt = 0;

	int maxCnt = 1;

	@Override
	public boolean retry(ITestResult result) {

		if (cnt < maxCnt) {
			System.out.println("Test failed, Retrying for " + cnt + " ,Test Name : " + result.getMethod().getMethodName());
			cnt++;
			return true;
		}

		return false;
	}

}
