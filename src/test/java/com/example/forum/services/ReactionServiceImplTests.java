package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction;
import com.example.forum.models.User;
import com.example.forum.repositories.PostRepository;
import com.example.forum.repositories.ReactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class ReactionServiceImplTests {
    @Mock
    ReactionRepository mockReactRepository;

    @Mock
    PostRepository postMockRepository;


    @InjectMocks
    ReactionServiceImpl service;

    @Test
    public void addReaction_Should_ThrowException_when_UserBlocked(){
            User mockUser = createMockUser();
            mockUser.setBlocked(true);

            Assertions.assertThrows(
                    AuthorizationException.class,
                    () -> service.addReaction(1,"finger", mockUser));

        }

    @Test
    public void addReaction_Should_CallRemove_when_DuplicateRaction(){
        User mockUser = createMockUser();
        Post mockPost = createMockPost();
       Optional< Reaction> mockReaction = Optional.of(createMockReaction());

        Mockito.when(postMockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);


        Mockito.when(mockReactRepository.getReaction(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
                .thenReturn(mockReaction);


        service.addReaction(1,"finger", mockUser);
        Mockito.verify(mockReactRepository, Mockito.times(1)).removeReaction(Mockito.anyInt());

    }

    @Test
    public void addReaction_Should_CallAdd_when_RactionUnique(){
        User mockUser = createMockUser();
        Post mockPost = createMockPost();
        Optional<Reaction> mockReaction= Optional.empty();


        Mockito.when(postMockRepository.getPost(Mockito.anyInt())).thenReturn(mockPost);


        Mockito.when(mockReactRepository.getReaction(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
                .thenReturn(mockReaction);


        service.addReaction(1,"finger", mockUser);
        Mockito.verify(mockReactRepository, Mockito.times(1)).addReaction(Mockito.any());
    }



    @Test
    public void addReaction_Should_CallRemove_when_NegativeRaction(){
        User mockUser = createMockUser();
        mockUser.setId(1);
        Post mockPost = createMockPost();
        Optional<Reaction> mockReaction= Optional.empty();
        Reaction reaction = new Reaction();
        reaction.setId(1);
        Optional<Reaction> nreaction= Optional.of(reaction);



        Mockito.when(postMockRepository.getPost(1)).thenReturn(mockPost);


        Mockito.when(mockReactRepository.getReaction(1,1,"like"))
                .thenReturn(mockReaction);
        Mockito.when(mockReactRepository.getReaction(1,1,"dislike"))
                .thenReturn(nreaction);



        service.addReaction(1,"like", mockUser);
        Mockito.verify(mockReactRepository, Mockito.times(1)).removeReaction(Mockito.anyInt());
    }



    @Test
    public void getLiked_CallRepository() {
        service.getLiked(1);
        Mockito.verify(mockReactRepository, Mockito.times(1))
                .getLiked(1);
    }

    @Test
    public void getDisliked_CallRepository() {
        service.getDisliked(1);
        Mockito.verify(mockReactRepository, Mockito.times(1))
                .getDisliked(1);
    }

    @Test
    public void getReaction_CallRepository() {
        service.getReaction(1,1,"test");
        Mockito.verify(mockReactRepository, Mockito.times(1))
                .getReaction(1,1,"test");
    }



}

