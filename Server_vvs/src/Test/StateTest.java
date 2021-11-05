package Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.Server;

public class StateTest {
    Server server =new Server();

    @Test
    public void test_set_getStatus1() {
        server.setStateServer(1);
        assertEquals(1,server.getStateServer());
    }

    @Test
    public void test_set_getStatus2() {
        server.setStateServer(2);
        assertEquals(2,server.getStateServer());
    }

    @Test
    public void test_set_getStatus3() {
        server.setStateServer(3);
        assertEquals(3,server.getStateServer());
    }

}
