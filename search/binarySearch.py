# O(b(n))

# While-loop version
def bisection_search (a_list, target):
  '''
    Searches a given number (target) in a SORTED list of numbers (a_list)
    Returns the index of that number in the list or -1 if not found
  '''
  min = 0
  max = len(a_list) - 1
  
  while (max >= min):
    guess = int( (min + max) / 2 )
    if a_list[guess] == target:
      return guess
    elif a_list[guess] < target:
      min = guess + 1
    elif a_list[guess] > target:
      max = guess - 1
      
  return -1
  
  # Recursive version
  def bisection_search_r (a_list, target, min = None, max = None):
  '''
    Searches a given number (target) in a SORTED list of numbers (a_list)
    Returns the index of that number in the list or -1 if not found
  '''
  if min is None:
    min = 0
  if max is None:
    max = len(a_list) - 1
  
  if max < min:
    return -1
    
  guess = int( (min + max) / 2 )
  
  if a_list[guess] == target:
    return guess
  elif a_list[guess] < target:
    min = guess + 1
    return bisection_search_r(a_list, target, min, max)
  elif a_list[guess] > target:
    max = guess - 1
    return bisection_search_r(a_list, target, min, max)
