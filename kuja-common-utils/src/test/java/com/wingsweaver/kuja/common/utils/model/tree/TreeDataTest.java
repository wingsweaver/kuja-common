package com.wingsweaver.kuja.common.utils.model.tree;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeDataTest {
    @Test
    void test() {
        TreeData<String> china = new TreeData<>();
        assertNull(china.getData());
        assertNull(china.getParent());
        assertFalse(china.hasChildren());
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(0, provinces.length);
        }

        TreeData<String> beijing = new TreeData<>("beijing");
        assertEquals("beijing", beijing.getData());
        assertTrue(china.addChild(beijing));
        assertTrue(china.hasChildren());
        assertSame(china, beijing.getParent());
        assertSame(china, beijing.getRoot());
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(1, provinces.length);
            assertSame(beijing, provinces[0]);
        }

        TreeData<String> haidian = new TreeData<>("haidian");
        assertTrue(beijing.addChild(haidian));
        assertSame(beijing, haidian.getParent());
        assertSame(china, haidian.getRoot());
        {
            assertFalse(china.addChild(haidian));
            assertSame(beijing, haidian.getParent());
            assertSame(china, haidian.getRoot());
        }

        TreeData<String> chaoyang = new TreeData<>("chaoyang");
        assertTrue(beijing.addChild(chaoyang));
        {
            TreeData<String>[] districts = beijing.getChildren();
            assertNotNull(districts);
            assertEquals(2, districts.length);
            assertSame(haidian, districts[0]);
            assertSame(chaoyang, districts[1]);
        }

        TreeData<String> shanghai = new TreeData<>();
        shanghai.setData("shanghai");
        assertTrue(china.addChild(shanghai));
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(2, provinces.length);
            assertSame(beijing, provinces[0]);
            assertSame(shanghai, provinces[1]);
        }

        TreeData<String> hubei = new TreeData<>("hubei");
        assertTrue(china.addChild(hubei));
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(3, provinces.length);
            assertSame(beijing, provinces[0]);
            assertSame(shanghai, provinces[1]);
            assertSame(hubei, provinces[2]);
        }

        // 节点排序
        china.sortChildren(StringUtils::compare, false);
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(3, provinces.length);
            assertSame(beijing, provinces[0]);
            assertSame(hubei, provinces[1]);
            assertSame(shanghai, provinces[2]);
        }
        {
            TreeData<String>[] districts = beijing.getChildren();
            assertNotNull(districts);
            assertEquals(2, districts.length);
            assertSame(haidian, districts[0]);
            assertSame(chaoyang, districts[1]);
        }
        china.sortChildren(StringUtils::compare, true);
        {
            TreeData<String>[] districts = beijing.getChildren();
            assertNotNull(districts);
            assertEquals(2, districts.length);
            assertSame(chaoyang, districts[0]);
            assertSame(haidian, districts[1]);
        }

        // 删除一个节点
        assertTrue(china.removeChild(hubei));
        assertNull(hubei.getParent());
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(2, provinces.length);
            assertSame(beijing, provinces[0]);
            assertSame(shanghai, provinces[1]);
        }
        assertFalse(china.removeChild(haidian));
        assertSame(beijing, haidian.getParent());

        // 清空所有节点
        china.clearChildren();
        {
            TreeData<String>[] provinces = china.getChildren();
            assertNotNull(provinces);
            assertEquals(0, provinces.length);
        }
    }
}