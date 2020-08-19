/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators.RandomFunctionTree.Functions;

/**
 * All the function classes inherit from this class and implement the methods below.
 * The number of function classes that inherit from this class is limitless.
 * The calculated values in  the function classes do not follow strict rules.
 * However, as a rule of thumb, trigonometric functions that contain sine and cosine
 * calculations are known to help create more interesting images and shapes.
 *
 * @author Ardamelih
 */
public abstract class Function {
    public abstract double calculate(double x, double y, double z);
    public abstract  int numberOfParameters();

}

