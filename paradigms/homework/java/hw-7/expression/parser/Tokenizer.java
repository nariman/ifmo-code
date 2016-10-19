/**
 * Nariman Safiulin (woofilee)
 * File: Tokenizer.java
 * Created on: Апр. 23, 2016
 */

package expression.parser;

import expression.ExpressionNumber;

public abstract class Tokenizer<T> {
    private char[] expression;
    private int position;
    private Token prev;
    private Token current;

    public void ready(char[] expression) {
        this.expression = expression;
        position = -1;
        prev = null;
        current = Token.START;
    }

    private void switchToken(Token current) {
        this.prev = this.current;
        this.current = current;
    }

    private void skipSpace() {
        position++;
        while (position < expression.length && Character.isWhitespace(expression[position])) {
            position++;
        }
    }

    private boolean match(char[] token) {
        int exprLen = expression.length;
        int exprPos = position;
        int tokenLen = token.length;
        int tokenPos = 0;

        while (exprPos < exprLen && expression[exprPos] == token[tokenPos]) {
            exprPos++;
            tokenPos++;
            if (tokenPos == tokenLen) {
                return true;
            }
        }

        return false;
    }

    // TODO: Check for twice dot problem
    private boolean checkConstToken() {
        int savedPos = position;
        skipSpace();
        if (position < expression.length && Character.isDigit(expression[position])) {
            position = savedPos;
            return true;
        }
        position = savedPos;
        return false;
    }

    protected abstract ExpressionNumber<T> parseConstToken(String num);

    public ExpressionNumber<T> getConstToken() {
        StringBuilder sb = new StringBuilder();
        if (expression[position] == '-') {
            sb.append("-");
            skipSpace();
        }

        if (Character.isDigit(expression[position])) {
            while (position < expression.length && Character.isDigit(expression[position])) {
                sb.append(expression[position]);
                position++;
            }

            if (position < expression.length && expression[position] == '.') {
                position++;
                while (position < expression.length && Character.isDigit(expression[position])) {
                    sb.append(expression[position]);
                    position++;
                }
            }
        }

        position--;
        return parseConstToken(sb.toString());
    }

    public String getVariableToken() {
        StringBuilder v = new StringBuilder();
        while (position < expression.length && Character.isAlphabetic(expression[position])) {
            v.append(expression[position]);
            position++;
        }
        position--;
        return v.toString();
    }

    public int getPosition() {
        return position;
    }

    public Token getCurrentToken() {
        return current;
    }

    public void prev() {
        if (current == Token.DIVIDE || current == Token.MULTIPLY
                || current == Token.ADD || current == Token.SUBTRACT
                || current == Token.CLOSING_PARENTHESIS || current == Token.OPENING_PARENTHESIS) {
            position--;
        } else if (current == Token.LOG || current == Token.POW) {
            position -= 2;
        } else if (current == Token.MOD) {
            position -= 3;
        }
        current = prev;
        prev = null;
    }

    public Token next() {
        skipSpace();
        if (position >= expression.length) {
            switchToken(Token.END);
        } else {
            if (match("(".toCharArray())) {
                switchToken(Token.OPENING_PARENTHESIS);
            } else if (match(")".toCharArray())) {
                switchToken(Token.CLOSING_PARENTHESIS);
            } else if (match("square".toCharArray())) {
                position += 5;
                switchToken(Token.SQUARE);
            } else if (match("sqrt".toCharArray())) {
                position += 3;
                switchToken(Token.SQRT);
            } else if (match("abs".toCharArray())) {
                position += 2;
                switchToken(Token.ABS);
            } else if (match("mod".toCharArray())) {
                position += 2;
                switchToken(Token.MOD);
            } else if (match("//".toCharArray())) {
                position += 1;
                switchToken(Token.LOG);
            } else if (match("**".toCharArray())) {
                position += 1;
                switchToken(Token.POW);
            } else { // Nothing from Complex Arithmetic
                if (expression[position] == '*') {
                    switchToken(Token.MULTIPLY);
                } else if (expression[position] == '/') {
                    switchToken(Token.DIVIDE);
                } else if (expression[position] == '+') {
                    switchToken(Token.ADD);
                } else if (expression[position] == '-') { // Multiple meanings
                    //if (current.is(new Token[]{Token.CONST, Token.VARIABLE, Token.CLOSING_PARENTHESIS})) {
                    if (current == Token.CONST || current == Token.VARIABLE || current == Token.CLOSING_PARENTHESIS) {
                        switchToken(Token.SUBTRACT);
                    } else if (checkConstToken()) {
                        switchToken(Token.CONST);
                    } else {
                        switchToken(Token.NEGATE);
                    }
                } else { // Nothing from Arithmetic
                    if (Character.isDigit(expression[position])) {
                        switchToken(Token.CONST);
                    } else if (match("x".toCharArray()) || match("y".toCharArray()) || match("z".toCharArray())) {
                        // I think, that all other tokens may be variables, but we limited by three variables
                        switchToken(Token.VARIABLE);
                    } else {
                        switchToken(Token.UNEXPECTED);
                    }
                }
            }
        }

        return current;
    }
}
