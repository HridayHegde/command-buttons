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

        // Get current time and calculate time to add to reach next morning (1000)
        long currentTime = client.world.getTimeOfDay() % 24000;
        long timeToAdd;
        
        if (currentTime < 1000) {
            // Current time is before morning, add time to reach morning
            timeToAdd = 1000 - currentTime;
        } else {
            // Current time is after morning, add time to reach next morning
            timeToAdd = 24000 - currentTime + 1000;
        }
        
        client.player.networkHandler.sendChatCommand("time add " + timeToAdd);
    }

    public static void executeSetTimeMidnight() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Get current time and calculate time to add to reach next midnight (18000)
        long currentTime = client.world.getTimeOfDay() % 24000;
        long timeToAdd;
        
        if (currentTime < 18000) {
            // Current time is before midnight, add time to reach midnight
            timeToAdd = 18000 - currentTime;
        } else {
            // Current time is after midnight, add time to reach next midnight
            timeToAdd = 24000 - currentTime + 18000;
        }
        
        client.player.networkHandler.sendChatCommand("time add " + timeToAdd);
    }

    public static void executeSetTimeAfternoon() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Get current time and calculate time to add to reach next afternoon (6000)
        long currentTime = client.world.getTimeOfDay() % 24000;
        long timeToAdd;
        
        if (currentTime < 6000) {
            // Current time is before afternoon, add time to reach afternoon
            timeToAdd = 6000 - currentTime;
        } else {
            // Current time is after afternoon, add time to reach next afternoon
            timeToAdd = 24000 - currentTime + 6000;
        }
        
        client.player.networkHandler.sendChatCommand("time add " + timeToAdd);
    }
}
