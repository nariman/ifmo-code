/**
 * Nariman Safiulin (woofilee)
 * File: Token.java
 * Created on: Apr 23, 2016
 */

package expression.exceptions;

public enum Token {
    END, START, UNEXPECTED, // System
    CONST, VARIABLE, // Numeric
    CLOSING_PARENTHESIS, OPENING_PARENTHESIS, // Arithmetic
    ABS, NEGATE, SQRT, // Unary
    LOG, POW, // Binary 3
    DIVIDE, MULTIPLY, // Binary 2
    ADD, SUBTRACT; // Binary 1


    public static final Token[] ARITHMETIC = new Token[]{
            CLOSING_PARENTHESIS,
            OPENING_PARENTHESIS,
    };

    public static final Token[] BINARY = new Token[]{
            ADD,
            DIVIDE,
            LOG,
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
