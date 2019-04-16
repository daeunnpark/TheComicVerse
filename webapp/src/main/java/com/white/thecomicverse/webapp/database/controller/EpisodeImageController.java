package com.white.thecomicverse.webapp.database.controller;

import com.white.thecomicverse.webapp.database.model.Episode;
import com.white.thecomicverse.webapp.database.model.EpisodeImage;
import com.white.thecomicverse.webapp.database.repositories.EpisodeImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path="/Image")
public class EpisodeImageController {

    @Autowired
    EpisodeImageRepository episodeImageRepository;

    @RequestMapping(value="addImage")
    public String addImage(@RequestParam(value="episodeID") int episodeID, @RequestParam(value="thumbnail") byte[] thumbnail){

        int max = 0;
        for (EpisodeImage episodeImage : episodeImageRepository.findAll()){
            if (episodeImage.getEpisodeID()==episodeID && episodeImage.getIndex()>max){
                max = episodeImage.getIndex();
            }
        }

        EpisodeImage newEpisodeImage = new EpisodeImage();
        newEpisodeImage.setEpisodeID(episodeID);
        newEpisodeImage.setIndex(max);
        newEpisodeImage.setThumbnail(thumbnail);
        this.episodeImageRepository.save(newEpisodeImage);
        return "redirect:/upload_episode";
    }

}
