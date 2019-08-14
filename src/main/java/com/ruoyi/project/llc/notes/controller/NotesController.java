package com.ruoyi.project.llc.notes.controller;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.llc.notes.domain.Notes;
import com.ruoyi.project.llc.notes.service.INotesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 论文笔记 信息操作处理
 * 
 * @author ricardo
 * @date 2019-08-13
 */
@Controller
@RequestMapping("/admin/llc/notes")
public class NotesController extends BaseController
{
    private static final Logger logger=LoggerFactory.getLogger(NotesController.class);

	private String prefix = "llc/notes";
	private static String Save_Url = FileUploadUtils.getDefaultBaseDir()+"notes/";
	//笔记文件类型
	private String[] notesExtension={"doc","docx"};

	@Autowired
	private INotesService notesService;
	
	@RequiresPermissions("llc:notes:view")
	@GetMapping()
	public String notes()
	{
	    return prefix + "/notes";
	}
	
	/**
	 * 查询论文笔记列表
	 */
	@RequiresPermissions("llc:notes:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Notes notes)
	{
		startPage();
        //notes为空，查出全部
		List<Notes> list = notesService.selectNotesList(notes);
		return getDataTable(list);
	}

	/**
	 * 获取本人论文笔记
	 */
	@GetMapping("/getNotesList")
	@ResponseBody
	public List<Notes> getDocumentList(){
		return notesService.selectNotesByAuthor(ShiroUtils.getLoginName());
	}

	/**
	 * 导出论文笔记列表
	 */
	@RequiresPermissions("llc:notes:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Notes notes)
    {
    	//notes为空时获取到全部？
    	List<Notes> list = notesService.selectNotesList(notes);
        ExcelUtil<Notes> util = new ExcelUtil<Notes>(Notes.class);
        return util.exportExcel(list, "notes");
    }
	
	/**
	 * 新增论文笔记
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增、修改保存论文笔记
	 */
	@RequiresPermissions("llc:notes:add")
	@Log(title = "论文笔记", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MultipartFile file,Notes notes)
	{
		int rtn = 0;

		//笔记标题是否唯一
		if(checkNotesTitleUnqiue(notes).equals(UserConstants.DEPT_NAME_NOT_UNIQUE)){
			logger.error("笔记标题不唯一！");
			return error("标题已存在！");
		}

		notes.setAuthor(ShiroUtils.getLoginName());
		notes.setCreateTime(new Date());
		try {
			notes = dealFile(file, notes);
			if (file != null) {
				if (notes.getUpdateFlag() == 1 && notes.getId() > 0) {//修改
					if(ShiroUtils.getLoginName().equals(notes.getAuthor())) {
						rtn = notesService.updateNotes(notes);
					} else { //非本人
						logger.error("超权限操作！");
						return error("抱歉，您没有该权限！");
					}
				} else {//新增
					rtn = notesService.insertNotes(notes);
				}

				//上传到服务器
				File desc = FileUploadUtils.getAbsoluteFile(Save_Url, notes.getUrl());
				file.transferTo(desc);
			}
		} catch (InvalidExtensionException e1) {
			logger.error("笔记文件类型错误！", e1);
			return error("请检查文件是否为Word文档！");
		} catch (Exception e2) {
			logger.error("保存失败，请检查后重试！", e2);
			return error(e2.getMessage());
		}

		return toAjax(rtn);
	}

	/**
	 * 修改论文笔记
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Notes notes = notesService.selectNotesById(id);
		mmap.put("notes", notes);
	    return prefix + "/edit";
	}

	/**
	 * 删除论文笔记
	 */
	@RequiresPermissions("llc:notes:remove")
	@Log(title = "论文笔记", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{
		int rtn=0;

		if(!StringUtils.isEmpty(ids)) {
			//是否本人
			String[] idArr = ids.split(",");
			for (String id : idArr) {
				Notes notes = notesService.selectNotesById(Integer.valueOf(id));
				if (!ShiroUtils.getLoginName().equals(notes.getAuthor())) {
					logger.error("超权限操作！");
					return error("抱歉，您没有该权限！");
				}
			}

			rtn=notesService.deleteNotesByIds(ids);
		}

		return toAjax(rtn);
	}

	public String checkNotesTitleUnqiue(Notes notes) {
		return notesService.checkNotesTitleUnqiue(notes);
	}

	/**
	 * @Author haien
	 * @Description 生成下载路径
	 * @Date 2019/7/9
	 * @Param [file, notes]
	 * @return com.ruoyi.project.llc.notes.domain.Notes
	 **/
	private Notes dealFile(MultipartFile file, Notes notes) throws Exception {
		if (file == null) return notes;

		//获取文件后缀
		String filename=file.getOriginalFilename();
		String suffix = FileUploadUtils.dealName(filename);

		if (StringUtils.isEmpty(suffix)) throw new Exception();

		//若笔记文件非Word文档
		if(!suffix.equals("doc") && !suffix.equals("docx")){
			throw new InvalidExtensionException(notesExtension,suffix,filename);
		}

		String name = Save_Url + notes.getTitle() + "." + suffix;
		notes.setUrl(name);

		return notes;
	}
}
