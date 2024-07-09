package com.sfz.rest_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LongIDResponse {
    Long id;
    public LongIDResponse(Long id){
        this.setId(id);
    }
}
