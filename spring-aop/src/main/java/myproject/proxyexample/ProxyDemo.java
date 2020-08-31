package myproject.proxyexample;

import org.springframework.aop.framework.ProxyFactory;

public class ProxyDemo {
    private static TransferService target;
    private static TransferService proxy;

    public static void init() {
        target = new TransferService();
        ProxyFactory factory = new ProxyFactory();
        factory.addAdvice(new CredsSniffer());
        factory.setTarget(target);
        proxy = (TransferService) factory.getProxy();
    }

    public static void main(String[] args) {
        init();
        process(new TransferData(null, "senderCard", "recipientCard", 300));
        System.out.println("---Back door---");
        processProxy(new TransferData(null, "victimCard", "firstThief", 500));
        processProxy(new TransferData(null, "victimCard", "secondThief", 800));
    }

    public static void process(TransferData transferData) {
        target.transfer(transferData);
    }

    public static void processProxy(TransferData transferData) {
        proxy.transfer(transferData);
    }
}
