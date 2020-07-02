package fr.openent.pmb.bean;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;

public class Report {
    private long startTime;
    private long duration;
    private final HashMap<String, Object> structureImportCount = new HashMap<>();

    public Report start() {
        startTime = System.currentTimeMillis();
        return this;
    }

    public Report end() {
        duration = System.currentTimeMillis() - startTime;
        return this;
    }

    public void incrementStructureCount(String uai, Integer value) {
        if (!structureImportCount.containsKey(uai)) {
            structureImportCount.put(uai, 0);
        }

        structureImportCount.put(uai, ((int) structureImportCount.get(uai)) + value);
    }

    public JsonObject generate() {
        return new JsonObject()
                .put("DURATION", duration)
                .put("STRUCTURE_IMPORT_COUNT", new JsonObject(structureImportCount));
    }
}
