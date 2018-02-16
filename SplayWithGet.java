/**
 * The
 *
 * @author Thomas Jinton
 * @author Kevin Solovjov
 */

public class SplayWithGet <E extends Comparable<? super E>> extends BinarySearchTree<E> implements CollectionWithGet<E> {

    /**
     * Directions Left and Right.
     */
    private enum Direction{
        LEFT, RIGHT
    }

    /**
     * Find the first occurence of an element
     * in the collection that is equal to the argument
     * @param e element, the element we use to find an entry in the tree
     * @return Returns the entry that matches the provided element
     */
    @Override
    public E get(E e) {
        if(e == null){
            throw new NullPointerException();
        }
        Entry t = find(e, root);

        return t == null ? null : t.element;

    }

    /**
     * Method for finding the element
     * and compares it so it can be splayed.
     * @param e The element to be found
     * @param entry An entry, initially root
     * @return root containing the element after splay if it's found, otherwise null
     */
    @Override
    public Entry find(E e, Entry entry){

        if (entry == null){
            return null;
        }

        int cmp = e.compareTo(entry.element);

        if(cmp == 0){

            splay(entry);

            return root;
        }
        else if(cmp < 0){
            if(entry.left == null){
                splay(entry);
                return null;
            }
            return find(e,entry.left);
        }
        else {
            if(entry.right == null){
                splay(entry);
                return null;
            }
            return find(e,entry.right);
        }
    }

    /**
     * Method for enabling the Splaying,
     * move-to-root operation.
     * @param entry The entry to splay
     */

    private void splay(Entry entry){

        Direction parentDirection;
        Direction grandParentDirection;
        Direction child;

        //Check if entry has parent
        if(entry.parent != null){

            Entry parent = entry.parent;

            //Check if entry is a right child
            if(parent.right == entry){
                parentDirection = Direction.RIGHT;
            }
            //else entry is a left child
            else{
                parentDirection = Direction.LEFT;
            }

            //Check if entry has a grandparent
            if(parent.parent !=null){

                Entry grandParent = parent.parent;

                //Check if parent is a right child
                if(grandParent.right == parent){
                    grandParentDirection = Direction.RIGHT;
                }
                //else parent is a left child
                else {
                    grandParentDirection = Direction.LEFT;
                }

                //Check which splay function should be preformed depending on parent relations.
                if(grandParentDirection == Direction.RIGHT){


                    if (parentDirection == Direction.RIGHT){
                        zigzig(grandParent);
                    }

                    else {
                        zigzag(grandParent);
                    }

                }else {

                    if(parentDirection == Direction.RIGHT){
                        zagzig(grandParent);
                    }


                    else{
                        zagzag(grandParent);
                    }
                }
                splay(grandParent);
            }
            //The child is child of root
            else {

                if(parentDirection == Direction.RIGHT){
                    zig(parent);
                }
                else{
                    zag(parent);
                }
            }
        }

        //The entry is root, splaying done!
        else {
            return;
        }
    }
    // ========== ========== ========== ==========
    //The child is LEFT LEFT (zag zag)
    /*

           x'           z'
          / \          / \
         y   A        D   y
        / \              / \
       z'  B    -->     C   x'
      / \                  / \
     D   C                B   A
     */

    /**
     * The target element is lifted up
     * by two levels in each case, here
     * it is Left Left.
     * @param x entry to be splayed
     */
    public void zagzag(Entry x) {

        Entry z = x.left.left;

        Entry y = x.left;

        E temp = x.element;

        x.element = z.element;
        z.element = temp;


        x.left = z.left;


        if (x.left != null) {
            x.left.parent = x;
        }

        Entry a = x.right;
        x.right = y;

        y.left = z.right;
        if (y.left != null) {
            y.left.parent = y;
        }


        Entry b = y.right;
        y.right = z;

        z.right = a;
        if (z.right != null) {
            z.right.parent = z;
        }

        z.left = b;
        if(z.left != null){
            z.left.parent = z;
        }
    }
    // ========== ========== ========== ==========
    //The child is RIGHT RIGHT (zig zig)
    /*
             x'                     z'
            / \                    / \
           A   y                  y   D
              / \                / \
             B   z'  -->        x'  C
                / \            / \
               C   D          A   B
     */

    /**
     * The target element is lifted up
     * by two levels in each case, here
     * it is Right Right.
     * @param x entry to be splayed
     */
    public void zigzig(Entry x){

        Entry z = x.right.right;

        Entry y = x.right;

        E temp = x.element;


        x.element = z.element;
        z.element = temp;


        x.right = z.right;

        if(x.right != null){
            x.right.parent = x;
        }

        Entry a = x.left;
        x.left = y;

        y.right = z.left;
        if(y.right != null){
            y.right.parent = y;
        }

        Entry b = y.left;
        y.left = z;

        z.left = a;
        if(z.left != null){
            z.left.parent = z;
        }

        z.right = b;
        if(z.right != null){
            z.right.parent = z;
        }
    }

    /**
     * Method for rotating one step right using Zag.
     * @param x entry to be splayed
     */
    private void zag( Entry x ) {
        Entry   y = x.left;
        E    temp = x.element;
        x.element = y.element;
        y.element = temp;
        x.left    = y.left;
        if ( x.left != null )
            x.left.parent   = x;
        y.left    = y.right;
        y.right   = x.right;
        if ( y.right != null )
            y.right.parent  = y;
        x.right   = y;
    }

    /**
     * Method for rotating one step left using Zig.
     * @param x entry to be splayed
     */
    private void zig( Entry x ) {
        Entry  y  = x.right;
        E temp    = x.element;
        x.element = y.element;
        y.element = temp;
        x.right   = y.right;
        if ( x.right != null )
            x.right.parent  = x;
        y.right   = y.left;
        y.left    = x.left;
        if ( y.left != null )
            y.left.parent   = y;
        x.left    = y;
    }

    /**
     * Method for rotating two steps right using ZagZig.
     * @param x entry to be splayed
     */
    private void zagzig( Entry x ) {
        Entry   y = x.left,
                z = x.left.right;
        E       e = x.element;
        x.element = z.element;
        z.element = e;
        y.right   = z.left;
        if ( y.right != null )
            y.right.parent = y;
        z.left    = z.right;
        z.right   = x.right;
        if ( z.right != null )
            z.right.parent = z;
        x.right   = z;
        z.parent  = x;
    }
    /**
     * Method for rotating two steps left using ZigZag.
     * @param x entry to be splayed
     */
    private void zigzag( Entry x ) {
        Entry  y  = x.right,
                z  = x.right.left;
        E      e  = x.element;
        x.element = z.element;
        z.element = e;
        y.left    = z.right;
        if ( y.left != null )
            y.left.parent = y;
        z.right   = z.left;
        z.left    = x.left;
        if ( z.left != null )
            z.left.parent = z;
        x.left    = z;
        z.parent  = x;
    }
}
