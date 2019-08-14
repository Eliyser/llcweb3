package com.ruoyi.project.llc.notes.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;
        
/**
 * 论文笔记表 llc_notes
 *
 * @author ricardo
 * @date 2019-08-13
 */
@Entity
@Table(name="llc_notes")
public class Notes extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Excel(name = "id")
    private Integer id;
    /** 完整url */
    @Column(name = "url")
    @Excel(name = "完整url")
    private String url;
    /** 是否可用 */
    @Column(name = "status")
    @Excel(name = "是否可用")
    private String status;
    /** 标题 */
    @Column(name = "title")
    @Excel(name = "标题")
    private String title;
    /** 上传者 */
    @Column(name = "author")
    @Excel(name = "上传者")
    private String author;
    /** 创建时间 */
    @Column(name = "create_time")
    @Excel(name = "创建时间")
    private Date createTime;
    /** 更新时间 */
    @Column(name = "update_time")
    @Excel(name = "更新时间")
    private Date updateTime;
    /**修改标识 0 新增 1 修改**/
    @Transient
    private int updateFlag;

    public Notes() {
        this.updateTime=new Date();
    }

    public Notes(Integer id, String url, String status, String title,
                 String author, Date createTime, Date updateTime) {
        this.id = id;
        this.url = url;
        this.status = status;
        this.title = title;
        this.author = author;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void setId(Integer id)
        {
            this.id = id;
        }

    public Integer getId()
        {
            return id;
        }
    public void setUrl(String url)
        {
            this.url = url;
        }

    public String getUrl()
        {
            return url;
        }
    public void setStatus(String status)
        {
            this.status = status;
        }

    public String getStatus()
        {
            return status;
        }
    public void setTitle(String title)
        {
            this.title = title;
        }

    public String getTitle()
        {
            return title;
        }
    public void setAuthor(String author)
        {
            this.author = author;
        }

    public String getAuthor()
        {
            return author;
        }
    public void setCreateTime(Date createTime)
        {
            this.createTime = createTime;
        }

    public Date getCreateTime()
        {
            return createTime;
        }
    public void setUpdateTime(Date updateTime)
        {
            this.updateTime = updateTime;
        }

    public Date getUpdateTime()
        {
            return updateTime;
        }

    public int getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("url", getUrl())
                .append("status", getStatus())
                .append("title", getTitle())
                .append("author", getAuthor())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
            .toString();
        }
}
