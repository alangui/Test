package cn.itcast.test.dao.impl;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import cn.itcast.test.dao.TestDao;
import cn.itcast.test.entity.Person;

public class TestDaoImpl implements TestDao {
	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public void save(Person person) {
		this.sessionFactory.getCurrentSession().save(person);
	}

	@Override
	public Person findPerson(Serializable id) {
		return (Person) sessionFactory.getCurrentSession().get(Person.class, id);
	}

}
