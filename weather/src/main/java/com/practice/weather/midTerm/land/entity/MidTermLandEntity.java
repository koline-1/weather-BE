package com.practice.weather.midTerm.land.entity;

import com.practice.weather.main.entity.BaseEntity;
import com.practice.weather.midTerm.land.dto.MidTermLandDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mid_term_land")
@Entity(name = "MidTermLandEntity")
public class MidTermLandEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MID_TERM_LAND_ID_GENERATOR")
    private long id;

    @Column(name = "REG_ID")
    private String regId;

    @Column
    private String rnSt3Am;

    @Column
    private String rnSt3Pm;

    @Column
    private String rnSt4Am;

    @Column
    private String rnSt4Pm;

    @Column
    private String rnSt5Am;

    @Column
    private String rnSt5Pm;

    @Column
    private String rnSt6Am;

    @Column
    private String rnSt6Pm;

    @Column
    private String rnSt7Am;

    @Column
    private String rnSt7Pm;

    @Column
    private String rnSt8;

    @Column
    private String rnSt9;

    @Column
    private String rnSt10;

    @Column
    private String wf3Am;

    @Column
    private String wf3Pm;

    @Column
    private String wf4Am;

    @Column
    private String wf4Pm;

    @Column
    private String wf5Am;

    @Column
    private String wf5Pm;

    @Column
    private String wf6Am;

    @Column
    private String wf6Pm;

    @Column
    private String wf7Am;

    @Column
    private String wf7Pm;

    @Column
    private String wf8;

    @Column
    private String wf9;

    @Column
    private String wf10;
    public MidTermLandDto toDto() {
        return MidTermLandDto.builder().id(id).regId(regId).rnSt3Am(rnSt3Am).rnSt3Pm(rnSt3Pm).rnSt4Am(rnSt4Am).rnSt4Pm(rnSt4Pm)
                .rnSt5Am(rnSt5Am).rnSt5Pm(rnSt5Pm).rnSt6Am(rnSt6Am).rnSt6Pm(rnSt6Pm).rnSt7Am(rnSt7Am).rnSt7Pm(rnSt7Pm)
                .rnSt8(rnSt8).rnSt9(rnSt9).rnSt10(rnSt10).wf3Am(wf3Am).wf3Pm(wf3Pm).wf4Am(wf4Am).wf4Pm(wf4Pm)
                .wf5Am(wf5Am).wf5Pm(wf5Pm).wf6Am(wf6Am).wf6Pm(wf6Pm).wf7Am(wf7Am).wf7Pm(wf7Pm).wf8(wf8).wf9(wf9).wf10(wf10).build();
    }


}
