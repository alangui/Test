package cn.itcast.test.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import cn.itcast.test.service.TestService;

import com.opensymphony.xwork2.ActionSupport;


@Scope("prototype")
public class TestAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	@Resource
	private TestService testService;
	
	public String execute(){
		testService.say();
		return SUCCESS;
	}
}
