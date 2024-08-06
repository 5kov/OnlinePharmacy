package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByTitleEnContainingIgnoreCaseOrTitleBgContainingIgnoreCase(String title, String title2, Pageable pageable);

    Optional<News> findByTitleEn(String title);
    Optional<News> findByTitleBg(String title);
}
