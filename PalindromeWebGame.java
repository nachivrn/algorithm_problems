package org;

import java.util.*;
import java.util.concurrent.*;


public class PalindromeWebGame extends Thread {
    private static final int MAX_SIZE = 3;
    private final ConcurrentMap<String, Integer> playerMap = new ConcurrentHashMap<String, Integer>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_SIZE);
    protected BlockingQueue queue = null;
    protected boolean isStopped = false;

    private PalindromeWebGame(BlockingQueue queue) {
        this.queue = queue;
        resetServer();
    }

    public void submitUserString(String userName, String palindromeStr) {
        if (userName == null) {
            System.out.println("Invalid user name");
            return;
        }
        int score = computeScoreForUser(palindromeStr, userName);
        synchronized (playerMap) {
            if (playerMap.containsKey(userName)) {
                score += playerMap.get(userName);
                playerMap.replace(userName, score);
            } else {
                playerMap.put(userName, score);
            }
        }
        System.out.println(Thread.currentThread().getName()
                + " executing palindrome string " + palindromeStr + "  submitted by " + userName);
    }

    private int computeScoreForUser(String palindromeStr, String name) {
        int score = 0;

        if (palindromeStr == null || palindromeStr.length() < 2)
            return 0;

        if (checkIfStringPalindrome(palindromeStr)) {
            score = PalindromeUtil.getSize(palindromeStr) / 2;
        }

        return score;
    }

    private boolean checkIfStringPalindrome(String palindromeStr) {
        return PalindromeUtil.isPalindrome(palindromeStr);
    }

    public LinkedHashMap findHallOfFamers() {
        if (playerMap.size() == 0)
            return (LinkedHashMap) Collections.emptyMap();
        return sortMapByValues(playerMap);
    }

    public void resetServer() {
        for (String key : playerMap.keySet()) {
            playerMap.replace(key, 0);
        }
    }

    public synchronized void stopGame() {
        this.isStopped = true;
    }

    private LinkedHashMap sortMapByValues(Map<String, Integer> unsortedMap) {
        int count = 0;
        List<Map.Entry> entryList = new LinkedList(unsortedMap.entrySet());
        LinkedHashMap sortedMap = new LinkedHashMap();
        Collections.sort(entryList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) o2).getValue()).
                        compareTo(((Map.Entry) o1).getValue());
            }
        });

        for (Map.Entry entry : entryList) {
            if (count < 5) {
                sortedMap.put(entry.getKey(), entry.getValue());
                count++;
            } else {
                break;
            }
        }
        return sortedMap;
    }

    public void run() {
        String gameplay = null;
        while (!isStopped) {
            try {
                gameplay = (String) queue.take();
            } catch (InterruptedException e) {
            }
            if (gameplay != null) {
                executorService.execute(new Worker(gameplay));
                gameplay = null;
            }
        }
        executorService.shutdown();
        System.out.println("Server Stopped.");
    }

    class Worker implements Runnable {
        private String gameplayStr;

        public Worker(String str) {
            this.gameplayStr = str;
        }

        public void run() {
            String[] gameplayArr = gameplayStr.split(":");
            submitUserString(gameplayArr[0], gameplayArr[1]);
        }
    }

    public static void main(String[] args) throws Exception {
        BlockingQueue queue = new ArrayBlockingQueue(MAX_SIZE);
        PalindromeWebGame game = new PalindromeWebGame(queue);
        MultiPlayerTest test = new MultiPlayerTest(queue);
        game.start();
        test.start();
        //Wait for tests to finish.
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {

        }
        game.stopGame();
        game.interrupt();
        System.out.println(game.findHallOfFamers());
    }
}


class PalindromeUtil {
    public static boolean isPalindrome(String str) {
        boolean palindrome = true;
        String strBuffer = removeSpacesAndPunctuations(str);
        int lastPos = strBuffer.length() - 1;
        for (int i = 0; i < (strBuffer.length() / 2); i++) {
            if (strBuffer.charAt(i) != strBuffer.charAt(lastPos)) {
                palindrome = false;
                break;
            }
            lastPos--;
        }
        return palindrome;
    }

    public static int getSize(String str) {
        return removeSpacesAndPunctuations(str).length() - 1;
    }

    private static String removeSpacesAndPunctuations(String str) {
        str = str.toLowerCase();
        String[] strArr = str.split(" ");
        StringBuffer strBuffer = new StringBuffer();
        for (String str1 : strArr) {
            strBuffer.append(removePunctuations(str1));
        }
        return strBuffer.toString();
    }

    private static String removePunctuations(String str) {
        char[] charArr = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < charArr.length; i++) {
            if (Character.isLetter(charArr[i]))
                stringBuffer.append(charArr[i]);
        }
        return stringBuffer.toString();
    }
}

// Test class to show multiple threads accessing the player state.
class MultiPlayerTest extends Thread {

    protected BlockingQueue queue = null;

    public MultiPlayerTest(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("Sid: Poor Dan is in a droop");
            queue.put("Mike: A man, a plan, a canal Panama.");
            queue.put("Mike: A Santa dog lived as a devil God at NASA.");
            queue.put("turk: Animal loots foliated detail of stool lamina.");
            queue.put("turk: Animal loots foliated detail of stool lamina.");
            queue.put("turk: Animal loots foliated detail of stool lamina.");
            queue.put("John: Do geese see God?");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("Sid: Madam Adam");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
