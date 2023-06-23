package com.wingsweaver.kuja.common.utils.model.tree;

import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TreeDataRepoTest {
    String[] districtNames(District[] districts) {
        String[] names = new String[districts.length];
        for (int i = 0; i < districts.length; i++) {
            names[i] = districts[i].getName();
        }
        return names;
    }

    String[] districtNames(TreeData<District>[] districts) {
        String[] names = new String[districts.length];
        for (int i = 0; i < districts.length; i++) {
            names[i] = districts[i].getData().getName();
        }
        return names;
    }

    @Test
    void test() {
        District[] districts = new District[]{
                new District(1, "beijing", 0),
                new District(3, "hubei", 0),
                new District(0, "zhongguo", null),
                new District(2, "shanghai", 0),
                new District(4, "wuhan", 3),
                new District(5, "haidian", 1),
                new District(6, "putong", 2),
                new District(7, "hongqiao", 2),
                new District(8, "chaoyang", 1),
                new District(9, "shandong", 0),
        };

        TreeDataRepo<Integer, District> treeDataRepo = TreeDataRepo.<Integer, District>builder()
                .collection(CollectionUtil.listOf(districts))
                .keyResolver(District::getId)
                .parentKeyResolver(District::getParentId)
                .build();

        TreeData<District>[] roots = treeDataRepo.getRoots();
        assertEquals(1, roots.length);
        District rootDistrict = roots[0].getData();
        assertEquals(0, rootDistrict.getId());
        assertEquals("zhongguo", rootDistrict.getName());
        assertNull(rootDistrict.getParentId());

        {
            TreeData<District>[] provinces = roots[0].getChildren();
            assertEquals(4, provinces.length);
            assertArrayEquals(new String[]{"beijing", "hubei", "shanghai", "shandong"}, districtNames(provinces));

            TreeData<District> shanghai = treeDataRepo.getTreeDataMap().get(2);
            TreeData<District>[] areasInShanghai = shanghai.getChildren();
            assertArrayEquals(new String[]{"putong", "hongqiao"}, districtNames(areasInShanghai));
        }

        treeDataRepo.sort(Comparator.comparing(District::getName));
        {
            TreeData<District>[] provinces = roots[0].getChildren();
            assertEquals(4, provinces.length);
            assertArrayEquals(new String[]{"beijing", "hubei", "shandong", "shanghai"}, districtNames(provinces));

            TreeData<District> shanghai = treeDataRepo.getTreeDataMap().get(2);
            TreeData<District>[] areasInShanghai = shanghai.getChildren();
            assertArrayEquals(new String[]{"hongqiao", "putong"}, districtNames(areasInShanghai));
        }
    }

    @Getter
    @AllArgsConstructor
    static class District {
        private final Integer id;

        private final String name;

        private final Integer parentId;

        @Override
        public String toString() {
            return this.name;
        }
    }
}