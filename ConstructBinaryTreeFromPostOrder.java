import java.util.HashMap;
import java.util.Map;

public class ConstructBinaryTreeFromPostOrder {
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

    public static TreeNode buildBinaryTreeFromPostOrder(int[] postorder,
                                                        int inorderStart,
                                                        int inorderEnd,
                                                        int postorderStart,
                                                        int postorderEnd) {
        if (inorderStart > inorderEnd)
            return null;
        int nodeVal = postorder[postorderEnd];
        TreeNode node = new TreeNode(nodeVal);
        int inOrderNodeIndex = (Integer) mapIndex.get(nodeVal);
        int length =  inOrderNodeIndex - inorderStart ;
        node.left = buildBinaryTreeFromPostOrder(postorder, inorderStart, inOrderNodeIndex - 1
                    , postorderStart, postorderStart + length - 1);
        node.right = buildBinaryTreeFromPostOrder(postorder, inOrderNodeIndex + 1, inorderEnd, postorderStart + length,
                postorderEnd - 1);
        return node;
    }

    public static void main(String[] args) {
        int[] inorderArr = new int[]{4, 10, 3, 1, 7, 11, 8, 2};
        int[] postorderArr = new int[]{4, 1, 3, 10, 11, 8, 2, 7};
        mapToIndices(inorderArr);
        buildBinaryTreeFromPostOrder(postorderArr, 0, inorderArr.length - 1, 0, postorderArr.length - 1);
    }
}
