package com.BwleErp.websocket.action;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.BwleErp.config.Config;

public class CrawlerTaobao {
	// 用来存在线连接用户信息
	private static ConcurrentHashMap<String, WebDriver> driverPool = new ConcurrentHashMap<String, WebDriver>();

	public static boolean doCaptchaChromeAndroidLoginTaobao(String mobile) throws Exception {
		WebDriver driver;
		if (driverPool.contains(mobile)) {
			driver = driverPool.get(mobile);
		} else {
			System.setProperty("webdriver.chrome.driver", Config.chromedriver);
			Map<String, Object> dict = new HashMap<>();
			// String userAgent = "\"Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300
			// Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile
			// Safari/534.30\";"
			// String userAgent = "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW)
			// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile
			// Safari/537.36";
			String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1";
			dict.put("userAgent", userAgent);
			Map<String, Object> deviceMetrics = new HashMap<>();
			deviceMetrics.put("width", 360);
			deviceMetrics.put("height", 640);
			deviceMetrics.put("pixelRatio", 3.0);
			dict.put("deviceMetrics", deviceMetrics);
			ChromeOptions options = new ChromeOptions();
			//options.addArguments("--headless");
			options.setExperimentalOption("mobileEmulation", dict);
			driver = new ChromeDriver(options);// 本行添加参数
		}
		driver.get("https://main.m.taobao.com/");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("tb-toolbar-icon-my")));
		driver.findElement(By.className("tb-toolbar-icon-my")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.switchTo().frame(0);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fm-sms-login-id")));
		System.out.println(driver.getTitle());
		driver.findElement(By.className("fm-agreement-text")).click();
		driver.findElement(By.id("fm-sms-login-id")).click();
		driver.findElement(By.id("fm-sms-login-id")).sendKeys(mobile);
		driver.findElement(By.className("sms-login")).click();
		wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		driver.findElement(By.className("send-btn-link")).click();
		// driver.close();
		return true;
	}

	public static boolean captcha(String mobile, String captcha) {
		WebDriver driver;
		if (driverPool.contains(mobile)) {
			driver = driverPool.get(mobile);
			driver.findElement(By.id("tel-box-container")).sendKeys(captcha);
		} else {
			return false;
		}
		return true;
	}
}
