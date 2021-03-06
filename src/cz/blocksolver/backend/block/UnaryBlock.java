package cz.blocksolver.backend.block;

import cz.blocksolver.backend.block.unary.*;
import cz.blocksolver.backend.port.InputPort;
import cz.blocksolver.backend.port.OutputPort;
import cz.blocksolver.backend.port.PortType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Trida reprezentujici unarni blok
 * @author David Endrych (xendry02)
 * @author Marek Kukučka (xkukuc04)
 */
public class UnaryBlock extends Block{

    private IUnaryOperation operation;
    private Collection<IUnaryOperation> listOfOperations;
    private InputPort inputPort;
    private OperationResult result;

    /**
     * Konstruktor
     * @param name nazev bloku
     * @param x x-oova pozice
     * @param y y-nova pozice
     * @param width sirka
     * @param height vyska
     * @param operation Operace bloku
     */
    public UnaryBlock(String name, Integer x, Integer y, Integer width, Integer height, IUnaryOperation operation) {
        super(name, x, y, width, height, BlockType.UNARY);
        this.operation = operation;
        inputPort = new InputPort(PortType.NUMBER, 0.0, 1 );
        listOfOperations = new ArrayList<>();
        initializeListOfOperations();
    }

    /**
     * Metoda inicializuje seznam operaci
     */
    private void initializeListOfOperations() {
        listOfOperations.add(IncrementOperation.getInstance());
        listOfOperations.add(DecrementOperation.getInstance());
        listOfOperations.add(SquareOperation.getInstance());
        listOfOperations.add(SquareRootOperation.getInstance());
        listOfOperations.add(CubeRootOperation.getInstance());
    }

    /**
     * Metoda provadi operaci
     * @param one prvni hodnota
     * @return uspesnost operace
     */
    public Boolean executeBlock(Double one) {
        result = this.operation.executeOperation(one);
        outputPort.setValue(result.getResult());
        return true;
    }

    /**
     * Metoda vraci vstupni port podle indexu
     * @param index Index portu ktery chceme ziskat
     * @throws IndexOutOfBoundsException V pripade spatneho indexu
     * @return vstupni port
     */
    public InputPort getInputPort(Integer index){
        if(index == 1){
            return inputPort;
        }else{
            throw new IndexOutOfBoundsException("getInputPort in GoniometricBlock\n");
        }
    }

    /**
     * Metoda meni operaci na bloku
     * @param operation nova operace
     */
    public void changeOperation(IUnaryOperation operation){
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryBlock that = (UnaryBlock) o;
        return Objects.equals(operation, that.operation) &&
                Objects.equals(listOfOperations, that.listOfOperations) &&
                Objects.equals(inputPort, that.inputPort) &&
                Objects.equals(result, that.result)&&
                Objects.equals(name, that.name) &&
                Objects.equals(x,that.x)&&
                Objects.equals(y,that.y)&&
                Objects.equals(width,that.width)&&
                Objects.equals(height,that.height)&&
                Objects.equals(outputPort,that.outputPort)&&
                Objects.equals(type,that.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(operation, listOfOperations, inputPort, result,name,x,y,width,height,outputPort,type);
    }
}
