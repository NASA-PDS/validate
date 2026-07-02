package gov.nasa.pds.validate.report;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.validate.status.Status;

public class StreamingJsonReport extends Report {
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
            // Calculate error/warning counts from the collected messages
            int fatals = 0, errors = 0, warnings = 0, infos = 0;

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

            // Write to the configured output stream (file or console)
            if (getWriter() != null) {
                getWriter().println(gson.toJson(this.currentProduct));
                getWriter().flush();
            } else {
                System.out.println(gson.toJson(this.currentProduct));
            }

            // Clear memory
            this.currentProduct = null;
            this.currentMessages = null;
        }
    }

    // --- Implement remaining abstract methods as empty ---

    @Override protected void append(String title) {}
    @Override protected void appendConfig(String label, String message, String value) {}
    @Override protected void appendParam(String label, String message, String value) {}
    @Override protected void summarizeAddMessage(String msg, long count) {}
    @Override protected void summarizeDepWarn(String msg) {}
    @Override protected void summarizeRefs(int failed, int passed, int skipped, int total) {}
    @Override protected void summarizeTotals(int errors, int total, int warnings) {}

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
            System.out.println(gson.toJson(summary));
        }
    }
}