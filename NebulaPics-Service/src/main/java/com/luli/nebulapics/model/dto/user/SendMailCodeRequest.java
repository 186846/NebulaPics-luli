package com.luli.nebulapics.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendMailCodeRequest implements Serializable {
    private String email;
}
