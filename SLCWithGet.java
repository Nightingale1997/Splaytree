import java.util.*;

public class SLCWithGet<E extends Comparable<? super E>> extends LinkedCollection<E> implements CollectionWithGet<E> {

    @Override
    public boolean add(E element) {
        if (element == null)
            throw new NullPointerException();
        else {


            insert(element);

            return true;
        }
    }

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

    @Override
    public E get(E e) {
        Entry t = find(e, head);
        return t == null ? null : t.element;
    }  // get

    private Entry find(E element, Entry head) {

        Entry current = head;    //Initialize current
        while (current != null) {

            int cmp = current.element.compareTo(element);

            if(cmp < 0){
                current = current.next;
            }
            else if(cmp == 0){
                return current;    //data found
            }
            else if(cmp > 0){
                break;
            }


        }
        return null;
    }


}

