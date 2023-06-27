package org.mb.tedd.main;

import java.util.ArrayList;
import java.util.List;
import org.mb.tedd.utils.Graph;
import org.mb.tedd.utils.Properties;
import org.mb.tedd.algorithm.execution.TestCaseExecutor;
import org.mb.tedd.algorithm.execution.TestResult;
import java.io.PrintWriter;

public class ExLinear
{
    private static List<String> getTests()
    {
        final List<String> tests = new ArrayList<>();
        for (final String test : Properties.tests_order) tests.add(test);
        return tests;
    }

    private static Graph<String> algorithm() throws Exception
    {
        final TestCaseExecutor<String> executor = new TestCaseExecutor<>();
        final List<String> tests = getTests();

        final Graph<String> g = new Graph<>(tests);
        for (int i = 0; i < tests.size(); ++i) {
            final List<String> schedule = new ArrayList<>(tests);
            schedule.remove(i);
            List<TestResult> results = executor.runTests(schedule);

            int first_failed = results.indexOf(TestResult.FAIL);

            while (first_failed != -1) {
                g.addEdge(schedule.get(first_failed), tests.get(i));
                schedule.remove(first_failed);
                results = executor.runTests(schedule);
                first_failed = results.indexOf(TestResult.FAIL);
            }
        }

        g.transitiveReduction();
        return g;
    }

    public static void main(final String[] args)
    {
        try {
            if (args.length < 1) {
                System.out.println("Need to provide test application");
                System.exit(1);
            }
            Properties.getInstance().createPropertiesFile();
            Graph<String> result = algorithm();

            PrintWriter out = new PrintWriter(args[0] + "-ex-linear.gv");
            out.print(result.toString());
            out.close();

            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
            System.err.println(e.getStackTrace());
        }
    }
}
