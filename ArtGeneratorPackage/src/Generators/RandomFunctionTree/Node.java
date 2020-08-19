/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree;

import Generators.RandomFunctionTree.Functions.Function;

/**
 *
 * This class helps build the root and the children of the random function tree.
 * @author Ardamelih
 */
public class Node {

    String data;
    double returnValue;
    Node left;
    Node middle;
    Node right;
    private Function function;

    public Node(Function function) {
        left = null;
        right = null;
        middle = null;
        this.function = function;
    }


    //this method decides which of the three children will be built, based on the number of the parameters the assigned function has.
    public void createChildren(int depth) throws NumberFormatException {
        if (depth < 0) throw new NumberFormatException();

        left = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
        if (depth >= 1) {
            left.createChildren(depth - 1);
        }

        if (function.numberOfParameters() == 2) {
            right = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
            if (depth >= 1) {
                right.createChildren(depth - 1);
            }
        }
        if (function.numberOfParameters() == 3) {
            middle = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
            if (depth >= 1) {
                middle.createChildren(depth - 1);
                right = new Node(RandomFunctionTreeGenerator.functionGenerator.getRandomFunction());
                if (depth >= 1) {
                    //  System.out.println("create children of right node");
                    right.createChildren(depth - 1);
                }

            }
        }
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double calculate(double x, double y, double z) {
        if (left != null) {
            //System.out.println("Calculate left child");
            x = left.calculate(x, y, z);
        }
        if (middle != null) {
            //System.out.println("Calculate right child");
            z = middle.calculate(x, y, z);
        }
        if (right != null) {
            //System.out.println("Calculate right child");
            y = right.calculate(x, y, z);
        }

        // System.out.println("Calculate with func=" + function.name());
        return function.calculate(x, y, z);
    }

}

