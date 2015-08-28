package com.doubletuan.sns.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A OpenFireConfiguration.
 */
@Entity
@Table(name = "OPENFIRECONFIGURATION")
public class OpenFireConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "authentication_token")
    private String authenticationToken;

    @Column(name = "server_address")
    private String serverAddress;

    @Column(name = "rest_api_port")
    private String restApiPort;

    @Column(name = "identifier")
    private String identifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getRestApiPort() {
        return restApiPort;
    }

    public void setRestApiPort(String restApiPort) {
        this.restApiPort = restApiPort;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OpenFireConfiguration openFireConfiguration = (OpenFireConfiguration) o;

        if ( ! Objects.equals(id, openFireConfiguration.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OpenFireConfiguration{" +
                "id=" + id +
                ", authenticationToken='" + authenticationToken + "'" +
                ", serverAddress='" + serverAddress + "'" +
                ", restApiPort='" + restApiPort + "'" +
                ", identifier='" + identifier + "'" +
                '}';
    }
}
