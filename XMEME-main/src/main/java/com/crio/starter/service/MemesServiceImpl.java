package com.crio.starter.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.crio.starter.data.MemeEntity;
import com.crio.starter.exceptions.DuplicateMemeException;
import com.crio.starter.exchange.MemeRequestDto;
import com.crio.starter.repository.MemesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemesServiceImpl implements MemesService {

    @Autowired
    private MemesRepository memesRepository;

    @Override
    public List<MemeEntity> getMemes() {
        return memesRepository.findTop100ByOrderByCreatedAtDesc();
    }

    @Override
    public Long addMeme(MemeRequestDto memeDto) {
        if(memesRepository.existsByNameAndUrlAndCaption(memeDto.getName(), memeDto.getUrl(), memeDto.getCaption())){
            throw new DuplicateMemeException();
        }
        return memesRepository.save(new MemeEntity(generateMemeId(), memeDto.getName(),memeDto.getUrl(),memeDto.getCaption(),new Date())).getId();
    }

    @Override
    public Optional<MemeEntity> getMeme(Long memeId) {
        return memesRepository.findById(memeId);
    }

    private Long generateMemeId(){
        return memesRepository.count()+1L;
    }

}
