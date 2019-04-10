package com.white.thecomicverse.webapp.database.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

@Entity // This tells Hibernate to make a table out of this class
public class EpisodeImage {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int EpisodeImageID;

    private int EpisodeID;

    private int index;

    private Blob imageData;

    public int getEpisodeImageID() {
        return EpisodeImageID;
    }

    public void setEpisodeImageID(int episodeImageID) {
        EpisodeImageID = episodeImageID;
    }

    public int getEpisodeID() {
        return EpisodeID;
    }

    public void setEpisodeID(int episodeID) {
        EpisodeID = episodeID;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getimageData(){

        try {

            int length = (int) this.imageData.length();
            return this.imageData.getBytes(1, length);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void setThumbnail(byte[] byteArray){

        try {
            this.imageData = new SerialBlob(byteArray);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


}