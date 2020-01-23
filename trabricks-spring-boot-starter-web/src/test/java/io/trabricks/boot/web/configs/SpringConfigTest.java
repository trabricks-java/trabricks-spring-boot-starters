package io.trabricks.boot.web.configs;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class SpringConfigTest {

  @Test
  public void test() {
    final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
        MyConfig.class);
    ctx.getBeansOfType(AtomicInteger.class).entrySet().stream().forEach(
        b -> System.out
            .println(b.getKey() + " : " + b.getValue() + " (" + b.getValue().hashCode() + ")"));
  }

  @Configuration
  public static class MyConfig extends AbstractConfig {

    // @Bean(name = "anotherName")
    @Override
    public AtomicInteger myBean() {
      return new AtomicInteger(5);
    }
  }

  public static abstract class AbstractConfig {

    @Bean
    public AtomicInteger myBean() {
      return new AtomicInteger(10);
    }
  }

}
