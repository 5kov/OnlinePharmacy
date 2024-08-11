package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.dto.AddNewsDTO;
import bg.softuni.onlinepharmacy.model.entity.News;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NewsServiceImplTest {

    @Mock
    private RestClient newsRestClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News());

        when(newsRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/news")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(newsList);

        Page<News> result = newsService.getAllNews(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(newsRestClient, times(1)).get();
    }

    @Test
    public void testCreateNews_TitleExists() throws UnsupportedEncodingException {
        AddNewsDTO newsDTO = new AddNewsDTO();
        newsDTO.setTitleEn("Test Title");
        newsDTO.setTitleBg("Тестово Заглавие");

        List<News> newsList = new ArrayList<>();
        newsList.add(new News());

        when(newsRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/news/titleCheck/Test%20Title")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(newsList);

        String result = newsService.createNews(newsDTO);

        assertEquals("titleExist", result);
        verify(newsRestClient, times(1)).get();
    }

    @Test
    public void testCreateNews_Success() throws UnsupportedEncodingException {
        AddNewsDTO newsDTO = new AddNewsDTO();
        newsDTO.setTitleEn("Test Title1");
        newsDTO.setTitleBg("Тестово Заглавие1");

        List<News> newsList = new ArrayList<>();
        newsList.add(new News());

        when(newsRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/news/titleCheck/Test%20Title")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(newsList);

        when(requestHeadersUriSpec.uri("/news/titleCheck/%D0%A2%D0%B5%D1%81%D1%82%D0%BE%D0%B2%D0%BE%20%D0%97%D0%B0%D0%B3%D0%BB%D0%B0%D0%B2%D0%B8%D0%B5")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(newsList);

        when(newsRestClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/news")).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(AddNewsDTO.class))).thenReturn(requestBodySpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        String result = newsService.createNews(newsDTO);

        assertEquals("success", result);
        verify(newsRestClient, times(2)).get();
        verify(newsRestClient, times(1)).post();
    }

    @Test
    public void testDeleteNews() {
        Long newsId = 1L;

        when(newsRestClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/news/" + newsId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(null);

        newsService.deleteNews(newsId);

        verify(newsRestClient, times(1)).delete();
    }

    @Test
    public void testSearchNewsByTitle() throws UnsupportedEncodingException {
        String title = "Test Title";
        int page = 0;
        int size = 10;

        List<News> newsList = new ArrayList<>();
        newsList.add(new News());

        when(newsRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/news/title/Test%20Title")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(newsList);

        Page<News> result = newsService.searchNewsByTitle(title, page, size);

        assertEquals(1, result.getTotalElements());
        verify(newsRestClient, times(1)).get();
    }
}
