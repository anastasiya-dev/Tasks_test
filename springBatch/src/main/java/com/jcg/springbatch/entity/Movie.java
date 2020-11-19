package com.jcg.springbatch.entity;

import java.util.List;

public class Movie {
    private String title;

    private long year;

    private List<String> cast;

    private List<String> genres;

    public String getTitle() {
        return title;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

}
