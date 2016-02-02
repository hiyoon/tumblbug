package com.tbroad.tcommerce.api.core.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tbroad.tcommerce.api.core.component.ApiPropertyComponent;
import com.tbroad.tcommerce.api.core.component.support.EncryptType;
import com.tbroad.tcommerce.api.core.jsonobj.CancelDetailView;
import com.tbroad.tcommerce.api.core.jsonobj.Deposit;
import com.tbroad.tcommerce.api.core.jsonobj.General;
import com.tbroad.tcommerce.api.core.jsonobj.MyCoupon;
import com.tbroad.tcommerce.api.core.jsonobj.MyOrder;
import com.tbroad.tcommerce.api.core.jsonobj.MyPage;
import com.tbroad.tcommerce.api.core.jsonobj.OrderCancel;
import com.tbroad.tcommerce.api.core.jsonobj.OrderDetailView;
import com.tbroad.tcommerce.api.core.jsonobj.SaveAmt;
import com.tbroad.tcommerce.api.core.jsonobj.WishYn;

/**
 * @author hchlee
 * 
 */
@Service
public class MyPageService {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * MY_MAIN_URL.
     */
    private static final String MY_MAIN_URL = "/tv/mypage/my-main"
    		+ "?mediaCode={mediaCode}&currentPage={currentPage}&rowsPerPage={rowsPerPage}";
    /**
     * MY_WISHE_LIST_URL.
     */
    private static final String MY_WISHE_LIST_URL = "/tv/mypage/my-main"
    		+ "?flag={flag}&mediaCode={mediaCode}&currentPage={currentPage}&rowsPerPage={rowsPerPage}";
	
    /**
     * MY_COUPON_LIST_URL.
     */
    private static final String MY_COUPON_LIST_URL = "/tv/mypage/my-coupon-list"
			+ "?mediaCode={mediaCode}&currentPage={currentPage}&rowsPerPage={rowsPerPage}";	
    /**
     * MY_ORDER_LIST_URL.
     */
    private static final String MY_ORDER_LIST_URL = "/tv/mypage/my-order-list"
			+ "?dateFlag={dateFlag}&currentPage={currentPage}"
			+ "&rowsPerPage={rowsPerPage}&mediaCode={mediaCode}";
    /**
     * MY_ORDER_DETAIL_URL.
     */
    private static final String MY_ORDER_DETAIL_URL = "/tv/mypage/my-order-view"
			+ "?orderNo={orderNo}&mediaCode={mediaCode}";	
    /**
     * MY_CANCEL_LIST_URL.
     */
    private static final String MY_CANCEL_LIST_URL = "/tv/mypage/my-cancel-list"
			+ "?dateFlag={dateFlag}&currentPage={currentPage}"
			+ "&rowsPerPage={rowsPerPage}&mediaCode={mediaCode}";
    /**
     * MY_CANCEL_DETAIL_URL.
     */
    private static final String MY_CANCEL_DETAIL_URL = "/tv/mypage/my-cancel-view"
			+ "?orderNo={orderNo}&mediaCode={mediaCode}";
    /**
     * MY_SAVE_LIST_URL.
     */
    private static final String MY_SAVE_LIST_URL = "/tv/mypage/my-saveamt-list"
			+ "?currentPage={currentPage}&rowsPerPage={rowsPerPage}&mediaCode={mediaCode}";		
    /**
     * MY_DEPOSIT_URL.
     */
    private static final String MY_DEPOSIT_URL = "/tv/mypage/my-deposit-list"
			+ "?currentPage={currentPage}&rowsPerPage={rowsPerPage}&mediaCode={mediaCode}";	
    /**
     * SAVE_WISH_URL.
     */
    private static final String SAVE_WISH_URL = "/tv/mypage/wish-save"
			+ "?goodsCode={goodsCode}&goodsdtCode={goodsdtCode}&mediaCode={mediaCode}";
    /**
     * DELETE_WISH_URL.
     */
    private static final String DELETE_WISH_URL = "/tv/mypage/wish-delete"
			+ "?wishKey={wishKey}&mediaCode={mediaCode}";	
    /**
     * YN_WISH_URL.
     */
    private static final String YN_WISH_URL = "/tv/mypage/wish-yn"
			+ "?goodsCode={goodsCode}&mediaCode={mediaCode}";
    /**
     * FLAG_WISH.
     */
    private static final String FLAG_WISH = "2";
        
    /**
     * HttpClient.
     */
    @Autowired
    private HttpClient client;
    
    /**
     * apiPropertyComponent.
     */
    @Autowired
    private ApiPropertyComponent apiPropertyComponent;

    /**
     * @param mediaCode mediaCode
     * @param sessionId sessionId
     * @param currentPage currentPage
     * @param rowsPerPage rowsPerPage
     * @return json object
     */
    public Object getMyPage(final String mediaCode, final String sessionId
    		, final String currentPage, final String rowsPerPage) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getMyPage " 
					+ "request url :" + MY_MAIN_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_MAIN_URL, params, sessionId);
		MyPage page = MyPage.fromJson(res);
		return page.toResJson(apiPropertyComponent.getBackofficeImgServerUrl(mediaCode));
	}

    /**
     * @param mediaCode mediaCode
	 * @param sessionId sessionId
	 * @param currentPage currentPage
	 * @param rowsPerPage rowsPerPage
     * @return json object
     */
    public Object getCoupons(final String mediaCode, final String sessionId
    		, final String currentPage, final String rowsPerPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getCoupons " 
					+ "request url :" + MY_COUPON_LIST_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_COUPON_LIST_URL, params, sessionId);
		MyCoupon coupon = MyCoupon.fromJson(res);
		return coupon.toResJson();
	}

	/**
	 * @param mediaCode mediaCode
	 * @param dateFlag dateFlag
	 * @param sessionId sessionId
	 * @param currentPage currentPage
	 * @param rowsPerPage rowsPerPage
	 * @return json object
	 */
	public Object getOrders(final String mediaCode, final String dateFlag, final String sessionId
			, final String currentPage, final String rowsPerPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateFlag", dateFlag);
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getOrders " 
					+ "request url :" + MY_ORDER_LIST_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_ORDER_LIST_URL, params, sessionId);
		MyOrder order = MyOrder.fromJson(res);
		return order.toResJson();
	}

	/**
	 * @param mediaCode mediaCode
	 * @param orderNo orderNo
	 * @param sessionId sessionId
	 * @param type encrypt type
	 * @return json object
	 */
	public Object getOrderDetail(final String mediaCode, final String orderNo
			, final String sessionId, final EncryptType type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("orderNo", orderNo);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getOrderDetail " 
					+ "request url :" + MY_ORDER_DETAIL_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_ORDER_DETAIL_URL, params, sessionId);
		OrderDetailView detail = OrderDetailView.fromJson(res);
		return detail.toResJsonWithEncrypt(type);
	}

	/**
	 * @param mediaCode mediaCode
	 * @param dateFlag dateFlag
	 * @param sessionId sessionId
	 * @param currentPage vcurrentPage
	 * @param rowsPerPage rowsPerPage
	 * @return json object
	 */
	public Object getCancels(final String mediaCode, final String dateFlag, final String sessionId
			, final String currentPage, final String rowsPerPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateFlag", dateFlag);
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getCancels " 
					+ "request url :" + MY_CANCEL_LIST_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_CANCEL_LIST_URL, params, sessionId);
		OrderCancel cancel = OrderCancel.fromJson(res);
		return cancel.toResJson();
	}

	/**
	 * @param mediaCode mediaCode
	 * @param orderNo orderNo
	 * @param sessionId sessionId
	 * @param type encrypt type
	 * @return json object
	 */
	public Object getCancelDetail(final String mediaCode, final String orderNo
			, final String sessionId, final EncryptType type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("orderNo", orderNo);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getCancelDetail " 
					+ "request url :" + MY_CANCEL_DETAIL_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_CANCEL_DETAIL_URL, params, sessionId);
		CancelDetailView detail = CancelDetailView.fromJson(res);
		return detail.toResJsonWithEncrypt(type);
	}

	/**
	 * @param mediaCode mediaCode
	 * @param sessionId 
	 * @param currentPage currentPage
	 * @param rowsPerPage rowsPerPage
	 * @return json object
	 */
	public Object getSave(final String mediaCode, final String sessionId
			, final String currentPage, final String rowsPerPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getSave " 
					+ "request url :" + MY_SAVE_LIST_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_SAVE_LIST_URL, params, sessionId);
		SaveAmt save = SaveAmt.fromJson(res);
		return save.toResJson();
	}

	/**
	 * @param mediaCode mediaCode
	 * @param sessionId sessionId
	 * @param currentPage currentPage
	 * @param rowsPerPage rowsPerPage
	 * @return json object
	 */
	public Object getDeposit(final String mediaCode, final String sessionId
			, final String currentPage, final String rowsPerPage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getDeposit " 
					+ "request url :" + MY_DEPOSIT_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_DEPOSIT_URL, params, sessionId);
		Deposit deposit = Deposit.fromJson(res);
		return deposit.toResJson();
	}

	/**
	 * @param mediaCode mediaCode
	 * @param sessionId sessionId
	 * @param currentPage currentPage
	 * @param rowsPerPage rowsPerPage
	 * @return json object
	 */
	public Object getMyWishes(final String mediaCode
			, final String sessionId, final String currentPage, final String rowsPerPage) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", FLAG_WISH);
		params.put("mediaCode", mediaCode);
		params.put("currentPage", currentPage);
		params.put("rowsPerPage", rowsPerPage);

		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService getMyWishes " 
					+ "request url :" + MY_WISHE_LIST_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(MY_WISHE_LIST_URL, params, sessionId);
		MyPage page = MyPage.fromJson(res);
		return page.toResJson(apiPropertyComponent.getBackofficeImgServerUrl(mediaCode));
	}

	/**
	 * @param mediaCode mediaCode
	 * @param goodsCode goodsCode
	 * @param goodsdtCode goodsdtCode
	 * @param sessionId sessionId
	 * @return json object
	 */
	public Object insertMyWish(final String mediaCode, final String goodsCode, final String goodsdtCode
			, final String sessionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("goodsCode", goodsCode);
		params.put("goodsdtCode", goodsdtCode);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService insertMyWish " 
					+ "request url :" + SAVE_WISH_URL
					+ "params: " + params.toString());
		}
		String res = client.postExchange(SAVE_WISH_URL, params, sessionId);
		General general = General.fromJson(res);
		return general.toResJson();
	}

	/**
	 * @param mediaCode mediaCode
	 * @param wishKey wishKey
	 * @param sessionId sessionId
	 * @return json object
	 */
	public Object deleteMyWish(final String mediaCode, final String wishKey
			, final String sessionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("wishKey", wishKey);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService deleteMyWish " 
					+ "request url :" + DELETE_WISH_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(DELETE_WISH_URL, params, sessionId);
		General general = General.fromJson(res);
		return general.toResJson();
	}
	
	/**
	 * @param mediaCode mediaCode
	 * @param goodsCode goodsCode
	 * @param sessionId sessionId
	 * @return json object
	 */
	public Object ynMyWish(final String mediaCode, final String goodsCode
			, final String sessionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mediaCode", mediaCode);
		params.put("goodsCode", goodsCode);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Called MyPageService ynMyWish " 
					+ "request url :" + YN_WISH_URL
					+ "params: " + params.toString());
		}
		String res = client.getExchange(YN_WISH_URL, params, sessionId);
		WishYn myWish = WishYn.fromJson(res);
		return myWish.toResJson();
	}
}
