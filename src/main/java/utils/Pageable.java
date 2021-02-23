package utils;

public class Pageable {
    Boolean isPaged;
    Integer pageNum = 0; // 从零开始

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getNextPage() {
        pageNum ++;
        return pageNum;
    }

    public void  setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Boolean isPaged() {
        return isPaged;
    }
}
