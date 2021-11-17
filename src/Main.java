public class Main {
    public static void main(String[] args) {
        var big1 = new BigNumber("-815");
        var big2 = new BigNumber("-481516");

        System.out.println(big1);
        System.out.println(big1.shiftRight(4));
        System.out.println(big1.shiftLeft(4));

        System.out.println(big1.plus(big2));
        System.out.println(big1.minus(big2));
    }
}
