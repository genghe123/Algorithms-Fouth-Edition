[Percolation](http://coursera.cs.princeton.edu/algs4/assignments/percolation.html)
---
 ![Percolation yes](https://github.com/genghe123/Algorithms-Fouth-Edition/tree/master/Coursera/Percolation/src/description.jpg)  
 
 使用union-find 方法，即[Disjoint-set data structure](https://en.wikipedia.org/wiki/Disjoint-set_data_structure)，按秩合并  
 可以采用虚拟顶点与虚拟底点的方式判断顶层格子是否与底层格子连通，这样可以无需遍历判断一层的格子
 
 但是这样会有backwash现象，为了解决该问题，可以使用两个Union-Find数组  
 `One for checking if the system percolates(include virtual top and bottom), and the other to check if a given cell is full(only include virtual top)`