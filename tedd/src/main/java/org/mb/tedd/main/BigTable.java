package org.mb.tedd.main;

import com.google.common.base.Preconditions;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.mb.tedd.algorithm.refinement.DependencyRefiner;
import org.mb.tedd.graph.DependencyGraphManager;
import org.mb.tedd.graph.GraphEdge;
import org.mb.tedd.graph.GraphNode;
import org.mb.tedd.graph.dot.exportgraph.GraphExporter;
import org.mb.tedd.utils.ExecutionTime;
import org.mb.tedd.utils.Properties;
import org.mb.tedd.algorithm.execution.TestCaseExecutor;

public class BigTable {
    static void algorithm() {
        try {
            Properties.getInstance().createPropertiesFile();
            TestCaseExecutor<String> testCaseExecutor = new TestCaseExecutor<>();

            List<String> schedule = new ArrayList<>();
            for (String test : Properties.tests_order) schedule.add(test);

            // for( int i =0;i<Properties.tests_order.length;i++) {
            //     // System.out.println( Properties.tests_order[i] );
            //     schedule.add( Properties.tests_order[i] );
            // }
            // testCaseExecutor.executeTestsRemoteJUnitCore( Properties.tests_order );

            testCaseExecutor.executeTestsRemoteJUnitCore(schedule);
        } catch (Exception e) {
            System.out.println("Blyaat");
        }
        
    }

    public static void main(String[] args){
        algorithm();
    }
}
