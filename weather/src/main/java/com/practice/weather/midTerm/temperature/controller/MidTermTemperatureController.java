package com.practice.weather.midTerm.temperature.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.weather.midTerm.temperature.dto.MidTermTemperatureDto;
import com.practice.weather.midTerm.temperature.entity.MidTermTemperatureEntity;
import com.practice.weather.midTerm.temperature.repository.MidTermTemperatureRepository;
import com.practice.weather.midTerm.temperature.service.MidTermTemperatureService;
import com.practice.weather.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class MidTermTemperatureController {

    private final MidTermTemperatureService midTermTemperatureService;

    private final MidTermTemperatureRepository midTermTemperatureRepository;

    private final Utility utility;

    @Value("${service.key}")
    private String serviceKey;

    private ObjectMapper objectMapper;

    // Dependency Injection - 생성자 주입
    @Autowired
    public MidTermTemperatureController(
            MidTermTemperatureService midTermTemperatureService,
            MidTermTemperatureRepository midTermTemperatureRepository,
            ObjectMapper objectMapper,
            Utility utility
    ) {
        this.midTermTemperatureService = midTermTemperatureService;
        this.midTermTemperatureRepository = midTermTemperatureRepository;
        this.utility = utility;
        this.objectMapper = objectMapper.registerModule(new JavaTimeModule());
    }


    // 중기 기온 조회 실시간
    @Deprecated
    @GetMapping("/mid-term/temperature/current/{location}")
    private ResponseEntity<MidTermTemperatureDto> midTermTemperatureController(
            @PathVariable String location
    ) {

        // 현재 시간 기준 baseDate 와 baseTime 값 받아오기
        String baseDateTime = utility.getMidTermBaseDateTimeAsString();

        // 서비스 URL
        String urlStr = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" +
                "?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&dataType=JSON&regId=" +
                (location != null && !location.equals("") ? location : "11B10101") + "&tmFc=" + baseDateTime;

        // DTO 객체로 변환후 String 으로 파싱하여 return
        return ResponseEntity.ok(midTermTemperatureService.parseMapToMidTermTemperatureDto(
                utility.parseJsonArrayToMap(utility.getDataAsJsonArray(urlStr))));
    }


    // 중기 기온 조회 데이터 DB 저장
    @PostMapping("/mid-term/temperature/current")
    public ResponseEntity<MidTermTemperatureEntity> saveMidTermTemperature (
            @RequestBody String data
    ) {

        // 받아온 data JSONObject 로 파싱
        JSONObject jObject = new JSONObject(data);

        try {
            // 필요한 data 부분만 추출하여 DTO 로 파싱
            MidTermTemperatureDto midTermTemperatureDto = objectMapper.readValue(jObject.get("data").toString(), MidTermTemperatureDto.class);

            // 중복 확인 후 저장 & return
            if (!midTermTemperatureRepository.isExist(midTermTemperatureDto.getRegId(), utility.getMidTermBaseDateTimeAsLocalDateTime())) {
                return ResponseEntity.ok(midTermTemperatureRepository.save(midTermTemperatureDto.toEntity()));
            }

            // 중복된 데이터일 경우 빈 Entity return
            return ResponseEntity.ok(MidTermTemperatureEntity.builder().regId("").build());

        } catch (JsonProcessingException e) {
            log.error("[saveMidTermTemperature]JSON processing failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MidTermTemperatureEntity());
        }
    }


    // MidTermTemperatureEntity 의 list 를  return
    @GetMapping("/mid-term/temperature/list")
    public ResponseEntity<List<MidTermTemperatureEntity>> midTermTemperatureList (
            final Pageable pageable,
            @RequestParam(name = "location", required = false) String location
    )  {

        // location 이 파라미터로 전달될경우 location 별 데이터 return
        if (location == null || location.equals("")) {
            return ResponseEntity.ok(midTermTemperatureRepository.selectList(pageable));
        } else {
            return ResponseEntity.ok(midTermTemperatureRepository.selectListByLocation(pageable, location));
        }
    }


    // MidTermTemperature 의 총 갯수를 return
    @GetMapping("/mid-term/temperature/count")
    public ResponseEntity<String> midTermTemperatureCount (
            @RequestParam(name = "location", required = false) String location
    ) {
        long count;

        if (location == null || location.equals("")) {
            count = midTermTemperatureRepository.count();
        } else {
            count = midTermTemperatureRepository.countByLocation(location);
        }

        return ResponseEntity.ok("{\"count\": \"" + count + "\"}");
    }
    

    // 아이디로 데이터 조회
    @GetMapping("/mid-term/temperature/{id}")
    public ResponseEntity<MidTermTemperatureEntity> midTermTemperatureAllData (
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(midTermTemperatureRepository.selectById(id));
    }


    // MidTermTemperature 데이터 수정
    @PatchMapping("/mid-term/temperature/{id}")
    public ResponseEntity<MidTermTemperatureEntity> midTermTemperaturePatch (
            @PathVariable Long id,
            @RequestBody String data
    ) {
        try {

            //JSONObject 로 받아서 dto 객체로 변환
            JSONObject jObject = new JSONObject(data);

            MidTermTemperatureDto dto = objectMapper.readValue(jObject.get("data").toString(), MidTermTemperatureDto.class);

            // 수정 대상에 변경사항 적용
            MidTermTemperatureEntity entityToUpdate = midTermTemperatureRepository.selectById(id);

            entityToUpdate.updateFromDto(dto);

            midTermTemperatureRepository.save(entityToUpdate);

            return ResponseEntity.ok(entityToUpdate);

        } catch (JsonProcessingException e) {
            log.error("[midTermTemperaturePatch]JSON processing failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MidTermTemperatureEntity());
        } catch (Exception e) {
            log.error("[midTermTemperaturePatch]Exception Occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MidTermTemperatureEntity());
        }
    }


    @DeleteMapping("/mid-term/temperature/{id}")
    public ResponseEntity<String> midTermTemperatureDelete (
            @PathVariable Long id
    ) {

        // ID로 데이터 조회 안될 시 Not Found return
        if (!midTermTemperatureRepository.existsById(id)) {
            log.error("[midTermTemperatureDelete] Delete failed: Data not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": \"Data not found.\"}");
        }

        try {
            // 삭제 성공시 삭제된 데이터의 id return
            midTermTemperatureRepository.deleteById(id);
            return ResponseEntity.ok("{\"result\": \"" + id + "\"}");
        } catch (Exception e) {
            log.error("[midTermTemperatureDelete] Exception occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": \"Exception occurred.\"}");
        }
    }
    
}
