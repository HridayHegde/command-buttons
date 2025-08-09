package command.buttons;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class CommandButtonsClient implements ClientModInitializer {
	private static final int BUTTON_WIDTH = 70;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_SPACING = 5;
	private static final int START_X = 5;
	private static final int START_Y = 5;
	
	// Inventory tracking for button functionality
	private static boolean isInventoryOpen = false;
	private static InventoryScreen currentInventoryScreen = null;

	@Override
	public void onInitializeClient() {
		// Initialize the command buttons mod on the client side
		CommandButtons.LOGGER.info("Command Buttons mod client initialized - inventory buttons ready!");
		
		// Register screen event to add buttons when inventory screen opens
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			// Debug: Log all screen types to see what creative mode uses
			CommandButtons.LOGGER.info("CommandButtons: Screen opened: {} (class: {})", 
				screen.getTitle().getString(), screen.getClass().getSimpleName());
			
			if (screen instanceof InventoryScreen && !(screen instanceof CreativeInventoryScreen)) {
				// Only for survival/adventure inventory, not creative
				CommandButtons.LOGGER.info("CommandButtons: Survival InventoryScreen detected via ScreenEvents.AFTER_INIT!");
				CommandButtons.LOGGER.info("CommandButtons: Screen dimensions: {}x{}", scaledWidth, scaledHeight);
				isInventoryOpen = true;
				currentInventoryScreen = (InventoryScreen) screen;
				addButtonsToInventory((InventoryScreen) screen);
			} else if (screen instanceof CreativeInventoryScreen) {
				// For creative mode, we only track but DON'T create invisible buttons
				// The mixin handles everything for creative mode
				CommandButtons.LOGGER.info("CommandButtons: CreativeInventoryScreen detected - using mixin only (no invisible buttons)");
				CommandButtons.LOGGER.info("CommandButtons: Screen dimensions: {}x{}", scaledWidth, scaledHeight);
				isInventoryOpen = true;
				// Don't call addButtonsToCreativeInventory - this was causing the duplicates!
			}
		});
		
		// Register screen close event
		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			// Reset if opening a different screen
			if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeInventoryScreen) && isInventoryOpen) {
				CommandButtons.LOGGER.info("CommandButtons: Non-inventory screen opened, closing inventory tracking");
				isInventoryOpen = false;
				currentInventoryScreen = null;
			}
		});
		
		// Register custom rendering for survival mode only
		// Creative mode uses CreativeInventoryScreenMixin
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof InventoryScreen && !(screen instanceof CreativeInventoryScreen)) {
				// Add a render callback specifically for survival inventory screen
				ScreenEvents.afterRender(screen).register((screen2, drawContext, mouseX, mouseY, tickDelta) -> {
					if (screen2 == currentInventoryScreen && isInventoryOpen) {
						// Only render in survival/adventure mode, not creative
						if (client.player != null) {
							net.minecraft.world.GameMode gameMode = client.interactionManager.getCurrentGameMode();
							if (gameMode != net.minecraft.world.GameMode.CREATIVE) {
								renderSurvivalButtons(drawContext);
							}
						}
					}
				});
			}
		});
		
		CommandButtons.LOGGER.info("CommandButtons: Registered screen event handler!");
	}
	
	private void addButtonsToInventory(InventoryScreen screen) {
		try {
			CommandButtons.LOGGER.info("CommandButtons: Starting to add buttons to survival inventory screen...");
			
			// Create main buttons
			addButtonToScreen(screen, "GameMode", START_X, START_Y, command.buttons.util.CommandExecutor::executeGameModeToggle);
			addButtonToScreen(screen, "Rain", START_X, START_Y + (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeRainToggle);
			addButtonToScreen(screen, "Heal", START_X, START_Y + 2 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeHeal);
			addButtonToScreen(screen, "Morning", START_X, START_Y + 3 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeSetTimeMorning);
			addButtonToScreen(screen, "Midnight", START_X, START_Y + 4 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeSetTimeMidnight);
			addButtonToScreen(screen, "Afternoon", START_X, START_Y + 5 * (BUTTON_HEIGHT + BUTTON_SPACING), command.buttons.util.CommandExecutor::executeSetTimeAfternoon);
			
			CommandButtons.LOGGER.info("CommandButtons: Successfully added all buttons to survival inventory!");
		} catch (Exception e) {
			CommandButtons.LOGGER.error("CommandButtons: Error adding buttons to survival inventory", e);
		}
	}
	
	// Note: Creative inventory buttons are handled entirely by CreativeInventoryScreenMixin
	// No invisible buttons are created for creative mode to prevent duplicates
	
	private void addButtonToScreen(Object screenObj, String text, int x, int y, Runnable action) {
		try {
			ButtonWidget button = ButtonWidget.builder(Text.literal(text), (btn) -> {
				CommandButtons.LOGGER.info("CommandButtons: Button '{}' clicked!", text);
				action.run();
			})
			.dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
			.build();
			
			// Try multiple approaches to add the button
			boolean success = false;
			
			// Approach 1: Use Fabric's Screens API (cast to Screen first)
			try {
				if (screenObj instanceof net.minecraft.client.gui.screen.Screen) {
					net.minecraft.client.gui.screen.Screen screen = (net.minecraft.client.gui.screen.Screen) screenObj;
					Screens.getButtons(screen).add(button);
					CommandButtons.LOGGER.info("CommandButtons: Added button '{}' at ({}, {}) using Screens API", text, x, y);
					success = true;
				}
			} catch (Exception e1) {
				CommandButtons.LOGGER.warn("CommandButtons: Screens API failed for button '{}': {}", text, e1.getMessage());
			}
			
			// Approach 2: Try to use ScreenAccessor if Screens API failed
			if (!success) {
				try {
					if (screenObj instanceof command.buttons.mixin.client.ScreenAccessor) {
						((command.buttons.mixin.client.ScreenAccessor) screenObj).invokeAddDrawableChild(button);
						CommandButtons.LOGGER.info("CommandButtons: Added button '{}' at ({}, {}) using ScreenAccessor", text, x, y);
						success = true;
					}
				} catch (Exception e2) {
					CommandButtons.LOGGER.warn("CommandButtons: ScreenAccessor failed for button '{}': {}", text, e2.getMessage());
				}
			}
			
			if (!success) {
				CommandButtons.LOGGER.error("CommandButtons: Failed to add button '{}' with all methods!", text);
			}
			
		} catch (Exception e) {
			CommandButtons.LOGGER.error("CommandButtons: Error creating button '{}'", text, e);
		}
	}
	
	private static void renderSurvivalButtons(DrawContext drawContext) {
		MinecraftClient client = MinecraftClient.getInstance();
		
		try {
			// Draw custom buttons directly on the screen
			int x = START_X;
			int y = START_Y;
			
			// Button backgrounds and text - only the main 6 buttons
			String[] buttonTexts = {"GameMode", "Rain", "Heal", "Morning", "Midnight", "Afternoon"};
			
			for (int i = 0; i < buttonTexts.length; i++) {
				int buttonX = x;
				int buttonY = y + i * (BUTTON_HEIGHT + BUTTON_SPACING);
				
				// Draw button background with Minecraft-style appearance
				// Dark border
				drawContext.fill(buttonX, buttonY, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, 0xFF000000);
				// Main button background
				drawContext.fill(buttonX + 1, buttonY + 1, buttonX + BUTTON_WIDTH - 1, buttonY + BUTTON_HEIGHT - 1, 0xFFC0C0C0);
				// Top/left highlight (3D effect)
				drawContext.fill(buttonX + 1, buttonY + 1, buttonX + BUTTON_WIDTH - 1, buttonY + 2, 0xFFFFFFFF);
				drawContext.fill(buttonX + 1, buttonY + 1, buttonX + 2, buttonY + BUTTON_HEIGHT - 1, 0xFFFFFFFF);
				// Bottom/right shadow (3D effect)  
				drawContext.fill(buttonX + 1, buttonY + BUTTON_HEIGHT - 2, buttonX + BUTTON_WIDTH - 1, buttonY + BUTTON_HEIGHT - 1, 0xFF808080);
				drawContext.fill(buttonX + BUTTON_WIDTH - 2, buttonY + 1, buttonX + BUTTON_WIDTH - 1, buttonY + BUTTON_HEIGHT - 1, 0xFF808080);
				
				// Draw button text (centered)
				int textWidth = client.textRenderer.getWidth(buttonTexts[i]);
				int textX = buttonX + (BUTTON_WIDTH - textWidth) / 2;
				int textY = buttonY + (BUTTON_HEIGHT - 8) / 2;
				drawContext.drawText(client.textRenderer, buttonTexts[i], textX, textY, 0x000000, false);
			}
			
		} catch (Exception e) {
			CommandButtons.LOGGER.error("CommandButtons: Error rendering survival buttons", e);
		}
	}
}