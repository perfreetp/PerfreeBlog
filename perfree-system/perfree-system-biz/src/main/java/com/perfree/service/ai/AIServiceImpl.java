package com.perfree.service.ai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.perfree.commons.exception.ServiceException;
import com.perfree.model.Option;
import com.perfree.service.option.OptionService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI服务实现类
 */
@Service
public class AIServiceImpl implements AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

    @Resource
    private OptionService optionService;

    /**
     * 获取AI配置
     */
    private Map<String, String> getAIConfig() {
        Map<String, String> config = new HashMap<>();
        List<Option> options = optionService.getOptionByIdentification("system_setting");
        for (Option option : options) {
            config.put(option.getKey(), option.getValue());
        }
        return config;
    }

    /**
     * 调用LLM API
     */
    private String callLLM(String prompt) {
        try {
            Map<String, String> config = getAIConfig();
            String apiKey = config.get("WEB_AI_KEY");
            String endpoint = config.get("WEB_AI_ENDPOINT");
            String model = config.get("WEB_AI_MODEL_NAME");

            if (apiKey == null || apiKey.isEmpty()) {
                throw new ServiceException(500, "AI API Key 未配置");
            }
            if (endpoint == null || endpoint.isEmpty()) {
                throw new ServiceException(500, "AI API Endpoint 未配置");
            }
            if (model == null || model.isEmpty()) {
                throw new ServiceException(500, "AI Model 未配置");
            }

            logger.info("调用AI服务 - endpoint: {}, model: {}", endpoint, model);
            logger.debug("请求prompt: {}", prompt);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2000);
            requestBody.put("stream", false); // 确保不是流式响应
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            requestBody.put("messages", new Object[]{message});

            String requestBodyStr = JSONUtil.toJsonStr(requestBody);
            logger.debug("请求体: {}", requestBodyStr);

            // 发送请求
            HttpResponse response = HttpRequest.post(endpoint)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(requestBodyStr)
                    .timeout(60000)
                    .execute();

            String responseBody = response.body();
            logger.info("AI API 响应状态: {}, 响应内容: {}", response.getStatus(), responseBody);

            if (!response.isOk()) {
                logger.error("AI API 请求失败 - 状态码: {}, 响应: {}", response.getStatus(), responseBody);
                throw new ServiceException(500, "AI API 请求失败: " + response.getStatus() + " - " + responseBody);
            }

            JSONObject result = JSONUtil.parseObj(responseBody);
            
            // 检查是否有错误信息
            if (result.containsKey("error")) {
                JSONObject error = result.getJSONObject("error");
                String errorMsg = error.getStr("message", "未知错误");
                logger.error("AI API 返回错误: {}", errorMsg);
                throw new ServiceException(500, "AI API 错误: " + errorMsg);
            }

            // 检查choices是否存在
            if (!result.containsKey("choices") || result.getJSONArray("choices").isEmpty()) {
                logger.error("AI API 响应中没有choices字段或为空");
                throw new ServiceException(500, "AI API 响应格式错误: 没有返回内容");
            }

            String content = result.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getStr("content");

            logger.info("AI生成内容成功, 长度: {}", content != null ? content.length() : 0);
            return content;

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("调用AI服务异常", e);
            throw new ServiceException(500, "调用AI服务失败: " + e.getMessage());
        }
    }

    @Override
    public String continueWriting(String content) {
        String prompt = "请续写以下内容，保持原文的风格和语气，续写长度适中：\n\n" + content;
        return callLLM(prompt);
    }

    @Override
    public String generateSummary(String content) {
        String prompt = "请为以下内容生成一个简洁的摘要，摘要长度控制在100-200字之间：\n\n" + content;
        return callLLM(prompt);
    }

    @Override
    public String optimizeSeoTitle(String title, String content) {
        String prompt = "请根据以下文章内容，优化SEO标题，要求：\n" +
                "1. 标题长度控制在30-60个字符之间\n" +
                "2. 包含主要关键词\n" +
                "3. 具有吸引力\n" +
                "4. 适合搜索引擎优化\n\n" +
                "原标题: " + title + "\n\n" +
                "文章内容: " + content;
        return callLLM(prompt);
    }

    @Override
    public String generateSeoKeywords(String content) {
        String prompt = "请为以下文章内容生成5-8个SEO关键词，用英文逗号分隔，关键词要精准且有搜索量：\n\n" + content;
        return callLLM(prompt);
    }

    @Override
    public String generateSeoDescription(String content) {
        String prompt = "请为以下文章内容生成SEO描述，要求：\n" +
                "1. 长度控制在50-160个字符之间\n" +
                "2. 包含主要关键词\n" +
                "3. 能准确概括文章内容\n" +
                "4. 具有吸引力能提高点击率\n\n" + content;
        return callLLM(prompt);
    }
}
