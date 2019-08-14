package com.ruoyi.project.llc.notes.service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.project.llc.notes.domain.Notes;
import com.ruoyi.project.llc.notes.mapper.NotesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 论文笔记 服务层实现
 * 
 * @author ricardo
 * @date 2019-08-13
 */
@Service
public class NotesServiceImpl implements INotesService 
{
	@Autowired
	private NotesMapper notesMapper;
	@Autowired
	private NotesRepository notesRepository;

	/**
     * 查询论文笔记信息
     * 
     * @param id 论文笔记ID
     * @return 论文笔记信息
     */
    @Override
	public Notes selectNotesById(Integer id)
	{
	    return notesMapper.selectNotesById(id);
	}
	
	/**
     * 查询论文笔记列表
     * 
     * @param notes 论文笔记信息
     * @return 论文笔记集合
     */
	@Override
	public List<Notes> selectNotesList(Notes notes)
	{
	    return notesMapper.selectNotesList(notes);
	}

	/**
	 * 查询论文笔记列表
	 *
	 * @param notes 论文笔记信息
	 * @return 论文笔记集合
	 */
	@Override
	public List<Notes> selectNotesByAuthor(String author){
		return notesRepository.findByAuthor(author);
	}
    /**
     * 新增论文笔记
     * 
     * @param notes 论文笔记信息
     * @return 结果
     */
	@Override
	public int insertNotes(Notes notes)
	{
	    return notesMapper.insertNotes(notes);
	}
	
	/**
     * 修改论文笔记
     * 
     * @param notes 论文笔记信息
     * @return 结果
     */
	@Override
	public int updateNotes(Notes notes)
	{
		int result=0;

		//判断笔记是否存在
		Notes oldNotes=notesMapper.selectNotesById(notes.getId());
		if(null!=oldNotes){
			result=notesMapper.updateNotes(notes);
			File file=new File(oldNotes.getUrl());
			file.delete();
		}

		return result;
	}

	/**
     * 删除论文笔记对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteNotesByIds(String ids)
	{
		if(ids.length()>0){
			String[] id = ids.split(",");
			Notes notes;
			File file;

			//删除服务器文件
			for (int i = 0; i < id.length; i++) {
				notes=selectNotesById(Integer.valueOf(id[i]));
				if(null!=notes){
					file=new File(notes.getUrl());
					file.delete();
				}
			}

			//删除数据库记录
			return notesMapper.deleteNotesByIds(id);
		}

		return 0;
	}

	@Override
	public String checkNotesTitleUnqiue(Notes srcNote) {
		Notes notes = notesRepository.findByTitle(srcNote.getTitle());
		if (notes != null && !notes.getId().equals(srcNote.getId())) {
			return UserConstants.DEPT_NAME_NOT_UNIQUE;
		}
		return UserConstants.DEPT_NAME_UNIQUE;
	}
}
