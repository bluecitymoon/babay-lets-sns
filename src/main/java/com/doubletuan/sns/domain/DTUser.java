package com.doubletuan.sns.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTUser.
 */
@Entity
@Table(name = "DTUSER")
public class DTUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "sign")
    private String sign;

    @Column(name = "birthday")
    private String birthday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DTUser dTUser = (DTUser) o;

        if ( ! Objects.equals(id, dTUser.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DTUser{" +
                "id=" + id +
                ", username='" + username + "'" +
                ", password='" + password + "'" +
                ", avatar='" + avatar + "'" +
                ", phone='" + phone + "'" +
                ", sign='" + sign + "'" +
                ", birthday='" + birthday + "'" +
                '}';
    }
}
