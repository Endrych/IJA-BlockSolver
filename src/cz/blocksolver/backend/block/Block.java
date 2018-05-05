package cz.blocksolver.backend.block;


import cz.blocksolver.backend.port.InputPort;
import cz.blocksolver.backend.port.OutputPort;
import cz.blocksolver.backend.port.PortType;

public abstract class Block implements IBlock {
    protected String name;
    protected Integer x, y, width, height;
    protected OutputPort outputPort;
    protected BlockType type;
    protected Boolean executed;


    public Block(String name, Integer x, Integer y, Integer width, Integer height, BlockType type) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.outputPort = new OutputPort(PortType.NUMBER, 0.0);
        this.type = type;
        this.executed = false;
    }

    public void setOutputPort(OutputPort outputPort) {
        this.outputPort = outputPort;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    public BlockType getType() {
        return type;
    }

    public Boolean executeBlock(Double one, Double two) {
        return null;
    }

    public Boolean executeBlock(Double one) {
        return null;
    }

    @Override
    public abstract InputPort getInputPort(Integer index);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public OutputPort getOutputPort() {
        return outputPort;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
