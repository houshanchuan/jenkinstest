package hos.srm.srmbusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan(basePackages = {"hos.srm.srmbusiness"})

@SpringBootApplication
public class SrmbusinessApplication {

    public static void main(String[] args) {
        //SpringApplication.run(SrmbusinessApplication.class, args);
        ConfigurableApplicationContext run=SpringApplication.run(SrmbusinessApplication.class, args);
        System.out.println(11);
    }


}
