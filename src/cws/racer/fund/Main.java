package cws.racer.fund;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.*;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.edge.driver","./EdgeDriver.exe");
		// TODO Auto-generated method stub
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create(); 
	    
		List<VideoInstance> recordedVideos = new ArrayList<VideoInstance>();
		VideoInstance placeholder = new VideoInstance();
		placeholder.addTimeStamp(1);
		placeholder.addPlaysAmount(1);
		placeholder.setBvNum("This is a placeholder");
		recordedVideos.add(placeholder);

		WebDriver driver = new EdgeDriver();
		driver.get("https://search.bilibili.com/all?keyword=%E6%A0%B8%E5%BA%9F%E6%B0%B4&order=pubdate");
		driver.navigate().refresh();
		
		Thread.sleep(5000);
		
		Date date = new Date();
	    int currentTime = (int) (date.getTime()/1000);
	    
		List<WebElement> foundVideos = driver.findElements(By.cssSelector(".video-list-item:not(.to_hide_xs)"));
		
		for(int i=0;i<foundVideos.size();i++) {
			String videoLink = foundVideos.get(i).findElement(By.cssSelector(".bili-video-card__info--right")).findElements(By.xpath("./child::*")).get(0).getAttribute("href");
			String StringtotalPlays = foundVideos.get(i).findElement(By.cssSelector(".bili-video-card__stats--item")).getText();

			String BvNumber = videoLink.substring(31, 43);
			int timeStamp=currentTime;
			int totalPlays = Integer.valueOf(StringtotalPlays);
			
			boolean videoExist = false;
			for(int j=0;j<recordedVideos.size();j++) {
				if(BvNumber.equals(recordedVideos.get(j).getBvNum())) {
					videoExist = true;
					break;
				}
			}
			if(!videoExist) {
				VideoInstance video = new VideoInstance();
				video.setBvNum(BvNumber);
				video.addTimeStamp(timeStamp);
				video.addPlaysAmount(totalPlays);
				recordedVideos.add(video);
			}
		}		
	    String jsonString = gson.toJson(recordedVideos); 
	    System.out.println(jsonString);
	    driver.close();
	}
}

class VideoInstance { 
	   private String BvNum; 
	   private List<Integer> timeStamp;
	   private List<Integer> videoTotalPlays;

	   public VideoInstance(){
		   timeStamp = new ArrayList<Integer>();
		   videoTotalPlays = new ArrayList<Integer>();
	   } 
	   
	   public String getBvNum() { 
	      return BvNum; 
	   }
	   
	   public void setBvNum(String str) { 
	      this.BvNum = str; 
	   } 
		   
	   public void addTimeStamp(int ts) { 
		   this.timeStamp.add(ts);
	   } 
	   
	   public void addPlaysAmount(int pa) { 
		   this.videoTotalPlays.add(pa);
	   } 
	   
	}