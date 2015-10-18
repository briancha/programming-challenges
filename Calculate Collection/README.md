# calculateCollection.java
From a list of numbers, this program extracts a collection of numbers whose sum is a specific number. The algorithm works by subtracting random numbers from the list of numbers from the sum until the sum <= 0. The method is called again recursively if the sum < 0.

Out of this list of numbers
{ 26, 39, 104, 195, 403, 504, 793, 995, 1156, 1673 }
the collection of numbers that add up to 3165 are
1. 26, 195, 793, 995, 1156
2. 195, 504, 793, 1673