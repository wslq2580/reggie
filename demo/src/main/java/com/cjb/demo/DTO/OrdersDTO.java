package com.cjb.demo.DTO;


import com.cjb.demo.entity.OrderDetail;
import com.cjb.demo.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDTO extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
