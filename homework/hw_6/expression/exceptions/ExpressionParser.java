/**
 * Nariman Safiulin (woofilee)
 * File: ExpressionParser.java
 * Created on: Mar 28, 2016
 */

package expression.exceptions;

import expression.*;

import java.io.IOException;
import java.text.ParseException;

public class ExpressionParser implements Parser {
    private String expressionString = "";
    private int expressionPos = 0;

    private enum BinaryOperatorToken {
        ADD("+", 1),
        SUBTRACT("-", 1),
        MULTIPLY("*", 2),
        DIVIDE("/", 2);

        public final String stringRepr;
        public final int priority;
//        public final Class<? extends CheckedAbstractBinaryOperation> operatorClass;

        BinaryOperatorToken(String stringRepr, int priority) {
            this.stringRepr = stringRepr;
            this.priority = priority;
//            this.operatorClass = operatorClass;
        }
    }

    private enum VariableToken {
        X("x"),
        Y("y"),
        Z("z");

        public final String stringRepr;

        VariableToken(String stringRepr) {
            this.stringRepr = stringRepr;
        }
    }

    private char nextSym() throws IOException {
        while (expressionPos + 1 < expressionString.length() && expressionString.charAt(expressionPos) == ' ') {
            expressionPos++;
        }

        if (expressionPos == expressionString.length()) {
            throw new IOException("[ERROR] Right arithmetic expression expected");
        }

        return expressionString.charAt(expressionPos++);
    }

    private void returnSym() {
        expressionPos--;
    }

    private String parseToken() throws IOException {
        char c = nextSym();

        // Integer
        if (Character.isDigit(c)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isDigit(c)) {
                sb.append(c);
                c = nextSym();
            }

            returnSym();
            return sb.toString();
        }

        // Object
        return String.valueOf(c);
    }

    private void returnToken(String token) {
        expressionPos -= token.length();
    }

    private BinaryOperatorToken parseBinaryOperator() throws ParseException, IOException {
        String operator = parseToken();

        for (BinaryOperatorToken bot : BinaryOperatorToken.values()) {
            if (bot.stringRepr.equals(operator)) {
                return bot;
            }
        }

        throw new ParseException(
                "Binary operator expected, `" + operator + "` found",
                expressionPos - operator.length()
        );
    }

    private TripleExpression parseExpressionObject() throws ParseException, IOException {
        String token = parseToken();

        // CheckedConst integer
        if (Character.isDigit(token.charAt(0))) {
            return new CheckedConst(Integer.parseInt(token));
        }

        // CheckedVariable integer
        for (VariableToken variableToken : VariableToken.values()) {
            if (variableToken.stringRepr.equals(token)) {
                return new CheckedVariable(token);
            }
        }

        // Inner expression
        if (token.equals("(")) {
            TripleExpression innerExpression = parseExpression(0);

            char c = nextSym();
            if (c != ')') {
                throw new ParseException(
                        "Closing parenthesis expected, `" + String.valueOf(c) + "` found",
                        expressionPos - 1
                );
            }

            return innerExpression;
        }

        // CheckedNegate operation
        return new CheckedNegate(parseExpressionObject());
    }

    private TripleExpression parseExpression(int minPriority) throws ParseException, IOException {
        TripleExpression left = parseExpressionObject();

        while (true) {
            BinaryOperatorToken operator;
            try {
                operator = parseBinaryOperator();
            } catch (IOException e) {
                return left;
            }

            if (operator.priority <= minPriority) {
                returnToken(operator.stringRepr);
                return left;
            }

            TripleExpression right = parseExpression(operator.priority);

            switch (operator) {
                case ADD:
                    left = new CheckedAdd(left, right);
                    break;
                case SUBTRACT:
                    left = new CheckedSubtract(left, right);
                    break;
                case MULTIPLY:
                    left = new CheckedMultiply(left, right);
                    break;
                case DIVIDE:
                    left = new CheckedDivide(left, right);
                    break;
            }
        }
    }

    @Override
    public TripleExpression parse(String expression) throws ParseException, IOException {
        expressionString = expression;
        expressionPos = 0;

//        try {
        return parseExpression(0);
//        } catch (ParseException e) {
//            System.out.println("[PARSE] Error while parsing: " + e.getMessage());
//            return new CheckedConst(0);
//        }
    }
}
