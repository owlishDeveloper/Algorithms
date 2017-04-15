# O(n^2)

def insertionSort (a):
    '''
    Sorts an array of numbers in increasing order by
    inserting each next number to the right place 
    in the sorted part of the array to the left of the number.
    '''
    def insert (a, rightIndex, value):
        '''
        Helper function to perform insert operation.
        '''
        j = rightIndex
        while (j>=0 and a[j]>value):
            a[j+1] = a[j]
            j -= 1
        a[j+1] = value
    
    for i in range( 1, len(a) ):
        insert(a, i-1, a[i])
