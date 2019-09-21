package com.example.commons.entity;

import com.example.commons.entity.dto.SqlBusinessEventsDto;

public interface SqlBusinessEventsInterface {
    SqlBusinessEventsDto getCurrentSqlBusinessEventsDto();
    void removeCurrentSqlBusinessEventsDto();
}
