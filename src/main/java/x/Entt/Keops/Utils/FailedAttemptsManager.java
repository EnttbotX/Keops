package x.Entt.Keops.Utils;

import java.util.HashMap;
import java.util.Map;

public class FailedAttemptsManager {
    private final Map<String, Integer> attempts = new HashMap<>();

    public int registerFailedAttempt(String user, int maxAttempts) {
        int count = attempts.getOrDefault(user, 0) + 1;
        attempts.put(user, count);
        return maxAttempts - count;
    }

    public void resetAttempts(String user) {
        attempts.remove(user);
    }

    public int getCurrentAttempts(String user) {
        return attempts.getOrDefault(user, 0);
    }
}