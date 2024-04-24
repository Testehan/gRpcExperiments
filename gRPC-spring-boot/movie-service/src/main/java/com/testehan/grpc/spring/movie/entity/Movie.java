package com.testehan.grpc.spring.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class Movie {

    @Id
    private int id;
    private String title;

    @Column(name="release_year")
    private int year;

    private double rating;
    private String genre;

}
