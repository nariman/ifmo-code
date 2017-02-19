package info.kgeorgiy.java.advanced.arrayset;

import net.java.quickcheck.collection.Pair;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;

import static net.java.quickcheck.generator.CombinedGeneratorsIterables.somePairs;
import static net.java.quickcheck.generator.PrimitiveGenerators.fixedValues;
import static org.junit.Assert.assertEquals;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NavigableSetTest extends SortedSetTest {
    @Test
    public void test18_lower() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            for (final Integer element : inAndOut(elements)) {
                assertEquals(
                        "in lower(" + element + ") (comparator = " + comparator + ", elements = " + elements + ")",
                        treeSet.lower(element),
                        set.lower(element)
                );
            }
        }
    }

    @Test
    public void test19_ceiling() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            for (final Integer element : inAndOut(elements)) {
                assertEquals(
                        "in ceiling(" + element + ") (comparator = " + comparator + ", elements = " + elements + ")",
                        treeSet.ceiling(element),
                        set.ceiling(element)
                );
            }
        }
    }

    @Test
    public void test20_higher() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            for (final Integer element : inAndOut(elements)) {
                assertEquals(
                        "in higher(" + element + ") (comparator = " + comparator + ", elements = " + elements + ")",
                        treeSet.higher(element),
                        set.higher(element)
                );
            }
        }
    }

    @Test
    public void test21_floor() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            for (final Integer element : inAndOut(elements)) {
                assertEquals(
                        "in floor(" + element + ") (comparator = " + comparator + ", elements = " + elements + ")",
                        treeSet.floor(element),
                        set.floor(element)
                );
            }
        }
    }

    @Test
    public void test22_navigableTailSet() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            for (final Integer element : inAndOut(elements)) {
                assertEq(
                        set.tailSet(element, true),
                        treeSet.tailSet(element, true),
                        "in tailSet(" + element + ", true) (comparator = " + comparator + ", elements = " + elements + ")"
                );
                assertEq(
                        set.tailSet(element, false),
                        treeSet.tailSet(element, false),
                        "in tailSet(" + element + ", false) (comparator = " + comparator + ", elements = " + elements + ")"
                );
            }
        }
    }

    @Test
    public void test23_navigableHeadSet() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            for (final Integer element : inAndOut(elements)) {
                assertEq(
                        set.headSet(element, true),
                        treeSet.headSet(element, true),
                        "in headSet(" + element + ", true) (comparator = " + comparator + ", elements = " + elements + ")"
                );
                assertEq(
                        set.headSet(element, false),
                        treeSet.headSet(element, false),
                        "in headSet(" + element + ", false) (comparator = " + comparator + ", elements = " + elements + ")"
                );
            }
        }
    }

    @Test
    public void test24_navigableSubSet() {
        for (final Pair<NamedComparator, List<Integer>> pair : withComparator()) {
            final List<Integer> elements = pair.getSecond();
            final Comparator<Integer> comparator = pair.getFirst();
            final NavigableSet<Integer> set = set(elements, comparator);
            final NavigableSet<Integer> treeSet = treeSet(elements, comparator);

            final Collection<Integer> all = values(elements);
            for (final Pair<Integer, Integer> p : somePairs(fixedValues(all), fixedValues(all))) {
                final Integer from = p.getFirst();
                final Integer to = p.getSecond();
                if (comparator.compare(from, to) <= 0) {
                    for (int i = 0; i < 4; i++) {
                        assertEq(
                                set.subSet(from, i % 2 == 1, to, i / 2 == 1),
                                treeSet.subSet(from, i % 2 == 1, to, i / 2 == 1),
                                String.format("in subSet(%d, %b, %d, %b) (comparator = %s, elements = %s",
                                        from, i %2 == 1,
                                        to, i / 2 == 1,
                                        comparator, elements
                                )
                        );
                    }
                }
            }
        }
    }

    protected NavigableSet<Integer> set(final List<Integer> elements, final Comparator<Integer> comparator) {
        return (NavigableSet<Integer>) super.set(elements, comparator);
    }


    protected NavigableSet<Integer> treeSet(final List<Integer> elements, final Comparator<Integer> comparator) {
        return (NavigableSet<Integer>) super.treeSet(elements, comparator);
    }
}
