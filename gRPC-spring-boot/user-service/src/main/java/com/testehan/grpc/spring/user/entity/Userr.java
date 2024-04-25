package com.testehan.grpc.spring.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class Userr {

    @Id
    private String login;
    private String name;
    private String genre;


}
