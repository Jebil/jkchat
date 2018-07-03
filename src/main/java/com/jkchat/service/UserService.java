package com.jkchat.service;

import com.jkchat.models.ChatMessage;
import com.jkchat.models.ServerLocation;
import com.jkchat.models.User;
import com.jkchat.models.UserMessages;

import java.util.List;

/**
 * @author Jebil Kuruvila
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

    public ServerLocation getLastLocationByName(String userName);

    public ServerLocation setLastLocationByName(String userName, ServerLocation loc);

}
