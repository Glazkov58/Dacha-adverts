package ads.dacha.services.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    
    boolean existsByUserAndAdvert(User user, Advert advert);
    
    void deleteByUserAndAdvert(User user, Advert advert);
}
