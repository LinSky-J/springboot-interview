package com.jinlin.springbootinterview.interview.springcloud.overview;

/**
 * 微服务核心治理能力与技术选型元数据实体
 */
public class MicroserviceComponentMetadata {

    private final String dimension;
    private final String netflix;
    private final String alibaba;
    private final String springOfficial;

    public MicroserviceComponentMetadata(String dimension, String netflix, String alibaba, String springOfficial) {
        this.dimension = dimension;
        this.netflix = netflix;
        this.alibaba = alibaba;
        this.springOfficial = springOfficial;
    }

    public String getDimension() {
        return dimension;
    }

    public String getNetflix() {
        return netflix;
    }

    public String getAlibaba() {
        return alibaba;
    }

    public String getSpringOfficial() {
        return springOfficial;
    }
}
