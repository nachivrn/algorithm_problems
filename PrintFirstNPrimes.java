// count = 3, prints 2,3,5
// count = 4, prints 2,3,5,7
public class PrintNPrimes {
    static void printFirstNPrimeNumbers(int count) {
        int nextPrime = 2;
        if (count == 0) {
            return;
        }
        System.out.print(nextPrime + ", ");
        if (count == 1) {
            System.out.print(nextPrime);
            return;
        } else {
            System.out.print(nextPrime + ", ");
        }
        for (int i = 1; i < count; i++) {
            while (true) {
                nextPrime = nextPrime + 1;
                if (isComposite(nextPrime)) {
                    continue;
                } else {
                    if (i == count) {
                        System.out.println(nextPrime);
                    } else {
                        System.out.print(nextPrime + ", ");
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


