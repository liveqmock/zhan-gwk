package gateway.mbs.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:53:22
 * To change this template use File | Settings | File Templates.
 */
public class PageResult {
    /**
     * 查询的分页数据
     */
    private List data = new LinkedList();   
    /**
     * 记录总条数
     */
    private int totalRecord;

    public PageResult(List data,int totalRecord){
        this.data=data;
        this.totalRecord=totalRecord;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
}