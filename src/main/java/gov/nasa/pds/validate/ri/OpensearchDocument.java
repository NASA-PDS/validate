package gov.nasa.pds.validate.ri;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig.Builder;
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

public class OpensearchDocument implements DocumentInfo, RestClientBuilder.HttpClientConfigCallback, RestClientBuilder.RequestConfigCallback {
  final private int PAGE_SIZE = 5000;
  final private AuthInformation context;
  final private HashMap<String,Map<String,Object>> documents = new HashMap<String,Map<String,Object>>();
  final private LidvidComparator lidvid_compare = new LidvidComparator();
  final private Logger log = LogManager.getLogger(OpensearchDocument.class);
 
  private void load(String lidvid) {
    if (!this.documents.containsKey(lidvid) ) {
      try {
        RestHighLevelClient client = null;
        SearchRequest request = new SearchRequest()
            .indices("registry")
            .source(new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery(lidvid.contains("::") ? "lidvid":"lid", lidvid)))
                .size(this.PAGE_SIZE));
        SearchResponse response;
        URL url = new URL(this.context.getUrl());
        try {
          client = new RestHighLevelClient(RestClient.builder(new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
              .setHttpClientConfigCallback(this)
              .setRequestConfigCallback(this));
          response = client.search(request, RequestOptions.DEFAULT);
          if (response != null && response.getHits() != null && response.getHits().getTotalHits() != null) {
            if (response.getHits().getTotalHits().value == 1L) this.documents.put(lidvid, response.getHits().getAt(0).getSourceAsMap());
            else {
              ArrayList<String> ids = new ArrayList<String>();
              for (SearchHit hit : response.getHits()) ids.add(hit.getId());
              ids.sort(lidvid_compare);
              for (SearchHit hit : response.getHits()) {
                if (hit.getId().equals(ids.get(ids.size()-1))) this.documents.put(hit.getId(), hit.getSourceAsMap());
              }
            }
          }
        } finally { if (client != null) client.close(); }
      }
      catch (IOException ioe) {
        this.log.fatal("Error reading from URL: " + this.context.getUrl(), ioe);
      }
    }
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
      if (this.documents.get(lidvid).containsKey("product_class")) result = this.documents.get(lidvid).get("product_class").toString();
    }
    return result;
  }
  @SuppressWarnings("unchecked")
  @Override
  public List<String> getReferencesOf(String lidvid) {
    ArrayList<String> references = new ArrayList<String>();
    if (this.exists(lidvid)) {
      for (String key : this.documents.get(lidvid).keySet()) {
        if (key.startsWith("ref_lid_")) {
          Object value = this.documents.get(lidvid).get(key);
          if (value instanceof String) references.add((String)value);
          else if (value instanceof List) references.addAll((List<String>)value);
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
      }
      catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
        this.log.fatal("Could not set self signed trust", e);
      }
    }
    if (!"".equals(this.context.getUsername())) {
      BasicCredentialsProvider handler = new BasicCredentialsProvider();
      handler.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.context.getUsername(), this.context.getPassword()));
      httpClientBuilder.setDefaultCredentialsProvider(handler);
    } else log.info("Skipping credentials because no username was given.");
    return httpClientBuilder;
  }
  @Override
  public Builder customizeRequestConfig(Builder requestConfigBuilder) {
    final int ms = 1000;
    requestConfigBuilder.setConnectionRequestTimeout(5*ms);
    requestConfigBuilder.setSocketTimeout(10*ms);
    return requestConfigBuilder;
  }
}
