package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.Series;
import com.white.thecomicverse.webapp.database.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/series")
public class SeriesController {
    @Autowired
    private SeriesRepository seriesRepository;

    @RequestMapping(value="/createSeries") // Map ONLY GET Requests
    public String addNewLogin (HttpServletRequest req, @RequestParam(value = "seriesName") String seriesName) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        for (Series series : seriesRepository.findAll()){
            if (series.getSeriesName().equals(seriesName)){
                return "redirect:/create_comic_series?seriesNameExist";
            }
        }

        Series newSeries = new Series();
        newSeries.setSeriesName(seriesName);
        newSeries.setAuthor("Dummy Author");
        newSeries.setCategories("category");
        this.seriesRepository.save(newSeries);
        return "redirect:/create_comic_series?success=" + seriesName;

    }


    @RequestMapping(value="/checkSeriesName") // Map ONLY GET Requests
    public ModelAndView getSeriesByName (HttpServletRequest req, @RequestParam(value = "seriesName") String seriesName) {
        List<Series> s = new ArrayList<Series>();
        for (Series series : seriesRepository.findAll()){
            if (series.getSeriesName().equals(seriesName)){
                s.add(series);
            }
        }

        ModelAndView mv =  new ModelAndView("browse");
        mv.addObject(s);
        return mv;


    }

    @RequestMapping(value="/checkSeriesAuthor") // Map ONLY GET Requests
    public ModelAndView getSeriesByAuthor (HttpServletRequest req, @RequestParam(value = "authorName") String seriesAuthor) {
        List<Series> s = new ArrayList<Series>();

        for (Series series : seriesRepository.findAll()){
            if (series.getAuthor().equals(seriesAuthor)){
                s.add(series);
            }
        }

        ModelAndView mv = new ModelAndView("browse");
        mv.addObject(s);
        return mv;

    }


}
