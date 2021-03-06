package cz.blocksolver.frontend;

import cz.blocksolver.backend.block.*;
import cz.blocksolver.backend.block.arithmetic.*;
import cz.blocksolver.backend.block.goniometric.*;
import cz.blocksolver.backend.block.unary.*;
import cz.blocksolver.backend.port.PortType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.scene.paint.Color.DODGERBLUE;
import static javafx.scene.paint.Paint.valueOf;


/**
 * Objekt, ktery reprezentuje jednotlive zobrazene bloky
 * @author David Endrych (xendry02)
 * @author Marek Kukučka (xkukuc04)
 */
public class DragBlock extends AnchorPane{

    @FXML public AnchorPane block_pane;
    @FXML public Label block_type;
    @FXML public Circle input_1;
    @FXML public Circle input_2;
    @FXML public Circle output;

    private EventHandler handle_input1 = null;
    private EventHandler handle_input2 = null;
    private EventHandler handle_output = null;
    private EventHandler handle_showResult = null;
    private EventHandler handle_righClick = null;

    public Boolean isHiglighted = false;
    private DragBlock self = this;
    private Boolean showingResult = false;
    public MainDisplay display;
    public Integer XCoord;
    public Integer YCoord;
    public Integer Index;
    public String Type;
    public String Operation;
    public ConnectingLine inputPortLine1 = new ConnectingLine();
    public ConnectingLine inputPortLine2 = new ConnectingLine();
//    public Boolean eventsActive = false;
    public Block dragBlock =  new ArithmeticBlock("Unknown", 0, 0, 64, 64, AddOperation.getInstance());

    /**
     * Konstruktor, nastavi styl bloku
     */
    public DragBlock(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource("/cz/blocksolver/frontend/resources/DragBlock.fxml")));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            loader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }

        block_pane.setStyle("-fx-border-style: solid;"
                + "-fx-border-width:1;" + "-fx-background-color: white;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");

        input_1.setStroke(valueOf("white"));
        input_2.setStroke(valueOf("white"));
        output.setStroke(valueOf("white"));
    }

    /**
     * Konstruktor, nastavi styl bloku
     * @param display - objekt reprezentujici hlavni obrazovku
     */
    public DragBlock(MainDisplay display){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource("/cz/blocksolver/frontend/resources/DragBlock.fxml")));
        loader.setRoot(this);
        loader.setController(this);
        this.display = display;
        try {
            loader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    /**
     * Konstruktor, nastavi styl bloku
     * @param type - typ bloku
     * @param display - objekt reprezentujici hlavni obrazovku
     */
    public DragBlock(String type, MainDisplay display){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation((getClass().getResource("/cz/blocksolver/frontend/resources/DragBlock.fxml")));
        loader.setRoot(this);
        loader.setController(this);
        this.display = display;
        try {
            loader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }

        if(type.equals("g")){
            input_2.setVisible(false);
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:1;" + "-fx-background-color: #47a8bd;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: #2e6b79;");
            input_1.setLayoutX(11.0);
            input_1.setLayoutY(36.0);

        }else if(type.equals("u")){
            input_2.setVisible(false);
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:1;" + "-fx-background-color: #ffb84d;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: #ba7e4d;");
            input_1.setLayoutX(11.0);
            input_1.setLayoutY(36.0);
        }else if(type.equals("a")){
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:1;" + "-fx-background-color: #f5e663;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: #dfd25a;");
        }

    }

    /**
     * Slouzi k nastaveni event handleru
     * @param load - informuje zda jsou event handlery
     *             nastavovany na bloku, ktery byl nacten
     *             z XML dokumentu
     */
    public void activateEvents(Boolean load) {
        if(!load){
            chooseBlock();
        }else{
            setBlockStyle();
        }

        Label label = new Label();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Detail");
        item1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String name = BlockDetail.display(dragBlock, self);
                if(name != null){
                    if(!name.equals("")){
                        dragBlock.setName(name);
                    }
                }
            }
        });
        MenuItem item2 = new MenuItem("Remove");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for(int i = 0; i < display.getDragBlockList().size();i++) {
                    if (display.getDragBlockList().get(i).dragBlock.getInputPort(1).getConnectedToOutputPort()) {
                        if (display.getDragBlockList().get(i).dragBlock.getInputPort(1).getOutputPort().getBlockIndex() == Index) {
                            display.getDragBlockList().get(i).getInputPortLine1().getLine().setVisible(false);
                            display.getDragBlockList().get(i).getInputPortLine1().setLine(null);
                            display.getDragBlockList().get(i).dragBlock.getInputPort(1).setConnectedToOutputPort(false);
                            display.getDragBlockList().get(i).dragBlock.getInputPort(1).setOutputPort(null);
                            display.getDragBlockList().get(i).dragBlock.getInputPort(1).setValueSet(false);
                            display.getDragBlockList().get(i).dragBlock.setExecuted(false);
                        }
                    }
                    if (display.getDragBlockList().get(i).dragBlock.getType() == BlockType.ARITHMETIC) {
                        if (display.getDragBlockList().get(i).dragBlock.getInputPort(2).getConnectedToOutputPort()) {
                            if (display.getDragBlockList().get(i).dragBlock.getInputPort(2).getOutputPort().getBlockIndex() == Index) {
                                display.getDragBlockList().get(i).getInputPortLine2().getLine().setVisible(false);
                                display.getDragBlockList().get(i).getInputPortLine2().setLine(null);
                                display.getDragBlockList().get(i).dragBlock.getInputPort(2).setConnectedToOutputPort(false);
                                display.getDragBlockList().get(i).dragBlock.getInputPort(2).setOutputPort(null);
                                display.getDragBlockList().get(i).dragBlock.getInputPort(2).setValueSet(false);
                                display.getDragBlockList().get(i).dragBlock.setExecuted(false);
                            }
                        }
                    }
                }

                for(int i = 0; i < display.getDragBlockList().size();i++){

                    if(display.getDragBlockList().get(i).getIndex() == Index){
                        display.getDragBlockList().get(i).setVisible(false);
                        if(inputPortLine1.getLine() != null){
                            inputPortLine1.getLine().setVisible(false);
                            inputPortLine1.setLine(null);

                        }
                        if(display.getDragBlockList().get(i).dragBlock.getType() == BlockType.ARITHMETIC){
                            if(inputPortLine2.getLine() != null){
                                inputPortLine2.getLine().setVisible(false);
                                inputPortLine2.setLine(null);
                            }
                        }
                        display.getDragBlockList().remove(i);
                        display.schema.removeBlock(dragBlock);
                    }
                }
            }
        });

        contextMenu.getItems().addAll(item1, item2);

        block_pane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(block_pane, event.getScreenX(), event.getScreenY());
            }
        });

        Handle_showResult(display.showBlockValue);

        dragBlock.getOutputPort().setBlockIndex(Index);
        if(Type.equals("arithmetic")){
            handle_input1 = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(display.getOutputActive()){
                        display.setOutputActive(false);
                        display.outputDragBlock.removeHiglightOutputPort();
                        display.inputPort = dragBlock.getInputPort(1);
                        display.inputIndex = Index;
                        if(display.checkPortsIndex()){
                            if(inputPortLine1.getLine() != null){
                                inputPortLine1.getLine().setVisible(false);
                                inputPortLine1.setLine(null);

                            }
                            inputPortLine1 = new ConnectingLine(display.outputPort,display.getOutputCoords().get("x"), display.getOutputCoords().get("y"),
                                    XCoord, YCoord, 1 , "a");
                            inputPortLine1.setLine(display.ConnectBlocks(XCoord, YCoord, "a" ,1, display.outputPort, inputPortLine1));
                            dragBlock.getInputPort(1).setOutputPort(display.outputPort);
                            dragBlock.getInputPort(1).setConnectedToOutputPort(true);
                        }

                    }else{
                        String val;
                        if(dragBlock.getInputPort(1).getOutputPort() != null){
                            val = ChangeInputArithmetic.display("first", dragBlock.getInputPort(1).getOutputPort().getValue());
                        }else{
                            val = ChangeInputArithmetic.display("first", dragBlock.getInputPort(1).getValue());
                        }
                        if(val.equals("canceled")){
                        }else{
                            dragBlock.getInputPort(1).setValueSet(true);
                            if(inputPortLine1.getLine() != null){
                                inputPortLine1.getLine().setVisible(false);
                                inputPortLine1.setLine(null);
                                dragBlock.getInputPort(1).setConnectedToOutputPort(false);
                            }
                            dragBlock.getInputPort(1).setValue(Double.parseDouble(val));
                        }
                    }
                }
            };
            handle_input2 = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(display.getOutputActive()){
                        display.setOutputActive(false);
                        display.outputDragBlock.removeHiglightOutputPort();
                        display.inputPort = dragBlock.getInputPort(1);
                        display.inputIndex = Index;
                        if(display.checkPortsIndex()){
                            if(inputPortLine2.getLine() != null){
                                inputPortLine2.getLine().setVisible(false);
                                inputPortLine2.setLine(null);
                            }
                            inputPortLine2 = new ConnectingLine(display.outputPort,display.getOutputCoords().get("x"), display.getOutputCoords().get("y"),
                                    XCoord, YCoord, 2 , "a");
                            inputPortLine2.setLine(display.ConnectBlocks(XCoord, YCoord, "a", 2, display.outputPort, inputPortLine2));

                            dragBlock.getInputPort(2).setOutputPort(display.outputPort);
                            dragBlock.getInputPort(2).setConnectedToOutputPort(true);
                        }

                    }else{
                        String val;
                        if(dragBlock.getInputPort(2).getOutputPort() != null){
                            val = ChangeInputArithmetic.display("second", dragBlock.getInputPort(2).getOutputPort().getValue());
                        }else{
                            val = ChangeInputArithmetic.display("second", dragBlock.getInputPort(2).getValue());
                        }
                        if(val.equals("canceled")){
                        }else{
                            if(inputPortLine2.getLine() != null){
                                inputPortLine2.getLine().setVisible(false);
                                inputPortLine2.setLine(null);

                                dragBlock.getInputPort(2).setConnectedToOutputPort(false);
                            }
                            dragBlock.getInputPort(2).setValueSet(true);
                            dragBlock.getInputPort(2).setValue(Double.parseDouble(val));
                        }
                    }
                }
            };
            handle_output = Handle_output();


            handle_righClick = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    MouseButton button = event.getButton();
                    if(button==MouseButton.SECONDARY){

                    }
                }
            };


            input_1.setOnMouseClicked(handle_input1);
            input_2.setOnMouseClicked(handle_input2);
            output.setOnMouseClicked(handle_output);
//            block_pane.setOnMouseEntered(handle_showResult);
            block_pane.setOnMouseClicked(handle_righClick);
        }else if(Type.equals("goniometric")){
            input_2.setVisible(false);
            handle_input1 = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(display.getOutputActive()){
                        display.setOutputActive(false);
                        display.outputDragBlock.removeHiglightOutputPort();
                        display.inputPort = dragBlock.getInputPort(1);
                        display.inputIndex = Index;
                        dragBlock.getInputPort(1).setType(PortType.DEGREE);
                        if(display.checkPortsIndex()){
                            if(inputPortLine1.getLine() != null){
                                inputPortLine1.getLine().setVisible(false);
                                inputPortLine1.setLine(null);

                            }
                            inputPortLine1 = new ConnectingLine(display.outputPort,display.getOutputCoords().get("x"), display.getOutputCoords().get("y"),
                                    XCoord, YCoord, 1 , "g");
                            inputPortLine1.setLine(display.ConnectBlocks(XCoord, YCoord, "g" ,1, display.outputPort, inputPortLine1));

                            dragBlock.getInputPort(1).setOutputPort(display.outputPort);
                            dragBlock.getInputPort(1).setConnectedToOutputPort(true);
                            if(display.outputPort.getType() == PortType.NUMBER){
                                dragBlock.getInputPort(1).setType(PortType.NUMBER);
                            }else if(display.outputPort.getType() == PortType.DEGREE){
                                dragBlock.getInputPort(1).setType(PortType.DEGREE);
                            }else if(display.outputPort.getType() == PortType.RADIAN){
                                dragBlock.getInputPort(1).setType(PortType.RADIAN);
                            }
//                            dragBlock.getInputPort(1).setType();
                        }
                    }else{
                        GoniometricInput val;
                        if(dragBlock.getInputPort(1).getOutputPort() != null){
                            val = ChangeInputGoniometric.display("first", dragBlock.getInputPort(1).getOutputPort().getValue(),dragBlock.getInputPort(1).getType());
                        }else{
                            val = ChangeInputGoniometric.display("first", dragBlock.getInputPort(1).getValue(), dragBlock.getInputPort(1).getType());
                        }

                        if(val.getValue().equals("canceled")){
                        }else{
                            if(Double.parseDouble(val.getValue()) == dragBlock.getInputPort(1).getValue()){
                                dragBlock.getInputPort(1).setType(val.getType());
                            }else{
                                if(inputPortLine1.getLine() != null){
                                    inputPortLine1.getLine().setVisible(false);
                                    inputPortLine1.setLine(null);

                                    dragBlock.getInputPort(1).setConnectedToOutputPort(false);
                                }
                                dragBlock.getInputPort(1).setValue(Double.parseDouble(val.getValue()));
                                dragBlock.getInputPort(1).setType(val.getType());
                                dragBlock.getInputPort(1).setValueSet(true);

                            }
                        }
                    }
                }
            };
            handle_output = Handle_output();

            input_1.setOnMouseClicked(handle_input1);
            output.setOnMouseClicked(handle_output);

        }else if(Type.equals("unary")){
            input_2.setVisible(false);
            handle_input1 = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(display.getOutputActive()){
                        display.setOutputActive(false);
                        display.outputDragBlock.removeHiglightOutputPort();
                        display.inputPort = dragBlock.getInputPort(1);
                        display.inputIndex = Index;
                        if(display.checkPortsIndex()){
                            if(inputPortLine1.getLine() != null){
                                inputPortLine1.getLine().setVisible(false);
                                inputPortLine1.setLine(null);

                            }
                            inputPortLine1 = new ConnectingLine(display.outputPort,display.getOutputCoords().get("x"), display.getOutputCoords().get("y"),
                                    XCoord, YCoord, 1 , "u");
                            inputPortLine1.setLine(display.ConnectBlocks(XCoord, YCoord, "u" ,1, display.outputPort, inputPortLine1));

                            dragBlock.getInputPort(1).setOutputPort(display.outputPort);
                            dragBlock.getInputPort(1).setConnectedToOutputPort(true);
                        }
                    }else{
                        String val;
                        if(dragBlock.getInputPort(1).getOutputPort() != null){
                            val = ChangeInputUnary.display("first", dragBlock.getInputPort(1).getOutputPort().getValue());
                        }else{
                            val = ChangeInputUnary.display("first", dragBlock.getInputPort(1).getValue());
                        }
                        if(val.equals("canceled")){
                        }else{
                            dragBlock.getInputPort(1).setValueSet(true);
                            if(inputPortLine1.getLine() != null){
                                inputPortLine1.getLine().setVisible(false);

                                inputPortLine1.setLine(null);
                                dragBlock.getInputPort(1).setConnectedToOutputPort(false);
                            }
                            dragBlock.getInputPort(1).setValue(Double.parseDouble(val));
                        }
                    }
                }
            };

            handle_output = Handle_output();
            input_1.setOnMouseClicked(handle_input1);
            output.setOnMouseClicked(handle_output);
        }

    }


    @FXML
    private void initialize() {}


    /**
     * Vytvori a zobrazi box, ktery pozaduje zadat vstupni hodnotu portu
     * @param portNum - pozice vstupniho portu
     */
    public void demandInputValue(Integer portNum){
        if(dragBlock.getType() == BlockType.ARITHMETIC || dragBlock.getType() == BlockType.UNARY){
            if(portNum == 1){
                String val;
                val = "12";
                if(dragBlock.getType() == BlockType.ARITHMETIC){
                    val = ChangeInputArithmetic.display("first", dragBlock.getInputPort(1).getValue());
                }else{
                    val = ChangeInputUnary.display("first", dragBlock.getInputPort(1).getValue());
                }

                dragBlock.getInputPort(1).setValueSet(true);
                dragBlock.getInputPort(1).setValue(Double.parseDouble(val));
            }else{
                String val;
                val = "45";
                val = ChangeInputArithmetic.display("second", dragBlock.getInputPort(2).getValue());
                dragBlock.getInputPort(2).setValueSet(true);
                dragBlock.getInputPort(2).setValue(Double.parseDouble(val));
            }
        }else{
            GoniometricInput val;
            val = ChangeInputGoniometric.display("first", dragBlock.getInputPort(1).getValue(),dragBlock.getInputPort(1).getType());
            dragBlock.getInputPort(1).setValue(Double.parseDouble(val.getValue()));
            dragBlock.getInputPort(1).setType(val.getType());
            dragBlock.getInputPort(1).setValueSet(true);
        }
    }


    /**
     * Presune DragBlock na danou pozici
     * @param point2D - koordinaty nove pozice
     */
    public void relocateToPoint(Point2D point2D) {
        Point2D localCoordinates = getParent().sceneToLocal(point2D);

        relocate(
                (int) (localCoordinates.getX() - (getBoundsInLocal().getWidth()/2)),
                (int) (localCoordinates.getY() - (getBoundsInLocal().getHeight()/2))
        );
    }

    /**
     * V zavislosti na typu blocku a operaci nastavi objekt Block
     */
    public void chooseBlock(){
        if (Type.equals("arithmetic")){
            if(Operation.equals("add")){
                dragBlock =  new ArithmeticBlock("Arr", XCoord, YCoord, 64, 64, AddOperation.getInstance());
            }else if(Operation.equals("sub")){
                dragBlock =  new ArithmeticBlock("Arr", XCoord, YCoord, 64, 64, SubtractionOperation.getInstance());
            }else if(Operation.equals("mult")){
                dragBlock =  new ArithmeticBlock("Arr", XCoord, YCoord, 64, 64, MultiplyOperation.getInstance());
            }else if(Operation.equals("div")){
                dragBlock =  new ArithmeticBlock("Arr", XCoord, YCoord, 64, 64, DivisionOperation.getInstance());
            }else if(Operation.equals("pow")){
                dragBlock =  new ArithmeticBlock("Arr", XCoord, YCoord, 64, 64, PowOperation.getInstance());
            }
        }else if(Type.equals("goniometric")){
            if(Operation.equals("sin")){
                dragBlock = new GoniometricBlock("Gon", XCoord, YCoord, 64, 64, SinusOperation.getInstance());
                dragBlock.getInputPort(1).setType(PortType.DEGREE);
            }else if(Operation.equals("cos")){
                dragBlock = new GoniometricBlock("Gon", XCoord, YCoord, 64, 64, CosinusOperation.getInstance());
                dragBlock.getInputPort(1).setType(PortType.DEGREE);
            }else if(Operation.equals("tang")){
                dragBlock = new GoniometricBlock("Gon", XCoord, YCoord, 64, 64, TangensOperation.getInstance());
                dragBlock.getInputPort(1).setType(PortType.DEGREE);
            }else if(Operation.equals("cotg")){
                dragBlock = new GoniometricBlock("Gon", XCoord, YCoord, 64, 64, CotangensOperation.getInstance());
                dragBlock.getInputPort(1).setType(PortType.DEGREE);
            }
        }else if(Type.equals("unary")){
            if(Operation.equals("cro")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, CubeRootOperation.getInstance());
            }else if(Operation.equals("pot")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, SquareOperation.getInstance());
            }else if(Operation.equals("sqr")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, SquareRootOperation.getInstance());
            }else if(Operation.equals("dec")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, DecrementOperation.getInstance());
            }else if(Operation.equals("inc")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, IncrementOperation.getInstance());
            }else if(Operation.equals("rad")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, NumToRad.getInstance());
                dragBlock.getOutputPort().setType(PortType.RADIAN);
            }
            else if(Operation.equals("deg")){
                dragBlock = new UnaryBlock("Unary", XCoord, YCoord, 64, 64, NumToDeg.getInstance());
                dragBlock.getOutputPort().setType(PortType.DEGREE);
            }
        }
    }


    /**
     * Nastavi koordinaty pro DragBlock
     * @param x - pozice X pro DragBlock
     * @param y - pozice Y pro DragBlock
     */
    public void setCoordinates(double x, double y) {
        Double dx = new Double(x);
        Double dy = new Double(y);
        XCoord = dx.intValue();
        YCoord = dy.intValue();
    }


    /**
     * Zvyrazni DragBlock
     */
    public void higlight() {
        isHiglighted = true;
        if(Type.equals("goniometric")){
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:2;" + "-fx-background-color: #47a8bd;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: red;");
        }else if(Type.equals("unary")){
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:2;" + "-fx-background-color: #ffb84d;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: red;");
        }else if(Type.equals("arithmetic")){
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:2;" + "-fx-background-color: #f5e663;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: red;");
        }
    }

    /**
     * Odstrani zvyrazneni DragBlocku
     */
    public void removeHiglight() {
        isHiglighted = false;
        setBlockStyle();

    }

    /**
     * Stara se o zobrazovani hodnoty DragBlocku v pripadeze je vyzadovano
     * @param On - informace zda ma zobrazit hodnotu DragBlocku
     */
    public void Handle_showResult(Boolean On) {
        display.showBlockValue = On;
        if(On){
            handle_showResult = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!showingResult){
                        showingResult = true;
                        Label tempLabel = new Label();
                        tempLabel.setLayoutX(XCoord-232);
                        tempLabel.setLayoutY(YCoord-84);
                        Timer timer = new java.util.Timer();
                        if(dragBlock.getExecuted()){
                            tempLabel.setText(dragBlock.getOutputPort().getValue().toString());
                            display.main_display.getChildren().add(tempLabel);

                            timer.schedule(new TimerTask() {
                                public void run() {
                                    Platform.runLater(new Runnable() {
                                        public void run() {
                                            display.main_display.getChildren().remove(tempLabel);
                                            showingResult = false;
                                        }
                                    });
                                }
                            }, 2000);
                        }else{
                            tempLabel.setText("Block not executed");
                            display.main_display.getChildren().add(tempLabel);

                            timer.schedule(new TimerTask() {
                                public void run() {
                                    Platform.runLater(new Runnable() {
                                        public void run() {
                                            display.main_display.getChildren().remove(tempLabel);
                                            showingResult = false;
                                        }
                                    });
                                }
                            }, 2000);
                        }
                    }

                }
            };

        }else{
            handle_showResult = null;
        }

        block_pane.setOnMouseEntered(handle_showResult);
    }

    /**
     * Nastavi styl blocku
     */
    public void setBlockStyle(){
        if(Type.equals("goniometric")){
            input_2.setVisible(false);
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:1;" + "-fx-background-color: #47a8bd;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: #2e6b79;");
            input_1.setLayoutX(11.0);
            input_1.setLayoutY(36.0);
        }else if(Type.equals("unary")){
            input_2.setVisible(false);
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:1;" + "-fx-background-color: #ffb84d;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: #ba7e4d;");
            input_1.setLayoutX(11.0);
            input_1.setLayoutY(36.0);
        }else if(Type.equals("arithmetic")){
            block_pane.setStyle("-fx-border-style: solid;"
                    + "-fx-border-width:1;" + "-fx-background-color: #f5e663;"
                    + "-fx-border-radius: 5;" + "-fx-border-color: #dfd25a;");
        }
    }

    /**
     * Slouzi k navraceni Event handleru
     * @return - Event handler pro vystupno port
     */
    public EventHandler Handle_output() {
        EventHandler ev = handle_output = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!display.getOutputActive()) {
                    display.outputIndex = Index;
                    display.setOutputCoords(XCoord, YCoord);
                    display.setOutputActive(true);
                    display.outputPort = dragBlock.getOutputPort();
                    display.outputDragBlock = self;
                    higlightOutputPort();
                } else {
                    display.setOutputActive(false);
                    removeHiglightOutputPort();
                }
            }
        };
        return ev;
    }

    /**
     * Zvyrazni vystupni port
     */
    public void higlightOutputPort(){
        output.setFill(valueOf("#eda2a2"));
        output.setStroke(valueOf("#d00000"));
    }

    /**
     * Zrusi oznaceni vystupniho portu
     */
    public void removeHiglightOutputPort(){
        output.setFill(valueOf("white"));
        output.setStroke(valueOf("black"));
    }


    /**
     * Vraci informaci jestli je DragBlock zvyrazneny
     * @return
     */
    public Boolean getHiglighted() {
        return isHiglighted;
    }

    /**
     * Vrati objekt, ktery drzi informace o propoji prvniho vstupniho portu
     * @return
     */
    public ConnectingLine getInputPortLine1() {
        return inputPortLine1;
    }


    /**
     * Nastavi objekt, ktery drzi informace o propoji prvniho vstupniho portu
     * @param inputPortLine1
     */
    public void setInputPortLine1(ConnectingLine inputPortLine1) {
        this.inputPortLine1 = inputPortLine1;
    }

    /**
     * Vrati objekt, ktery drzi informace o propoji druheho vstupniho portu
     * @return
     */
    public ConnectingLine getInputPortLine2() {
        return inputPortLine2;
    }
    /**
     * Nastavi objekt, ktery drzi informace o propoji druheho vstupniho portu
     * @param inputPortLine2
     */
    public void setInputPortLine2(ConnectingLine inputPortLine2) {
        this.inputPortLine2 = inputPortLine2;
    }

    /**
     * Vrati objekt reprezentujici hlavni displej
     * @return
     */
    public MainDisplay getDisplay() {
        return display;
    }

    /**
     * Nastavi hodnotu objektu reprezentujici hlavni displej
     * @param display
     */
    public void setDisplay(MainDisplay display) {
        this.display = display;
    }

    /**
     * Nastavi jmeno dragBLock
     * @param name
     */
    public void setName(String name){
        block_type.setText(name);
    }

    /**
     * Vrati jmeno dragBLock
     * @return
     */
    public String getName(){
        return block_type.getText();
    }


    /**
     * Vrati typ bloku
     * @return
     */
    public String getType() {
        return Type;
    }

    /**
     * Vrati index bloku
     * @return
     */
    public Integer getIndex() {
        return Index;
    }

    /**
     * Vrati operaci, kterou blok vykonava
     * @return
     */
    public String getOperation() {
        return Operation;
    }

    /**
     * Nastavi Index Dragblock
     * @param index
     */
    public void setIndex(Integer index) {
        Index = index;
    }

    /**
     * Nastavy typ DragBlocku
     * @param type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * Nastavi operaci DragBlocku
     * @param operation
     */
    public void setOperation(String operation) {
        Operation = operation;
    }


}