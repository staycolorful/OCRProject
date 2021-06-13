package spider;

import domain.UrlConditionDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Component
public class DefaultSpiderHandler {
    /**
     * 获取网页内容
     * @param url
     * @return
     * @throws Exception
     */
    public String crawl(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(url))) {
            String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取页面的url地址
     * @return
     */
    public List<String> parseHtml(String startPage, UrlConditionDto urlConditionDto) {
        try {
            String page = crawl(startPage);


        } catch (IOException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }
}
