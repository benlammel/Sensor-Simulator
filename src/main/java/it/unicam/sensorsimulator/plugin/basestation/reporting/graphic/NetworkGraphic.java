package it.unicam.sensorsimulator.plugin.basestation.reporting.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import javafx.embed.swing.SwingNode;

public class NetworkGraphic extends SwingNode {

	private Graph<Integer, String> g;
	private Layout<Integer, String> layout;

	public NetworkGraphic(HashMap<Integer, Set<Integer>> measurement1) {
		
		g = new SparseMultigraph<Integer, String>();
		generateVertext(measurement1);
		generateEdges(measurement1);

		layout = new KKLayout<Integer, String>(g);
		layout.setSize(new Dimension(600, 400));
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.setPreferredSize(new Dimension(550, 450));
		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
			public Paint transform(Integer i) {
				return Color.GREEN;
			}
		};
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
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

	private void generateEdges(HashMap<Integer, Set<Integer>> measurement1) {
		for (Entry<Integer, Set<Integer>> entry : measurement1.entrySet()) {

			Iterator<Integer> i = entry.getValue().iterator();
			while (i.hasNext()) {
				
				Integer value = i.next();
				if(!entry.getKey().equals(value)){
					System.out.println("addEdge " +entry.getKey() +"-" +value);

					g.addEdge(
							Integer.toString(entry.getKey()) + "-"
									+ Integer.toString(value), entry.getKey(),
							value);
				}
			}
		}
	}

	private void generateVertext(HashMap<Integer, Set<Integer>> measurement1) {
		for (Entry<Integer, Set<Integer>> entry : measurement1.entrySet()) {
			g.addVertex(entry.getKey());
			System.out.println("addVertex " +entry.getKey());
		}
	}
}
