package ads.dacha.services.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/auth")
    public String Auth() {
        return "auth";
    }

    @GetMapping("/reg")
    public String Reg() {
        return "reg";
    }

}
