package com.joseph.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.joseph.dao.UserDao;
import com.joseph.model.User;
@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private SessionFactory session;
	
	@Override
	public void addUser(User user) {
		session.getCurrentSession().save(user);
	}

	@Override
	public void editUser(User user) {
		session.getCurrentSession().update(user);
	}

	@Override
	public void deleteUser(int userId) {
		session.getCurrentSession().delete(findUser(userId));
	}

	@Override
	public User findUser(int userId) {
		return (User) session.getCurrentSession().get(User.class, userId);
	}

	@Override
	public User findUserByName(String username) {
		Criteria criteria = session.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));		
		return (User) criteria.uniqueResult();
	}

	@Override
	public List<User> getAllUsers() {
		return session.getCurrentSession().createQuery("from User").list();
	}

}
