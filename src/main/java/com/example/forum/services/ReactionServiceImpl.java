package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction;
import com.example.forum.models.User;
import com.example.forum.repositories.PostRepository;
import com.example.forum.repositories.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;

    private final PostRepository postRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository, PostRepository postRepository) {
        this.reactionRepository = reactionRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void addReaction(int postId, String reaction, User loggedUser) {
        if (loggedUser.isBlocked()) {
            throw new AuthorizationException("Banned users can't react on posts");
        }

        String negative;
        if(reaction.equals("like")){
            negative="dislike";
        }else{
            negative="like";
        }

        Post post = postRepository.getPost(postId);
        Optional<Reaction> react = reactionRepository.getReaction(loggedUser.getId(), postId, reaction);
        Optional<Reaction> nreaction = reactionRepository.getReaction(loggedUser.getId(), postId, negative);


        if (react.isPresent()) {
            Reaction reactionToRemove = react.get();
            removeReaction(reactionToRemove.getId());
        }else if(nreaction.isPresent()){
            Reaction reactionToRemove = nreaction.get();
            removeReaction(reactionToRemove.getId());
            Reaction reactionToAdd = new Reaction();
            reactionToAdd.setUser(loggedUser.getId());
            reactionToAdd.setPost(post.getPost_id());
            reactionToAdd.setReaction(reaction);
            reactionRepository.addReaction(reactionToAdd);
        } else {
            Reaction reactionToAdd = new Reaction();
            reactionToAdd.setUser(loggedUser.getId());
            reactionToAdd.setPost(post.getPost_id());
            reactionToAdd.setReaction(reaction);
            reactionRepository.addReaction(reactionToAdd);
        }
    }

    @Override
    public void removeReaction(int dislikeID) {
        reactionRepository.removeReaction(dislikeID);
    }

    @Override
    public Optional<Reaction> getReaction(int user_id, int post_id, String reaction) {
        return reactionRepository.getReaction(user_id,post_id,reaction);
    }

    @Override
    public List<Integer> getLiked(int id) {
        return reactionRepository.getLiked(id);
    }

    @Override
    public List<Integer> getDisliked(int id) {
        return reactionRepository.getDisliked(id);
    }
}
