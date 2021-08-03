package com.treasuredata.fileclient;

import com.treasuredata.fileclient.components.IClientFileComponent;
import com.treasuredata.fileclient.console.ApplicationConsole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class ApplicationConsoleTests {

    @Mock
    IClientFileComponent clientFileComponent;

    @InjectMocks
    ApplicationConsole applicationConsole;

    @Test
    void testRunApplicationStoppedByConnectionFailed() {
        when(clientFileComponent.connect()).thenReturn(false);
        int retCode = applicationConsole.run(new String[] {});
        assertEquals(1, retCode);
    }

    @Test
    void testRunApplication() {
        String responseList = "NghiaHo 2021-06-03 10:30 2021-06-03 15:22 readme.txt";

        when(clientFileComponent.connect()).thenReturn(true);
        when(clientFileComponent.sendAction(eq("index"))).thenReturn(responseList);
        when(clientFileComponent.sendAction(eq("get readme.txt"))).thenReturn("Hi I am readme content !!!");

        System.setIn(new ByteArrayInputStream((
                "index" + System.lineSeparator() +
                "get readme.txt" + System.lineSeparator() +
                "exit").getBytes()));
        int retCode = applicationConsole.run(new String[] {});

        assertEquals(0, retCode);
    }
}
