package cz.blocksolver.backend.block;

import cz.blocksolver.backend.block.arithmetic.*;
import cz.blocksolver.backend.port.InputPort;
import cz.blocksolver.backend.port.PortType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Trida reprezentujici aritmeticky blok
 * @author David Endrych (xendry02)
 * @author Marek Kukučka (xkukuc04)
 */
public class ArithmeticBlock extends Block implements IBlock {

    private Collection<IArithmeticOperation> listOfOperations;
    private IArithmeticOperation operation;
    private InputPort[] inputPorts = new InputPort[2];
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
    public ArithmeticBlock(String name, Integer x, Integer y, Integer width, Integer height, IArithmeticOperation operation) {
        super(name, x, y, width, height, BlockType.ARITHMETIC);
        this.operation = operation;
        this.listOfOperations = new ArrayList<>();
        this.inputPorts[0] = new InputPort(PortType.NUMBER, 0.0, 1);
        this.inputPorts[1] = new InputPort(PortType.NUMBER, 0.0,2);
        initializeListOfOperations();
    }

    /**
     * Metoda inicializuje seznam operaci
     */
    private void initializeListOfOperations() {
        this.listOfOperations.add(AddOperation.getInstance());
        this.listOfOperations.add(SubtractionOperation.getInstance());
        this.listOfOperations.add(MultiplyOperation.getInstance());
        this.listOfOperations.add(DivisionOperation.getInstance());
        this.listOfOperations.add(PowOperation.getInstance());
    }

    /**
     * Metoda meni operaci na bloku
     * @param operation nova operace
     */
    public void changeOperation(IArithmeticOperation operation){
        this.operation = operation;
    }

    /**
     * Metoda provadi operaci
     * @param one prvni hodnota
     * @param two druha hodnota
     * @return uspesnost operace
     */
    public Boolean executeBlock(Double one, Double two){
        if(compareTypes()) {
            this.result = operation.executeOperation(one,two);
            if(this.result.getStatus()){
                outputPort.setValue(result.getResult());
                return true;
            }else{
            }
        }else{
//            TODO:
        }

        return false;
    }


    /**
     * Metoda kontroluje typy portu
     * @return true pokud jsou typy shodne jinak false
     */
    private Boolean compareTypes(){
        return inputPorts[0].getType() == inputPorts[1].getType();
    }

    /**
     * Metoda vraci vstupni port podle indexu
     * @param index Index portu ktery chceme ziskat
     * @throws IndexOutOfBoundsException V pripade spatneho indexu
     * @return vstupni port
     */
    public InputPort getInputPort(Integer index){
        if(index == 1){
            return inputPorts[0];
        }else if(index == 2){
            return inputPorts[1];
        }else{
            throw new IndexOutOfBoundsException("getInputPort in ArithmeticBlock\n");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArithmeticBlock that = (ArithmeticBlock) o;
        return Objects.equals(listOfOperations, that.listOfOperations) &&
                Objects.equals(operation, that.operation) &&
                Arrays.equals(inputPorts, that.inputPorts) &&
                Objects.equals(result, that.result) &&
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

        int result1 = Objects.hash(listOfOperations, operation, result);
        result1 = 31 * result1 + Arrays.hashCode(inputPorts);
        result1 = Objects.hash(result1,name,x,y,width,height,outputPort,type);
        return result1;
    }
}
