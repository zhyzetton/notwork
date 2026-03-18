package com.notwork.notwork_backend.mq.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlogEventMessage implements Serializable {
    private String type;   // CREATE UPDATE DELETE
    private Long blogId;
    private Long tagId;
}
