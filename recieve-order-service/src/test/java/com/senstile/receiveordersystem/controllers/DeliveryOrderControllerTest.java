package com.senstile.receiveordersystem.controllers;

import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.services.DeliveryOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DeliveryOrderController.class)
class DeliveryOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryOrderService mockDeliveryOrderService;

    @Test
    void testCreate() throws Exception {
        final ResponseEntity responseEntity = new ResponseEntity<>("Required params are missing", HttpStatus.OK);

        when(mockDeliveryOrderService.executeLogic(anyString(), anyString(), anyString(), anyString())).thenReturn(responseEntity);

        final MockHttpServletResponse response = mockMvc.perform(post("/create-new-delivery-order")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testFindAll() throws Exception {

        final List<CommonDeliveryOrderDto> commonDeliveryOrderDtos = List.of(CommonDeliveryOrderDto.builder().build());
        when(mockDeliveryOrderService.collectAllAndReturn()).thenReturn(commonDeliveryOrderDtos);

        final MockHttpServletResponse response = mockMvc.perform(get("/find-all-delivery-orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testFindAll_DeliveryOrderServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockDeliveryOrderService.collectAllAndReturn()).thenReturn(Collections.emptyList());

        final MockHttpServletResponse response = mockMvc.perform(get("/find-all-delivery-orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
