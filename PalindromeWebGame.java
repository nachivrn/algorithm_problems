import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


public class PalindromeWebGame extends Thread {
    private static final int MAX_SIZE = 3;
    private final ConcurrentMap<String, Player> playerMap = new ConcurrentHashMap<String, Player>();
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
        Player p = new Player(userName, score);
        synchronized (this) {
            if (checkIfPlayerAlreadyExist(userName)) {
                playerMap.replace(userName, p);
            } else {
                playerMap.put(userName, p);
            }
        }
    }

    private synchronized boolean checkIfPlayerAlreadyExist(String userName) {
        return playerMap.containsKey(userName);
    }

    private int computeScoreForUser(String palindromeStr, String name) {
        int score = 0;

        if (palindromeStr == null || palindromeStr.length() < 2)
            return 0;

        if (checkIfStringPalindrome(palindromeStr)) {
            score = PalindromeUtil.getSize(palindromeStr) / 2;
        }
        synchronized (this) {
            if (playerMap.containsKey(name)) {
                Player p = playerMap.get(name);
                score = score + p.getScore();
            }
        }
        return score;
    }

    private boolean checkIfStringPalindrome(String palindromeStr) {
        return PalindromeUtil.isPalindrome(palindromeStr);
    }

    public List<Player> findHallOfFamers() {
        if (playerMap.size() == 0)
            return Collections.<Player>emptyList();

        List<Player> playerList = new ArrayList<Player>();
        for (String key : playerMap.keySet()) {
            playerList.add(playerMap.get(key));
        }
        Collections.sort(playerList);
        Collections.reverse(playerList);
        if (playerList.size() < 5)
            return playerList.subList(0, playerList.size());
        else
            return playerList.subList(0, 5);
    }

    public void resetServer() {
        for (String key : playerMap.keySet()) {
            Player p = playerMap.get(key);
            String name = p.getName();
            playerMap.remove(p);
            playerMap.replace(name, new Player(name, 0));
        }
    }

    public synchronized void stopGame() {
        this.isStopped = true;
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
            System.out.println(Thread.currentThread().getName()
                    + " executing palindrome string " + gameplayArr[1] + "  given by " + gameplayArr[0]);
            submitUserString(gameplayArr[0], gameplayArr[1]);
        }
    }

    public static void main(String[] args) throws Exception {
        BlockingQueue queue = new ArrayBlockingQueue(MAX_SIZE);
        PalindromeWebGame game = new PalindromeWebGame(queue);
        MultiPlayerTest test = new MultiPlayerTest(queue);
        game.start();
        test.start();
        //Wait for threads to finish.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {

        }
        System.out.println(game.findHallOfFamers());
        game.stopGame();
        game.interrupt();
    }
}

// Immutable class -- Only way player state can be updated if it already exist is
// to create a new player object with a score
// and replace the existing object.
class Player implements Comparable {

    private String name;
    private int score;

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        Player p = (Player) obj;
        return (name == p.getName() || (name != null && name.equals(p.getName())));
    }

    public int compareTo(Object o) {
        Player p = (Player) o;
        return new Integer(score).compareTo(new Integer(p.getScore()));
    }

    @Override
    public int hashCode() {
        int hash = 9;
        hash = (31 * hash) + (null == name ? 0 : name.hashCode());
        return hash;
    }

    public String toString() {
        return name + " : " + score;
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
            queue.put("Sid: Poor Dan is in a droop");
            queue.put("John: A man, a plan, a canal: Panama.");
            queue.put("Mike: A Santa dog lived as a devil God at NASA.");
            queue.put("turk: Animal loots foliated detail of stool lamina.");
            queue.put("John: Do geese see God?");
            queue.put("John: A Toyota! Race fast, safe car! A Toyota!");
            queue.put("Sid: Madam Adam");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
