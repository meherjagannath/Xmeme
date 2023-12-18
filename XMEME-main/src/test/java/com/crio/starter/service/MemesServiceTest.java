package com.crio.starter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.exceptions.DuplicateMemeException;
import com.crio.starter.exchange.MemeRequestDto;
import com.crio.starter.repository.MemesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemesServiceTest {
    @Mock
    private MemesRepository memesRepository;

    @InjectMocks
    private MemesServiceImpl memesService;

    @Test
    @DisplayName("Test Add Meme Returns MemeId")
    void testAddMeme(){
        MemeEntity memeEntity = new MemeEntity(6L, "name", "url", "caption", new Date());
        when(memesRepository.existsByNameAndUrlAndCaption("name", "url", "caption")).thenReturn(false);
        when(memesRepository.count()).thenReturn(5L);
        when(memesRepository.save(any(MemeEntity.class))).thenReturn(memeEntity);
        Long actualMemeEntityId = memesService.addMeme(new MemeRequestDto("name", "url", "caption"));
        assertEquals(6L, actualMemeEntityId);
    }

    @Test
    @DisplayName("Test Add Meme Throws DuplicateMemeException If Meme With Same Name,Url,Caption Exists")
    void testAddDuplicateMeme(){
        when(memesRepository.existsByNameAndUrlAndCaption("name", "url", "caption")).thenReturn(true);
        assertThrows(DuplicateMemeException.class, ()->{
            memesService.addMeme(new MemeRequestDto("name", "url", "caption"));
        });
    }

    @Test
    @DisplayName("Test Get Meme Returns Meme With Respective Id")
    void testGetMeme(){
        MemeEntity expectedMeme = new MemeEntity(6L, "name", "url", "caption", new Date());
        when(memesRepository.findById(6L)).thenReturn(Optional.of(expectedMeme));
        assertEquals(expectedMeme, memesService.getMeme(6L).get());
    }

    @Test
    @DisplayName("Test Get Memes Returns 100 Recent Memes Added")
    void testGetMemes(){
        List<MemeEntity> expectedMemes = List.of(new MemeEntity(1L, "name", "url", "caption", new Date()),
        new MemeEntity(2L, "name", "url", "caption", new Date()));
        when(memesRepository.findTop100ByOrderByCreatedAtDesc()).thenReturn(expectedMemes);
        assertEquals(expectedMemes, memesService.getMemes());
    }
}
