public class LinkedList {

    static Node buildOneTwoThree() {
        Node three = new Node(3, null);
        Node two = new Node(2, three);
        Node one = new Node(1, two);
        return one;
    }

    static Node buildFromIntArr(int[] intArr) {
        Node head = new Node(intArr[intArr.length - 1], null);
        for (int i = intArr.length - 2; i >= 0; i--) {
            Node prev = new Node(intArr[i], head);
            head = prev;
        }
        return head;
    }

    static int count(Node head) {
        int count = 0;
        Node current = head;
        while (current != null) {
            current = current.next;
            count++;
        }
        return count;
    }

    static Node reverse(Node head) {
        Node pointer1 = head;
        Node pointer3 = null;
        Node pointer2;

        while (pointer1 != null) {
            pointer2 = pointer1.next;
            pointer1.next = pointer3;
            pointer3 = pointer1;
            pointer1 = pointer2;
        }
        head = pointer3;
        return head;
    }

    static Node recursiveReverse(Node head) {
        if ((head == null || head.next == null)) {
            return head;
        }
        Node result = recursiveReverse(head.next);
        head.next.next = head;
        head.next = null;
        return result;
    }

    static Node sortedInsert(Node head, Node newNode) {
        if (head == null || head.getData() > newNode.getData()) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                if (current.next.getData() < newNode.getData()) {
                    current = current.next;
                } else {
                    break;
                }
            }

            newNode.next = current.next;
            current.next = newNode;
        }
        return head;
    }

    static Node insertSort(Node head) {
        Node result = null;
        Node temp = null;
        Node current = head;
        while (current != null) {
            temp = current;
            current = current.next;
            result = sortedInsert(result, temp);
        }
        return result;
    }

    static Node[] frontBackSplit(Node head) {
        int length = count(head);
        int midIndex = (length + 1) / 2;
        Node second = null;
        Node first = head;

        for (int i = 1; i < midIndex; i++) {
            first = first.next;
        }
        second = first.next;
        first.next = null;
        first = head;
        return new Node[]{first, second};
    }

    static Node removeDuplicates(Node head) {
        Node current = head;
        while (current != null && current.next != null) {
            if (current.next.getData() == current.getData()) {
                current.next = current.next.next;
            }
            current = current.next;
        }
        return current;
    }

    static Node[] moveNode(Node dest, Node source) {
        if (source == null)
            return null;
        Node temp = source.next;
        source.next = dest;
        dest = source;
        source = temp;
        return new Node[]{dest, source};
    }

    static Node[] alternateSplit(Node head) {
        int count = 0;
        Node first = null;
        Node second = null;
        Node current = head;
        Node[] nodeArr = null;
        while (current != null) {
            if (count % 2 == 0) {
                nodeArr = moveNode(second, current);
                second = nodeArr[0];
                current = nodeArr[1];
            } else {
                nodeArr = moveNode(first, current);
                first = nodeArr[0];
                current = nodeArr[1];
            }
            count++;
        }
        return new Node[]{first, second};
    }

    static Node shuffleMerge(Node list1, Node list2) {
        Node result = null;
        Node[] nodeArr = null;
        while (list1 != null && list2 != null) {
            nodeArr = moveNode(result, list1);
            result = nodeArr[0];
            list1 = nodeArr[1];
            nodeArr = moveNode(result, list2);
            result = nodeArr[0];
            list2 = nodeArr[1];
        }
        while (list1 != null) {
            Node temp = list1;
            list1 = list1.next;
            temp.next = result;
            result = temp;
        }
        while (list2 != null) {
            Node temp = list2;
            list2 = list2.next;
            temp.next = result;
            result = temp;
        }
        return reverse(result);
    }

    static Node sortedMerge(Node list1, Node list2) {
        Node currentResult = null;
        Node result = null;
        Node current;
        if (list1 == null)
            return list2;
        if (list2 == null)
            return list1;
        while (list1 != null && list2 != null) {
            if (list1.getData() > list2.getData()) {
                current = list2;
                list2 = list2.next;
                current.next = null;
            } else {
                current = list1;
                list1 = list1.next;
                current.next = null;
            }
            if (currentResult == null) {
                currentResult = current;
                result = current;
            } else {
                currentResult.next = current;
                currentResult = currentResult.next;
            }
        }
        while (list1 != null) {
            current = list1;
            list1 = list1.next;
            current.next = null;
            currentResult.next = current;
            currentResult = currentResult.next;
        }
        while (list2 != null) {
            current = list2;
            list2 = list2.next;
            current.next = null;
            currentResult.next = current;
            currentResult = currentResult.next;
        }
        return result;
    }

    static Node mergeSort(Node head) {
        if (head == null)
            return null;
        if (head.next == null)
            return head;
        Node[] nodeArr = frontBackSplit(head);
        Node left = nodeArr[0];
        Node right = nodeArr[1];
        left = mergeSort(left);
        right = mergeSort(right);
        return sortedMerge(left, right);
    }

    static Node sortedIntersect(Node list1, Node list2) {
        Node result = null;
        Node currentResult = null;
        Node current = null;
        while (list1 != null && list2 != null) {
            if (list1.getData() > list2.getData()) {
                list2 = list2.next;
            } else if  (list1.getData() < list2.getData()){
                list1 = list1.next;
            } else {
                current = list1;
                list1 = list1.next;
                list2 = list2.next;
                current.next = null;
                if (currentResult == null) {
                    currentResult = current;
                    result = currentResult;
                } else {
                    currentResult.next = current;
                    currentResult = currentResult.next;
                }
            }
        }
        return result;
    }


    static class Node {
        Node next;
        private int data;
        public Node(int data) {
            this.data = data;
        }
        public Node(int data, Node ref) {
            this.data = data;
            this.next = ref;
        }
        public int getData() {
            return data;
        }

    }
}
