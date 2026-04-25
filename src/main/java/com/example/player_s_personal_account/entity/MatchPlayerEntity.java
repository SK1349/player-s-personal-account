package com.example.player_s_personal_account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_players",
        uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "user_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "match_result", nullable = false, length = 10)
    private String result;

    @Column(nullable = false)
    private Integer kills;

    @Column(nullable = false)
    private Integer deaths;

    @Column(name = "opponent_rating_snapshot")
    private Integer opponentRatingSnapshot;
}