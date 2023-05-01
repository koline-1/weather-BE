package com.practice.weather.midTerm.expectation.dto;

import com.practice.weather.midTerm.expectation.entity.MidTermExpectationEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MidTermExpectationDto {

    private long id;

    private String stnId;

    private String wfSv;

    private LocalDateTime date;

    public MidTermExpectationEntity toEntity() {
        return MidTermExpectationEntity.builder().stnId(stnId).wfSv(wfSv).build();
    }
    
}