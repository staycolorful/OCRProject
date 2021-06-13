package domain;

import lombok.Data;

import java.util.List;

/**
 * 查询多级url条件
 */
@Data
public class QueryDto {
    List<UrlConditionDto> urlConditionDtoList;
}
