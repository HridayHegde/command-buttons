package command.buttons.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.GameMode;

public class CommandExecutor {
    private static boolean rainToggle = false;

    public static void executeGameModeToggle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        GameMode currentMode = client.interactionManager.getCurrentGameMode();
        String command = currentMode == GameMode.CREATIVE ? "gamemode survival" : "gamemode creative";
        client.player.networkHandler.sendChatCommand(command);
    }

    public static void executeRainToggle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String command = rainToggle ? "weather clear" : "weather rain";
        rainToggle = !rainToggle;
        client.player.networkHandler.sendChatCommand(command);
    }

    public static void executeHeal() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Give instant health effect
        client.player.networkHandler.sendChatCommand("effect give @s minecraft:instant_health 1 10");
        // Also restore food level
        client.player.networkHandler.sendChatCommand("effect give @s minecraft:saturation 1 10");
    }

    public static void executeSetTimeMorning() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        client.player.networkHandler.sendChatCommand("time set 1000");
    }

    public static void executeSetTimeMidnight() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        client.player.networkHandler.sendChatCommand("time set 18000");
    }

    public static void executeSetTimeAfternoon() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        client.player.networkHandler.sendChatCommand("time set 6000");
    }
}
