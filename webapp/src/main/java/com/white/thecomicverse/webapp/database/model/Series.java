package com.white.thecomicverse.webapp.database.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

@Entity // This tells Hibernate to make a table out of this class
public class Series {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int SeriesID;

    private String SeriesName;

    private String author;

    private String categories;

    private String description;

    private Blob thumbnail;

    private String imageData;

    public String getImageData() {
        return this.imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }


    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }


    public int getSeriesID() {
        return this.SeriesID;
    }

    public void setSeriesID(int seriesID) {
        this.SeriesID = seriesID;
    }

    public String getSeriesName() {
        return this.SeriesName;
    }

    public void setSeriesName(String seriesName) {
        this.SeriesName = seriesName;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategories() {
        return this.categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public byte[] getThumbnail(){

        try {

            int length = (int) this.thumbnail.length();
            System.out.println("getThunbmail");
            System.out.println("length is" + length);
            return this.thumbnail.getBytes(1, length);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void setThumbnail(byte[] byteArray){

        try {
            this.thumbnail = new SerialBlob(byteArray);
            System.out.println("setThumnail working");
            System.out.println();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


}