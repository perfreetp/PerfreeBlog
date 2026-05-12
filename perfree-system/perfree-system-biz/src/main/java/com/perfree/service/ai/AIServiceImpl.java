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
        String prompt = "你是一位专业的文章写作助手。请续写以下内容，保持原文的风格和语气，续写长度适中。\n\n" +
                "要求：\n" +
                "1. 只返回续写的内容，不要返回原文\n" +
                "2. 不要任何解释、说明或前后缀\n" +
                "3. 保持与原文连贯的过渡\n\n" +
                "原文：" + content + "\n\n" +
                "请直接输出续写的内容：";
        String result = callLLM(prompt);
        return result.trim();
    }

    @Override
    public String generateSummary(String content) {
        String prompt = "你是一位专业的文章摘要生成助手。请为以下内容生成一个简洁的摘要。\n\n" +
                "要求：\n" +
                "1. 摘要长度控制在100-200字之间\n" +
                "2. 准确概括文章主要内容\n" +
                "3. 只返回摘要文本，不要任何解释或说明\n\n" +
                "文章内容：" + content + "\n\n" +
                "请直接输出摘要：";
        String result = callLLM(prompt);
        return result.trim();
    }

    @Override
    public String optimizeSeoTitle(String title, String content) {
        String prompt = "你是一位专业的SEO优化专家。请根据以下文章内容，优化SEO标题。\n\n" +
                "要求：\n" +
                "1. 标题长度控制在30-60个字符之间\n" +
                "2. 包含主要关键词\n" +
                "3. 具有吸引力，适合搜索引擎优化\n" +
                "4. 只返回优化后的标题文本，不要任何解释、说明或前后缀\n" +
                "5. 不要使用markdown格式\n\n" +
                "原标题：" + title + "\n\n" +
                "文章内容：" + content + "\n\n" +
                "请直接输出优化后的标题：";
        String result = callLLM(prompt);
        // 清理返回内容，确保只返回标题
        return result
                .replaceAll("^#.*$", "")
                .replaceAll("^优化后的标题[:：]\\s*", "")
                .replaceAll("^标题[:：]\\s*", "")
                .replaceAll("^【.*】$", "")
                .replaceAll("^[\"'`]|[\"'`]$", "")
                .trim();
    }

    @Override
    public String generateSeoKeywords(String content) {
        String prompt = "你是一位专业的SEO优化专家。请为以下文章内容生成5-8个SEO关键词。\n\n" +
                "要求：\n" +
                "1. 用英文逗号分隔关键词\n" +
                "2. 关键词要精准且有搜索量\n" +
                "3. 只返回关键词，不要任何解释或说明\n" +
                "4. 不要使用数字编号\n\n" +
                "文章内容：" + content + "\n\n" +
                "请直接输出关键词：";
        String result = callLLM(prompt);
        return result
                .replaceAll("^关键词[:：]\\s*", "")
                .replaceAll("^\\d+[.、]\\s*", "")
                .replaceAll("\\n", ",")
                .replaceAll("[,，]+", ",")
                .trim();
    }

    @Override
    public String generateSeoDescription(String content) {
        String prompt = "你是一位专业的SEO优化专家。请为以下文章内容生成SEO描述。\n\n" +
                "要求：\n" +
                "1. 长度控制在50-160个字符之间\n" +
                "2. 包含主要关键词\n" +
                "3. 能准确概括文章内容\n" +
                "4. 具有吸引力能提高点击率\n" +
                "5. 只返回描述文本，不要任何解释或说明\n\n" +
                "文章内容：" + content + "\n\n" +
                "请直接输出SEO描述：";
        String result = callLLM(prompt);
        return result
                .replaceAll("^SEO描述[:：]\\s*", "")
                .replaceAll("^描述[:：]\\s*", "")
                .trim();
    }
}
