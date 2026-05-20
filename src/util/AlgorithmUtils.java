package util;

import model.Driver;
import java.util.ArrayList;

public class AlgorithmUtils {

    // 1. QUICK SORT by Driver ID (O(n log n))
    public static void sortDriversById(ArrayList<Driver> list, int low, int high) {
        if (low < high) {
            int pi = partitionById(list, low, high);
            sortDriversById(list, low, pi - 1);
            sortDriversById(list, pi + 1, high);
        }
    }

    // ─── QUICK SORT BY NAME (Ascending A-Z) ──────────────────────────────
    public static void sortDriversByName(ArrayList<Driver> list, int low, int high) {
        if (low < high) {
            int pi = partitionByName(list, low, high);
            sortDriversByName(list, low, pi - 1);
            sortDriversByName(list, pi + 1, high);
        }
    }

    private static int partitionByName(ArrayList<Driver> list, int low, int high) {
        String pivot = list.get(high).getName();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            // Compare alphabetically
            if (list.get(j).getName().compareToIgnoreCase(pivot) < 0) {
                i++;
                Driver temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Driver temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    // ─── QUICK SORT BY RATING (Descending 5.0 to 0.0) ────────────────────
    public static void sortDriversByRating(ArrayList<Driver> list, int low, int high) {
        if (low < high) {
            int pi = partitionByRating(list, low, high);
            sortDriversByRating(list, low, pi - 1);
            sortDriversByRating(list, pi + 1, high);
        }
    }

    private static int partitionByRating(ArrayList<Driver> list, int low, int high) {
        double pivot = list.get(high).getRating();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            // Notice the ">" sign. This puts the HIGHEST numbers at the top!
            if (list.get(j).getRating() > pivot) {
                i++;
                Driver temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Driver temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    private static int partitionById(ArrayList<Driver> list, int low, int high) {
        String pivot = list.get(high).getId();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            // Compare strings alphabetically
            if (list.get(j).getId().compareToIgnoreCase(pivot) < 0) {
                i++;
                // Swap list[i] and list[j]
                Driver temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Driver temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    // 2. BINARY SEARCH by Driver ID (O(log n)) - *List MUST be sorted first!*
    public static Driver binarySearchDriverById(ArrayList<Driver> list, String targetId) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Driver midDriver = list.get(mid);
            int comparison = midDriver.getId().compareToIgnoreCase(targetId);

            if (comparison == 0) {
                return midDriver; // Found
            }
            if (comparison < 0) {
                left = mid + 1; // Target is in the right half
            } else {
                right = mid - 1; // Target is in the left half
            }
        }
        return null; // Not found
    }
}