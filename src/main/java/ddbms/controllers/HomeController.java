package ddbms.controllers;
import ddbms.models.Article;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home(@RequestParam(value="articles", required=false, defaultValue="World") ArrayList<Article> articles, Model model) {
        articles = new ArrayList<Article>();
        Article article1 = new Article("aid", "timestamp", "title", "category", "abst", "articleTags", "authors", "language", "text", "georges.jpg", "NULL");
        Article article2 = new Article("aid", "timestamp", "title2", "category", "abst", "articleTags", "authors", "language", "text", "georges.jpg", "NULL");
        articles.add(article1);
        articles.add(article2);
        model.addAttribute("articles", articles);
        return "home";
    }
}
