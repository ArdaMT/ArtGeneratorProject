package Generators.RandomFunctionTree.Functions;

public class FunctionH  extends Function {


    @Override
    public double calculate(double x, double y, double z) {
        return (1 + Math.cos(x * 2 * Math.PI)) / 2;
    }

    public int numberOfParameters() {
        return 1;
    }
}
