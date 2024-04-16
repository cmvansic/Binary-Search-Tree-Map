import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<K extends Comparable<K>, V> extends AbstractBinarySearchTree<K,V> {
   /**
    * Removes the mapping for a key from this map if it is present
    * (optional operation).   More formally, if this map contains a mapping
    * from key <tt>k</tt> to value <tt>v</tt> such that
    * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
    * is removed.  (The map can contain at most one such mapping.)
    *
    * <p>Returns the value to which this map previously associated the key,
    * or <tt>null</tt> if the map contained no mapping for the key.
    *
    * <p>If this map permits null values, then a return value of
    * <tt>null</tt> does not <i>necessarily</i> indicate that the map
    * contained no mapping for the key; it's also possible that the map
    * explicitly mapped the key to <tt>null</tt>.
    *
    * <p>The map will not contain a mapping for the specified key once the
    * call returns.
    *
    * @param key key whose mapping is to be removed from the map
    * @return the previous value associated with <tt>key</tt>, or
    *      <tt>null</tt> if there was no mapping for <tt>key</tt>.
    * @throws UnsupportedOperationException if the <tt>remove</tt> operation
    *                                       is not supported by this map
    * @throws ClassCastException            if the key is of an inappropriate type for
    *                                       this map
    *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
    * @throws NullPointerException          if the specified key is null and this
    *                                       map does not permit null keys
    *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
    */
   @Override
   public V remove(Object key) {
      Node parent = null;
      Node current = getRoot();
      while (current != null && !current.getKey().equals(key)) {
         parent = current;
         if (((Comparable) key).compareTo(current.getKey()) < 0) {
            current = current.getLeftChild();
         } else {
            current = current.getRightChild();
         }
      }

      if (current == null) {
         return null; // Key not found
      }

      V oldValue = current.getValue();

      if (current.getLeftChild() == null && current.getRightChild() == null) {
         // Case 1: Node with no children
         if (parent == null) {
            setRoot(null);
         } else if (parent.getLeftChild() == current) {
            parent.setLeftChild(null);
         } else {
            parent.setRightChild(null);
         }
      } else if (current.getLeftChild() == null || current.getRightChild() == null) {
         // Case 2: Node with one child
         Node child = (current.getLeftChild() != null) ? current.getLeftChild() : current.getRightChild();
         if (parent == null) {
            setRoot(child);
         } else if (parent.getLeftChild() == current) {
            parent.setLeftChild(child);
         } else {
            parent.setRightChild(child);
         }
      } else {
         // Case 3: Node with two children
         Node successor = findMin(current.getRightChild());
         remove(successor.getKey()); // Remove successor from its current location
         successor.setLeftChild(current.getLeftChild());
         successor.setRightChild(current.getRightChild());
         if (parent == null) {
            setRoot(successor);
         } else if (parent.getLeftChild() == current) {
            parent.setLeftChild(successor);
         } else {
            parent.setRightChild(successor);
         }
      }

      return oldValue;
   }

   private Node findMin(Node node) {
      while (node.getLeftChild() != null) {
         node = node.getLeftChild();
      }
      return node;
   }


   /**
    * Recursive Algorithm: Postorder Binary Tree Traversal
    *    1. Postorder traverse the left subtree.
    *    2. Postorder traverse the right subtree.
    *    3. Visit the root.
    *
    * Example:
    *    Tree
    *              4
    *             / \
    *            2   5
    *           / \
    *          1   3
    *
    *    Postorder traversal: 1, 3, 2, 5, 4
    *
    * @param visitor Lambda expression to process the key and value of each node.
    *                For example: (key, value) -> System.out.println( key )
    */
   @Override
   public void traversePostorder( Visitor visitor ) {
      traversePostorder(getRoot(), visitor);
   }

   private void traversePostorder(Node node, Visitor visitor) {
      if (node != null) {
         traversePostorder(node.getLeftChild(), visitor);   // Visit left subtree
         traversePostorder(node.getRightChild(), visitor);  // Visit right subtree
         visitor.visit(node.getKey(), node.getValue());      // Visit the node itself
      }
   }

   /**
    * Iterative Algorithm: Level order Binary Tree Traversal
    *    Beginning at the root, visit each node in a level, from left to right
    *    then proceeding to the next level and repeat until all nodes are visited.
    *
    * Example:
    *    Tree
    *              4
    *             / \
    *            2   5
    *           / \
    *          1   3
    *
    *    Level order traversal: 4, 2, 5, 1, 3
    *
    * Hint: Every time you visit a node, add its children to a queue.
    *
    * @param visitor Lambda expression to process the key and value of each node.
    *                For example: (key, value) -> System.out.println( key )
    */
   @Override
   public void traverseLevelorder( Visitor visitor ) {
      if (getRoot() == null) {
         return; // If the tree is empty, do nothing
      }

      Queue<Node> queue = new LinkedList<>(); // Create a queue to hold the nodes
      queue.add(getRoot()); // Start with the root

      while (!queue.isEmpty()) {
         Node current = queue.remove(); // Remove the head of the queue

         visitor.visit(current.getKey(), current.getValue()); // Visit the current node

         if (current.getLeftChild() != null) {
            queue.add(current.getLeftChild()); // Add the left child to the queue if it exists
         }
         if (current.getRightChild() != null) {
            queue.add(current.getRightChild()); // Add the right child to the queue if it exists
         }
      }
   }
 }
