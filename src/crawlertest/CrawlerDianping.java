package crawlertest;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.mysql.jdbc.Connection;

public class CrawlerDianping {

	private static String currentWindow;
	private static String xiangxiWindow;
	private static String kouwei;
	private static String huanjing;
	private static String fuwu;
	private static String comment_all;
	private static String url;
	private static String title;
	private static String pinglun;
	private static WebElement name;
	private static WebElement time;
	private static String shop_comment_all;
	
	
	public static WebElement doesWebElementExist(WebElement driver, By selector)
	{ 

		WebElement ele;
	        try 
	        { 
	               ele=driver.findElement(selector); 
	               return ele; 
	        } 
	        catch (NoSuchElementException e) 
	        { 
	        		
	                return null; 
	        } 
	}
	
	public static WebElement doesWebElementExist(WebDriver driver, By selector)
	{ 

		WebElement ele;
	        try 
	        { 
	               ele=driver.findElement(selector); 
	               return ele; 
	        } 
	        catch (NoSuchElementException e) 
	        { 
	        		
	                return null; 
	        } 
	}

	
	
	
	public static void getComment(WebDriver driver) throws InterruptedException,NoSuchElementException
	{
		Connection conn=DBHelper.getConnection();
		String sql_1;
		String sql_2;
		//List<WebElement> piclink = driver.findElements(By.cssSelector("div.pic>a>img"));
		//System.out.println(piclink.size());
		WebElement nextPage =doesWebElementExist(driver,By.cssSelector("a.next")); 
		//driver.findElement(By.cssSelector("a.next"));
		WebDriver window;
		
		do
		//while(nextPage!=null)
		{
			List<WebElement> piclink = driver.findElements(By.cssSelector("div.pic>a>img"));
			currentWindow = driver.getWindowHandle();
		for (int i = 0; i < piclink.size(); i++) {
			
			WebElement pinjia = piclink.get(i);
			pinjia.click();
			

			Set<String> handles = driver.getWindowHandles();
			for (String s : handles) {
				WebDriver xiangxi;
				// current page is don't close
				if (s.equals(currentWindow))
					continue;
				else {
					window = driver.switchTo().window(s);
					//
					//try {
						WebElement more = doesWebElementExist(window,By.cssSelector("p.comment-all>a"));
						//window.findElement(By.cssSelector("p.comment-all>a"));
						WebDriver userwindow;
						// if((more=window.findElement(By.cssSelector("p.comment-all>a")))!=null)
						if(more!=null)
						{
						more.click();
						window.close();
						handles = driver.getWindowHandles();
						for (String s2 : handles) {
							if (s2.equals(currentWindow))
								continue;
							else {

								xiangxi = driver.switchTo().window(s2);
								xiangxiWindow=xiangxi.getWindowHandle();
								WebElement nextpage;
								//= doesWebElementExist(xiangxi,By.cssSelector("a.NextPage"));
//								List<WebElement> pages=xiangxi.findElements(By.cssSelector("div.Pages>div.Pages>a"));
//								int page_num=pages.size();
//								System.out.println(page_num);
							//	for(int k=0;k<page_num;k++)

								do
								{
									nextpage = doesWebElementExist(xiangxi,By.cssSelector("a.NextPage"));
									// System.out.println(xiangxi.getTitle());
									String url = xiangxi.getCurrentUrl();
									title = getElementText(xiangxi,"h1>a");
											//xiangxi.findElement(By.cssSelector("h1>a"));
									List<WebElement> commentlist=xiangxi.findElements(By.cssSelector("div.comment-list>ul>li"));
									//System.out.println(commentlist.size());
									for(WebElement comment:commentlist)
									{
										
										
										name = comment.findElement(By.cssSelector("p.name>a"));
										Thread.sleep(1000);
										name.click();
//										
//									
										handles = driver.getWindowHandles();
										
										for (String s3 : handles) {
											if (s3.equals(currentWindow)||s3.equals(xiangxiWindow))
												continue;
											else {
												userwindow=xiangxi.switchTo().window(s3);
												try{
												Thread.sleep(1000);
												comment_all=getElementText(userwindow,"div.nav>ul>li:nth-of-type(2)>a");
												
												}
												catch(NoSuchElementException e)
												{
													System.out.println("cuowu");
													comment_all="点评(0)";
												}
												//comment_all=e_comment_all.getText();
												finally{
												Thread.sleep(1000);
												userwindow.close();
												}
											}
										}
										xiangxi.switchTo().window(xiangxiWindow);
										//System.out.println(xiangxi.getCurrentUrl());
										WebElement e_kouwei = doesWebElementExist(comment, By.cssSelector("div.comment-rst>span.rst:nth-of-type(1)"));
										WebElement e_huanjing = doesWebElementExist(comment, By.cssSelector("div.comment-rst>span.rst:nth-of-type(2)"));
										WebElement e_fuwu = doesWebElementExist(comment, By.cssSelector("div.comment-rst>span.rst:nth-of-type(3)"));
										//WebElement content = comment.findElement(By.cssSelector("div.J_brief-cont"));
										
										time = comment
												.findElement(By.cssSelector("div.misc-info>span.time:nth-of-type(1)"));
										try{
										shop_comment_all=comment.findElement(By.cssSelector("div.user-info>span")).getAttribute("title");
										}
										catch(NoSuchElementException e)
										{
											shop_comment_all="";
											System.out.println("无总评");
										}
										kouwei=e_kouwei==null?"":e_kouwei.getText();
										huanjing=e_huanjing==null?"":e_huanjing.getText();
										fuwu=e_fuwu==null?"":e_fuwu.getText();
										try{
											pinglun=comment_all.substring(3).replace(")", "");
										}
										catch(StringIndexOutOfBoundsException e)
										{
											pinglun="";
										}
										//pinglun=comment_all.substring(3).replace(")", "");
										sql_1="insert into chart_one (name,comment_all) values ('"+name.getText()+"','"+pinglun+"')";
										DBHelper.insert(sql_1, conn);
										sql_2="insert into chart_two (username,shop,time,comment_all,kouwei,huanjing,fuwu,url) "
												+ "values ('"+name.getText()+"','"+title+"','"+time.getText()+"','"+shop_comment_all+"',"
												+ "'"+kouwei+"','"+huanjing+"','"+fuwu+"','"+url+"')";
										//System.out.println(sql_2);
										//System.out.println(sql_1);
//										System.out.println(name.getText()+"在"+title.getText()+"发表评论数"+comment_all
//										+kouwei+huanjing+fuwu+"时间"+time.getText()+"网址："+url+shop_comment_all.getAttribute("title"));
										DBHelper.insert(sql_2, conn);
										
									}
									try{
									
									nextpage.click();
									}
									catch(NullPointerException e)
									{
										System.out.println("end page");
									}
									xiangxi = driver.switchTo().window(driver.getWindowHandle());
									
									
									//xiangxi.findElement(By.cssSelector("a.NextPage"));
									
							
//									

								}while(nextpage!=null);
								xiangxi=xiangxi.switchTo().window(xiangxiWindow);
								xiangxi.close();
							}
						}
						}
				//	} catch (NoSuchElementException e) {
						else{
						System.out.println("无详细页");
						
						title=getElementText(window,"div.breadcrumb>span");
						//window.findElement(By.cssSelector("div.breadcrumb>span"));
						url=window.getCurrentUrl();
						String window_nobrief=window.getWindowHandle();
						WebElement more_2 =doesWebElementExist(window,By.cssSelector("div#comment>h2>a:nth-of-type(2)"));
						if(more_2!=null)
						{
						//window.findElement(By.cssSelector("div#comment>h2>a:nth-of-type(2)"));
						more_2.click();
						List<WebElement> commentlist=window.findElements(By.cssSelector("div#comment>ul.comment-list>li"));
						System.out.println(commentlist.size());
						for(WebElement comment:commentlist)
						{
							
							
							name = comment.findElement(By.cssSelector("a.name"));
//							
							name.click();
//							
							handles = driver.getWindowHandles();
							//System.out.println(handles.size());
							for (String s3 : handles) {
								if (s3.equals(currentWindow)||s3.equals(window_nobrief))
									continue;
								else {
									userwindow=window.switchTo().window(s3);
									try{
									Thread.sleep(1000);
									
									
									comment_all=getElementText(userwindow,"div.nav>ul>li:nth-of-type(2)>a");
									//user_comment_all=userwindow.findElement(By.cssSelector("div.nav>ul>li:nth-of-type(2)>a"));
									//comment_all=user_comment_all.getText();
									}
									catch(NoSuchElementException e)
									{
										System.out.println("cuowu");
										comment_all="";
									}
									//comment_all=e_comment_all.getText();
									Thread.sleep(1000);
									userwindow.close();
									
								}
							}
							window.switchTo().window(window_nobrief);
							//System.out.println(xiangxi.getCurrentUrl());
							WebElement e_kouwei = doesWebElementExist(comment, By.cssSelector("div.content>p.shop-info>span.item:nth-of-type(1)"));
							
							
						
							WebElement e_huanjing = doesWebElementExist(comment, By.cssSelector("div.content>p.shop-info>span.item:nth-of-type(2)"));
							WebElement e_fuwu = doesWebElementExist(comment, By.cssSelector("div.content>p.shop-info>span.item:nth-of-type(3)"));
							//WebElement content = comment.findElement(By.cssSelector("div.content>p.desc"));
						
							
							time = comment
									.findElement(By.cssSelector("div.misc-info>span.time"));
							WebElement shop_comment_all=comment.findElement(By.cssSelector("p.shop-info>span:nth-of-type(1)"));
							kouwei=e_kouwei==null?"":e_kouwei.getText();
							huanjing=e_huanjing==null?"":e_huanjing.getText();
							fuwu=e_fuwu==null?"":e_fuwu.getText();
							try{
								pinglun=comment_all.substring(3).replace(")", "");
							}
							catch(StringIndexOutOfBoundsException e)
							{
								pinglun="";
							}
							sql_1="insert into chart_one (name,comment_all) values ('"+name.getText()+"','"+pinglun+"')";
							DBHelper.insert(sql_1, conn);
							sql_2="insert into chart_two (username,shop,time,comment_all,kouwei,huanjing,fuwu,url) "
									+ "values ('"+name.getText()+"','"+title+"','"+time.getText()+"','"+shop_comment_all.getAttribute("class")+"',"
									+ "'"+kouwei+"','"+huanjing+"','"+fuwu+"','"+url+"')";
							DBHelper.insert(sql_2, conn);
//							System.out.println(name.getText()+"在"+title.getText()+"发表评论数"+comment_all
//							+kouwei+huanjing+fuwu+"时间"+time.getText()+"网址："+url+shop_comment_all.getAttribute("class"));
						}
						
						
						
						window.close();
						}
						else
						{
							window.close();
						}
						
						}
						
					//}

					// System.out.println(piclink.get(i).getText());
				}

				
			}
			driver.switchTo().window(currentWindow);

		}
		nextPage.click();
		driver=driver.switchTo().window(driver.getWindowHandle());
		nextPage=doesWebElementExist(driver,By.cssSelector("a.next"));
		//driver.findElement(By.cssSelector("a.next"));
		
		}while(nextPage!=null);
		
	}
	
	
	public static String getElementText(WebDriver driver,String css)
	{
		WebElement element=doesWebElementExist(driver,By.cssSelector(css));
		String content;
		if(element!=null)
		{
		//driver.findElement(By.cssSelector(css));
		content=element.getText();
		return content;
		}
		else{
			content="";
			return content;
		}
		
		
	}
	
	public static void main(String[] args) {
		System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		
		String proxyIp="183.216.151.74";
		String proxyPort="8123";
		
		FirefoxProfile profile = new FirefoxProfile();
		
		profile.setPreference("network.proxy.type", 1);

		// http协议代理配置

		profile.setPreference("network.proxy.http", proxyIp);
		profile.setPreference("network.proxy.http_port", proxyPort);
		profile.setPreference("network.proxy.share_proxy_settings", true);
		
		//profile.setPreference("network.proxy.no_proxies_on", "localhost");
		WebDriver driver = new FirefoxDriver(profile);

		driver.get("http://www.dianping.com/search/category/2/10/r1497p14");

		try {
			getComment(driver);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NoSuchElementException e){
			e.printStackTrace();
		}
		//System.out.println(driver.getTitle());
		//driver.quit();
	}
}
