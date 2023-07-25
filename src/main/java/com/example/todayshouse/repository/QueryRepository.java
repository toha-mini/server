package com.example.todayshouse.repository;

import com.example.todayshouse.domain.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryRepository {
    private final EntityManager entityManager;

    public Long findLikeCount(Long postId) {
        String query = "select count(l) from LikePost l where l.post.id = :postId";

        try {
            return (Long) entityManager.createQuery(query).setParameter("postId", postId).getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }

    }

    public Long findScrapCount(Long postId) {
        String query = "select count(l) from ScrapPost l where l.post.id = :postId";

        try {
            return (Long) entityManager.createQuery(query).setParameter("postId", postId).getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }

    }

    public List<Post> findPostsSortedByLikeCount() {
        String query = "select p from Post p left join LikePost l On p.id = l.post.id"
                + " group by p.id order by count(l) DESC";
        try {
            return entityManager.createQuery(query, Post.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
