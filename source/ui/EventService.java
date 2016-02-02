package com.ns.ctcom.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ns.ctcom.core.component.exception.ServiceException;
import com.ns.ctcom.core.dao.CommonPopupDao;
import com.ns.ctcom.core.dao.EventDao;
import com.ns.ctcom.core.domain.CtcomRegGdDomain;
import com.ns.ctcom.core.domain.EventDomain;
import com.ns.ctcom.core.domain.GoodDetailBasNew;
import com.ns.ctcom.core.domain.PlatformDomain;
import com.ns.ctcom.core.domain.SearchMainMenuDomain;
import com.ns.ctcom.core.mapper.EventRewardTbMapper;
import com.ns.ctcom.core.mapper.EventRnkRewardTbMapper;
import com.ns.ctcom.core.mapper.EventTbMapper;
import com.ns.ctcom.core.mapper.domain.EventRewardTb;
import com.ns.ctcom.core.mapper.domain.EventRewardTbExample;
import com.ns.ctcom.core.mapper.domain.EventRnkRewardTb;
import com.ns.ctcom.core.mapper.domain.EventRnkRewardTbExample;
import com.ns.ctcom.core.mapper.domain.EventTb;
import com.ns.ctcom.core.mapper.domain.EventTbExample;
import com.ns.ctcom.core.service.support.ResultType;
import com.ns.ctcom.core.util.AuthenticationUtils;
import com.ns.ctcom.core.util.Utils;

/** 
 * 이벤트 관리.
 * 
 * <p>Copyright (c) 1997-2013 Alticast, Inc. All rights reserved.
 *
 * @since	1.0
 * @author	hiyoon - 2015. 10. 16.
 */
@Service 
public class EventService {

    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /** variable eventDao.*/
    @Autowired
    private EventDao eventDao;
    
    /** variable eventTbMapper.*/
    @Autowired
    private EventTbMapper eventTbMapper;
    
    /** variable eventRnkRewardTbMapper.*/
    @Autowired
    private EventRnkRewardTbMapper eventRnkRewardTbMapper;
    
    /** variable eventRewardTbMapper.*/
    @Autowired
    private EventRewardTbMapper eventRewardTbMapper;
    
    /** variable localDefaultPath.*/
    @Value("${local.default.path}")
    private String localDefaultPath;
    
    /** variable mainMenuRelativePath.*/
    @Value("${local.event.relative.path}")
    private String eventRelativePath;
    
    /** variable commonPopupDao.*/
    @Autowired
    private CommonPopupDao commonPopupDao;
    
    /**
     * 이벤트 목록을 가져온다.
     * @param eventDomain PvtInfoTermDomain.
     * @return 이벤트 목록.
     */
    @Transactional(readOnly = true)
    public List<EventDomain> getEventList(final EventDomain eventDomain) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventService getEventList."
                    + " eventDomain: " + eventDomain.toString());
        }
    	return eventDao.selectEventList(eventDomain);
    }
    
    /**
     * 이벤트 목록 총 개수를 가져옵니다.
     * @param eventDomain PvtInfoTermDomain.
     * @return 이벤트 목록 총 개수.
     */
    @Transactional(readOnly = true)
    public Integer getEventListTotalCount(final EventDomain eventDomain) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventService getEventListTotalCount."
                    + " eventDomain: " + eventDomain.toString());
        }
    	return eventDao.selectEventListTotalCount(eventDomain);
    }
    
    /**
     * 이벤트 정보를 가져옵니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 6.
     * @param eventDomain eventDomain
     * @return 이벤트 정보
     */
    @Transactional(readOnly = true)
    public EventDomain getEventInfo(final EventDomain eventDomain) {
    	EventDomain eventInfo =  eventDao.selectEventInfo(eventDomain);
    	if (eventInfo.getEventType().equals("24")) {
	    	SearchMainMenuDomain searchMainMenuDomain = new SearchMainMenuDomain();
	    	searchMainMenuDomain.setPlfCd(eventInfo.getPlfCd());
	    	searchMainMenuDomain.setSectionId(eventInfo.getEventTypeInfo01().toString());
	    	searchMainMenuDomain.setGoodId(eventInfo.getEventTypeInfo02());
	    	//2015.11.23 기간계 Good_detail_bas_new문제로 join부분 제외후 서브 조회로 변경
	    	CtcomRegGdDomain ctcomRegGdDomain = commonPopupDao.selectGoodInfo(searchMainMenuDomain);
	    	if (ctcomRegGdDomain != null) {
	        	GoodDetailBasNew goodDetailBasNew = commonPopupDao.getGoodDetailBas(Integer.parseInt(ctcomRegGdDomain.getGoodId()));
	    		if (goodDetailBasNew != null) {
	    			ctcomRegGdDomain.setGoodCd(goodDetailBasNew.getGoodCd());
	        		ctcomRegGdDomain.setPrice(new BigDecimal(goodDetailBasNew.getPrc()));
	        		ctcomRegGdDomain.setDcPrice(new BigDecimal(goodDetailBasNew.getDcPrc()));
	    		} else {
	    			ctcomRegGdDomain.setGoodCd("0");
	    			ctcomRegGdDomain.setPrice(BigDecimal.ZERO);
	    			ctcomRegGdDomain.setDcPrice(BigDecimal.ZERO);
	    		}
	    		eventInfo.setSectionNm(ctcomRegGdDomain.getSectionName());
	    		eventInfo.setGoodNm(ctcomRegGdDomain.getGoodName());
	        }
    	}
    	return eventInfo;
    }
    
    /**
     * 이벤트 보상목록을 가져옵니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 6.
     * @param eventDomain eventDomain.
     * @return 이벤트 보상 목록.
     */
    @Transactional(readOnly = true)
    public List<EventRnkRewardTb> getEventRnkRewardList(final EventDomain eventDomain) {
    	EventRnkRewardTbExample eventRnkRewardTbExample = new EventRnkRewardTbExample();
    	eventRnkRewardTbExample.createCriteria()
    		.andPlfCdEqualTo(eventDomain.getPlfCd())
    		.andEventIdEqualTo(eventDomain.getEventId())
    		.andEventTypeEqualTo(eventDomain.getEventType());
    	eventRnkRewardTbExample.setOrderByClause("OFR_RNK");
    	return eventRnkRewardTbMapper.selectByExample(eventRnkRewardTbExample);
    }

    /**
     * 이벤트 보상 공식 목록을 가져옵니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 6.
     * @param eventDomain eventDomain
     * @return 이벤트 보상 공식 목록.
     */
    public List<EventRewardTb> getEventRewardList(final EventDomain eventDomain) {
    	EventRewardTbExample eventRewardTbExample = new EventRewardTbExample();
    	eventRewardTbExample.createCriteria()
    		.andPlfCdEqualTo(eventDomain.getPlfCd())
    		.andEventIdEqualTo(eventDomain.getEventId())
    		.andEventTypeEqualTo(eventDomain.getEventType());
    	eventRewardTbExample.setOrderByClause("EVENT_DATE, OFR_RNK");
    	return eventRewardTbMapper.selectByExample(eventRewardTbExample);
    }
    
    
    /**
     * 이벤트를 삭제합니다..
     * @param eventDomain eventDomain.
     * @return result.
     */
    @Transactional(readOnly = false)
    public ResultType deleteEvent(final EventDomain eventDomain) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventService deleteEvent."
            		+ " eventDomain: " + eventDomain.toString());
        }

    	//지역변수
    	ResultType resultType = ResultType.FAIL;
    	
    	try {
	    	String[] arrEventId = eventDomain.getArrEventId();
	    	EventTbExample eventTbExample = new EventTbExample();
	    	for (String eventId : arrEventId) {
	    		eventTbExample.or(new EventTbExample().createCriteria().andEventIdEqualTo(eventId));
			}
	    	
	    	EventTb eventTb = new EventTb();
	    	eventTb.setUseYn("0");
	    	eventTbMapper.updateByExampleSelective(eventTb, eventTbExample);
			
	        // 결과값
	        resultType = ResultType.SUCCESS;
		} catch (Exception e) {
		    logger.error(Utils.getStacktraceString(e));
		    throw new ServiceException(e.getMessage(), e);
		}
		
		return resultType;
    }
    
    /**
     * 이벤트 노출 여부 및 노출 순서를 저장합니다..
     * @param eventList eventList.
     * @param plfList 플랫폼 목록.
     * @return result.
     */
    @Transactional(readOnly = false)
    public ResultType saveEvent(final List<EventTb> eventList, final List<PlatformDomain> plfList) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventService saveEvent."
            		+ " eventList: " + ObjectUtils.toString(eventList));
        }

    	//지역변수
    	ResultType resultType = ResultType.FAIL;
    	Integer idx = 1;
    	
    	try {
	    	for (PlatformDomain platformDomain : plfList) {
	    		idx = 1;
				for (EventTb event : eventList) {
					if (platformDomain.getPlfCd().equals(event.getPlfCd())) {
						event.setDisplayOrder(new BigDecimal(idx));
						event.setDisplayYn(event.getDisplayYn());
						if (StringUtils.hasLength(event.getBnrDisplayYn())) {
							event.setBnrDisplayYn(event.getBnrDisplayYn());
						}
						EventTbExample eventTbExample = new EventTbExample();
						eventTbExample.createCriteria().andEventIdEqualTo(event.getEventId());
						eventTbMapper.updateByExampleSelective(event, eventTbExample);
						idx++;
					}
				}
			}
			
	        // 결과값
	        resultType = ResultType.SUCCESS;
		} catch (Exception e) {
		    logger.error(Utils.getStacktraceString(e));
		    throw new ServiceException(e.getMessage(), e);
		}
		
		return resultType;
    }
    
    /**
     * 이벤트를 등록합니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 5.
     * @param eventDomain eventDomain.
     * @return 등록 결과.
     */
    @Transactional(readOnly = false)
    public ResultType addEvent(final EventDomain eventDomain) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventService addEvent."
            		+ " eventDomain: " + eventDomain.toString());
        }
    	
    	//지역변수
    	ResultType resultType = ResultType.FAIL;
    	String[] arrPlfCd = eventDomain.getArrPlfCd();
    	String acctId = AuthenticationUtils.getCurrentUser().getAcctId();
    	Date date = new Date();
    	
    	try {
    		for (String plfCd : arrPlfCd) {
    			if (StringUtils.hasLength(plfCd)) {
    				String nextEventId = eventDao.selectMaxEventId();
					EventTb eventTb = new EventTb();
					eventTb.setPlfCd(plfCd);
					eventTb.setEventId(nextEventId);
					eventTb.setEventType(eventDomain.getEventType());
					eventTb.setEventTitle(eventDomain.getEventTitle());
					eventTb.setEventStartDate(eventDomain.getEventStartDate());
					eventTb.setEventStartHour(eventDomain.getEventStartHour());
					eventTb.setEventEndDate(eventDomain.getEventEndDate());
					eventTb.setEventEndHour(eventDomain.getEventEndHour());
					eventTb.setDisplayOrder(eventDao.selectMaxDisplayOrder(plfCd));
					
					// 롱배너 이미지
					eventTb.setBnrDisplayYn(eventDomain.getBnrDisplayYn());
					if (StringUtils.hasLength(eventDomain.getBnrImgRealFileRte())) {
						String bnrImgRealFileNm = Utils.writeImageFile(eventDomain.getBnrImgRealFileRte(), localDefaultPath, eventRelativePath);
						eventTb.setBnrImgRealFileRte("/" + eventRelativePath + bnrImgRealFileNm);
						eventTb.setBnrImgRealFileNm(bnrImgRealFileNm);
		    		}
					
					// 리스트 제목
					eventTb.setLstDescUseYn(eventDomain.getLstDescUseYn());
					if (StringUtils.hasLength(eventDomain.getLstDescUseYn())
		    				&& eventDomain.getLstDescUseYn().equals("0")) {
						String lstImgFileName = Utils.writeImageFile(eventDomain.getLstImgFileRte(), localDefaultPath, eventRelativePath);
						eventTb.setLstImgFileRte("/" + eventRelativePath + lstImgFileName);
						eventTb.setLstImgRealFileNm(lstImgFileName);
		    		} else {
		    			eventTb.setLstDesc(eventDomain.getLstDesc());
		    		}
					
					// 내용
					eventTb.setContDescUseYn(eventDomain.getContDescUseYn());
					if (StringUtils.hasLength(eventDomain.getContDescUseYn())
		    				&& eventDomain.getContDescUseYn().equals("0")) {
						String contImgFileNm = Utils.writeImageFile(eventDomain.getContImgFileRte(), localDefaultPath, eventRelativePath);
						eventTb.setContImgFileRte("/" + eventRelativePath + contImgFileNm);
						eventTb.setContImgRealFileNm(contImgFileNm);
		    		} else {
		    			eventTb.setContDesc(eventDomain.getContDesc());
		    		}
					
					if (eventDomain.getEventType().equals("21")) { // 참여형인경우.
						eventTb.setEventTypeInfo01(eventDomain.getEventTypeInfo01());
					} else if (eventDomain.getEventType().equals("24")) { // 구매형인경우.
						for (EventDomain goodInfo : eventDomain.getGoodList()) {
							if (goodInfo.getPlfCd().equals(plfCd)) {
								eventTb.setEventTypeInfo01(new BigDecimal(goodInfo.getSectionId()));
								eventTb.setEventTypeInfo02(goodInfo.getGoodId());
								break;
							}
						}
					}
					eventTb.setUseYn("1");
					eventTb.setRegAcctId(acctId);
					eventTb.setRegDate(date);
					eventTb.setModAcctId(acctId);
					eventTb.setModDate(date);
					eventTb.setDisplayYn(eventDomain.getDisplayYn());
					eventTbMapper.insert(eventTb);
					
					if (eventDomain.getEventType().equals("21")) {
						insertEventRewardTb(plfCd, nextEventId, eventDomain);
					}
    			}
			}
    		
	        // 결과값
	        resultType = ResultType.SUCCESS;
    	} catch (Exception e) {
		    logger.error(Utils.getStacktraceString(e));
		    throw new ServiceException(e.getMessage(), e);
		}
    	
    	return resultType;
    }
    
    /**
     * 이벤트를 등록합니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 5.
     * @param eventDomain eventDomain.
     * @return 등록 결과.
     */
    @Transactional(readOnly = false)
    public ResultType editEvent(final EventDomain eventDomain) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Called EventService addEvent."
            		+ " eventDomain: " + eventDomain.toString());
        }
    	
    	//지역변수
    	ResultType resultType = ResultType.FAIL;
    	String acctId = AuthenticationUtils.getCurrentUser().getAcctId();
    	Date date = new Date();
    	String plfCd = eventDomain.getPlfCd();
    	String eventId = eventDomain.getEventId();
    	
    	try {
			EventTb eventTb = new EventTb();
			eventTb.setPlfCd(plfCd);
			eventTb.setEventId(eventId);
			eventTb.setEventType(eventDomain.getEventType());
			eventTb.setEventTitle(eventDomain.getEventTitle());
			eventTb.setEventStartDate(eventDomain.getEventStartDate());
			eventTb.setEventStartHour(eventDomain.getEventStartHour());
			eventTb.setEventEndDate(eventDomain.getEventEndDate());
			eventTb.setEventEndHour(eventDomain.getEventEndHour());
			eventTb.setDisplayOrder(eventDomain.getDisplayOrder());
			
			// 롱배너 이미지
			eventTb.setBnrDisplayYn(eventDomain.getBnrDisplayYn());
			if (StringUtils.hasLength(eventDomain.getBnrUrlImgChangeFlag())
    				&& eventDomain.getBnrUrlImgChangeFlag().equals("1")) {
				if (StringUtils.hasLength(eventDomain.getBnrImgRealFileRte())) {
					String bnrImgRealFileNm = Utils.writeImageFile(eventDomain.getBnrImgRealFileRte(), localDefaultPath, eventRelativePath);
					eventTb.setBnrImgRealFileRte("/" + eventRelativePath + bnrImgRealFileNm);
					eventTb.setBnrImgRealFileNm(bnrImgRealFileNm);
				} else {
					eventTb.setBnrImgRealFileRte("");
					eventTb.setBnrImgRealFileNm("");
				}
			}
			
			// 리스트 제목
			eventTb.setLstDescUseYn(eventDomain.getLstDescUseYn());
			if (StringUtils.hasLength(eventDomain.getLstDescUseYn())
    				&& eventDomain.getLstDescUseYn().equals("0")) {
				if (StringUtils.hasLength(eventDomain.getLstImgUrlImgChangeFlag())
	    				&& eventDomain.getLstImgUrlImgChangeFlag().equals("1")) {
					if (StringUtils.hasLength(eventDomain.getLstImgFileRte())) {
						String lstImgFileName = Utils.writeImageFile(eventDomain.getLstImgFileRte(), localDefaultPath, eventRelativePath);
						eventTb.setLstImgFileRte("/" + eventRelativePath + lstImgFileName);
						eventTb.setLstImgRealFileNm(lstImgFileName);
					} else {
						eventTb.setLstImgFileRte("");
						eventTb.setLstImgRealFileNm("");
					}
				}
    		} else {
    			eventTb.setLstImgFileRte("");
				eventTb.setLstImgRealFileNm("");
    			eventTb.setLstDesc(eventDomain.getLstDesc());
    		}
			
			// 내용
			eventTb.setContDescUseYn(eventDomain.getContDescUseYn());
			if (StringUtils.hasLength(eventDomain.getContDescUseYn())
    				&& eventDomain.getContDescUseYn().equals("0")) {
				if (StringUtils.hasLength(eventDomain.getContUrlImgChangeFlag())
	    				&& eventDomain.getContUrlImgChangeFlag().equals("1")) {
					if (StringUtils.hasLength(eventDomain.getContImgFileRte())) {
	 					String contImgFileNm = Utils.writeImageFile(eventDomain.getContImgFileRte(), localDefaultPath, eventRelativePath);
						eventTb.setContImgFileRte("/" + eventRelativePath + contImgFileNm);
						eventTb.setContImgRealFileNm(contImgFileNm);
					} else {
						eventTb.setContImgFileRte("");
						eventTb.setContImgRealFileNm("");
					}
				}
    		} else {
    			eventTb.setContImgFileRte("");
				eventTb.setContImgRealFileNm("");
    			eventTb.setContDesc(eventDomain.getContDesc());
    		}
			
			if (eventDomain.getEventType().equals("21")) { // 참여형인경우.
				eventTb.setEventTypeInfo01(eventDomain.getEventTypeInfo01());
			} else if (eventDomain.getEventType().equals("24")) { // 구매형인경우.
				for (EventDomain goodInfo : eventDomain.getGoodList()) {
					if (goodInfo.getPlfCd().equals(plfCd)) {
						eventTb.setEventTypeInfo01(new BigDecimal(goodInfo.getSectionId()));
						eventTb.setEventTypeInfo02(goodInfo.getGoodId());
						break;
					}
				}
			}
			eventTb.setModAcctId(acctId);
			eventTb.setModDate(date);
			eventTb.setDisplayYn(eventDomain.getDisplayYn());
			eventTbMapper.updateByPrimaryKeySelective(eventTb);
			
			// 참여형인 경우 보상 테이블에 값을 등록합니다.
			// 참여형 외의 경우 보상 테이블의 값을 삭제합니다.
			if (eventDomain.getEventType().equals("21")) {
				insertEventRewardTb(plfCd, eventId, eventDomain);
			} else {
				deleteEventRewardTb(plfCd, eventId);
			}
    		
	        // 결과값
	        resultType = ResultType.SUCCESS;
    	} catch (Exception e) {
		    logger.error(Utils.getStacktraceString(e));
		    throw new ServiceException(e.getMessage(), e);
		}
    	
    	return resultType;
    }
    
    /**
     * 이벤트 보상 테이블에 데이터를 등록합니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 5.
     * @param plfCd plfCd.
     * @param eventId eventId.
     * @param eventDomain eventDomain.
     */
    private void insertEventRewardTb(final String plfCd, final String eventId, final EventDomain eventDomain) {
    	
    	// 값 셋팅
    	Integer ofrRnk = Integer.valueOf(eventDomain.getOfrRnk());
    	eventDomain.setPlfCd(plfCd);
    	eventDomain.setEventId(eventId);
    	List<EventRnkRewardTb> eventRnkRewardList = eventDomain.getEventRnkRewardList();
    	for (EventRnkRewardTb eventRnkRewardTb : eventRnkRewardList) {
    		BigDecimal ofrRnkTemp = eventRnkRewardTb.getOfrRnk();
    		if (ofrRnkTemp != null
    				&& ofrRnkTemp.compareTo(new BigDecimal(ofrRnk)) == 1) {
    			eventRnkRewardTb.setOfrRnk(null);
    		}
		}
    	
    	// Event Reward의 기존 데이터 정리합니다.
    	if (eventDomain.getEventTypeInfo01().compareTo(BigDecimal.ZERO) == 0) {
    		eventDao.deleteEventReward(eventDomain);
    	} else if (eventDomain.getEventTypeInfo01().compareTo(BigDecimal.ONE) == 0) {
    		EventRewardTbExample eventRewardTbExample = new EventRewardTbExample();
    		eventRewardTbExample.createCriteria().andPlfCdEqualTo(plfCd).andEventIdEqualTo(eventId);
    		eventRewardTbMapper.deleteByExample(eventRewardTbExample);
    	}
    	
    	// Event Rnk Reward의 기존 데이터 정리합니다.
    	eventDao.deleteEventRnkReward(eventDomain);
    	
    	// Event Rnk Reward의 새로운 데이터를 등록합니다.
    	for (EventRnkRewardTb eventRnkRewardTb : eventRnkRewardList) {
    		if (eventRnkRewardTb.getOfrRnk() != null
    				&& eventRnkRewardTb.getOfrRnk().compareTo(new BigDecimal(ofrRnk + 1)) == -1) {
	    		eventRnkRewardTb.setPlfCd(plfCd);
	    		eventRnkRewardTb.setEventId(eventId);
	    		eventRnkRewardTb.setEventType(eventDomain.getEventType());
	    		eventRnkRewardTb.setRewardType(eventDomain.getRewardType());
	    		eventDao.insertEventRnkReward(eventRnkRewardTb);
    		}
		}
    	
    	// Event Reward의 새로운 데이터 등록 및 수정합니다.
    	if (eventDomain.getEventTypeInfo01().compareTo(BigDecimal.ZERO) == 0) {
	    	List<EventRewardTb> eventRewardList = eventDomain.getEventRewardList();
	    	for (EventRewardTb eventReward : eventRewardList) {
	    		eventReward.setPlfCd(plfCd);
	    		eventReward.setEventId(eventId);
	    		eventReward.setEventType(eventDomain.getEventType());
	    		eventReward.setOfrRnk(eventReward.getOfrRnk());
	    		eventReward.setCurrentWinCount(BigDecimal.ZERO);
	    		eventDao.insertEventReward(eventReward);
			}
    	}
    }
    
    /**
     * 이벤트 보상 테이블에 데이터를 삭제합니다.
     * @since 	1.0
     * @author 	hiyoon - 2015. 11. 5.
     * @param plfCd plfCd.
     * @param eventId eventId.
     */
    private void deleteEventRewardTb(final String plfCd, final String eventId) {
    	
    	// Event Reward의 기존 데이터 정리합니다.
    	EventRewardTbExample eventRewardTbExample = new EventRewardTbExample();
    	eventRewardTbExample.createCriteria().andPlfCdEqualTo(plfCd).andEventIdEqualTo(eventId);
    	eventRewardTbMapper.deleteByExample(eventRewardTbExample);
    	
    	// Event Rnk Reward의 기존 데이터 정리합니다.
    	EventRnkRewardTbExample eventRnkRewardTbExample = new EventRnkRewardTbExample();
    	eventRnkRewardTbExample.createCriteria().andPlfCdEqualTo(plfCd).andEventIdEqualTo(eventId);
    	eventRnkRewardTbMapper.deleteByExample(eventRnkRewardTbExample);
    }
}
