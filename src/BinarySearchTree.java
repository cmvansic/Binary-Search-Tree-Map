import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<K extends Comparable<K>, V> extends AbstractBinarySearchTree<K, V> {
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
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public V remove(Object key) throws UnsupportedOperationException, ClassCastException, NullPointerException {

        if (key == null) {
            return null;
        }
        Node currentValue = getRoot(); // Start with the root
        Node remover = null;
        V valueRemoved = null;

        while (currentValue != null) { // Traverse throughout the tree until it becomes null.
            if (((K) key).compareTo(currentValue.getKey()) == 0) {
                remover = currentValue; // Mark for removal if there is a match.
                break;
            } else if (((K) key).compareTo(currentValue.getKey()) < 0) {
                currentValue = currentValue.getLeftChild();
            } else {
                currentValue = currentValue.getRightChild();
            }
        }
        valueRemoved = remover.getValue();

        //Node has only child on the left
        if ((remover.hasLeftChild() && !remover.hasRightChild())) {
            Node childReplace = remover.getLeftChild();
            if (remover.isRoot()) {
                this.setRoot(childReplace);
            } else {
                childReplace.setParent(remover.getParent());
                if (remover.getParent().getLeftChild() == remover) {
                    remover.getParent().setLeftChild(childReplace);
                } else if (remover.getParent().getRightChild() == remover) {
                    remover.getParent().setRightChild(childReplace);
                }
            }
        }


        //Node has only child on the right
        else if (remover.hasRightChild() && !remover.hasLeftChild()) {
            Node childReplace = remover.getRightChild();
            if (remover.isRoot()) {
                this.setRoot(childReplace);
            } else if (remover.getParent().getLeftChild() == remover) {
                remover.getParent().setLeftChild(childReplace);
            } else if (remover.getParent().getRightChild() == remover) {
                remover.getParent().setRightChild(childReplace);
            }


        }
        //Node has both left and right children
        else if (remover.hasLeftChild() && remover.hasRightChild()) {
            Node childReplace = remover.getRightChild();
            while (childReplace.hasLeftChild()) {
                childReplace = childReplace.getLeftChild();
            }
            if (remover.isRoot()) {
                this.setRoot(childReplace);
                if (remover.hasLeftChild()) {
                    this.getRoot().setLeftChild(remover.getLeftChild());
                }
            } else {
                childReplace.setParent(remover.getParent());


                childReplace.setRightChild(remover.getRightChild());
                childReplace.setLeftChild(remover.getLeftChild());

                if (remover.getParent().getLeftChild() == remover) {
                    remover.getParent().setLeftChild(childReplace);
                } else if (remover.getParent().getRightChild() == remover) {
                    remover.getParent().setRightChild(childReplace);
                }
            }
        }
        //Node to remove is root and leaf
        else if (remover.isRoot() && remover.isLeaf()) {
            this.setRoot(null); //
        }
        //Node to remove is leaf
        else if (remover.isLeaf() && (remover.getParent().getLeftChild() == remover)) {
            remover.getParent().setLeftChild(null);
        } else if (remover.isLeaf() && (remover.getParent().getRightChild() == remover)) {
            remover.getParent().setRightChild(null);
        }
        this.decrementSize(); // decreased tree size
        return valueRemoved; // value of new node
    }


    /**
     * Recursive Algorithm: Postorder Binary Tree Traversal
     * 1. Postorder traverse the left subtree.
     * 2. Postorder traverse the right subtree.
     * 3. Visit the root.
     * <p>
     * Example:
     * Tree
     * 4
     * / \
     * 2   5
     * / \
     * 1   3
     * <p>
     * Postorder traversal: 1, 3, 2, 5, 4
     *
     * @param visitor Lambda expression to process the key and value of each node.
     *                For example: (key, value) -> System.out.println( key )
     */
    @Override
    public void traversePostorder(Visitor visitor) {
        traversePostorder(getRoot(), visitor);
    }

    private void traversePostorder(Node node, Visitor visitor) {
        if (node != null) {
            traversePostorder(node.getLeftChild(), visitor);   // Visit left subtree
            traversePostorder(node.getRightChild(), visitor);  // Visit right subtree
            visitor.visit(node.getKey(), node.getValue());      // Visit the node
        }
    }

    /**
     * Iterative Algorithm: Level order Binary Tree Traversal
     * Beginning at the root, visit each node in a level, from left to right
     * then proceeding to the next level and repeat until all nodes are visited.
     * <p>
     * Example:
     * Tree
     * 4
     * / \
     * 2   5
     * / \
     * 1   3
     * <p>
     * Level order traversal: 4, 2, 5, 1, 3
     * <p>
     * Hint: Every time you visit a node, add its children to a queue.
     *
     * @param visitor Lambda expression to process the key and value of each node.
     *                For example: (key, value) -> System.out.println( key )
     */
    @Override
    public void traverseLevelorder(Visitor visitor) {
        if (getRoot() == null) {
            return; // do nothing if tree is empty
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(getRoot());

        while (!queue.isEmpty()) {
            Node current = queue.remove();

            visitor.visit(current.getKey(), current.getValue());

            if (current.getLeftChild() != null) {
                queue.add(current.getLeftChild());
            }
            if (current.getRightChild() != null) {
                queue.add(current.getRightChild());
            }
        }
    }
}
