
package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.EpisodeImage;
import com.white.thecomicverse.webapp.database.repositories.EpisodeImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.white.thecomicverse.webapp.database.model.Episode;
import com.white.thecomicverse.webapp.database.repositories.EpisodeRepository;
import org.springframework.web.servlet.ModelAndView;

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
    EpisodeImageRepository episodeImageRepository;

    @RequestMapping(value = "/addEpisode") // Map ONLY GET Requests
    public String addEpisode(HttpServletRequest req, @RequestParam(value = "seriesID") int SeriesID,
            @RequestParam(value = "episodeName") String episodeName,
            @RequestParam(value = "thumbnail") String thumbnail,
            @RequestParam(value = "imageList") List<String> imageList) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeName().equals(episodeName)) {
                return "redirect:/upload_episode?episodeExist";
            }

        }

        byte[] thumbnailByteArr = thumbnail.getBytes();
        Date d = new Date();
        Episode epi = new Episode();
        epi.setSeriesID(SeriesID);
        epi.setEpisodeName(episodeName);
        epi.setNumDislikes(0);
        epi.setNumLikes(0);
        epi.setNumView(0);
        epi.setThumbnail(thumbnailByteArr);
        epi.setDateCreated(d.toGMTString());
        this.EpiRepository.save(epi);
        for(int i=0; i<imageList.size();i++){
            addImage(epi.getEpisodeID(), imageList.get(i));
        }
        return "redirect:/upload_episode";

    }

    /**
     * redirect to previous episode
     **/
    @RequestMapping(value = "/prevEp")
    public ModelAndView prevEpisode(HttpServletRequest req, @RequestParam(value = "seriesID") int seriesID,
            @RequestParam(value = "episodeIndex") String episodeIndex) {

        String prevEpID = Integer.toString(-1);
        int epiIndex = Integer.parseInt(episodeIndex);
        ModelAndView mv = new ModelAndView("read_episode");

        if (epiIndex == 0){
            return null;
        }
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID && episode.getIndex() == (Integer.parseInt(episodeIndex)-1) ) {
                prevEpID = Integer.toString(episode.getEpisodeID());
            }
        }

        mv.addObject("episodeID", prevEpID);
        return mv;
    }

    /**
     * redirect to next episode
     **/
    @RequestMapping(value = "/nextEp") // Map ONLY GET Requests
    public ModelAndView nextEpisode(HttpServletRequest req, @RequestParam(value = "seriesID") int seriesID,
                                    @RequestParam(value = "episodeIndex") String episodeIndex) {
        String nextEpID = Integer.toString(-1);
        int max = 0;
        int epiIndex = Integer.parseInt(episodeIndex);
        ModelAndView mv = new ModelAndView("read_episode");

        for (Episode episode : EpiRepository.findAll()) {
            if(episode.getSeriesID() == seriesID && episode.getIndex() > max){
                max = episode.getIndex();
            }
        }
        if (epiIndex >= max) {
            return null;
        }
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID && episode.getIndex() == (Integer.parseInt(episodeIndex)+1) ) {
                nextEpID = Integer.toString(episode.getEpisodeID());
            }
        }

        mv.addObject("episodeID", nextEpID);

        return mv;
    }

    /**
     * retrieve episodes of a specific series
     */
    @RequestMapping(value = "/allEp") // Map ONLY GET Requests
    public String allEpisodes(HttpServletRequest req, @RequestParam(value = "seriesID") int SeriesID) {
        return "";
    }

    /**
     * read Episode
     */
    @RequestMapping(value = "/readEpisode") // Map ONLY GET Requests
    public ModelAndView readEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID) {
        ModelAndView mv = new ModelAndView("read_episode");
        List<EpisodeImage> ImageList = new ArrayList<>();
        for (Episode episode : EpiRepository.findAll()) {
            if(episode.getEpisodeID() == episodeID){
                mv.addObject("episode",episode);
                for(EpisodeImage episodeImage: episodeImageRepository.findAll()){
                    if(episodeImage.getEpisodeID() == episodeID) {
                        ImageList.add(episodeImage.getIndex(),episodeImage);
                    }
                }
                mv.addObject("ImageList",ImageList);
            }
        }
        return mv;
    }

    public void addImage(int episodeID, String imageData){

        int max = 0;
        for (EpisodeImage episodeImage : episodeImageRepository.findAll()){
            if (episodeImage.getEpisodeID()==episodeID && episodeImage.getIndex()>max){
                max = episodeImage.getIndex();
            }
        }
        byte[] imageDataBytes = imageData.getBytes();

        EpisodeImage newEpisodeImage = new EpisodeImage();
        newEpisodeImage.setEpisodeID(episodeID);
        newEpisodeImage.setIndex(max);
        newEpisodeImage.setImageData(imageDataBytes);
        this.episodeImageRepository.save(newEpisodeImage);
    }

    @RequestMapping(value = "/deleteEpisode") // Map ONLY GET Requests
    public String deleteEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID) {
        for (EpisodeImage episodeImage : episodeImageRepository.findAll()){
            if (episodeImage.getEpisodeID()==episodeID){
                episodeImageRepository.delete(episodeImage);
            }
        }
        for (Episode epi : EpiRepository.findAll()){
            if (epi.getEpisodeID()==episodeID){
                EpiRepository.delete(epi);
            }
        }
        return "redirect:/manage_my_episodes";

    }
}