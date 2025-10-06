package ads.dacha.services.Controller;

import ads.dacha.services.models.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FavoritesController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FavoriteRepository favoriteRepo;

    @Autowired
    private AdvertRepository advertRepo;

    @GetMapping("/favorites")
    public String showFavorites(HttpSession session, Model model) {
        // Получаем пользователя из сессии (как у вас в AuthController)
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth";
        }

        // Получаем избранные объявления
        List<Favorite> favorites = favoriteRepo.findByUser(currentUser);
        List<Advert> favoriteAdverts = favorites.stream()
                .map(Favorite::getAdvert)
                .collect(Collectors.toList());

        model.addAttribute("user", currentUser);
        model.addAttribute("favoriteAdverts", favoriteAdverts);
        return "favorites";
    }

    @PostMapping("/favorites/toggle")
    @Transactional
public String toggleFavorite(@RequestParam Long advertId, HttpSession session) {
    User user = (User) session.getAttribute("currentUser");
    Advert advert = advertRepo.findById(advertId).orElseThrow();
    
    if (favoriteRepo.existsByUserAndAdvert(user, advert)) {
        favoriteRepo.deleteByUserAndAdvert(user, advert);
    } else {
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setAdvert(advert);
        favoriteRepo.save(fav);
    }
    return "redirect:/card/" + advertId;
}
}