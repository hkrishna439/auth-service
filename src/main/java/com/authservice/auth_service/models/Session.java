package com.authservice.auth_service.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@Entity
public class Session extends BaseModel{
    private String  token;
    private Date expiringAt;
    private SessionStatus sessionStatus;

    @ManyToOne
    private User user;
}
