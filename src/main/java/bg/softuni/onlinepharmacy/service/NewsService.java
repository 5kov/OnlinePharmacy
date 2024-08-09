package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.model.dto.AddNewsDTO;
import bg.softuni.onlinepharmacy.model.entity.News;
import org.springframework.data.domain.Page;
import java.io.UnsupportedEncodingException;


public interface NewsService {
    Page<News> getAllNews(int page, int size);

    String createNews(AddNewsDTO news) throws UnsupportedEncodingException;

    void deleteNews(Long id);

    Page<News> searchNewsByTitle(String title, int page, int size) throws UnsupportedEncodingException;
}
