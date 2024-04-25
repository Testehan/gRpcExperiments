package com.testehan.grpc.spring.aggregator.controller;

import com.testehan.grpc.spring.aggregator.dto.RecommendedMovie;
import com.testehan.grpc.spring.aggregator.dto.UserGenre;
import com.testehan.grpc.spring.aggregator.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMovies(@PathVariable String loginId){
        return this.userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user")
    public UserGenre setUserGenre(@RequestBody UserGenre userGenre){
        return this.userMovieService.setUserGenre(userGenre);
    }

}
