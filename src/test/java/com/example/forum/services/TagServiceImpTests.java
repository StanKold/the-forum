package com.example.forum.services;

import com.example.forum.models.Tag;
import com.example.forum.models.TagFilterDto;
import com.example.forum.repositories.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forum.Helpers.createTag;
import static com.example.forum.Helpers.createTagFilter;

@ExtendWith(MockitoExtension.class)
public class TagServiceImpTests {

    @Mock
    TagRepository mockRepository;


    @InjectMocks
    TagServiceImpl mockService;

    @Test
    public void getTagByName_Should_CallRepository() {

        //Arrange
        Tag tag = createTag();

        // Act
        mockService.getTagByName(tag.getContent());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getTagByName(tag.getContent());
    }

    @Test
    public void getTag_CallRepository() {

        // Act
        mockService.getTag(1);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getTag(1);
    }

    @Test
    public void deleteTag_CallRepository() {

        // Act
        mockService.deleteTag(1);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .deleteTag(1);
    }


    @Test
    public void getTags_CallRepository() {
        TagFilterDto filter = createTagFilter();
        // Act
        mockService.getTags(filter);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getTags(filter);
    }


}
