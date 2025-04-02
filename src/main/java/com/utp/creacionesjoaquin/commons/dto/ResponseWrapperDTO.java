package com.utp.creacionesjoaquin.commons.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapperDTO<T>{
    private Boolean success;
    private String message;
    private String status;
    private T content;
}
