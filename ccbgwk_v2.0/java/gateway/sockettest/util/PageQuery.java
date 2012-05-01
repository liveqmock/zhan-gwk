package gateway.sockettest.util;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:52:52
 * To change this template use File | Settings | File Templates.
 */
public class PageQuery {
    public static int DEFAULT_PAGE_SIZE = 15;
    private int startIndex;
    private int pageSize;

    public PageQuery(int startIndex, int pageSize) {
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}   