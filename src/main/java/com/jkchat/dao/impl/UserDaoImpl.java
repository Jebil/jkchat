package com.jkchat.dao.impl;

import com.jkchat.dao.UserDao;
import com.jkchat.models.ChatMessage;
import com.jkchat.models.ServerLocation;
import com.jkchat.models.User;
import com.jkchat.models.UserMessages;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jebil Kuruvila
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao, UserDetailsService {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
    @Autowired
    SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     *
     * @see com.jkchat.dao.UserDao#getuserDetails(java.lang.String)
     */
    public User getuserDetails(String username) {
        logger.debug("inside getUser ");
        Session session = sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(User.class);
        cr.add(Restrictions.eq("name", username));
        User foundUser = (User) cr.uniqueResult();
        logger.debug("end getUser ");
        return foundUser;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jkchat.dao.UserDao#addUser(com.jkchat.models.User)
     */
    public boolean addUser(User user) {
        logger.debug("inside addUser ");
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        logger.debug("end addUser ");
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jkchat.dao.UserDao#getAllUserNames()
     */
    @Override
    public List<String> getAllUserNames() {
        Session session = sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(User.class);
        cr.setProjection(Projections.property("name"));
        return cr.list();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jkchat.dao.UserDao#getAllOtherNames(java.lang.String)
     */
    @Override
    public List<String> getAllOtherNames(String name) {
        Session session = sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(User.class);
        cr.setProjection(Projections.property("name"));
        cr.add(Restrictions.ne("name", name));
        return cr.list();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jkchat.dao.UserDao#getMessages(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<ChatMessage> getMessages(String me, String from) {
        Session session = sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(UserMessages.class);
        cr.createAlias("cm", "cm");
        cr.setProjection(Projections.projectionList()
                .add(Projections.property("cm.id"), "id")
                .add(Projections.property("cm.fromUser"), "fromUser")
                .add(Projections.property("cm.message"), "message"));
        cr.add(Restrictions.or(
                Restrictions.and(Restrictions.eq("uName", from),
                        Restrictions.eq("cm.fromUser", me)),
                Restrictions.and(Restrictions.eq("uName", me),
                        Restrictions.eq("cm.fromUser", from))));
        cr.addOrder(Order.desc("id"));
        cr.setMaxResults(5);
        return cr.list();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jkchat.dao.UserDao#saveMessages(com.jkchat.models.UserMessages)
     */
    @Override
    public boolean saveMessages(UserMessages um) {
        logger.debug("inside saveMessages ");
        Session session = sessionFactory.getCurrentSession();
        session.persist(um);
        logger.debug("end saveMessages ");
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return getuserDetails(username);
    }

    @Override
    public ServerLocation getLastLocationByName(String userName) {
        logger.debug("inside getLastLocationByName");
        Session session = sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(User.class);
        cr.add(Restrictions.eq("name", userName));
        cr.setProjection(Projections.property("lastLocation"));
        return (ServerLocation) cr.uniqueResult();
    }

    @Override
    public void setLastLocationByName(String userName, ServerLocation loc) {
        User user = getuserDetails(userName);
        user.setLastLocation(loc);
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }
}
