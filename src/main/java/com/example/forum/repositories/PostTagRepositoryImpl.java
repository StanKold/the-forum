package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.PostTags;
import com.example.forum.models.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostTagRepositoryImpl implements PostTagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostTagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addPostTag(Post post, Tag tag) {
         PostTags postTag = new PostTags();
         postTag.setPostId(post.getPost_id());
         postTag.setTagId(tag.getID());

         try(Session session = sessionFactory.openSession()){
             session.beginTransaction();
             session.persist(postTag);
             session.getTransaction().commit();
         }

    }
    @Override
    public void removePostTag(Post post, Tag tag) {
        PostTags postTag;

        try (Session session = sessionFactory.openSession()) {
            Query<PostTags> query = session.createQuery("from PostTags" +
                    " where tagId= :tagId and postId = :postId", PostTags.class);
            query.setParameter("postId", post.getPost_id());
            query.setParameter("tagId", tag.getID());

            List<PostTags> result= query.list();
            if(result.size()==0){
            throw new EntityNotFoundException("PostTag","tag", tag.getContent()+" for post "+post.getPost_id());
            }
            postTag=result.get(0);
        }

        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(postTag);
            session.getTransaction().commit();
        }

    }

    @Override
    public List<String> getAllPostTags(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("select t from PostTags p, Tag t " +
                                                          " where p.tagId= t.tagId and p.postId = :postId", Tag.class);
            query.setParameter("postId", postId);

            return query.list().stream().map(Tag::toString).collect(Collectors.toList());
        }
    }
}



