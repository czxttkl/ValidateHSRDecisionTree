package io.github.czxttkl.test;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import io.github.czxttkl.jsontree.HSRTree;
import io.github.czxttkl.jsontree.InterfaceDeserializer;
import io.github.czxttkl.jsontree.Leaf;
import io.github.czxttkl.jsontree.Node;
import io.github.czxttkl.jsontree.exception.DuplicateException;
import io.github.czxttkl.jsontree.exception.NotAppearException;
import io.github.czxttkl.jsontree.exception.OutBoundException;

public class TestMain {
	public static boolean leftSubTreeValidated = false;
	public static boolean rightSubTreeValidated = false;
	public static boolean qEdgesValidated = false;
	public static boolean kYesValidated = false;
	public static boolean internalNodeValidated = false;
	public static boolean leafValidated = false;

	public static void main(String... args) {
		HSRTree hsrTreeRoot = new HSRTree(4);
		HSRTree hsrTreeLeft = new HSRTree(2);
		HSRTree hsrTreeRight = new HSRTree(6);

		HSRTree hsrTreeLeftLeft = new HSRTree(1);
		hsrTreeLeftLeft.setLeftNode(new Leaf(0));
		hsrTreeLeftLeft.setRightNode(new Leaf(1));

		HSRTree hsrTreeLeftRight = new HSRTree(3);
		hsrTreeLeftRight.setLeftNode(new Leaf(2));
		hsrTreeLeftRight.setRightNode(new Leaf(3));

		hsrTreeLeft.setLeftNode(hsrTreeLeftLeft);
		hsrTreeLeft.setRightNode(hsrTreeLeftRight);

		hsrTreeRoot.setLeftNode(hsrTreeLeft);

		HSRTree hsrTreeRightLeft = new HSRTree(5);
		hsrTreeRightLeft.setLeftNode(new Leaf(4));
		hsrTreeRightLeft.setRightNode(new Leaf(5));

		HSRTree hsrTreeRightRight = new HSRTree(7);
		hsrTreeRightRight.setLeftNode(new Leaf(6));
		HSRTree hsrTreeRightRightRight = new HSRTree(8);
		hsrTreeRightRightRight.setLeftNode(new Leaf(7));
		hsrTreeRightRightRight.setRightNode(new Leaf(8));

		hsrTreeRightRight.setRightNode(hsrTreeRightRightRight);

		hsrTreeRight.setLeftNode(hsrTreeRightLeft);
		hsrTreeRight.setRightNode(hsrTreeRightRight);

		hsrTreeRoot.setRightNode(hsrTreeRight);

		// Create a Gson instance to convert TernaryDecisionTree Java Object into JSON String
		Gson gson = new GsonBuilder().registerTypeAdapter(HSRTree.class, new InterfaceDeserializer()).create();
		String jsonString = gson.toJson(hsrTreeRoot);
		System.out.println(jsonString);

		HSRTree root1 = gson.fromJson(jsonString, HSRTree.class);

		checkLeftSubtree(root1.getLeftNode(), root1.getRung());
		checkRightSubtree(root1.getRightNode(), root1.getRung());
		checkQEdges(root1, 4);
		checkKYes(root1, 3);
		checkInternalNode(root1, 9);
		checkLeaf(root1, 9);
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
			int i = ((HSRTree) node).getRung();
			record = recordLeaf(((HSRTree) node).getLeftNode(), record);
			record = recordLeaf(((HSRTree) node).getRightNode(), record);
			return record;
		}
	}
}
