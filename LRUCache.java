import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class LRUCache {
    private static class Cache {
        private class Entry implements Comparable<Entry> {
            String key;
            String value;
            Entry next;
            Entry prev;

            public Entry(String key, String value) {
                this.key = key;
                this.value = value;
            }

            public int compareTo(Entry e) {
                return this.key.compareTo(e.key);
            }
        }

        private Map<String, Entry> cacheMap = new HashMap<String, Entry>();
        private int size;
        private Entry head;
        private Entry tail;

        public Cache() {
        }

        public void setCacheSize(int size) {
            this.size = size;
            adjustCacheSize();
        }

        public void insertEntry(String key, String value) {
            if (cacheMap.containsKey(key)) {
                Entry e = getFromCache(key);
                e.value = value;
                return;
            }
            Entry e = new Entry(key, value);
            if (head == null) {
                head = e;
                tail = e;
            } else {
                e.next = head;
                head.prev = e;
                head = e;
            }
            cacheMap.put(key, e);
            if (cacheMap.size() > size) {
                removeEntry();
            }
        }

        public void removeEntry() {
            if (tail == null)
                return;
            Entry e = tail;
            if (tail.prev != null) {
                tail.prev.next = null;
                tail = tail.prev;
            }
            e.next = null;
            e.prev = null;
            cacheMap.remove(e.key);
        }

        public void getEntry(String key, boolean modify) {
            Entry e = null;
            if (cacheMap.containsKey(key)) {
                if (modify)
                    e = getFromCache(key);
                else
                    e = cacheMap.get(key);
                System.out.println(e.value);
            } else {
                System.out.println("NULL");
            }
        }

        public void dumpCache() {
            TreeSet<Entry> sortedEntry = new TreeSet<Entry>();
            Entry current = head;
            while (current != null) {
                sortedEntry.add(current);
                current = current.next;
            }
            for (Entry e : sortedEntry) {
                System.out.println(e.key + " " + e.value);
            }
        }

        private void adjustCacheSize() {
            while (cacheMap.size() > size) {
                removeEntry();
            }
        }

        private Entry getFromCache(String key) {
            Entry e = cacheMap.get(key);
            if (e.prev != null) {
                {
                    e.prev.next = e.next;
                    if (e.next != null) {
                        e.next.prev = e.prev;
                    } else {
                        tail = e.prev;
                    }
                    e.prev = null;
                    e.next = head;
                    head.prev = e;
                    head = e;
                }
            }
            return e;
        }
    }

    static void readInput() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();
        int numberOfCommands = Integer.parseInt(str);
        String[] command;
        Cache cache = new Cache();
        while (numberOfCommands > 0) {
            str = br.readLine();
            command = str.split(" ");
            char commandChar = command[0].charAt(0);
            switch (commandChar) {
                case 'B':
                    cache.setCacheSize(Integer.parseInt(command[1]));
                    break;
                case 'S':
                    cache.insertEntry(command[1], command[2]);
                    break;
                case 'G':
                    cache.getEntry(command[1], true);
                    break;
                case 'P':
                    cache.getEntry(command[1], false);
                    break;
                case 'D':
                    cache.dumpCache();
                    break;
            }
            numberOfCommands--;
        }
        br.close();
    }

    public static void main(String[] args) {
        try {
            readInput();
        } catch (Exception e) {
            System.err.println("Error:" + e.getMessage());
        }
    }

}
