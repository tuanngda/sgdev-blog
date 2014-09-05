package com.sgdevblog.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sgdevblog.model.Site;


@Repository
@Transactional
public class SiteDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Site> getAllSite() {
		Query query = entityManager.createQuery("SELECT e FROM Site e", Site.class);

		List<Site> sites = query.getResultList();

		if(sites==null){
			sites = new ArrayList<Site>();
		}


		return sites;
	}

}
