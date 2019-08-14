package com.ruoyi.project.llc.notes.service;

import com.ruoyi.project.llc.notes.domain.Notes;
import java.util.List;

/**
 * 论文笔记 服务层
 * 
 * @author ricardo
 * @date 2019-08-13
 */
public interface INotesService 
{
	/**
     * 查询论文笔记信息
     * 
     * @param id 论文笔记ID
     * @return 论文笔记信息
     */
	public Notes selectNotesById(Integer id);
	
	/**
     * 查询论文笔记列表
     * 
     * @param notes 论文笔记信息
     * @return 论文笔记集合
     */
	public List<Notes> selectNotesList(Notes notes);

	/**
	 * 查询本人论文笔记
	 *
	 * @param notes 论文笔记信息
	 * @return 论文笔记集合
	 */
	public List<Notes> selectNotesByAuthor(String author);
	
	/**
     * 新增论文笔记
     * 
     * @param notes 论文笔记信息
     * @return 结果
     */
	public int insertNotes(Notes notes);
	
	/**
     * 修改论文笔记
     * 
     * @param notes 论文笔记信息
     * @return 结果
     */
	public int updateNotes(Notes notes);
		
	/**
     * 删除论文笔记信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteNotesByIds(String ids);

	/**
	 * 查询笔记标题是否唯一
	 */
	public String checkNotesTitleUnqiue(Notes srcNote);
	
}
