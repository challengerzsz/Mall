package com.bsb.web.service.impls;

import com.bsb.common.ServerResponse;
import com.bsb.web.dao.IShippingMapper;
import com.bsb.web.pojo.Shipping;
import com.bsb.web.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zeng
 */
@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private IShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping) {

        shipping.setUserId(userId);

        int rowCount = shippingMapper.insertSelective(shipping);
        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功", result);
        }

        return ServerResponse.createByErrorMsg("新建地址失败");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId) {

        // TODO: 18-8-8 bug
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMsg("删除地址成功");
        }

        return ServerResponse.createByErrorMsg("删除地址失败");

    }

    public ServerResponse update(Integer userId, Shipping shipping) {

        shipping.setUserId(userId);

        int rowCount = shippingMapper.updateByShipping(shipping);

        System.out.println(userId + " " + shipping.getId());

        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }

        return ServerResponse.createByErrorMsg("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {

        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMsg("无法查询到改地址信息");
        }
        return ServerResponse.createBySuccess("查询地址成功", shipping);
    }

    public ServerResponse<PageInfo> list(int pageNum, int pageSize, Integer userId) {

        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
