package com.white.thecomicverse.webapp.database.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.white.thecomicverse.webapp.database.model.Login;
import com.white.thecomicverse.webapp.database.repositories.LoginRepository;
import java.util.List;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/sign_up_form")
public class LoginController {
    @Autowired
    private LoginRepository loginRepository;

    @RequestMapping(value="/addLogin") // Map ONLY GET Requests
    public String addNewLogin (HttpServletRequest req, @RequestParam(value = "Email") String email, @RequestParam(value = "username") String username
            ,@RequestParam(value = "password") String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        for (Login login : loginRepository.findAll()){
            if (login.getUsername().equals(username)){
                return "redirect:/sign_up_form?userNameExist";
            }
            if (login.getEmail().equals(email)){
                return "redirect:/sign_up_form?EmailExist";
            }
        }

        Login l = new Login();
        l.setEmail(email);
        l.setusername(username);
        l.setPassword(password);
        this.loginRepository.save(l);
        return "redirect:/sign_up_form";

    }

    /*
    @RequestMapping(value="/checkLogin") // Map ONLY GET Requests
    public String checkLogin (HttpServletRequest req, @RequestParam(value = "email") String email
            ,@RequestParam(value = "password") String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        for (Login login : loginRepository.findAll()){
            if (login.getEmail().equals(email)){
                if (login.getPassword().equals(password)){

                    name = email;
                    return "redirect:/menu";
                }

            }
        }


        return "redirect:/login?error";

    }


    @GetMapping(path="/allLogin")
    public @ResponseBody Iterable<Login> getAllLogin() {
        // This returns a JSON or XML with the users
        return loginRepository.findAll();
    }
*/

}