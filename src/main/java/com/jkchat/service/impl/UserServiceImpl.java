package com.jkchat.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jkchat.dao.UserDao;
import com.jkchat.models.ChatMessage;
import com.jkchat.models.User;
import com.jkchat.models.UserMessages;
import com.jkchat.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	private static Map<String, List<ChatMessage>> map = new Hashtable<String, List<ChatMessage>>();

	private static final Logger logger = Logger
			.getLogger(UserServiceImpl.class);

	public User getUserDetails(String userName) {
		logger.debug("inside getUser ");
		return userDao.getuserDetails(userName);
	}

	public boolean addUser(User user) {
		logger.debug("inside addUser ");
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		return userDao.addUser(user);
	}

	@Override
	public List<String> getAllUserNames() {
		logger.debug("inside getAllUserNames ");
		return userDao.getAllUserNames();
	}

	@Override
	public List<String> getAllOtherNames(String name) {
		logger.debug("inside getAllOtherNames ");
		return userDao.getAllOtherNames(name);
	}

	@Override
	public List<String> getOnlineNames() {
		logger.debug("inside getOnlineNames ");
		List<Object> principals = sessionRegistry.getAllPrincipals();
		List<String> usersOnlineList = new ArrayList<String>();
		for (Object principal : principals) {
			System.out.println("****** " + principal.toString());
			if (principal.getClass() == User.class) {
				usersOnlineList.add(((User) principal).getUsername());
			} else {
				usersOnlineList.add((String) principal);
			}
		}
		logger.debug("End of getOnlineNames ");
		return usersOnlineList;
	}

	@Override
	public boolean putMessage(String to, ChatMessage cm) {
		List<ChatMessage> list = map.get(to);
		if (list == null) {
			list = new ArrayList<ChatMessage>();
			list.add(cm);
		} else {
			list.add(cm);
		}
		map.put(to, list);
		return true;
	}

	@Override
	public List<ChatMessage> getMessages(String to, String from) {
		List<ChatMessage> list = map.get(to);
		map.remove(to);
		return list;
	}

	@Override
	public List<ChatMessage> getMessagesFromDB(String me, String from) {
		List<ChatMessage> list = userDao.getMessages(me, from.toLowerCase());
		return list;
	}

	@Override
	public boolean saveMessagesToDB(UserMessages um) {
		userDao.saveMessages(um);
		return true;
	}
}
