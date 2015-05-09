package it.unicam.sensorsimulator.plugin.heed.reporting.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import javafx.embed.swing.SwingNode;

public class NetworkGraphic extends SwingNode {

	private int taskID;
	private Graph<Integer, String> g;
	private Layout<Integer, String> layout;

	public NetworkGraphic(int taskID, int pictureWidth, int pictureHeight, Set<Integer> nodesList, HashMap<Integer, ArrayList<Integer>> networkView) {
		this.taskID = taskID;
		g = new DirectedSparseMultigraph<Integer, String>();
		generateVertexts(nodesList);
		generateEdges(networkView);
		
		Dimension preferredSize = new Dimension(600, 600);
		
		layout = new FRLayout<Integer, String>(g);
		layout.setSize(new Dimension(pictureWidth, pictureHeight));
		final VisualizationModel<Integer, String> visualizationModel = 	new DefaultVisualizationModel<Integer, String>(layout, preferredSize);
		BasicVisualizationServer<Integer, String> vv = new VisualizationViewer<Integer, String>(visualizationModel, preferredSize);
		
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		
		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
			public Paint transform(Integer i) {
				if(networkView.containsKey(i)){
					return Color.LIGHT_GRAY;
				}else{
					return Color.GREEN;
				}
				
			}
		};
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				return edgeStroke;
			}
		};
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		this.setContent(vv);
	}

	private void generateEdges(HashMap<Integer, ArrayList<Integer>> networkView) {
		for(Entry<Integer, ArrayList<Integer>> cluster : networkView.entrySet()){
//			System.out.println("grrr" +cluster.getKey() + "-"+ cluster.getValue());
			if(!cluster.getValue().isEmpty()){
				for(Integer clusterMembers : cluster.getValue()){
					g.addEdge(Integer.toString(cluster.getKey()) + "-"+ Integer.toString(clusterMembers), cluster.getKey(), clusterMembers, EdgeType.DIRECTED);
//					System.out.println("generateEdges" +cluster.getKey() + "-"+ Integer.toString(clusterMembers));
				}
			}
		}
	}

	private void generateVertexts(Set<Integer> nodesList) {
		for(Integer node : nodesList){
			g.addVertex(node);
		}
	}

	public int getTaskID() {
		return taskID;
	}
}
