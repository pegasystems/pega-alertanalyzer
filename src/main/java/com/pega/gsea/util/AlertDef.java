package com.pega.gsea.util;

import java.util.HashMap;

public class AlertDef {

    private AlertDef(){}

    static HashMap<String, String> alertDefMap = new HashMap<String, String>();

    static {
        alertDefMap.put("PEGA0048", "Page copy time and waiting time are more than the loader activity execution time for the data page");
        alertDefMap.put("PEGA0046", "Queue entry not yet started by the load activity");
        alertDefMap.put("PEGA0043", "Queue waiting time is more than x for x times");
        alertDefMap.put("PEGA0036", "PegaRULES engine intentionally shut down");
        alertDefMap.put("PEGA0044", "Reached threshold limit for message ID: PEGA00XX, will send again after [Date]");
        alertDefMap.put("PEGA0050", "Lightweight list has been copied n times.");
        alertDefMap.put("PEGA0042", "Packaging of database query exceeded time threshold (V6)");
        alertDefMap.put("PEGA0041", "Work object written to the pr_other table (V6)");
        alertDefMap.put("PEGA0040", "BLOB size written to the database exceeds a threshold (V6)");
        alertDefMap.put("PEGA0039", "The size of a BLOB column read exceeds a threshold (V6)");
        alertDefMap.put("PEGA0038", "The wait time for rule cache access exceeds a threshold (V6)");
        alertDefMap.put("PEGA0037", "Rule assembly time exceeded threshold (V6)");
        alertDefMap.put("PEGA0035", "A Page List property has a of elements that exceed a threshold");
        alertDefMap.put("PEGA0034", "The number of declare indexes from an interaction exceeds");
        alertDefMap.put("PEGA0033", "Database query length has exceeded a specified threshold");
        alertDefMap.put("PEGA0032", "Rule change invalidated entries in the Rule Assembly cache");
        alertDefMap.put("PEGA0031", "Generated stream overwritten and not sent to client");
        alertDefMap.put("PEGA0030", "The number of requestors for the system exceeds limit");
        alertDefMap.put("PEGA0029", "HTML stream size exceeds limit ");
        alertDefMap.put("PEGA0028", "Garbage collection cannot reclaim memory from memory pools ");
        alertDefMap.put("PEGA0027", "Number of rows exceeds database list limit ");
        alertDefMap.put("PEGA0026", "Time to connect to database exceeds limit");
        alertDefMap.put("PEGA0025", "Performing list with blob due to non-exposed columns");
        alertDefMap.put("PEGA0024", "Time to load declarative network time exceeds limit");
        alertDefMap.put("PEGA0023", "Rule cache has been enabled");
        alertDefMap.put("PEGA0022", "Rule cache has been disabled");
        alertDefMap.put("PEGA0020", "Total connect interaction time exceeds limit");
        alertDefMap.put("PEGA0019", "Long-running requestor detected");
        alertDefMap.put("PEGA0018", "Number of PRThreads exceeds limit");
        alertDefMap.put("PEGA0017", "Cache exceeds limit");
        alertDefMap.put("PEGA0016", "Cache reduced to target size");
        alertDefMap.put("PEGA0015", "Data parsing time exceeds limit");
        alertDefMap.put("PEGA0014", "Inbound mapping time exceeds limit");
        alertDefMap.put("PEGA0013", "Activity interaction time exceeds limit");
        alertDefMap.put("PEGA0012", "Outbound mapping time exceeds limit");
        alertDefMap.put("PEGA0011", "Total request time exceeds limit");
        alertDefMap.put("PEGA0010", "Agent Processing Disabled");
        alertDefMap.put("PEGA0009", "PegaRULES engine failed to start.");
        alertDefMap.put("PEGA0008", "PegaRULES engine started.");
        alertDefMap.put("PEGA0007", "Database operation time exceeds limit");
        alertDefMap.put("PEGA0006", "Update operation time exceeds limit");
        alertDefMap.put("PEGA0005", "Query time exceeds limit");
        alertDefMap.put("PEGA0004", "Quantity of data received by database query exceeds limit");
        alertDefMap.put("PEGA0003", "Rollback operation time exceeds limit");
        alertDefMap.put("PEGA0002", "Commit operation time exceeds limit");
        alertDefMap.put("PEGA0001", "HTTP interaction time exceeds limit");

    }

    public static String getAlertLabel(String id) {
        return alertDefMap.get(id);
    }

}
