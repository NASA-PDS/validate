package gov.nasa.pds.validate.ri;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;

public class OpensearchDocument implements DocumentInfo, RestClientBuilder.HttpClientConfigCallback,
    RestClientBuilder.RequestConfigCallback {
  protected static OpensearchDocument sourceOverride = null;
  final private int PAGE_SIZE = 5000;
  final private AuthInformation context;
  final private HashMap<String, Map<String, Object>> documents =
      new HashMap<String, Map<String, Object>>();
  final private HashMap<String, List<String>> references = new HashMap<String, List<String>>();
  final private LidvidComparator lidvid_compare = new LidvidComparator();
  final private Logger log = LogManager.getLogger(OpensearchDocument.class);

  private void load(String lidvid) {
    if (!this.documents.containsKey(lidvid)) {
      try {
        RestHighLevelClient client = null;
        SearchRequest request =
            new SearchRequest()
                .indices(
                    "registry")
                .source(
                    new SearchSourceBuilder()
                        .query(
                            QueryBuilders.boolQuery()
                                .must(QueryBuilders
                                    .termQuery(lidvid.contains("::") ? "lidvid" : "lid", lidvid)))
                        .size(this.PAGE_SIZE));
        SearchResponse response;
        URL url = new URL(this.context.getUrl());
        try {
          client = new RestHighLevelClient(
              RestClient.builder(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
                  .setHttpClientConfigCallback(this).setRequestConfigCallback(this));
          response = this.search(client, request);
          if (response != null && response.getHits() != null
              && response.getHits().getTotalHits() != null) {
            if (response.getHits().getTotalHits().value == 1L)
              this.documents.put(lidvid, response.getHits().getAt(0).getSourceAsMap());
            else {
              ArrayList<String> ids = new ArrayList<String>();
              for (SearchHit hit : response.getHits())
                ids.add(hit.getId());
              ids.sort(lidvid_compare);
              for (SearchHit hit : response.getHits()) {
                if (hit.getId().equals(ids.get(ids.size() - 1))) {
                  this.documents.put(lidvid, hit.getSourceAsMap());
                  this.documents.put(hit.getId(), hit.getSourceAsMap());
                }
              }
            }
          }
        } finally {
          if (client != null)
            client.close();
        }
      } catch (IOException ioe) {
        this.log.fatal("Error reading from URL: " + this.context.getUrl(), ioe);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void load_refs(String lidvid) {
    if (!this.references.containsKey(lidvid)) {
      try {
        RestHighLevelClient client = null;
        SearchRequest request = new SearchRequest().indices("registry-refs")
            .source(new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery(
                        lidvid.contains("::") ? "collection_lidvid" : "collection_lid", lidvid)))
                .size(this.PAGE_SIZE));
        SearchResponse response;
        URL url = new URL(this.context.getUrl());
        try {
          HashSet<String> newbies = new HashSet<String>();
          client = new RestHighLevelClient(
              RestClient.builder(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
                  .setHttpClientConfigCallback(this).setRequestConfigCallback(this));
          response = this.search(client, request);
          if (response != null && response.getHits() != null) {
            for (SearchHit hit : response.getHits()) {
              newbies.addAll((List<String>) hit.getSourceAsMap().get("product_lid"));
              newbies.addAll((List<String>) hit.getSourceAsMap().get("product_lidvid"));
            }
          }
          this.references.put(lidvid, new ArrayList<String>(newbies));
        } finally {
          if (client != null)
            client.close();
        }
      } catch (IOException ioe) {
        this.log.fatal("Error reading from URL: " + this.context.getUrl(), ioe);
      }
    }
  }

  protected SearchResponse search(RestHighLevelClient client, SearchRequest request)
      throws IOException {
    /*
     * Below is a particularly evil bit of code. It allows unit test to set the static variable
     * (evil 1) which can then flow a priori data back through the code as though it was retreived
     * from an opensearch database. It means the unit test code no only returns values it has to
     * wrap them up as though opensearch did its work (evil 2). Therefore the code becomes sensitive
     * to the ability of unit testing to emulate opensearch as well as the performance of the
     * integrity checks.
     * 
     * Despite the evil, it keeps the operational code from knowing about the unit test code.
     * Unfortunately the opensearch code RestHighLevelClient is not written to be overriden either.
     * Therefore cannot simply extend it or implement a common interface to do a more subtle
     * insertion of unit testing.
     */
    if (OpensearchDocument.sourceOverride != null)
      return OpensearchDocument.sourceOverride.search(client, request);
    return client.search(request, RequestOptions.DEFAULT);
  }

  public OpensearchDocument(AuthInformation context) {
    this.context = context;
  }

  @Override
  public boolean exists(String lidvid) {
    this.load(lidvid);
    return this.documents.containsKey(lidvid);
  }

  @Override
  public String getProductTypeOf(String lidvid) {
    String result = "";
    if (this.exists(lidvid)) {
      if (this.documents.get(lidvid).containsKey("product_class"))
        result = this.documents.get(lidvid).get("product_class").toString();
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<String> getReferencesOf(String lidvid) {
    ArrayList<String> references = new ArrayList<String>();
    if (this.exists(lidvid)) {
      if (this.getProductTypeOf(lidvid).equalsIgnoreCase("Product_Collection")) {
        load_refs(lidvid);
        references.addAll(this.references.get(lidvid));
      } else {
        for (String key : this.documents.get(lidvid).keySet()) {
          if (key.startsWith("ref_lid_")) {
            Object value = this.documents.get(lidvid).get(key);
            if (value instanceof String)
              references.add((String) value);
            else if (value instanceof List)
              references.addAll((List<String>) value);
          }
        }
      }
    }
    return references;
  }

  @Override
  public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
    if (this.context.getTrustSelfSigned()) {
      try {
        SSLContextBuilder sslBld = SSLContexts.custom();
        sslBld.loadTrustMaterial(new TrustSelfSignedStrategy());
        httpClientBuilder.setSSLContext(sslBld.build());
      } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
        this.log.fatal("Could not set self signed trust", e);
      }
    }
    if (!Objects.equals(this.context.getUsername(), "")) {
      BasicCredentialsProvider handler = new BasicCredentialsProvider();
      handler.setCredentials(AuthScope.ANY,
          new UsernamePasswordCredentials(this.context.getUsername(), this.context.getPassword()));
      httpClientBuilder.setDefaultCredentialsProvider(handler);
    } else
      log.info("Skipping credentials because no username was given.");
    return httpClientBuilder;
  }

  @Override
  public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
    final int ms = 1000;
    requestConfigBuilder.setConnectionRequestTimeout(5 * ms);
    requestConfigBuilder.setSocketTimeout(10 * ms);
    return requestConfigBuilder;
  }
}
