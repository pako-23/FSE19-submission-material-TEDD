package org.mb.tedd.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import org.mb.tedd.utils.Graph;
import org.mb.tedd.utils.Properties;
import org.mb.tedd.algorithm.execution.TestCaseExecutor;
import org.mb.tedd.algorithm.execution.TestResult;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BigTable
{

    private static long testRuns = 0;
    private static long testSuiteRuns = 0;

    private static List<String> getTests()
    {
        final List<String> tests = new ArrayList<>();
        for (final String test : Properties.tests_order) tests.add(test);
        return tests;
    }

    private static List<String> getSchedule(List<Map.Entry<Integer, String>> tests)
    {
        List<String> schedule = new ArrayList<>();
        for (final Map.Entry<Integer, String> test : tests)
            schedule.add(test.getValue());
        return schedule;
    }

    private static Set<List<Map.Entry<Integer, String>>> algorithm() throws Exception
    {
        final TestCaseExecutor<String> executor = new TestCaseExecutor<>();
        final List<String> tests = getTests();

        final Set<Map.Entry<Integer, String>> notPassed = new HashSet<>();
        final Map<Integer, List<List<Map.Entry<Integer, String>>>> table =
            new HashMap<>();
        final Set<List<Map.Entry<Integer, String>>> workingSchedules = new HashSet<>();

        for (int i = 0; i < tests.size(); ++i) {
            final String test = tests.get(i);
            List<String> schedule = new ArrayList<>();
            schedule.add(test);
            ++testSuiteRuns;
            ++testRuns;
            if (executor.runTests(schedule).get(0) == TestResult.PASS) {
                if (!table.containsKey(1)) table.put(1, new ArrayList<>());
                List<Map.Entry<Integer, String>> s = new ArrayList<>();
                s.add(Map.entry(i, tests.get(i)));
                table.get(1).add(s);
                workingSchedules.add(s);
            } else notPassed.add(Map.entry(i, tests.get(i)));
        }

        for (int rank = 2; rank <= tests.size(); ++rank) {
            table.put(rank, new ArrayList<>());
            final Set<Map.Entry<Integer, String>> passed = new HashSet<>();


            for (final Map.Entry<Integer, String> test : notPassed) {
                boolean testPassed = false;

                for (final List<Map.Entry<Integer, String>> seq : table.get(rank-1)) {
                    if (seq.get(seq.size()-1).getKey() > test.getKey()) continue;
                    List<String> schedule = getSchedule(seq);
                    schedule.add(test.getValue());
                    List<TestResult> results = executor.runTests(schedule);
                    ++testSuiteRuns;
                    int first_failed = results.indexOf(TestResult.FAIL);
                    if (first_failed == -1) {
                        testRuns += results.size();
                        List<Map.Entry<Integer, String>> s = new ArrayList<>(seq);
                        s.add(test);
                        workingSchedules.add(s);
                        table.get(rank).add(s);
                        passed.add(test);
                        break;
                    } else testRuns += first_failed + 1;
                }

                if (testPassed || test.getKey() != rank-1) continue;


                Set<List<Map.Entry<Integer, String>>> baseSet = new HashSet<>(workingSchedules);
                for (final List<Map.Entry<Integer, String>> base : baseSet) {
                    Set<List<Map.Entry<Integer, String>>> itSet = new HashSet<>(workingSchedules);
                    itSet.remove(base);
                    for (final List<Map.Entry<Integer, String>> it : itSet) {
                        Set<Map.Entry<Integer, String>> set = new HashSet<>(base);
                        set.addAll(it);

                        List<Map.Entry<Integer, String>> s = new ArrayList<>(set);

                        s.sort((a, b) -> {
                            if (a.getKey() > b.getKey()) return 1;
                            else if (a.getKey() < b.getKey()) return -1;
                            else return 0;
                        });

                        if (!table.containsKey(s.size()-1)) table.put(s.size()-1, new ArrayList<>());
                        if (!workingSchedules.contains(s)) {
                            table.get(s.size()-1).add(s);
                            workingSchedules.add(s);
                        }

                        if (s.get(s.size()-1).getKey() < test.getKey()) {
                            s.add(test);
                            List<TestResult> results = executor.runTests(getSchedule(s));
                            ++testSuiteRuns;
                            int first_failed = results.indexOf(TestResult.FAIL);
                            if (first_failed == -1) {
                                testRuns += results.size();
                                if (!table.containsKey(s.size()-1)) table.put(s.size()-1, new ArrayList<>());
                                table.get(s.size()-1).add(s);
                                workingSchedules.add(s);
                                passed.add(test);
                                testPassed = true;
                                break;
                            } else testRuns += first_failed + 1;
                        }
                    }
                    if (testPassed) break;
                }
            }
            notPassed.removeAll(passed);
        }
        return workingSchedules;
    }

    public static void main(String[] args)
    {
      try {
            if (args.length < 1) {
                System.out.println("Need to provide test application");
                System.exit(1);
            }
            Properties.getInstance().createPropertiesFile();
            long startTime = System.currentTimeMillis();
            Set<List<Map.Entry<Integer, String>>> seqs = algorithm();
            long executionTime = System.currentTimeMillis() - startTime;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String stats_filename = args[0] + "-" + timestamp + "-statistics.txt";
            String csv_filename = args[0] + "-" + timestamp + "-big-table.csv";

            PrintWriter out = new PrintWriter( csv_filename );
            for (final List<Map.Entry<Integer, String>> seq : seqs)
                out.println(String.join(", ",  getSchedule(seq)));
            out.close();

            out = new PrintWriter( stats_filename );
            out.println("Test runs: " + testRuns);
            out.println("Test-suite runs: " + testSuiteRuns);
            out.println("Execution time: " + executionTime);
            out.close();

            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
            System.err.println(e.getStackTrace());
        }
    }
}
