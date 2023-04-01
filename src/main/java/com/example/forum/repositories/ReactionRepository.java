package com.example.forum.repositories;

import com.example.forum.models.Reaction;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository {
    Reaction getReactionById(int id);

    void addReaction(Reaction reaction);

    void removeReaction(int dislikeID);

    Optional<Reaction> getReaction(int user_id, int post_id, String reaction);

    List<Integer> getLiked(int id);
    List<Integer> getDisliked(int id);

}
