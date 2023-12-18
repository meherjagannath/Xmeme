package com.crio.starter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.crio.starter.App;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.exceptions.DuplicateMemeException;
import com.crio.starter.exchange.MemeRequestDto;
import com.crio.starter.exchange.MemeResponseDto;
import com.crio.starter.service.MemesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@SpringBootTest(classes = {App.class})
public class MemesControllerTest {

        @InjectMocks
        private MemesController memesController;
        @Mock
        private MemesService memesService;

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        @BeforeEach
        public void setup() {
                objectMapper = new ObjectMapper();

                mockMvc = MockMvcBuilders.standaloneSetup(memesController).build();
        }

        @Test
        @DisplayName("Test Get Memes Returns List of MemeResponseDto")
        void testGetMemes() throws Exception {
                MemeEntity memeEntity1 = new MemeEntity(1L, "name", "url", "caption", new Date());
                MemeEntity memeEntity2 = new MemeEntity(2L, "name", "url", "caption", new Date());
                when(memesService.getMemes()).thenReturn(List.of(memeEntity1, memeEntity2));
                MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                                .get("/memes")
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                                .getResponse();
                String json = response.getContentAsString();
                List<MemeResponseDto> expectedMemes =
                                List.of(new MemeResponseDto(1L, "name", "url", "caption"),
                                                new MemeResponseDto(2L, "name", "url", "caption"));
                List<MemeResponseDto> actualMemes = Arrays
                                .asList(objectMapper.readValue(json, MemeResponseDto[].class));
                assertEquals(expectedMemes, actualMemes);
        }

        @Test
        @DisplayName("Test Get Meme Returns MemeResponseDto With Respective Id")
        void testGetMeme() throws Exception {
                MemeEntity memeEntity = new MemeEntity(1L, "name", "url", "caption", new Date());
                when(memesService.getMeme(1L)).thenReturn(Optional.of(memeEntity));
                MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                                .get("/memes/{id}", 1L)
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                                .getResponse();
                String json = response.getContentAsString();
                MemeResponseDto expectedMeme = new MemeResponseDto(1L, "name", "url", "caption");
                MemeResponseDto actualMeme = objectMapper.readValue(json, MemeResponseDto.class);
                assertEquals(expectedMeme, actualMeme);
        }

        @Test
        @DisplayName("Test Add Meme If Successfull Returns Meme Id")
        void testAddMeme() throws Exception {
                MemeRequestDto memeRequestDto = new MemeRequestDto("name", "url", "caption");
                when(memesService.addMeme(any(MemeRequestDto.class))).thenReturn(1L);
                MockHttpServletResponse response = mockMvc
                                .perform(MockMvcRequestBuilders.post("/memes")
                                                .content(new ObjectMapper()
                                                                .writer()
                                                                .writeValueAsString(memeRequestDto))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                                .getResponse();
                String json = response.getContentAsString();
                Integer actualId = JsonPath.read(json,"$.id");
                assertEquals(1, actualId);
        }

        @Test
        @DisplayName("Test Add Duplicate Meme Sends Conflict Status Code")
        void testAddDuplicateMeme() throws Exception {
                MemeRequestDto memeRequestDto = new MemeRequestDto("name", "url", "caption");
                when(memesService.addMeme(any(MemeRequestDto.class)))
                                .thenThrow(DuplicateMemeException.class);
                mockMvc.perform(MockMvcRequestBuilders.post("/memes")
                                .content(new ObjectMapper().writer()
                                                .writeValueAsString(memeRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.status().isConflict());
        }

        @Test
        @DisplayName("Test Add Invalid Meme Sends Bad Request Status Code")
        void testAddInvalidMeme() throws Exception {
                MemeRequestDto memeRequestDto = new MemeRequestDto();
                mockMvc.perform(MockMvcRequestBuilders.post("/memes")
                                .content(new ObjectMapper().writer()
                                                .writeValueAsString(memeRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
}
