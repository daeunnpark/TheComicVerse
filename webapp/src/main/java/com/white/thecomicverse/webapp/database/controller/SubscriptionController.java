

package com.white.thecomicverse.webapp.database.controller;


import com.white.thecomicverse.webapp.database.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.white.thecomicverse.webapp.database.model.subscription;
import com.white.thecomicverse.webapp.database.model.Series;

import com.white.thecomicverse.webapp.database.repositories.SubscriptionRepository;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/subs")
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    private SeriesRepository seriesRepository;

    @RequestMapping(value="/addSubscription")
    public ModelAndView subscribe (HttpServletRequest req, @RequestParam(value = "username") String username, @RequestParam(value = "SeriesName") int seriesID) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Date d = new Date();
        subscription sub = new subscription();
        sub.setDate(d.toGMTString());
        sub.setSeriesID(seriesID);
        sub.setUsername(username);
        this.subscriptionRepository.save(sub);
        Series s = new Series();
        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == seriesID) {
                s = series;
            }
        }

        ModelAndView mv = new ModelAndView("view_comic_series");
        mv.addObject(s);
        return mv;


    }

    @RequestMapping(value="/deleteSubscription")
    public ModelAndView unsubscribe (HttpServletRequest req, @RequestParam(value = "username") String username, @RequestParam(value = "SeriesName") String seriesID) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        subscription sub = new subscription();
        for (subscription s : subscriptionRepository.findAll()){
            if (s.getSeriesID() == seriesID){
                if (s.getUsername().equals(username)){
                    sub = s;
                }
            }
        }


        this.subscriptionRepository.delete(sub);


        Series s = new Series();
        for (Series series : seriesRepository.findAll()) {
            s= series;
        }

        ModelAndView mv = new ModelAndView("view_comic_series");
        mv.addObject(s);
        return mv;


    }

/*
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

    */

}