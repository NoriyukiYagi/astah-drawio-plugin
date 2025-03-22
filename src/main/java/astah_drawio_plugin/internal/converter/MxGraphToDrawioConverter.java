package astah_drawio_plugin.internal.converter;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

public class MxGraphToDrawioConverter {
	public byte[] convert(mxGraph graph) throws TransformerFactoryConfigurationError, TransformerException {
		var codec = new mxCodec();
		var doc = codec.encode(graph.getModel());
		var xml = mxXmlUtils.getXml(doc);

		// draw.ioの標準フォーマットでラップ
		var drawioFileContent = "<mxfile><diagram id=\"diagram1\" name=\"Page-1\">" + xml + "</diagram></mxfile>";

		var transformer = TransformerFactory.newDefaultInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // インデント幅を指定 (ここでは4スペース)

		// XML文字列をSourceに変換
		var xmlInput = new StreamSource(new StringReader(drawioFileContent));

		// 結果を格納するWriterを準備
		var stringWriter = new StringWriter();
		var xmlOutput = new StreamResult(stringWriter);

		// 整形処理を実行
		transformer.transform(xmlInput, xmlOutput);

		return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
	}
}
