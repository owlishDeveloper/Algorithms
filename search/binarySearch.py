# O(lb(n))

# While-loop version
def bisection_search (l, target):
  '''
    Searches a given number (target) in a SORTED list of numbers (l)
    Returns the index of that number in the list or -1 if not found
  '''
  min = 0
  max = len(l) - 1
  
  while (max >= min):
    guess = int( (min + max) / 2 )
    if l[guess] == target:
      return guess
    elif l[guess] < target:
      min = guess + 1
    elif l[guess] > target:
      max = guess - 1
      
  return -1
  
  # Recursive version
  def bisection_search_r (l, target, min = None, max = None):
  '''
    Searches a given number (target) in a SORTED list of numbers (l)
    Returns the index of that number in the list or -1 if not found
  '''
  if min is None:
    min = 0
  if max is None:
    max = len(l) - 1
  
  if max < min:
    return -1
    
  guess = int( (min + max) / 2 )
  
  if l[guess] == target:
    return guess
  elif l[guess] < target:
    min = guess + 1
    return bisection_search_r(l, target, min, max)
  elif l[guess] > target:
    max = guess - 1
    return bisection_search_r(l, target, min, max)
