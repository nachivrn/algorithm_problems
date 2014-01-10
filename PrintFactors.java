/** Print factor of a number
 *  Example PrintFactors 21 :
 *  21 * 1
 *  7 * 3  
 */

import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class PrintFactors {
    static boolean isComposite(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0)
                return true;
        }

        return false;
    }

    static List<List<Integer>> getFactors(int value, int max) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        for (int number2 = 2; number2 <= value / 2; number2++) {
            if (value % number2 == 0) {
                int number1 = value / number2;
                if (number1 <= max) {
                    List<Integer> f = new ArrayList<Integer>();

                    if (number1 >= number2) {
                        f.add(number1);
                        f.add(number2);
                        result.add(f);
                    }

                    if (isComposite(number2)) {

                        for (List<Integer> factors : getFactors(number2, number1)) {
                            f = new ArrayList<Integer>();
                            f.add(number1);
                            f.addAll(factors);

                            result.add(f);
                        }
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage  java -cp . PrintFactors number");
        }
        System.out.println(args[0] +"* 1");
        for (List<Integer> factors : getFactors(Integer.parseInt(args[0]), Integer.parseInt(args[0]))) {
            int count = 1;
            for (int num : factors) {
                if (count != factors.size()) {
                    System.out.print(num + " * ");
                    count++;
                } else {
                    System.out.print(num);
                }
            }
            System.out.println();
        }
    }
}
