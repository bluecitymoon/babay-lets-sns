package com.doubletuan.sns.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.doubletuan.sns.domain.util.CustomDateTimeDeserializer;
import com.doubletuan.sns.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * A UserPost.
 */
@Entity
@Table(name = "USERPOST")
public class UserPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content")
    private String content;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "create_date")
    private DateTime createDate;

    @Column(name = "greet_count")
    private Integer greetCount;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @Column(name = "jid")
    private String jid;
    
    @OneToMany
    List<PostImage> postImages;

    public List<PostImage> getPostImages() {
		return postImages;
	}

	public void setPostImages(List<PostImage> postImages) {
		this.postImages = postImages;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getGreetCount() {
        return greetCount;
    }

    public void setGreetCount(Integer greetCount) {
        this.greetCount = greetCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserPost userPost = (UserPost) o;

        if ( ! Objects.equals(id, userPost.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserPost{" +
                "id=" + id +
                ", content='" + content + "'" +
                ", createDate='" + createDate + "'" +
                ", greetCount='" + greetCount + "'" +
                ", commentsCount='" + commentsCount + "'" +
                ", jid='" + jid + "'" +
                '}';
    }
}
