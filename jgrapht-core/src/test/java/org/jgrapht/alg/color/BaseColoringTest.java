/*
 * (C) Copyright 2017-2017, by Dimitrios Michail and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg.color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.util.IntegerVertexFactory;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

/**
 * Base class for coloring tests.
 * 
 * @author Dimitrios Michail
 */
public abstract class BaseColoringTest
{

    public BaseColoringTest()
    {
        super();
    }

    protected abstract VertexColoringAlgorithm<Integer> getAlgorithm(
        Graph<Integer, DefaultEdge> graph);

    protected abstract int getExpectedResultOnDSaturNonOptimalGraph();

    protected int getExpectedResultOnGraph1()
    {
        return 3;
    }

    protected int getExpectedResultOnMyceil3Graph()
    {
        return 4;
    }

    protected int getExpectedResultOnMyceil4Graph()
    {
        return 5;
    }

    protected void assertColoring(
        Graph<Integer, DefaultEdge> g, Coloring<Integer> coloring, int expectedColors)
    {
        int n = g.vertexSet().size();
        assertTrue(coloring.getNumberColors() <= n);
        assertEquals(expectedColors, coloring.getNumberColors());
        Map<Integer, Integer> colors = coloring.getColors();

        for (Integer v : g.vertexSet()) {
            Integer c = colors.get(v);
            assertNotNull(c);
            assertTrue(c >= 0);
            assertTrue(c < n);
        }

        for (DefaultEdge e : g.edgeSet()) {
            assertNotEquals(colors.get(g.getEdgeSource(e)), colors.get(g.getEdgeTarget(e)));
        }
    }

    protected void testRandomGraphColoring(Random rng)
    {
        final int tests = 5;
        final int n = 20;
        final double p = 0.35;

        List<Function<Graph<Integer, DefaultEdge>, VertexColoringAlgorithm<Integer>>> algs =
            new ArrayList<>();
        algs.add((g) -> getAlgorithm(g));

        GraphGenerator<Integer, DefaultEdge, Integer> gen =
            new GnpRandomGraphGenerator<>(n, p, rng, false);

        for (int i = 0; i < tests; i++) {
            Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
            gen.generateGraph(g, new IntegerVertexFactory(), null);

            for (Function<Graph<Integer, DefaultEdge>,
                VertexColoringAlgorithm<Integer>> algProvider : algs)
            {
                VertexColoringAlgorithm<Integer> alg = algProvider.apply(g);
                Coloring<Integer> coloring = alg.getColoring();
                assertTrue(coloring.getNumberColors() <= n);
                Map<Integer, Integer> colors = coloring.getColors();

                for (Integer v : g.vertexSet()) {
                    Integer c = colors.get(v);
                    assertNotNull(c);
                    assertTrue(c >= 0);
                    assertTrue(c < n);
                }

                for (DefaultEdge e : g.edgeSet()) {
                    assertNotEquals(colors.get(g.getEdgeSource(e)), colors.get(g.getEdgeTarget(e)));
                }
            }
        }
    }

    final protected Graph<Integer, DefaultEdge> createGraph1()
    {
        Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addAllVertices(g, Arrays.asList(1, 2, 3, 4, 5));
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(1, 5);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        return g;
    }

    final protected Graph<Integer, DefaultEdge> createMyciel3Graph()
    {
        // This is a graph from http://mat.gsia.cmu.edu/COLOR/instances/myciel3.col.
        // SOURCE: Michael Trick (trick@cmu.edu)
        // DESCRIPTION: Graph based on Mycielski transformation.
        // Triangle free (clique number 2) but increasing coloring number
        Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addAllVertices(g, IntStream.range(1, 12).boxed().collect(Collectors.toList()));

        g.addEdge(1, 2);
        g.addEdge(1, 4);
        g.addEdge(1, 7);
        g.addEdge(1, 9);
        g.addEdge(2, 3);
        g.addEdge(2, 6);
        g.addEdge(2, 8);
        g.addEdge(3, 5);
        g.addEdge(3, 7);
        g.addEdge(3, 10);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        g.addEdge(4, 10);
        g.addEdge(5, 8);
        g.addEdge(5, 9);
        g.addEdge(6, 11);
        g.addEdge(7, 11);
        g.addEdge(8, 11);
        g.addEdge(9, 11);
        g.addEdge(10, 11);

        return g;
    }

    final protected Graph<Integer, DefaultEdge> createMyciel4Graph()
    {
        // This is a graph from http://mat.gsia.cmu.edu/COLOR/instances/myciel4.col.
        // SOURCE: Michael Trick (trick@cmu.edu)
        // DESCRIPTION: Graph based on Mycielski transformation.
        // Triangle free (clique number 2) but increasing coloring number
        Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addAllVertices(g, IntStream.range(1, 24).boxed().collect(Collectors.toList()));

        g.addEdge(1, 2);
        g.addEdge(1, 4);
        g.addEdge(1, 7);
        g.addEdge(1, 9);
        g.addEdge(1, 13);
        g.addEdge(1, 15);
        g.addEdge(1, 18);
        g.addEdge(1, 20);
        g.addEdge(2, 3);
        g.addEdge(2, 6);
        g.addEdge(2, 8);
        g.addEdge(2, 12);
        g.addEdge(2, 14);
        g.addEdge(2, 17);
        g.addEdge(2, 19);
        g.addEdge(3, 5);
        g.addEdge(3, 7);
        g.addEdge(3, 10);
        g.addEdge(3, 13);
        g.addEdge(3, 16);
        g.addEdge(3, 18);
        g.addEdge(3, 21);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        g.addEdge(4, 10);
        g.addEdge(4, 12);
        g.addEdge(4, 16);
        g.addEdge(4, 17);
        g.addEdge(4, 21);
        g.addEdge(5, 8);
        g.addEdge(5, 9);
        g.addEdge(5, 14);
        g.addEdge(5, 15);
        g.addEdge(5, 19);
        g.addEdge(5, 20);
        g.addEdge(6, 11);
        g.addEdge(6, 13);
        g.addEdge(6, 15);
        g.addEdge(6, 22);
        g.addEdge(7, 11);
        g.addEdge(7, 12);
        g.addEdge(7, 14);
        g.addEdge(7, 22);
        g.addEdge(8, 11);
        g.addEdge(8, 13);
        g.addEdge(8, 16);
        g.addEdge(8, 22);
        g.addEdge(9, 11);
        g.addEdge(9, 12);
        g.addEdge(9, 16);
        g.addEdge(9, 22);
        g.addEdge(10, 11);
        g.addEdge(10, 14);
        g.addEdge(10, 15);
        g.addEdge(10, 22);
        g.addEdge(11, 17);
        g.addEdge(11, 18);
        g.addEdge(11, 19);
        g.addEdge(11, 20);
        g.addEdge(11, 21);
        g.addEdge(12, 23);
        g.addEdge(13, 23);
        g.addEdge(14, 23);
        g.addEdge(15, 23);
        g.addEdge(16, 23);
        g.addEdge(17, 23);
        g.addEdge(18, 23);
        g.addEdge(19, 23);
        g.addEdge(20, 23);
        g.addEdge(21, 23);
        g.addEdge(22, 23);

        return g;
    }

    final protected Graph<Integer, DefaultEdge> createDSaturNonOptimalGraph()
    {
        Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        Graphs.addAllVertices(g, IntStream.range(1, 8).boxed().collect(Collectors.toList()));

        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 3);
        g.addEdge(2, 5);
        g.addEdge(4, 6);
        g.addEdge(4, 7);
        g.addEdge(5, 6);
        g.addEdge(5, 7);
        g.addEdge(6, 7);

        return g;
    }

    @Test
    public void testMyciel3()
    {
        Graph<Integer, DefaultEdge> g = createMyciel3Graph();
        assertColoring(g, getAlgorithm(g).getColoring(), getExpectedResultOnMyceil3Graph());
    }

    @Test
    public void testMyciel4()
    {
        Graph<Integer, DefaultEdge> g = createMyciel4Graph();
        assertColoring(g, getAlgorithm(g).getColoring(), getExpectedResultOnMyceil4Graph());
    }

    /**
     * Test instance where DSatur greedy coloring is non-optimal.
     */
    @Test
    public void testDSaturNonOptimal()
    {
        Graph<Integer, DefaultEdge> g = createDSaturNonOptimalGraph();
        assertColoring(
            g, getAlgorithm(g).getColoring(), getExpectedResultOnDSaturNonOptimalGraph());
    }

    @Test
    public void testGraph1()
    {
        Graph<Integer, DefaultEdge> g = createGraph1();
        assertColoring(g, getAlgorithm(g).getColoring(), getExpectedResultOnGraph1());
    }

    @Test
    public void testCompleteGraph()
    {
        final int n = 20;
        Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        CompleteGraphGenerator<Integer, DefaultEdge> gen = new CompleteGraphGenerator<>(n);
        gen.generateGraph(g, new IntegerVertexFactory(), null);
        Coloring<Integer> coloring = getAlgorithm(g).getColoring();
        assertEquals(n, coloring.getNumberColors());
    }

    @Test
    public void testRandomFixedSeed17()
    {
        final long seed = 17;
        Random rng = new Random(seed);
        testRandomGraphColoring(rng);
    }

    @Test
    public void testRandom()
    {
        Random rng = new Random();
        testRandomGraphColoring(rng);
    }

}
