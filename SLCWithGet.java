/**
 * Implementation of the SortedLinkedCollection,
 * the elements are added and ordered into the list
 * when the text files have been compared.
 *
 * @author Thomas Jinton
 * @author Kevin Solovjov
 */
public class SLCWithGet<E extends Comparable<? super E>> extends LinkedCollection<E> implements CollectionWithGet<E> {

    /**
     * Adds elements to our collection.
     *
     * @param element the object to add into the list
     * @throws NullPointerException if element is null.
     */
    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException();
        else {
            insert(element);
            return true;
        }
    }

    /**
     * Method that puts the Entry elements in their sorted
     * positions in the list.
     *
     * @param element the object to add into the list
     */
    private boolean insert(E element) {
        //Head finns inte, nytt head skapas
        if (head == null) {
            head = new Entry(element, null);
            return true;
        }

        Entry nextEntry = head;
        Entry previousEntry = null;

        while (nextEntry != null) {

            //Om elementet är mindre än nästa element
            if (element.compareTo(nextEntry.element) < 0) {

                //Om elementet är mindre än head blir elementet det nya head
                if (previousEntry == null) {
                    head = new Entry(element, head);
                    return true;

                    //Om elementet är mindre än nästa element läggs det in innan det
                } else {
                    Entry newEntry = new Entry(element, nextEntry);
                    previousEntry.next = newEntry;
                    return true;
                }
                //Om elementet inte var mindre än nästa element flyttas det vidare
            } else {
                previousEntry = nextEntry;
                nextEntry = nextEntry.next;
            }
        }

        //Om elementet inte var mindre än något i listan läggs det längst bak.
        Entry newEntry = new Entry(element, nextEntry);
        previousEntry.next = newEntry;
        return true;

    }

    /**
     * Find the first occurence of an element
     * in the collection that is equal to the argument
     *
     * @param e The element we use as a key
     * @return entry containing the word we searched for
     */
    @Override
    public E get(E e) {
        Entry t = find(e, head);
        return t == null ? null : t.element;
    }


    /**
     * Method that compares the elements in the list.
     * This method will return the entry that matches the searched element
     *
     * @param element the object we want to find
     * @param head    the first element in the list.
     */
    private Entry find(E element, Entry head) {

        //Initialize current
        Entry current = head;
        while (current != null) {

            int cmp = current.element.compareTo(element);

            if (cmp < 0) {
                current = current.next;
            } else if (cmp == 0) {
                //data found
                return current;
            } else if (cmp > 0) {
                break;
            }
        }
        return null;
    }


}