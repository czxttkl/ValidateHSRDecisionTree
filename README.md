ValidateJsonDecisionTree
========================

Usage
--------------------------------------
* Download `hsr.jar`: https://db.tt/onEEoTfa
* Execute 

```bash
java -jar hsr.jar <n> <k> <q> {<-s "json_string"> | <-f json_file_path_name>}
```

* `n`: rungs
* `k`: jars
* `q`: questions
* `-s "json_string"`: input your json string manually
* `-f json_file_path_name`: import your json string from a file


Example
--------------------------------------
```bash
java -jar hsr.jar 9 3 4 -s {"rung":4,"breakNode":{"rung":2,"breakNode":{"rung":1,"breakNode":{"h":0},"surviveNode":{"h":1}},"surviveNode":{"rung":3,"breakNode":{"h":2},"surviveNode":{"h":3}}},"surviveNode":{"rung":6,"breakNode":{"rung":5,"breakNode":{"h":4},"surviveNode":{"h":5}},"surviveNode":{"rung":7,"breakNode":{"h":6},"surviveNode":{"rung":8,"breakNode":{"h":7},"surviveNode":{"h":8}}}}}
```

or

```bash
java -jar hsr.jar 9 3 4 -f test.json
```

HSR Reference
--------------------------------------

http://www.ccs.neu.edu/home/lieber/courses/algorithms/cs5800/sp14/labs/HSR-problem-CS5800-1.pdf


JSON Notation Norms
--------------------------------------
HSRTree class has 3 attributes:
* `rung`: The current rung where a jar is to be dropped
* `breakNode`: The subtree if the jar breaks after falling from the current rung
* `surviveNode`: The subtree if the jar survives after falling from the current rung


Leaf class has 1 attribute:
* `h`: The highest rung keeping jars from breaking

Example
--------------------------------------

```json
{
  "rung": 4,
  "breakNode": {
    "rung": 2,
    "breakNode": {
      "rung": 1,
      "breakNode": {
        "h": 0
      },
      "surviveNode": {
        "h": 1
      }
    },
    "surviveNode": {
      "rung": 3,
      "breakNode": {
        "h": 2
      },
      "surviveNode": {
        "h": 3
      }
    }
  },
  "surviveNode": {
    "rung": 6,
    "breakNode": {
      "rung": 5,
      "breakNode": {
        "h": 4
      },
      "surviveNode": {
        "h": 5
      }
    },
    "surviveNode": {
      "rung": 7,
      "breakNode": {
        "h": 6
      },
      "surviveNode": {
        "rung": 8,
        "breakNode": {
          "h": 7
        },
        "surviveNode": {
          "h": 8
        }
      }
    }
  }
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

* each rung 1..n-1 appears exactly once as an internal node of the tree. (Definition of internal node: a node in a decision tree that is not a leaf.)

* each rung 0..n-1 appears exactly once as a leaf.


