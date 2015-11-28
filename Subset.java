public class Susbet {

    public static void generateSubset(char[] subsetElements) {
        boolean[] printArr = new boolean[subsetElements.length];
        generateSubset(printArr, subsetElements, 0, subsetElements.length - 1);
    }

    private static void generateSubset(boolean[] printArr, char[] subsetElements, int currentIndex, int N){
        if (currentIndex == N) {
            printArr[currentIndex] = false;
            printSubset(printArr, subsetElements, N);
            printArr[currentIndex] = true;
            printSubset(printArr, subsetElements, N);
            return;
        }
        printArr[currentIndex] = false;
        generateSubset(printArr, subsetElements, currentIndex + 1, N);
        printArr[currentIndex] = true;
        generateSubset(printArr, subsetElements, currentIndex + 1, N);
    }

    private static void printSubset(boolean[] printArr, char[] subsetElements, int N) {
        boolean firstElement = false;
        for (int i = 0; i <= N; i++) {
            if (printArr[i]) {
                if (!firstElement) {
                    System.out.print("["+ subsetElements[i]);
                    firstElement = true;
                }
                else {
                    System.out.print(" , " + subsetElements[i]);
                }
            }
        }
        if (firstElement)
            System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args) {
        char[] subsetElements = new char[] {'A', 'B', 'C', 'D'};
        generateSubset(subsetElements);
    }
}
