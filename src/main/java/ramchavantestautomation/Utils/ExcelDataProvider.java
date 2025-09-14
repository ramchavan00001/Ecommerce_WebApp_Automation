package ramchavantestautomation.Utils;

//package com.ramchavantestautomation.utils;

import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

    @DataProvider(name = "loginTestData")
    public Object[][] getLoginData() {
        String filePath = System.getProperty("user.dir") + "/testdata/TestData.xlsx";
        return ExcelUtil.readExcelData(filePath, "LoginData");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        String filePath = System.getProperty("user.dir") + "/testdata/TestData.xlsx";
        return ExcelUtil.readExcelData(filePath, "InvalidLoginData");
    }

    // Add more providers for other sheets as needed
}
