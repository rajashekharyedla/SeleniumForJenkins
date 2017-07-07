package com.wavelabs.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;


public class SeleniumScriptForJenkins {
	public static WebDriver driver = null;
	Properties prop = new Properties();
	static final Logger log = Logger.getLogger(SeleniumScriptForJenkins.class);

	@Test
	public void test() throws InterruptedException {
		//System.setProperty("webdriver.chrome.driver", "D://Seleniumjars//chromedriver.exe");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream("src/main/resources/application.properties");
		} catch (FileNotFoundException e) {
			log.error(e);
		}
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			log.error(e);
		}
		
		System.setProperty("webdriver.chrome.driver",prop.getProperty("chromeDriverPath"));
		driver = new ChromeDriver();
		//driver.manage().window().setPosition(new Point(-2000, 0));
		driver.get(prop.getProperty("url"));
		driver.findElement(By.id("j_username")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.name("j_password")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.id("yui-gen1-button")).click();
		while (true) {
			testProjects();
			testResults();
		}
	}

	private static void testProjects() throws InterruptedException {
		driver.manage().window().maximize();
		driver.findElement(By.id("open-blueocean-in-context")).click();
		Thread.sleep(5000);
		List<WebElement> divElements = driver
				.findElements(By.xpath("//pre[@class='commitId']/preceding-sibling::span/parent::span/parent::div"));
		List<WebElement> commitLinks = driver.findElements(By.xpath("//pre[@class='commitId']"));
		try {
			for (int i = 0; i < divElements.size(); i++) {
				driver.manage().window().maximize();
				WebElement innerDiv = divElements.get(i);
				innerDiv.getAttribute("class");
				commitLinks.get(i).click();
				Thread.sleep(10000);
				driver.navigate().back();
				Thread.sleep(10000);
				divElements = driver.findElements(
						By.xpath("//pre[@class='commitId']/preceding-sibling::span/parent::span/parent::div"));
				commitLinks = driver.findElements(By.xpath("//pre[@class='commitId']"));
			}
		} finally {

		}
	}

	private static void testResults() throws InterruptedException {
		/*
		 * driver.findElement(By.id("open-blueocean-in-context")).click();
		 * Thread.sleep(5000);
		 */
		driver.manage().window().setPosition(new Point(-2000, 0));
		driver.findElement(By.xpath(".//*[@id='root']/div/section/div/div/nav/a[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("jenkins-name-icon")).click();
		Thread.sleep(1000);
		try {
			List<WebElement> jobList = driver.findElements(By.xpath("//tr[@class=' job-status-blue']/td[3]//a"));
			driver.manage().window().maximize();
			for (int i = 0; i < jobList.size(); i++) {
				jobList.get(i).click();
				Thread.sleep(10000);
				driver.findElement(By.xpath("//a[contains(text(),'Coverage Report')]")).click();
				Thread.sleep(10000);
				driver.manage().window().setPosition(new Point(-2000, 0));
				driver.navigate().back();
				// Thread.sleep(1000);
				driver.manage().window().maximize();
				driver.findElement(By.xpath("//a[contains(text(),'Test Results Analyzer')]")).click();
				Thread.sleep(10000);
				driver.manage().window().setPosition(new Point(-2000, 0));
				driver.findElement(By.xpath("//a[contains(text(),'Back to Dashboard')]")).click();
				// Thread.sleep(1000);
				driver.manage().window().maximize();
				jobList = driver.findElements(By.xpath("//tr[@class=' job-status-blue']/td[3]//a"));
			}
		} finally {
			// driver.quit();
		}
	}
}
