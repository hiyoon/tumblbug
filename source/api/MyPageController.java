package com.tbroad.tcommerce.api.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tbroad.tcommerce.api.core.component.support.EncryptType;
import com.tbroad.tcommerce.api.core.service.MyPageService;

/**
 * @author hiyoon
 *
 */
@Controller
public class MyPageController {

    /**
     * logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * menuLogger.
     */
    private final Logger menuLogger = LoggerFactory.getLogger("stat-menu");
    
    /**
     * MyPageService.
     */
    @Autowired
    private MyPageService service;
    
    /**
     * @param mediaCode mediaCode
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/default/{mediaCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getMyPage(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {
        
    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getMyPage param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "sessionId: " + sessionId 
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	return service.getMyPage(mediaCode, sessionId, currentPage, rowsPerPage);
    }

    /**
     * @param mediaCode mediaCode
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/coupons/{mediaCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getCoupons(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getCoupons param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "sessionId: " + sessionId 
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	menuLogger.info("{}|2|2|{}", mediaCode, "쿠폰함");
    	return service.getCoupons(mediaCode, sessionId, currentPage, rowsPerPage);
    }

    /**
     * @param mediaCode mediaCode
     * @param dateFlag dateFlag
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/orders/{mediaCode}/{dateFlag}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getOrders(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("dateFlag") final String dateFlag
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getOrders param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "dateFlag: " + dateFlag 
    				+ "sessionId: " + sessionId 
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	menuLogger.info("{}|2|4|{}", mediaCode, "주문/배송조회");
    	return service.getOrders(mediaCode, dateFlag, sessionId, currentPage, rowsPerPage);
    }
    
    /**
     * @param mediaCode mediaCode
     * @param orderNo orderNo
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/orders/detail/{mediaCode}/{orderNo}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getOrderDetail(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("orderNo") final String orderNo
    		, @PathVariable("sessionId") final String sessionId) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getOrderDetail param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "orderNo: " + orderNo 
    				+ "sessionId: " + sessionId);
    	}
    	return service.getOrderDetail(mediaCode, orderNo, sessionId, EncryptType.PLAIN_TEXT);
    }

    /**
     * @param mediaCode mediaCode
     * @param orderNo orderNo
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/s51/mypage/orders/detail/{mediaCode}/{orderNo}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getOrderDetailWithKisaSeed(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("orderNo") final String orderNo
    		, @PathVariable("sessionId") final String sessionId) {
    	
    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getOrderDetail param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "orderNo: " + orderNo 
    				+ "sessionId: " + sessionId);
    	}
    	return service.getOrderDetail(mediaCode, orderNo, sessionId, EncryptType.KISA_SEED);
    }

    /**
     * @param mediaCode mediaCode
     * @param dateFlag dateFlag
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/cancels/{mediaCode}/{dateFlag}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getCancels(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("dateFlag") final String dateFlag
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getCancels param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "dateFlag: " + dateFlag 
    				+ "sessionId: " + sessionId
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	menuLogger.info("{}|2|5|{}", mediaCode, "취소/반품/교환");
    	return service.getCancels(mediaCode, dateFlag, sessionId, currentPage, rowsPerPage);
    }

    /**
     * @param mediaCode mediaCode
     * @param orderNo orderNo
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/cancels/detail/{mediaCode}/{orderNo}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getCancelDetail(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("orderNo") final String orderNo
    		, @PathVariable("sessionId") final String sessionId) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getCancelDetail param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "orderNo: " + orderNo 
    				+ "sessionId: " + sessionId);
    	}
    	return service.getCancelDetail(mediaCode, orderNo, sessionId, EncryptType.PLAIN_TEXT);
    }

    /**
     * @param mediaCode mediaCode
     * @param orderNo orderNo
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/s51/mypage/cancels/detail/{mediaCode}/{orderNo}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getCancelDetailWithKisaSeed(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("orderNo") final String orderNo
    		, @PathVariable("sessionId") final String sessionId) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getCancelDetail param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "orderNo: " + orderNo 
    				+ "sessionId: " + sessionId);
    	}
    	return service.getCancelDetail(mediaCode, orderNo, sessionId, EncryptType.KISA_SEED);
    }

    /**
     * @param mediaCode mediaCode
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/save/{mediaCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getSave(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getCancelDetail param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "sessionId: " + sessionId
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	menuLogger.info("{}|2|3|{}", mediaCode, "적립금/예치금");
    	return service.getSave(mediaCode, sessionId, currentPage, rowsPerPage);
    }

    /**
     * @param mediaCode mediaCode
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/deposit/{mediaCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getDeposit(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getDeposit param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "sessionId: " + sessionId
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	menuLogger.info("{}|2|3|{}", mediaCode, "적립금/예치금");
    	return service.getDeposit(mediaCode, sessionId, currentPage, rowsPerPage);
    }

    /**
     * @param mediaCode mediaCode
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/mywishes/{mediaCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object getMyWishes(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "currentPage", required = false) final String currentPage
    		, @RequestParam(value = "rowsPerPage", required = false) final String rowsPerPage) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController getMyWishes param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "sessionId: " + sessionId
    				+ "currentPage: " + currentPage 
    				+ "rowsPerPage: " + rowsPerPage);
    	}
    	return service.getMyWishes(mediaCode, sessionId, currentPage, rowsPerPage);
    }

    /**
     * @param mediaCode mediaCode
     * @param goodsCode goodsCode
     * @param goodsdtCode goodsdtCode
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/mywishes/insert/{mediaCode}/{goodsCode}/{goodsdtCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object insertMyWish(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("goodsCode") final String goodsCode
    		, @PathVariable("goodsdtCode") final String goodsdtCode
    		, @PathVariable("sessionId") final String sessionId) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController insertMyWish param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "goodsCode: " + goodsCode 
    				+ "goodsdtCode: " + goodsdtCode
    				+ "sessionId: " + sessionId);
    	}
    	return service.insertMyWish(mediaCode, goodsCode, goodsdtCode, sessionId);
    }

    /**
     * @param mediaCode mediaCode
     * @param wishKey wishKey
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/mywishes/delete/{mediaCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object deleteMyWish(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("sessionId") final String sessionId
    		, @RequestParam(value = "wishKey", required = false) final String wishKey) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController deleteMyWish param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "wishKey: " + wishKey 
    				+ "sessionId: " + sessionId);
    	}
    	return service.deleteMyWish(mediaCode, wishKey, sessionId);
    }
    
    /**
     * @param mediaCode mediaCode
     * @param goodsCode goodsCode
     * @param sessionId sessionId
     * @return json object
     */
    @RequestMapping(value = "/tvapp/v1/mypage/mywishes/yn/{mediaCode}/{goodsCode}/{sessionId:.*}",
    		method = RequestMethod.GET)
    @ResponseBody
    public Object ynMyWish(@PathVariable("mediaCode") final String mediaCode
    		, @PathVariable("goodsCode") final String goodsCode
    		, @PathVariable("sessionId") final String sessionId) {

    	if (logger.isDebugEnabled()) {
    		logger.debug("Called MyPageController ynMyWish param:" 
    				+ "mediaCode: " + mediaCode 
    				+ "goodsCode: " + goodsCode 
    				+ "sessionId: " + sessionId);
    	}
    	return service.ynMyWish(mediaCode, goodsCode, sessionId);
    }
}
