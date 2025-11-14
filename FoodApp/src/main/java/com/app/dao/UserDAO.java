package com.app.dao;

import java.util.List;

import com.app.models.User;

public interface UserDAO {
	
	void addUser(User user);
	
	User getUser(int userId);
	
	User getUserByUsername(String username);
	
	void updateUser(User user);
	
	void deleteUser(int userId);
	
	List<User>getAllUsers();
	
	
	
}
