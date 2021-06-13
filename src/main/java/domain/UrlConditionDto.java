package domain;

import lombok.Data;

import java.util.List;

@Data
public class UrlConditionDto {
    private String startLabel;
    private String endLabel;
    private List<String> filterWords;
    private List<String> containWords;
    private ContentConditionDto contentConditionDto;
}
