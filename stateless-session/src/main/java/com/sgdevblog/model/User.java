package com.sgdevblog.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sgdevblog.enums.RoleEnum;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id;

	@Column(name="login")
	private String userName;

	@JsonIgnore
	@Column(name="encrypted_password")
	private String password;

	@JsonIgnore
	@Column(name="password_salt")
	private String salt;

	@ManyToOne
	@JoinColumn(name="site_id", nullable=false)
	private Site site;

	@JsonIgnore
	@ManyToMany
	private List<Role> roles;

	@JsonIgnore
	@Transient
	public List<RoleEnum> getUserRoles(){
		List<RoleEnum> userRoles = new ArrayList<>();
		if (roles != null){
			for (Role userRole : roles) {
				userRoles.add(userRole.getRoleEnum());
			}
		}
		return userRoles;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@JsonIgnore
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
}
