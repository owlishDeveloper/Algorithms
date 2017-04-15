// O(n^2)

function insertionSort(array) {
  for (var i=1; i<array.length; i++) {
    insert(array, i-1, array[i]);
  }
  
  function insert(array, rightIndex, value) {
    for (var j=rightIndex; j>=0 && array[j]>value; j--) {
      array[j+1] = array[j];
    }
    array[j+1] = value;
  }
}
