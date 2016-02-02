package com.ns.ctcom.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ns.ctcom.core.component.support.StatusType;
import com.ns.ctcom.core.domain.EventDomain;
import com.ns.ctcom.core.domain.PlatformDomain;
import com.ns.ctcom.core.mapper.domain.EventRewardTb;
import com.ns.ctcom.core.mapper.domain.EventRnkRewardTb;
import com.ns.ctcom.core.service.EventService;
import com.ns.ctcom.core.service.PlatformService;
import com.ns.ctcom.core.service.support.JsonObjectMapperUtil;
import com.ns.ctcom.core.service.support.ResultType;
import com.ns.ctcom.core.util.ItemUtils;
import com.ns.ctcom.core.util.Utils;
import com.ns.ctcom.web.command.EventCommand;
import com.ns.ctcom.web.command.SearchMainMenuCommand;
import com.ns.ctcom.web.validator.EventValidator;

/** 
 * 이벤트 관리.
 * 
 * <p>Copyright (c) 1997-2013 Alticast, Inc. All rights reserved.
 *
 * @since	1.0
 * @author	hiyoon - 2015. 10. 16.
 */
@Controller
@RequestMapping("/event")
public class EventController {

    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /** variable eventService.*/
    @Autowired
    private EventService eventService;
    
    /** variable platformService.*/
    @Autowired
    private PlatformService platformService;
    
    /** variable eventValidator.*/
    @Autowired
    private EventValidator eventValidator;
    
    /**
     * 플랫폼 목록을 가져옵니다.
     * @return 플랫폼 목록
     */
    @ModelAttribute("plfList") 
    public final List<PlatformDomain> getPlfList() {
    	return platformService.getPlatformList();
    }
    
    /**
     * 이벤트 목록 화면 요청.
     * @param command EventCommand.
     * @param model model.
     * @return 이벤트 목록 화면
     * @throws Exception Exception.
     */
    @RequestMapping(value = "/event/list.do", method = RequestMethod.GET)
    public final String gotoEventList(@ModelAttribute("command") final EventCommand command
    		, final Model model) throws Exception {
    	if (logger.isDebugEnabled()) {
        	logger.debug("Request EventController gotoEventList." 
        			+ "command: " + command.toString());
        }
    	
    	List<EventDomain> eventList = eventService.getEventList(command.getEventDomain());
		model.addAttribute("eventList", eventList);
		model.addAttribute("useYnList", ItemUtils.getUseYnList());
     	
     	return "event/event/list";
    }
    
    /**
	 * 이벤트 삭제 요청.
	 * @param command PvtInfoTermCommand.
	 * @param model model.
	 * @return 삭제 결과.
	 * @throws Exception Exception.
	 */
    @ResponseBody
	@RequestMapping(value = "/event/delete.json", method = RequestMethod.POST)
	public final Map<String, Object> deleteEvent(@ModelAttribute("command") final EventCommand command
			, final Model model) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Request EventController deleteEvent. command : {}", command.toString());
		}
		
		ResultType result = ResultType.FAIL;
        Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			result = eventService.deleteEvent(command.getEventDomain());
		} catch (Exception e) {
	        logger.error(Utils.getStacktraceString(e));
	    }
	    resultMap.put("result", result.ordinal());
	    return resultMap;
	}
    
    /**
	 * 이벤트 목록노출, 롱배너노출, 노출순서 저장.
	 * @param command PvtInfoTermCommand.
	 * @param plfList 플랫폼 목록
	 * @param model model.
	 * @return 저장 결과.
	 * @throws Exception Exception.
	 */
    @ResponseBody
	@RequestMapping(value = "/event/save.json", method = RequestMethod.POST)
	public final Map<String, Object> saveEvent(@ModelAttribute("command") final EventCommand command
			, @ModelAttribute("plfList") final List<PlatformDomain> plfList
			, final Model model) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Request EventController saveEvent. command : {}", command.toString());
		}
		
		ResultType result = ResultType.FAIL;
        Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			result = eventService.saveEvent(command.getEventList(), plfList);
		} catch (Exception e) {
	        logger.error(Utils.getStacktraceString(e));
	    }
	    resultMap.put("result", result.ordinal());
	    return resultMap;
	}
    
    /**
	 * 이벤트 등록 화면 요청.
	 * @param command SampleCommand.
	 * @param model model.
	 * @return 이벤트 등록 화면.
	 * @throws Exception Exception.
	 */
	@RequestMapping(value = "/event/add.do", method = RequestMethod.GET)
	public final String gotoEventAdd(@ModelAttribute("command") final EventCommand command
			, final Model model) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Request EventController gotoEventAdd. command : {}", command.toString());
		}

		// 사용여부 default 노출안함으로 셋팅
    	command.setLstDescUseYn("0");
    	command.setBnrDisplayYn("0");
    	command.setContDescUseYn("0");
    	command.setDisplayYn("0");
		
		// 사용여부 목록을 셋팅.
		model.addAttribute("useYnList", ItemUtils.getUseYnList());
		model.addAttribute("eventTextImageUseTypeList", ItemUtils.getEventTextImageUseTypeList());
		model.addAttribute("eventTypeList", ItemUtils.getEventTypeList());
		model.addAttribute("hourList", ItemUtils.getHourList());
		model.addAttribute("eventDrawTypeList", ItemUtils.getEventDrawTypeList());
		model.addAttribute("eventRewardTypeList", ItemUtils.getEventRewardTypeList());
		model.addAttribute("eventOfrRnkList", ItemUtils.getEventOfrRnkList());
		model.addAttribute("searchCommand", new SearchMainMenuCommand());
		
		return "event/event/add";
	}
	
	/**
	 * 이벤트 등록.
	 * @param command PvtInfoTermCommand.
	 * @param model model
	 * @param errors errors
	 * @return result.
	 * @throws Exception Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/event/add.json", method = RequestMethod.POST)
	public final Map<String, Object> addEvent(@ModelAttribute("command") final EventCommand command
			, final Model model
			, final BindingResult errors) throws Exception {
	    if (logger.isDebugEnabled()) {
            logger.debug("Request EventController addEvent"
                    + "command: " + command.toString());
        }
        
        ResultType result = ResultType.FAIL;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	command.setStatusType(StatusType.INSERT);
        	eventValidator.validate(command, errors);
            if (errors.hasErrors()) {
                resultMap.put("errors", errors.getAllErrors());
            } else {
            	//command는 service로 직접 넘기지 않습니다. domain으로 변경 후 service로 넘깁니다.
            	EventDomain eventDomain = command.getEventDomain();
            	result = eventService.addEvent(eventDomain);
            }
        } catch (Exception e) {
            logger.error(Utils.getStacktraceString(e));
        }
        resultMap.put("result", result.ordinal());
        return resultMap;
	}

    
    /**
	 * 이벤트 등록 화면 요청.
	 * @param command SampleCommand.
	 * @param model model.
	 * @return 이벤트 등록 화면.
	 * @throws Exception Exception.
	 */
	@RequestMapping(value = "/event/edit.do", method = RequestMethod.GET)
	public final String gotoEventEdit(@ModelAttribute("command") final EventCommand command
			, final Model model) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Request EventController gotoEventEdit. command : {}", command.toString());
		}
		
		EventDomain eventDomain = command.getEventDomain();

		// 이벤트 정보
		command.setEventCommand(eventService.getEventInfo(eventDomain));
		
		// 이벤트 보상 경보
		List<EventRnkRewardTb> eventRnkRewardList = eventService.getEventRnkRewardList(eventDomain);
		if (eventRnkRewardList != null && eventRnkRewardList.size() > 0) {
			command.setOfrRnk(String.valueOf(eventRnkRewardList.size())); 
			command.setRewardType(eventRnkRewardList.get(0).getRewardType()); // 테이블 구조상 이렇게 셋팅한다.. null값 체크 안한 이유는 값은 꼭 채워져 있기 때문에...
			model.addAttribute("eventRnkRewardJsonObject", JsonObjectMapperUtil.toJsonString(eventRnkRewardList));
		} else {
			command.setOfrRnk("1"); 
		}
		
		// 이벤트 보상 추첨 경보
		List<EventRewardTb> eventRewardList = eventService.getEventRewardList(eventDomain);
		if (eventRewardList != null && eventRewardList.size() > 0) {
			model.addAttribute("eventRewardJsonObject", JsonObjectMapperUtil.toJsonString(eventRewardList));
		}
		
		// 사용여부 목록을 셋팅.
		model.addAttribute("useYnList", ItemUtils.getUseYnList());
		model.addAttribute("eventTextImageUseTypeList", ItemUtils.getEventTextImageUseTypeList());
		model.addAttribute("eventTypeList", ItemUtils.getEventTypeList());
		model.addAttribute("hourList", ItemUtils.getHourList());
		model.addAttribute("eventDrawTypeList", ItemUtils.getEventDrawTypeList());
		model.addAttribute("eventRewardTypeList", ItemUtils.getEventRewardTypeList());
		model.addAttribute("eventOfrRnkList", ItemUtils.getEventOfrRnkList());
		model.addAttribute("searchCommand", new SearchMainMenuCommand());
		
		return "event/event/edit";
	}
	
	/**
	 * 이벤트 등록.
	 * @param command PvtInfoTermCommand.
	 * @param model model
	 * @param errors errors
	 * @return result.
	 * @throws Exception Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/event/edit.json", method = RequestMethod.POST)
	public final Map<String, Object> editEvent(@ModelAttribute("command") final EventCommand command
			, final Model model
			, final BindingResult errors) throws Exception {
	    if (logger.isDebugEnabled()) {
            logger.debug("Request EventController addEvent"
                    + "command: " + command.toString());
        }
        
        ResultType result = ResultType.FAIL;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	command.setStatusType(StatusType.EDIT);
        	eventValidator.validate(command, errors);
            if (errors.hasErrors()) {
                resultMap.put("errors", errors.getAllErrors());
            } else {
            	//command는 service로 직접 넘기지 않습니다. domain으로 변경 후 service로 넘깁니다.
            	EventDomain eventDomain = command.getEventDomain();
            	result = eventService.editEvent(eventDomain);
            }
        } catch (Exception e) {
            logger.error(Utils.getStacktraceString(e));
        }
        resultMap.put("result", result.ordinal());
        return resultMap;
	}

}
