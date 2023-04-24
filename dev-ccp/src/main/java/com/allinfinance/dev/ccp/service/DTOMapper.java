package com.allinfinance.dev.ccp.service;

import com.allinfinance.dev.common.http.api.dto.HttpRequestDTO;
import com.allinfinance.dev.common.http.api.dto.HttpResponseDTO;
import com.allinfinance.dev.framework.http.driver.dto.HttpRequest;
import com.allinfinance.dev.framework.http.driver.dto.HttpResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author qipeng
 * @date 2022/9/7 19:23
 * @desc
 */
@Mapper
public interface DTOMapper {
    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    HttpRequest convertToHttpRequest(HttpRequestDTO requestDTO);

    @Mapping(source = "response", target = "body")
    HttpResponseDTO convertToHttpResponseDTO(HttpResponse response);
}
