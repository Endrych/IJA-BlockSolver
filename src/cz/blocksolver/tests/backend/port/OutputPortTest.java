package cz.blocksolver.tests.backend.port;

import cz.blocksolver.backend.port.InputPort;
import cz.blocksolver.backend.port.OutputPort;
import cz.blocksolver.backend.port.PortType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class OutputPortTest {

    private OutputPort outputPort;

    @Before
    public void init(){
        outputPort = new OutputPort(PortType.NUMBER, 0.0);
    }

    @Test
    public void outputHashTest(){
        OutputPort outputPort1 = new OutputPort(PortType.NUMBER, 0.0);
        outputPort1.setInputPort(new InputPort(PortType.NUMBER, 0.0, 1));
        outputPort.setInputPort(new InputPort(PortType.NUMBER, 0.0, 1));
        assertEquals(outputPort.hashCode(),outputPort1.hashCode());
        outputPort.setName("Blbost");
        assertNotEquals(outputPort.hashCode(),outputPort1.hashCode());
    }

    @Test
    public void twoEqualsOutputPorts(){
        OutputPort inputPort1 = new OutputPort(PortType.NUMBER, 0.0);
        inputPort1.setInputPort(new InputPort(PortType.NUMBER, 0.0, 1));
        outputPort.setInputPort(new InputPort(PortType.NUMBER, 0.0, 1));
        assertEquals(inputPort1.equals(outputPort),true);
        outputPort.setName("Blbost");
        assertEquals(inputPort1.equals(outputPort),false);
    }
    @Test
    public void outputPortToString(){
        assertEquals("Výstupní port",outputPort.toString());
    }
}