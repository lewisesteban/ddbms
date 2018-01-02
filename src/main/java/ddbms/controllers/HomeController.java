package ddbms.controllers;
import ddbms.Domain;
import ddbms.TemporalGranularity;
import ddbms.models.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home(Model model) {
        ArrayList<Article> articles = null;
        try {
            articles = Domain.viewArticles(0, 100);
            model.addAttribute("articles", articles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "home";
    }
}
