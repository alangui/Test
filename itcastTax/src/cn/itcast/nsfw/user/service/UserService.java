package cn.itcast.nsfw.user.service;

import java.io.File;
import java.util.List;

import javax.servlet.ServletOutputStream;

import cn.itcast.core.service.BaseService;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;

public interface UserService extends BaseService<User>{

	public void exportExcel(List<User> userList,ServletOutputStream outputStream);
	public void importExcel(File userExcel,String userExcelFileName);
	public List<User> findUserByAccountAndId(String id,String account);
	public void saveUserAndRole(User user,String... roleIds);
	public List<UserRole> getUserRolesByUserId(String userId);
	public void updateUserAndRole(User user,String... roleIds);
	public List<User> findUserByAccountAndPass(String account,String password);
}
