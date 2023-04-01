package com.example.forum.repositories;


import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.PostFilterDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository

public class PostRepositoryImpl implements PostRepository {
    private final SessionFactory sessionFactory;



    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Post> getPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.list();
        }
    }

    

 @Override
 public Post getPost(int id) {
     try (Session session = sessionFactory.openSession()) {
         Post result = session.get(Post.class, id);
         if (result == null) {
             throw new EntityNotFoundException("Post", "id", String.valueOf(id));
         }
         return result;
     }

 }

    @Override
    public List<Post> getUserPosts(int user_id, Optional<String> title,
                                   Optional<String> content, Optional<String> sort) {

        try {

            StringBuilder queryString = new StringBuilder(" from Post where user_id = :userId ");

            if (title.isPresent() || content.isPresent()) {
                queryString.append(" and ");
            }
            if (title.isPresent()) {
                queryString.append(" title like :title ");
            }

            if (content.isPresent()) {
                queryString.append(title.isPresent() ? " and " : "");
                queryString.append(" content like :content ");
            }

            sort.ifPresent(s -> queryString.append(generateStringFromSort(s)));

            try (Session session = sessionFactory.openSession()) {
                Query<Post> query = session.createQuery(queryString.toString(), Post.class);

                query.setParameter("userId", user_id);

                title.ifPresent(s -> query.setParameter("title", "%" + s + "%"));

                content.ifPresent(s -> query.setParameter("content", "%" + s + "%"));

                return query.list();
            }
        } catch (EntityNotFoundException e) {
            return new ArrayList<>();
        }
    }


    private String generateStringFromSort(String value) {
        StringBuilder queryString = new StringBuilder(" order by ");
        String[] params = value.split("_");

        if (value.isEmpty()) {
            return "";
        }

        switch (params[0]) {
            case "title" -> queryString.append(" title ");
            case "content" -> queryString.append(" content ");
            case "likes" -> queryString.append(" likes ");
            case "dislikes" -> queryString.append(" dislikes ");
            default -> throw new UnsupportedOperationException(
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
    public Post createPost(Post post) {
        post.setCreatedOn(LocalDate.now());
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
        return post;

    }

    @Override
    public void delete(int id) {
        Post toDelete = getPost(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(toDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public Post update(int post_id, Post post) {
        post.setPost_id(post_id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(post);
            session.getTransaction().commit();
        }
        return post;
    }

    @Override
    public List<Post> getTopCommentedPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("SELECT p FROM Post p " +
                    "JOIN p.comments c GROUP BY p ORDER BY COUNT(c) DESC",Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public Boolean existTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title like :title", Post.class);
            query.setParameter("title", title);

            List<Post> posts = query.list();
            if (posts.size() == 0) {
                return false;
            }

            }
        return true;
    }

    @Override
    public List<Post> getMostRecentPosts() {
        try (Session session = sessionFactory.openSession()){
            Query<Post> postQuery = session.createQuery("select p from Post p order by p.createdOn desc ",Post.class);
            postQuery.setMaxResults(10);
            return postQuery.list();
        }
    }

    @Override
    public List<Post> searchPost(Optional<String> title, Optional<String> tag) {
            StringBuilder queryString = new StringBuilder(" from Post p JOIN p.tags t ");

            if (title.isPresent() || tag.isPresent()) {
                queryString.append(" where ");
            }
            if (title.isPresent()) {
                queryString.append(" p.title like :title or p.content like :title ");
            }

            if (tag.isPresent()) {
                queryString.append(title.isPresent() ? " and " : "");
                queryString.append(" t.content like :tag");
            }

            try (Session session = sessionFactory.openSession()) {
                Query<Post> query = session.createQuery(queryString.toString(),Post.class);

                title.ifPresent(s -> query.setParameter("title", "%" + s + "%"));

                tag.ifPresent(s -> query.setParameter("tag", "%" + s + "%"));

                return query.list();
            } catch (EntityNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select count(*) from Post");
                return (long)query.uniqueResult();
            }

    }

    @Override
    public List<Post> getAllPosts(PostFilterDto postFilterDto) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

           if(postFilterDto.getAuthorID()!=0 ){
                    filters.add(" p.user_id =:userID");
                    params.put("userID",  postFilterDto.getAuthorID());
            }
           if(postFilterDto.getTag()!=null && !postFilterDto.getTag().isEmpty()) {
               filters.add(" t.content like :tag");
               params.put("tag", String.format("%%%s%%", postFilterDto.getTag()));
           }
           if(postFilterDto.getContains()!=null && !postFilterDto.getContains().isEmpty()){
                filters.add(" p.title like :title or p.content like :title1");
                params.put("title", String.format("%%%s%%", postFilterDto.getContains()));
                params.put("title1", String.format("%%%s%%", postFilterDto.getContains()));
            }

            StringBuilder queryString = new StringBuilder("select p from Post p left join p.tags t ");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(postFilterDto));

            Query<Post> queryFilter = session.createQuery(queryString.toString(), Post.class);
            queryFilter.setProperties(params);

            return queryFilter.list();
         }
    }

        private String generateOrderBy(PostFilterDto postFilterDto) {
            if (postFilterDto.getSortBy()==null || postFilterDto.getSortBy().isEmpty()) {
                return "";
            }

            String orderBy;
            switch (postFilterDto.getSortBy()) {
                case "date" -> orderBy = "p.createdOn";
                case "title" -> orderBy = "p.title";
                case "likes" -> orderBy = "p.likes";
                case "dislikes" -> orderBy = "p.dislikes";
                case "comments" -> orderBy = "size(p.comments)";
                default -> {
                    return "";
                }
            }

            orderBy = String.format(" order by %s", orderBy);

            if (!postFilterDto.getSortOrder().isEmpty() && postFilterDto.getSortOrder().equalsIgnoreCase("desc")) {
                orderBy = String.format("%s desc", orderBy);
            }

            return orderBy;
        }

}
