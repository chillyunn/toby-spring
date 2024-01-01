package com.book.tobyspring.learningtest.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    @Test
    void invokeMethod() throws Exception {
        String name = "Spring";

        assertThat(name.length()).isEqualTo(6);

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name)).isEqualTo(6);

        assertThat(name.charAt(0)).isEqualTo('S');

        Method charatMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charatMethod.invoke(name, 0)).isEqualTo('S');
    }

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

    @Test
    void uppercaseTest(){
        //Hello 인터페이스를 구현한 프록시
        //Hello proxiedHello = new HelloUppercase(new HelloTarget());
        //다이내믹 프록시 이용
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),new Class[] {Hello.class},new UppercaseHandler(new HelloTarget()));
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");


    }


}
