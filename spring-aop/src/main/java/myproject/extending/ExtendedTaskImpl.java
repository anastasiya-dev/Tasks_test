package myproject.extending;

public class ExtendedTaskImpl implements DoExtendedTask {
    @Override
    public void doExtraJob() {
        System.out.println("Doing extended job");
    }
}
