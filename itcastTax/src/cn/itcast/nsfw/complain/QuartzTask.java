package cn.itcast.nsfw.complain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuartzTask {

	public void doSimpleTriggerTask(){
		System.out.println("do simpletrigger task..." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
	public void doCronTriggerTask(){
		System.out.println("do cronTrigger task..." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
}
