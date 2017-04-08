# O(n^2)

def selection_sort (l):
  '''
    Sorts a list in increasing order.
    First, selects the smallest number and swaps it with the first number.
    Then selects the second smallest number and swaps it with the second number.
    And so on.
  '''
  for i in range( len(l) - 1 ):
    next_smallest_i = get_next_smallest(l, i)
    (List[i], List[next_smallest_i]) = (List[next_smallest_i], List[i])
    
    
def get_next_smallest (l, start_index):
  '''
    Helper function to find the index of the smallest number
    in the portion of the list after the start index.
    Returns the index of the number.
  '''
  min_value = l[start_index]; 
  index = start_index;
    
  for i in range( start_index + 1, len(l) ):
    if l[i] < min_value:
      min_value = l[i]
      index = i
    
  return index
  
