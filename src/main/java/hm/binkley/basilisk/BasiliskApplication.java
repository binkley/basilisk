package hm.binkley.basilisk;

import lombok.Generated;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class BasiliskApplication {
    @Generated // Lie to JaCoCo
    public static void main(final String... args) {
        run(BasiliskApplication.class, args);
    }
}
