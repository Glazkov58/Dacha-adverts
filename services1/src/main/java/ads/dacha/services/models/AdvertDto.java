package ads.dacha.services.models;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AdvertDto {
    
    @NotBlank(message = "Название объявления обязательно")
    @Size(max = 100, message = "Максимум 100 символов")
    private String adTitle;
    
    @NotBlank(message = "Описание обязательно")
    @Size(max = 2000, message = "Максимум 2000 символов")
    private String adDescription;
    
    @NotBlank(message = "Категория обязательна")
    private String adCategory;
    
    @NotBlank(message = "Регион обязателен")
    private String adRegion;
    
    @NotBlank(message = "Район обязателен")
    private String adDistrict;
    
    @NotBlank(message = "Город обязателен")
    private String adCity;
    
    @NotBlank(message = "Адрес обязателен")
    private String adAddress;
    
    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    private Double adPrice;
    
    @NotNull(message = "Площадь обязательна")
    @Min(value = 1, message = "Минимум 1 сотка")
    private Integer adArea;
    
    private Integer adElectricity = 0;
    private Integer adWater = 0;
    private Integer adGas = 0;
    private Integer adSewerage = 0;
    private Integer adSecurity = 0;
    private Integer adFruitTrees = 0;
    private Integer adBathhouse = 0;
    private Integer adGarage = 0;
    private Integer adFence = 0;
    
    private String adOtherFeatures;
    
    @NotBlank(message = "Имя контакта обязательно")
    private String contactName;
    
    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$", message = "Некорректный формат телефона")
    private String contactPhone;
    
    @Email(message = "Некорректный email")
    private String contactEmail;
    
    private Boolean adActive = true;
    
    private Integer adViews = 0;

    private List<MultipartFile> photoFiles;

    public List<MultipartFile> getPhotoFiles() {
        return photoFiles;
    }

    public void setPhotoFile(List<MultipartFile> photoFiles) {
        this.photoFiles = photoFiles;
    }
}
