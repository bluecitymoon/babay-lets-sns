package com.doubletuan.sns.domain;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * A PostImage.
 */
@Entity
@Table(name = "POSTIMAGE")
public class PostImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "src")
    private String src;

    @Column(name = "src1")
    private String src1;

    @Column(name = "src2")
    private String src2;

    @Column(name = "src3")
    private String src3;
    
    @Transient
    @JsonProperty
    private String fullSrc;
    

    @ManyToOne
    private UserPost userPost;

    public String getFullSrc() {
		return fullSrc;
	}

	public void setFullSrc(String fullSrc) {
		this.fullSrc = fullSrc;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc1() {
        return src1;
    }

    public void setSrc1(String src1) {
        this.src1 = src1;
    }

    public String getSrc2() {
        return src2;
    }

    public void setSrc2(String src2) {
        this.src2 = src2;
    }

    public String getSrc3() {
        return src3;
    }

    public void setSrc3(String src3) {
        this.src3 = src3;
    }

    public UserPost getUserPost() {
        return userPost;
    }

    public void setUserPost(UserPost userPost) {
        this.userPost = userPost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostImage postImage = (PostImage) o;

        if ( ! Objects.equals(id, postImage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PostImage{" +
                "id=" + id +
                ", src='" + src + "'" +
                ", src1='" + src1 + "'" +
                ", src2='" + src2 + "'" +
                ", src3='" + src3 + "'" +
                '}';
    }
}
