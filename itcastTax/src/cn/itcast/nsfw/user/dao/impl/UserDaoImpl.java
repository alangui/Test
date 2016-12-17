package cn.itcast.nsfw.user.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import cn.itcast.core.dao.impl.BaseDaoImpl;
import cn.itcast.nsfw.user.dao.UserDao;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{

	@Override
	public List<User> findUserByAccountAndId(String id, String account) {
		String sql = "from User where account=?";
		if(StringUtils.isNotBlank(id)){
			sql += " and id!=?";
		}
		Query query = getSession().createQuery(sql);
		query.setParameter(0, account);
		if(StringUtils.isNotBlank(id)){
			query.setParameter(1, id);
		}
		return query.list();
	}

	@Override
	public void saveUserRole(UserRole userRole) {
		getHibernateTemplate().save(userRole);
	}

	@Override
	public List<UserRole> getUserRolesByUserId(String userId) {
		Query query = getSession().createQuery("from UserRole where id.userId=?");
		query.setParameter(0, userId);
		return query.list();
	}

	@Override
	public void deleteUserRoleByUserId(Serializable userId) {
		Query query = getSession().createQuery("delete from UserRole where id.userId=?");
		query.setParameter(0, userId);
		query.executeUpdate();
	}

	@Override
	public List<User> findUsersByAccountAndPass(String account, String password) {
		Query query = getSession().createQuery("from User where account=? and password=?");
		query.setParameter(0, account);
		query.setParameter(1, password);
		return query.list();
	}

}
