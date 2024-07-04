package com.sfz.rest_api.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "vote_options")
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "title", columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(name = "count", nullable = false)
    private Long count;

    // Getters and setters
}
