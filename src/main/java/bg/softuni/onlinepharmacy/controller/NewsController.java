package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.dto.AddNewsDTO;
import bg.softuni.onlinepharmacy.model.entity.News;
import bg.softuni.onlinepharmacy.service.impl.NewsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class NewsController {
    @Autowired
    private NewsServiceImpl newsServiceImpl;

    @ModelAttribute("newsData")
    public AddNewsDTO addNewsDTO() {
        return new AddNewsDTO();
    }

    @GetMapping("/news")
    public String listNews(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<News> newsPage = newsServiceImpl.getAllNews(page, size);
        model.addAttribute("newsPage", newsPage);
        return "news";
    }

    @GetMapping("/news/add")
    public String showAddNewsForm() {
        return "administrator-add-news";
    }

    @PostMapping("/news/add")
    public String addNews(@Valid AddNewsDTO data, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("newsData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newsData", bindingResult);
            return "redirect:/news/add";
        }

        String result = newsServiceImpl.createNews(data);
        if (result.equals("titleExist")) {
            redirectAttributes.addFlashAttribute("newsData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newsData", bindingResult);
            redirectAttributes.addFlashAttribute("titleExist", true);
            return "redirect:/news/add";
        }

        return "redirect:/news";
    }

    @GetMapping("/news/delete")
    public String showDeleteNewsPage(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                     @RequestParam(name = "title", required = false) String title) throws UnsupportedEncodingException {
        Page<News> newsPage;
        if (title != null && !title.isEmpty()) {
            newsPage = newsServiceImpl.searchNewsByTitle(title, page, size);
        } else {
            newsPage = newsServiceImpl.getAllNews(page, size);
        }
        model.addAttribute("newsPage", newsPage);
        model.addAttribute("title", title);
        return "administrator-delete-news";
    }

    @PostMapping("/news/delete")
    public String deleteNews(@RequestParam Long id, @RequestParam String title,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "10") int size) throws UnsupportedEncodingException {
        newsServiceImpl.deleteNews(id);
        String encoded = URLEncoder.encode(title, "UTF-8");
        encoded = encoded.replace("+", "%20");
        return "redirect:/news/delete?title=" + encoded + "&page=" + page + "&size=" + size;
    }
}
