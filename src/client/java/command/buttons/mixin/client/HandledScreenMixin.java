package command.buttons.mixin.client;

import command.buttons.client.InventoryButtonRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void onHandledScreenInit(CallbackInfo ci) {
        // Only add buttons if this is an inventory screen
        if ((Object) this instanceof InventoryScreen) {
            command.buttons.CommandButtons.LOGGER.info("CommandButtons: HandledScreen init() called for InventoryScreen - adding buttons");
            InventoryScreen screen = (InventoryScreen) (Object) this;
            InventoryButtonRenderer.onInventoryScreenInit(screen);
        }
    }
}
