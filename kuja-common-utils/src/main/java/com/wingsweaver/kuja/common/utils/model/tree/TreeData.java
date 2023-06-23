package com.wingsweaver.kuja.common.utils.model.tree;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 树形结构。
 *
 * @param <T> 数据的类型
 * @author wingsweaver
 */
@Getter
@Setter
public class TreeData<T> {
    /**
     * 空数组。
     */
    private static final TreeData<?>[] EMPTY_ARRAY = new TreeData[0];

    /**
     * 本节点的数据。
     */
    private T data;

    /**
     * 父节点。
     */
    @Setter(lombok.AccessLevel.NONE)
    private TreeData<T> parent;

    /**
     * 子孙节点。
     */
    @Setter(lombok.AccessLevel.NONE)
    private List<TreeData<T>> children;

    public TreeData() {
        this(null);
    }

    public TreeData(T data) {
        this.data = data;
    }

    /**
     * 获取根节点。
     *
     * @return 根节点
     */

    public TreeData<T> getRoot() {
        TreeData<T> root = getParent();

        while (root != null) {
            if (root.getParent() == null) {
                break;
            }
            root = root.getParent();
        }

        // 返回
        return root;
    }

    /**
     * 获取所有的子节点的数组。
     *
     * @return 子节点的数组
     */
    @SuppressWarnings("unchecked")
    public TreeData<T>[] getChildren() {
        if (CollectionUtils.isEmpty(this.children)) {
            return (TreeData<T>[]) EMPTY_ARRAY;
        } else {
            return this.children.toArray(new TreeData[0]);
        }
    }

    /**
     * 检查是否有子节点。
     *
     * @return 是否有子节点
     */
    public boolean hasChildren() {
        return !CollectionUtils.isEmpty(this.children);
    }

    /**
     * 添加子节点。<br>
     * 如果 child 已经有 parent，则不会添加成功。
     *
     * @param child 子节点
     * @return 是否添加成功
     */
    public boolean addChild(TreeData<T> child) {
        // 检查参数
        if (child == null || child.getParent() != null) {
            return false;
        }

        // 将 child 加入到 children 中
        if (this.children == null) {
            this.children = new LinkedList<>();
        }
        this.children.add(child);

        // 设置 child 的 parent
        child.parent = this;

        // 返回
        return true;
    }

    /**
     * 删除子节点。<br>
     * 如果 child 不是本节点的子节点，则不会删除成功。
     *
     * @param child 子节点
     */
    public boolean removeChild(TreeData<T> child) {
        // 检查参数
        if (child == null || child.getParent() != this) {
            return false;
        }

        // 将 child 从 children 中删除
        Objects.requireNonNull(this.children, "children should be not null, data corrupted.");
        this.children.remove(child);

        // 清空 child 的 parent
        child.parent = null;

        // 返回结果
        return true;
    }

    /**
     * 清空所有的子节点。
     */
    public void clearChildren() {
        if (this.children != null) {
            this.children.forEach(child -> child.parent = null);
            this.children.clear();
        }
    }

    /**
     * 对子节点进行排序。
     *
     * @param sortMethod 排序方法
     * @param recursive  是否递归排序子孙节点
     */
    public void sortChildren(Comparator<T> sortMethod, boolean recursive) {
        if (CollectionUtils.isEmpty(this.children)) {
            return;
        }

        // 对直属子节点进行排序
        this.children.sort((o1, o2) -> sortMethod.compare(o1.getData(), o2.getData()));

        // （按需）对孙子节点进行排序
        if (recursive) {
            this.children.forEach(child -> child.sortChildren(sortMethod, true));
        }
    }
}
