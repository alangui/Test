package cn.itcast.login.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import cn.itcast.core.constant.Constant;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.service.UserService;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	@Resource
	private UserService userService;
	private String loginResult;
	private User user;
	

	public String toLoginUI(){
		return "loginUI";
	}
	
	public String login(){
		if(user!=null){
			if(StringUtils.isNotBlank(user.getAccount()) && StringUtils.isNotBlank(user.getPassword())){
				List<User> list = userService.findUserByAccountAndPass(user.getAccount(),user.getPassword());
				if(list!=null && list.size()>0){
					User user = list.get(0);
					user.setUserRoles(userService.getUserRolesByUserId(user.getId()));
					ServletActionContext.getRequest().getSession().setAttribute(Constant.USER, user);
					Log log = LogFactory.getLog(getClass());
					log.info("用户名称为" + user.getName() + "的用户登录了系统！");
					return "home";
				}else{
					loginResult = "账号或密码不正确！";
				}
			}else{
				loginResult = "用户名或密码不能为空！";
			}
		}else{
			loginResult = "请输入用户名和密码！";
		}
		return toLoginUI();
	}
	
	public String logout(){
		ServletActionContext.getRequest().getSession().removeAttribute(Constant.USER);
		return toLoginUI();
	}
	
	public String toNoPermissionUI(){
		return "noPermissionUI";
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}
	
}
