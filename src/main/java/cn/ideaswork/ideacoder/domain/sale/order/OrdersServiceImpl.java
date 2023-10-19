package cn.ideaswork.ideacoder.domain.sale.order;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import cn.ideaswork.ideacoder.domain.lms.course.CourseService;
import cn.ideaswork.ideacoder.domain.sale.saleproduct.SaleProduct;
import cn.ideaswork.ideacoder.domain.sale.saleproduct.SaleProductService;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.UserService;
import cn.ideaswork.ideacoder.infrastructure.config.WxPayConfig;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.yungouos.pay.entity.PayOrder;
import com.yungouos.pay.order.SystemOrder;
import com.yungouos.pay.wxpay.WxPay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    SaleProductService saleProductService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Orders saveOrders(final Orders orders) {
        return ordersDao.save(orders);
    }

    @Override
    public List<Orders> getAllOrderss() {
        return ordersDao.findAll();
    }

    @Override
    public Orders getOrdersById(final String id) {
        return ordersDao.findById(id).orElse(new Orders());
    }

    @Override
    @Transactional
    public Orders updateOrdersById(final Orders orders, final String id) {
        Orders ordersDb = ordersDao.findById(id).orElse(new Orders());
        BeanUtils.copyProperties(orders, ordersDb);
        return ordersDao.save(ordersDb);
    }

    @Override
    @Transactional
    public void deleteOrdersById(final String id) {
        ordersDao.deleteById(id);
    }

    @Override
    public Boolean isOrdersExist(final String id) {
        return ordersDao.existsById(id);
    }

    @Override
    public Page<Orders> getPageByCondition(final OrdersDTO ordersDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(ordersDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(ordersDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getYgosorderid())) {
            Criteria criteria = Criteria.where("ygosorderid").is(ordersDTO.getYgosorderid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getPayno())) {
            Criteria criteria = Criteria.where("payno").is(ordersDTO.getPayno());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getShorderno())) {
            Criteria criteria = Criteria.where("shorderno").is(ordersDTO.getShorderno());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(ordersDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getOpenid())) {
            Criteria criteria = Criteria.where("openid").is(ordersDTO.getOpenid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getUsername())) {
            Criteria criteria = Criteria.where("username").is(ordersDTO.getUsername());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getPhone())) {
            Criteria criteria = Criteria.where("phone").is(ordersDTO.getPhone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").is(ordersDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getType())) {
            Criteria criteria = Criteria.where("type").is(ordersDTO.getType());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(ordersDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (ordersDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").gte(ordersDTO.getCzsj());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Orders.class), pageable);
    }

    @Override
    public List<Orders> getListByCondition(final OrdersDTO ordersDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(ordersDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(ordersDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getYgosorderid())) {
            Criteria criteria = Criteria.where("ygosorderid").is(ordersDTO.getYgosorderid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getPayno())) {
            Criteria criteria = Criteria.where("payno").is(ordersDTO.getPayno());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getShorderno())) {
            Criteria criteria = Criteria.where("shorderno").is(ordersDTO.getShorderno());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(ordersDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getOpenid())) {
            Criteria criteria = Criteria.where("openid").is(ordersDTO.getOpenid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getUsername())) {
            Criteria criteria = Criteria.where("username").is(ordersDTO.getUsername());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getPhone())) {
            Criteria criteria = Criteria.where("phone").is(ordersDTO.getPhone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").is(ordersDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getType())) {
            Criteria criteria = Criteria.where("type").is(ordersDTO.getType());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ordersDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(ordersDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (ordersDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").gte(ordersDTO.getCzsj());
            query.addCriteria(criteria);
        }
        // 获取当前年的订单
        if (ordersDTO.getYear() != null) {
            Criteria criteria = Criteria.where("czsj")
                    .gte(SysTools.getYearFirst(ordersDTO.getYear()))
                    .lte(SysTools.getYearLast(ordersDTO.getYear()));
            query.addCriteria(criteria);
        }

        // 根据日期倒排序
        query.with(Sort.by(Sort.Direction.DESC, "czsj"));
        return mongoTemplate.find(query, Orders.class);
    }

    @Override
    public ResponseEntity<?> studentCreateOrder(CreateOrderDTO createOrderDTO, User user) {
        String userId = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        createOrderDTO.setBlrId(userId);
        createOrderDTO.setBlrName(name);
        createOrderDTO.setUserid(userId);
        createOrderDTO.setUsername(name);

        List<OrderItem> items = createOrderDTO.getItems();
        String orderInfo = "";
        BigDecimal price = new BigDecimal(0);
        for (OrderItem item : items) {
            String type = item.getType();
            if (type.equals(OrderItem.ProductTypeEnum.LMS_ONLINE_COURSE.getCode())) {
                Course course = courseService.getCourseById(item.getProductId());
                price = price.add(course.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += course.getName() + " * " + item.getNum() + " ";
            } else if (type.equals(OrderItem.ProductTypeEnum.LMS_OFFLINE_COURSE.getCode())) {
                Course course = courseService.getCourseById(item.getProductId());
                price = price.add(course.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += course.getName() + " ";
            } else {
                return ResponseEntity.badRequest().body("没有该类型");
            }

        }

        // 根据商品类型和商品id获取商品详情  TODO 从数据库查询
        String goodsInfo = orderInfo; // 从数据库中获取的商品信息
        Orders order = new Orders();
        order.setId(UUID.randomUUID().toString());
        order.setInfo(goodsInfo);
        order.setCzsj(new Date());
        order.setStatus(Orders.OrderStatus.NEW.getCode());

        order.setUserid(user.getId());
        order.setType(Orders.OrderType.LMS.getCode());
        order.setUsername(user.getName());
        order.setItems(createOrderDTO.getItems());
        order.setMoney(price);
        order.setBlrId(createOrderDTO.getBlrId());
        order.setBlrName(createOrderDTO.getBlrName());
        order.setBz(createOrderDTO.getBz());
        order.setPayChannel(createOrderDTO.getPayType());
        order.setPhone(createOrderDTO.getPhone());
        order.setShippingStatus(Orders.ShippingStatus.UNDELIVERED.getCode());
        Orders orders = this.saveOrders(order);

        String attach = "";// 附带参数
        String body = orders.getInfo();
        String fee = orders.getMoney() + "";// 订单费用
        String out_trade_no = orders.getId();// 订单号 全局唯一  (目前设想用订单表id)
        String return_url = getCashierPayUrl(attach, body, fee, out_trade_no);

        Map<String, String> map = new HashMap<String, String>();
        map.put("url", return_url);
        map.put("orderId", order.getId());
        map.put("productId", order.getItems().get(0).getProductId());

        System.out.println(map.get("url"));
        System.out.println(map.get("orderId"));
        System.out.println(map.get("productId"));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<?> CreateSaleProductOrder(CreateOrderDTO createOrderDTO, User loginUser) {
        // 设置订单信息
        String userId = loginUser.getId();
        String name = loginUser.getName();
        String email = loginUser.getEmail();
        createOrderDTO.setBlrId(userId);
        createOrderDTO.setBlrName(name);
        createOrderDTO.setUserid(userId);
        createOrderDTO.setUsername(name);

        // 设置订单项信息
        List<OrderItem> items = createOrderDTO.getItems();
        String orderInfo = "";
        BigDecimal price = new BigDecimal(0);
        for (OrderItem item : items) {
            String type = item.getType();
            if (type.equals(OrderItem.ProductTypeEnum.LMS_ONLINE_COURSE.getCode())) {
                Course course = courseService.getCourseById(item.getProductId());
                price = price.add(course.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += course.getName() + " * " + item.getNum() + " ";
            } else if (type.equals(OrderItem.ProductTypeEnum.LMS_OFFLINE_COURSE.getCode())) {
                Course course = courseService.getCourseById(item.getProductId());
                price = price.add(course.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += course.getName() + " ";
            } else if (type.equals(OrderItem.ProductTypeEnum.VMS_SALE_PRODUCT.getCode())) {
                SaleProduct saleProduct = saleProductService.getSaleProductById(item.getProductId());
                price = price.add(saleProduct.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += saleProduct.getName() + " * " + item.getNum() + " ";
            } else {
                return ResponseEntity.badRequest().body("没有该类型");
            }
        }
        // 保存当前订单信息
        Orders order = new Orders();
        order.setId(UUID.randomUUID().toString());
        order.setInfo(orderInfo);
        order.setCzsj(new Date());
        order.setStatus(Orders.OrderStatus.NEW.getCode());
        order.setUserid(loginUser.getId());
        order.setType(Orders.OrderType.VMS.getCode());
        order.setUsername(loginUser.getName());
        order.setItems(createOrderDTO.getItems());
        order.setMoney(price);
        order.setBlrId(createOrderDTO.getBlrId());
        order.setBlrName(createOrderDTO.getBlrName());
        order.setBz(createOrderDTO.getBz());
        order.setPayChannel(createOrderDTO.getPayType());
        order.setPhone(createOrderDTO.getPhone());
        order.setShippingStatus(Orders.ShippingStatus.UNDELIVERED.getCode());
        Orders orders = this.saveOrders(order);

        // 调用支付接口
        String attach = "";// 附带参数
        String body = orders.getInfo();
        String fee = orders.getMoney() + "";// 订单费用
        String out_trade_no = orders.getId();// 订单号 全局唯一  (目前设想用订单表id)
        String return_url = getCashierPayUrl(attach, body, fee, out_trade_no);

        Map<String, String> map = new HashMap<String, String>();
        map.put("url", return_url);
        map.put("orderId", order.getId());
        map.put("productId", order.getItems().get(0).getProductId());

        System.out.println(map.get("url"));
        System.out.println(map.get("orderId"));
        System.out.println(map.get("productId"));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<?> getOrderStatus(String orderId, User loginUser) {
        Orders ordersById = this.getOrdersById(orderId);
        if (StringUtils.isBlank(ordersById.getId())) {
            return ResponseEntity.badRequest().body("没有找到你的订单，请联系站长解决");
        }

        String mchId = wxPayConfig.mchId;
        String key = wxPayConfig.key;

        // 查询第三方支付当前订单是否支付成功
        PayOrder payOrder = SystemOrder.getOrderInfoByOutTradeNo(orderId, mchId, key);
        String orderNo = payOrder.getOrderNo();
        String attach = payOrder.getAttach();
        String body = payOrder.getBody();
        String money = payOrder.getMoney();
        String outTradeNo = payOrder.getOutTradeNo(); // 自己系统的订单 id
        String payChannel = payOrder.getPayChannel();
        String payNo = payOrder.getPayNo();
        int payStatus = payOrder.getPayStatus();
        String payType = payOrder.getPayType();

        User userById = userService.getUserById(loginUser.getId());

        // 更新本地数据库订单信息
        ordersById.setStatus(payStatus + "")
                .setYgosorderid(orderNo)
                .setPayChannel(payType)
                .setPayno(payNo)
                .setAttach(attach)
                .setPayChannel(payChannel);

        // 如果订单未发货，且支付成功，更新订单状态为已收货
        if (ordersById.getShippingStatus().equals(Orders.ShippingStatus.UNDELIVERED.getCode())) {
            if (payStatus == 1) {
                // 给用户添加产品用量
                List<OrderItem> items = ordersById.getItems();
                for (OrderItem item : items) {
                    if (item.getType().equals(OrderItem.ProductTypeEnum.VMS_SALE_PRODUCT.getCode())) {
                        SaleProduct saleProduct = saleProductService.getSaleProductById(item.getProductId());
                        String classification = saleProduct.getClassification();
                        if (classification.equals("AI模型")) {
                            String name = saleProduct.getName();
                            // 提取name中的数字
                            String usageNumString = name.replaceAll("[^0-9]", "");
                            Integer num = item.getNum();
                            Integer usageNum = Integer.parseInt(usageNumString);
                            // 计算使用量
                            Integer usage = num * usageNum;
                            userById.setAiCount(userById.getAiCount() + usage);

                        } else if (classification.equals("语音生成")) {

                            String name = saleProduct.getName();
                            // 提取name中的数字
                            String usageNumString = name.replaceAll("[^0-9]", "");
                            Integer num = item.getNum();
                            Integer usageNum = Integer.parseInt(usageNumString);
                            // 计算使用量
                            Integer usage = num * usageNum;
                            userById.setVoiceCount(userById.getVoiceCount() + usage);
                        } else {
                            return ResponseEntity.badRequest().body("没有该类型");
                        }
                    } else if (item.getType().equals(OrderItem.ProductTypeEnum.LMS_ONLINE_COURSE.getCode())) {
                        List<String> myCourses = userById.getMyCourses();
                        if (myCourses == null) {
                            myCourses = new ArrayList<>();
                        }
                        Set<String> myCoursesSet = new HashSet<>(myCourses);
                        myCoursesSet.add(item.getProductId());
                        userById.setMyCourses(new ArrayList<>(myCoursesSet));
                        userService.updateUserById(userById, userById.getId());
                    } else if (item.getType().equals(OrderItem.ProductTypeEnum.LMS_OFFLINE_COURSE.getCode())) {
                        List<String> myCourses = userById.getMyCourses();
                        if (myCourses == null) {
                            myCourses = new ArrayList<>();
                        }
                        Set<String> myCoursesSet = new HashSet<>(myCourses);
                        myCoursesSet.add(item.getProductId());
                        myCoursesSet.add(item.getProductId());

                        userById.setMyCourses(new ArrayList<>(myCoursesSet));
                        userService.updateUserById(userById, userById.getId());

                    }
                    userService.saveUser(userById);
                }
            }
        }
        Orders order = this.updateOrdersById(ordersById, ordersById.getId());

        return ResponseEntity.ok(order);
    }

    @Override
    public Boolean getIsHaveCourse(String courseId, User loginUser) {
        List<Orders> ordersList = ordersDao.findAllByUserid(loginUser.getId());
        for (int i = 0; i < ordersList.size(); i++) {
            List<OrderItem> items = ordersList.get(i).getItems();
            for (OrderItem item : items) {
                if (item.getProductId().equals(courseId)) {
                    if (ordersList.get(i).getStatus().equals("1")) {
                        return true;
                    } else {
                        return false;
                    }

                }
            }

        }
        return false;
    }

    @Override
    public ResponseEntity<?> createUnderLineOrder(CreateOrderDTO createOrderDTO, User loginUser) {
        List<OrderItem> items = createOrderDTO.getItems();
        String orderInfo = "";
        BigDecimal price = new BigDecimal(0);
        for (OrderItem item : items) {
            String type = item.getType();
            if (type.equals(OrderItem.ProductTypeEnum.LMS_OFFLINE_COURSE.getCode())) {
                Course course = courseService.getCourseById(item.getProductId());
                price = price.add(course.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += course.getName() + " * " + item.getNum() + " ";
            } else if (type.equals(OrderItem.ProductTypeEnum.LMS_ONLINE_COURSE.getCode())) {
                Course course = courseService.getCourseById(item.getProductId());
                price = price.add(course.getPrice().multiply(new BigDecimal(item.getNum())));
                orderInfo += course.getName() + " ";
            } else {
                return ResponseEntity.badRequest().body("没有该类型");
            }
        }

        // 根据商品类型和商品id获取商品详情  TODO 从数据库查询
        String goodsInfo = orderInfo; // 从数据库中获取的商品信息


        Orders order = new Orders();
        order.setId(UUID.randomUUID().toString());
        order.setInfo(goodsInfo);
        order.setCzsj(new Date());
        order.setStatus(Orders.OrderStatus.NEW.getCode());

        order.setUserid(loginUser.getId());
        order.setType(Orders.OrderType.LMS.getCode());
        order.setUsername(loginUser.getName());
        order.setItems(createOrderDTO.getItems());
        order.setMoney(price);
        order.setBlrId(createOrderDTO.getBlrId());
        order.setBlrName(createOrderDTO.getBlrName());
        order.setBz(createOrderDTO.getBz());
        order.setPayChannel(createOrderDTO.getPayType());
        order.setInfo(goodsInfo);
        order.setPhone(createOrderDTO.getPhone());
        Orders orders = this.saveOrders(order);

        String attach = "";// 附带参数
        String body = orders.getInfo();
        String fee = orders.getMoney() + "";// 订单费用
        String out_trade_no = orders.getId();// 订单号 全局唯一  (目前设想用订单表id)
        String return_url = getCashierPayUrl(attach, body, fee, out_trade_no);

        Map<String, String> map = new HashMap<String, String>();
        map.put("url", return_url);
        map.put("orderId", order.getId());
        map.put("productId", order.getItems().get(0).getProductId());
        System.out.println(map.get("url"));
        System.out.println(map.get("orderId"));
        System.out.println(map.get("productId"));
        return ResponseEntity.ok(map);
    }


    /**
     * 获取微信收银台支付链接
     *
     * @param attach       同步回调要拼接的参数
     * @param body         订单的信息
     * @param fee          订单的价格
     * @param out_trade_no 订单id
     * @return
     */
    private String getCashierPayUrl(String attach, String body, String fee, String out_trade_no) {
        String notify_url = wxPayConfig.notify_url + "/api/callback/notify";// 支付完成后异步回调的地址
//        String return_url = wxPayConfig.return_url+"/hello";// 支付完成后跳转的地址  // 跳转到课程的页面
//        String return_url = "https://www.baidu.com";// 支付完成后跳转的地址
        String mchId = wxPayConfig.mchId;
        String key = wxPayConfig.key;
        String cashierPayUrl = WxPay.cashierPay(out_trade_no, fee, mchId, body, attach, notify_url, null, null, null, null, null, key);
        return cashierPayUrl;
    }


    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Orders mapToEntity(final OrdersDTO ordersDTO, final Orders orders) {
        BeanUtils.copyProperties(ordersDTO, orders);
        return orders;
    }

    public OrdersDTO mapToDTO(final Orders orders, final OrdersDTO ordersDTO) {
        BeanUtils.copyProperties(orders, ordersDTO);
        return ordersDTO;
    }
}