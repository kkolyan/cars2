package net.kkolyan.pivot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nplekhanov
 */
public class KeywordExpressionParser {
    public static Collection<String> parseKeywordExpression(String expression) {
        Collection<String> tokens = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < expression.length(); i ++) {
            char c = expression.charAt(i);
            if (c == ' ' && !escaped) {
                if (buf.length() > 0) {
                    tokens.add(buf.toString());
                    buf = new StringBuilder();
                }
            }
            else if (c == '\"') {
                escaped = !escaped;
            }
            else {
                buf.append(c);
            }
        }
        if (buf.length() > 0) {
            tokens.add(buf.toString());
        }
        return tokens;
    }
}
