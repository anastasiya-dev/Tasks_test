package by.it.academy.service;

import by.it.academy.pojo.Recipient;
import by.it.academy.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipientService {

    @Autowired
    GenericDao userDao;

    public List<String> search(String searchStr) {
        return (List<String>)userDao.findAll(searchStr)
                .stream()
                .map(o -> ((Recipient) o).getId() + " " + ((Recipient) o).getMobilePhone() + " " + ((Recipient) o).getEmailAddress())
                .collect(Collectors.toList());
    }

    public boolean createNewRecipient(Recipient recipient) {
        if (userDao.find(recipient.getUserId())!= null) {
            return false;
        }
        userDao.create(recipient);
        return true;
    }

    public List<Recipient> getAll() {
        return userDao.findAll("");
    }

    public Recipient get(String id) {
        return (Recipient) userDao.read(Recipient.class, id);
    }

    public void update(Recipient recipient) {
        userDao.update(recipient);
    }
}