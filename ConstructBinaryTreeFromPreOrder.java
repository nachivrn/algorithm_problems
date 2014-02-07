import java.util.HashMap;
import java.util.Map;

public class ConstructBinaryTreeFromPreOrder {
    static Map mapIndex = new HashMap();

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void mapToIndices(int[] inOrder) {
        for (int i = 0; i < inOrder.length; i++) {
            mapIndex.put(inOrder[i], i);
        }
    }

    public static TreeNode buildBinaryTreeFromPreOrder(int[] preorder,
                                                       int inorderStart,
                                                       int inorderEnd,
                                                       int preorderStart,
                                                       int preorderEnd) {
        if (inorderStart > inorderEnd)
            return null;
        int nodeVal = preorder[preorderStart];
        TreeNode node = new TreeNode(nodeVal);
        int inOrderNodeIndex = (Integer) mapIndex.get(nodeVal);
        int length = inOrderNodeIndex - inorderStart;
        node.left = buildBinaryTreeFromPreOrder(preorder, inorderStart, inOrderNodeIndex - 1
                , preorderStart + 1, preorderStart + length);
        node.right = buildBinaryTreeFromPreOrder(preorder, inOrderNodeIndex + 1, inorderEnd, preorderStart + length + 1,
                preorderEnd);
        return node;
    }

    public static void main(String[] args) {
        int[] preorderArr = new int[]{7, 10, 4, 3, 1, 2, 8, 11};
        int[] inorderArr = new int[]{4, 10, 3, 1, 7, 11, 8, 2};
        mapToIndices(inorderArr);
        buildBinaryTreeFromPreOrder(preorderArr, 0, inorderArr.length - 1, 0, preorderArr.length - 1);
    }
}
