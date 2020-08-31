package myproject.hibernate;

import myproject.ApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HibMain {

    private static int counter = 0;

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        BaseDao daoEngine = (BaseDao) context.getBean("daoEngine");

        TransferObject transferData1 = (TransferObject) context.getBean("transferObject");
        fillInData(transferData1);
        daoEngine.create(transferData1);
        TransferObject transferData2 = (TransferObject) context.getBean("transferObject");
        fillInData(transferData2);
        daoEngine.create(transferData2);
        TransferObject transferData3 = (TransferObject) context.getBean("transferObject");
        fillInData(transferData3);
        daoEngine.create(transferData3);
        daoEngine.update(transferData3.getToCard());

        TransferObject transferObject3Read = (TransferObject) daoEngine.read(TransferObject.class, transferData3.getId());
        System.out.println("read object: " + transferObject3Read);

        TransferObject transferData4 = (TransferObject) context.getBean("transferObject");
        fillInData(transferData4);
        daoEngine.create(transferData4);
        daoEngine.delete(transferData4);

        try {
            TransferObject transferObject4Deleted = (TransferObject) daoEngine.read(TransferObject.class, transferData3.getId());
            System.out.println("delete object: " + transferObject4Deleted);
        } catch (Exception e) {
            System.out.println("deleted");
        }


    }

    private static void fillInData(TransferObject transferData) {
        counter++;
        transferData.setId(String.valueOf(counter));
        transferData.setFromCard("fromCard" + counter);
        transferData.setToCard("toCard" + counter);
        transferData.setAmount(100 * counter);
    }

}
