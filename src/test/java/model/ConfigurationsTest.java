package model;

import controller.Configurations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationsTest {

    @Test
    public void loadConfigurationTest() {
        assertNotEquals(Configurations.BOARD_SIZE, 9);
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        assertEquals(Configurations.BOARD_SIZE, 9);
    }
}