/**
 * Nariman Safiulin (woofilee)
 * File: Parser.java
 * Created on: Apr 23, 2016
 */

package expression.parser;

import expression.ExpressionObject;
import expression.object.*;
import expression.object.operation.*;

class ParseException extends java.text.ParseException {
    public ParseException(String s, int errorOffset) {
        super(String.format(s, errorOffset), errorOffset);
    }
}

public class Parser<T> {
    private Tokenizer<T> tokenizer;

    public Parser(Tokenizer<T> tokenizer) {
        this.tokenizer = tokenizer;
    }

    public ExpressionObject<T> parse(String expression) throws ParseException {
        tokenizer.ready(expression.toCharArray());
        return parseExpression(0, false);
    }

    private int priority(Token token) {
        if (token == Token.LOG || token == Token.POW) {
            return 3;
        } else if (token == Token.DIVIDE || token == Token.MOD || token == Token.MULTIPLY) {
            return 2;
        }
        return 1;
    }

    private ExpressionObject<T> parseExpressionObject() throws ParseException {
        switch (tokenizer.next()) {
            // ARITHMETIC
            case OPENING_PARENTHESIS:
                ExpressionObject<T> innerExpression = parseExpression(0, true);
                if (tokenizer.next() != Token.CLOSING_PARENTHESIS) {
                    throw new ParseException(
                            "[ERROR] Closing parenthesis expected, end of expression reached :( at pos %d",
                            tokenizer.getPosition()
                    );
                }
                return innerExpression;
            // NUMERIC
            case VARIABLE:
                return new Variable<>(tokenizer.getVariableToken());
            case CONST:
                return new Constant<>(tokenizer.getConstToken());
            // UNARY
            case ABS:
                return new Abs<>(parseExpressionObject());
            case NEGATE:
                return new Negate<>(parseExpressionObject());
            case SQRT:
                return new Sqrt<>(parseExpressionObject());
            case SQUARE:
                return new Square<>(parseExpressionObject());
            // OTHER
            default:
                throw new ParseException(
                        "[ERROR] Constant, variable, unary operation or opening parenthesis expected, error symbol found at pos %d :(",
                        tokenizer.getPosition()
                );
        }
    }

    private ExpressionObject<T> parseExpression(int minPriority, boolean isRecursive) throws ParseException {
        ExpressionObject<T> left = parseExpressionObject();

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

            ExpressionObject<T> right = parseExpression(priority(operation), isRecursive);

            switch (operation) {
                case ADD:
                    left = new Add<>(left, right);
                    break;
                case DIVIDE:
                    left = new Divide<>(left, right);
                    break;
                case LOG:
                    left = new Log<>(left, right);
                    break;
                case MOD:
                    left = new Mod<>(left, right);
                    break;
                case MULTIPLY:
                    left = new Multiply<>(left, right);
                    break;
                case POW:
                    left = new Pow<>(left, right);
                    break;
                case SUBTRACT:
                    left = new Subtract<>(left, right);
                    break;
            }
        }
    }
}
