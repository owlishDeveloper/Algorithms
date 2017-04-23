// O(n log(n))

// CLRS variant
function mergeSort(array, p, r) {
    if (p < r) {
        var q = Math.floor( (p + r) / 2 );
        mergeSort(array, p, q);
        mergeSort(array, q+1, r);
        merge(array, p, q, r);
    }
    
    function merge(array, p, q, r) {
      // 0. Preliminary: Divide the array into halves according to the calculated indeces
      var lowHalf = [];
      var highHalf = [];

      var k = p;
      var i;
      var j;
      for (i = 0; k <= q; i++, k++) {
          lowHalf[i] = array[k];
      }
      for (j = 0; k <= r; j++, k++) {
          highHalf[j] = array[k];
      }
      
      // 1. Merge the halves
      k = p;
      i = 0;
      j = 0;

      while (i < lowHalf.length && j < highHalf.length) {
          if (lowHalf[i] < highHalf[j]) {
              array[k] = lowHalf[i];
              i++;
          } else {
              array[k] = highHalf[j];
              j++;
          }
          k++;
      }

      while (i < lowHalf.length) {
          array[k] = lowHalf[i];
          k++;
          i++;
      }

      while (j < highHalf.length) {
          array[k] = highHalf[j];
          k++;
          j++;
      }
  }
}

