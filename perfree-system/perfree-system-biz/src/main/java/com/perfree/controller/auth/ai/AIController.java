package com.perfree.controller.auth.ai;

import com.perfree.commons.common.CommonResult;
import com.perfree.controller.auth.ai.vo.*;
import com.perfree.service.ai.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static com.perfree.commons.common.CommonResult.success;

/**
 * AI辅助写作控制器
 */
@RestController
@Tag(name = "AI辅助写作接口")
@RequestMapping("api/auth/ai")
public class AIController {

    @Resource
    private AIService aiService;

    @PostMapping("/continueWriting")
    @Operation(summary = "AI续写")
    public CommonResult<String> continueWriting(@RequestBody @Valid AIContinueWritingReqVO reqVO) {
        String result = aiService.continueWriting(reqVO.getContent());
        return success(result);
    }

    @PostMapping("/generateSummary")
    @Operation(summary = "生成文章摘要")
    public CommonResult<String> generateSummary(@RequestBody @Valid AIGenerateSummaryReqVO reqVO) {
        String result = aiService.generateSummary(reqVO.getContent());
        return success(result);
    }

    @PostMapping("/optimizeSeoTitle")
    @Operation(summary = "SEO标题优化")
    public CommonResult<String> optimizeSeoTitle(@RequestBody @Valid AIOptimizeSeoTitleReqVO reqVO) {
        String result = aiService.optimizeSeoTitle(reqVO.getTitle(), reqVO.getContent());
        return success(result);
    }

    @PostMapping("/generateSeoKeywords")
    @Operation(summary = "生成SEO关键词")
    public CommonResult<String> generateSeoKeywords(@RequestBody @Valid AIGenerateSeoKeywordsReqVO reqVO) {
        String result = aiService.generateSeoKeywords(reqVO.getContent());
        return success(result);
    }

    @PostMapping("/generateSeoDescription")
    @Operation(summary = "生成SEO描述")
    public CommonResult<String> generateSeoDescription(@RequestBody @Valid AIGenerateSeoDescriptionReqVO reqVO) {
        String result = aiService.generateSeoDescription(reqVO.getContent());
        return success(result);
    }
}
