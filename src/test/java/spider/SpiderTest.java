package src.test.java.spider;


import domain.ContentConditionDto;
import domain.UrlConditionDto;
import domain.UrlDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import spider.DefaultSpiderHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpiderTest {

    private DefaultSpiderHandler spiderHandler = new DefaultSpiderHandler();
    @Test
    public void testSpider() throws Exception{
        UrlConditionDto urlConditionDto = GenerateUrlConditionLevel1();
        String startUrl = "http://www.foodaily.com/corpnews/";
        String pageContent = spiderHandler.crawl(startUrl);
        List<UrlDto> urlDtoLevel1List = parseUrl(pageContent, urlConditionDto, 0, -1);
        UrlConditionDto urlConditionDto2 = GenerateUrlConditionLevel2();
        List<UrlDto> urlDtoLevel2List = new ArrayList<>();
        for (UrlDto urlDto : urlDtoLevel1List) {
            String pageContentLevel2 = spiderHandler.crawl(urlDto.getUrl());
            urlDtoLevel2List.addAll(parseUrl2(pageContentLevel2, urlConditionDto2, 1000, urlDto.getId()));
        }
        List<UrlDto> urlDtoLevel3List = new ArrayList<>();
        UrlConditionDto urlConditionDto3 = GenerateUrlConditionLevel3();
        for (UrlDto urlDto : urlDtoLevel2List) {
            String pageContentLevel3 = spiderHandler.crawl(urlDto.getUrl());
            UrlDto urlDto1 = new UrlDto();
            urlDto1.setContentMap(parseContent(pageContentLevel3,urlConditionDto3,urlDto.getId()));
            urlDtoLevel3List.add(urlDto1);
        }

    }

    private List<UrlDto> parseUrl(String pageContent, UrlConditionDto urlConditionDto, Integer startId, Integer parentId) {
        int start = pageContent.indexOf(urlConditionDto.getStartLabel()) + urlConditionDto.getStartLabel().length();
        int end = pageContent.indexOf(urlConditionDto.getEndLabel());
        String pageContentLocal = pageContent.substring(start,end);
        Document doc = Jsoup.parse(pageContentLocal);
        Elements elements = doc.select("a");
        List<UrlDto> urlDtoList = new ArrayList<>();
        for(Element element : elements)  {
            startId ++;
            String url = element.attr("href");
            if (checkUrl(url, urlConditionDto)) {
                UrlDto urlDto = new UrlDto();
                urlDto.setUrl(url);
                urlDto.setId(startId);
                urlDto.setParentId(parentId);
                List<TextNode> textNodeList = element.textNodes();
                if (!CollectionUtils.isEmpty(textNodeList)) {
                    urlDto.setName(textNodeList.get(0).text());
                }
                urlDtoList.add(urlDto);
            }
        }
        return urlDtoList;
    }

    private List<UrlDto> parseUrl2(String pageContent, UrlConditionDto urlConditionDto, Integer startId, Integer parentId) {
        int start = pageContent.indexOf(urlConditionDto.getStartLabel()) + urlConditionDto.getStartLabel().length();
        int end = pageContent.indexOf(urlConditionDto.getEndLabel());
        String pageContentLocal = pageContent.substring(start,end);
        Document doc = Jsoup.parse(pageContentLocal);
        Elements elements = doc.select("td[align=left]");
        List<UrlDto> urlDtoList = new ArrayList<>();
        for(Element element : elements)  {
            startId ++;
            Elements childElement = element.getElementsByTag("ul").get(0).children();
            UrlDto urlDto = new UrlDto();
            Map<String, String> contentMap = new HashMap<>();
            for (Element childEle : childElement) {
                if (childEle instanceof Element) {
                    String url = element.select("a").attr("href");
                    if (!StringUtils.isEmpty(url) && checkUrl(url,urlConditionDto)) {
                        contentMap.put("url", url);
                        urlDto.setUrl(url);
                        urlDto.setId(startId);
                        urlDto.setParentId(parentId);
                    }
                    String name = element.select("strong[class=px16]").text();
                    if (!StringUtils.isEmpty(name)){
                        contentMap.put("name", name);
                        urlDto.setName(name);
                    }
                    String desc = element.select("li[class=f_gray]").text();
                    if (!StringUtils.isEmpty(desc)) {
                        contentMap.put("desc", desc);
                    }

                }
            }
            urlDto.setContentMap(contentMap);
            urlDtoList.add(urlDto);

        }
        return urlDtoList;
    }

    private Map<String, String> parseContent(String pageContent, UrlConditionDto urlConditionDto, Integer parentId) throws Exception{
        int start = pageContent.indexOf(urlConditionDto.getStartLabel()) + urlConditionDto.getStartLabel().length();
        int end = pageContent.indexOf(urlConditionDto.getEndLabel());
        String pageContentLocal = pageContent.substring(start,end);
        Document doc = Jsoup.parse(pageContentLocal);
        Elements elements = doc.select("img");
        for (Element element : elements) {
            String src = element.attr("src");
            String name = UUID.nameUUIDFromBytes(src.getBytes(StandardCharsets.UTF_8)).toString();
            try {
                download(src, name, "/Users/stay/Downloads/images/");
                pageContentLocal = pageContentLocal.replace(src, "/" + name);
            } catch (Exception e) {
                pageContentLocal = pageContentLocal.replace(src, "");
                System.out.println(e);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("content", pageContentLocal);

        return map;
    }

    private Boolean checkUrl(String url, UrlConditionDto urlConditionDto) {
        List<String> filterWordList = urlConditionDto.getFilterWords();
        List<String> containWordList = urlConditionDto.getContainWords();
        if (!CollectionUtils.isEmpty(filterWordList)) {
            for(String s : filterWordList) {
                if (url.contains(s)) {
                    return false;
                }
            }
        }
        if (!CollectionUtils.isEmpty(containWordList)) {
            for(String s : containWordList) {
                if (!url.contains(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    private UrlConditionDto GenerateUrlConditionLevel1() {
        UrlConditionDto urlConditionDto = new UrlConditionDto();
        urlConditionDto.setStartLabel("<div class=\"left_box\">");
        urlConditionDto.setEndLabel("<div class=\"box_corpnews  b_list_corpnews\">");
        urlConditionDto.setFilterWords(Arrays.asList("index.php"));
        return urlConditionDto;
    }

    private UrlConditionDto GenerateUrlConditionLevel2() {
        UrlConditionDto urlConditionDto = new UrlConditionDto();
        urlConditionDto.setStartLabel("<div class=\"box_corpnews  b_list_corpnews\">");
        urlConditionDto.setEndLabel("<div class=\"m_n f_l\">");
        return urlConditionDto;
    }

    private UrlConditionDto GenerateUrlConditionLevel3() {
        UrlConditionDto urlConditionDto = new UrlConditionDto();
        urlConditionDto.setStartLabel("<div id=\"content\" class=\"content\">");
        urlConditionDto.setEndLabel("<div class=\"pages\">");
        return urlConditionDto;
    }

    public  void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"/"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }


}
