package com.BwleErp.controller.crawler;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v124.network.Network;
//import org.openqa.selenium.devtools.v111.network.Network;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.BwleErp.config.Config;
import com.BwleErp.websocket.WebSocketTaobaoCrawlerServer;
import com.base.controller.AbstractAction;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IndexAction extends AbstractAction {	
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if (method == null)
			method = "";
		//
		switch (method) {
		case "login_taobao":
			this.doChromeAndroidLoginTaobao(request, response);
			break;
		case "crawler_taobao":
			this.doCrawlerTaobao(request, response);
			break;
		case "crawler_wstaobao":
			this.doCrawlerWsTaobao(request, response);
			break;	
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public void doChromeAndroidLoginTaobao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mobile = request.getParameter("mobile");
		WebDriver driver = WebSocketTaobaoCrawlerServer.getDriver(mobile);
		WebSocketTaobaoCrawlerServer ws = new WebSocketTaobaoCrawlerServer();
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", Config.chromedriver);
			Map<String, Object> dict = new HashMap<>();
		    //String userAgent = "\"Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30\";"
		    //String userAgent = "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36";
		    String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1";
		    dict.put("userAgent", userAgent);
		    Map<String,Object> deviceMetrics = new HashMap<>();
		    deviceMetrics.put("width", 360);
		    deviceMetrics.put("height", 640);
		    deviceMetrics.put("pixelRatio", 3.0);
		    dict.put("deviceMetrics", deviceMetrics);
		    ChromeOptions options = new ChromeOptions();
		    options.addArguments("--user-data-dir=E:\\workspace\\ChromeUserData");
		    //options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		    //options.setHeadless(true);//并配置headless属性为true，表示不在前台打开chrome。
		    //options.addArguments("--headless");
		    options.setExperimentalOption("mobileEmulation",dict);
		    driver = new ChromeDriver(options);//本行添加参数
		    ws.setDriver(mobile, driver);
		}
		
		ws.setCaptcha(mobile);//清空老验证码
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
		//driver.findElement(By.className("send-btn-link")).click();
		//driver.close();
		
		ws.sendOneMessage("request:captcha", mobile);
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));//等到5秒
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("view-input")));
		if(driver.findElements(By.className("login-error-dialog-ok-btn")).size() > 0) {
			driver.findElement(By.className("login-error-dialog-ok-btn")).click();
		};
		JavascriptExecutor executor = (JavascriptExecutor) driver;
        //System.out.println(executor.executeScript(xxx));
		String captcha = "";
		int min = 1;
		while(captcha.equals("") && min <= 30) {
			captcha = ws.getCaptcha(mobile);
			System.out.println("captcha=====>"+captcha);
			if(!captcha.equals("")) {
				System.out.println("captcha=====>"+1);
				//driver.findElement(By.id("fm-sms-login-id")).sendKeys(captcha);
				System.out.println("captcha=====>"+2);
				//driver.findElement(By.className("sim-input")).sendKeys(captcha);// 
				//driver.findElement(By.className("sim-input-container")).sendKeys(captcha);
				
				//driver.findElement(By.className("tel-box-container")).sendKeys(captcha);
				if(driver.findElements(By.className("view-input")).size() > 0) {
					System.out.println("driver.findElements(By.className(\"view-input\")).size()=====>"+driver.findElements(By.className("view-input")).size());
					//driver.findElements(By.className("view-input")).get(0).sendKeys("1");
				}
				System.out.println("div[@class=view-input]=====>"+4);
				//;//'//input[@name="email"]' '//iframe[@scrolling="no"]'
				WebElement element = driver.findElement(By.xpath("//div[@class='view-input focus sms-login-input']"));
				executor.executeScript("arguments[0].click", element);
				Actions action = new Actions(driver);
				action.moveToElement(element).sendKeys(captcha).perform();
				//driver.findElement(By.xpath("//div[@class='view-input focus sms-login-input']")).sendKeys(captcha);
				//driver.findElement(By.className("sms-login-input")).sendKeys(captcha);
				break;
			}
			Thread.sleep(1000);;//等到1秒
			min++;
		}
		ws.setCaptcha(mobile);
		this.success.setErrorCode(ErrorCode.__T_SUCCESS);
	}

	public void doCrawlerTaobao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mobile = request.getParameter("mobile");
		String url = "https://main.m.taobao.com/security-h5-detail/home?id=524247843919&scm=1007.18975.229300.0&pvid=3d152b9f-0000-4b2d-bfd9-b98c4709bb00&utparam=%7B%22ranger_buckets_native%22%3A%22%22%2C%22x_object_type%22%3A%22item%22%2C%22mtx_ab%22%3A9%2C%22mtx_sab%22%3A0%2C%22scm%22%3A%221007.18975.229300.0%22%2C%22x_object_id%22%3A%22524247843919%22%7D&spm=a2141.8138988.guess.0&fromNormal=true";
		System.setProperty("webdriver.chrome.bin",Config.chromebin);
		System.setProperty("webdriver.chrome.driver", Config.chromedriver);
		//System.setProperty("webdriver.http.factory", "jdk-http-client");
		WebDriver driver = WebSocketTaobaoCrawlerServer.getDriver(mobile);
		WebSocketTaobaoCrawlerServer ws = new WebSocketTaobaoCrawlerServer();
		if (driver == null) {
			Map<String, Object> dict = new HashMap<>();
		    //String userAgent = "\"Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30\";"
		    //String userAgent = "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36";
		    String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1";
		    dict.put("userAgent", userAgent);
		    Map<String,Object> deviceMetrics = new HashMap<>();
		    deviceMetrics.put("width", 360);
		    deviceMetrics.put("height", 640);
		    deviceMetrics.put("pixelRatio", 3.0);
		    dict.put("deviceMetrics", deviceMetrics);
		    ChromeOptions options = new ChromeOptions();
		    options.addArguments("--user-data-dir=E:\\workspace\\ChromeUserData");
		    
		    options.addArguments("--no-sandbox");
		    options.addArguments("--disable-dev-shm-usage");
		    options.addArguments("--disable-gpu");
		    options.addArguments("--remote-debugging-port=9222");
		    options.addArguments("--single-process");
		    
		    //options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
		    //options.setHeadless(true);//并配置headless属性为true，表示不在前台打开chrome。
		    options.addArguments("--headless");
		    //options.addArguments("--disable-blink-features");
		    //options.addArguments("--disable-blink-features=AutomationControlled");
		    options.setExperimentalOption("mobileEmulation",dict);
		    options.setBinary(Config.chromebin);
		    //options.addArguments("--remote-allow-origins=*");
		    driver = new ChromeDriver(options);//本行添加参数
		    ws.setDriver(mobile, driver);
		}
		driver.get(url);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ice-container")));
		String html = driver.getPageSource();
		Document document = null;
		if(html != null) {
			document = Jsoup.parse(html);
		} else {
			document = Jsoup.connect(url).get();
		}
		if (document != null) {
			System.out.println("reshtml==>开始");
			Element resultEle = document.getElementById("ice-container");
			if (resultEle != null) {//
				HashMap<String,String> taobaoProductMap = new HashMap<>();
				Elements resClassEle = resultEle.getElementsByAttributeValueStarting("class", "title--");
				if (resClassEle != null) {//
					for (int i = 0; i < resClassEle.size(); i++) {
						// 遍历行
						String reshtml = resClassEle.get(i).html();
						System.out.println("reshtml==>"+reshtml);
						ws.sendOneMessage(reshtml, mobile);
					}
				}
			}
		}
		driver.close();
		this.success.setErrorCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doCrawlerWsTaobao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mobile = request.getParameter("mobile");
		String url = "https://main.m.taobao.com/security-h5-detail/home?id=524247843919&scm=1007.18975.229300.0&pvid=3d152b9f-0000-4b2d-bfd9-b98c4709bb00&utparam=%7B%22ranger_buckets_native%22%3A%22%22%2C%22x_object_type%22%3A%22item%22%2C%22mtx_ab%22%3A9%2C%22mtx_sab%22%3A0%2C%22scm%22%3A%221007.18975.229300.0%22%2C%22x_object_id%22%3A%22524247843919%22%7D&spm=a2141.8138988.guess.0&fromNormal=true";
		System.setProperty("webdriver.chrome.bin",Config.chromebin);
		System.setProperty("webdriver.chrome.driver", Config.chromedriver);
		
		ChromeOptions options = new ChromeOptions();
	    options.setExperimentalOption("debuggerAddress", "http://127.0.0.1:9222");
		 WebDriver driver =  new ChromeDriver(options);
	        driver = new Augmenter().augment(driver);
	        DevTools devTools = ((HasDevTools) driver).getDevTools();
	        devTools.createSession();
	        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
	        devTools.addListener(Network.responseReceived(),
	                entry -> {
	                    System.out.println("Request Id:"+entry.getRequestId() + "\t" + entry.getResponse().getUrl());
	                });
	        driver.get(url);
		
		this.success.setErrorCode(ErrorCode.__T_SUCCESS);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void doChromeIphoneLoginTaobao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.setProperty("webdriver.chrome.driver","D:/app/webdriver/chromedriver.exe");
		//
		HashMap<String, String> mobileEmulation = new HashMap<>();// 增加本行
		mobileEmulation.put("deviceName", "iPhone X");// 这里是要使用的模拟器名称
		ChromeOptions chromeOptions = new ChromeOptions();//增加本行
		chromeOptions.setExperimentalOption("mobileEmulation",mobileEmulation);//增加本行
		WebDriver driver = new ChromeDriver(chromeOptions);//本行添加参数
		//
		

		this.success.setErrorCode(ErrorCode.__F_AUTH);
	}
	
	public void doFirefoxLoginTaobao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.setProperty("webdriver.firefox.bin", Config.firefoxbin);
		System.setProperty("webdriver.gecko.driver", Config.geckoDriver);
		File pathToFirefoxBinary = new File(Config.firefoxbin);
		FirefoxBinary firefoxbin = new FirefoxBinary(pathToFirefoxBinary);
		FirefoxOptions fireFoxOptions = new FirefoxOptions();
		fireFoxOptions.addArguments("--headless");
		fireFoxOptions.setBinary(firefoxbin);
		WebDriver driver = new FirefoxDriver(fireFoxOptions);// 这里使用这个构造方法。
		//	

		this.success.setErrorCode(ErrorCode.__F_AUTH);
	}

	public void doTestTaobao(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String verify_code = request.getParameter("verify_code");
		if (verify_code != null && !verify_code.equals("")) {
			String url = "https://www.chsi.com.cn/xlcx/bg.do?srcid=bgcx&vcode=" + verify_code.trim();
			System.setProperty("webdriver.firefox.bin", Config.firefoxbin);
			System.setProperty("webdriver.gecko.driver", Config.geckoDriver);
			File pathToFirefoxBinary = new File(Config.firefoxbin);

			FirefoxBinary firefoxbin = new FirefoxBinary(pathToFirefoxBinary);
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless");
			options.setBinary(firefoxbin);
			WebDriver driver = new FirefoxDriver(options);// 这里使用这个构造方法。
			// WebDriver driver = new FirefoxDriver();
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("resultTable")));
			String html = driver.getPageSource();
			// System.out.println("============>"+html);
			driver.quit();
			Document document = null;
			if (html != null) {
				document = Jsoup.parse(html);
			} else {
				document = Jsoup.connect(url).get();
			}
			if (document != null) {
				Element resultEle = document.getElementById("resultTable");
				Elements resClassEle = resultEle.getElementsByClass("report-info-item");
			}
		}

		this.success.setErrorCode(ErrorCode.__F_AUTH);
	}
}
/*
 * Actions类下的不同方法
键盘接口
鼠标界面 基本上有两种方法可以帮助处理Selenium中的操作，即：让我们详细了解它们。
sendKeys(KeysToSend)：向元素发送一系列按键。
keydown(Thekey)：发送不释放的按键。后续操作可能会将其假定为已按下。
keyUp(Thekey)：执行密钥释放。
Click ()：单击元素。
DoubleClick()：双击元素。
contextClick()：在元素上执行上下文单击(右键单击)。
clickAndHold()：在当前鼠标位置单击，不松开。
dragAndDrop(source，target)：在释放鼠标之前，单击源位置并移动到目标元素的位置。源(要抓取的元素，要释放的目标元素)。
dragAndDropBy(source，xOffset，yOffset)：在源位置单击并按住，移动给定的偏移值，然后释放鼠标。(X偏移-水平平移，Y偏移-垂直平移)。
moveByOffset(x-Offset，y-Offset)：将鼠标从其当前位置(或0，0)偏移给定的偏移量。x-Offset-设置水平偏移(负值-向左移动鼠标)，y-Offset-设置垂直偏移(负值-向上移动鼠标)。
moveToElement(ToElement)：将鼠标移动到元素的中心。
Release()：在现有鼠标位置释放按下的鼠标左键。
键盘接口例如：Keys.ALT、Keys.SHIFT或Keys.CONTROL)；鼠标接口一旦您对Actions类下的不同方法有了正确的了解，就很容易实现相同的用例。
*/
