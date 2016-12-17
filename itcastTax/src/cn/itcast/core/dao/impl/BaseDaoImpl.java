package cn.itcast.core.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.itcast.core.dao.BaseDao;
import cn.itcast.core.page.PageResult;
import cn.itcast.core.util.QueryHelper;

public abstract class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T>{

	Class<T> clazz;
	@SuppressWarnings("unchecked")
	public BaseDaoImpl(){
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) pt.getActualTypeArguments()[0];
	}
	@Override
	public void save(T entity) {
		getHibernateTemplate().save(entity);
	}

	@Override
	public void update(T entity) {
		getHibernateTemplate().update(entity);
	}

	@Override
	public void delete(Serializable id) {
		getHibernateTemplate().delete(findObjectById(id));
	}

	@Override
	public T findObjectById(Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findObjects() {
		Query query = getSession().createQuery("FROM " + clazz.getSimpleName());
		return query.list();
	}
	
	@Override
	public List<T> findObjects(String hql,List<Object> parameters){
		Query query = getSession().createQuery(hql);
		if(parameters!=null){
			for(int i=0;i<parameters.size();i++){
				query.setParameter(i, parameters.get(i));
			}
		}
		return query.list();
	}
	
	@Override
	public List<T> findObjects(QueryHelper queryHql){
		Query query = getSession().createQuery(queryHql.getQueryListHql());
		List<Object> parameters = queryHql.getParameters();
		if(parameters!=null){
			for(int i=0;i<parameters.size();i++){
				query.setParameter(i, parameters.get(i));
			}
		}
		return query.list();
	}
	
	@Override
	public PageResult getPageResult(QueryHelper queryHelper,int pageNo,int pageSize){
		Query query = getSession().createQuery(queryHelper.getQueryListHql());
		List<Object> parameters = queryHelper.getParameters();
		if(parameters!=null){
			for(int i=0;i<parameters.size();i++){
				query.setParameter(i, parameters.get(i));
			}
		}

		Query queryCount = getSession().createQuery(queryHelper.getQueryCountHql());
		if( parameters!=null){
			for(int i=0;i<parameters.size();i++){
				queryCount.setParameter(i, parameters.get(i));
			}
		}
		long totalCount = (Long) queryCount.uniqueResult();
		
		if( pageNo < 1 ){
			pageNo=1;
		}
		int tem = (int) (totalCount/pageSize);
		int totalPageCount = (totalCount%pageSize==0)?tem:(tem+1);
		if(pageNo>totalPageCount){
			pageNo = totalPageCount;
		}
		query.setFirstResult( (pageNo-1)*pageSize );
		query.setMaxResults(pageSize);
		List items = query.list();
		return new PageResult(totalCount,pageNo,pageSize,items);
	}
}
