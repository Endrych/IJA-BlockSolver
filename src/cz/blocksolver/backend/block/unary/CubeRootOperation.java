package cz.blocksolver.backend.block.unary;

import cz.blocksolver.backend.block.IUnaryOperation;
import cz.blocksolver.backend.block.OperationResult;

/**
 * Trida reprezentujici operaci treti odmocninu
 * @author David Endrych (xendry02)
 * @author Marek Kukučka (xkukuc04)
 */
public class CubeRootOperation implements IUnaryOperation {

    /**
     * Instance operace
     */
    private static IUnaryOperation instance;

    private Double result;
    private OperationResult output;

    /**
     * Privatni konstruktor (singleton)
     */
    private CubeRootOperation(){
    }

    /**
     * Ziska instanci tridy (singleton)
     * @return instance
     */
    public static IUnaryOperation getInstance(){
        if(instance == null){
            instance = new CubeRootOperation();
        }
        return instance;
    }

    /**
     * Provedeni operace
     * @param input vstupni hodnota
     * @return vysledek operace
     */
    @Override
    public OperationResult executeOperation(Double input) {
        result = Math.cbrt(input);
        output = new OperationResult(result);
        return output;
    }
}
