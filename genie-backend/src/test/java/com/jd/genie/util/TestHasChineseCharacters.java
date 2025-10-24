package com.jd.genie.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChineseCharacterCounterTest {

    @Test
    void testIsChineseCharacter() {
        // 测试中文字符
        assertTrue(ChineseCharacterCounter.isChineseCharacter('中'));
        assertTrue(ChineseCharacterCounter.isChineseCharacter('文'));
        assertTrue(ChineseCharacterCounter.isChineseCharacter('字'));

        // 测试非中文字符
        assertFalse(ChineseCharacterCounter.isChineseCharacter('a'));
        assertFalse(ChineseCharacterCounter.isChineseCharacter('1'));
        assertFalse(ChineseCharacterCounter.isChineseCharacter('@'));

        // 测试边界值
        assertTrue(ChineseCharacterCounter.isChineseCharacter('\u4E00'));  // 第一个中文字符
        assertTrue(ChineseCharacterCounter.isChineseCharacter('\u9FA5'));  // 最后一个中文字符
        assertFalse(ChineseCharacterCounter.isChineseCharacter('\u4DFF')); // 第一个中文字符前一个
        assertFalse(ChineseCharacterCounter.isChineseCharacter('\u9FA6')); // 最后一个中文字符后一个
    }

    @Test
    void testHasChineseCharacters() {
        // 测试包含中文字符的字符串
        assertTrue(ChineseCharacterCounter.hasChineseCharacters("Hello世界"));
        assertTrue(ChineseCharacterCounter.hasChineseCharacters("中文字符"));
        assertTrue(ChineseCharacterCounter.hasChineseCharacters("123测试"));

        // 测试不包含中文字符的字符串
        assertFalse(ChineseCharacterCounter.hasChineseCharacters("Hello"));
        assertFalse(ChineseCharacterCounter.hasChineseCharacters("123"));
        assertFalse(ChineseCharacterCounter.hasChineseCharacters("@#$"));

        // 测试空字符串
        assertFalse(ChineseCharacterCounter.hasChineseCharacters(""));

        // 测试null
        assertFalse(ChineseCharacterCounter.hasChineseCharacters(null));

        // 测试混合字符串
        assertTrue(ChineseCharacterCounter.hasChineseCharacters("a中b文c字d"));
        assertFalse(ChineseCharacterCounter.hasChineseCharacters("a1b2c3"));
    }
}
