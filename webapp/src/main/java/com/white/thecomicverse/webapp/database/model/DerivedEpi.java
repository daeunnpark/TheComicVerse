package com.white.thecomicverse.webapp.database.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

@Entity // This tells Hibernate to make a table out of this class
public class DerivedEpi {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int derivedEpiID;

    private int OriginalID;

    private Blob endingScene;

    private String author;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDerivedEpiID() {
        return derivedEpiID;
    }

    public void setDerivedEpiID(int derivedEpiID) {
        this.derivedEpiID = derivedEpiID;
    }

    public int getOriginalID() {
        return OriginalID;
    }

    public void setOriginalID(int originalID) {
        OriginalID = originalID;
    }

    public byte[] getEndingScene(){

        try {

            int length = (int) this.endingScene.length();

            return this.endingScene.getBytes(1, length);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void setEndingScene(byte[] byteArray){

        try {
            this.endingScene = new SerialBlob(byteArray);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


}