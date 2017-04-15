// O(n^2)

function insertionSort(array) {
  // Sorts an array by inserting each next number
  // in the right place in the sorted part of the array
  // to the left of the number.
  // Sorts in increasing order
  for (var i=1; i<array.length; i++) {
    insert(array, i-1, array[i]);
  }
  
  function insert(array, rightIndex, value) {
    // Helper function for inserting number (value)
    // into the sorted part of the array (up to array[rightIndex]).
    for (var j=rightIndex; j>=0 && array[j]>value; j--) {
      array[j+1] = array[j];
    }
    array[j+1] = value;
  }
}
