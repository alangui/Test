package cn.itcast.nsfw.user.dao;

import java.io.Serializable;
import java.util.List;

import cn.itcast.core.dao.BaseDao;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;

public interface UserDao extends BaseDao<User>{

	public List<User> findUserByAccountAndId(String id,String account);
	
	public void saveUserRole(UserRole userRole);
	public List<UserRole> getUserRolesByUserId(String userId);
	public void deleteUserRoleByUserId(Serializable userId);
	public List<User> findUsersByAccountAndPass(String account,String password);
}
