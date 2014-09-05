package com.sgdevblog.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sgdevblog.model.User;

@Repository
@Transactional
public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked") //Query.getResultList, does not support generic
	public User getUser(String siteCode, String userName) {
		String sql = "SELECT e FROM User e WHERE e.userName = :userName and e.site.siteCode = :siteCode";


		Query query = entityManager.createQuery(sql, User.class);
		query.setParameter("siteCode", siteCode);
		query.setParameter("userName", userName);
		List<User> users = query.getResultList();

		if (users!=null && users.size() == 1){
			return users.get(0);
		}


		return null;
	}

	@SuppressWarnings("unchecked")
	public User getUser(int userId){
		User user = null;

		Query query = entityManager.createQuery("SELECT e FROM User e WHERE e.id = :userId", User.class);
		query.setParameter("userId", userId);

		List<User> users = (List<User>)query.getResultList();

		if (users!=null && users.size() == 1){
			user = users.get(0);
		}

		return user;
	}

}
