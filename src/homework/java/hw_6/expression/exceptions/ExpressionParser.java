/**
 * Nariman Safiulin (woofilee)
 * File: ExpressionParser.java
 * Created on: Apr 23, 2016
 */

package expression.exceptions;

import expression.*;

class ParseException extends java.text.ParseException {
    public ParseException(String s, int errorOffset) {
        super(String.format(s, errorOffset), errorOffset);
    }
}

public class ExpressionParser implements Parser {
    private Tokenizer tokenizer;

    public ExpressionParser() {
        this.tokenizer = new Tokenizer();
    }

    @Override
    public TripleExpression parse(String expression) throws ParseException {
        tokenizer.ready(expression.toCharArray());
        return parseExpression(0, false);
    }

    private int priority(Token token) {
        if (token == Token.LOG || token == Token.POW) {
            return 3;
        } else if (token == Token.DIVIDE || token == Token.MULTIPLY) {
            return 2;
        }
        return 1;
    }

    private TripleExpression parseExpressionObject() throws ParseException {
        switch (tokenizer.next()) {
            // ARITHMETIC
            case OPENING_PARENTHESIS:
                TripleExpression innerExpression = parseExpression(0, true);
                if (tokenizer.next() != Token.CLOSING_PARENTHESIS) {
                    throw new ParseException(
                            "[ERROR] Closing parenthesis expected, end of expression reached :( at pos %d",
                            tokenizer.getPosition()
                    );
                }
                return innerExpression;
            // NUMERIC
            case VARIABLE:
                return new Variable(tokenizer.getVariableToken());
            case CONST:
                return new Constant(tokenizer.getConstToken());
            // UNARY
            case ABS:
                return new CheckedAbs(parseExpressionObject());
            case NEGATE:
                return new CheckedNegate(parseExpressionObject());
            case SQRT:
                return new CheckedSqrt(parseExpressionObject());
            // OTHER
            default:
                throw new ParseException(
                        "[ERROR] Constant, variable, unary operation or opening parenthesis expected, error symbol found at pos %d :(",
                        tokenizer.getPosition()
                );
        }
    }

    private TripleExpression parseExpression(int minPriority, boolean isRecursive) throws ParseException {
        TripleExpression left = parseExpressionObject();

        while (true) {
            Token operation = tokenizer.next();

            if (operation == Token.END) {
                return left;
            } else if (operation == Token.CLOSING_PARENTHESIS && isRecursive) {
                tokenizer.prev();
                return left;
            } else if (operation.is(Token.BINARY)) {
                if (priority(operation) <= minPriority) {
                    tokenizer.prev();
                    return left;
                }
                // otherwise all correct
            } else {
                throw new ParseException(
                        "[ERROR] Binary operation or closing parenthesis expected, other token found at pos %d :(",
                        tokenizer.getPosition()
                );
            }

            TripleExpression right = parseExpression(priority(operation), isRecursive);

            switch (operation) {
                case ADD:
                    left = new CheckedAdd(left, right);
                    break;
                case DIVIDE:
                    left = new CheckedDivide(left, right);
                    break;
                case LOG:
                    left = new CheckedLogarithm(left, right);
                    break;
                case MULTIPLY:
                    left = new CheckedMultiply(left, right);
                    break;
                case POW:
                    left = new CheckedPow(left, right);
                    break;
                case SUBTRACT:
                    left = new CheckedSubtract(left, right);
                    break;
            }
        }
    }
}
