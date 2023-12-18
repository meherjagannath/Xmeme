package com.crio.starter.repository;

import java.util.List;
import java.util.Optional;
import com.crio.starter.data.MemeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemesRepository extends MongoRepository<MemeEntity,Long> {
    Optional<MemeEntity> findById(Long id);
    List<MemeEntity> findTop100ByOrderByCreatedAtDesc();
    boolean existsByNameAndUrlAndCaption(String name,String url,String caption);
}
