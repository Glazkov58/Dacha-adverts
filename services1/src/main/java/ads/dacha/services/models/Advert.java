package ads.dacha.services.models;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adTitle;
    private String adDescription;
    private String adCategory;
    private String adRegion;
    private String adDistrict;
    private String adCity;
    private String adAddress;
    private boolean adElectricity;
    private boolean adWater;
    private boolean adGas;
    private boolean adSewerage;
    private boolean adSecurity;
    private boolean adFruitTrees;
    private boolean adBathhouse;
    private boolean adGarage;
    private boolean adFence;
    private String adOtherFeatures;
    private double adPrice;
    private int adArea;
    private int adViews;
    private boolean adActive;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    
    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getAdCategory() {
        return adCategory;
    }

    public void setAdCategory(String adCategory) {
        this.adCategory = adCategory;
    }

    public String getAdRegion() {
        return adRegion;
    }

    public void setAdRegion(String adRegion) {
        this.adRegion = adRegion;
    }

    public String getAdDistrict() {
        return adDistrict;
    }

    public void setAdDistrict(String adDistrict) {
        this.adDistrict = adDistrict;
    }

    public String getAdCity() {
        return adCity;
    }

    public void setAdCity(String adCity) {
        this.adCity = adCity;
    }

    public String getAdAddress() {
        return adAddress;
    }

    public void setAdAddress(String adAddress) {
        this.adAddress = adAddress;
    }

    public boolean isAdElectricity() {
        return adElectricity;
    }

    public void setAdElectricity(boolean adElectricity) {
        this.adElectricity = adElectricity;
    }

    public boolean isAdWater() {
        return adWater;
    }

    public void setAdWater(boolean adWater) {
        this.adWater = adWater;
    }

    public boolean isAdGas() {
        return adGas;
    }

    public void setAdGas(boolean adGas) {
        this.adGas = adGas;
    }

    public boolean isAdSewerage() {
        return adSewerage;
    }

    public void setAdSewerage(boolean adSewerage) {
        this.adSewerage = adSewerage;
    }

    public boolean isAdSecurity() {
        return adSecurity;
    }

    public void setAdSecurity(boolean adSecurity) {
        this.adSecurity = adSecurity;
    }

    public boolean isAdFruitTrees() {
        return adFruitTrees;
    }

    public void setAdFruitTrees(boolean adFruitTrees) {
        this.adFruitTrees = adFruitTrees;
    }

    public boolean isAdBathhouse() {
        return adBathhouse;
    }

    public void setAdBathhouse(boolean adBathhouse) {
        this.adBathhouse = adBathhouse;
    }

    public boolean isAdGarage() {
        return adGarage;
    }

    public void setAdGarage(boolean adGarage) {
        this.adGarage = adGarage;
    }

    public boolean isAdFence() {
        return adFence;
    }

    public void setAdFence(boolean adFence) {
        this.adFence = adFence;
    }

    public String getAdOtherFeatures() {
        return adOtherFeatures;
    }

    public void setAdOtherFeatures(String adOtherFeatures) {
        this.adOtherFeatures = adOtherFeatures;
    }

    public double getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(double adPrice) {
        this.adPrice = adPrice;
    }

    public int getAdArea() {
        return adArea;
    }

    public void setAdArea(int adArea) {
        this.adArea = adArea;
    }

    public int getAdViews() {
        return adViews;
    }

    public void setAdViews(int adViews) {
        this.adViews = adViews;
    }

    public boolean isAdActive() {
        return adActive;
    }

    public void setAdActive(boolean adActive) {
        this.adActive = adActive;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Long getId(){ 
        return id;
    }

    public void setId(Long id) { 
        this.id = id;
    }
    
    public void addPhotoPath(String path){
        Photo photo = new Photo();
        photo.setPath(path);
        photo.setAdvert(this);
        photos.add(photo);
    }
    public String getMainPhoto() {
        if (photos == null || photos.size() == 0) {
            return "";
        }
        return photos.get(0).getPath();
    }

    public List<Photo> getPhotos() {
        return photos;
    }



}
