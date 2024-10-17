package com.hhplus.server.domain.concert.model;

import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Concert extends BaseEntity {
    @Id
    @Column(name = "concert_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;

    @Column(name = "concert_name")
    private String concertName;

}
