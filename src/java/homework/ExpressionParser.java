/**
 * Nariman Safiulin (woofilee)
 * File: ExpressionParser.java
 * Created on: Mar 28, 2016
 */

package expression.exceptions;

import expression.*;

import java.text.ParseException;
import java.util.ArrayList;

public class ExpressionParser implements Parser {
    private Tokenizer tokenizer;

//    private TripleExpression parseExpressionObject(boolean negated) throws ParseException {
//        Object token = parseToken();
//
//        if (token instanceof Token.ConstantToken) {
//            return new CheckedConst(Integer.parseInt(((negated) ? "-" : "") + ((Token.ConstantToken) token).value));
//        } else if (token instanceof Token.VariableToken) {
//            if (negated) {
//                return new CheckedNegate(new CheckedVariable(((Token.VariableToken) token).string));
//            }
//            return new CheckedVariable(((Token.VariableToken) token).string);
//        } else if (token instanceof Token.UnaryOperationToken) {
//            switch (((Token.UnaryOperationToken) token)) {
//                case SQRT:
//                    if (negated) {
//                        return new CheckedNegate(new CheckedSqrt(parseExpressionObject(false)));
//                    }
//                    return new CheckedSqrt(parseExpressionObject(false));
//                case ABS:
//                    if (negated) {
//                        return new CheckedNegate(new CheckedAbs(parseExpressionObject(false)));
//                    }
//                    return new CheckedAbs(parseExpressionObject(false));
//                case NEGATE:
//                    TripleExpression innerObject = parseExpressionObject(true);
//                    return (negated) ? new CheckedNegate(innerObject) : innerObject;
//            }
//        } else if (token instanceof Token.ArithmeticToken) {
//            if (token == Token.ArithmeticToken.OPENING_PARENTHESIS) {
//                TripleExpression innerExpression = parseExpression(0, true);
//
//                token = parseToken();
//                if (token != Token.ArithmeticToken.CLOSING_PARENTHESIS) {
//                    throw new ParseException(
//                            "[ERROR] Closing parenthesis expected, unexpected token found :(",
//                            position
//                    );
//                }
//
//                return (negated) ? new CheckedNegate(innerExpression) : innerExpression;
//            } else {
//                throw new ParseException(
//                        "[ERROR] Unexpected closing parenthesis `" +
//                                Token.ArithmeticToken.CLOSING_PARENTHESIS.string + "` found",
//                        position
//                );
//            }
//        }
//
//        throw new ParseException("[ERROR] Unexpected token found :(", position);
//    }
//
//    private TripleExpression parseExpression(int minPriority, boolean isRecursive) throws ParseException {
//        TripleExpression left = parseExpressionObject(false);
//
//        while (true) {
//            Object token = parseToken();
//
//            if (token == Token.UnaryOperationToken.NEGATE) {
//                token = Token.BinaryOperationToken.SUBTRACT;
//            }
//
//            if (token == Token.SystemToken.EOF) {
//                return left;
//            } else if (token == Token.ArithmeticToken.CLOSING_PARENTHESIS && isRecursive) {
//                returnToken(")");
//                return left;
//            } else if (token instanceof Token.BinaryOperationToken) {
//                if (((Token.BinaryOperationToken) token).priority <= minPriority) {
//                    returnToken(((Token.BinaryOperationToken) token).string);
//                    return left;
//                }
//                // else continue working
//            } else {
//                throw new ParseException(
//                        "[ERROR] Unexpected token found :(",
//                        position
//                );
//            }
//
//            TripleExpression right = parseExpression(((Token.BinaryOperationToken) token).priority, true);
//
//            switch ((Token.BinaryOperationToken) token) {
//                case LOGARITHM:
//                    left = new CheckedLogarithm(left, right);
//                    break;
//                case POW:
//                    left = new CheckedPow(left, right);
//                    break;
//                case DIVIDE:
//                    left = new CheckedDivide(left, right);
//                    break;
//                case MULTIPLY:
//                    left = new CheckedMultiply(left, right);
//                    break;
//                case ADD:
//                    left = new CheckedAdd(left, right);
//                    break;
//                case SUBTRACT:
//                    left = new CheckedSubtract(left, right);
//                    break;
//            }
//        }
//    }

    @Override
    public TripleExpression parse(String expression) throws ParseException {
        tokenizer = new Tokenizer(expression);
        tokenizer.registerMatchingToken(ArithmeticToken.class);
        tokenizer.registerMatchingToken(UnaryOperationToken.class);
        return parseExpression(0, false);
    }

    private static class Tokenizer {
        private char[] expression;
        private int position;
        private ArrayList<Class> registeredMatchingTokens;

        private void setState(String expression, int position) {
            this.expression = expression.toCharArray();
            this.position = position;
            this.registeredMatchingTokens = new ArrayList<>();
        }

        void setState(String expressionString) {
            setState(expressionString, -1);
        }

        void registerMatchingToken(MatchingToken token) {
            if (token.isInstance(MatchingToken.class)) {
                registeredMatchingTokens.add(token);
            }
        }

        Tokenizer(String expression) {
            setState(expression);
        }

        Token next() {
            Character c = symbol();

            if (c == null) {
                return new SystemToken(SystemToken.Type.EOF);
            }

            // Integer
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();

                while (c != null && Character.isDigit(c)) {
                    sb.append(c);
                    c = symbol();
                }

                returnSymbol();
                return new ConstantToken(sb.toString());
            }

            for (Class tokenClass: registeredMatchingTokens) {
                for (Enum token : tokenClass.)
            }


            // Some unexpected token... we don't know, what to do.. :(
            return new SystemToken(SystemToken.Type.UNEXPECTED);
        }

        private Character symbol() {
            ++position;
            while (position < expression.length
                    && Character.isWhitespace(expression[position])) {
                ++position;
            }

            if (position >= expression.length) {
                position = expression.length;
                return null;
            }

            return expression[position];
        }

        private void returnSymbol() {
            position--;
        }

        private boolean compare(char[] token) {
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

        abstract static class Token {
        }

        abstract static class MatchingToken extends Token {
            Enum type;

            MatchingToken(Enum type) {
                this.type = type;
            }

            public static Enum[] getValues() {
                return new Enum[]{};
            }
        }

        static class SystemToken extends Tokenizer.Token {
            Enum type;

            SystemToken(Enum type) {
                this.type = type;
            }

            enum Type {
                UNEXPECTED,
                EOF
            }
        }

        static class ConstantToken extends Tokenizer.Token {
            String value;

            ConstantToken(String value) {
                this.value = value;
            }
        }
    }

    private static class VariableToken extends Tokenizer.MatchingToken {
        VariableToken(Enum type) {
            super(type);
        }

        enum Type {
            X("x"),
            Y("y"),
            Z("z");

            public final char[] representation;

            Type(String string) {
                this.representation = string.toCharArray();
            }
        }

        public static Type[] getValues() {
            return Type.values();
        }
    }

    private static class ArithmeticToken extends Tokenizer.MatchingToken {
        ArithmeticToken(Enum type) {
            super(type);
        }

        enum Type {
            OPENING_BRACKET("("),
            CLOSING_BRACKET(")");

            public final char[] representation;

            Type(String string) {
                this.representation = string.toCharArray();
            }
        }

        public static Type[] getValues() {
            return Type.values();
        }
    }

    private static class UnaryOperationToken extends Tokenizer.MatchingToken {
        UnaryOperationToken(Enum type) {
            super(type);
        }

        enum Type {
            SQRT("sqrt"), // Longer first
            ABS("abs"),
            NEGATE("-");

            public final char[] representation;

            Type(String string) {
                this.representation = string.toCharArray();
            }
        }

        public static Type[] getValues() {
            return Type.values();
        }
    }

    private static class BinaryOperationToken extends Tokenizer.MatchingToken {
        BinaryOperationToken(Enum type) {
            super(type);
        }

        enum Type {
            LOGARITHM("//", 3), // High priority first, Longer first
            POW("**", 3),
            DIVIDE("/", 2),
            MULTIPLY("*", 2),
            ADD("+", 1),
            SUBTRACT("-", 1);

            public final char[] representation;
            public final int priority;

            Type(String string, int priority) {
                this.representation = string.toCharArray();
                this.priority = priority;
            }
        }

        public static Type[] getValues() {
            return Type.values();
        }
    }
}
