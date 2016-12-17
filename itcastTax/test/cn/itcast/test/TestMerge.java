package cn.itcast.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.test.entity.Person;
import cn.itcast.test.service.TestService;
import cn.itcast.test.service.impl.TestServiceImpl;

public class TestMerge {
	
	private ApplicationContext ac;
	@Before
	public void loadXml(){
		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	@Test
	public void testSpring() {
		TestServiceImpl testService = (TestServiceImpl) ac.getBean("testService");
		testService.say();
	}
	@Test
	public void testServiceAndDao() throws Exception {
		TestService ts = (TestService) ac.getBean("testService");
		ts.save(new Person("tiantian"));
	}
	@Test
	public void testTrasactionReadOnly(){
		TestService tx = (TestService) ac.getBean("testService");
		System.out.println(tx.findPerson("ef3ae795557330780155733079920000").getName());
	}
	@Test
	public void testTracsactionRollBack(){
		TestService ts = (TestService) ac.getBean("testService");
		ts.save(new Person("测试"));
	}
}
