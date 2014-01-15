package io.github.czxttkl.jsontree;
import io.github.czxttkl.jsontree.exception.DuplicateException;
import io.github.czxttkl.jsontree.exception.NotAppearException;
import io.github.czxttkl.jsontree.exception.OutBoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class HSR {
	
	/**
	 * @param n n rungs
	 * @param k k jars
	 * @param q q questions
	 */
	public static int n;
	public static int k;
	public static int q;
	
	public static boolean leftSubTreeValidated = false;
	public static boolean rightSubTreeValidated = false;
	public static boolean qEdgesValidated = false;
	public static boolean kYesValidated = false;
	public static boolean internalNodeValidated = false;
	public static boolean leafValidated = false;

	
	public static void main(String[] args) {
		
		if (args.length != 5) {
			System.out.println("Invalid arguments length.");
			System.exit(1);
		}
		
		try {
			n = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
			q = Integer.parseInt(args[2]);	
			
			if ( n < 0 || k < 0 || q < 0)
				throw new NumberFormatException();
				
		} catch (NumberFormatException e) {
	        System.err.println("Invalid arguments. Argument must be an positive integer");
	        System.exit(1);
	    }
		Gson gson = new GsonBuilder().registerTypeAdapter(HSRTree.class, new InterfaceDeserializer()).create();
		HSRTree root = null;
		if (args[3].equals("-s")){
			String json = args[4];
			try {
			root = gson.fromJson(json, HSRTree.class);
			} catch (JsonSyntaxException e) {
				System.out.println("Invalid arguments. Invalid json string.");
				System.exit(1);
			}
		} else {
			if(args[3].equals("-f")) {
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(new File(args[4]).getCanonicalPath()));
					root = gson.fromJson(br, HSRTree.class);
				} catch (FileNotFoundException e) {
					System.out.println("Invalid arguments. Invalid file name.");
					System.exit(1);
				} catch (IOException e) {
					System.out.println("Invalid arguments. Can't read file.");
					System.exit(1);
				} catch (JsonSyntaxException e) {
					System.out.println("Invalid arguments. Invalid json string.");
					System.exit(1);
				}
			} else {
				System.out.println("Invalid arguments. Specify from String or from File.");
				System.exit(1);
			}
			
		}
		
		
		if (root != null && root.getLeftNode()!=null && root.getRightNode()!=null) {
			checkLeftSubtree(root.getLeftNode(), root.getRung());
			checkRightSubtree(root.getRightNode(), root.getRung());
			checkQEdges(root, q);
			checkKYes(root, k);
			checkInternalNode(root, n);
			checkLeaf(root, n);
		} else {
			System.out.println("Invalid json string.");
			System.exit(1);
		}
		
		System.out.println("");
		System.out.println("--------------------------------------");
		if (leftSubTreeValidated && rightSubTreeValidated && qEdgesValidated && kYesValidated && internalNodeValidated && leafValidated) {
			System.out.println("Your decision tree is validated.");
		} else {
			System.out.println("Your decision tree is not validated.");
		}
		System.out.println("--------------------------------------");
			
		
	}

	public static void checkLeftSubtree(Node node, int n) {
		int max = findLargest(node);
		leftSubTreeValidated = n - max == 1 ? true : false;
		System.out.println("");
		System.out.println("Check if the root is one larger than the largest node in the left subtree: " + (leftSubTreeValidated ? "yes" : "no"));
		System.out.println("The root is " + n + ". The largest node in the left subtree is " + max);
	}

	public static int findLargest(Node o) {
		if (o instanceof Leaf)
			return ((Leaf) o).getH();
		else {
			return Math.max(findLargest(((HSRTree) o).getLeftNode()), findLargest(((HSRTree) o).getRightNode()));
		}
	}

	public static void checkRightSubtree(Node node, int n) {
		int min = findSmallest(node);
		rightSubTreeValidated = n == min ? true : false;
		System.out.println("");
		System.out.println("Check if the root is equal to the smallest node in the right subtree: " + (rightSubTreeValidated ? "yes" : "no"));
		System.out.println("The root is " + n + ". The smallest node in the right subtree is " + min);
	}

	public static int findSmallest(Node o) {
		if (o instanceof Leaf)
			return ((Leaf) o).getH();
		else {
			return Math.min(findSmallest(((HSRTree) o).getLeftNode()), findSmallest(((HSRTree) o).getRightNode()));
		}
	}

	public static void checkQEdges(HSRTree root, int q) {
		int maxDepth = maxDepth(root);
		if (q >= maxDepth)
			qEdgesValidated = true;
		System.out.println("");
		System.out.println("Check if the longest root-leaf path has " + q + " edges: " + (q >= maxDepth ? "yes" : "no"));
		System.out.println("The max depth of the tree: " + maxDepth);
	}

	public static int maxDepth(Node node) {
		if (node instanceof Leaf)
			return 0;
		if (node instanceof HSRTree)
			return Math.max(maxDepth(((HSRTree) node).getLeftNode()), maxDepth(((HSRTree) node).getRightNode())) + 1;
		return -1;
	}

	public static void checkKYes(HSRTree root, int k) {
		int maxLeftEdges = maxLeftEdges(root, 0);
		if (maxLeftEdges <= k)
			kYesValidated = true;
		System.out.println("");
		System.out.println("Check if there are at most " + k + " yes from the root to any leaf: " + (kYesValidated ? "yes" : "no"));
		System.out.println("The maximum of left edges in the tree is " + maxLeftEdges);
	}

	/**
	 * @param side
	 *            1: left child of its parent; -1: right child of its parent; 0: no parent;
	 */
	public static int maxLeftEdges(Node node, int side) {
		if (node instanceof Leaf) {
			return 0;
		} else {
			return Math.max(maxLeftEdges(((HSRTree) node).getLeftNode(), 1) + 1, maxLeftEdges(((HSRTree) node).getRightNode(), -1));
		}
	}

	public static void checkInternalNode(HSRTree root, int n) {
		int[] record = new int[n];
		Arrays.fill(record, 0);
		System.out.println("");
		System.out.print("Check if each rung 1..n-1 appears exactly once as internal node: ");
		try {
			record = recordInternalNode(root, record);
			for (int i = 1; i < n; i++) {
				int j = record[i];
				if (j != 1) {
					if (j > 1)
						throw new DuplicateException(i);
					else
						throw new NotAppearException(i);
				}
			}
			System.out.println("yes");
			internalNodeValidated = true;
		} catch (OutBoundException e) {
			System.out.println("no");
			if (e.outIndex > (n - 1))
				System.out.println("Exists a node with value " + e.outIndex + " that is larger than " + (n - 1));
			else
				System.out.println("Exists a node with value " + e.outIndex + " that is smaller than 0");
		} catch (DuplicateException e) {
			System.out.println("no");
			System.out.println("Exists more than one nodes with the same value: " + e.dupIndex);
		} catch (NotAppearException e) {
			System.out.println("no");
			System.out.println("Rung " + e.noIndex + " didn't show up as a internal node.");
		}
	}

	public static int[] recordInternalNode(Node node, int[] record) throws OutBoundException {
		if (node instanceof Leaf) {
			return record;
		} else {
			int i = ((HSRTree) node).getRung();
			if (i <= record.length - 1 && i >= 1)
				record[i]++;
			else
				throw new OutBoundException(i);
			record = recordInternalNode(((HSRTree) node).getLeftNode(), record);
			record = recordInternalNode(((HSRTree) node).getRightNode(), record);
			return record;
		}
	}
	
	public static void checkLeaf(HSRTree root, int n) {
		int[] record = new int[n];
		Arrays.fill(record, 0);
		System.out.println("");
		System.out.print("Check if each rung 0..n-1 appears exactly once as a leaf: ");
		try {
			record = recordLeaf(root, record);
			for(int p = 0; p < n; p++) {
				int q = record[p];
				if ( q!=1 ) {
					if (q>1)
						throw new DuplicateException(p);
					else
						throw new NotAppearException(p);
				}
			}
			System.out.println("yes");
			leafValidated = true;
		} catch (OutBoundException e) {
			System.out.println("no");
			if(e.outIndex > (n-1)){				
				System.out.println("Exists a leaf with value " + e.outIndex + " that is larger than " + (n-1));
			}else {
				System.out.println("Exists a leaf with value " + e.outIndex + " that is smaller than 0");
			}
		} catch (DuplicateException e) {
			System.out.println("no");
			System.out.println("Exists more than one leaves with the same value: " + e.dupIndex);
		} catch (NotAppearException e) {
			System.out.println("no");
			System.out.println("Rung " + e.noIndex + " didn't show up as a leaf.");
		}
	}
	
	public static int[] recordLeaf(Node node, int[] record) throws OutBoundException {
		if (node instanceof Leaf) {
			int h = ((Leaf)node).getH();
			if(h <= record.length-1 && h >=0) {
				record[h]++;
			} else {
				throw new OutBoundException(h);
			}
			return record;
		} else {
			record = recordLeaf(((HSRTree) node).getLeftNode(), record);
			record = recordLeaf(((HSRTree) node).getRightNode(), record);
			return record;
		}
	}
	
}
