# O(n^2)

def selectionSort (List):
  '''
    Sorts a list in increasing order.
    First, selects the smallest number and swaps it with the first number.
    Then selects the second smallest number and swaps it with the second number.
    And so on.
  '''
  for i in range( len(List) - 1 ):
    nextSmallest_i = getNextSmallest(List, i)
    (List[i], List[nextSmallest_i]) = (List[nextSmallest_i], List[i])
    
    
def getNextSmallest (List, startIndex):
  '''
    Helper function to find the index of the smallest number
    in the portion of the list after the start index.
    Returns the index of the number.
  '''
  minValue = List[startIndex]; 
  index = startIndex;
    
  for i in range( startIndex + 1, len(List) ):
    if List[i] < minValue:
      minValue = List[i]
      index = i
    
  return index
  
