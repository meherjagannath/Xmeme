package com.crio.starter.service;

import java.util.List;
import java.util.Optional;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.exchange.MemeRequestDto;

public interface MemesService {
    public List<MemeEntity> getMemes();

    public Long addMeme(MemeRequestDto memeDto);

    public Optional<MemeEntity> getMeme(Long memeId);
}
