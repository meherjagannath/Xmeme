package com.crio.starter.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.crio.starter.data.MemeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemeResponseDto {
    private Long id;
    private String name;
    private String url;
    private String caption;

    public MemeResponseDto(MemeEntity memeEntity){
        this.id = memeEntity.getId();
        this.name = memeEntity.getName();
        this.url = memeEntity.getUrl();
        this.caption = memeEntity.getCaption();
    }
}
