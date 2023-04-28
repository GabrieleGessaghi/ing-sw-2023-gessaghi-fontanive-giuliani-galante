package server.model;

import server.controller.utilities.ConfigLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationsTest {

    @Test
    public void loadConfigurationTest() {
        assertNotEquals(ConfigLoader.BOARD_SIZE, 9);
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        assertEquals(ConfigLoader.BOARD_SIZE, 9);
    }
}