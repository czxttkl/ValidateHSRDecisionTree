package io.github.czxttkl.jsontree;
import io.github.czxttkl.jsontree.exception.DuplicateException;
import io.github.czxttkl.jsontree.exception.NotAppearException;
import io.github.czxttkl.jsontree.exception.OutBoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class JsonTreeTest {
	
	/**
	 * @param n n rungs
	 * @param k k jars
	 * @param q q questions
	 */
	public static int n;
	public static int k;
	public static int q;
	
	public static void main(String[] args) {
		
		if (args.length != 5)
			System.out.println("Incorrect parameters");
		try {
			n = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
			q = Integer.parseInt(args[2]);	
			
			if ( n < 0 || k < 0 || q < 0)
				throw new NumberFormatException();
				
		} catch (NumberFormatException e) {
	        System.err.println("Argument" + " must be an positive integer");
	        System.exit(1);
	    }


		
		
		Gson gson = new Gson();
//		 Root root1 =
//		gson.fromJson("{ \"decisiontree\":[1,{\"h\":0},[2,{\"h\":1},[3,{\"h\":2},[4, {\"h\":3}, {\"h\":4}]]]]}",
//		 Root.class);

//		Root root1 = gson
//				.fromJson(
//						"{ \"decisiontree\":[2, [1, {\"h\":0}, {\"h\":1}], [3, {\"h\":2}, {\"h\":4}]]}",
//						Root.class);
		Root root1 = gson.fromJson("{\"decision_tree\":[4, [2, [1, {\"h\":0}, {\"h\":1}], [3, {\"h\":2}, {\"h\":3}] ], [6, [5, {\"h\":4}, {\"h\":5} ], [7, {\"h\":6}, [8, {\"h\":7}, {\"h\":8}] ] ] ]}", Root.class);
		
		if(root1 != null) {
		checkLeftSubtree(root1.decision_tree[1], root1.getN());
		checkRightSubtree(root1.decision_tree[2], root1.getN());
		checkQEdges(root1, 3);
		checkKYes(root1, 2);
		checkInternalNode(root1, 9);
		checkLeaf(root1, 9);
		}else {
			System.out.println("Invalid json string.");
		}
			
		
	}

	
	public static void checkInternalNode(Root root1, int n) {
		int[] record = new int[n];
		Arrays.fill(record, 0);
		System.out.print("Check if each rung 1..n-1 appears exactly once as internal node of the tree: ");
		try {
			record = recordInternalNode(root1.decision_tree[1], record);
			record = recordInternalNode(root1.decision_tree[2], record);
			record[root1.getN()]++;
			
			for ( int i = 1; i < n; i++) {
				int j = record[i];

				if (j != 1) {
					if ( j > 1)
						throw new DuplicateException(i);
					else
						throw new NotAppearException(i);
				}
				
			}
			
			System.out.println("yes");
			
		} catch (OutBoundException e) {
			System.out.println("no");
			if (e.outIndex > (n-1)) 
				System.out.println("Exists a node with value " + e.outIndex + " that is larger than " + (n-1));
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
	
	
	public static int[] recordInternalNode(Object o, int[] record) throws OutBoundException {
		if(o instanceof LinkedTreeMap)
			return record;
		
		if(o instanceof ArrayList) {
			int i = ((Double) ((ArrayList)o).get(0)).intValue();
			if( i <= record.length-1 && i>=1)
				record[i]++;
			else
				throw new OutBoundException(i);
			record = recordInternalNode( ((ArrayList)o).get(1), record);
			record = recordInternalNode( ((ArrayList)o).get(2), record);
		}
		
		return record;
	}
	
	public static void checkLeaf(Root root1, int n) {
		int[] record = new int[n];
		Arrays.fill(record, 0);
		System.out.print("Check if each rung 0..n-1 appears exactly once as a leaf: ");
		try {
			record = recordLeaf(root1.decision_tree[1], record);
			record = recordLeaf(root1.decision_tree[2], record);
			
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
	
	public static int[] recordLeaf(Object o, int[] record) throws OutBoundException {
		Stack s = new Stack();

		Object prev = null;
		Object curr = o;
		s.push(curr);

		do {

			if (curr instanceof LinkedTreeMap) {
				Double h = (Double) ((LinkedTreeMap) curr).get("h");
				int intH = h.intValue();
				
				if(intH <= record.length-1 && intH >=0) {
					record[intH]++;
				}
				else {
					OutBoundException e = new OutBoundException(intH);
					throw e;
				}
				
				//if prev==null then the subtree is only one leaf
				if(prev!=null) {
					prev = curr;
				 	curr = s.pop();
				 	continue;
				}else
					break;
			}

			if (curr instanceof ArrayList) {

				if (prev == null) {
					prev = curr;
					curr = ((ArrayList) curr).get(1);
					continue;
				}

				if (prev instanceof LinkedTreeMap) {
					if (prev == ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						continue;
					}

					if (prev == ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
				} 
				
				if (prev instanceof ArrayList) {

					if (curr == ((ArrayList) prev).get(2)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}

					if (curr == ((ArrayList) prev).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						continue;
					}

				}

			}
		} while (!s.empty()
				|| (curr instanceof ArrayList ? (prev != ((ArrayList) curr)
						.get(2)) : true));

		return record;
	}
	
	
	public static void checkRightSubtree(Object object, int n) {
		int min = findSmallest(object);
		System.out.println("Check if the root is equal to the smallest node in the right subtree: " + (n==min?"yes":"no"));
		System.out.println("The root is " + n + ". The smallest node in the right subtree is " + min);
	}
	
	
	public static void checkLeftSubtree(Object object, int n) {
		int max = findLargest(object);
		System.out.println("Check if the root is one larger than the largest node in the left subtree: " + (n>max?"yes":"no") );
		System.out.println("The root is " + n + ". The largest node in the left subtree is " + max);
	}



	public static int maxDepth(Object o) {
		if (o instanceof LinkedTreeMap)
			return 0;
		if (o instanceof Root)
			return Math.max(maxDepth(((Root) o).decision_tree[1]),
					maxDepth(((Root) o).decision_tree[2])) + 1;

		// Then o must be arraylist
		ArrayList oa = (ArrayList) o;

		int leftMax = maxDepth(oa.get(1));
		int rightMax = maxDepth(oa.get(2));

		return (leftMax > rightMax) ? leftMax + 1 : rightMax + 1;
	}

	
	
	
	public static void checkQEdges(Root root, int q) {
		int maxDepth = maxDepth(root);
		System.out.println("Check if the longest root-leaf path has " + q
				+ " edges: " + (maxDepth > q ? "no" : "yes"));
		System.out.println("The max depth of the tree: " + maxDepth);
	}

	
	
	public static int findLargest(Object o) {
		int max = 0;
		Stack s = new Stack();

		Object prev = null;
		Object curr = o;
		s.push(curr);

		do {

			if (curr instanceof LinkedTreeMap) {
				Double h = (Double) ((LinkedTreeMap) curr).get("h");
				if (h.intValue() > max) {
					max = h.intValue();
				}
				
				//if prev==null then the subtree is only one leaf
				if(prev!=null) {
					prev = curr;
				 	curr = s.pop();
				 	continue;
				}else
					break;
			}

			if (curr instanceof ArrayList) {

				if (prev == null) {
					prev = curr;
					curr = ((ArrayList) curr).get(1);
					continue;
				}

				if (prev instanceof LinkedTreeMap) {
					if (prev == ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						continue;
					}

					if (prev == ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
				} 
				
				if (prev instanceof ArrayList) {

					if (curr == ((ArrayList) prev).get(2)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}

					if (curr == ((ArrayList) prev).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						continue;
					}

				}

			}
		} while (!s.empty()
				|| (curr instanceof ArrayList ? (prev != ((ArrayList) curr)
						.get(2)) : true));

		return max;
	}

	public static void checkKYes(Root root, int k) {
		int leftTree = maxLeftEdges(root.decision_tree[1]) + 1;
		int rightTree = maxLeftEdges(root.decision_tree[2]);
		int maxLeftEdges = Math.max(leftTree, rightTree);
		System.out.println("Check if there are at most " + k + " yes from the root to any leaf: " + (maxLeftEdges <= k? "yes":"no"));
		System.out.println("The maximum of left edges in the tree is " + maxLeftEdges);
	}
	
	public static int maxLeftEdges(Object o) {
		int maxEdges = 0;
		int edges = 0;
		Stack s = new Stack();
		Object prev = null;
		Object curr = o;
		s.push(curr);

		do {

			if (curr instanceof LinkedTreeMap) {
				//if prev==null then the subtree is only one leaf
				if(prev!=null) {
					prev = curr;
				 	curr = s.pop();
				 	edges--;
				 	continue;
				}else
					break;
			}

			if (curr instanceof ArrayList) {

				if (prev == null) {
					prev = curr;
					curr = ((ArrayList) curr).get(1);
					edges++;
					if(edges > maxEdges)
						maxEdges = edges;
					continue;
				}

				if (prev instanceof LinkedTreeMap) {
					if (prev == ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						edges--;
						continue;
					}

					if (prev == ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
				} 
				
				if (prev instanceof ArrayList) {

					if (curr == ((ArrayList) prev).get(2)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}

					if (curr == ((ArrayList) prev).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						edges++;
						if(edges > maxEdges)
							maxEdges = edges;
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						edges--;
						continue;
					}

				}

			}
		} while (!s.empty()
				|| (curr instanceof ArrayList ? (prev != ((ArrayList) curr)
						.get(2)) : true));

		return maxEdges;
	}
	
	
	
	public static int findSmallest(Object o) {
		int min = 2147483647;
		Stack s = new Stack();

		Object prev = null;
		Object curr = o;
		s.push(curr);

		do {

			if (curr instanceof LinkedTreeMap) {
				Double h = (Double) ((LinkedTreeMap) curr).get("h");
				if (h.intValue() < min) {
					min = h.intValue();
				}
				
				//if prev==null then the subtree is only one leaf
				if(prev!=null) {
					prev = curr;
				 	curr = s.pop();
				 	continue;
				}else
					break;
			}

			if (curr instanceof ArrayList) {

				if (prev == null) {
					prev = curr;
					curr = ((ArrayList) curr).get(1);
					continue;
				}

				if (prev instanceof LinkedTreeMap) {
					if (prev == ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						continue;
					}

					if (prev == ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
				} 
				
				if (prev instanceof ArrayList) {

					if (curr == ((ArrayList) prev).get(2)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}

					if (curr == ((ArrayList) prev).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(1);
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(2)) {
						prev = curr;
						curr = s.pop();
						continue;
					}
					
					if(prev== ((ArrayList) curr).get(1)) {
						s.push(curr);
						prev = curr;
						curr = ((ArrayList) curr).get(2);
						continue;
					}

				}

			}
		} while (!s.empty()
				|| (curr instanceof ArrayList ? (prev != ((ArrayList) curr)
						.get(2)) : true));

		return min;
	}
	
	
	
}
