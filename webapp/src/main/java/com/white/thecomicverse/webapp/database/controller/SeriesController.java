package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.Login;
import com.white.thecomicverse.webapp.database.model.Series;
import com.white.thecomicverse.webapp.database.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/series")
public class SeriesController {
    @Autowired
    private SeriesRepository seriesRepository;

    @RequestMapping(value = "/createSeries") // Map ONLY GET Requests
    public ModelAndView createSeries(HttpServletRequest req, @RequestParam(value = "seriesName") String seriesName,
                                     @RequestParam(value = "description") String description,
            @RequestParam(value = "categories") String categories, @RequestParam(value = "author") String author,
            @RequestParam(value = "thumbnail") byte[] thumbnail) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        System.out.println(categories);

        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesName().equals(seriesName)) {

                ModelAndView mv2 = new ModelAndView("create_comic_series?seriesNameExist");
                return mv2;
            }
        }

        Series newSeries = new Series();
        newSeries.setSeriesName(seriesName);
        newSeries.setAuthor(author);
        newSeries.setDescription(description);
        newSeries.setCategories(categories);
        newSeries.setThumbnail(thumbnail);
        this.seriesRepository.save(newSeries);
        List<Series> seriesList = new ArrayList<Series>();

        for (Series s : seriesRepository.findAll()) {
            if (s.getSeriesName().equals(seriesName)){
                if (s.getAuthor().equals(author)){
                    seriesList.add(s);
                }
            }

        }

        ModelAndView mv = new ModelAndView("manage_my_series");
        mv.addObject(seriesList);
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
            if (series.getSeriesName().equals(seriesName)) {
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject(s);
        return mv;

    }

    @RequestMapping(value = "/checkSeriesAuthor") // Map ONLY GET Requests
    public ModelAndView getSeriesByAuthor(HttpServletRequest req,
            @RequestParam(value = "authorName") String seriesAuthor) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getAuthor().equals(seriesAuthor)) {
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject(s);
        return mv;

    }

    @RequestMapping(value = "/checkSeriesAll") // Map ONLY GET Requests
    public ModelAndView getSeriesByAll(HttpServletRequest req,
                                          @RequestParam(value = "authorName") String seriesInfo) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getAuthor().equals(seriesInfo)) {
                s.add(series);
            }
            if (series.getSeriesName().equals(seriesInfo)) {
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject(s);
        return mv;

    }

    @GetMapping(path = "/allSeries")
    public ModelAndView getAllSeries(HttpServletRequest req) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()) {
            s.add(series);
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject(s);
        return mv;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Series> getAllSeries() {
        // This returns a JSON or XML with the users
        return seriesRepository.findAll();
    }

}
