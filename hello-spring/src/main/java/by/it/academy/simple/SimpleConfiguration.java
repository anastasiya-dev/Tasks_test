package by.it.academy.simple;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleConfiguration {

    @Bean
    public A a1() {
        return new A("A1");
    }

    @Bean
    public A a2() {
        return new A("A2");
    }

    @Bean
    public B b1(@Qualifier("a2") A a) {
        return new B(a);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SimpleConfiguration.class);
        B b1 = (B) context.getBean("b1");
        System.out.println(b1.getA().getA());
    }
}

class A {
    private String a;

    public String getA() {
        return a;
    }

    public A(String a) {
        this.a = a;
    }
}

class B {

    private A a;

    public B(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
