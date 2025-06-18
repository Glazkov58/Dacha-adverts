package ads.dacha.services.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ads.dacha.services.models.Advert;
import ads.dacha.services.models.AdvertRepository;
import ads.dacha.services.models.User;
import ads.dacha.services.models.UserRepository;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(AdvertRepository advertRepository, UserRepository userRepository) {
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showUserProfile(Model model, Principal principal) {
        // Получаем текущего пользователя
        User user = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Получаем объявления пользователя
        List<Advert> userAdverts = advertRepository.findByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("userAdverts", userAdverts);
        
        return "profile";
    }
    
}
