package com.ruoyi.project.llc.notes.service;

import com.ruoyi.project.llc.notes.domain.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Notes,Integer> {

    /**
     * @Author haien
     * @Description 查询同标题笔记
     * @Date 2019/7/9
     * @Param [title]
     * @return com.ruoyi.project.llc.notes.domain.Notes
     **/
    public Notes findByTitle(String title);

    /**
     * @Author haien
     * @Description 查询本人笔记
     * @Date 2019/8/14
     * @Param [author]
     * @return java.util.List<com.ruoyi.project.llc.notes.domain.Notes>
     **/
    public List<Notes> findByAuthor(String author);

}
