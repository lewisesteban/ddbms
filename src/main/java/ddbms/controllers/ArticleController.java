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
        System.out.println("fuggggggg\n\n\n\n\n");
        return "article";
    }

    @RequestMapping(value = "/article/{user_id}/{article_id}", method= RequestMethod.GET)
    public String article(@PathVariable String user_id, @PathVariable String article_id, Model model) {
        //getArticle()
        try {
            System.out.println(user_id);
            System.out.println(article_id);
            Article article = Domain.readArticle(user_id, article_id);
            model.addAttribute("article", article);
            System.out.println(article.getAuthors());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "article";
    }
}
