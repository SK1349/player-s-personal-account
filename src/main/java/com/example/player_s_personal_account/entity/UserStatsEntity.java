package com.example.player_s_personal_account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_stats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsEntity {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "matches_played")
    private Integer matchesPlayed = 0;

    @Column(name = "wins")
    private Integer wins = 0;

    @Column(name = "losses")
    private Integer losses = 0;

    @Column(name = "draws")
    private Integer draws = 0;

    @Column(name = "total_kills")
    private Integer totalKills = 0;

    @Column(name = "total_deaths")
    private Integer totalDeaths = 0;

    @Column(name = "last_processed_match_id")
    private Long lastProcessedMatchId;
}