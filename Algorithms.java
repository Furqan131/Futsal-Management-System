public class Algorithms {

    public void display(FutsalGround[] array, int size) {
        System.out.println("--- LIST OF COURTS ---");
        for (int i = 0; i < size; i++) {
            System.out.println(array[i]);
        }
    }

    public int binarySearchById(String id, FutsalGround[] array) {
        sortById(array, array.length); 
        
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = compareIds(array[mid].id, id);

            if (comparison == 0) {
                return mid; 
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public void linearSearchByName(FutsalGround[] array, int size, String name) {
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (array[i].name.equalsIgnoreCase(name)) {
                System.out.println("FOUND: " + array[i]);
                found = true;
            }
        }
        if (!found) System.out.println("NOT FOUND");
    }

    public void sortByName(FutsalGround[] array, int size) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (array[j].name.compareToIgnoreCase(array[j + 1].name) > 0) {
                    FutsalGround temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }


    public void sortById(FutsalGround[] array, int size) {
        for (int i = 0; i < size - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (compareIds(array[j].id, array[minIndex].id) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                FutsalGround temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }

    public void sortByPrice(FutsalGround[] array, int size) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (array[j].price > array[j + 1].price) {
                    FutsalGround temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private int compareIds(String id1, String id2) {
        if (isNumeric(id1) && isNumeric(id2)) {
            try {
                long n1 = Long.parseLong(id1);
                long n2 = Long.parseLong(id2);
                return Long.compare(n1, n2);
            } catch (NumberFormatException e) {
                return id1.compareTo(id2);
            }
        }
        return id1.compareToIgnoreCase(id2);
    }

    private boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}