package command.buttons.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class InventoryButtonRenderer {
    private static final List<ButtonWidget> BUTTONS = new ArrayList<>();
    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 16;
    private static final int BUTTON_SPACING = 3;
    private static final int START_X = 8;
    private static final int START_Y = 8;
    
    private static boolean buttonsInitialized = false;

    public static void onInventoryScreenInit(InventoryScreen screen) {
        command.buttons.CommandButtons.LOGGER.info("CommandButtons: InventoryButtonRenderer.onInventoryScreenInit called!");
        
        BUTTONS.clear();
        buttonsInitialized = false;
        
        try {
            // Create buttons
            addButton(screen, "GameMode", START_X, START_Y, command.buttons.util.CommandExecutor::executeGameModeToggle);
            addButton(screen, "Rain", START_X, START_Y + (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeRainToggle);
            addButton(screen, "Heal", START_X, START_Y + 2 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeHeal);
            addButton(screen, "Morning", START_X, START_Y + 3 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeSetTimeMorning);
            addButton(screen, "Midnight", START_X, START_Y + 4 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeSetTimeMidnight);
            addButton(screen, "Afternoon", START_X, START_Y + 5 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeSetTimeAfternoon);
            
            buttonsInitialized = true;
            command.buttons.CommandButtons.LOGGER.info("CommandButtons: Successfully created {} buttons", BUTTONS.size());
        } catch (Exception e) {
            command.buttons.CommandButtons.LOGGER.error("CommandButtons: Error creating buttons", e);
        }
    }

    private static void addButton(InventoryScreen screen, String text, int x, int y, Runnable action) {
        try {
            ButtonWidget button = ButtonWidget.builder(Text.literal(text), (btn) -> {
                command.buttons.CommandButtons.LOGGER.info("CommandButtons: Button '{}' clicked!", text);
                action.run();
            })
            .dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
            .build();
            
            BUTTONS.add(button);
            
            // Use the ScreenAccessor to add the button
            if (screen instanceof command.buttons.mixin.client.ScreenAccessor) {
                ((command.buttons.mixin.client.ScreenAccessor) screen).invokeAddDrawableChild(button);
                command.buttons.CommandButtons.LOGGER.info("CommandButtons: Added button '{}' at ({}, {}) using ScreenAccessor", text, x, y);
            } else {
                command.buttons.CommandButtons.LOGGER.error("CommandButtons: Screen is not instance of ScreenAccessor!");
            }
        } catch (Exception e) {
            command.buttons.CommandButtons.LOGGER.error("CommandButtons: Error adding button '{}'", text, e);
        }
    }

    public static void onRender(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!buttonsInitialized || BUTTONS.isEmpty()) {
            return;
        }
        
        // Additional rendering if needed - buttons should auto-render as they're added to the screen
        // This is here for debugging purposes
        command.buttons.CommandButtons.LOGGER.debug("CommandButtons: Rendering {} buttons", BUTTONS.size());
    }
}
