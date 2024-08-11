package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.dto.AddNewsDTO;
import bg.softuni.onlinepharmacy.model.entity.News;
import bg.softuni.onlinepharmacy.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsServiceImpl newsServiceImpl;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testListNews() throws Exception {
        News news = new News();
        Page<News> newsPage = new PageImpl<>(Collections.singletonList(news), PageRequest.of(0, 10), 1);

        when(newsServiceImpl.getAllNews(anyInt(), anyInt())).thenReturn(newsPage);

        mockMvc.perform(get("/news").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("news"))
                .andExpect(model().attributeExists("newsPage"))
                .andExpect(model().attribute("newsPage", newsPage));
    }

    @Test
    public void testShowAddNewsForm() throws Exception {
        mockMvc.perform(get("/news/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-add-news"));
    }

    @Test
    public void testAddNews() throws Exception {
        AddNewsDTO addNewsDTO = new AddNewsDTO();
        addNewsDTO.setTitleEn("Test News EN");
        addNewsDTO.setTitleBg("Test News BG");
        addNewsDTO.setContentEn("This is a test news content in English.");
        addNewsDTO.setContentBg("This is a test news content in Bulgarian.");

        when(newsServiceImpl.createNews(any(AddNewsDTO.class))).thenReturn("success");

        mockMvc.perform(post("/news/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("titleEn", addNewsDTO.getTitleEn())
                        .param("titleBg", addNewsDTO.getTitleBg())
                        .param("contentEn", addNewsDTO.getContentEn())
                        .param("contentBg", addNewsDTO.getContentBg()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news"));
    }

    @Test
    public void testAddNewsTitleExists() throws Exception {
        AddNewsDTO addNewsDTO = new AddNewsDTO();
        addNewsDTO.setTitleEn("Test News EN");
        addNewsDTO.setTitleBg("Test News BG");
        addNewsDTO.setContentEn("This is a test news content in English.");
        addNewsDTO.setContentBg("This is a test news content in Bulgarian.");

        when(newsServiceImpl.createNews(any(AddNewsDTO.class))).thenReturn("titleExist");

        mockMvc.perform(post("/news/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("titleEn", addNewsDTO.getTitleEn())
                        .param("titleBg", addNewsDTO.getTitleBg())
                        .param("contentEn", addNewsDTO.getContentEn())
                        .param("contentBg", addNewsDTO.getContentBg()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news/add"))
                .andExpect(flash().attributeExists("titleExist"));
    }

    @Test
    public void testAddNewsBindingErrors() throws Exception {
        mockMvc.perform(post("/news/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("titleEn", "")
                        .param("titleBg", "")
                        .param("contentEn", "")
                        .param("contentBg", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news/add"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.newsData"));
    }

    @Test
    public void testShowDeleteNewsPage() throws Exception {
        News news = new News();
        Page<News> newsPage = new PageImpl<>(Collections.singletonList(news), PageRequest.of(0, 10), 1);

        when(newsServiceImpl.getAllNews(anyInt(), anyInt())).thenReturn(newsPage);

        mockMvc.perform(get("/news/delete").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-delete-news"))
                .andExpect(model().attributeExists("newsPage"))
                .andExpect(model().attribute("newsPage", newsPage));
    }

    @Test
    public void testDeleteNews() throws Exception {
        mockMvc.perform(post("/news/delete")
                        .param("id", "1")
                        .param("title", "Test News")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/news/delete?title=Test%20News&page=0&size=10"));
    }
}
