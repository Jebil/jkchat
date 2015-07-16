package com.jkchat.dao;

import java.util.List;

import com.jkchat.models.ChatMessage;
import com.jkchat.models.ServerLocation;
import com.jkchat.models.User;
import com.jkchat.models.UserMessages;

/**
 * @author Jebil Kuruvila
 *
 */
public interface UserDao {
	public User getuserDetails(String username);

	public boolean addUser(User user);

	public List<String> getAllUserNames();

	public List<String> getAllOtherNames(String name);

	public List<ChatMessage> getMessages(String me, String from);

	public boolean saveMessages(UserMessages um);

	public ServerLocation getLastLocationByName(String userName);

	public void setLastLocationByName(String userName,ServerLocation loc);
}
