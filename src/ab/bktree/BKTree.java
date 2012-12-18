package ab.bktree;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BKTree<T> {

	private final MetricSpace<T> metricSpace;
	
	private Node<T> root;

	public static <E> BKTree<E> mkBKTree(MetricSpace<E> ms, Collection<E> elems) {
		
		BKTree<E> bkTree = new BKTree<E>(ms);
		
		for (E elem : elems) {
			bkTree.put(elem);
		}
		
		return bkTree;
	}
	
	
	public BKTree(MetricSpace<T> metricSpace) {
		this.metricSpace = metricSpace;
	}

	public void put(T term) {
		
		if (root == null) {
			root = new Node<T>(term);
		} else {
			root.add(metricSpace, term);
		}		
		
	}
	
	public Set<T> query(T term, int radius) {
		
		Set<T> results = new HashSet<T>();
		
		if (root != null) {
			root.query(metricSpace, term, radius, results);
		}
		
		return results;
	}
	
	private static final class Node<T> {
	
		private final T value;

		private final Map<Integer, Node<T>> children;
		
		public Node(T term) {
			this.value = term;
			this.children = new HashMap<Integer, BKTree.Node<T>>();
		}
		
		public void add(MetricSpace<T> ms, T value) {
			
			Integer distance = ms.distance(this.value, value);
			
			if (distance == 0) {
				return;
			}
			
			Node<T> child = children.get(distance);
			
			if (child == null) {
				children.put(distance, new Node<T>(value));
			} else {
				child.add(ms, value);
			}
		}
		
		public void query(MetricSpace<T> ms, T term, int radius, Set<T> results) {
			
			int distance = ms.distance(this.value, term);
			
			if (distance <= radius) {
				results.add(this.value);
			}
			
			for (int i = Math.max(distance - radius, 1); i <= distance + radius; ++i) {
				
				Node<T> child = children.get(i);
				
				if (child != null) {
					child.query(ms, term, radius, results);
				}
				
			}
			
		}
		
	}
	
}
