package domain;

import lombok.Data;

import java.util.Map;

@Data
public class UrlDto {
    private Integer id;
    private Integer parentId;
    private String name;
    private String url;
    private Map<String,String> contentMap;

}
