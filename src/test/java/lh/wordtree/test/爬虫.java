package lh.wordtree.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class 爬虫 {


    public static void main(String[] args) {
        Spider.create(new GithubRepoPageProcessor())
                .addUrl("http://www.gashuw.com/modules/article/search.php?searchkey=%E5%9B%BD%E7%8E%8B")
                .addPipeline(new JsonFilePipeline("D:\\Code\\"))
                .thread(5).run();
    }

    static class GithubRepoPageProcessor implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

        @Override
        public void process(Page page) {
            page.putField("title", page.getHtml().css("#content > table > tbody > tr > td:nth-child(1)").get());
            page.putField("author", page.getHtml().css("#content > table > tbody > tr > td:nth-child(3)").get());
        }

        @Override
        public Site getSite() {
            return site;
        }
    }

    @TargetUrl("https://github.com/\\w+/\\w+")
    @HelpUrl("https://github.com/\\w+")
    public static class GithubRepo {

        @ExtractBy(value = "//h1[@class='public']/strong/a/text()", notNull = true)
        private String name;

        @ExtractByUrl("https://github\\.com/(\\w+)/.*")
        private String author;

        @ExtractBy("//div[@id='readme']/tidyText()")
        private String readme;
    }

}
