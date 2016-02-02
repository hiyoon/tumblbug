package com.ns.ctcom.web.validator;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ns.ctcom.core.component.support.StatusType;
import com.ns.ctcom.core.domain.EventDomain;
import com.ns.ctcom.core.mapper.domain.EventRewardTb;
import com.ns.ctcom.core.mapper.domain.EventRnkRewardTb;
import com.ns.ctcom.web.command.EventCommand;

/** 
 * 이벤트 관리.
 * 
 * <p>Copyright (c) 1997-2013 Alticast, Inc. All rights reserved.
 *
 * @since	1.0
 * @author	hiyoon - 2015. 10. 16.
 */
@Component
public class EventValidator implements Validator {

    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /** DB 컬럼 입력 길이 50. */
    @Value("${size.db.column50}")
    private Integer sizeDbColumn50;
    
    /** DB 컬럼 입력 길이 200. */
    @Value("${size.db.column200}")
    private Integer sizeDbColumn200;
    
    @Override
    public final boolean supports(final Class<?> clazz) {
        return EventCommand.class.equals(clazz);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public final void validate(final Object target, final Errors errors) {
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventValidator");
        }
    	
    	EventCommand command = (EventCommand) target;
    	
    	// 플랫폼
		if (command.getStatusType() == StatusType.INSERT) {
	    	if (command.getArrPlfCd() == null || command.getArrPlfCd().length < 1) {
	    		errors.rejectValue("arrPlfCd", null, "플랫폼을 선택해주세요.");
	    	}
		}
    	
    	// 제목
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventTitle", null, "제목을 입력해주세요.");
    	if (command.getEventTitle().length() > sizeDbColumn50) {
            errors.rejectValue("eventTitle", null, "제목은 " + sizeDbColumn50 + "자 이하로 입력 하여 주십시오.");
        }
    	
    	// 이벤트 타입
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventType", null, "이벤트 타입을 입력해주세요.");
    	
    	// 롱배너
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bnrDisplayYn", null, "롱배너 노출여부를 선택해주세요.");
    	if (StringUtils.hasLength(command.getBnrDisplayYn())
    			&& command.getBnrDisplayYn().equals("1")) {
    		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bnrImgRealFileRte", null, "롱배너 이미지를 등록해주세요.");
    		if (!StringUtils.hasLength(command.getBnrImgRealFileRte())) {
    			errors.rejectValue("bnrDisplayYn", null, "롱배너 이미지가 등록된 경우에만 노출함을 선택할 수 있습니다.");
    		}
    	}
    	
    	// 리스트제목
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lstDescUseYn", null, "리스트제목 타입을 선택해주세요.");
    	if (StringUtils.hasLength(command.getLstDescUseYn())) {
			if (command.getLstDescUseYn().equals("1")) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lstDesc", null, "리스트제목을 입력해주세요.");
				if (StringUtils.hasLength(command.getLstDesc()) 
		    			&& command.getLstDesc().length() > sizeDbColumn200) {
		            errors.rejectValue("lstDesc", null, "리스트제목은 " + sizeDbColumn200 + "자 이하로 입력 하여 주십시오.");
		        }
			} else if (command.getLstDescUseYn().equals("0")) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lstImgFileRte", null, "리스트제목 이미지를 등록해주세요.");
			}
    	}
    	
    	// 내용
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contDescUseYn", null, "내용 타입을 선택해주세요.");
    	if (StringUtils.hasLength(command.getContDescUseYn())) {
			if (command.getContDescUseYn().equals("1")) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contDesc", null, "내용을 입력해주세요.");
			} else if (command.getContDescUseYn().equals("0")) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contImgFileRte", null, "내용 이미지를 등록해주세요.");
			}
    	}
    	
    	String eventType = command.getEventType();
    	if (StringUtils.hasLength(eventType)) {
    		if (eventType.equals("21")) {
	    		// 추첨방식
	        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventTypeInfo01", null, "추첨방식을 선택해주세요.");
	    		
	        	// 보상타입
	        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rewardType", null, "보상타입을 선택해주세요.");
	    		
	        	// 제공순위
	        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventRnkRewardList", null, "상세정보를 모두 입력해주세요.");
	        	int ofrRnk = Integer.valueOf(command.getOfrRnk());
	        	for (EventRnkRewardTb eventRnkReward : command.getEventRnkRewardList()) {
					if (eventRnkReward.getOfrRnk().compareTo(new BigDecimal(ofrRnk + 1)) == -1) {
						if (!StringUtils.hasLength(eventRnkReward.getEventRewardInfo())) {
							errors.rejectValue("eventRnkRewardList", null, "상세 정보를 모두 입력해주세요.");
						}
						break;
					}
				}
	        	
	        	// 추첨 공식
	        	if (command.getEventTypeInfo01() != null 
	        			&& command.getEventTypeInfo01().compareTo(BigDecimal.ZERO) == 0) {
		        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eventRewardList", null, "상세정보를 모두 입력해주세요.");
		    		if (command.getEventRewardList() != null) {
		    			for (EventRewardTb eventReward : command.getEventRewardList()) {
		    				if (eventReward.getWinRateFomular() == null
		    						|| eventReward.getMaxWinPerson() == null) {
								errors.rejectValue("eventRewardList", null, "상세 정보를 모두 입력해주세요.");
							}
							break;
						}
		    		}
	        	}
	    	} else if (eventType.equals("24")) {
	        	
	        	// 상품정보
	    		String[] arrPlfCd = command.getArrPlfCd();
	    		if (arrPlfCd != null && arrPlfCd.length > 0) {
	    			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "goodList", null, "상품을 등록해주세요.");
	    			if (command.getGoodList() != null 
	    					&& command.getGoodList().size() > 0) {
	    				for (String plfCd : arrPlfCd) {
	    					for (EventDomain goodInfo : command.getGoodList()) {
	    						if (plfCd.equals(goodInfo.getPlfCd())) {
	    							if (!StringUtils.hasLength(goodInfo.getSectionId())
	    									|| !StringUtils.hasLength(goodInfo.getGoodId())) {
	    								errors.rejectValue("goodList", null, "선택한 플랫폼의 상품 정보를 모두 입력해주세요.");
	    							}
	    							break;
	    						}
	    					}
						}
	    			}
	    		}
	    	}
    	}
    	
    	
    	if (errors.hasErrors()) {
    		logger.error("Error occurred from PvtInfoTermValidator." + errors.toString());
        }
    	
    }

}
