package ads.dacha.services.Controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import ads.dacha.services.models.Advert;
import ads.dacha.services.models.AdvertRepository;
import ads.dacha.services.models.User;
import ads.dacha.services.models.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private AdvertRepository advertRepo; // Предполагая, что у вас есть репозиторий для объявлений
    
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            return "redirect:/auth";
        }
        
        // Получаем объявления пользователя
        List<Advert> userAdverts = advertRepo.findByUserId(currentUser.getId());
        
        model.addAttribute("user", currentUser);
        model.addAttribute("userAdverts", userAdverts != null ? userAdverts : new ArrayList<>());
        
        return "profile";
    }
}
