package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Reaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReactionRepositoryImpl implements ReactionRepository {

    private final SessionFactory sessionFactory;

    public ReactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Reaction getReactionById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Reaction reaction = session.get(Reaction.class, id);

            if (reaction == null)
                throw new EntityNotFoundException("Dislike", "id", String.valueOf(id));

            return reaction;
        }
    }

    @Override
    public void addReaction(Reaction reaction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(reaction);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeReaction(int dislikeID) {
        Reaction reaction = getReactionById(dislikeID);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(reaction);
            session.getTransaction().commit();
        }
    }

    @Override
    public Optional<Reaction> getReaction(int user_id, int post_id, String reaction) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reaction> query = session.createQuery(
                    "FROM Reaction d WHERE d.userID = :userId AND d.postID = :postId and d.reaction = :reaction",
                    Reaction.class);
            query.setParameter("userId", user_id);
            query.setParameter("postId", post_id);
            query.setParameter("reaction", reaction);
            List<Reaction> reactions = query.list();
            return reactions.isEmpty() ? Optional.empty() : Optional.of(reactions.get(0));
        }
    }

    @Override
    public List<Integer> getLiked(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Object> query = session.createQuery(
                    "SELECT userID FROM Reaction  WHERE postID=:postId and reaction like 'like' ", Object.class);
            query.setParameter("postId", postId);

            return query.list().stream().map(o->(int)o).collect(Collectors.toList());
        }

    }

    @Override
    public List<Integer> getDisliked(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Object> query = session.createQuery(
                    "SELECT userID FROM Reaction  WHERE postID=:postId and  reaction like 'dislike' ", Object.class);
            query.setParameter("postId", postId);

            return query.list().stream().map(o->(int)o).collect(Collectors.toList());
        }


    }
}
