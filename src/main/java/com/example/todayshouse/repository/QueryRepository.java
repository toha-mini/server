package com.example.todayshouse.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryRepository {
    private final EntityManager entityManager;

    public Long findLikeCount(Long postId) {
        String query = "select count(l) from LikePost l left join l.post p where p.id = :postId";

        try {
            return (Long) entityManager.createQuery(query).setParameter("postId", postId).getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }

    }

    public Long findScrapCount(Long postId) {
        String query = "select count(l) from ScrapPost l left join l.post p where p.id = :postId";

        try {
            return (Long) entityManager.createQuery(query).setParameter("postId", postId).getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }

    }
}
