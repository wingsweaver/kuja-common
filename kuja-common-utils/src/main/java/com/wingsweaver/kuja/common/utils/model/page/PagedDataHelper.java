package com.wingsweaver.kuja.common.utils.model.page;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link PagedData} 的工具类。
 *
 * @author wingsweaver
 */
public final class PagedDataHelper {
    private PagedDataHelper() {
        // 禁止实例化
    }

    /**
     * 根据指定的集合、以及其偏移量，计算要返回的 PagedData 实例。
     *
     * @param pageSize         每页的记录条数
     * @param pageNumber       当前的页码（从 1 开始）
     * @param totalCount       总的记录条数
     * @param collection       数据的集合
     * @param collectionOffset 数据的偏移量
     * @param <T>              数据的类型
     * @return PagedData 实例
     * @throws IllegalArgumentException 参数不正确
     */
    public static <T> PagedData<T> makePagedData(int pageSize, int pageNumber, long totalCount,
                                                 Collection<T> collection, long collectionOffset) throws IllegalArgumentException {
        PagedData<T> pagedData = new PagedData<>();
        pagedData.setTotalCount(totalCount);
        pagedData.setPageSize(pageSize);
        pagedData.setPageNumber(pageNumber);
        pagedData.setList(Collections.emptyList());

        // 计算本页的数据的偏移量
        long pageStartIndex = pagedData.getPageStartIndex();
        long collectionMaxIndex = collectionOffset + CollectionUtil.size(collection);
        AssertArgs.isTrue(pageStartIndex <= totalCount, () -> "Start index of current page (" + pageStartIndex
                + ") is greater than total count (" + totalCount + ").");
        AssertArgs.isTrue(pageStartIndex >= collectionOffset, () -> "Start index of current page (" + pageStartIndex
                + ") is less than collection offset (" + collectionOffset + ").");
        AssertArgs.isTrue(pageStartIndex <= collectionMaxIndex, () -> "Start index of current page (" + pageStartIndex
                + ") is greater than collection max index (" + collectionMaxIndex + ").");

        // 计算复制范围, [copyStartIndex, copyEndIndex)
        // 即：>= copyStartIndex && < copyEndIndex。
        long pageEndIndex = Math.min(pageStartIndex + pageSize, Math.min(totalCount, collectionMaxIndex));
        int pagedDataCount = (int) (pageEndIndex - pageStartIndex);
        if (pagedDataCount > 0) {
            int lbound = (int) (pageStartIndex - collectionOffset);
            int ubound = lbound + pagedDataCount;
            // 复制数据
            if (collection instanceof List) {
                // 如果是 List 的话，使用 subList 方法
                pagedData.setList(((List<T>) collection).subList(lbound, ubound));
            } else {
                // 如果不是 List 的话，逐个便利 collection, 复制在区间范围内的数据
                List<T> list = new ArrayList<>(pagedDataCount);
                int index = 0;
                for (T item : collection) {
                    if (index >= ubound) {
                        // 如果已经达到了最大计数，那么退出
                        break;
                    } else if (index >= lbound) {
                        // 如果处于数据拷贝区，那么复制数据
                        list.add(item);
                    }

                    // 增加计数
                    index++;
                }
                pagedData.setList(list);
            }
        }

        // 返回
        return pagedData;
    }

    /**
     * 根据指定的集合、以及其偏移量，计算要返回的 PagedData 实例。
     *
     * @param pageSize   每页的记录条数
     * @param pageNumber 当前的页码（从 1 开始）
     * @param totalCount 总的记录条数（可以为 null）
     * @param collection 数据的集合
     * @param <T>        数据的类型
     * @return PagedData 实例
     * @throws IllegalArgumentException 参数不正确
     */
    @SuppressWarnings("unused")
    public static <T> PagedData<T> makePagedData(int pageSize, int pageNumber, long totalCount,
                                                 Collection<T> collection) throws IllegalArgumentException {
        return makePagedData(pageSize, pageNumber, totalCount, collection, 0);
    }

    /**
     * 根据指定的集合、以及其偏移量，计算要返回的 PagedData 实例。
     *
     * @param pageSize   每页的记录条数
     * @param pageNumber 当前的页码（从 1 开始）
     * @param collection 数据的集合
     * @param <T>        数据的类型
     * @return PagedData 实例
     * @throws IllegalArgumentException 参数不正确
     */
    @SuppressWarnings("unused")
    public static <T> PagedData<T> makePagedData(int pageSize, int pageNumber, Collection<T> collection)
            throws IllegalArgumentException {
        return makePagedData(pageSize, pageNumber, CollectionUtil.size(collection), collection, 0);
    }

    /**
     * 根据指定的集合、以及其偏移量，计算要返回的 PagedData 实例。
     *
     * @param pageSize         每页的记录条数
     * @param pageNumber       当前的页码（从 1 开始）
     * @param collection       数据的集合
     * @param collectionOffset 数据的偏移量
     * @param <T>              数据的类型
     * @return PagedData 实例
     * @throws IllegalArgumentException 参数不正确
     */
    public static <T> PagedData<T> makePagedData(int pageSize, int pageNumber, Collection<T> collection, int collectionOffset)
            throws IllegalArgumentException {
        return makePagedData(pageSize, pageNumber, (long) CollectionUtil.size(collection) + collectionOffset, collection, collectionOffset);
    }
}
