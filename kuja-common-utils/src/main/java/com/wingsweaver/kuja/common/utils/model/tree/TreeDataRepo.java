package com.wingsweaver.kuja.common.utils.model.tree;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 同时包含树形结构和字典的、便于查询的混合数据结构。
 *
 * @param <K> 字典的 Key 的类型
 * @param <T> 数据的类型
 * @author wingsweaver
 */
public class TreeDataRepo<K, T> {
    /**
     * 一对一的字典（不可更改）。
     */
    @Getter
    private Map<K, TreeData<T>> treeDataMap;

    /**
     * （虚拟）根节点。
     */
    private final TreeData<T> virtualRootNode = new TreeData<>();

    /**
     * 获取所有的顶级节点。
     *
     * @return 所有的顶级节点的数组
     */
    public TreeData<T>[] getRoots() {
        return virtualRootNode.getChildren();
    }

    /**
     * 对所有的节点进行排序。
     *
     * @param sortMethod 排序函数
     */
    public void sort(Comparator<T> sortMethod) {
        virtualRootNode.sortChildren(sortMethod, true);
    }

    /**
     * 创建 {@link Builder} 实例。
     *
     * @param <P> 字典的 Key 的类型
     * @param <V> 数据的类型
     * @return {@link Builder} 实例
     */
    public static <P, V> Builder<P, V> builder() {
        return new Builder<>();
    }

    /**
     * {@link TreeDataRepo} 的创建者工具类。
     *
     * @param <P> 字典的 Key 的类型
     * @param <V> 数据的类型
     */
    public static class Builder<P, V> {
        /**
         * 目标集合。
         */
        private Collection<V> collection;

        /**
         * 数据的 Key 的解析器。
         */
        private Function<V, P> keyResolver;

        /**
         * 父节点的 Key 的解析器。
         */
        private Function<V, P> parentKeyResolver;

        public Builder<P, V> collection(Collection<V> collection) {
            this.collection = collection;
            return this;
        }

        public Builder<P, V> keyResolver(Function<V, P> keyResolver) {
            this.keyResolver = keyResolver;
            return this;
        }

        public Builder<P, V> parentKeyResolver(Function<V, P> parentKeyResolver) {
            this.parentKeyResolver = parentKeyResolver;
            return this;
        }

        /**
         * 构建 {@link TreeDataRepo} 实例。
         *
         * @return TreeDataRepo 实例
         */
        public TreeDataRepo<P, V> build() {
            // 检查参数
            AssertState.Named.notNull("keyResolver", keyResolver);
            AssertState.Named.notNull("parentKeyResolver", parentKeyResolver);

            // 创建 TreeDataRepo
            TreeDataRepo<P, V> treeDataRepo = new TreeDataRepo<>();
            if (!CollectionUtils.isEmpty(this.collection)) {
                // 先提取成 Map
                List<Tuple2<P, TreeData<V>>> keyTreeDataList = new ArrayList<>(this.collection.size());
                Map<P, TreeData<V>> treeDataMap = this.createTreeDataMap(keyTreeDataList);
                treeDataRepo.treeDataMap = treeDataMap;

                // 再组装成树形结构
                if (!CollectionUtils.isEmpty(treeDataMap)) {
                    this.constructTreeDataRepo(treeDataRepo, keyTreeDataList);
                }
            }

            // 返回
            return treeDataRepo;
        }

        private void constructTreeDataRepo(TreeDataRepo<P, V> treeDataRepo, List<Tuple2<P, TreeData<V>>> keyTreeDataList) {
            Map<P, TreeData<V>> treeDataMap = treeDataRepo.treeDataMap;

            // 补全各个节点的父节点
            for (Tuple2<P, TreeData<V>> tuple2 : keyTreeDataList) {
                TreeData<V> treeData = tuple2.getT2();

                // 计算父节点的 Key 和 TreeData
                P parentKey = this.parentKeyResolver.apply(treeData.getData());
                TreeData<V> parentTreeData = null;
                if (parentKey != null) {
                    parentTreeData = treeDataMap.get(parentKey);
                }
                if (parentTreeData == null) {
                    parentTreeData = treeDataRepo.virtualRootNode;
                }

                // 添加父子关系
                parentTreeData.addChild(treeData);
            }
        }

        private Map<P, TreeData<V>> createTreeDataMap(List<Tuple2<P, TreeData<V>>> keyTreeDataList) {
            int size = MapUtil.hashInitCapacity(collection.size() + 1, MapUtil.FULL_LOAD_FACTOR);
            Map<P, TreeData<V>> treeDataMap = new HashMap<>(size, MapUtil.FULL_LOAD_FACTOR);
            for (V item : collection) {
                P key = keyResolver.apply(item);
                if (key != null) {
                    TreeData<V> treeData = new TreeData<>();
                    treeData.setData(item);
                    treeDataMap.put(key, treeData);

                    // 保存到临时列表中
                    keyTreeDataList.add(Tuple2.of(key, treeData));
                }
            }
            return treeDataMap;
        }
    }
}
