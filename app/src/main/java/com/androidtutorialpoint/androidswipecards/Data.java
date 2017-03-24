package com.androidtutorialpoint.androidswipecards;

/**
 * Created by navneet on 20/11/16.
 */

public class Data {

    private String description;
    private String cardImage;
    private String challenge;

    public Data(String cardImage,String description, String challenge) {
        this.description = description;
        this.cardImage = cardImage;
        this.challenge = challenge;
    }

    public String getDescription() {
        return description;
    }

    public String getCardImage(){return cardImage;}

    public String getChallenge(){return challenge;}


}
