package pl.jordanmruczynski.mctiktok;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.jordanmruczynski.mctiktok.commands.TiktokCommand;
import pl.jordanmruczynski.mctiktok.configuration.GiftCodesConfig;
import pl.jordanmruczynski.mctiktok.giftactions.GiftActionListeners;
import pl.jordanmruczynski.mctiktok.placeholderapi.PlaceholderInject;
import pl.jordanmruczynski.mctiktok.util.FileCopy;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * author jordanmruczynski
 * date 09.2022
 */

public final class McTiktok extends JavaPlugin {

    private static ScheduledExecutorService executorServiceScheduled = Executors.newScheduledThreadPool(1);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private UserRepository userRepository;
    private GiftCodesConfig giftCodesConfig;
    private File giftsConfig, chestItemsConfig, settingsConfig;
    private final String gifstConfigFileName = "gifts.json";
    private final String settingsConfigFileName = "settings.json";
    private final String chestItemsConfigFileName = "chestItems.json";

    @Override
    public void onEnable() {
        giftCodesConfig = new GiftCodesConfig();
        setupFiles();

        try {
            giftCodesConfig.loadCodesFromJson(getDataFolder() + "/" + gifstConfigFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        userRepository = new UserRepository(executorServiceScheduled, Bukkit.getScheduler(), giftCodesConfig);
        userRepository.start();

        getCommand("mctiktok").setExecutor(new TiktokCommand(userRepository));

        Bukkit.getPluginManager().registerEvents(userRepository, this);
        Bukkit.getPluginManager().registerEvents(new GiftActionListeners(), this);

        new PlaceholderInject(userRepository).register();
    }

    @Override
    public void onDisable() {
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static ScheduledExecutorService getExecutorServiceScheduled() {
        return executorServiceScheduled;
    }

    public static McTiktok getMcTiktok() {
        return McTiktok.getPlugin(McTiktok.class);
    }

    private void setupFiles() {
        File dataFolder = getDataFolder();
        giftsConfig = new File(dataFolder, gifstConfigFileName);
        chestItemsConfig = new File(dataFolder, chestItemsConfigFileName);
        settingsConfig = new File(dataFolder, settingsConfigFileName);

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        FileCopy.createFileFromResource(gifstConfigFileName, giftsConfig, this);
        FileCopy.createFileFromResource(chestItemsConfigFileName, chestItemsConfig, this);
        FileCopy.createFileFromResource(settingsConfigFileName, settingsConfig, this);
    }

    public String getServerAddress() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(settingsConfig);
            return jsonNode.get("serverAddress").asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServerPort() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(settingsConfig);
            return jsonNode.get("serverPort").asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
