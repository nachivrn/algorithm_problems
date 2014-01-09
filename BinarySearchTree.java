package org.nachi.interviews;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {

    private Node root;

    class Node {
        int data;
        Node left;
        Node right;

        public Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node insert(int data) {
        return insert(root, data);
    }

    public boolean lookup(int data) {
        return lookup(root, data);
    }

    public int size() {
        return size(root);
    }

    public int height() {
        return height(root);
    }

    public int minValue() {
        return minValue(root);
    }

    public void printTree() {
        printTree(root);
        System.out.println();
    }

    public void printPostOrder() {
        printPostOrder(root);
        System.out.println();
    }

    public boolean hasPathSum(int sum) {
        List<Integer> pathSum = new ArrayList<Integer>();
        getPathSum(root, 0, pathSum);
        return pathSum.contains(sum);
    }

    public void printPaths() {
        List<Integer> path = new ArrayList<Integer>();
        printPathsRecur(root, path);
    }

    public void mirror() {
        mirror(root);
    }

    public void doubleTree() {
        doubleTree(root);
    }

    public boolean sameTree(BinarySearchTree other) {
     return( sameTree(root, other.root) );
    }

    public boolean isBST() {
        return isBSTRecur(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBSTRecur(Node node, int min, int max) {
        if (node == null)
            return true;
        if (node.data <= min && node.data > max) {
            return false;
        }
        return (isBSTRecur(node.left, min, node.data) &&
                isBSTRecur(node.right, node.data, max));
    }


    public boolean sameTree(Node node1, Node node2) {
        if (node1 == null && node2 == null)
            return true;
        if (node1 == null || node2 == null ||
                node1.data != node2.data)
            return false;
        return sameTree(node1.left, node2.left) && sameTree(node1.right, node2.right);
    }

    private void doubleTree(Node node) {
        if (node == null)
            return;
        doubleTree(node.left);
        Node newNode = new Node(node.data);
        Node temp = node.left;
        node.left = newNode;
        newNode.left = temp;
        doubleTree(node.right);
    }

    private void mirror(Node node) {
        if (node == null)
            return;
        if (node.left != null && node.right != null) {
            Node temp = node.left;
            node.left = node.right;
            node.right = temp;
            mirror(node.left);
            mirror(node.right);
        } else if (node.left != null) {
            node.right = node.left;
            node.left = null;
            mirror(node.right);
        } else if (node.right != null) {
            node.left = node.right;
            node.right = null;
            mirror(node.left);
        }

    }

    private void printPathsRecur(Node node, List<Integer> path) {
        if (node == null) {
            return;
        }
        path.add(node.data);
        printPathsRecur(node.left, path);
        printPathsRecur(node.right, path);
        if (node.left == null && node.right == null) {
            for (int current : path) {
                System.out.print(current + " \t");
            }
            System.out.println();
        }
        path.remove(path.size() - 1);
    }


    private void getPathSum(Node node, int sum, List<Integer> pathSum) {
        if (node == null) {
            return;
        }
        getPathSum(node.left, sum + node.data, pathSum);
        getPathSum(node.right, sum + node.data, pathSum);
        if (node.left == null && node.right == null) {
            pathSum.add(sum + node.data);
        }
    }

    private void printPostOrder(Node node) {
        if (node == null) {
            return;
        }
        printPostOrder(node.left);
        printPostOrder(node.right);
        System.out.print(node.data + " \t");
    }

    private void printTree(Node node) {
        if (node == null) {
            return;
        }
        printTree(node.left);
        System.out.print(node.data + " \t");
        printTree(node.right);
    }

    private int minValue(Node node) {
        if (node == null) {
            return -1;
        }
        if (node.left == null) {
            return node.data;
        }
        return minValue(node.left);
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }


    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return size(node.left) + 1 + size(node.right);
    }

    private boolean lookup(Node node, int data) {
        boolean found = false;
        if (node == null) {
            return false;
        } else {
            if (node.data == data) {
                found = true;
            } else if (node.data > data) {
                lookup(node.left, data);
            } else {
                lookup(node.right, data);
            }
        }
        return found;
    }

    private Node insert(Node node, int data) {
        if (node == null) {
            node = new Node(data);
        } else {
            if (node.data > data) {
                node.left = insert(node.left, data);
            } else {
                node.right = insert(node.right, data);
            }
        }
        return node;
    }

    public static BinarySearchTree buildTree() {
        BinarySearchTree bst = new BinarySearchTree();
        BinarySearchTree.Node root = bst.insert(7);
        bst.insert(root, 9);
        bst.insert(root, 8);
        bst.insert(root, 5);
        bst.insert(root, 6);
        bst.insert(root, 1);
        bst.insert(root, 13);
        bst.insert(root, 11);
        bst.setRoot(root);
        return bst;
    }

    public static BinarySearchTree buildTree1234() {
            BinarySearchTree bst = new BinarySearchTree();
            BinarySearchTree.Node root = bst.insert(2);
            bst.insert(root, 3);
            bst.insert(root, 4);
            bst.insert(root, 1);
            bst.setRoot(root);
            return bst;
        }
}
