package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.CommentsFilterDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Comment> getComments() {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> commentQuery = session.createQuery("from Comment ", Comment.class);
            return commentQuery.list();
        }
    }

    @Override
    public Comment getComment(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);

            if (comment == null)
                throw new EntityNotFoundException("Comment", "ID", String.valueOf(id));
            return comment;
        }
    }

    @Override
    public List<Comment> getComments(String username, Optional<String> content, Optional<String> sort) {

        StringBuilder queryString = new StringBuilder(" from Comment where creator.username = :username ");

        if (content.isPresent()) {
            queryString.append(" and ");
            queryString.append(" content like :content");
        }

        sort.ifPresent(s -> queryString.append(generateStringFromSort(s)));

        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(queryString.toString());

            query.setParameter("username", username);

            content.ifPresent(s -> query.setParameter("content", "%" + s + "%"));

            return query.list();
        }
    }

    private String generateStringFromSort(String value) {
        StringBuilder queryString = new StringBuilder(" order by ");
        String[] params = value.split("_");

        if (value.isEmpty()) {
            return "";
        }

        if (params[0].equals("content")) {
            queryString.append(" content ");
        } else {
            throw new UnsupportedOperationException(
                    "Sort should have max two params divided by _ symbol!");
        }

        if (params.length > 2) {
            throw new UnsupportedOperationException(
                    "Sort should have max two params divided by _ symbol!");
        }

        if (params.length == 2 && params[1].equalsIgnoreCase("desc")) {
            queryString.append(" desc ");
        }


        return queryString.toString();
    }

    @Override
    public List<Comment> getCommentsByPostTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> commentQuery = session.createQuery("from Comment where " +
                    "post.title = :title", Comment.class);
            commentQuery.setParameter("title", title);
            return commentQuery.list();
        }

    }

    @Override
    public List<Comment> getCommentsByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> commentQuery = session.createQuery("from Comment where " +
                    "post.post_id = :post_id", Comment.class);
            commentQuery.setParameter("post_id", postId);
            return commentQuery.list();
        }
    }

    @Override
    public Comment createComment(Comment comment) {
        comment.setCreatedOn(LocalDate.now());
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
        return comment;
    }

    @Override
    public void delete(int id) {
        Comment commentToDelete = getComment(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public Comment update(int id, Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
        return getComment(id);
    }

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery(" select count (*) from Comment");
            return (long) query.uniqueResult();
        }
    }

    @Override
    public List<Comment> getFilteredComments(CommentsFilterDto commentsFilterDto) {

        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();


            if(commentsFilterDto.getAuthorId()!=0)
                filters.add(" creator  = :user ");
            params.put("user",  commentsFilterDto.getAuthorId());

            if(commentsFilterDto.getPostTitle()!=null && !commentsFilterDto.getPostTitle().isEmpty())
                filters.add(" post.title like :text ");
            params.put("text", "%"+ commentsFilterDto.getPostTitle()+"%");

            if(commentsFilterDto.getContent()!=null && !commentsFilterDto.getContent().isEmpty())
                filters.add(" content like :text2 ");
            params.put("text2", "%"+ commentsFilterDto.getContent()+"%");


        StringBuilder queryString = new StringBuilder(" from Comment ");

        if(!filters.isEmpty()){
            queryString.append(" where ").append(String.join(" and ",filters));
        }

            queryString.append(generateOrderBy(commentsFilterDto));

            Query<Comment> queryFilter = session.createQuery(queryString.toString(), Comment.class);
            queryFilter.setProperties(params);

            return queryFilter.list();
        }

    }
    private String generateOrderBy(CommentsFilterDto commentsFilterDto) {
        if (commentsFilterDto.getSortBy()==null || commentsFilterDto.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy;
        switch (commentsFilterDto.getSortBy()) {
            case "order" -> orderBy = "replyId";
            case "content" -> orderBy = "content";
            case "user" -> orderBy = "creator.username";
            default -> {
                return "";
            }
        }

        orderBy = String.format(" order by %s", orderBy);

        if (!commentsFilterDto.getSortOrder().isEmpty() && commentsFilterDto.getSortOrder().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }


}