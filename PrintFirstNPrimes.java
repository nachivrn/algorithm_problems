public class PrintFirstNPrimes {
    static void printFirstNPrimeNumbers(int count) {
        System.out.print(2 + ", ");
        if (count == 1) {
            return;
        }
        int lastPrime = 2;
        for (int i = 1; i < count; i++) {
            while (true) {
                lastPrime = lastPrime + 1;
                if (isComposite(lastPrime)) {
                    continue;
                } else {
                    if (i == count) {
                        System.out.println(lastPrime);
                    } else {
                        System.out.print(lastPrime + ", ");
                    }
                    break;
                }
            }
        }
    }

    static boolean isComposite(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage  java -cp . PrintFirstNPrimes number");
        }
        printFirstNPrimeNumbers(Integer.parseInt(args[0]));
    }
}
