package lh.wordtree.service.language;

import lh.wordtree.utils.PinYinUtils;

public class WTWriterEditorServiceImpl implements WTWriterEditorService {

    public SearchInfo search(String keyWord) {
        String pingyi;
        // 判断该keyWord是否是中文，如果是中文则返回该中文的拼音
        if (PinYinUtils.containsChinese(keyWord)) {
            pingyi = PinYinUtils.getPinYin(keyWord);
        } else pingyi = keyWord;
        // 在网上查找搜索结果，然后返回,这里我们直接使用爬虫
        String info = "";
        return new SearchInfo(keyWord, pingyi, info);
    }

    public TranslationInfo translation(String keyWord) {

        return new TranslationInfo();
    }

}
