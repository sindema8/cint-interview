## Program Usage

The program takes the file path of the input file as its only command-line argument.

## Exception Handling

The program does some basic validations of the input file and arguments. This validation is not exhaustive as
 the validation of user input should probably not be the major point of focus in this assignment.

## Complexity

### Memory
The list is loaded into memory at once, that is $O(n)$. Then a list of tanks is created, also $O(n)$.
All other variables are $O(1)$. The loading could be done by lines and tanks created iteratively, to save some
space, but in this case (with regard to set limitations), the memory savings would be insignificant and the
code readability could be hindered.

### Time
Creating the tanks list is $O(n)$ and the main loop is $O(n^2)$ in worst case scenario: The search for smallest 
capacity tank is $O(n)$ and the recalculation of the flows as well. And the loop can run up to $n$ times.

The LinkedList implementation is slower when finding min value than ArrayList and in special cases, when there
is not many tanks filling up at the same time, the ArrayList will be faster. But the $O(1)$ complexity of removal
from LinkedList should improve the performance in other cases.

There is a big time penalty for searching for a min value in unordered list. This could be theoretically improved
with the use of other sorted collection for storing filling times, i.e. TreeMap (keys as filling times and values
as number of tanks with the same filling time). This is not implemented, because the double precision arithmetics
introduce some problems with differentiating between the "same" filling times (as mentioned below) and lead to 
inconsistent results.

## Precision
This solution has some limitations in regard to precision. With certain inputs, the double precision division
will introduce rounding errors, which could lead to infinite loops. That is why I introduced tolerance for
doubles comparison. But this can distort the results, when rounding the final value down (as to honor the 
assignment), i.e. from 39.999999999997 to 39 instead of 40. That is why I decided to round up in place of 5th
decimal digit (max tank capacity is 10^9 and double can hold up to 16 digits) and then round down to whole seconds.

When using BigDecimal values, the situation does not improve much, because there can be examples, with infinite
periodicals. So the BigDecimal wil increase complexity of computation and will not mitigate this issue. One approach
might be to use some external library for fractions, i.e. org.apache.commons.lang3.math.Fraction.

