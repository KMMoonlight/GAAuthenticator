package com.kmmoonlight.gademo;

public class Code {

    private String codeName;

    private String codeContent;

    public Code(String codeName, String codeContent) {
        this.codeName = codeName;
        this.codeContent = codeContent;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }
}
