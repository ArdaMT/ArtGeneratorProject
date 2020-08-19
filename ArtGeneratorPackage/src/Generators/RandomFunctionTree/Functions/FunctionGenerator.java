/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree.Functions;

import Generators.RandomFunctionTree.Functions.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class creates  adds a selection functions in a list.
 * @author Ardamelih
 */
public class FunctionGenerator {
    private final ArrayList<Function> functionList = new ArrayList<>();
    private final Random rn;

    public FunctionGenerator(int seed) {
        rn = new Random(seed);
        //here all or some of the functions can be added to see which combination produces more interesting results.
        functionList.add(new FunctionA());
        functionList.add(new FunctionB());
        functionList.add(new FunctionC());
        functionList.add(new FunctionD());
        functionList.add(new FunctionE());
        functionList.add(new FunctionF());
      //  functionList.add(new FunctionG());
      //  functionList.add(new FunctionH());

        
    }

    public Function getRandomFunction() {
        return functionList.get(rn.nextInt(functionList.size()));
    }
 }
