public class SplayWithGet <E extends Comparable<? super E>> extends BinarySearchTree<E> implements CollectionWithGet<E> {

    /**
     * Enum for the directions left and right, used to describe parent-child relationship
     */
    private enum Dir{
        RIGHT, LEFT
    }

    @Override
    protected void addIn(E newElem, Entry t) {
        // dubletter borde kanske inte tillåtas
        int comp = newElem.compareTo( t.element);
        if ( comp < 0 ) {
            if ( t.left == null ) { // här behöver vi titta ner
                t.left = new Entry( newElem, t);
                splay(t.left);
            } else {
                addIn( newElem, t.left );
            }
        } else if ( comp > 0 ) {
            if ( t.right == null ) {
                t.right = new Entry( newElem, t);
                splay(t.right);
            } else {
                addIn( newElem, t.right );
            }
        } else {
            size--; // Update do nothing
            // update => E has to have a update method
        }

    }

    /**
     * Finds and returns the specified element in the tree
     * @param e element to compare to
     * @return the element if found, otherwise null
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
     * Finds the specified element in the tree, returns its entry
     * @param e element to find
     * @param entry current entry to compare to
     * @return entry if found, null otherwise
     */
    @Override
    public Entry find(E e, Entry entry){

        if (entry == null){
            return null;
        }

        int compare = e.compareTo(entry.element);

        if(compare == 0){

            splay(entry);

            return entry;
        }
        else if(compare < 0){
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
     * Performs the splaying
     * @param entry entry to splay upwards
     */
    private void splay(Entry entry){

        Dir grandParentDir;
        Dir parentDir;

        //Check if entry has parent
        if(entry.parent != null){

            Entry parent = entry.parent;

            //Check if entry is a right child
            if(parent.right == entry){
                parentDir = Dir.RIGHT;
            }
            //else entry is a left child
            else{
                parentDir = Dir.LEFT;
            }

            //Check if entry has a grandparent
            if(parent.parent !=null){

                Entry grandParent = parent.parent;

                //Check if parent is a right child
                if(grandParent.right == parent){
                    grandParentDir = Dir.RIGHT;
                }
                //else parent is a left child
                else {
                    grandParentDir = Dir.LEFT;
                }

                //Check which rotation that should be preformed depending on parent relations.
                if(grandParentDir == Dir.RIGHT){

                    //the relation is: Right -- Right
                    if (parentDir == Dir.RIGHT){
                        zigzig(grandParent);
                    }
                    //the relation is: Right -- Left
                    else {
                        zigzag(grandParent);
                    }


                }else {

                    //the relation is: Left -- Right
                    if(parentDir == Dir.RIGHT){
                        zagzig(grandParent);
                    }

                    //the relation is: Left -- Left
                    else{
                        zagzag(grandParent);
                    }
                }
                splay(grandParent);
            }
            //The child is child of root
            else {
                //the relation is: Right
                if(parentDir == Dir.RIGHT){
                    zig(parent);
                }
                //the relation is: Left
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
     * Rotates a left-left child to top
     * @param x The entry that should be splayed to
     */
    public void zagzag(Entry x) {

        Entry z = x.left.left;

        Entry y = x.left;

        E elemX = x.element;

        //CHANGE TOP ELEMENT
        x.element = z.element;
        z.element = elemX;

        // z --> A
        x.left = z.left;

        //A --> z
        if (x.left != null) {
            x.left.parent = x;
        }

        //y --> C
        Entry a = x.right;
        x.right = y;

        //C --> y
        y.left = z.right;
        if (y.left != null) {
            y.left.parent = y;
        }

        //y --> z
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
     * Rotates a right-right child to top
     * @param x The entry that should be splayed to
     */
    public void zigzig(Entry x){

        Entry z = x.right.right;

        Entry y = x.right;

        E elemX = x.element;

        //CHANGE TOP ELEMENT
        x.element = z.element;
        z.element = elemX;

        //TOP ELEMENTS ONLY RIGHT CHILD
        x.right = z.right;

        if(x.right != null){
            x.right.parent = x;
        }

        Entry a = x.left;
        x.left = y;

        //Y<--->D
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

    // ========== ========== ========== ==========

    /* Rotera 1 steg i hogervarv, dvs
              x'                 y'
             / \                / \
            y'  C   -->        A   x'
           / \                    / \
          A   B                  B   C
    */

    /**
     * @param x The entry that should be splayed to.
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
    } //   rotateRight
    // ========== ========== ========== ==========

    /* Rotera 1 steg i vanstervarv, dvs
              x'                 y'
             / \                / \
            A   y'  -->        x'  C
               / \            / \
              B   C          A   B
    */

    /**
     * @param x The entry that should be splayed to.
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
    } //   rotateLeft
    // ========== ========== ========== ==========

    /* Rotera 2 steg i hogervarv, dvs
              x'                  z'
             / \                /   \
            y'  D   -->        y'    x'
           / \                / \   / \
          A   z'             A   B C   D
             / \
            B   C
    */

    /**
     * @param x The entry that should be splayed to.
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
    }  //  doubleRotateRight
    // ========== ========== ========== ==========

    /* Rotera 2 steg i vanstervarv, dvs
               x'                  z'
              / \                /   \
             A   y'   -->       x'    y'
                / \            / \   / \
               z   D          A   B C   D
              / \
             B   C
     */

    /**
     * @param x The entry that should be played to.
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
    } //  doubleRotateLeft
    // ========== ========== ========== ==========
}
