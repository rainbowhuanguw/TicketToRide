package route.shortestpaths;

import java.util.*;

public class HeapMinPQ<T>  {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    int start;
    int end;
    private Map<T, Integer> map;


    public HeapMinPQ() {
        items = new ArrayList<>();
        start = START_INDEX;
        end = 0;
        map = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     * use with isPriorTo()
     */
    private void swap(int a, int b) {
        if (a > end || b > end) { // handle out of index
            throw new IllegalArgumentException();
        }

        PriorityNode<T> nodeA = items.get(a);
        PriorityNode<T> nodeB = items.get(b);

        items.set(b, nodeA);   // swap
        items.set(a, nodeB);

    }

    /**
     * check which of the two given node is of smaller priority,
     * true if a's priority < b's, return false if a > b, a = b is not valid
     *
     * @param a : index
     * @param b : index
     * @return true if a's priority < b's, return false if a > b, a = b is not valid
     */

    private boolean isPriorTo(int a, int b) {
        if (a > end || b > end) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> nodeA = items.get(a);
        PriorityNode<T> nodeB = items.get(b);

        return (nodeA.getPriority() < nodeB.getPriority());
    }


    /**
     * find the index of a given item, making use of the contains function of Treemap,
     * takes O(log n) runtime
     *
     * @return the index of the item in the heap, -1 if not found
     */
    private int findIndex(T item) {
        if (map.containsKey(item)) {
            return map.get(item);
        }
        return -1;

    }

    /***************************************************************************
     * Helper functions check if parent node, left and right children node exist
     ***************************************************************************/

    // func: confirming a node has parent
    private boolean hasParent(int index) {
        return index > start && index <= end;
    }

    // func: confirming a node has left child
    private boolean hasLeftChild(int index) {
        return (2 * index + 1) <= end;
    }

    // func: confirming a node has right child
    private boolean hasRightChild(int index) {
        return (2 * index + 2) <= end;
    }

    /***************************************************************************
     * Helper functions to get parent and children nodes
     ***************************************************************************/

    // func: get a node's parent node
    private int getParentIndex(int index) {
        return (int) Math.floor((index - 1) * 1.0 / 2);
    }

    // func: get a node's left child
    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private int getRightChildIndex(int index) {
        return index * 2 + 2;
    }


    /**
     * helper function for percolate down,
     * among a node and it's left and right children,
     * swapping with the left child if it is of the smallest priority among three,
     * swapping with the right if it is of the smallest priority among three
     *
     * @param index - a node as parent
     * @return the final index where an item is placed
     */
    private int percolateDown(int index) {
        // has at least one child
        while (hasLeftChild(index)) {
            int leftIndex = getLeftChildIndex(index);
            boolean leftSmallerThanCurr = isPriorTo(leftIndex, index);
            // has right child
            if (hasRightChild(index)) {
                int rightIndex = getRightChildIndex(index);
                boolean leftSmallerThanRight = isPriorTo(leftIndex, rightIndex);
                boolean rightSmallerThanCurr = isPriorTo(rightIndex, index);
                // swap with left child
                if (leftSmallerThanRight && leftSmallerThanCurr) {
                    T leftItem = items.get(leftIndex).getItem();
                    swap(leftIndex, index);
                    map.put(leftItem, index);
                    index = leftIndex;
                } else if (!leftSmallerThanRight && rightSmallerThanCurr) {
                    T rightItem = items.get(rightIndex).getItem();
                    swap(rightIndex, index);  // swap with right child
                    map.put(rightItem, index);
                    index = rightIndex;
                } else { // stop the loop if the parent is the smallest
                    return index;
                }
            } else {  // doesn't have right child
                if (leftSmallerThanCurr) {
                    T leftItem = items.get(leftIndex).getItem();
                    swap(leftIndex, index);
                    map.put(leftItem, index);
                    index = leftIndex;
                } else {
                    return index;
                }
            }
        }

        return index;
    }

    /**
     * helper function for percolate up,
     * between a node and parent, swapping with the parent if child is
     * of smaller priority
     *
     * @param index - a node's index as child
     */
    private int percolateUp(int index) {

        while (hasParent(index)) {
            int parentIdx = getParentIndex(index);
            if (isPriorTo(index, parentIdx)) {
                T parentItem = items.get(parentIdx).getItem();
                swap(index, parentIdx);
                map.put(parentItem, index);
                index = parentIdx;
            } else {        // once find a child is larger than parent, break and return child index
                break;
            }
        }
        return index;      // return parent index if reaches root
    }



    public void add(T item, double priority) {
        if (item == null || contains(item)) {   // don't allow null item or duplicate items
            throw new IllegalArgumentException();
        }

        PriorityNode<T> node = new PriorityNode<>(item, priority);
        items.add(node); // add to last

        int index;
        if (items.size() != 1) {
            end = start + items.size() - 1;
            index = percolateUp(end);   // percolate up
        } else {
            index = 0;

        }

        map.put(item, index); // add to map

    }


    public boolean contains(T item) {
        return items.size() != 0 && findIndex(item) != -1;
    }


    public T peekMin() {
        if (items.size() == 0) {
            throw new NoSuchElementException();
        }
        return items.get(start).getItem();
    }


    public T removeMin() {
        if (items.size() == 0) {
            throw new NoSuchElementException();
        }

        T min = items.get(start).getItem();
        PriorityNode<T> last = items.remove(end);

        // more than one element left
        if (items.size() != 0) {
            end = start + items.size() - 1;
            items.set(start, last);
            int index = percolateDown(start);
            map.put(last.getItem(), index);   // update the index of the last element being moved
        }

        map.remove(min); //remove from map, O(log n)

        return min;
    }


    public void changePriority(T item, double priority) {
        if (items.size() == 0 || !contains(item)) {
            throw new NoSuchElementException();
        }

        int index = findIndex(item);
        double oldPriority = items.get(index).getPriority();
        items.get(index).setPriority(priority);

        int changedIndex;
        if (oldPriority > priority) {
            changedIndex = percolateUp(index);
        } else {
            changedIndex = percolateDown(index);
        }

        map.put(item, changedIndex); // modify map
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
