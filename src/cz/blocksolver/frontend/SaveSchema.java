package cz.blocksolver.frontend;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cz.blocksolver.backend.block.ArithmeticBlock;
import cz.blocksolver.backend.block.Block;
import cz.blocksolver.backend.block.BlockType;
import cz.blocksolver.backend.schema.Schema;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Stara se o ulozeni Schematu do XML souboru
 * @author David Endrych (xendry02)
 * @author Marek Kukučka (xkukuc04)
 */
public class SaveSchema {

    /**
     * Provede ulozeni aktualniho schematu do XML souboru
     * @param schema
     * @param dragBlocks
     * @param primaryStage
     */
    public void execute(Schema schema, ArrayList<DragBlock> dragBlocks, Stage primaryStage) {


        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("schema");
            doc.appendChild(rootElement);

            Element blockElement;
            Element blockNameElement;
            Element blockCoordXElement;
            Element blockCoordYElement;
            Element blockWidthElement;
            Element blockHeightElement;
            Element blockTypeElement;
            Element blockInputPort1Element;
            Element blockInputPort2Element;
            Element blockOperatioTypeElement;
            Element inputOutputPortElement;
            Element input2OutputPortElement;
            Element inputPortTypeElement;
            Element inputValueElement;
            Element input2ValueElement;
            Element blockIndexElement;
            Element line1Element;
            Element line1OutX;
            Element line1OutY;
            Element line1InX;
            Element line1InY;
            Element line1Type;
            Element line1OutportIndex;
            Element line2Element;
            Element line2OutX;
            Element line2OutY;
            Element line2InX;
            Element line2InY;
            Element line2Type;
            Element line2OutportIndex;
            Element dragBlockNameElement;

            Block currentBlock;
            DragBlock currDragBlock;
            for(int i = 0; i < schema.getBlocks().size(); i++ ){
                currentBlock = schema.getBlocks().get(i);
                currDragBlock = dragBlocks.get(i);
                blockElement = doc.createElement("block");
                rootElement.appendChild(blockElement);

                blockNameElement = doc.createElement("name");
                blockNameElement.setTextContent(currentBlock.getName());
                blockElement.appendChild(blockNameElement);

                dragBlockNameElement = doc.createElement("drag_name");
                dragBlockNameElement.setTextContent(currDragBlock.getName());
                blockElement.appendChild(dragBlockNameElement);

                blockCoordXElement = doc.createElement("coord_x");
                blockCoordXElement.setTextContent(currDragBlock.XCoord.toString());
                blockElement.appendChild(blockCoordXElement);

                blockCoordYElement = doc.createElement("coord_y");
                blockCoordYElement.setTextContent(currDragBlock.YCoord.toString());
                blockElement.appendChild(blockCoordYElement);

                blockWidthElement = doc.createElement("width");
                blockWidthElement.setTextContent(currentBlock.getWidth().toString());
                blockElement.appendChild(blockWidthElement);

                blockHeightElement = doc.createElement("height");
                blockHeightElement.setTextContent(currentBlock.getHeight().toString());
                blockElement.appendChild(blockHeightElement);

                blockTypeElement = doc.createElement("block_type");
                blockTypeElement.setTextContent(currentBlock.getType().toString());
                blockElement.appendChild(blockTypeElement);

                blockInputPort1Element = doc.createElement("input_port1");
                blockElement.appendChild(blockInputPort1Element);

                inputOutputPortElement = doc.createElement("in1_output_port");
                inputValueElement = doc.createElement("in1_value");

                if(!currentBlock.getInputPort(1).getConnectedToOutputPort()){
                    if(currentBlock.getInputPort(1).getValueSet()){
                        inputValueElement.setTextContent(currentBlock.getInputPort(1).getValue().toString());
                    }else{
                        inputValueElement.setTextContent("null");
                    }
                    blockElement.appendChild(inputValueElement);
                }else{
                    inputOutputPortElement.setTextContent(currentBlock.getInputPort(1).getOutputPort().getBlockIndex().toString());
                    blockElement.appendChild(inputOutputPortElement);
                }
                if(currentBlock.getType() == BlockType.GONIOMETRIC){
                    inputPortTypeElement = doc.createElement("in_type");
                    inputPortTypeElement.setTextContent(currentBlock.getInputPort(1).getType().toString());
                    blockElement.appendChild(inputPortTypeElement);
                }
                if(currentBlock.getType() == BlockType.ARITHMETIC){
                    blockInputPort2Element = doc.createElement("input_port2");
                    blockElement.appendChild(blockInputPort2Element);
                    input2OutputPortElement = doc.createElement("in2_output_port");
                    input2ValueElement = doc.createElement("in2_value");
                    if(!currentBlock.getInputPort(2).getConnectedToOutputPort()){
                        if(currentBlock.getInputPort(2).getValueSet()){
                            input2ValueElement.setTextContent(currentBlock.getInputPort(2).getValue().toString());
                        }else{
                            input2ValueElement.setTextContent("null");
                        }
                        blockElement.appendChild(input2ValueElement);
                    }else{
                        input2OutputPortElement.setTextContent(currentBlock.getInputPort(2).getOutputPort().getBlockIndex().toString());
                        blockElement.appendChild(input2OutputPortElement);
                    }
                }
                blockOperatioTypeElement = doc.createElement("operation_type");
                blockOperatioTypeElement.setTextContent(currDragBlock.getOperation());
                blockElement.appendChild(blockOperatioTypeElement);
                blockIndexElement = doc.createElement("index");
                blockIndexElement.setTextContent(currentBlock.getOutputPort().getBlockIndex().toString());
                blockElement.appendChild(blockIndexElement);
                if(currDragBlock.getInputPortLine1().getLine() != null){
                    line1Element = doc.createElement("line1");
                    blockElement.appendChild(line1Element);

                    line1OutX= doc.createElement("out_x");
                    line1OutX.setTextContent(currDragBlock.getInputPortLine1().getOutX().toString());
                    blockElement.appendChild(line1OutX);

                    line1OutY= doc.createElement("out_y");
                    line1OutY.setTextContent(currDragBlock.getInputPortLine1().getOutY().toString());
                    blockElement.appendChild(line1OutY);

                    line1InX= doc.createElement("in_x");
                    line1InX.setTextContent(currDragBlock.getInputPortLine1().getInX().toString());
                    blockElement.appendChild(line1InX);

                    line1InY= doc.createElement("in_y");
                    line1InY.setTextContent(currDragBlock.getInputPortLine1().getInY().toString());
                    blockElement.appendChild(line1InY);

                    line1Type= doc.createElement("type");
                    line1Type.setTextContent(currDragBlock.getInputPortLine1().getType().toString());
                    blockElement.appendChild(line1Type);

                    line1OutportIndex= doc.createElement("out_index");
                    line1OutportIndex.setTextContent(currDragBlock.getInputPortLine1().getOutputPort().getBlockIndex().toString());
                    blockElement.appendChild(line1OutportIndex);

                }

                if(currDragBlock.getInputPortLine2().getLine() != null){
                    line2Element = doc.createElement("line2");
                    blockElement.appendChild(line2Element);

                    line2OutX= doc.createElement("out_x2");
                    line2OutX.setTextContent(currDragBlock.getInputPortLine2().getOutX().toString());
                    blockElement.appendChild(line2OutX);

                    line2OutY= doc.createElement("out_y2");
                    line2OutY.setTextContent(currDragBlock.getInputPortLine2().getOutY().toString());
                    blockElement.appendChild(line2OutY);

                    line2InX= doc.createElement("in_x2");
                    line2InX.setTextContent(currDragBlock.getInputPortLine2().getInX().toString());
                    blockElement.appendChild(line2InX);

                    line2InY= doc.createElement("in_y2");
                    line2InY.setTextContent(currDragBlock.getInputPortLine2().getInY().toString());
                    blockElement.appendChild(line2InY);

                    line2Type= doc.createElement("type2");
                    line2Type.setTextContent(currDragBlock.getInputPortLine2().getType().toString());
                    blockElement.appendChild(line2Type);

                    line2OutportIndex= doc.createElement("out_index2");
                    line2OutportIndex.setTextContent(currDragBlock.getInputPortLine2().getOutputPort().getBlockIndex().toString());
                    blockElement.appendChild(line2OutportIndex);
                }

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Schema");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if(file != null){
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);

                StreamResult consoleResult = new StreamResult(System.out);
                transformer.transform(source, consoleResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}