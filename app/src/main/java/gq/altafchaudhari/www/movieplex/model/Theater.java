package gq.altafchaudhari.www.movieplex.model;

public class Theater {
    public String theater_name,theater_city,movie_name,movie_image;
    int theater_image;

    public Theater() {
    }

    public Theater(String movie_name,String theater_name, String theater_city,String movie_image, int theater_image) {
        this.movie_name = movie_name;
        this.theater_name = theater_name;
        this.theater_city = theater_city;
        this.movie_image = movie_image;
        this.theater_image = theater_image;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getTheater_name() {
        return theater_name;
    }

    public void setTheater_name(String theater_name) {
        this.theater_name = theater_name;
    }

    public String getTheater_city() {
        return theater_city;
    }

    public void setTheater_city(String theater_city) {
        this.theater_city = theater_city;
    }

    public String getMovie_image() {
        return movie_image;
    }

    public void setMovie_image(String movie_image) {
        this.movie_image = movie_image;
    }

    public int getTheater_image() {
        return theater_image;
    }

    public void setTheater_image(int theater_image) {
        this.theater_image = theater_image;
    }
}
