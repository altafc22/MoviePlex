package gq.altafchaudhari.www.movieplex.api;

import gq.altafchaudhari.www.movieplex.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {



    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    /*

    https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2019-01-01&primary_release_date.lte=2019-03-23&region=IN&api_key=a1cbe7cb54feb5fb5cca45a3a0592d13*/
    @GET("discover/movie")
    Call<MoviesResponse> getNowInTheaterMovies(@Query("region") String region,
                                               @Query("primary_release_date.gte")String minDate,
                                               @Query("primary_release_date.lte")String maxDate,
                                               @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MoviesResponse> getNowPlayingMovies(@Query("region") String region,
                                               @Query("api_key") String apiKey);

    /*
    https://api.themoviedb.org/3/movie/now_playing?region=IN&api_key=a1cbe7cb54feb5fb5cca45a3a0592d13&page=1
    */

    /**
     * http://api.themoviedb.org/3/discover/movie?&region=IN&release_date.gte=2019-03-22&api_key=********
     *
     * **/
    @GET("discover/movie")
    Call<MoviesResponse> getUpcomingMovies(@Query("region") String region,
                                               @Query("release_date.gte")String minDate,
                                               @Query("api_key") String apiKey);



    //https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2019-01-01&primary_release_date.lte=2019-03-23&region=IN&page=10&api_key=a1cbe7cb54feb5fb5cca45a3a0592d13
    @GET("discover/movie")
    Call<MoviesResponse> getNowInTheaterMoviesPage(
            @Query("region") String region,
            @Query("primary_release_date.gte")String minDate,
            @Query("primary_release_date.lte")String maxDate,
            @Query("page") int page,
            @Query("api_key") String apiKey
    );


    //https://api.themoviedb.org/3/discover/movie?&region=IN&release_date.gte=2019-03-22&page=3&api_key=a1cbe7cb54feb5fb5cca45a3a0592d13
    @GET("discover/movie")
    Call<MoviesResponse> getUpcomingMoviesPage(
            @Query("region") String region,
            @Query("release_date.gte")String minDate,
            @Query("page") int page,
            @Query("api_key") String apiKey);
}
