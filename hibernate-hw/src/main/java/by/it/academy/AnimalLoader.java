package by.it.academy;

import javax.persistence.EntityManager;
import java.util.Date;

public class AnimalLoader {
    public static void main(String[] args) {
        Animal animal = new Animal();
        animal.dateOfBirth = new Date();
        animal.name = "Sharik";
        animal.numberOfLegs = 4;
        animal.sex = 'M';
        EntityManager entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(animal);
        entityManager.getTransaction().commit();
        HibernateUtil.close();
    }
}
