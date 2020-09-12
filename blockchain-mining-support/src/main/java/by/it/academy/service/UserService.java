package by.it.academy.service;

import by.it.academy.pojo.User;
import by.it.academy.repository.UserDao;
import org.hibernate.SessionFactory;

//@Service
public class UserService {

    //    @Autowired
//    @Value("#{userDao}")
    UserDao baseDao = new UserDao();

    public boolean createNewUser(SessionFactory sessionFactory, User user) {
        if (baseDao.findById(sessionFactory, user.getUserId()) != null) {
            return false;
        }
        baseDao.create(sessionFactory, user);
        return true;
    }

    public User findUserById(SessionFactory sessionFactory, String id) {
        return (User) baseDao.findById(sessionFactory, id);
    }

//    public User findUserByName(String name) {
//        return (User) baseDao.findByName(name);
//    }
//
    public User update(SessionFactory factory, User user) {
        return (User) baseDao.update(factory, user);
    }
}
