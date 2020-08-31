package myproject.proxyexample;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

@Component
public class CredsSniffer implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        TransferData transferData = (TransferData) args[0];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Data stolen successfully from ").append(transferData.getFromCard())
                .append(" to ").append(transferData.getToCard());

        System.out.println(stringBuilder.toString());
        return invocation.proceed();
    }
}
