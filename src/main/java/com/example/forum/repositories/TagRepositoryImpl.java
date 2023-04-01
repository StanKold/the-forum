package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Tag;
import com.example.forum.models.TagFilterDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag getTagByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("FROM Tag WHERE content Like :name", Tag.class);
            query.setParameter("name", name);

            List<Tag> result = query.list();
            if (result.size() == 0) {
               return addTag(name);
            }
            return result.get(0);
        }
    }

    @Override
    public Tag addTag(String name) {
        Tag tag = new Tag();
        tag.setContent(name);
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();

            return getTagByName(name);
        }

    }

    @Override
    public Tag getTag(int tagId) {
        try (Session session = sessionFactory.openSession()) {
            Tag tag = session.get(Tag.class, tagId);
            if (tag == null) {
                throw new EntityNotFoundException("Tag", "id", String.valueOf(tagId));
            }
            return tag;
        }
    }

    @Override
    public List<Tag> getTags(TagFilterDto tagFilterDto) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();


            if(tagFilterDto.getContent()!=null && !tagFilterDto.getContent().isEmpty()){
                filters.add(" content like :text ");
                params.put("text", "%"+ tagFilterDto.getContent()+"%");
            }



            StringBuilder queryString = new StringBuilder(" from Tag ");

            if(!filters.isEmpty()){
                queryString.append(" where ").append(String.join(" and ",filters));
            }

            queryString.append(generateOrderBy(tagFilterDto));

            Query<Tag> queryFilter = session.createQuery(queryString.toString(), Tag.class);
            queryFilter.setProperties(params);

            return queryFilter.list();
        }
    }

    @Override
    public void deleteTag(int id) {
        Tag tag = getTag(id);
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(tag);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(TagFilterDto tagFilterDto) {
        if (tagFilterDto.getSortBy()==null || tagFilterDto.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy;
        switch (tagFilterDto.getSortBy()) {
            case "order" -> orderBy = "tagId";
            case "content" -> orderBy = "content";
            default -> {
                return "";
            }
        }

        orderBy = String.format(" order by %s", orderBy);

        if (!tagFilterDto.getSortOrder().isEmpty() && tagFilterDto.getSortOrder().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }


}



