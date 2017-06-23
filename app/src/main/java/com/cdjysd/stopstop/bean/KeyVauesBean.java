package com.cdjysd.stopstop.bean;

/**类名: KeyVauesBean
 * <br/>功能描述: 存放key-value
 * <br/>作者: 陈渝金
 * <br/>时间: 2017/3/10
 * <br/>最后修改者:
 * <br/>最后修改内容:
 */

public class KeyVauesBean {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public KeyVauesBean(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
