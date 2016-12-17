package cn.itcast.test.dao;

import java.io.Serializable;

import cn.itcast.test.entity.Person;

public interface TestDao {
	
	public void save(Person person);
	public Person findPerson(Serializable id);
}
