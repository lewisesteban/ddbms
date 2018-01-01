package ddbms.controllers;
import ddbms.models.Article;
import ddbms.Domain;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {

    @RequestMapping("/article")
    public String standard_article(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "article";
    }
    @RequestMapping(value = "/article/{user_id}/{article_id}", method= RequestMethod.GET)
    public String article(@PathVariable String article_id, @PathVariable String user_id, @RequestParam(value="article", required=false, defaultValue="World") Article article, Model model) {
        //getArticle()
        try {
            article = Domain.readArticle(user_id, article_id);
        } catch (Exception e) {

        }
        model.addAttribute("article", article);
        return "article";
    }
}
