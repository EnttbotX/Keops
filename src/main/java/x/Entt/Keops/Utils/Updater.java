package x.Entt.Keops.Utils;

import x.Entt.Keops.K;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Updater {

    private K plugin;
    private int resourceId;

    public Updater(K plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public String getLatestVersion() throws IOException {
        URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        InputStream inputStream = url.openConnection().getInputStream();
        Scanner scanner = new Scanner(inputStream);
        if (scanner.hasNext()) {
            return scanner.next();
        }
        return null;
    }

    public boolean isUpdateAvailable() throws IOException {
        String currentVersion = plugin.getDescription().getVersion();
        String latestVersion = getLatestVersion();
        return latestVersion != null && !latestVersion.equals(currentVersion);
    }
}