package cn.itcast.test.service;

import java.io.Serializable;

import cn.itcast.test.entity.Person;

public interface TestService {
	public void say();
	
	public void save(Person person);
	
	public Person findPerson(Serializable id);
}
