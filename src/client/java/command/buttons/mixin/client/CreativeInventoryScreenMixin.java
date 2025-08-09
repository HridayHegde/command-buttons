package command.buttons.mixin.client;

import command.buttons.CommandButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    
    @Inject(method = "render", at = @At("TAIL"))
    private void renderCommandButtons(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Only render if in creative mode and this is the current screen
            if (client.player != null && client.currentScreen == (Object) this) {
                net.minecraft.world.GameMode gameMode = client.interactionManager.getCurrentGameMode();
                
                if (gameMode == net.minecraft.world.GameMode.CREATIVE) {
                    CommandButtons.LOGGER.info("CommandButtons: Rendering creative buttons via render method");
                    renderButtons(context);
                    renderTooltips(context, mouseX, mouseY);
                }
            }
        } catch (Exception e) {
            CommandButtons.LOGGER.error("CommandButtons: Error in creative render mixin", e);
        }
    }
    
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Only handle clicks in creative mode
            if (client.player != null && client.interactionManager.getCurrentGameMode() == net.minecraft.world.GameMode.CREATIVE) {
                if (handleButtonClick((int)mouseX, (int)mouseY)) {
                    cir.setReturnValue(true); // Cancel further click processing
                }
            }
        } catch (Exception e) {
            CommandButtons.LOGGER.error("CommandButtons: Error handling creative mouse click", e);
        }
    }
    
    private boolean handleButtonClick(int mouseX, int mouseY) {
        // Button configuration (horizontal layout)
        int BUTTON_SIZE = 20; // Square buttons
        int BUTTON_SPACING = 2;
        int START_X = 5;
        int START_Y = 5;
        
        String[] buttonTexts = {"GameMode", "Rain", "Heal", "Morning", "Midnight", "Afternoon"};
        
        for (int i = 0; i < buttonTexts.length; i++) {
            int buttonX = START_X + i * (BUTTON_SIZE + BUTTON_SPACING);
            int buttonY = START_Y;
            
            // Check if click is within this button's bounds
            if (mouseX >= buttonX && mouseX <= buttonX + BUTTON_SIZE && 
                mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                
                CommandButtons.LOGGER.info("CommandButtons: Creative button '{}' clicked!", buttonTexts[i]);
                executeCreativeCommand(buttonTexts[i]);
                return true; // Click was handled
            }
        }
        return false; // Click was not on any button
    }
    
    private void executeCreativeCommand(String buttonText) {
        switch (buttonText) {
            case "GameMode":
                command.buttons.util.CommandExecutor.executeGameModeToggle();
                break;
            case "Rain":
                command.buttons.util.CommandExecutor.executeRainToggle();
                break;
            case "Heal":
                command.buttons.util.CommandExecutor.executeHeal();
                break;
            case "Morning":
                command.buttons.util.CommandExecutor.executeSetTimeMorning();
                break;
            case "Midnight":
                command.buttons.util.CommandExecutor.executeSetTimeMidnight();
                break;
            case "Afternoon":
                command.buttons.util.CommandExecutor.executeSetTimeAfternoon();
                break;
        }
    }
    
    private void renderButtons(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Button configuration (horizontal layout)
        int BUTTON_SIZE = 20; // Square buttons
        int BUTTON_SPACING = 2;
        int START_X = 5;
        int START_Y = 5;
        
        String[] buttonNames = {"gamemode", "rain", "heal", "morning", "midnight", "afternoon"};
        
        for (int i = 0; i < buttonNames.length; i++) {
            int buttonX = START_X + i * (BUTTON_SIZE + BUTTON_SPACING);
            int buttonY = START_Y;
            
            // Draw button background with Minecraft-style appearance (square)
            // Dark border
            drawContext.fill(buttonX, buttonY, buttonX + BUTTON_SIZE, buttonY + BUTTON_SIZE, 0xFF000000);
            // Main button background
            drawContext.fill(buttonX + 1, buttonY + 1, buttonX + BUTTON_SIZE - 1, buttonY + BUTTON_SIZE - 1, 0xFFC0C0C0);
            // Top/left highlight (3D effect)
            drawContext.fill(buttonX + 1, buttonY + 1, buttonX + BUTTON_SIZE - 1, buttonY + 2, 0xFFFFFFFF);
            drawContext.fill(buttonX + 1, buttonY + 1, buttonX + 2, buttonY + BUTTON_SIZE - 1, 0xFFFFFFFF);
            // Bottom/right shadow (3D effect)  
            drawContext.fill(buttonX + 1, buttonY + BUTTON_SIZE - 2, buttonX + BUTTON_SIZE - 1, buttonY + BUTTON_SIZE - 1, 0xFF808080);
            drawContext.fill(buttonX + BUTTON_SIZE - 2, buttonY + 1, buttonX + BUTTON_SIZE - 1, buttonY + BUTTON_SIZE - 1, 0xFF808080);
            
            // Render icon (assume it always exists)
            renderButtonIcon(drawContext, buttonNames[i], buttonX, buttonY);
        }
    }
    
    private void renderButtonIcon(DrawContext drawContext, String iconName, int x, int y) {
        try {
            net.minecraft.util.Identifier iconId = new net.minecraft.util.Identifier("commandbuttons", "textures/gui/buttons/" + iconName + ".png");
            
            // Render the icon centered in the button
            int iconSize = 16; // 16x16 pixel icons
            int iconX = x + (20 - iconSize) / 2; // Center in 20x20 button
            int iconY = y + (20 - iconSize) / 2;
            
            drawContext.drawTexture(iconId, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        } catch (Exception e) {
            CommandButtons.LOGGER.warn("CommandButtons: Failed to render creative icon for {}: {}", iconName, e.getMessage());
        }
    }
    
    private void renderTooltips(DrawContext drawContext, int mouseX, int mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Button configuration
        int BUTTON_SIZE = 20;
        int BUTTON_SPACING = 2;
        int START_X = 5;
        int START_Y = 5;
        
        // Button data
        String[] buttonNames = {"gamemode", "rain", "heal", "morning", "midnight", "afternoon"};
        String[] tooltips = {
            "Toggle Game Mode",
            "Toggle Weather", 
            "Heal to Full Health",
            "Set Time to Morning",
            "Set Time to Midnight",
            "Set Time to Afternoon"
        };
        
        // Check if mouse is hovering over any button
        for (int i = 0; i < buttonNames.length; i++) {
            int buttonX = START_X + i * (BUTTON_SIZE + BUTTON_SPACING);
            int buttonY = START_Y;
            
            // Check if mouse is within button bounds
            if (mouseX >= buttonX && mouseX <= buttonX + BUTTON_SIZE && 
                mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                
                // Render tooltip
                drawContext.drawTooltip(client.textRenderer, net.minecraft.text.Text.literal(tooltips[i]), mouseX, mouseY);
                break; // Only show one tooltip at a time
            }
        }
    }
}
