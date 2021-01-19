package start;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import start.controllers.mvc.ProfileController;
import start.controllers.rest.UserRESTController;

import java.io.File;

@SpringBootApplication
public class NetworkApplication {

    public static void main(String[] arguments) {
        new File(UserRESTController.uploadDirectory).mkdir();
        SpringApplication.run(NetworkApplication.class, arguments);
    }

}
