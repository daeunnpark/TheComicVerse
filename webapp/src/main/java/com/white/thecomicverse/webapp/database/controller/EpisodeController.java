
package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.EpisodeImage;
import com.white.thecomicverse.webapp.database.repositories.EpisodeImageRepository;
import com.white.thecomicverse.webapp.database.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.white.thecomicverse.webapp.database.model.Episode;
import com.white.thecomicverse.webapp.database.model.Series;
import com.white.thecomicverse.webapp.database.repositories.EpisodeRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

//@RequestMapping(path = "/episode") already exists somewhere
@Controller
@RequestMapping(path = "/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeRepository EpiRepository;

    @Autowired
    private EpisodeImageRepository episodeImageRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @RequestMapping(value = "/upload_episode")
    public ModelAndView uploadEpisode(HttpServletRequest req, @RequestParam(value = "username") String username) {
        ModelAndView mv = new ModelAndView("upload_episode");

        List<Series> seriesList = new ArrayList<>();
        for (Series series : seriesRepository.findAll()) {
            if (series.getAuthor().equals(username)) {
                // System.out.println(series.getSeriesID());
                seriesList.add(series);
            }
        }
        // ra.addFlashAttribute("series", seriesList);
        mv.addObject("series", seriesList);
        return mv;

    }

    @RequestMapping(value = "/addEpisode") // Map ONLY GET Requests
    public ModelAndView addEpisode(HttpServletRequest req, @RequestParam(value = "seriesID") String SeriesID,
            @RequestParam(value = "episodeName") String episodeName,
            @RequestParam(value = "thumbnail") String thumbnail, @RequestParam(value = "episodeImage") String image) {

        // System.out.println("add Epi:series ID = " + SeriesID);

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeName().equals(episodeName)) {
                return new ModelAndView("redirect:/upload_episode?episodeExist");
            }

        }

        ModelAndView mv = new ModelAndView("home");

        byte[] thumbnailByteArr = thumbnail.getBytes();
        Date d = new Date();
        Episode epi = new Episode();
        epi.setSeriesID(Integer.parseInt(SeriesID));
        epi.setEpisodeName(episodeName);
        epi.setNumDislikes(0);
        epi.setNumLikes(0);
        epi.setNumView(0);
        epi.setThumbnail(thumbnailByteArr);
        epi.setDateCreated(d.toGMTString());

        int max = -1;
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == Integer.parseInt(SeriesID) && episode.getIndices() > max) {
                max = episode.getIndices();
            }
        }
        epi.setIndices(max + 1);
        this.EpiRepository.save(epi);
        addImage(epi.getEpisodeID(), image);

        return mv;

    }

    /**
     * redirect to previous episode
     **/
    @RequestMapping(value = "/prevEp")
    public ModelAndView prevEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") String episodeID,
            @RequestParam(value = "episodeIndex") String episodeIndex) {
        int seriesID = -1;
        for (Episode episode : EpiRepository.findAll()) {
            if (Integer.parseInt(episodeID) == episode.getEpisodeID()) {
                seriesID = episode.getSeriesID();
            }
        }

        int prevEpID = -1;
        int epiIndex = Integer.parseInt(episodeIndex);
        ModelAndView mv = new ModelAndView("read_episode");

        if (epiIndex == 0) {
            return null;
        }
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID && episode.getIndices() == (Integer.parseInt(episodeIndex) - 1)) {
                prevEpID = episode.getEpisodeID();
            }
        }

        List<EpisodeImage> imageList = new ArrayList<>();
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeID() == prevEpID) {
                mv.addObject("episode", episode);
                for (EpisodeImage episodeImage : episodeImageRepository.findAll()) {
                    if (episodeImage.getEpisodeID() == prevEpID) {
                        episodeImage.setImageString(new String(episodeImage.getImageData()));
                        imageList.add(episodeImage.getIndices(), episodeImage);
                    }
                }
                mv.addObject("imageList", imageList);
            }
        }

        return mv;
    }

    /**
     * redirect to next episode
     **/
    @RequestMapping(value = "/nextEp") // Map ONLY GET Requests
    public ModelAndView nextEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") String episodeID,
            @RequestParam(value = "episodeIndex") String episodeIndex) {
        int nextEpID = -1;
        int max = -1;
        int epiIndex = Integer.parseInt(episodeIndex);
        ModelAndView mv = new ModelAndView("read_episode");

        int seriesID = -1;
        for (Episode episode : EpiRepository.findAll()) {
            if (Integer.parseInt(episodeID) == episode.getEpisodeID()) {
                seriesID = episode.getSeriesID();
            }
        }

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID && episode.getIndices() > max) {
                max = episode.getIndices();
            }
        }
        if (epiIndex >= max) {
            return null;
        }
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID && episode.getIndices() == (Integer.parseInt(episodeIndex) + 1)) {
                nextEpID = episode.getEpisodeID();
            }
        }

        List<EpisodeImage> imageList = new ArrayList<>();
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeID() == nextEpID) {
                mv.addObject("episode", episode);
                for (EpisodeImage episodeImage : episodeImageRepository.findAll()) {
                    if (episodeImage.getEpisodeID() == nextEpID) {
                        episodeImage.setImageString(new String(episodeImage.getImageData()));
                        imageList.add(episodeImage.getIndices(), episodeImage);
                    }
                }
                mv.addObject("imageList", imageList);
            }
        }

        return mv;
    }

    /**
     * retrieve episodes of a specific series
     */
    @RequestMapping(value = "/allEpisodes") // Map ONLY GET Requests
    public ModelAndView allEpisodes(HttpServletRequest req, @RequestParam(value = "seriesID") String seriesID) {

        // System.out.println("ALL EPISODES of series ID = " + seriesID + " in int " +
        // Integer.parseInt(seriesID));

        ModelAndView mv = new ModelAndView("manage_my_episodes");
        List<Episode> episodeList = new ArrayList<>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == Integer.parseInt(seriesID)) {
                mv.addObject("series", series); // single serie
                break;
            }
        }

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == Integer.parseInt(seriesID)) {
                episodeList.add(episode);
            }
        }
        mv.addObject("episodes", episodeList);

        return mv;
    }

    /**
     * read Episode
     */
    @RequestMapping(value = "/readEpisode") // Map ONLY GET Requests
    public ModelAndView readEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID) {
        System.out.println("received episode ID: " + episodeID);
        ModelAndView mv = new ModelAndView("read_episode"); // ("redirect:/read_episode");
        List<EpisodeImage> imageList = new ArrayList<>();
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeID() == episodeID) {
                mv.addObject("episode", episode);
                for (EpisodeImage episodeImage : episodeImageRepository.findAll()) {
                    if (episodeImage.getEpisodeID() == episodeID) {
                        episodeImage.setImageString(new String(episodeImage.getImageData()));
                        System.out.println("epiString length = " + episodeImage.getImageString().length());
                        System.out.println("epiImageData length = " + episodeImage.getImageString().length());
                        imageList.add(episodeImage.getIndices(), episodeImage);
                    }
                }
                // System.out.println("ImageList: "+ imageList);
                mv.addObject("imageList", imageList);
                // ra.addFlashAttribute("imageList",imageList);
            }
        }
        return mv;
    }

    public void addImage(int episodeID, String imageData) {
        System.out.println("imageData passed: " + imageData);
        System.out.println("episodeID passed: " + episodeID);
        int max = -1;
        for (EpisodeImage episodeImage : episodeImageRepository.findAll()) {
            if (episodeImage.getEpisodeID() == episodeID && episodeImage.getIndices() > max) {
                max = episodeImage.getIndices();
            }
        }
        byte[] imageDataBytes = imageData.getBytes();
        // System.out.println("imageDataBytes: " + imageDataBytes);
        EpisodeImage newEpisodeImage = new EpisodeImage();
        newEpisodeImage.setEpisodeID(episodeID);
        newEpisodeImage.setIndices(max + 1);
        newEpisodeImage.setImageData(imageDataBytes);

        this.episodeImageRepository.save(newEpisodeImage);
    }

    @RequestMapping(value = "/deleteEpisode") // Map ONLY GET Requests
    public ModelAndView deleteEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
            @RequestParam(value = "seriesID") String seriesID) {

        System.out.println("deleting EPISODE of series ID = " + Integer.parseInt(seriesID));

        for (EpisodeImage episodeImage : episodeImageRepository.findAll()) {
            if (episodeImage.getEpisodeID() == episodeID) {
                episodeImageRepository.delete(episodeImage);
            }
        }
        for (Episode epi : EpiRepository.findAll()) {
            if (epi.getEpisodeID() == episodeID) {
                EpiRepository.delete(epi);
            }
        }

        return allEpisodes(req, seriesID);

    }
}