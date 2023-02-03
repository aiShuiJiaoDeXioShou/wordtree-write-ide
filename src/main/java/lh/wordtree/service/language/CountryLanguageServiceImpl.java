package lh.wordtree.service.language;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lh.wordtree.config.Config;
import lh.wordtree.utils.ConfigUtils;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class CountryLanguageServiceImpl implements CountryService {
    private final Map<String, String> languageMap = new LinkedHashMap<>();
    private Log log = LogFactory.get();

    // 加载语言资源包
    public CountryLanguageServiceImpl() {
        var language = ConfigUtils.getProperties("language");
        var properties = new Properties();
        try {
            var inputStream = FileUtil
                    .getUtf8Reader(Config.COUNTRY_LANGUAGE + "/" + language + ".properties");
            properties.load(inputStream);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                languageMap.put(key, properties.getProperty(key));
            }
        } catch (Exception e) {
            log.error("找不到所需要的语言配置文件！");
        }
    }

    public Map<String, String> getLanguageMap() {
        return languageMap;
    }
}
