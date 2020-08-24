package by.it.academy;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    public Recipient find(String userId) {
        //POJO hibernate session load...
        //мы не создаем объекты, а берем их из спринг контекста
        // (ctrl+shift+f - видим, что через конструктутор нью созданы только тестовые данные)
        Recipient recipient = new Recipient();
        recipient.setEmailAddress("user@gmail.com");
        recipient.setMobilePhone("+375292658195");
        return recipient;
    }
}
