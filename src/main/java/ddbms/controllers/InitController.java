package ddbms.controllers;

import ddbms.Dal;
import ddbms.Domain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
public class InitController {

    @RequestMapping("/init")
    public String article() {
        System.out.println("init db");
        try {
            Dal.get().initDb(Domain.getRegion());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "init";
    }

}
