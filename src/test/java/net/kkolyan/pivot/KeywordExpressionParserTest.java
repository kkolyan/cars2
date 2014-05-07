package net.kkolyan.pivot;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static net.kkolyan.pivot.KeywordExpressionParser.*;
import static org.junit.Assert.*;

/**
 * @author nplekhanov
 */
public class KeywordExpressionParserTest {

    @Test
    public void test1() {
        assertEquals("[]", parseKeywordExpression("").toString());
    }

    @Test
    public void test2() {
        assertEquals("[a, b d34, c, d]", parseKeywordExpression("a \"b d34\" c \"d\"").toString());
    }

    @Test
    public void test3() {
        assertEquals("[b d34]", parseKeywordExpression("\"b d34\"").toString());
    }

    @Test
    public void test4() {
        assertEquals("[b d34]", parseKeywordExpression("\"b d34\" \"").toString());
    }
}
