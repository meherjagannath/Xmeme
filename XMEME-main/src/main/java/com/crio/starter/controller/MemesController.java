package com.crio.starter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.exceptions.DuplicateMemeException;
import com.crio.starter.exchange.MemeRequestDto;
import com.crio.starter.exchange.MemeResponseDto;
import com.crio.starter.service.MemesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemesController {

    @Autowired
    private MemesService memesService;

    @GetMapping("/memes")
    public ResponseEntity<List<MemeResponseDto>> getMemes() {
        try {
            return ResponseEntity.ok(memesService.getMemes().stream()
            .map(memeEntity -> new MemeResponseDto(memeEntity))
            .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/memes/{id}")
    public ResponseEntity<MemeResponseDto> getMeme(@PathVariable("id") Long memeId) {
        try {
            Optional<MemeEntity> memeEntity = memesService.getMeme(memeId);
            if (memeEntity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new MemeResponseDto(memeEntity.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/memes")
    public ResponseEntity<Map<String, Long>> addMeme(@RequestBody MemeRequestDto memeDetails) {
        if (isInvalidMemeDetails(memeDetails)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            Map<String, Long> response = new HashMap<>();
            response.put("id", memesService.addMeme(memeDetails));
            return ResponseEntity.ok(response);
        } catch (DuplicateMemeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isInvalidMemeDetails(MemeRequestDto memeRequestDto) {
        return memeRequestDto.getName() == null || memeRequestDto.getUrl() == null
                || memeRequestDto.getCaption() == null;
    }

}
