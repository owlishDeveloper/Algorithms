// O(n^2)

function selectionSort(array) {
  // Sorts an array by selecting the smallest number
  // and swapping it with the first number. Then it
  // selects the second smallest number and swaps it with the
  // second number and so on, until the array is sorted.
  
  // Increasing order

  for (var i = 0; i < array.length; i++) {
    // Loop over the array and swap each element with the smallest
    // from the rest of the array
    swap(array, i, getNextSmallest(array, i));
  }
  
  // Helper function to perform a swap operation if the target indices are known
  // Takes in the array and indices of the two elements to swap
  function swap(array, currentIndex, nextSmallestIndex) {
    var temp = array[currentIndex];
    array[currentIndex] = array[nextSmallestIndex];
    array[nextSmallestIndex] = temp;
  }
  
  // Helper function to find the index of the first smallest number
  // in the portion of the array after the current index.
  // Takes in array and current index; returns the index
  function getNextSmallest(array, startIndex) {
    var minValue = array[startIndex];
    var index = startIndex;
    
    for (var i = startIndex + 1; i < array.length; i++) {
      if (array[i] < minValue) {
        minValue = array[i];
        index = i;
      }
    }
    return index;
  }
}
