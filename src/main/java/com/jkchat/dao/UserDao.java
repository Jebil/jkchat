package com.jkchat.dao;

import java.util.List;

import com.jkchat.models.User;

public interface UserDao {
	public User getuserDetails(String username);

	public boolean addUser(User user);

	public List<String> getAllUserNames();

	public List<String> getAllOtherNames(String name);
}
