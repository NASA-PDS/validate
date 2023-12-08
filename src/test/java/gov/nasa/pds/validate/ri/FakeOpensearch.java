package gov.nasa.pds.validate.ri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.DeprecationHandler;
import org.opensearch.common.xcontent.NamedXContentRegistry;
import org.opensearch.common.xcontent.XContentParser;
import org.opensearch.common.xcontent.json.JsonXContent;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.TermQueryBuilder;

public class FakeOpensearch extends OpensearchDocument {
  final private Logger log = LogManager.getLogger(FakeOpensearch.class);
  final private Set<String> broken = new HashSet<String>();

  public FakeOpensearch() {
    super(null);
    OpensearchDocument.sourceOverride = this;
  }

  private SearchResponse getSearchResponseFromJson(String jsonResponse) throws IOException {
    XContentParser parser = JsonXContent.jsonXContent.createParser(NamedXContentRegistry.EMPTY,
        DeprecationHandler.IGNORE_DEPRECATIONS, jsonResponse);
    return SearchResponse.fromXContent(parser);
  }

  @Override
  protected synchronized SearchResponse search(RestHighLevelClient client, SearchRequest request)
      throws IOException {
    SearchResponse result;
    if (request.source().query() != null) {
      FileInputStream fis = null;
      result = this.getSearchResponseFromJson(
          "{\"took\":4,\"timed_out\":false,\"_shards\":{\"total\":0,\"successful\":1,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":{\"value\":0,\"relation\":\"eq\"},\"max_score\":4.841559,\"hits\":[]}}");
      String field = ((TermQueryBuilder) ((BoolQueryBuilder) request.source().query()).must().get(0))
          .fieldName();
      String index = request.indices()[0];
      String value = ((TermQueryBuilder) ((BoolQueryBuilder) request.source().query()).must().get(0))
          .value().toString();
      String workingFilename =
          "src/test/resources/riut/" + index + "___" + field + "___" + value + ".json";
      try {
        fis = new FileInputStream(workingFilename);
        result = this.getSearchResponseFromJson(new String(fis.readAllBytes()));
      } catch (FileNotFoundException e) {
        if (!this.broken.contains(workingFilename)) {
          this.broken.add(workingFilename);
          log.error("Incomplete test data set because of missing file: " + workingFilename);
        }
      } catch (IOException e) {
        if (!this.broken.contains(workingFilename)) {
          this.broken.add(workingFilename);
          log.error(
              "Incomplete test data set because of JSON parsing error of file: " + workingFilename,
              e);
        }
      } finally {
        if (fis != null)
          fis.close();
      }
    } else {
      result = this.getSearchResponseFromJson(
          "{\"took\":233,\"timed_out\":false,\"_shards\":{\"total\":1,\"successful\":1,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":{\"value\":10000,\"relation\":\"gte\"},\"max_score\":null,\"hits\":[]},\"aggregations\":{}}}\n"
          );
    }
    return result;
  }
}
