package ru.kest.nexttrain.widget.model.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by KKharitonov on 06.01.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination {

    private boolean hasNext;
    private int perPage;
    private int pageCount;
    private int total;
    private int page;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "hasNext=" + hasNext +
                ", perPage=" + perPage +
                ", pageCount=" + pageCount +
                ", total=" + total +
                ", page=" + page +
                '}';
    }
}
