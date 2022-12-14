package com.yourseason.backend.consulting.consultant.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsultingFinishRequest {

    private Long reservationId;
    private String tone;
    private String consultingComment;
    private List<String> bestColorSet;
    private List<String> worstColorSet;
}
