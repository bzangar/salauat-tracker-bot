package org.example.user;

public record UserRankingDto(
        String username,
        Long totalCount,
        int rank
) {}
