package com.testehan.grpc.spring.aggregator.service;

import com.testehan.grpc.spring.aggregator.dto.RecommendedMovie;
import com.testehan.grpc.spring.aggregator.dto.UserGenre;
import com.testehan.grpc.spring.common.Genre;
import com.testehan.grpc.spring.movie.MovieSearchRequest;
import com.testehan.grpc.spring.movie.MovieSearchResponse;
import com.testehan.grpc.spring.movie.MovieServiceGrpc;
import com.testehan.grpc.spring.user.UserGenreUpdateRequest;
import com.testehan.grpc.spring.user.UserResponse;
import com.testehan.grpc.spring.user.UserSearchRequest;
import com.testehan.grpc.spring.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestions(String loginId){
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse = this.userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = this.movieStub.getMovies(movieSearchRequest);

        return movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto -> new RecommendedMovie(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating()))
                .collect(Collectors.toList());
    }

    public UserGenre setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();

        UserResponse userResponse = this.userStub.updateUserGenre(userGenreUpdateRequest);
        var userGenreUpdated = UserGenre.builder()
                .genre(userResponse.getGenre().toString())
                .loginId(userResponse.getLoginId()).build();
        return userGenreUpdated;
    }


}
