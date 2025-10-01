package ads.dacha.services.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ads.dacha.services.models.Advert;
import ads.dacha.services.models.AdvertDto;
import ads.dacha.services.models.AdvertRepository;
import ads.dacha.services.models.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class CatalogController {

    private final AdvertRepository AdvertRepo;

    @Value("${upload.path}") // путь для загрузки файлов из application.properties
    private String uploadPath;

    @Autowired
    public CatalogController(AdvertRepository repo){
        AdvertRepo = repo;
    }
    private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);
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
            //@RequestParam("photoFiles") MultipartFile photoFiles
    ) throws IOException {        
        /*
        if (bindingResult.hasErrors()) {
            // Возвращаем форму с ошибками
            return "addadvert";
        }
        */
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

        /*if (photoFiles != null && !photoFiles.isEmpty()) {
            String fileName = savePhoto(photoFiles);
            advert.setPhotoPath(fileName);
        }*/

        Advert saveAdvert = AdvertRepo.save(advert);

        String advertFolder = "advert_" + saveAdvert.getId();
        Path advertPath = Paths.get(uploadPath, advertFolder);
        Files.createDirectories(advertPath);

        if (advertDto.getPhotoFiles() != null) {
            for (MultipartFile file : advertDto.getPhotoFiles()) {
                if (!file.isEmpty()) {
                    String fileName = savePhoto(file, advertPath.toString());
                    saveAdvert.addPhotoPath(advertFolder + "/" + fileName);
                }
            }
            AdvertRepo.save(saveAdvert);
        }

        return "redirect:/"; // Перенаправляем в каталог
    }


    

    private String savePhoto(MultipartFile file, String folderPath) throws IOException {
        String fileExtension = file.getOriginalFilename()
            .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + fileExtension;
        // Создаем директорию, если ее нет
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
            
        // Генерируем уникальное имя файла
        /*String fileExtension = getFileExtension(file.getOriginalFilename());
        String uuidFile = UUID.randomUUID().toString();
        String fileName = uuidFile + "." + fileExtension;

        Path filePath = uploadDir.resolve(fileName);
        file.transferTo(filePath);*/
        Path targetPath = Paths.get(folderPath, fileName);
        file.transferTo(targetPath);

        return fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
            
            // Сохраняем файл
            /*photoFiles.transferTo(Paths.get(uploadPath + "/" + resultFilename));
            
            // Сохраняем путь к файлу в базе данных
            advert.setPhotoPath(resultFilename);
        } else {
            System.out.println("No photo");
        }

        AdvertRepo.save(advert);
        return "redirect:/"; // Перенаправляем в каталог
    }
    
    //public String AddNewAdvert(Advert advert) {
        //AdvertRepo.save(advert);
        //return "catalog";*/
          
    

    @GetMapping("/card/{id}")
    public String getCard(@PathVariable long id, Model model, HttpSession session) {
        Advert adv = AdvertRepo.findById(id).orElseThrow();
        /*if (adv.getPhotoPaths() != null && !adv.getPhotoPaths().isEmpty()) {
            List<String> validPhotoPaths = new ArrayList<>();

            for (String photoPath : adv.getPhotoPaths()) {
            Path fullPath = Paths.get(uploadPath, photoPath);
            if (Files.exists(fullPath)) {
                validPhotoPaths.add(photoPath);
            } else {
                logger.warn("Фото не найдено: {}", fullPath);
            }
        }
        adv.setPhotoPaths(validPhotoPaths);*/
        /*Path photoPath = Paths.get(uploadPath, adv.getPhotoPaths());
        if (!Files.exists(photoPath)) {
            System.out.println("Файл не найден: " + photoPath.toAbsolutePath());
            adv.setPhotoPaths(null); // Очищаем путь, если файл не существует
        }*/
        
        User user = (User)session.getAttribute("currentUser");
        model.addAttribute("currentUser", user);
        model.addAttribute("dacha", adv);
        return "card";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model){
        System.out.println("Запрос: " + query);
        List<Advert> adverts = AdvertRepo.findByTitle(query);
        System.out.println("Найдено:" + adverts.size());

        model.addAttribute("ads", adverts);
        return "catalog";

    }
    @GetMapping("/filter")
    public String filter(@RequestParam(required = false) String category,
        @RequestParam(required = false) String region, Model model) {
            if (category.isEmpty()) {
                category = null;
            }
            if (region.isEmpty()) {
                region = null;
            }
        List<Advert> adverts = AdvertRepo.findByFilter(category,region);
        model.addAttribute("ads", adverts);
        return "catalog";
    }
    




}
