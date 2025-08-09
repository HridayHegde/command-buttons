package command.buttons.mixin.client;

import command.buttons.CommandButtons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenRenderMixin {
    
    @Inject(method = "drawForeground", at = @At("TAIL"), require = 0)
    private void renderCommandButtonsForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // Only render if NOT in creative mode and screen is regular InventoryScreen
            if (client.player != null && client.currentScreen == (Object) this) {
                GameMode gameMode = client.interactionManager.getCurrentGameMode();
                
                // Only render for survival/adventure mode, not creative
                if (gameMode != GameMode.CREATIVE && !(client.currentScreen instanceof CreativeInventoryScreen)) {
                    renderButtons(context);
                }
            }
        } catch (Exception e) {
            CommandButtons.LOGGER.error("CommandButtons: Error in inventory foreground mixin", e);
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
    }
}
