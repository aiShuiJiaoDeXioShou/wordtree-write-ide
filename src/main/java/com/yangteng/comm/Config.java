package com.yangteng.comm;

public class Config {

    public enum THEME {
        THEME_LIGHT(0),  THEME_DART(1);
        public Integer value = 0;

        THEME(Integer value) {
            this.value = value;
        }
    }

}
