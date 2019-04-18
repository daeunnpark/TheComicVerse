

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
import org.springframework.web.servlet.ModelAndView;


@Controller    // This means that this class is a Controller
@RequestMapping(path="/login")
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    /**
     *  Sign up method
     */
    @RequestMapping(value="/addLogin") // Map ONLY GET Requests
    public String addNewLogin (HttpServletRequest req, @RequestParam(value = "email") String email, @RequestParam(value = "username") String username
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
        return "redirect:/home";

    }

    /**
     * Login method
     */
    @RequestMapping(value="/checkLogin") // Map ONLY GET Requests
    public String checkLogin (HttpServletRequest req, @RequestParam(value = "username") String username
            ,@RequestParam(value = "password") String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        for (Login login : loginRepository.findAll()){
            if (login.getUsername().equals(username)){
                if (login.getPassword().equals(password)){
                    return "redirect:/home?username="+username;
                }

            }
        }


        return "redirect:/home?error";

    }

    @RequestMapping(value="/getUser") // Map ONLY GET Requests
    public ModelAndView checkLogin (HttpServletRequest req, @RequestParam(value = "username") String username) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Login l = new Login();

        for (Login login : loginRepository.findAll()){
            if (login.getUsername().equals(username)){
                l = login;
            }
        }

        ModelAndView mv = new ModelAndView("account_settings");
        mv.addObject(l);
        return mv;

    }

    @RequestMapping(value="/changePassword") // Map ONLY GET Requests
    public ModelAndView changePassword (HttpServletRequest req, @RequestParam(value = "username") String username
            ,@RequestParam(value = "password") String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Login l = new Login();
        for (Login login : loginRepository.findAll()){
            if (login.getUsername().equals(username)){
                l = login;
            }
        }
        l.setPassword(password);
        this.loginRepository.save(l);

        ModelAndView mv = new ModelAndView("account_settings");
        mv.addObject(l);
        return mv;

    }


    @GetMapping(path="/allLogin")
    public @ResponseBody Iterable<Login> getAllLogin() {
        // This returns a JSON or XML with the users
        return loginRepository.findAll();
    }

}