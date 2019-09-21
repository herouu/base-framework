package com.example.commons.entity.dto;

import lombok.Data;

@Data
public class SqlBusinessEventsDto {
    //操作人
    String operator;
    String operatorUUID;
    Long operatorId;
    //业务事件
    String eventName;
    String eventKey;
    String eventId;
}
