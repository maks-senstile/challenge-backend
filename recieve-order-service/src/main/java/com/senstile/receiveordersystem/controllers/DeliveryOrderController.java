package com.senstile.receiveordersystem.controllers;

import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.services.DeliveryOrderService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.senstile.receiveordersystem.utils.ConstantsUtils.ADDRESS_ID;
import static com.senstile.receiveordersystem.utils.ConstantsUtils.EXECUTE_AT;
import static com.senstile.receiveordersystem.utils.ConstantsUtils.PRODUCT_IDS;
import static com.senstile.receiveordersystem.utils.ConstantsUtils.REQUIRED_PARAMS_ARE_MISSING;
import static com.senstile.receiveordersystem.utils.ConstantsUtils.USER_ID;


@AllArgsConstructor
@RestController
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    @PostMapping("/create-new-delivery-order")
    public ResponseEntity create(HttpServletRequest request) {
        String userId = request.getParameter(USER_ID);
        String addressId = request.getParameter(ADDRESS_ID);
        String executeAt = request.getParameter(EXECUTE_AT);
        String productIds = request.getParameter(PRODUCT_IDS);
        if (Strings.isBlank(userId) || Strings.isBlank(addressId) || Strings.isBlank(productIds) || Strings.isBlank(executeAt)) {
            return new ResponseEntity(REQUIRED_PARAMS_ARE_MISSING, HttpStatus.BAD_REQUEST);
        }
        return deliveryOrderService.executeLogic(userId, addressId, executeAt, productIds);
    }


    @GetMapping("/find-all-delivery-orders")
    public ResponseEntity<List<CommonDeliveryOrderDto>> findAll() {
        List<CommonDeliveryOrderDto> result = deliveryOrderService.collectAllAndReturn();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
