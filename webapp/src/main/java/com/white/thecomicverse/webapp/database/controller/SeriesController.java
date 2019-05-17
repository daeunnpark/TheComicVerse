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
@RequestMapping(path = "/series")
public class SeriesController {
    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private EpisodeImageRepository episodeImageRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @RequestMapping(value = "/createSeries") // Map ONLY GET Requests
    public ModelAndView createSeries(HttpServletRequest req, @RequestParam(value = "seriesName") String seriesName,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "categories") String categories, @RequestParam(value = "author") String author,
            @RequestParam(value = "thumbnail") String thumbnail, RedirectAttributes redir) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        // System.out.println("Category: " + categories);

        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesName().equals(seriesName)) {

                ModelAndView mv2 = new ModelAndView("create_comic_series?seriesNameExist");
                return mv2;
            }
        }
        // System.out.println("enteringing: " + thumbnail.length());

        byte[] b = thumbnail.getBytes();
        Series newSeries = new Series();
        newSeries.setSeriesName(seriesName);
        newSeries.setAuthor(author);
        newSeries.setDescription(description);
        newSeries.setCategories(categories);
        newSeries.setThumbnail(b);
        newSeries.setImageData(null);
        this.seriesRepository.save(newSeries);

        return getMySeries(req, author);
    }

    @RequestMapping(value = "/mySeries") // Map ONLY GET Requests
    public ModelAndView getMySeries(HttpServletRequest req, @RequestParam(value = "username") String author) {

        // System.out.println(author + "in My Series");
        List<Series> seriesList = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            series.setImageData(new String(series.getThumbnail()));
        }

        for (Series s : seriesRepository.findAll()) {
            // if (s.getSeriesName().equals(seriesName)) {
            if (s.getAuthor().equals(author)) {
                seriesList.add(s);
            }
            // }
        }

        ModelAndView mv = new ModelAndView("manage_my_series");
        mv.addObject("series", seriesList);
        return mv;
    }
/*
    @RequestMapping(value = "/categoryBrowse")
    public ModelAndView getSeriesByCategories(HttpServletRequest req,
            @RequestParam(value = "searchOption") List<String> categoryList) {

        List<Series> s = new ArrayList<Series>();

        if (categoryList.size() == 0){

        }
        for (Series series : seriesRepository.findAll()) {
            if (categoryList.contains(series.getCategories())) {
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject("series", s);
        return mv;

    }
    */
    @RequestMapping(value="/addSubscription")
    public ModelAndView subscribe (HttpServletRequest req, @RequestParam(value = "username") String username, @RequestParam(value = "SeriesID") int seriesID) {

        ModelAndView mv = new ModelAndView("manage_my_series");


        Date d = new Date();
        Subscription sub = new Subscription();
        sub.setDate(d.toGMTString());
        sub.setSeriesID(seriesID);
        sub.setUsername(username);
        this.subscriptionRepository.save(sub);


        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == seriesID){
                mv.addObject("series", series);
                break;

            }
        }

        mv.addObject("subs", true);

        return mv;
    }



    @RequestMapping(value="/checkSubscription")
    public ModelAndView checkSubscription (HttpServletRequest req, @RequestParam(value = "username") String username, @RequestParam(value = "SeriesID") int seriesID) {

        boolean subs = false;
        Subscription sub = new Subscription();
        for (Subscription s : subscriptionRepository.findAll()){
            if (s.getSeriesID() == seriesID){
                if (s.getUsername().equals(username)){
                    subs = true;
                }
            }
        }
        ModelAndView mv = new ModelAndView("view_comic_series");
        List<Episode> episodeList = new ArrayList<>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == seriesID) {
                series.setImageData(new String(series.getThumbnail()));
                mv.addObject("series", series);
            }
        }

        for (Episode episode : episodeRepository.findAll()) {
            if (episode.getSeriesID() == seriesID) {
                episode.setImageData(new String(episode.getThumbnail()));
                episodeList.add(episode);
            }
        }

        mv.addOject("sub", subs);

        mv.addObject("episodes", episodeList);

        return mv;
    }

    @RequestMapping(value="/deleteSubscription")
    public ModelAndView unsubscribe (HttpServletRequest req, @RequestParam(value = "username") String username, @RequestParam(value = "SeriesID") int seriesID) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        ModelAndView mv = new ModelAndView("manage_my_series");

        Subscription sub = new Subscription();
        for (Subscription s : subscriptionRepository.findAll()){
            if (s.getSeriesID() == seriesID){
                if (s.getUsername().equals(username)){
                    sub = s;
                }
            }
        }

        this.subscriptionRepository.delete(sub);


        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == seriesID){
                mv.addObject("series", series);
                break;

            }
        }


        mv.addObject("subs", false);

        return mv;




    }

    @RequestMapping(value = "subscriptedSeries")
    public ModelAndView getSeriesBySubscription(HttpServletRequest req,
            @RequestParam(value = "username") String username) {

        List<Series> s = new ArrayList<Series>();
        List<Integer> seriesIDs = new ArrayList<Integer>();

        for (Subscription sub : subscriptionRepository.findAll()) {
            if (sub.getUsername().equalsIgnoreCase(username)) {
                for (Series series : seriesRepository.findAll()) {
                    if (series.getSeriesID() == sub.getSeriesID()) {
                        s.add(series);
                        break;
                    }
                }
            }
        }

        ModelAndView mv = new ModelAndView("home");
        mv.addObject("series", s);
        return mv;

    }

    @RequestMapping(value = "/search") // Map ONLY GET Requests
    public ModelAndView getSearchOption(HttpServletRequest req,
            @RequestParam(value = "searchOption") String searchOption,
            @RequestParam(value = "keyword") String keyword) {

        if (searchOption.equals("title")) {
            return getSeriesByName(req, keyword);
        } else if (searchOption.equals("author")) {
            return getSeriesByAuthor(req, keyword);
        }
        return getSeriesByAll(req, keyword);

    }

    @RequestMapping(value = "/checkSeriesName") // Map ONLY GET Requests
    public ModelAndView getSeriesByName(HttpServletRequest req, @RequestParam(value = "seriesName") String seriesName) {

        List<Series> s = new ArrayList<Series>();
        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesName().toLowerCase().contains(seriesName.toLowerCase())) {
                series.setImageData(new String(series.getThumbnail()));
                s.add(series);

                // System.out.println(seriesName);

            }
        }

        ModelAndView mv = new ModelAndView("browse");
        // mv.addObject(s);
        mv.addObject("series", s);
        return mv;

    }

    @RequestMapping(value = "/checkSeriesAuthor") // Map ONLY GET Requests
    public ModelAndView getSeriesByAuthor(HttpServletRequest req,
            @RequestParam(value = "authorName") String seriesAuthor) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getAuthor().toLowerCase().contains(seriesAuthor.toLowerCase())) {
                // System.out.println(seriesAuthor);
                series.setImageData(new String(series.getThumbnail()));
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        // mv.addObject(s);
        mv.addObject("series", s);
        return mv;

    }

    @RequestMapping(value = "/checkSeriesAll") // Map ONLY GET Requests
    public ModelAndView getSeriesByAll(HttpServletRequest req, @RequestParam(value = "authorName") String seriesInfo) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getAuthor().toLowerCase().contains(seriesInfo.toLowerCase())) {
                // System.out.println(seriesInfo);
                series.setImageData(new String(series.getThumbnail()));
                s.add(series);
            }
            if (series.getSeriesName().toLowerCase().contains(seriesInfo.toLowerCase())) {
                series.setImageData(new String(series.getThumbnail()));
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject("series", s);
        return mv;

    }

    // For Browse
    @RequestMapping(value = "/allSeries")
    public ModelAndView getAllSeries(HttpServletRequest req) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            series.setImageData(new String(series.getThumbnail()));
            s.add(series);
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject("series", s);
        return mv;
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Series> getAllSeries() {
        // This returns a JSON or XML with the users
        return seriesRepository.findAll();
    }

    @GetMapping(path = "/view_series")
    public @ResponseBody ModelAndView viewSeries(HttpServletRequest req,
                                                 @RequestParam(value = "seriesID") int seriesID, @RequestParam(value = "username") String username) {
        // System.out.println("view_Epi :series ID = " + seriesID);

        return checkSubscription(req, username, seriesID);
    }

//loged in user
    @GetMapping(path = "/home_series")
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
        return mv;

    }

    //unlogged in user
    @GetMapping(path = "/home_series2")
    public @ResponseBody ModelAndView homeSeries2(HttpServletRequest req, @RequestParam(value = "username") String username) {
        // System.out.println("view_Epi :series ID = " + seriesID);
        List<Series> s = new ArrayList<Series>();

        for (Series se : seriesRepository.findAll()){
            se.setSumLikes(0);
            for (Episode ep : episodeRepository.findAll()){
                if (ep.getSeriesID() == se.getSeriesID()){
                    se.setSumLikes(se.getSumLikes() + ep.getNumLikes());
                }
            }
        }
        Collections.sort(s, new Comparator<Series>() {
            @Override
            public int compare(Series o1, Series o2) {
                Integer num1 = new Integer(o1.getSumLikes());
                Integer num2 = new Integer(o2.getSumLikes());
                return num1.compareTo(num2);
            }
        });

        ModelAndView mv = new ModelAndView("home");
        // mv.addObject(s);
        mv.addObject("series", s);
        return mv;

    }






    @GetMapping(path = "/editComic")
    public @ResponseBody ModelAndView editSeries(HttpServletRequest req,
                                                 @RequestParam(value = "seriesID") int seriesID) {
        // System.out.println("view_Epi :series ID = " + seriesID);

        ModelAndView mv = new ModelAndView("edit_series");

        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == seriesID) {
                series.setImageData(new String(series.getThumbnail()));
                mv.addObject("series", series);
            }
        }

        return mv;
    }

    @RequestMapping(value = "/deleteSeries") // Map ONLY GET Requests
    public ModelAndView deleteEpisode(HttpServletRequest req, @RequestParam(value = "username") String username,
            @RequestParam(value = "seriesID") int seriesID) {

        List<Integer> episodeIDList = new ArrayList<Integer>();

        for (Episode epi : episodeRepository.findAll()) {
            if (epi.getSeriesID() == seriesID) {
                episodeIDList.add(epi.getEpisodeID());
                episodeRepository.delete(epi);
            }
        }

        for (EpisodeImage episodeImage : episodeImageRepository.findAll()) {
            for (int epiID : episodeIDList) {
                if (episodeImage.getEpisodeID() == epiID) {
                    episodeImageRepository.delete(episodeImage);
                }
            }
        }

        for (Series s : seriesRepository.findAll()) {
            if (seriesID == s.getSeriesID()) {
                seriesRepository.delete(s);
            }
        }

        return getMySeries(req, username);

    }

}
