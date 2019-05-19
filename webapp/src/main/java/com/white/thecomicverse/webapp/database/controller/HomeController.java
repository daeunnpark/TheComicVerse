package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.*;

import com.white.thecomicverse.webapp.database.repositories.EpisodeImageRepository;
import com.white.thecomicverse.webapp.database.repositories.EpisodeRepository;
import com.white.thecomicverse.webapp.database.repositories.SeriesRepository;
import com.white.thecomicverse.webapp.database.repositories.SubscriptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.white.thecomicverse.webapp.database.model.Subscription;
import com.white.thecomicverse.webapp.database.repositories.SubscriptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;
@Controller // This means that this class is a Controller
@RequestMapping(path = "/home")
public class HomeController {
    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private EpisodeImageRepository episodeImageRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    public void updateSumLikes(int SeriesID){

        System.out.println("sum likes here");

        for (Series se : seriesRepository.findAll()){
            int sum = 0;

            if(se.getSeriesID() == SeriesID){
                se.setSumLikes(0);
                //System.out.println("sum likes before" + se.getSumLikes());

                for (Episode ep : episodeRepository.findAll()){
                    if (ep.getSeriesID() == se.getSeriesID()){
                        //System.out.println("this epi num" + ep.getNumLikes());
                        sum  = sum + ep.getNumLikes();
                        //System.out.println("sum likes en cours" + sum);
                    }
                }
                se.setSumLikes(sum);
                //System.out.println("sum likes done" + se.getSumLikes());
                break;
            }

        }

    }

    //unlogged in user
    @GetMapping(path = "/home2")
    public @ResponseBody ModelAndView homeSeries2(HttpServletRequest req) {
        // System.out.println("view_Epi :series ID = " + seriesID);
        List<Series> s = new ArrayList<Series>();

/*
        for (Series se : seriesRepository.findAll()){
            se.setSumLikes(0);
            for (Episode ep : episodeRepository.findAll()){
                if (ep.getSeriesID() == se.getSeriesID()){
                    se.setSumLikes(se.getSumLikes() + ep.getNumLikes());
                }
            }
        }
*/

        for (Series se : seriesRepository.findAll()){
            updateSumLikes(se.getSeriesID());
            s.add(se);
        }

        s.sort(new Comparator<Series>() {
            @Override
            public int compare(Series o1, Series o2) {
                Integer num1 = o1.getSumLikes();
                Integer num2 = o2.getSumLikes();
                return num1.compareTo(num2);
            }
        });

        ModelAndView mv = new ModelAndView("home");
        // mv.addObject(s);
        mv.addObject("series", s);
        mv.addObject("username", null);
        return mv;

    }


    //loged in user
    @GetMapping(path = "/home")
    public @ResponseBody ModelAndView homeSeries(HttpServletRequest req, @RequestParam(value = "username") String username) {
        // System.out.println("view_Epi :series ID = " + seriesID);
        List<Series> s = new ArrayList<Series>();
        for (Subscription sub : subscriptionRepository.findAll()) {
            if (sub.getUsername().equals(username)) {
                for (Series se : seriesRepository.findAll()){
                    if (se.getSeriesID() == sub.getSeriesID()){
                        s.add(se);
                    }
                }

                // System.out.println(seriesName);

            }
        }

        ModelAndView mv = new ModelAndView("home");
        // mv.addObject(s);
        mv.addObject("series", s);
        mv.addObject("username", username);
        return mv;

    }

}
