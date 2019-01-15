package com.example.sagar.couchbasegames;


public class Score {

    private String email, name;
    private int score;


    public Score(String email, String name, int score) {
        this.email = email;
        this.name = name;
        this.score = score;
    }



    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }


    // END
}
