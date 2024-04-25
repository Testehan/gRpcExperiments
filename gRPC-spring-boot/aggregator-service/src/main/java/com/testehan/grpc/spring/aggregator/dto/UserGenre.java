package com.testehan.grpc.spring.aggregator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGenre {

    private String loginId;
    private String genre;

}
