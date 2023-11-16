import java.util.List;


/**
 * T(n) = O(n log n)
 * This is a more efficient way of sorting an ArrayList<String> than the brute force method.
 */
public class QuickSort {
    public static void quickSort(List<Contact> list) {
        quickSort(list, 0, list.size() - 1);
    }

    protected static void quickSort(List<Contact> list, int first, int last) {
        if (last > first) {
            int pivotIndex = partition(list, first, last);
            quickSort(list, first, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, last);
        }
    }


    protected static int partition(List<Contact> list, int first, int last) {
        Contact pivot = list.get(first); // Choose the first element as the pivot
        int low = first + 1; // Index for forward search
        int high = last; // Index for backward search

        while (high > low) {
            // Search forward from left
            while (low <= high && list.get(low).compareTo(pivot) <= 0)
                low++;

            // Search backward from right
            while (low <= high && list.get(high).compareTo(pivot) > 0)
                high--;

            // Swap two elements in the list
            if (high > low) {
                Contact temp = list.get(high);
                list.set(high, list.get(low));
                list.set(low, temp);
            }
        }


        while (high > first && list.get(high).compareTo(pivot) >= 0)
            high--;

        // Swap pivot with list[high]
        if (pivot.compareTo(list.get(high)) > 0) {
            list.set(first, list.get(high));
            list.set(high, pivot);
            return high;
        } else {
            return first;
        }
    }

}