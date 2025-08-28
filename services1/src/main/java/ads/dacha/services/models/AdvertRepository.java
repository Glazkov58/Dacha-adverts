package ads.dacha.services.models;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findByUser(User user);
    //List<Advert> getAds();

    List<Advert> findByUserId(Long id);

}
