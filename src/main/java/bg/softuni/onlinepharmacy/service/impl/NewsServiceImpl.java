package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.dto.AddNewsDTO;
import bg.softuni.onlinepharmacy.model.entity.News;
import bg.softuni.onlinepharmacy.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    private Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);
    private final RestClient newsRestClient;

    public NewsServiceImpl(@Qualifier("newsRestClient") RestClient newsRestClient) {
        this.newsRestClient = newsRestClient;
    }

    @Override
    public Page<News> getAllNews(int page, int size) {
        LOGGER.info("Getting all news...");
        Pageable pageable = PageRequest.of(page, size);

        List<News> list = newsRestClient
                .get()
                .uri("/news")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});

        List<News> newList = new ArrayList<>();
        int beginning = page * size;
        int ending = beginning + size;
        if (list.size() < ending) {
            ending = list.size();
        }

        for (int i = beginning; i < ending; i++) {
            newList.add(list.get(i));
        }

        long totalElements = list.size();

        Page<News> pageNews = new PageImpl<>(newList, pageable, totalElements);
        return pageNews;
    }

    @Override
    public String createNews(AddNewsDTO news) throws UnsupportedEncodingException {
        LOGGER.info("Creating new news...");

        String encodedStringEn = news.getTitleEn().replace(" ", "%20");
        String encodedStringBg = URLEncoder.encode(news.getTitleBg()).replace("+", "%20");

        try {
            List<News> list = newsRestClient
                    .get()
                    .uri("/news/titleCheck/" + encodedStringEn)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>(){});
        } catch(Exception e) {
            try {
                List<News> list = newsRestClient
                        .get()
                        .uri("/news/titleCheck/" + encodedStringBg)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>(){});
            } catch(Exception e2) {
                    newsRestClient
                    .post()
                    .uri("/news")
                    .body(news)
                    .retrieve();
            return "success";
            }
        }
        return "titleExist";
    }

    @Override
    public void deleteNews(Long id) {
        LOGGER.info("Deleting news...");
        newsRestClient
                .delete()
                .uri("/news/" + id)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Page<News> searchNewsByTitle(String title, int page, int size) throws UnsupportedEncodingException {
        LOGGER.info("Getting specific news...");
        String encodedString = URLEncoder.encode(title, "UTF-8");
        encodedString = encodedString.replace("+", "%20");

        Pageable pageable = PageRequest.of(page, size);

        List<News> list = newsRestClient
                .get()
                .uri("/news/title/" + encodedString)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});

        List<News> newList = new ArrayList<>();
        int beginning = page * size;
        int ending = beginning + size;
        if (list.size() < ending) {
            ending = list.size();
        }

        for (int i = beginning; i < ending; i++) {
            newList.add(list.get(i));
        }

        long totalElements = list.size();

        Page<News> pageNews = new PageImpl<>(newList, pageable, totalElements);
        return pageNews;
    }
}
