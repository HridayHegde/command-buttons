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
        // Button configuration
        int BUTTON_WIDTH = 70;
        int BUTTON_HEIGHT = 20;
        int BUTTON_SPACING = 5;
        int START_X = 5;
        int START_Y = 5;
        
        String[] buttonTexts = {"GameMode", "Rain", "Heal", "Morning", "Midnight", "Afternoon"};
        
        for (int i = 0; i < buttonTexts.length; i++) {
            int buttonX = START_X;
            int buttonY = START_Y + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            
            // Check if click is within this button's bounds
            if (mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH && 
                mouseY >= buttonY && mouseY <= buttonY + BUTTON_HEIGHT) {
                
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
        
        // Button configuration
        int BUTTON_WIDTH = 70;
        int BUTTON_HEIGHT = 20;
        int BUTTON_SPACING = 5;
        int START_X = 5;
        int START_Y = 5;
        
        String[] buttonTexts = {"GameMode", "Rain", "Heal", "Morning", "Midnight", "Afternoon"};
        
        for (int i = 0; i < buttonTexts.length; i++) {
            int buttonX = START_X;
            int buttonY = START_Y + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            
            // Draw button background with Minecraft-style appearance (same as survival)
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
    }
}
