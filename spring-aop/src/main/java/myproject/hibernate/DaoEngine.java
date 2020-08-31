package myproject.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Repository
public class DaoEngine implements BaseDao<TransferObject> {

    @Autowired
    SessionFactory factory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String create(TransferObject transferData) {
        Session session = factory.openSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.saveOrUpdate(transferData);
        transaction.commit();
        System.out.println("saving: " + transferData);
        System.out.println("with the hash code : " + transferData.hashCode());
        session.close();
        return transferData.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TransferObject read(Class clazz, Serializable id) {
        System.out.println("calling create");
        return factory.getCurrentSession()
                .createQuery("from TransferObject td where td.id=:id", TransferObject.class)
                .setParameter("id", id)
                .list()
                .stream()
                .findFirst()
                .orElseThrow();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int update(String toCard) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        String queryText7 = "update TransferObject td set td.toCard=:newCard where td.toCard=:toCard";
        Query query7 = session.createQuery(queryText7);
        query7.setParameter("toCard", toCard);
        query7.setParameter("newCard", "thiefCard");
        int result = query7.executeUpdate();
        tx.commit();
        session.close();
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int delete(TransferObject transferData) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        String queryText7 = "delete from TransferObject td where td.id=:id";
        Query query7 = session.createQuery(queryText7);
        query7.setParameter("id", transferData.getId());
        int result = query7.executeUpdate();
        tx.commit();
        session.close();
        return result;
    }
}
