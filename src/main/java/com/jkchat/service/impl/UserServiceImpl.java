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
import com.jkchat.models.ServerLocation;
import com.jkchat.models.User;
import com.jkchat.models.UserMessages;
import com.jkchat.service.UserService;

/**
 * @author Jebil Kuruvila
 *
 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#getUserDetails(java.lang.String)
	 */
	public User getUserDetails(String userName) {
		logger.debug("inside getUser ");
		return userDao.getuserDetails(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#addUser(com.jkchat.models.User)
	 */
	public boolean addUser(User user) {
		logger.debug("inside addUser ");
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		return userDao.addUser(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#getAllUserNames()
	 */
	@Override
	public List<String> getAllUserNames() {
		logger.debug("inside getAllUserNames ");
		return userDao.getAllUserNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#getAllOtherNames(java.lang.String)
	 */
	@Override
	public List<String> getAllOtherNames(String name) {
		logger.debug("inside getAllOtherNames ");
		return userDao.getAllOtherNames(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#getOnlineNames()
	 */
	@Override
	public List<String> getOnlineNames() {
		logger.debug("inside getOnlineNames ");
		List<Object> principals = sessionRegistry.getAllPrincipals();
		List<String> usersOnlineList = new ArrayList<String>();
		for (Object principal : principals) {
			if (principal.getClass() == User.class) {
				usersOnlineList.add(((User) principal).getUsername());
			} else {
				usersOnlineList.add((String) principal);
			}
		}
		logger.debug("End of getOnlineNames ");
		return usersOnlineList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#putMessage(java.lang.String,
	 * com.jkchat.models.ChatMessage)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#getMessages(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<ChatMessage> getMessages(String to, String from) {
		List<ChatMessage> list = map.get(to);
		map.remove(to);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#getMessagesFromDB(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<ChatMessage> getMessagesFromDB(String me, String from) {
		List<ChatMessage> list = userDao.getMessages(me, from.toLowerCase());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jkchat.service.UserService#saveMessagesToDB(com.jkchat.models.
	 * UserMessages)
	 */
	@Override
	public boolean saveMessagesToDB(UserMessages um) {
		userDao.saveMessages(um);
		return true;
	}

	@Override
	public ServerLocation getLastLocationByName(String userName) {
		return userDao.getLastLocationByName(userName);
	}

	@Override
	public ServerLocation setLastLocationByName(String userName,
			ServerLocation loc) {
		userDao.setLastLocationByName(userName, loc);
		return null;
	}
}
