package gov.nasa.pds.validate.ri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.opensearch.search.builder.SearchSourceBuilder;

class DuplicateFileAreaFilenames extends OpensearchDocument implements Runnable, RestClientBuilder.HttpClientConfigCallback, RestClientBuilder.RequestConfigCallback {
  final private AuthInformation registry;
  final private AuthInformation search;
  final private HashMap<String, List<String>> duplicates = new HashMap<String, List<String>>();
  final private Logger log = LogManager.getLogger(CommandLineInterface.class);
  private boolean done = true;

  public DuplicateFileAreaFilenames(AuthInformation registry, AuthInformation search) {
    super(search);
    this.registry = registry;
    this.search = search;    
  }
  public void findDuplicates() {
    if (AuthInformation.NO_AUTH.equals(this.registry)) {
      viaOpenSearch();
    } else {
      viaRegistry();
    }
  }
  public void findDuplicatesInBackground() {
    this.done = false;
    Thread t = new Thread(this);
    t.setDaemon(true);
    t.start();
  }
  public Map<String, List<String>> getResults() {
    return this.duplicates;
  }
  @Override
  public void run() {
    findDuplicates();
    this.done = true;
    synchronized(this) {
      this.notifyAll();
    }
  }
  private void viaOpenSearch() {
    RestHighLevelClient client = null;
    /* 
     * GET registry/_search
     * {
     *   "size": 0,
     *   "aggregations": {
     *     "duplicates": {
     *       "terms": {
     *         "field": "ops:Data_File_Info/ops:file_name",
     *         "size": 100,
     *         "min_doc_count": 2
     *       }
     *     }
     *   }
     * }
    */
    SearchRequest request = new SearchRequest()
        .indices("registry")
        .source(new SearchSourceBuilder()
            .aggregation(new TermsAggregationBuilder("duplicates")
                .field("ops:Data_File_Info/ops:file_name")
                .minDocCount(2)
                .size(this.PAGE_SIZE)
                )
            .size(0));
    SearchResponse response;
    try {
      URL url = new URL(this.search.getUrl());
      client = new RestHighLevelClient(
          RestClient.builder(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
              .setHttpClientConfigCallback(this).setRequestConfigCallback(this));
      response = this.search(client, request);
      for (Terms.Bucket bucket : ((Terms)response.getAggregations().get("duplicates")).getBuckets()) {
        SearchRequest findIDs = new SearchRequest()
            .indices("registry")
            .source(new SearchSourceBuilder()
                .fetchSource ("lid", null)
                .query(
                    QueryBuilders.boolQuery()
                        .must(QueryBuilders
                            .termQuery("ops:Data_File_Info/ops:file_name", bucket.getKeyAsString())))
                .size(this.PAGE_SIZE));
        SearchResponse duplicators = this.search(client, findIDs);
        HashSet<String> lids = new HashSet<String>();
        for (SearchHit hit : duplicators.getHits()) {
          lids.add(hit.getSourceAsMap().get("lid").toString());
        }
        if (1 < lids.size()) {
          this.log.error("Found duplicate file: " + bucket.getKeyAsString());
          this.duplicates.put (bucket.getKeyAsString(), new ArrayList<String>(lids));
        }
      }
    } catch (MalformedURLException e) {
      this.log.error ("Could not form a valid URL from " + this.search.getUrl(), e);
    } catch (IOException e) {
      this.log.error ("Something went wrong talking to opensearch.", e);
    }
  }
  private void viaRegistry() {
    this.log.fatal("finding duplicate file area filenames is not implemented.");
  }
  public synchronized void waitTillDone() {
    while (!this.done) {
      try {
        this.wait();
      } catch (InterruptedException e) {
        // ignore
      }
    }
  }
}
