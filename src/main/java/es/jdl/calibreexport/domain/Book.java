package es.jdl.calibreexport.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * nombre size type null
 * id	4	INTEGER 1
 * title	12	TEXT 0
 * sort	12	TEXT 1
 * timestamp	12	TIMESTAMP 1
 * pubdate	12	TIMESTAMP 1
 * series_index	6	REAL 0
 * author_sort	12	TEXT 1
 * isbn	12	TEXT 1
 * lccn	12	TEXT 1
 * path	12	TEXT 0
 * flags	4	INTEGER 0
 * uuid	12	TEXT 1
 * has_cover	4	BOOL 1
 * last_modified	12	TIMESTAMP 0
 */
public class Book {
    private int id;
    private String title;
    private String sort;
    private Date timestamp;
    private Date pubdate;
    private BigDecimal seriesIndex;
    private String authorSort;
    private String isbn;
    private String iccn;
    private String path;
    private BigDecimal flags;
    private String uuid;
    private Boolean hasCover;
    private Date lastModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public BigDecimal getSeriesIndex() {
        return seriesIndex;
    }

    public void setSeriesIndex(BigDecimal seriesIndex) {
        this.seriesIndex = seriesIndex;
    }

    public String getAuthorSort() {
        return authorSort;
    }

    public void setAuthorSort(String authorSort) {
        this.authorSort = authorSort;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIccn() {
        return iccn;
    }

    public void setIccn(String iccn) {
        this.iccn = iccn;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BigDecimal getFlags() {
        return flags;
    }

    public void setFlags(BigDecimal flags) {
        this.flags = flags;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getHasCover() {
        return hasCover;
    }

    public void setHasCover(Boolean hasCover) {
        this.hasCover = hasCover;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
