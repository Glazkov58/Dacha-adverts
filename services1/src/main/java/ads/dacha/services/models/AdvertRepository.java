package ads.dacha.services.models;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findByUser(User user);
    //List<Advert> getAds();

    List<Advert> findByUserId(Long id);

    @Query("SELECT a FROM Advert a WHERE a.adTitle LIKE CONCAT('%', :query, '%')")
    List<Advert> findByTitle(@Param("query") String query);

    @Query("SELECT a FROM Advert a WHERE (:category IS NULL OR a.adCategory = :category) AND(:region IS NULL OR a.adDistrict = :region)")
    List<Advert> findByFilter(@Param("category") String category, @Param("region") String region);

}
