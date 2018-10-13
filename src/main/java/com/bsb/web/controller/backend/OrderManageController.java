package com.bsb.web.controller.backend;

import com.bsb.common.ServerResponse;
import com.bsb.web.service.IOrderService;
import com.bsb.web.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;


    @GetMapping("/list")
    public ServerResponse<PageInfo> orderList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return orderService.manageList(pageNum, pageSize);
    }

    @GetMapping("/getDetail")
    public ServerResponse<OrderVo> getDetail(Long orderNo) {

        return orderService.manageDetail(orderNo);
    }

    @PostMapping("/search")
    public ServerResponse<PageInfo> search(Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return orderService.manageSearch(orderNo, pageNum, pageSize);

    }

    @GetMapping("/sendGoods")
    public ServerResponse<String> sendGoods(Long orderNo) {

        return orderService.manageSendGoods(orderNo);
    }
}
