package cn.ideaswork.ideacoder.domain.sale.order;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import cn.ideaswork.ideacoder.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OrdersService {
    Orders saveOrders(Orders orders);

    List<Orders> getAllOrderss();

    Orders getOrdersById(final String id);

    Orders updateOrdersById(Orders orders, final String id);

    void deleteOrdersById(final String id);

    Boolean isOrdersExist(final String id);

    Page<Orders> getPageByCondition(OrdersDTO ordersDTO, Pageable pageable);

    List<Orders> getListByCondition(OrdersDTO ordersDTO);

    ResponseEntity<?> studentCreateOrder(CreateOrderDTO createOrderDTO, User user);

    ResponseEntity<?> getOrderStatus(String orderId, User loginUser);

    Boolean getIsHaveCourse(String courseId,User loginUser);

    ResponseEntity<?> createUnderLineOrder(CreateOrderDTO createOrderDTO, User loginUser);

    ResponseEntity<?> CreateSaleProductOrder(CreateOrderDTO createOrderDTO, User loginUser);
}
