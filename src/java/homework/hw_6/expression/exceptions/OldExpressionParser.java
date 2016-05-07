/**
 * Nariman Safiulin (woofilee)
 * File: OldExpressionParser.java
 * Created on: Mar 28, 2016
 */

package expression.exceptions;

import expression.*;

public class OldExpressionParser implements Parser {
    private static class Token {
        enum SystemToken {
            UNEXPECTED,
            EOF
        }

        static class ConstantToken {
            public final String value;

            ConstantToken(String value) {
                this.value = value;
            }
        }

        enum VariableToken {
            X("x"),
            Y("y"),
            Z("z");

            public final String string;
            public final char[] charArray;

            VariableToken(String string) {
                this.string = string;
                this.charArray = string.toCharArray();
            }
        }

        enum ArithmeticToken {
            OPENING_PARENTHESIS("("),
            CLOSING_PARENTHESIS(")");

            public final String string;
            public final char[] charArray;

            ArithmeticToken(String string) {
                this.string = string;
                this.charArray = string.toCharArray();
            }
        }

        enum UnaryOperationToken {
            SQRT("sqrt"), // Longer first
            ABS("abs"),
            NEGATE("-");

            public final String string;
            public final char[] charArray;

            UnaryOperationToken(String string) {
                this.string = string;
                this.charArray = string.toCharArray();
            }
        }

        enum BinaryOperationToken {
            LOGARITHM("//", 3), // High priority first, Longer first
            POW("**", 3),
            DIVIDE("/", 2),
            MULTIPLY("*", 2),
            ADD("+", 1),
            SUBTRACT("-", 1);

            public final String string;
            public final char[] charArray;
            public final int priority;

            BinaryOperationToken(String string, int priority) {
                this.string = string;
                this.charArray = string.toCharArray();
                this.priority = priority;
            }
        }
    }

    private char[] expression;
    private int position;

    private void setParserState(String expressionString, int expressionPos) {
        this.expression = expressionString.toCharArray();
        this.position = expressionPos;
    }

    private void setParserState(String expressionString) {
        setParserState(expressionString, -1);
    }

    public OldExpressionParser() {
        setParserState("");
    }

    private Character nextSymbol() {
        if (position == expression.length) {
            return null;
        }

        do {
            ++position;
        } while (position < expression.length
                && Character.isWhitespace(expression[position]));

        if (position == expression.length) {
            return null;
        }

        return expression[position];
    }

    private void returnSymbol() {
        position--;
    }

    private boolean compareInputToken(char[] token) {
        int pos = position;
        int tokenLen = token.length;
        int tokenPos = 0;

        while (tokenPos < tokenLen) {
            if (pos < expression.length && expression[pos] == token[tokenPos]) {
                pos++;
                tokenPos++;
            } else {
                return false;
            }
        }

        return true;
    }

    private Object parseToken() {
        Character c = nextSymbol();

        if (c == null) {
            return Token.SystemToken.EOF;
        }

        // Integer
        if (Character.isDigit(c)) {
            StringBuilder sb = new StringBuilder();

            while (c != null && Character.isDigit(c)) {
                sb.append(c);
                c = nextSymbol();
            }

            returnSymbol();
            return new Token.ConstantToken(sb.toString());
        }

        // Variable
        for (Token.VariableToken token : Token.VariableToken.values()) {
            if (compareInputToken(token.charArray)) {
                position += token.charArray.length - 1;
                return token;
            }
        }

        // Parenthesis
        for (Token.ArithmeticToken token : Token.ArithmeticToken.values()) {
            if (compareInputToken(token.charArray)) {
                position += token.charArray.length - 1;
                return token;
            }
        }

        // Unary operation
        for (Token.UnaryOperationToken token : Token.UnaryOperationToken.values()) {
            if (compareInputToken(token.charArray)) {
                position += token.charArray.length - 1;
                return token;
            }
        }

        // Binary operation
        for (Token.BinaryOperationToken token : Token.BinaryOperationToken.values()) {
            if (compareInputToken(token.charArray)) {
                position += token.charArray.length - 1;
                return token;
            }
        }

        // Some unexpected token... we don't know, what to do.. :(
        return Token.SystemToken.UNEXPECTED;
    }

    private void returnToken(String token) {
        position -= token.length();
    }

    private TripleExpression parseExpressionObject(boolean negated) throws ParseException {
        Object token = parseToken();

        if (token instanceof Token.ConstantToken) {
            return new Constant(Integer.parseInt(((negated) ? "-" : "") + ((Token.ConstantToken) token).value));
        } else if (token instanceof Token.VariableToken) {
            if (negated) {
                return new CheckedNegate(new Variable(((Token.VariableToken) token).string));
            }
            return new Variable(((Token.VariableToken) token).string);
        } else if (token instanceof Token.UnaryOperationToken) {
            switch (((Token.UnaryOperationToken) token)) {
                case SQRT:
                    if (negated) {
                        return new CheckedNegate(new CheckedSqrt(parseExpressionObject(false)));
                    }
                    return new CheckedSqrt(parseExpressionObject(false));
                case ABS:
                    if (negated) {
                        return new CheckedNegate(new CheckedAbs(parseExpressionObject(false)));
                    }
                    return new CheckedAbs(parseExpressionObject(false));
                case NEGATE:
                    TripleExpression innerObject = parseExpressionObject(true);
                    return (negated) ? new CheckedNegate(innerObject) : innerObject;
            }
        } else if (token instanceof Token.ArithmeticToken) {
            if (token == Token.ArithmeticToken.OPENING_PARENTHESIS) {
                TripleExpression innerExpression = parseExpression(0, true);

                token = parseToken();
                if (token != Token.ArithmeticToken.CLOSING_PARENTHESIS) {
                    throw new ParseException(
                            "[ERROR] Closing parenthesis expected, eof found :( at pos %d",
                            position
                    );
                }

                return (negated) ? new CheckedNegate(innerExpression) : innerExpression;
            } else {
                throw new ParseException(
                        "[ERROR] Constant, Variable, Unary or opening bracket expected, unexpected closing parenthesis `" +
                                Token.ArithmeticToken.CLOSING_PARENTHESIS.string + "` found at pos %d",
                        position
                );
            }
        }

        throw new ParseException("[ERROR] Constant, Variable, Unary or " +
                "opening bracket expected, error symbol found at " +
                "pos %d :(", position);
    }

    private TripleExpression parseExpression(int minPriority, boolean isRecursive) throws ParseException {
        TripleExpression left = parseExpressionObject(false);

        while (true) {
            Object token = parseToken();

            if (token == Token.UnaryOperationToken.NEGATE) {
                token = Token.BinaryOperationToken.SUBTRACT;
            }

            if (token == Token.SystemToken.EOF) {
                return left;
            } else if (token == Token.ArithmeticToken.CLOSING_PARENTHESIS && isRecursive) {
                returnToken(((Token.ArithmeticToken) token).string);
                return left;
            } else if (token instanceof Token.BinaryOperationToken) {
                if (((Token.BinaryOperationToken) token).priority <= minPriority) {
                    returnToken(((Token.BinaryOperationToken) token).string);
                    return left;
                }
                // else continue working
            } else {
                throw new ParseException(
                            "[ERROR] Binary operation or closing bracket expected, other token found at pos %d :(",
                        position
                );
            }

            TripleExpression right = parseExpression(((Token.BinaryOperationToken) token).priority, true);

            switch ((Token.BinaryOperationToken) token) {
                case LOGARITHM:
                    left = new CheckedLogarithm(left, right);
                    break;
                case POW:
                    left = new CheckedPow(left, right);
                    break;
                case DIVIDE:
                    left = new CheckedDivide(left, right);
                    break;
                case MULTIPLY:
                    left = new CheckedMultiply(left, right);
                    break;
                case ADD:
                    left = new CheckedAdd(left, right);
                    break;
                case SUBTRACT:
                    left = new CheckedSubtract(left, right);
                    break;
            }
        }
    }

    @Override
    public TripleExpression parse(String expression) throws ParseException {
        setParserState(expression);
        return parseExpression(0, false);
    }

    class ParseException extends java.text.ParseException {
        public ParseException(String s, int errorOffset) {
            super(String.format(s, errorOffset), errorOffset);
        }
    }
}
