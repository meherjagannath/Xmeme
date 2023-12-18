package com.crio.starter.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "memes")
@AllArgsConstructor
@NoArgsConstructor
public class MemeEntity {
    @Id
    private Long id;
    private String name;
    private String url;
    private String caption;
    @CreatedDate
    private Date createdAt;
}
