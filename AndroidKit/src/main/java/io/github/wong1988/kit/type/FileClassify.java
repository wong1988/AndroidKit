package io.github.wong1988.kit.type;

public enum FileClassify {

    AUDIO("音频类型"), VIDEO("视频类型"), IMAGE("图片类型"), EXCEL("Excel文档类型"), WORD("Word文档类型"),
    PPT("PowerPoint文档类型"), PDF("Pdf类型"), ZIP("压缩包类型"), TXT("Txt文本类型"), HTML("Html文件类型"), OTHER("其它类型");

    public String describe;

    FileClassify(String describe) {
        this.describe = describe;
    }
}
