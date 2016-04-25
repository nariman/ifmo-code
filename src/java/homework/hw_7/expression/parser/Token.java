/**
 * Nariman Safiulin (woofilee)
 * File: Token.java
 * Created on: Apr 23, 2016
 */

package expression.parser;

public enum Token {
    END, START, UNEXPECTED, // System
    CONST, VARIABLE, // Numeric
    CLOSING_PARENTHESIS, OPENING_PARENTHESIS, // Arithmetic
    ABS, NEGATE, SQRT, SQUARE, // Unary
    LOG, POW, // Binary 3
    DIVIDE, MOD, MULTIPLY, // Binary 2
    ADD, SUBTRACT; // Binary 1

    public static class Operation {
        Token token;
        String assoc;
        Operation(Token token, String assoc) {
            this.token = token;
            this.assoc = assoc;
        }
    }

    public static final Token[] ARITHMETIC = new Token[]{
            CLOSING_PARENTHESIS,
            OPENING_PARENTHESIS,
    };

    public static final Token[] BINARY = new Token[]{
            ADD,
            DIVIDE,
            LOG,
            MOD,
            MULTIPLY,
            POW,
            SUBTRACT,
    };

    public static final Token[] NUMERIC = new Token[]{
            CONST,
            VARIABLE,
    };

    public static final Token[] UNARY = new Token[]{
            ABS,
            NEGATE,
            SQRT,
            SQUARE,
    };

    public boolean is(Token[] tokens) {
        for (Token token: tokens) {
            if (this == token) {
                return true;
            }
        }
        return false;
    }
}
