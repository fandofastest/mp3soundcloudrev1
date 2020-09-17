package com.music.free.modalclass;

public class GenreModel {

    int image;
    String genrename;
    String origenrename;

    public GenreModel( String genrename) {

        this.genrename = genrename;

        String genrelower=genrename.toLowerCase();
        this.origenrename = genrelower.replaceAll("[^a-zA-Z0-9]", " ");
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getGenrename() {
        return genrename;
    }

    public void setGenrename(String genrename) {
        this.genrename = genrename;
    }

    public String getOrigenrename() {
        return origenrename;
    }

    public void setOrigenrename(String origenrename) {
        this.origenrename = origenrename;
    }
}
