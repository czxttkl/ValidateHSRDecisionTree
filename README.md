ValidateJsonDecisionTree
========================

Usage
--------------------------------------
* Download hsr.jar
* Execute
```bash
java -jar hsr.jar <n> <k> <q> {<-s json_string> | <-f file_path_name>}
```

HSR Reference
--------------------------------------

http://www.ccs.neu.edu/home/lieber/courses/algorithms/cs5800/f13/piazza/hsr/HSR-problem-CS5800-1.pdf


JSON Notation Norms
--------------------------------------

The grammar and object structure would be in an EBNF-like notation:

```bash
DTH = "{" "\"decision tree\"" ":" <dt> DT.
DT = Compound | Leaf.
Compound = "[" <q> int "," <yes> DT "," <no> DT "]".
Leaf = "{" "\"h\" " ":" <leaf> int "}".
```

Example
--------------------------------------

```json
 // h = highest safe rung or leaf
{ "decision tree" :
[1,{"h":0},[2,{"h":1},[3,{"h":2},{"h":3}]]]
}
```


Validation
--------------------------------------
A decision tree in DT for HSR(n,k,q) must satisfy the following properties:

* the BST (Binary Search Tree Property): For any left subtree: the root is one larger than 
the largest node in the subtree and for any right subtree the root is equal to the smallest 
(i.e., leftmost) node in the subtree.

* there are at most k yes from the root to any leaf.

* the longest root-leaf path has q edges.

* each rung 1..n-1 appears exactly once as internal node of the tree.

* each rung 0..n-1 appears exactly once as a leaf.


