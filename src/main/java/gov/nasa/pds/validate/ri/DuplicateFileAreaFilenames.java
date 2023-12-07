package gov.nasa.pds.validate.ri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.opensearch.search.builder.SearchSourceBuilder;

class DuplicateFileAreaFilenames extends OpensearchDocument implements Runnable, RestClientBuilder.HttpClientConfigCallback, RestClientBuilder.RequestConfigCallback {
  final private AuthInformation registry;
  final private AuthInformation search;
  final private Logger log = LogManager.getLogger(CommandLineInterface.class);
  private boolean done = false;

  public DuplicateFileAreaFilenames(AuthInformation registry, AuthInformation search) {
    super(search);
    this.registry = registry;
    this.search = search;    
  }
  @Override
  public Builder customizeRequestConfig(Builder requestConfigBuilder) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
    // TODO Auto-generated method stub
    return null;
  }
  public void findDuplicates() {
    if (AuthInformation.NO_AUTH.equals(this.registry)) {
      viaOpenSearch();
    } else {
      viaRegistry();
    }
  }
  public void findDuplicatesInBackground() {
    Thread t = new Thread(this);
    t.start();
  }
  @Override
  public void run() {
    findDuplicates();
    done = true;
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
                .size(10000)
                )
            .size(0));
    SearchResponse response;
    try {
      URL url = new URL(this.search.getUrl());
      client = new RestHighLevelClient(
          RestClient.builder(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
              .setHttpClientConfigCallback(this).setRequestConfigCallback(this));
      response = this.search(client, request);
      System.out.println (response);
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
