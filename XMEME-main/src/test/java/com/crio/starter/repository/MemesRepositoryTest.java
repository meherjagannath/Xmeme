package com.crio.starter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.crio.starter.data.MemeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemesRepositoryTest {
    @Autowired
    MemesRepository memesRepository;
    @Test
    @DisplayName("Test Add Meme Adds MemeEntity To Repository")
    void testAddMeme(){
        MemeEntity memeEntity = new MemeEntity(10L, "name", "url", "caption", new Date());
        memesRepository.save(memeEntity);
        assertTrue(memesRepository.findById(10L).isPresent());
    }

    @Test
    @DisplayName("Test Get Meme Returns MemeEntity")
    void testGetMeme(){
        MemeEntity memeEntity = new MemeEntity(10L, "name", "url", "caption", new Date());
        memesRepository.save(memeEntity);
        assertTrue(memesRepository.findById(10L).isPresent());
    }

    @Test
    @DisplayName("Test Meme Exists Returns True If Meme Is Present")
    void testMemeExists(){
        MemeEntity memeEntity = new MemeEntity(10L, "name", "url", "caption", new Date());
        memesRepository.save(memeEntity);
        assertTrue(memesRepository.existsByNameAndUrlAndCaption("name", "url", "caption"));
    }

    @Test
    @DisplayName("Test Meme Exists Returns False If Meme Is Not Present")
    void testMemeDoesNotExists(){
        assertFalse(memesRepository.existsByNameAndUrlAndCaption("name", "url", "caption"));
    }

    @Test
    @DisplayName("Test Find Memes Returns List of Top 100 Recent Memes Added")
    void testGetRecent100Memes(){
        List<MemeEntity> memeEntities = new ArrayList<>();
        for(Long i=1L;i<110L;i++){
            memeEntities.add(new MemeEntity(i, "name"+i, "url"+i, "caption"+i, new Date()));
        }
        memesRepository.saveAll(memeEntities);
        int expectedSize=100;
        assertEquals(expectedSize, memesRepository.findTop100ByOrderByCreatedAtDesc().size());
        assertTrue(memesRepository.findById(10L).isPresent());
    }

    @AfterEach
    void removeDocs(){
        memesRepository.deleteAll();
    }
}
