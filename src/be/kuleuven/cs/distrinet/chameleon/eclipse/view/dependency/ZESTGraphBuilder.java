//package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.zest.core.widgets.Graph;
//import org.eclipse.zest.core.widgets.GraphConnection;
//import org.eclipse.zest.core.widgets.GraphNode;
//import org.eclipse.zest.core.widgets.ZestStyles;
//
//import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalyzer.GraphBuilder;
//import be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency.DependencyView.LabelGenerator;
//
//public class ZESTGraphBuilder<T> implements GraphBuilder<T>{
//	public ZESTGraphBuilder(Graph graph, LabelGenerator<T> generator) {
//		_graph = graph;
//		_labelGenerator = generator;
//		_nodeMap = new HashMap<>();
//		_edgeMap = new HashMap<>();
//	}
//	
//	public void reset() {
//		_graph.dispose();
//	}
//	
//	private Graph _graph;
//	
//	private LabelGenerator<T> _labelGenerator;
//
//	@Override
//	public void addVertex(T v) {
//		if(!_nodeMap.containsKey(v)) {
//			_nodeMap.put(v, new GraphNode(_graph, SWT.NONE, _labelGenerator.text(v)));
//		}
//	}
//
//	private Map<T,GraphNode> _nodeMap;
//	
//	private Map<T,Set<T>> _edgeMap;
//	
//	@Override
//	public void addEdge(T first, T second) {
//		Set<T> set = _edgeMap.get(first);
//		if(set != null && ! set.contains(second)) {
//			GraphNode firstNode = _nodeMap.get(first);
//			GraphNode secondNode = _nodeMap.get(second);
//			GraphConnection connection = new GraphConnection(_graph, ZestStyles.CONNECTIONS_DIRECTED, firstNode, secondNode);
//		}
//	}
//	
//}