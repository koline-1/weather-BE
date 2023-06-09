package com.practice.weather.shortTerm.expectation.service;

import com.practice.weather.shortTerm.expectation.dto.ShortTermExpectationDto;
import org.json.JSONArray;

import java.util.List;

public interface ShortTermExpectationService {

    // Map 형태의 데이터를 DTO 객체 List 로 변환
    List<ShortTermExpectationDto> parseJsonArrayToShortTermExpectationDto(JSONArray jArray, String version);
    
}
