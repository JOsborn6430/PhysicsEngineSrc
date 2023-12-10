public class OpTesting {
    public static void main(String[] args) {
        double[][] A = {{1,2},
                        {3,3}};
        double[] v = {-1,1};
        double[] r = Op.scalarMultiplyD(v,33);
        Op.printArrayD(v);
        Op.printArrayD(r);
    }
}
