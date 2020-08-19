package Generators.RandomFunctionTree;

//contains two variables allowing to keep two offset values in one object.
public class OffsetPair{
    private int offSetX;
    private int offSetY;

    public OffsetPair(int offSetX,int offSetY){
        this.offSetX=offSetX;
        this.offSetY=offSetY;
    }

    public int getOffSetX() {
        return offSetX;
    }

    public int getOffSetY() {
        return offSetY;
    }
}
