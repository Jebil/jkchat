package com.jkchat.service;

import java.util.List;

import com.jkchat.models.ChatMessage;
import com.jkchat.models.User;
import com.jkchat.models.UserMessages;

/**
 * @author Jebil Kuruvila
 *
 */
public interface UserService {
	public User getUserDetails(String userName);

	public boolean addUser(User user);

	public List<String> getAllUserNames();

	public List<String> getAllOtherNames(String name);

	public List<String> getOnlineNames();

	public boolean putMessage(String to, ChatMessage cm);

	public List<ChatMessage> getMessages(String to, String from);

	public List<ChatMessage> getMessagesFromDB(String me, String From);

	public boolean saveMessagesToDB(UserMessages um);

}
