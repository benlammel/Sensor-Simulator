package it.unicam.sensorsimulator.plugin.heed.reporting.graphic;

import it.unicam.sensorsimulator.plugin.heed.reporting.ClusterPicture;
import it.unicam.sensorsimulator.plugin.heed.reporting.ProtocolSnapshot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import javafx.embed.swing.SwingNode;

public class NetworkGraphic extends SwingNode {

	private Graph<Integer, String> g;
	private Layout<Integer, String> layout;
	private ArrayList<Integer> clusterHeads;

	public NetworkGraphic(int pictureWidth, int pictureHeight, Set<Integer> nodesList, ArrayList<Integer> clusterHeads, int until, HashMap<Integer, ClusterPicture> networkView) {
//		g = new DirectedSparseMultigraph<Integer, String>();
//		generateVertexts(nodesList);
//		generateEdges(networkView, until);
//		
//		Dimension preferredSize = new Dimension(pictureWidth, pictureHeight);
//		
//		layout = new FRLayout<Integer, String>(g);
//		layout.setSize(preferredSize);
//		final VisualizationModel<Integer, String> visualizationModel = 	new DefaultVisualizationModel<Integer, String>(layout, preferredSize);
//		BasicVisualizationServer<Integer, String> vv = new VisualizationViewer<Integer, String>(visualizationModel, preferredSize);
//		
//		float dash[] = { 10.0f };
//		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
//				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
//		
//		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
//			public Paint transform(Integer i) {
//				if(clusterHeads.contains(i)){
//					return Color.LIGHT_GRAY;
//				}else{
//					return Color.GREEN;
//				}
//			}
//		};
//		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
//			public Stroke transform(String s) {
//				return edgeStroke;
//			}
//		};
//		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
//		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
//		this.setContent(vv);
	}

	public NetworkGraphic(int pictureWidth, int pictureHeight, Set<Integer> agentList, HashMap<Integer, ProtocolSnapshot> networkEvolutionList, int pictureTil) {
		clusterHeads = new ArrayList<Integer>();
		g = new DirectedSparseMultigraph<Integer, String>();
		generateVertexts(agentList);
		generateEdges(networkEvolutionList, pictureTil);
		
		Dimension preferredSize = new Dimension(pictureWidth, pictureHeight);
		
		layout = new FRLayout<Integer, String>(g);
		layout.setSize(preferredSize);
		final VisualizationModel<Integer, String> visualizationModel = 	new DefaultVisualizationModel<Integer, String>(layout, preferredSize);
		BasicVisualizationServer<Integer, String> vv = new VisualizationViewer<Integer, String>(visualizationModel, preferredSize);
		
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		
		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
			public Paint transform(Integer i) {
				if(clusterHeads.contains(i)){
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

	private void generateEdges(HashMap<Integer, ProtocolSnapshot> networkEvolutionList, int pictureTil) {
		for(int i = 0; i<=pictureTil; i++){
			ProtocolSnapshot snapshot = networkEvolutionList.get(i);
			clusterHeads.add(snapshot.getClusterHead());
			for(int clusterMember : snapshot.getClusterMemberList()){
				g.addEdge(Integer.toString(snapshot.getClusterHead()) + "-"+ Integer.toString(clusterMember), snapshot.getClusterHead(), clusterMember, EdgeType.DIRECTED);
			}
		}
	}

//	private void generateEdges(HashMap<Integer, ClusterPicture> networkView, int until) {
//		for(int i = 0; i<until; i++){
//			if(!networkView.get(i).getList().isEmpty()){
//				for(Integer clusterMember : networkView.get(i).getList()){
//					System.out.println("edge " +Integer.toString(networkView.get(i).getClusterHead()) + "-"+ Integer.toString(clusterMember));
//					g.addEdge(Integer.toString(networkView.get(i).getClusterHead()) + "-"+ Integer.toString(clusterMember), networkView.get(i).getClusterHead(), clusterMember, EdgeType.DIRECTED);
//				}
//				
//			}
//		}
//	}

	private void generateVertexts(Set<Integer> nodesList) {
		for(Integer node : nodesList){
			System.out.println("node " +node);
			g.addVertex(node);
		}
	}
}
