package domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ContentConditionDto {
     private String name;
     private String startLabel;
     private String endLabel;
     private List<String> filterLabels;
     private List<String> containLabels;
     private Map<String, String> replaceWords;
     private Boolean hasDownloadImage;
     private Boolean hasRidOfWatermark;
     private String imageStoreLocation;
     private String namingType;
}
