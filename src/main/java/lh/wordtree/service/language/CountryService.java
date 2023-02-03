package lh.wordtree.service.language;

import java.util.Map;

public interface CountryService {
    Map<String, String> language = new CountryLanguageServiceImpl().getLanguageMap();

    Map<String, String> getLanguageMap();
}
