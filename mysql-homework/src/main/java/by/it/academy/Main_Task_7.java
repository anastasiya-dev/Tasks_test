package by.it.academy;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main_Task_7 {
    private static Logger log = Logger.getLogger(Main_Task_7.class.getName());

    public static void main(String[] args) {
        try {
            Dao dao = DaoFactory.getDaoImpl("mysql_homework");

            //info for expenses
            List<Integer> numsE = List.of(1, 2, 3, 4, 5, 6);
            List<Date> datesE = List.of(java.sql.Date.valueOf("2011-05-10"),
                    Date.valueOf("2011-05-10"), Date.valueOf("2011-05-11"), Date.valueOf("2011-05-11"),
                    Date.valueOf("2011-07-07"), Date.valueOf("2020-07-15"));
            List<Integer> receiversE = List.of(1, 2, 3, 2, 3, 1);
            List<Double> valuesE = List.of(94200.5, 10000.0, 12950.0, 20100.0, 20100.0, 0.4);

            //info for receivers
            List<Integer> numsR = List.of(1, 2, 3);
            List<String> namesR = List.of("Solo", "Korona", "MTS");

            //adding expenses
            for (int i = 0; i < numsE.size(); i++) {
                DtoExpense dtoExpense = new DtoExpense();
                dtoExpense.setNum(numsE.get(i));
                dtoExpense.setPaydate(datesE.get(i));
                dtoExpense.setReceiver(receiversE.get(i));
                dtoExpense.setValue(valuesE.get(i));
                log.info("Adding expense: " + dtoExpense);
                dao.addExpense(dtoExpense);
            }

            //adding receivers
            for (int i = 0; i < numsR.size(); i++) {
                DtoReceiver dtoReceiver = new DtoReceiver();
                dtoReceiver.setNum(numsR.get(i));
                dtoReceiver.setName(namesR.get(i));
                log.info("Adding receiver: " + dtoReceiver);
                dao.addReceiver(dtoReceiver);
            }

            //getting an expense by number
            int expenseN = 5;
            log.info("Getting an expense by number: " + dao.getExpense(expenseN));

            // getting all expenses
            List<DtoExpense> expensesFromDb = dao.getExpenses();
            log.info("Getting all the expenses from the database:");
            for (DtoExpense dtoExpense : expensesFromDb) {
                log.info(dtoExpense.toString());
            }

            //getting a receiver by number
            int receiverN = 2;
            log.info("Getting a receiver by number: " + dao.getReceiver(receiverN));

            // getting all expenses
            List<DtoReceiver> receversFromDb = dao.getReceivers();
            log.info("Getting all the receivers from the database:");
            for (DtoReceiver dtoReceiver : receversFromDb) {
                log.info(dtoReceiver.toString());
            }

        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        }
        finally {
            log.info("Finished successfully");
            System.exit(0);
        }
    }
}
