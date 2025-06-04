package ads.dacha.services.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ads.dacha.services.models.Advert;
import ads.dacha.services.models.AdvertDto;
import ads.dacha.services.models.AdvertRepository;
import jakarta.validation.Valid;


@Controller
public class CatalogController {

    private final AdvertRepository AdvertRepo;

    @Autowired
    public CatalogController(AdvertRepository repo){
        AdvertRepo = repo;
    }
    @GetMapping("/")
    public String showHomePage(Model model){
        List<Advert>ads = AdvertRepo.findAll();
        model.addAttribute("ads", ads);
        return "catalog";
    }        

    @GetMapping("/addadvert")
    public String showAddAdvertForm(Model model) {
        model.addAttribute("advertDto", new AdvertDto()); // Пустой DTO для формы
        return "addadvert";
    }

    @PostMapping("/addadvert")
    public String addNewAdvert(
            @Valid @ModelAttribute("advertDto") AdvertDto advertDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            // Возвращаем форму с ошибками
            return "addadvert";
        }

        // Конвертируем DTO в сущность Advert
        Advert advert = new Advert();
        advert.setAdTitle(advertDto.getAdTitle());
        advert.setAdDescription(advertDto.getAdDescription());
        advert.setAdPrice(advertDto.getAdPrice());

        AdvertRepo.save(advert);
        return "redirect:/"; // Перенаправляем в каталог
    }
    
    //public String AddNewAdvert(Advert advert) {
        //AdvertRepo.save(advert);
        //return "catalog";
          
    

    @GetMapping("/card/{id}")
    public String getCard(@PathVariable long id, Model model) {
        Advert adv = AdvertRepo.findById(id).get();
        model.addAttribute("dacha", adv);
        return "card";
    }




}
