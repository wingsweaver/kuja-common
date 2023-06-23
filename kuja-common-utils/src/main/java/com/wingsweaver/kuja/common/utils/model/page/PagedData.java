package com.wingsweaver.kuja.common.utils.model.page;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 带分页的数据。
 *
 * @param <T> 数据的类型
 * @author wingsweaver
 */
@Getter
@Setter
public class PagedData<T> {
    /**
     * 总的记录条数。
     */
    private long totalCount;

    /**
     * 每页的记录条数。
     */
    private int pageSize = 1;

    /**
     * 当前的页码（从 1 开始）。
     */
    private int pageNumber = 1;

    /**
     * 本页的数据的列表。
     */
    private List<T> list;

    public void setTotalCount(Long totalCount) {
        AssertArgs.Named.noLessThan("totalCount", totalCount, 0L);
        this.totalCount = totalCount;
    }

    /**
     * 获取总的页数。
     *
     * @return 总的页数
     */
    public int getTotalPageCount() {
        return (int) ((this.totalCount + this.pageSize - 1) / this.pageSize);
    }

    public void setPageSize(int pageSize) {
        AssertArgs.Named.greaterThan("pageSize", pageSize, 0);
        this.pageSize = pageSize;
    }

    public void setPageNumber(int pageNumber) {
        AssertArgs.Named.greaterThan("pageNumber", pageNumber, 0);
        this.pageNumber = pageNumber;
    }

    /**
     * 计算是否有前一页。
     *
     * @return 是否有前一页
     */
    public boolean hasPreviousPage() {
        return this.pageNumber > 1;
    }

    /**
     * 计算是否有后一页。
     *
     * @return 是否有后一页
     */
    public boolean hasNextPage() {
        return this.pageNumber < this.getTotalPageCount();
    }

    /**
     * 获取本页开始的记录编号（包含）。
     *
     * @return 本页开始的记录编号
     */
    public long getPageStartIndex() {
        return (long) this.pageSize * (this.pageNumber - 1);
    }

    /**
     * 获取本页结束的最大记录编号（不包含）。
     *
     * @return 本页结束的最大记录编号
     */
    @SuppressWarnings("unused")
    public long getPageEndIndex() {
        return (long) this.pageSize * this.pageNumber;
    }

    /**
     * 获取本页中有效记录的最大记录编号（不包含）。
     *
     * @return 获取本页中有效记录的最大记录编号
     */
    public long getPageLastRecordIndex() {
        long startIndex = this.getPageStartIndex();
        return startIndex + Math.min(this.pageSize, CollectionUtil.size(this.list));
    }
}
