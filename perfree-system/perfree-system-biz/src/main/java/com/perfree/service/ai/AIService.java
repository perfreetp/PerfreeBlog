package com.perfree.service.ai;

/**
 * AI服务接口
 */
public interface AIService {

    /**
     * AI续写
     * @param content 原文内容
     * @return 续写内容
     */
    String continueWriting(String content);

    /**
     * 生成摘要
     * @param content 原文内容
     * @return 摘要
     */
    String generateSummary(String content);

    /**
     * SEO标题优化
     * @param title 原标题
     * @param content 文章内容
     * @return 优化后的标题
     */
    String optimizeSeoTitle(String title, String content);

    /**
     * 生成SEO关键词
     * @param content 文章内容
     * @return 关键词列表,逗号分隔
     */
    String generateSeoKeywords(String content);

    /**
     * 生成SEO描述
     * @param content 文章内容
     * @return SEO描述
     */
    String generateSeoDescription(String content);
}
