package com.doubletuan.sns.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A ChatRoom.
 */
@Entity
@Table(name = "CHATROOM")
public class ChatRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatRoom chatRoom = (ChatRoom) o;

        if ( ! Objects.equals(id, chatRoom.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
