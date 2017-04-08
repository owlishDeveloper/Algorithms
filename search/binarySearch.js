// O(lb(n))

// Searches a given number (target) in a SORTED array of numbers (array)
// While-loop version
function biSearch(array, target) {
  // 1. Divide and conquer! Set interval borders:
  // min = lowest possible number
  var min = 0;
  // max = highest possible number
  var max = array.length - 1;
  
  var guess;
  
  // 1.5. Check if max is more than min. If it's not, target cannot be found
  while (max >= min) {
    // 2. Guess a number that's right in the middle of [min, max] interval
    guess = Math.floor((min + max) / 2);
    
    // 3.1. If that number == target, we have found the number. Return its index
    if (array[guess] === target)
      return guess;
      
    // 3.2. If that number is less than target, all the numbers before that number can be discarded.
    else if (array[guess] < target)
      min = guess + 1;
    
    // 3.3. If that number is more than target, all the numbers after that number can be discarded.
    else if (array[guess] > target)
      max = guess - 1;
  }
  
  return -1;
}


// Recursive version
function biSearchR(array, target, min, max) {
  if (!min && min != 0)
    min = 0;
  if (!max && max != 0)
    max = array.length - 1;
  
  if (min > max)
    return -1;
  
  var guess = Math.floor((min + max) / 2);
  
  if (array[guess] === target)
    return guess;
  else if (array[guess] < target) {
    min = guess + 1;
    return biSearchR(array, target, min, max);
  } else if (array[guess] > target) {
    max = guess - 1;
    return biSearchR(array, target, min, max);
  }
}
