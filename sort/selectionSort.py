# O(n^2)

def selection_sort (a_list):
  '''
    Sorts a list in increasing order.
    First, selects the smallest number and swaps it with the first number.
    Then selects the second smallest number and swaps it with the second number.
    And so on.
  '''
  for i in range( len(a_list) - 1 ):
    next_smallest_i = get_next_smallest(a_list, i)
    ( a_list[i], a_list[next_smallest_i] ) = ( a_list[next_smallest_i], a_list[i] )
    
    
def get_next_smallest (a_list, start_index):
  '''
    Helper function to find the index of the smallest number
    in the portion of the list after the start index.
    Returns the index of the number.
  '''
  min_value = a_list[start_index]; 
  index = start_index;
    
  for i in range( start_index + 1, len(a_list) ):
    if a_list[i] < min_value:
      min_value = a_list[i]
      index = i
    
  return index
  
