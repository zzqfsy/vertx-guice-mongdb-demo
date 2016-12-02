package com.zzqfsy;

import com.zzqfsy.vertx.HttpServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

/**
 * Created by john on 16-6-16.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void deployVerticle() {
        Vertx.vertx().deployVerticle(new HttpServerVerticle());
    }

  /*  private static final String RX_EXAMPLES_DIR = "stone-manager";
    private static final String RX_EXAMPLES_JAVA_DIR = RX_EXAMPLES_DIR + "/src/main/java/";

    public static void main(String... args) {
        runJavaExample(RX_EXAMPLES_JAVA_DIR, HttpServerVerticle.class, true);
    }

    private static void runJavaExample(String prefix, Class clazz, boolean clustered) {
        String exampleDir = prefix + clazz.getPackage().getName().replace(".", "/");
        runExample(exampleDir, clazz.getName(), clustered);
    }


    private static void runExample(String exampleDir, String verticleID, boolean clustered) {
        System.setProperty("vertx.cwd", exampleDir);
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(verticleID);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        if (clustered) {
            Vertx.clusteredVertx(new VertxOptions().setClustered(true), res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx();
            runner.accept(vertx);
        }
    }*/
}
