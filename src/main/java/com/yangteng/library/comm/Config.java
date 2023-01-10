package com.yangteng.library.comm;

public interface Config {

    enum THEME {
        THEME_LIGHT(0),  THEME_DART(1);
        public Integer value = 0;

        THEME(Integer value) {
            this.value = value;
        }
    }

}
