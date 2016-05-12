# PlusMinus
For a list of positive integers, see if it is possible to insert plus or minus signs in front of each positive integer in the list, so that the result equals a positive integer in that list.

Solution uses recursive backtracking to compute every possible permutation of the list. There is a Java and Scheme implementation. 

* PlusMinus.java 
  * Input: `7 1 5 2` (inside in.txt file)
  * Output: `Yes`
* PlusMinus.rkt
  * Input: `(plus-minus '(27 24 3 4 7 8) 0 0)`
  * Output: `#t`