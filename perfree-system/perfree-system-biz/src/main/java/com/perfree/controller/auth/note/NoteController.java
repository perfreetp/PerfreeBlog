package com.perfree.controller.auth.note;

import com.perfree.commons.common.CommonResult;
import com.perfree.commons.common.PageResult;
import com.perfree.controller.auth.article.vo.ArticleRespVO;
import com.perfree.controller.auth.note.vo.NoteAddReqVO;
import com.perfree.controller.auth.note.vo.NotePageReqVO;
import com.perfree.controller.auth.note.vo.NoteUpdateReqVO;
import com.perfree.service.article.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.perfree.commons.common.CommonResult.success;

@RestController
@Tag(name = "笔记相关接口")
@RequestMapping("api/auth/note")
public class NoteController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/page")
    @Operation(summary = "笔记分页列表")
    public CommonResult<PageResult<ArticleRespVO>> page(@RequestBody NotePageReqVO pageVO) {
        return success(articleService.notePage(pageVO));
    }

    @PostMapping("/createNote")
    @Operation(summary = "创建笔记")
    @PreAuthorize("@ss.hasPermission('admin:note:create')")
    public CommonResult<ArticleRespVO> createNote(@RequestBody @Valid NoteAddReqVO noteAddReqVO) {
        return CommonResult.success(articleService.createNote(noteAddReqVO));
    }

    @PutMapping("/updateNote")
    @Operation(summary = "修改笔记")
    @PreAuthorize("@ss.hasPermission('admin:note:update')")
    public CommonResult<ArticleRespVO> updateNote(@RequestBody @Valid NoteUpdateReqVO noteUpdateReqVO) {
        return CommonResult.success(articleService.updateNote(noteUpdateReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "根据id获取笔记")
    public CommonResult<ArticleRespVO> get(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(articleService.getNoteById(id));
    }

    @DeleteMapping("/del")
    @Operation(summary = "根据id删除笔记")
    @PreAuthorize("@ss.hasPermission('admin:note:delete')")
    public CommonResult<Boolean> del(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(articleService.del(id));
    }
}
