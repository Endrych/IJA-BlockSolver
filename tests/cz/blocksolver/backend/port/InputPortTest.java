package cz.blocksolver.backend.port;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputPortTest {

    private InputPort inputPort;

    @Before
    public void Init(){
        inputPort = new InputPort(PortType.NUMBER, 0.0, 1);
    }

    @Test
    public void InputHashTest(){
        InputPort inputPort1 = new InputPort(PortType.NUMBER, 0.0, 1);
        inputPort1.setOutputPort(new OutputPort(PortType.NUMBER, 0.0));
        inputPort.setOutputPort(new OutputPort(PortType.NUMBER, 0.0));
        assertEquals(inputPort1.hashCode(),inputPort.hashCode());
        inputPort.setName("Blbost");
        assertNotEquals(inputPort1.hashCode(),inputPort.hashCode());

    }

    @Test
    public void InputTwoEqualInputsPort(){
        InputPort inputPort1 = new InputPort(PortType.NUMBER, 0.0, 1);
        inputPort1.setOutputPort(new OutputPort(PortType.NUMBER, 0.0));
        inputPort.setOutputPort(new OutputPort(PortType.NUMBER, 0.0));
        assertEquals(inputPort1.equals(inputPort),true);
        inputPort.setName("Blbost");
        assertEquals(inputPort1.equals(inputPort),false);
    }
    @Test
    public void InputPortToString(){
        assertEquals("Vstupní port 1",inputPort.toString());
    }
}