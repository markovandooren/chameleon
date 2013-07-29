package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalyzer.GraphBuilder;
import be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency.DependencyView.LabelGenerator;

public class ZESTGraphBuilder<T> implements GraphBuilder<T>{
	public ZESTGraphBuilder(Graph graph, LabelGenerator<T> generator, Map<T,GraphNode> map) {
		_graph = graph;
		_labelGenerator = generator;
		_nodeMap = map;
	}
	
	private Graph _graph;
	
	private LabelGenerator<T> _labelGenerator;

	@Override
	public void addVertex(T v) {
		_nodeMap.put(v, new GraphNode(_graph, SWT.NONE, _labelGenerator.text(v)));
	}

	private Map<T,GraphNode> _nodeMap;
	
	@Override
	public void addEdge(T first, T second) {
		GraphNode firstNode = _nodeMap.get(first);
		GraphNode secondNode = _nodeMap.get(second);
		GraphConnection connection = new GraphConnection(_graph, ZestStyles.CONNECTIONS_DIRECTED, firstNode, secondNode);
	}
	
}