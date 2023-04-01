package com.example.forum.services;

import com.example.forum.models.Reaction;
import com.example.forum.models.User;

import java.util.List;
import java.util.Optional;

public interface ReactionService {

    void addReaction(int postId, String reaction, User user);

    void removeReaction(int dislikeID);

    Optional<Reaction> getReaction(int user_id, int post_id, String reaction);

    List<Integer> getLiked(int id);
    List<Integer> getDisliked(int id);
}
