package astah_drawio_plugin.internal.mxgraph.builder.node;

import java.util.HashMap;
import java.util.Map;

import com.change_vision.jude.api.inf.model.IPartition;
import com.change_vision.jude.api.inf.presentation.INodePresentation;

import astah_drawio_plugin.internal.annotation.GraphElementBuilder;
import astah_drawio_plugin.internal.mxgraph.builder.base.MxGraphNodeBuilder;

@GraphElementBuilder(astahTypes = "Partition")
public class MxGraphPartitionBuilder extends MxGraphNodeBuilder {

	private static IPartition getSuperDimension(IPartition partition) {
		var superPartition = partition.getSuperPartition();
		while (superPartition != null) {
			partition = superPartition;
			superPartition = partition.getSuperPartition();
		}
		return partition;
	}

	private static int calcDepth(IPartition partition, Map<IPartition, Integer> result) {
		if (result.containsKey(partition)) {
			return result.get(partition);
		}

		var subPartitions = partition.getSubPartitions();
		var max = 0;
		for (var subPartition : subPartitions) {
			var depth = calcDepth(subPartition, result);
			if (depth > max) {
				max = depth;
			}
		}

		max++;
		result.put(partition, max);
		return max;
	}

	private static int getSize(IPartition partition, Map<IPartition, Integer> partitionDepth) {
		if (partitionDepth.get(partition) == 1) {
			return partitionDepth.get(partition.getSuperPartition()) - 1;
		} else {
			return 1;
		}
	}

	public MxGraphPartitionBuilder(MxGraphNodeBuilder parent, INodePresentation p) {
		super(parent, p);

		if (parent == null) {
			// this is dimension (invisible)
			this.addStyle("fillColor", "none");
			this.addStyle("strokeColor", "none");
		} else {
			var model = (IPartition) p.getModel();
			var dimension = getSuperDimension(model);
			var depthCache = new HashMap<IPartition, Integer>();
			calcDepth(dimension, depthCache);
			var size = getSize(model, depthCache);

			var horizontal = (model.isHorizontal()) ? "0" : "1";
			this.addStyle("swimlane", null);
			this.addStyle("html", "1");
			this.addStyle("startSize", String.valueOf(35 * size));
			this.addStyle("collapsible", "0");
			this.addStyle("horizontal", horizontal);
			this.addStyle("swimlaneLine", "1");
			this.addStyle("whiteSpace", "wrap");
			this.setValue(p.getLabel());
		}
	}
}
