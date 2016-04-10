/**
 * Nariman Safiulin (woofilee)
 * File: ExpressionParser.java
 * Created on: Mar 28, 2016
 */

package expression.exceptions;

import expression.*;

import java.text.ParseException;

public class ExpressionParser implements Parser {
    public static class Token {
        public enum SystemToken {
            UNEXPECTED,
            EOF
        }

        public static class ConstantToken {
            public final String value;

            ConstantToken(String value) {
                this.value = value;
            }
        }

        public enum VariableToken {
            X("x"),
            Y("y"),
            Z("z");

            public final String stringRepresentation;
            public final char[] charArrayRepresentation;

            VariableToken(String stringRepresentation) {
                this.stringRepresentation = stringRepresentation;
                this.charArrayRepresentation = stringRepresentation.toCharArray();
            }
        }

        public enum ArithmeticToken {
            OPENING_PARENTHESIS("("),
            CLOSING_PARENTHESIS(")");

            public final String stringRepresentation;
            public final char[] charArrayRepresentation;

            ArithmeticToken(String stringRepresentation) {
                this.stringRepresentation = stringRepresentation;
                this.charArrayRepresentation = stringRepresentation.toCharArray();
            }
        }

        public enum UnaryOperationToken {
            SQRT("sqrt"), // Longer first
            ABS("abs"),
            NEGATE("-");

            public final String stringRepresentation;
            public final char[] charArrayRepresentation;

            UnaryOperationToken(String stringRepresentation) {
                this.stringRepresentation = stringRepresentation;
                this.charArrayRepresentation = stringRepresentation.toCharArray();
            }
        }

        public enum BinaryOperationToken {
            LOGARITHM("//", 3), // High priority first, Longer first
            POW("**", 3),
            DIVIDE("/", 2),
            MULTIPLY("*", 2),
            ADD("+", 1),
            SUBTRACT("-", 1);

            public final String stringRepresentation;
            public final char[] charArrayRepresentation;
            public final int priority;

            BinaryOperationToken(String stringRepresentation, int priority) {
                this.stringRepresentation = stringRepresentation;
                this.charArrayRepresentation = stringRepresentation.toCharArray();
                this.priority = priority;
            }
        }
    }

    private char[] expressionString;
    private int expressionPos;

    private void setParserState(String expressionString, int expressionPos) {
        this.expressionString = expressionString.toCharArray();
        this.expressionPos = expressionPos;
    }

    private void setParserState(String expressionString) {
        setParserState(expressionString, -1);
    }

    public ExpressionParser() {
        setParserState("");
    }

    private Character nextSymbol() {
        if (expressionPos == expressionString.length) {
            return null;
        }

        do {
            ++expressionPos;
        } while (expressionPos < expressionString.length
                && Character.isWhitespace(expressionString[expressionPos]));

        if (expressionPos == expressionString.length) {
            return null;
        }

//        System.out.println("[DEBUG] " + expressionString.charAt(expressionPos) + " will be returned, with current pos = " + expressionPos);

        return expressionString[expressionPos];
    }

    private void returnSymbol() {
        expressionPos--;
//        System.out.println("[DEBUG] Symbol returned...");
    }

    private boolean compareInputToken(char[] token) {
        int pos = expressionPos;
        int tokenLen = token.length;
        int tokenPos = 0;

        while (tokenPos < tokenLen) {
            if (pos < expressionString.length && expressionString[pos] == token[tokenPos]) {
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
            if (compareInputToken(token.charArrayRepresentation)) {
                expressionPos += token.charArrayRepresentation.length - 1;
                return token;
            }
        }

        // Parenthesis
        for (Token.ArithmeticToken token : Token.ArithmeticToken.values()) {
            if (compareInputToken(token.charArrayRepresentation)) {
                expressionPos += token.charArrayRepresentation.length - 1;
                return token;
            }
        }

        // Unary operation
        for (Token.UnaryOperationToken token : Token.UnaryOperationToken.values()) {
            if (compareInputToken(token.charArrayRepresentation)) {
                expressionPos += token.charArrayRepresentation.length - 1;
                return token;
            }
        }

        // Binary operation
        for (Token.BinaryOperationToken token : Token.BinaryOperationToken.values()) {
            if (compareInputToken(token.charArrayRepresentation)) {
                expressionPos += token.charArrayRepresentation.length - 1;
                return token;
            }
        }

        // Some unexpected token... we don't know, what to do.. :(
        return Token.SystemToken.UNEXPECTED;
    }

    private void returnToken(String token) {
        expressionPos -= token.length();
    }

    private TripleExpression parseExpressionObject(boolean negated) throws ParseException {
//        System.out.println("[DEBUG] parseExpressionObject called with negated = " + negated);
        Object token = parseToken();

        if (token instanceof Token.ConstantToken) {
            return new CheckedConst(Integer.parseInt(((negated) ? "-" : "") + ((Token.ConstantToken) token).value));
        } else if (token instanceof Token.VariableToken) {
            if (negated) {
                return new CheckedNegate(new CheckedVariable(((Token.VariableToken) token).stringRepresentation));
            }
            return new CheckedVariable(((Token.VariableToken) token).stringRepresentation);
        } else if (token instanceof Token.UnaryOperationToken) {
//            TripleExpression innerObject = parseExpressionObject();

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
//                    System.out.println(innerObject.nameSelf());
//                    System.out.println(innerObject instanceof CheckedConst);
            }
        } else if (token instanceof Token.ArithmeticToken) {
            if (token == Token.ArithmeticToken.OPENING_PARENTHESIS) {
                TripleExpression innerExpression = parseExpression(0, true);

                token = parseToken();
                if (token != Token.ArithmeticToken.CLOSING_PARENTHESIS) {
                    throw new ParseException(
                            "[ERROR] Closing parenthesis expected, unexpected token found :(",
                            expressionPos
                    );
                }

                return (negated) ? new CheckedNegate(innerExpression) : innerExpression;
            } else {
                throw new ParseException(
                        "[ERROR] Unexpected closing parenthesis `" +
                                Token.ArithmeticToken.CLOSING_PARENTHESIS.stringRepresentation + "` found",
                        expressionPos
                );
            }
        }

        throw new ParseException("[ERROR] Unexpected token found :(", expressionPos);
    }

    private TripleExpression parseExpression(int minPriority, boolean isRecursive) throws ParseException {
//        System.out.println("[DEBUG] parseExpression called with minPriority = " + minPriority + " and isRecursive = " + isRecursive);
        TripleExpression left = parseExpressionObject(false);

        while (true) {
            Object token = parseToken();

            if (token == Token.UnaryOperationToken.NEGATE) {
                token = Token.BinaryOperationToken.SUBTRACT;
            }

            if (token == Token.SystemToken.EOF) {
                return left;
            } else if (token == Token.ArithmeticToken.CLOSING_PARENTHESIS && isRecursive) {
                returnToken(((Token.ArithmeticToken) token).stringRepresentation);
                return left;
            } else if (token instanceof Token.BinaryOperationToken) {
                if (((Token.BinaryOperationToken) token).priority <= minPriority) {
                    returnToken(((Token.BinaryOperationToken) token).stringRepresentation);
                    return left;
                }
                // else continue working
            } else {
                throw new ParseException(
                        "[ERROR] Unexpected token found :(",
                        expressionPos
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
//        System.out.println("[DEBUG] Expression received: " + expression);
//        try {
//            TripleExpression te = parseExpression(0, false);
//            System.out.println("[DEBUG] Expression successfully parsed");
//            System.out.println(te.nameSelf());
//            return te;
//        } catch (ParseException e) {
//            System.out.println("[DEBUG] Parse Error: " + e.getMessage());
//            throw e;
//        }
    }
}
