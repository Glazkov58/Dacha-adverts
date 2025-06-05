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
        advert.setAdCategory(advertDto.getAdCategory());
        
        // Местоположение
        advert.setAdRegion(advertDto.getAdRegion());
        advert.setAdDistrict(advertDto.getAdDistrict());
        advert.setAdCity(advertDto.getAdCity());
        advert.setAdAddress(advertDto.getAdAddress());
        
        // Цена и площадь
        advert.setAdPrice(advertDto.getAdPrice());
        advert.setAdArea(advertDto.getAdArea());
        
        // Особенности (конвертируем 0/1 в boolean)
        advert.setAdElectricity(advertDto.getAdElectricity() == 1);
        advert.setAdWater(advertDto.getAdWater() == 1);
        advert.setAdGas(advertDto.getAdGas() == 1);
        advert.setAdSewerage(advertDto.getAdSewerage() == 1);
        advert.setAdSecurity(advertDto.getAdSecurity() == 1);
        advert.setAdFruitTrees(advertDto.getAdFruitTrees() == 1);
        advert.setAdBathhouse(advertDto.getAdBathhouse() == 1);
        advert.setAdGarage(advertDto.getAdGarage() == 1);
        advert.setAdFence(advertDto.getAdFence() == 1);
        
        // Контактная информация
        advert.setContactName(advertDto.getContactName());
        advert.setContactPhone(advertDto.getContactPhone());
        advert.setContactEmail(advertDto.getContactEmail());
        
        // Статус и просмотры
        advert.setAdActive(advertDto.getAdActive());
        advert.setAdViews(advertDto.getAdViews());

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
