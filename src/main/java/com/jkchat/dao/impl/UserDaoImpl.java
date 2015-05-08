package com.jkchat.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jkchat.dao.UserDao;
import com.jkchat.models.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
	private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	public User getuserDetails(String username) {
		logger.debug("inside getUser ");
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(User.class);
		cr.add(Restrictions.eq("name", username));
		User foundUser = (User) cr.uniqueResult();
		logger.debug("end getUser ");
		return foundUser;
	}

	public boolean addUser(User user) {
		logger.debug("inside addUser ");
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
		logger.debug("end addUser ");
		return true;
	}

	@Override
	public List<String> getAllUserNames() {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(User.class);
		cr.setProjection(Projections.property("name"));
		return cr.list();
	}

	@Override
	public List<String> getAllOtherNames(String name) {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(User.class);
		cr.setProjection(Projections.property("name"));
		cr.add(Restrictions.ne("name", name));
		return cr.list();
	}

}
