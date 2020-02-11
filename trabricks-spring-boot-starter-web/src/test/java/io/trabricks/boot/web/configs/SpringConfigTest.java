package io.trabricks.boot.web.configs;

import io.trabricks.boot.web.configs.SpringConfigTest.AbstractConfig.Greeting;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

public class SpringConfigTest {

  @Test
  public void test() {
    final AnnotationConfigApplicationContext ctx =
        new AnnotationConfigApplicationContext(MyConfig.class, MyConfig2.class, RestTemplateAutoConfiguration.class);

    ctx.getBeansOfType(AtomicInteger.class).entrySet().stream().forEach(
        b -> System.out
            .println(b.getKey() + " : " + b.getValue() + " (" + b.getValue().hashCode() + ")")
    );

    ctx.getBeansOfType(Greeting.class).entrySet().stream().forEach(
        b -> System.out
            .println(b.getKey() + " : " + ((Greeting) b.getValue()).getHello().getName() + " (" + b
                .getValue().hashCode() + ")")
    );

    MyConfig myConfig = new MyConfig();
    myConfig.test();
  }

  @Configuration
  @AutoConfigureBefore(MyConfig.class)
  public static class MyConfig2 {

    public static class Hello2 extends Hello {

      public Hello2(String name) {
        super(name);
      }


    }

    @Bean
    public Hello hello2() {
      return new Hello2("슈퍼맨");
    }

    @Bean
    public Batman batman() {
      return new Batman("배트맨");
    }

    @Getter
    @RequiredArgsConstructor
    public static class Batman {

      private final String name;
    }

    @Bean
    @Autowired
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
      return builder.build();
    }
  }

  @Configuration
  public static class MyConfig extends AbstractConfig {

    @Override
    public void test() {
      super.test();
      System.out.println(2);
    }
  }

  public static class AbstractConfig {

    @Autowired
    private Hello hello;

    public void test() {
      System.out.println("1");
    }

    @Bean
    @Autowired
    public Greeting greeting() {
      return new Greeting(hello);
    }

    @Bean
    @ConditionalOnMissingBean
    public Hello hello() {
      return new Hello("엄연구");
    }

    @Bean
    public AtomicInteger myBean() {
      return new AtomicInteger(10);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Greeting {

      private final Hello hello;
    }

  }
  @Getter
  @RequiredArgsConstructor
  public static class Hello {

    private final String name;
  }

}
