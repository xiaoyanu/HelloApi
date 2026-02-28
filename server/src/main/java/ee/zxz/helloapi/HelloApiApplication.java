package ee.zxz.helloapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApiApplication.class, args);
        System.out.println(
                """
                        
                          _    _        _  _            _     _____   _____\s
                         | |  | |      | || |          / \\   |  __ \\ |_   _|
                         | |__| |  ___ | || |  ___    / _ \\  | |__) |  | | \s
                         |  __  | / _ \\| || | / _ \\  / ___ \\ |  ___/   | | \s
                         | |  | ||  __/| || || (_) |/ /   \\ \\| |      _| |_\s
                         |_|  |_| \\___||_||_| \\___//_/     \\_\\_|     |_____|
                        """);
    }

}
