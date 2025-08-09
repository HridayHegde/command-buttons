package command.buttons.mixin.client;

import command.buttons.client.InventoryButtonRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void addCommandButtons(CallbackInfo ci) {
        command.buttons.CommandButtons.LOGGER.info("CommandButtons: Mixin init() called - delegating to InventoryButtonRenderer");
        InventoryScreen screen = (InventoryScreen) (Object) this;
        InventoryButtonRenderer.onInventoryScreenInit(screen);
    }
}
