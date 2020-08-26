package by.it.academy.results;

import by.it.academy.pojo.Chair;
import by.it.academy.support.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Date;

public class Task_17_19 {
    public static void main(String[] args) throws Exception {
        Chair chairInitial = new Chair();
        chairInitial.setId("1");
        chairInitial.setFurnitureClass('A');
        chairInitial.setManufacturer("Pinskdrev");
        chairInitial.setProducedDate(Date.valueOf("2020-12-15"));
        System.out.println(chairInitial.getId());
        System.out.println(chairInitial.getFurnitureClass());

        SessionFactory factory = HibernateUtil.start();
        Session session = factory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(chairInitial);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        Chair newChair = session.get(Chair.class, chairInitial.getId());
        System.out.println(newChair.toString());
        //task 19
        System.out.println(session.getIdentifier(newChair));
        HibernateUtil.finish();
    }
}
