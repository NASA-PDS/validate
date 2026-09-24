package gov.nasa.pds.validate.report;

import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.validate.status.Status;

public class StreamingJsonReport extends Report {
    private static final Logger LOG = Logger.getLogger(StreamingJsonReport.class.getName());
    private static final Gson gson = new Gson();
    private JsonObject currentProduct;
    private JsonArray currentMessages;

    @Override
    protected void begin(Block block) {
        if (block == Block.LABEL) {
            this.currentProduct = new JsonObject();
            this.currentMessages = new JsonArray();
        }
    }

    @Override
    protected void append(Status status, String lidvid, String target) {
        this.currentProduct.addProperty("label", target);
        this.currentProduct.addProperty("lidvid", lidvid);
        this.currentProduct.addProperty("status", status.getName());
    }

    @Override
    protected void append(ValidationProblem problem) {
        JsonObject msg = new JsonObject();
        msg.addProperty("severity", problem.getProblem().getSeverity().getName());
        msg.addProperty("type", problem.getProblem().getType().getKey());
        msg.addProperty("message", problem.getMessage());

        if (problem.getLineNumber() != -1) {
            msg.addProperty("line", problem.getLineNumber());
            if (problem.getColumnNumber() != -1) {
                msg.addProperty("column", problem.getColumnNumber());
            }
        }
        this.currentMessages.add(msg);
    }

    @Override
    protected void end(Block block) {
        if (block == Block.LABEL) {
            int fatals = 0;
            int errors = 0;
            int warnings = 0;
            int infos = 0;

            for (JsonElement element : this.currentMessages) {
                String severity = element.getAsJsonObject().get("severity").getAsString();
                if ("FATAL".equalsIgnoreCase(severity)) fatals++;
                else if ("ERROR".equalsIgnoreCase(severity)) errors++;
                else if ("WARNING".equalsIgnoreCase(severity)) warnings++;
                else if ("INFO".equalsIgnoreCase(severity)) infos++;
            }

            this.currentProduct.addProperty("fatal", fatals);
            this.currentProduct.addProperty("error", errors);
            this.currentProduct.addProperty("warning", warnings);
            this.currentProduct.addProperty("info", infos);

            this.currentProduct.add("messages", this.currentMessages);

            if (getWriter() != null) {
                getWriter().println(gson.toJson(this.currentProduct));
                getWriter().flush();
            } else {
                LOG.info(gson.toJson(this.currentProduct));
            }

            this.currentProduct = null;
            this.currentMessages = null;
        }
    }

    // --- Implement abstract overrides with explicit comments for SonarQube ---

    @Override
    protected void append(String title) {
        // Intentionally empty: Title blocks are excluded from line-delimited JSON log output
    }

    @Override
    protected void appendConfig(String label, String message, String value) {
        // Intentionally empty: Global tool setup configurations are excluded from streaming records
    }

    @Override
    protected void appendParam(String label, String message, String value) {
        // Intentionally empty: Run parameter notifications are excluded from streaming records
    }

    @Override
    protected void summarizeAddMessage(String msg, long count) {
        // Intentionally empty: Aggregate counts are dropped in favor of line-item logs
    }

    @Override
    protected void summarizeDepWarn(String msg) {
        // Intentionally empty: Deprecation warnings are handled in global stdout rather than streaming targets
    }

    @Override
    protected void summarizeRefs(int failed, int passed, int skipped, int total) {
        // Intentionally empty: Context references are skipped to isolate file validation metrics
    }

    @Override
    protected void summarizeTotals(int errors, int total, int warnings) {
        // Intentionally empty: Replaced by target summary record type handled in summarizeProds
    }

    @Override
    protected void summarizeProds(int failed, int passed, int skipped, int total) {
        JsonObject summary = new JsonObject();
        summary.addProperty("recordType", "summary");
        summary.addProperty("passed", passed);
        summary.addProperty("failed", failed);
        summary.addProperty("total", total);

        if (getWriter() != null) {
            getWriter().println(gson.toJson(summary));
            getWriter().flush();
        } else {
            LOG.info(gson.toJson(summary));
        }
    }
}