package info.kgeorgiy.java.advanced.base;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseTester {
    private final Map<String, Class<?>> tests = new HashMap<>();

    public void run(final String[] args) {
        if (args.length != 2 && args.length != 3) {
            printUsage();
        }

        final Class<?> token = tests.get(args[0]);
        if (token == null) {
            printUsage();
            return;
        }

        System.setProperty(BaseTest.CUT_PROPERTY, args[1]);
        final Result result = new JUnitCore().run(token);
        if (!result.wasSuccessful()) {
            for (final Failure failure : result.getFailures()) {
                System.err.println("Test " + failure.getDescription().getMethodName() + " failed: " + failure.getMessage());
                if (failure.getException() != null) {
                    failure.getException().printStackTrace();
                }
            }
            System.exit(1);
        } else {
            System.out.println("============================");
            System.out.println("OK " + token.getSimpleName() + " for " + args[1]);
            certify(token, args.length > 2 ? args[2] : "");
        }
    }

    protected void certify(final Class<?> token, final String salt) {
        try {
            final CG cg = (CG) Class.forName("info.kgeorgiy.java.advanced.base.CertificateGenerator").newInstance();
            cg.certify(token, salt);
        } catch (final ClassNotFoundException e) {
            // Ignore
        } catch (final Exception e) {
            System.err.println("Error running certificate generator");
            e.printStackTrace();
        }
    }

    private void printUsage() {
        System.out.println("Usage:");
        for (final String name : tests.keySet()) {
            System.out.println(String.format("    java %s %s full.class.name [salt]", getClass().getName(), name));
        }
        System.exit(1);
    }

    public BaseTester add(final String name, final Class<?> testClass) {
        tests.put(name, testClass);
        return this;
    }
}
