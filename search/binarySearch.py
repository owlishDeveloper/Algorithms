# O(lb(n))

# While-loop version
def biSearch (List, target):
  '''
    Searches a given number (target) in a SORTED list of numbers (List)
    Returns the index of that number in the list or -1 if not found
  '''
  min = 0
  max = len(List) - 1
  
  while (max >= min):
    guess = int( (min + max) / 2 )
    if List[guess] == target:
      return guess
    elif List[guess] < target:
      min = guess + 1;
    elif List[guess] > target:
      max = guess - 1;
      
  return -1
  
  # Recursive version
  def biSearchR (List, target, min = None, max = None):
  '''
    Searches a given number (target) in a SORTED list of numbers (List)
    Returns the index of that number in the list or -1 if not found
  '''
  if min is None:
    min = 0
  if max is None:
    max = len(List) - 1
  
  if max < min:
    return -1
    
  guess = int( (min + max) / 2 )
  
  if List[guess] == target:
    return guess
  elif List[guess] < target:
    min = guess + 1;
    return biSearchR(List, target, min, max)
  elif List[guess] > target:
    max = guess - 1;
    return biSearchR(List, target, min, max)
