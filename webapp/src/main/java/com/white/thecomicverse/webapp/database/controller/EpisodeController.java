
package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.EpisodeImage;
import com.white.thecomicverse.webapp.database.repositories.EpisodeImageRepository;
import com.white.thecomicverse.webapp.database.repositories.LikesRepository;
import com.white.thecomicverse.webapp.database.repositories.SeriesRepository;
import com.white.thecomicverse.webapp.database.repositories.DislikeRepository;
import com.white.thecomicverse.webapp.database.repositories.DerivedEpiRepository;
import com.white.thecomicverse.webapp.database.repositories.CommentsRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.white.thecomicverse.webapp.database.model.Episode;
import com.white.thecomicverse.webapp.database.model.Series;
import com.white.thecomicverse.webapp.database.model.Likes;
import com.white.thecomicverse.webapp.database.model.DerivedEpi;
import com.white.thecomicverse.webapp.database.model.Comments;


import com.white.thecomicverse.webapp.database.model.Dislike;

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

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private DislikeRepository dislikeRepository;

    @Autowired
    private DerivedEpiRepository derivedEpiRepository;

    @Autowired
    private CommentsRepository commentsRepository;

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
    public ModelAndView addEpisode(HttpServletRequest req, @RequestParam(value = "seriesID") int SeriesID,
            @RequestParam(value = "episodeName") String episodeName,
            @RequestParam(value = "thumbnail") String thumbnail, @RequestParam(value = "episodeImage") String image) {

        // System.out.println("add Epi:series ID = " + SeriesID);

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeName().equals(episodeName)) {
                return new ModelAndView("redirect:/upload_episode?episodeExist");
            }

        }

        //ModelAndView mv = new ModelAndView("home");

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

        int max = -1;

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == SeriesID && episode.getIndices() > max) {
                max = episode.getIndices();
            }
        }
        epi.setIndices(max + 1);
        this.EpiRepository.save(epi);
        addImage(epi.getEpisodeID(), image);

        return allEpisodes(req, SeriesID);

       // return mv;

    }


    @RequestMapping(value = "/addDrivedEpi") // Map ONLY GET Requests
    public ModelAndView addDrivedEpi(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                   @RequestParam(value = "username") String username,
                                     @RequestParam(value = "endingScene") String ending ){

        // System.out.println("add Epi:series ID = " + SeriesID);

        DerivedEpi dEpi = new DerivedEpi();

        Episode epi = new Episode();

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getEpisodeID() == episodeID){
                epi = episode;
                break;
            }

        }

        dEpi.setAuthor(username);
        dEpi.setOriginalID(episodeID);


        byte[] endingByteArr = ending.getBytes();

        dEpi.setEndingScene(endingByteArr);

        derivedEpiRepository.save(dEpi);

        List<DerivedEpi> dEpiList = new ArrayList<>();


        for (DerivedEpi dEpisode : derivedEpiRepository.findAll()) {
            if (dEpisode.getOriginalID() == episodeID){
                dEpiList.add(dEpisode);
            }

        }

        ModelAndView mv = new ModelAndView("See_Derived_Epi");

        mv.addObject("originalEpi", epi);
        mv.addObject("derivedEpi", dEpiList);


        return mv;

        // return mv;

    }

    /**
     * redirect to previous episode
     **/
    @RequestMapping(value = "/prevEp")
    public ModelAndView prevEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
            @RequestParam(value = "episodeIndex") int episodeIndex, @RequestParam(value = "username") String username) {
        int seriesID = -1;
        for (Episode episode : EpiRepository.findAll()) {
            if (episodeID == episode.getEpisodeID()) {
                seriesID = episode.getSeriesID();
            }
        }

        int prevEpID = -1;
        int epiIndex = episodeIndex;
        ModelAndView mv = new ModelAndView("read_episode");

        if (epiIndex == 0) {
            return null;
        }
        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID && episode.getIndices() == (episodeIndex - 1)) {
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
        boolean l = false;
        boolean dl = true;

        for (Likes like : LikesRepository.findAll()){
            if (like.getEpisodeID() == episodeID){
                if (like.getUsername().equalsIgnoreCase(username)) {
                    l = true;
                    break;
                }
            }
        }

        for (Dislike dislike : DislikeRepository.findAll()){
            if (dislike.getEpisodeID() == episodeID){
                if (dislike.getUsername().equalsIgnoreCase(username)) {
                    dl = true;
                    break;
                }
            }
        }

        mv.addObject("like", l);
        mv.addObject("dislike", dl);


        return mv;
    }

    @RequestMapping(value = "/addLike") // Map ONLY GET Requests
    public ModelAndView addLikes(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                    @RequestParam(value = "username") String username) {


        Likes l = new Likes();
        l.setEpisodeID(episodeID);
        l.setUsername(username);

        LikesRepository.save(l);

        return readEpisode(req, episodeID, username);
    }

    @RequestMapping(value = "/addComments") // Map ONLY GET Requests
    public ModelAndView addComments(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                 @RequestParam(value = "username") String username,
                                    @RequestParam(value = "text") String text) {


        Comments c = new Comments();
        c.setAuthor(username);
        c.setText(text);
        c.setEpisodeID(episodeID);

        commentsRepository.save(c);

        return readEpisode(req, episodeID, username);
    }

    @RequestMapping(value = "/removeComments") // Map ONLY GET Requests
    public ModelAndView removeComments(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                       @RequestParam(value = "username") String username,
                                       @RequestParam(value = "commentID") int commentID) {

        for (Comments comments : CommentsRepository.findAll()){
            if (comments.getCommentID() == commentID){
                commentsRepository.delete(comments);
            }
        }

        return readEpisode(req, episodeID, username);
    }



    @RequestMapping(value = "/addDislike") // Map ONLY GET Requests
    public ModelAndView addDislikes(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                    @RequestParam(value = "username") String username) {


        Dislike dl = new Dislike();
        dl.setEpisodeID(episodeID);
        dl.setUsername(username);

        LikesRepository.save(dl);

        return readEpisode(req, episodeID, username);
    }

    @RequestMapping(value = "/removeDislike") // Map ONLY GET Requests
    public ModelAndView removeDislikes(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                    @RequestParam(value = "username") String username) {

        for (Dislike dislike : DislikeRepository.findAll()){
            if (dislike.getEpisodeID() == episodeID){
                if (dislike.getUsername().equalsIgnoreCase(username)) {
                    DislikeRepository.delete(dislike);
                    break;
                }
            }
        }

        return readEpisode(req, episodeID, username);
    }

    @RequestMapping(value = "/removeLike") // Map ONLY GET Requests
    public ModelAndView removeDislikes(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                       @RequestParam(value = "username") String username) {

        for (Likes like : LikesRepository.findAll()){
            if (like.getEpisodeID() == episodeID){
                if (like.getUsername().equalsIgnoreCase(username)) {
                    LikesRepository.delete(like);
                    break;
                }
            }
        }

        return readEpisode(req, episodeID, username);
    }

    /**
     * redirect to next episode
     **/
    @RequestMapping(value = "/nextEp") // Map ONLY GET Requests
    public ModelAndView nextEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
            @RequestParam(value = "episodeIndex") int episodeIndex, @RequestParam(value = "username") String username) {
        int nextEpID = -1;
        int max = -1;
        int epiIndex = episodeIndex;
        ModelAndView mv = new ModelAndView("read_episode");

        int seriesID = -1;
        for (Episode episode : EpiRepository.findAll()) {
            if (episodeID == episode.getEpisodeID()) {
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
            if (episode.getSeriesID() == seriesID && episode.getIndices() == (episodeIndex + 1)) {
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


        boolean l = false;
        boolean dl = true;

        for (Likes like : LikesRepository.findAll()){
            if (like.getEpisodeID() == episodeID){
                if (like.getUsername().equalsIgnoreCase(username)) {
                    l = true;
                    break;
                }
            }
        }

        for (Dislike dislike : DislikeRepository.findAll()){
            if (dislike.getEpisodeID() == episodeID){
                if (dislike.getUsername().equalsIgnoreCase(username)) {
                    dl = true;
                    break;
                }
            }
        }

        mv.addObject("like", l);
        mv.addObject("dislike", dl);




        return mv;
    }

    /**
     * retrieve episodes of a specific series
     */
    @RequestMapping(value = "/allEpisodes") // Map ONLY GET Requests
    public ModelAndView allEpisodes(HttpServletRequest req, @RequestParam(value = "seriesID") int seriesID) {

        // System.out.println("ALL EPISODES of series ID = " + seriesID + " in int " +
        // Integer.parseInt(seriesID));

        ModelAndView mv = new ModelAndView("manage_my_episodes");
        List<Episode> episodeList = new ArrayList<>();

        for (Series series : seriesRepository.findAll()) {
            if (series.getSeriesID() == seriesID) {
                series.setImageData(new String(series.getThumbnail()));
                mv.addObject("series", series); // single serie
                break;
            }
        }

        for (Episode episode : EpiRepository.findAll()) {
            if (episode.getSeriesID() == seriesID) {
                episode.setImageData(new String(episode.getThumbnail()));
                System.out.println(episode.getImageData());
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
    public ModelAndView readEpisode(HttpServletRequest req, @RequestParam(value = "episodeID") int episodeID,
                                    @RequestParam(value = "username") String username) {

        System.out.println("received episode ID: " + episodeID);

        ModelAndView mv = new ModelAndView("read_episode"); // ("redirect:/read_episode");

        Episode epi = new Episode();
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

                for (Series series : seriesRepository.findAll()) {
                    if (series.getSeriesID() == episode.getSeriesID()) {
                        series.setImageData(new String(series.getThumbnail()));
                        mv.addObject("series", series); // single serie
                        break;
                    }
                }

            }
        }

        boolean l = false;
        boolean dl = true;

        for (Likes like : LikesRepository.findAll()){
            if (like.getEpisodeID() == episodeID){
                if (like.getUsername().equalsIgnoreCase(username)) {
                    l = true;
                    break;
                }
            }
        }

        for (Dislike dislike : DislikeRepository.findAll()){
            if (dislike.getEpisodeID() == episodeID){
                if (dislike.getUsername().equalsIgnoreCase(username)) {
                    dl = true;
                    break;
                }
            }
        }

        mv.addObject("like", l);
        mv.addObject("dislike", dl);

        List<Comments> commentsList = new ArrayList<>();

        for (Comments c : commentsRepository.findAll()){
            if (c.getEpisodeID() == episodeID){
                commentsList.add(c);
            }
        }

        mv.addObject("comments", commentsList);







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
            @RequestParam(value = "seriesID") int seriesID) {

        System.out.println("deleting EPISODE of series ID = " + seriesID);

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
        for (Likes l : LikesRepository.findAll()){
            if (l.getEpisodeID() == episodeID) {
                LikesRepository.delete(l);
            }
        }
        for (Dislike dl : DislikeRepository.findAll()){
            if (dl.getEpisodeID() == episodeID) {
                DislikeRepository.delete(dl);
            }
        }

        return allEpisodes(req, seriesID);

    }
}