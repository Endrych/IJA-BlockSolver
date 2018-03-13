package cz.blocksolver.backend.block;

import java.util.ArrayList;
import java.util.Collection;

public class ArithmeticBlock extends Block  {

    Collection<IArithmeticOperation> operations;
    IArithmeticOperation operation;
    Double result;

    public ArithmeticBlock(String name, Integer x, Integer y, Integer width, Integer height, IArithmeticOperation operation) {
        super(name, x, y, width, height);
        this.operation = operation;
        initializeOperations();
    }

    private void initializeOperations() {
    }

    @Override
    public void executeBlock(){
//        this.result = operation.executeOperation()
    }




}
