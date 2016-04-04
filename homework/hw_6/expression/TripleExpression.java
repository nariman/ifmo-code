package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression {
    String nameSelf();
    int evaluate(int x, int y, int z);
}
