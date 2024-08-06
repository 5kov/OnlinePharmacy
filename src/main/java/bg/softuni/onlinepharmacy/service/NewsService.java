package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.model.dto.AddNewsDTO;
import bg.softuni.onlinepharmacy.model.entity.News;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.NewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<News> getAllNews(int page, int size) {
        return newsRepository.findAll(PageRequest.of(page, size));
    }

    public String createNews(AddNewsDTO news) {
        Optional<News> user = newsRepository.findByTitleEn(news.getTitleEn());
        if (user.isPresent()) {
            return "titleEnExists";
        }
        user = newsRepository.findByTitleBg(news.getTitleBg());
        if (user.isPresent()) {
            return "titleBgExists";
        }
        News mapped = modelMapper.map(news, News.class);
        newsRepository.save(mapped);
        return "success";
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public Page<News> searchNewsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByTitleEnContainingIgnoreCaseOrTitleBgContainingIgnoreCase(title, title, pageable);
    }
}
